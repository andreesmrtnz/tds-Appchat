package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import persistencia.*;

/**
 * El repositorio de usuarios mantiene los objetos en memoria utilizando un mapa
 * para mejorar el rendimiento y sincronizar con una base de datos.
 */
public class RepositorioUsuarios {
    private Map<String, Usuario> usuarios; // Mapa de usuarios 
    private static RepositorioUsuarios unicaInstancia = new RepositorioUsuarios();

    private FactoriaDAO factoriaDAO;
    private IAdaptadorUsuarioDAO adaptadorUsuario;

    // Constructor privado para el patrón Singleton
    private RepositorioUsuarios() {
    	try {
			factoriaDAO = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
			adaptadorUsuario = factoriaDAO.getUsuarioDAO();
			usuarios = new HashMap<String, Usuario>();
			this.cargarCatalogo();
		} catch (DAOException eDAO) {
			eDAO.printStackTrace();
		}
    }

    private void cargarCatalogo() throws DAOException {
		List<Usuario> usuariosBD = adaptadorUsuario.recuperarTodosUsuarios();
		for (Usuario user : usuariosBD)
			usuarios.put(user.getUsuario(), user);
	}

	// Método para obtener la única instancia del repositorio
    public static RepositorioUsuarios getUnicaInstancia() {
        return unicaInstancia;
    }

    // Método para agregar un usuario al repositorio y persistirlo
    public void agregarUsuario(Usuario usuario) {
    	usuarios.put(usuario.getUsuario(), usuario);
    }

    // Método para obtener todos los usuarios en memoria
    public List<Usuario> getUsuarios() {
		ArrayList<Usuario> lista = new ArrayList<Usuario>();
		for (Usuario u : usuarios.values())
			lista.add(u);
		return lista;
	}

    // Método para recuperar un usuario por su código
    public Usuario getUsuario(int codigo) {
		return usuarios.values().stream().filter(u -> u.getCodigo() == codigo).findAny().orElse(null);
	}

	public Usuario getUsuario(String nick) {
		return usuarios.get(nick);
	}

    // Método para buscar un usuario por su teléfono
    public Optional<Usuario> getUsuarioPorTelefono(String telefono) {
        return usuarios.values().stream()
                .filter(u -> u.getTelefono().equals(telefono))
                .findAny();
    }

    // Verifica si un usuario ya está en el repositorio
    public boolean contieneUsuario(Usuario usuario) {
        return usuarios.containsValue(usuario);
    }
}

