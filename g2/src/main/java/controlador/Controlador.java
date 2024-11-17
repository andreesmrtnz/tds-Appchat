package controlador;

import java.util.Date;

import modelo.RepositorioUsuarios;
import modelo.Usuario;
import persistencia.DAOException;
import persistencia.FactoriaDAO;
import persistencia.IAdaptadorContactoIndividualDAO;
import persistencia.IAdaptadorGrupoDAO;
import persistencia.IAdaptadorMensajeDAO;
import persistencia.IAdaptadorUsuarioDAO;

public enum Controlador {

	INSTANCE; // uso del patron Singleton para que solo haya en todo el programa una instancia
				// de controlador

	private static Controlador unicaInstancia = null;

	private IAdaptadorUsuarioDAO adaptadorUsuario;
	private IAdaptadorMensajeDAO adaptadorMensaje;
	private IAdaptadorGrupoDAO adaptadorGrupo;
	private IAdaptadorContactoIndividualDAO adaptadorContactoIndividual;
	
	private RepositorioUsuarios repoUsuarios;

	private Controlador() {
		inicializarAdaptadores();
		inicializarRepos();
	}

	void inicializarRepos() {
		repoUsuarios = RepositorioUsuarios.getUnicaInstancia();

	}

	// Métodos para manejar la lógica del chat

	public void inicializarAdaptadores() {
		FactoriaDAO factoria = null;
		try {
			factoria = FactoriaDAO.getFactoriaDAO(FactoriaDAO.DAO_TDS);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		adaptadorUsuario = factoria.getUsuarioDAO();
		//adaptadorGrupo = factoria.getGrupoDAO();
		//adaptadorContactoIndividual = factoria.getContactoIndividualDAO();
		//adaptadorMensaje = factoria.getMensajeDAO();
	}

	public boolean doLogin(String user, char[] passwd) {
        Usuario usuario = repoUsuarios.getUsuarios().stream()
                .filter(u -> u.getUsuario().equals(user) && u.getContraseña().equals(new String(passwd)))
                .findFirst()
                .orElse(null);

        if (usuario != null) {
            repoUsuarios.setUsuarioActual(usuario); // Establecer como usuario actual
            return true;
        }
        return false; // Usuario no encontrado o contraseña incorrecta
    }
	
	public boolean doRegister(String name, String telefono, char[] password, String saludo, Date fecha) {
		
		Usuario nuevoUsuario = new Usuario(name, password.toString(), telefono, fecha, telefono, saludo);
		if (!catalogoUsuarios.contains(nuevoUsuario)) {
			// Guarda la imagen en el proyecto
			Theme.saveImage(imagen, Theme.PROFILE_PHOTO_NAME, nuevoUsuario.getCodigo(), 1);

			// Conexion con la persistencia
			catalogoUsuarios.addUsuario(nuevoUsuario);
			adaptadorUsuario.registrarUsuario(nuevoUsuario);

			return iniciarSesion(nick, password);
		}
		return false;
	}
	
	
}
