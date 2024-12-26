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
import modelo.Grupo;
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
		adaptadorGrupo = factoria.getGrupoDAO();
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
		System.out.println(cliente.getContactos().stream().map(c->c.getMensajesEnviados()).collect(Collectors.toList()));
		

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
	
	public Grupo crearGrupo(String nombre, List<ContactoIndividual> participantes) {
		if (usuarioActual.hasGroup(nombre)) {
			return null;
		}

		Group nuevoGrupo = new Group(nombre, new LinkedList<Message>(), participantes, usuarioActual);

		// Se añade el grupo al usuario actual y al resto de participantes
		usuarioActual.addGrupo(nuevoGrupo);
		usuarioActual.addGrupoAdmin(nuevoGrupo);
		participantes.stream().forEach(p -> p.addGrupo(nuevoGrupo));

		// Conexion con persistencia
		adaptadorGrupo.registrarGrupo(nuevoGrupo);

		adaptadorUsuario.modificarUsuario(usuarioActual);

		participantes.stream().forEach(p -> {
			User usuario = p.getUsuario();
			adaptadorUsuario.modificarUsuario(usuario);
		});

		return nuevoGrupo;
	}
	
	public Group modificarGrupo(Group grupo, String nombre, List<IndividualContact> participantes) {
		grupo.setNombre(nombre);

		// Creo listas para las altas y las bajas
		List<IndividualContact> nuevos = new LinkedList<>();
		List<IndividualContact> mantenidos = new LinkedList<>();

		for (IndividualContact contacto : participantes) {
			if (grupo.hasParticipante(contacto.getUsuario())) {
				mantenidos.add(contacto);
			} else {
				nuevos.add(contacto);
			}
		}

		List<IndividualContact> eliminados = new LinkedList<>(grupo.getParticipantes());
		eliminados.removeAll(participantes);

		// Le modifico el grupo si el usuario ya existia. Si es nuevo, se lo añado
		mantenidos.stream().forEach(p -> p.modificarGrupo(grupo));
		nuevos.stream().forEach(p -> p.addGrupo(grupo));

		// Elimino el grupo de los participantes que ya no lo tienen
		eliminados.stream().forEach(p -> {
			p.eliminarGrupo(grupo);
			adaptadorUsuario.modificarUsuario(p.getUsuario());
		});

		// Se le cambia al grupo la lista de participantes
		grupo.setIntegrantes(participantes);

		// Conexion con persistencia
		adaptadorGrupo.modificarGrupo(grupo);

		// Actualiza los usuarios que no estaban antes en el grupo
		nuevos.stream().map(IndividualContact::getUsuario).forEach(u -> adaptadorUsuario.modificarUsuario(u));

		return grupo;
	}
	
	public List<Group> getGruposAdminUsuarioActual() {
		// Devuelvo una lista de mis grupos. Saco el código del usuario actual.
		return usuarioActual.getGruposAdmin();
	}

	public List<Mensaje> getMensajes(Contacto contacto) {
		// Si la conversacion es conmigo mismo es suficiente con mostrar mis mensajes
		// Recuperamos todos los contactos
		
		if (contacto instanceof ContactoIndividual && !((ContactoIndividual) contacto).esUsuario(usuarioActual)) {
			return Stream
					.concat(contacto.getMensajesEnviados().stream(),
							contacto.getMensajesRecibidos(Optional.of(usuarioActual)).stream())
					.sorted().collect(Collectors.toList());
		} else {
			// Dentro de los enviados estan contenidos todos los mensajes
			return contacto.getMensajesEnviados().stream().sorted().collect(Collectors.toList());
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

	public List<Contacto> getContactosUsuarioActual() {
		if (usuarioActual == null)
			return new LinkedList<Contacto>();
		Usuario u = repoUsuarios.getUsuario(usuarioActual.getCodigo());
		return u.getContactos();
	}

	public Usuario getUsuarioActual() {
		return usuarioActual;
	}

	public Optional<Contacto> getContacto(String nombre) {
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
		} else {
			adaptadorGrupo.modificarGrupo((Grupo) contacto);
		}
		System.out.println(usuarioActual.getContactos().stream().map(c->c.getMensajesEnviados()).collect(Collectors.toList()));
		System.out.println("el usuario tiene ahora estos mensajes: " + adaptadorContactoIndividual.recuperarContacto(contacto.getCodigo()).getMensajesEnviados());
		
		
	}

	public Contacto getChatActual() {
		return chatActual;
	}

	public void setChatActual(Contacto chatActual) {
		this.chatActual = chatActual;
	}

}
