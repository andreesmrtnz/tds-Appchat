package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Usuario {
	private int codigo;
    private String usuario;
    private String contraseña;
    private String telefono;
    private LocalDate fechaNacimiento;
    private String imagen;
    private String saludo;
    private Descuento descuento;
    private List<Mensaje> enviados = new ArrayList<>();
    private List<Mensaje> recibidos = new ArrayList<>();
    private List<ContactoIndividual> contactos = new ArrayList<>();

    
    
    public Usuario(String usuario, String contraseña, String telefono, LocalDate fecha, String imagen,
			String saludo) {
		super();
		this.usuario = usuario;
		this.contraseña = contraseña;
		this.telefono = telefono;
		this.fechaNacimiento = fecha;
		this.imagen = imagen;
		this.saludo = saludo;
	}

	public ContactoIndividual getContactoIndividual(Usuario otroUsuario) {
        for (Contacto contacto : contactos) {
            if (contacto instanceof ContactoIndividual && 
                ((ContactoIndividual) contacto).getUsuario().equals(otroUsuario)) {
                return (ContactoIndividual) contacto;
            }
        }
        return null;
    }

    public List<Mensaje> getChatMensajes(Usuario otroUsuario) {
        List<Mensaje> chatMensajes = new ArrayList<>();
        for (Mensaje mensaje : enviados) {
            if (mensaje.getReceptor().equals(otroUsuario)) {
                chatMensajes.add(mensaje);
            }
        }
        for (Mensaje mensaje : recibidos) {
            if (mensaje.getEmisor().equals(otroUsuario)) {
                chatMensajes.add(mensaje);
            }
        }
        // Ordenar mensajes por fecha y hora
        chatMensajes.sort((m1, m2) -> {
            if (m1.getFecha().equals(m2.getFecha())) {
                return m1.getHora().compareTo(m2.getHora());
            }
            return m1.getFecha().compareTo(m2.getFecha());
        });
        return chatMensajes;
    }

	public String getUsuario() {
		return usuario;
	}

	public String getContraseña() {
		return contraseña;
	}

	public String getTelefono() {
		return telefono;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public String getImagen() {
		return imagen;
	}

	public String getSaludo() {
		return saludo;
	}

	public Descuento getDescuento() {
		return descuento;
	}

	public List<Mensaje> getEnviados() {
		return enviados;
	}

	public List<Mensaje> getRecibidos() {
		return recibidos;
	}

	public List<ContactoIndividual> getContactos() {
		return contactos;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	
    
    // Getters y Setters
}

