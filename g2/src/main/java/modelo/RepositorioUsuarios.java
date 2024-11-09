package modelo;

import java.util.ArrayList;
import java.util.List;

public class RepositorioUsuarios {
    private List<Usuario> usuarios = new ArrayList<>();
    private Usuario usuarioActual;

    // Métodos para manejar el repositorio de usuarios
    public void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
    }
}
