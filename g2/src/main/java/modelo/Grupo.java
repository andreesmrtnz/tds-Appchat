package modelo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Grupo extends Contacto {
    private String imagen;
    private List<ContactoIndividual> participantes;
    private Usuario emisor;

    public Grupo(String nombre, List<ContactoIndividual> contactos, Usuario emisor, String imagen) {
		super(nombre);
		this.participantes = contactos;
		this.emisor = emisor;
		this.imagen = imagen;
	}
    
    public Grupo(String nombre, List<Mensaje> mensajes, List<ContactoIndividual> contactos, Usuario emisor,String imagen) {
		super(nombre, mensajes);
		this.participantes = contactos;
		this.emisor = emisor;
		this.imagen = imagen;
	}

	public String getImagen() {
		return imagen;
	}
	
	public Usuario getEmisor() {
		return emisor;
	}

	public List<ContactoIndividual> getParticipantes() {
		return participantes;
	}
    
	public void addIntegrante(ContactoIndividual c) {
		participantes.add(c);
	}
    
	public void setImagen(String i) {
		imagen = i;
	}
	
	public void setParticipantes(List<ContactoIndividual> contactos) {
		this.participantes = contactos;
	}
	
	@Override
	public List<Mensaje> getMensajesRecibidos(Optional<Usuario> emptyOpt) {
		return this.participantes.stream().flatMap(c -> c.getUsuario().getContactos().stream())
				.filter(c -> c instanceof Grupo).map(c -> (Grupo) c).filter(g -> this.equals(g))
				.flatMap(g -> g.getMensajesEnviados().stream()).collect(Collectors.toList());
	}
	
	public List<Mensaje> getMisMensajesGrupo(Usuario usuario) {
		return getMensajesEnviados().stream().filter(m -> m.getEmisor().getCodigo() == usuario.getCodigo())
				.collect(Collectors.toList());
	}
	
	public boolean hasParticipante(Usuario usuario) {
		return participantes.stream().anyMatch(i -> i.getUsuario().equals(usuario));
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getNombre() == null) ? 0 : getNombre().hashCode());
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
		Grupo other = (Grupo) obj;
		if (getNombre() == null) {
			if (other.getNombre() != null)
				return false;
		} else if (!getNombre().equals(other.getNombre()))
			return false;
		return true;
	}
    // Getters y Setters
}

