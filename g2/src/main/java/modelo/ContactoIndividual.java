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
			return contacto.getMensajes();
		} else
			return new LinkedList<>();
	}
    
    
    
}

