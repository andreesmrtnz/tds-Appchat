package modelo;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public abstract class Contacto {
    private String nombre;
    private int codigo;
    private List<Mensaje> mensajes;

    public Contacto(String nombre) {
		this(nombre, new LinkedList<>());
	}
    
	public Contacto(String nombre, List<Mensaje> mensajes) {
		this.nombre = nombre;
		this.mensajes = mensajes;
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

	public List<Mensaje> getMensajesEnviados() {
		return mensajes;
	}

	public void setMensajes(List<Mensaje> mensajes) {
		this.mensajes = mensajes;
	}

	public abstract List<Mensaje> getMensajesRecibidos(Optional<Usuario> usuario);
	
	public void enviarMensaje(Mensaje message) {
		mensajes.add(message);
	}
    // Getters y Setters
	@Override
	public String toString() {
		return nombre;
	}
}

