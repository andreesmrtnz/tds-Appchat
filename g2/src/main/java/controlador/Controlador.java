package controlador;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

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
	
	private Usuario usuarioActual;

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
			factoria = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		adaptadorUsuario = factoria.getUsuarioDAO();
		//adaptadorGrupo = factoria.getGrupoDAO();
		//adaptadorContactoIndividual = factoria.getContactoIndividualDAO();
		//adaptadorMensaje = factoria.getMensajeDAO();
	}

	public boolean doLogin(String telefono, String passwd) {
		if (telefono.isEmpty() || passwd.isEmpty())
			return false;

		/*Usuario cliente = repoUsuarios.getUsuario(telefono);
		if (cliente == null)
			return false;*/
		 Optional<Usuario> clienteOpt = repoUsuarios.getUsuarioPorTelefono(telefono);
		    if (clienteOpt.isEmpty()) {
		        System.out.println("Usuario no encontrado para el teléfono: " + telefono);
		        return false;
		    }

		    Usuario cliente = clienteOpt.get();

		// Si la password esta bien inicia sesion
		if (cliente.getContraseña().equals(passwd)) {
			usuarioActual = cliente;

			
			return true;
		}
		return false;
    }
	
	public boolean doRegister(String name, String telefono, String password, String saludo, Date fecha, String image) {
		
		Usuario nuevoUsuario = new Usuario(name, password.toString(), telefono, fecha, image, saludo);
		if (!repoUsuarios.contieneUsuario(nuevoUsuario)) {
			// Guarda la imagen en el proyecto

			// Conexion con la persistencia
			repoUsuarios.agregarUsuario(nuevoUsuario);
			adaptadorUsuario.registrarUsuario(nuevoUsuario);

			return doLogin(telefono, password);
		}
		return false;
	}

	public Usuario getUsuarioActual() {
		return usuarioActual;
	}

	
	
	
	
}
