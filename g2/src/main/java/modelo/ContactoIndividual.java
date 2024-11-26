package modelo;

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
    
    
    
}

