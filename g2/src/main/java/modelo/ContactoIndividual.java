package modelo;

public class ContactoIndividual extends Contacto {
    private Usuario usuario;

    public ContactoIndividual(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}

