package modelo;

import java.util.List;

public class Grupo extends Contacto {
    private String imagen;
    private List<Usuario> usuarios;

    public Grupo(String nombre, String imagen) {
        this.setNombre(nombre);
        this.imagen = imagen;
    }

	public String getImagen() {
		return imagen;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}
    
    
    // Getters y Setters
}

