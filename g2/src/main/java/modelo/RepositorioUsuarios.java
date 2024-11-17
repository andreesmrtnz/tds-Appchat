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
    private Map<Integer, Usuario> usuarios; // Mapa de usuarios (clave: código del usuario)
    private Usuario usuarioActual;
    private static RepositorioUsuarios unicaInstancia = new RepositorioUsuarios();

    private FactoriaDAO factoriaDAO;
    private IAdaptadorUsuarioDAO adaptadorUsuario;

    // Constructor privado para el patrón Singleton
    private RepositorioUsuarios() {
        adaptadorUsuario = factoriaDAO.getUsuarioDAO();
		usuarios = new HashMap<>();
		cargarUsuarios();
    }

    // Método para obtener la única instancia del repositorio
    public static RepositorioUsuarios getUnicaInstancia() {
        return unicaInstancia;
    }

    // Método para agregar un usuario al repositorio y persistirlo
    public void agregarUsuario(Usuario usuario) {
        adaptadorUsuario.registrarUsuario(usuario); // Persistir el usuario
		usuarios.put(usuario.getCodigo(), usuario); // Añadir al mapa en memoria
    }

    // Método para obtener todos los usuarios en memoria
    public List<Usuario> getUsuarios() {
        return new ArrayList<>(usuarios.values());
    }

    // Método para recuperar un usuario por su código
    public Usuario getUsuario(int codigo) {
        return usuarios.get(codigo);
    }

    // Método para buscar un usuario por su teléfono
    public Optional<Usuario> getUsuarioPorTelefono(String telefono) {
        return usuarios.values().stream()
                .filter(u -> u.getTelefono().equals(telefono))
                .findAny();
    }


    // Métodos para manejar el usuario actual
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    // Verifica si un usuario ya está en el repositorio
    public boolean contieneUsuario(Usuario usuario) {
        return usuarios.containsValue(usuario);
    }
}

