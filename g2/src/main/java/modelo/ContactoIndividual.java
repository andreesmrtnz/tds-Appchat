package modelo;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ContactoIndividual extends Contacto {
    private Usuario usuario;
    private String movil;

    public ContactoIndividual(String nombre, Usuario usuario, String movil) {
        super(nombre);
    	this.usuario = usuario;
        this.movil = movil;
    }
    
    public ContactoIndividual(String nombre, LinkedList<Mensaje> mensajes, String movil, Usuario usuario) {
		super(nombre, mensajes);
		this.movil = movil;
		this.usuario = usuario;
	}

    public Usuario getUsuario() {
        return usuario;
    }
    

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getMovil() {
		return movil;
	}

	public void setMovil(String movil) {
		this.movil = movil;
	}

	public boolean esUsuario(Usuario otroUsuario) {
		return usuario.equals(otroUsuario);
	}
	
	public ContactoIndividual getContacto(Usuario usuario) {
		return this.usuario.getContactos().stream().filter(c -> c instanceof ContactoIndividual)
				.map(c -> (ContactoIndividual) c).filter(c -> c.getUsuario().equals(usuario)).findAny().orElse(null);
	}

	@Override
	public List<Mensaje> getMensajesRecibidos(Optional<Usuario> usuario) {
		ContactoIndividual contacto = getContacto(usuario.orElse(null));
		if (contacto != null) {
			return contacto.getMensajesEnviados();
		} else
			return new LinkedList<>();
	}
    
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Integer.parseInt(movil);
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContactoIndividual other = (ContactoIndividual) obj;
		if (movil != other.movil)
			return false;
		return true;
	}
    
    
}

