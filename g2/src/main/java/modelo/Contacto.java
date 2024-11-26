package modelo;

import java.util.List;

public abstract class Contacto {
    private String nombre;
    private int codigo;
    private List<Mensaje> mensajes;

	public Contacto(String nombre2) {
		this.nombre = nombre2;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public List<Mensaje> getMensajes() {
		return mensajes;
	}

	public void setMensajes(List<Mensaje> mensajes) {
		this.mensajes = mensajes;
	}

	
    
    // Getters y Setters
}

