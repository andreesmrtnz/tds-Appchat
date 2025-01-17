package modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Usuario {
	private static final double PRECIO = 9.99;
	
	private int codigo;
	private String usuario;
	private String contraseña;
	private String telefono;
	private Date fechaNacimiento;
	private String imagen;
	private String saludo;
	private boolean premium = false;
	private List<Grupo> gruposEmisor = new LinkedList<>();
	private List<Contacto> contactos = new ArrayList<>();
	private Optional<Descuento> descuento;

	public Usuario(String usuario, String contraseña, String telefono, Date fechaNacimiento2, String imagen,
			String saludo) {
		super();
		this.usuario = usuario;
		this.contraseña = contraseña;
		this.telefono = telefono;
		this.fechaNacimiento = fechaNacimiento2;
		this.imagen = imagen;
		this.saludo = saludo;
	}

	public ContactoIndividual getContactoIndividual(Usuario otroUsuario) {
		for (Contacto contacto : contactos) {
			if (contacto instanceof ContactoIndividual
					&& ((ContactoIndividual) contacto).getUsuario().equals(otroUsuario)) {
				return (ContactoIndividual) contacto;
			}
		}
		return null;
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

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public String getImagen() {
		return imagen;
	}

	public String getSaludo() {
		return saludo;
	}

	public int getNumeroMensajesUltimoMes() {
	    LocalDate fechaActual = LocalDate.now();
	    LocalDate primerDiaMes = fechaActual.withDayOfMonth(1); // Primer día del mes actual
	    LocalDate ultimoDiaMes = fechaActual.withDayOfMonth(fechaActual.lengthOfMonth()); // Último día del mes actual

	    return (int) contactos.stream()
	            .flatMap(c -> c.getMensajesEnviados().stream())
	            .map(m -> m.getHora().toLocalDate())
	            .filter(h -> !h.isBefore(primerDiaMes) && !h.isAfter(ultimoDiaMes))
	            .count();
	}
	
	public double getPrecio(GestorDescuentos gestorDescuentos) {
	    Descuento descuentoAplicable = gestorDescuentos.obtenerDescuentoAplicable(this);
	    if (descuentoAplicable != null) {
	        return descuentoAplicable.getDescuento(PRECIO);
	    } else {
	        return PRECIO;
	    }
	}

	
	public void setDescuento(Descuento descuento) {
		this.descuento = Optional.ofNullable(descuento);
	}

	public List<Contacto> getContactos() {
		return contactos;
	}

	public List<String> getContactosNombre() {
		List<String> nombres = new ArrayList<>();
	for (Contacto contacto: contactos) {
    	nombres.add(contacto.getNombre());
    }
		return nombres;
	}

	public int getCodigo() {
		return codigo;
	}
	
	public List<Grupo> getGruposEmisor() {
		return gruposEmisor;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public void addContacto(ContactoIndividual contacto) {
		contactos.add(contacto);
	}

	public boolean tieneContacto(String telefono) {
		return contactos.stream().anyMatch(c -> c instanceof ContactoIndividual && c.getNombre().equals(telefono));
	}
	
	public List<Grupo> getGrupos() {
		return contactos.stream().filter(c -> c instanceof Grupo).map(c -> (Grupo) c).collect(Collectors.toList());
	}
	
	public List<ContactoIndividual> getContactosIndividuales() {
		return contactos.stream().filter(c -> c instanceof ContactoIndividual).map(c -> (ContactoIndividual) c)
				.collect(Collectors.toList());
	}
	
	public void addGrupoEmisor(Grupo g) {
		gruposEmisor.add(g);
	}


	public void addGrupo(Grupo g) {
		contactos.add(g);
	}
	
	public void modificarGrupo(Grupo g) {
		List<Grupo> grupos = contactos.stream().filter(c -> c instanceof Grupo).map(c -> (Grupo) c)
				.collect(Collectors.toList());
		for (int i = 0; i < grupos.size(); i++)
			if (grupos.get(i).getCodigo() == g.getCodigo())
				grupos.set(i, g);
	}

	public void removeContact(Contacto c) {
		contactos.remove(c);
		if (c instanceof Grupo && ((Grupo) c).getEmisor().getCodigo() == this.codigo) {
			gruposEmisor.remove((Grupo) c);
		}
	}
	
	public boolean hasGroup(String nombreGrupo) {
		return contactos.stream().anyMatch(c -> c instanceof Grupo && c.getNombre().equals(nombreGrupo));
	}

	public boolean hasIndividualContact(String nomContacto) {
		return contactos.stream().anyMatch(c -> c instanceof ContactoIndividual && c.getNombre().equals(nomContacto));
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public void setSaludo(String saludo) {
		this.saludo = saludo;
	}

	public void setGruposEmisor(List<Grupo> gruposEmisor) {
		this.gruposEmisor = gruposEmisor;
	}

	public void setContactos(List<Contacto> contactos) {
		this.contactos = contactos;
	}

	@Override
	public String toString() {
		return "User [codigo=" + codigo + ", nombre=" + usuario + ", fechaNacimiento="
				+ fechaNacimiento + ", numTelefono=" + telefono + ", password=" + contraseña
				 + ", saludo=" + saludo + ", contactos=" + contactos + ", rol=" + descuento + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		result = prime * result + Integer.parseInt(telefono);
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
		Usuario other = (Usuario) obj;
		return usuario.equals(other.usuario) || telefono == other.telefono;
	}
	// Getters y Setters

	public boolean isPremium() {
		return premium;
	}

	public void setPremium(boolean premium) {
		this.premium = premium;
	}
}
