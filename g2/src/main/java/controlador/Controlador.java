package controlador;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import modelo.Contacto;
import modelo.ContactoIndividual;
import modelo.Mensaje;
import modelo.RepositorioUsuarios;
import modelo.Usuario;
import persistencia.DAOException;
import persistencia.FactoriaDAO;
import persistencia.IAdaptadorContactoIndividualDAO;
import persistencia.IAdaptadorGrupoDAO;
import persistencia.IAdaptadorMensajeDAO;
import persistencia.IAdaptadorUsuarioDAO;
import vista.Observer;

public enum Controlador {

	INSTANCE; // uso del patron Singleton para que solo haya en todo el programa una instancia
				// de controlador

	private static Controlador unicaInstancia = null;

	private IAdaptadorUsuarioDAO adaptadorUsuario;
	private IAdaptadorMensajeDAO adaptadorMensaje;
	private IAdaptadorGrupoDAO adaptadorGrupo;
	private IAdaptadorContactoIndividualDAO adaptadorContactoIndividual;

	private RepositorioUsuarios repoUsuarios;

	private Usuario usuarioActual;

	// Chat seleccionado
	private Contacto chatActual;

	private List<Observer> observers = new ArrayList<>();
	private List<Contacto> contactos = new ArrayList<>();

	private Controlador() {
		inicializarAdaptadores();
		inicializarRepos();
	}

	void inicializarRepos() {
		repoUsuarios = RepositorioUsuarios.getUnicaInstancia();

	}

	// Métodos para manejar la lógica del chat

	public void inicializarAdaptadores() {
		FactoriaDAO factoria = null;
		try {
			factoria = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		adaptadorUsuario = factoria.getUsuarioDAO();
		// adaptadorGrupo = factoria.getGrupoDAO();
		adaptadorContactoIndividual = factoria.getContactoIndividualDAO();
		adaptadorMensaje = factoria.getMensajeDAO();
	}

	public boolean doLogin(String telefono, String passwd) {
		if (telefono.isEmpty() || passwd.isEmpty())
			return false;

		/*
		 * Usuario cliente = repoUsuarios.getUsuario(telefono); if (cliente == null)
		 * return false;
		 */
		Optional<Usuario> clienteOpt = repoUsuarios.getUsuarioPorTelefono(telefono);
		if (clienteOpt.isEmpty()) {
			System.out.println("Usuario no encontrado para el teléfono: " + telefono);
			return false;
		}

		Usuario cliente = clienteOpt.get();

		// Si la password esta bien inicia sesion
		if (cliente.getContraseña().equals(passwd)) {
			usuarioActual = cliente;
			if (usuarioActual.getContactos() != null) {
				contactos.addAll(usuarioActual.getContactos());
			}
			
			notificarObservers();

			return true;
		}
		return false;
	}

	public boolean doRegister(String name, String telefono, String password, String saludo, Date fecha, String image) {

		Usuario nuevoUsuario = new Usuario(name, password.toString(), telefono, fecha, image, saludo);
		if (!repoUsuarios.contieneUsuario(nuevoUsuario)) {
			// Guarda la imagen en el proyecto

			// Conexion con la persistencia
			repoUsuarios.agregarUsuario(nuevoUsuario);
			adaptadorUsuario.registrarUsuario(nuevoUsuario);

			return doLogin(telefono, password);
		}
		return false;
	}

	public ContactoIndividual doAgregar(String nombre, String telefono) {
		if (!usuarioActual.tieneContacto(nombre)) {
			Optional<Usuario> usuarioOpt = repoUsuarios.getUsuarioPorTelefono(telefono);

			if (usuarioOpt.isPresent()) {
				ContactoIndividual nuevoContacto = new ContactoIndividual(nombre, usuarioOpt.get(), telefono);
				usuarioActual.addContacto(nuevoContacto);

				adaptadorContactoIndividual.registrarContacto(nuevoContacto);

				adaptadorUsuario.modificarUsuario(usuarioActual);

				contactos.add(nuevoContacto);
				notificarObservers();
				return nuevoContacto;
			}
		}
		return null;
	}

	public List<Mensaje> getMensajes(Contacto contacto) {
		// Si la conversacion es conmigo mismo es suficiente con mostrar mis mensajes
		if (contacto instanceof ContactoIndividual && !((ContactoIndividual) contacto).esUsuario(usuarioActual)) {
			return Stream
					.concat(contacto.getMensajes().stream(),
							contacto.getMensajesRecibidos(Optional.of(usuarioActual)).stream())
					.sorted().collect(Collectors.toList());
		} else {
			// Dentro de los enviados estan contenidos todos los mensajes
			return contacto.getMensajes().stream().sorted().collect(Collectors.toList());
		}
	}

	// Devuelvo el último mensaje con ese contacto.
	public Mensaje getUltimoMensaje(Contacto contacto) {
		List<Mensaje> mensajes = getMensajes(contacto);
		if (mensajes.isEmpty())
			return null;
		return mensajes.get(mensajes.size() - 1);
	}

	public List<String> getContactosNombre() {
		return usuarioActual.getContactosNombre();
	}

	public List<ContactoIndividual> getContactosUsuarioActual() {
		if (usuarioActual == null)
			return new LinkedList<ContactoIndividual>();
		Usuario u = repoUsuarios.getUsuario(usuarioActual.getCodigo());
		return u.getContactos();
	}

	public Usuario getUsuarioActual() {
		return usuarioActual;
	}

	public Optional<ContactoIndividual> getContacto(String nombre) {
		return getContactosUsuarioActual().stream().filter(c -> c.getNombre().equals(nombre)).findAny();
	}

	// Método para agregar observadores
	public void agregarObserver(Observer observer) {
		observers.add(observer);
	}

	// Método para notificar a los observadores
	private void notificarObservers() {
		for (Observer observer : observers) {
			observer.actualizar();
		}
	}

	public void enviarMensaje(Contacto contacto, String text) {
		Mensaje mensaje = new Mensaje(text, LocalDateTime.now(), usuarioActual, contacto);
		contacto.enviarMensaje(mensaje);

		adaptadorMensaje.registrarMensaje(mensaje);

		if (contacto instanceof ContactoIndividual) {
			adaptadorContactoIndividual.modificarContacto((ContactoIndividual) contacto);
		} //else {
			//adaptadorGrupo.modificarGrupo((Grupo) contacto);
		//}
		
	}

	public Contacto getChatActual() {
		return chatActual;
	}

	public void setChatActual(Contacto chatActual) {
		this.chatActual = chatActual;
	}

}
