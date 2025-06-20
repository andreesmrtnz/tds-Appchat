package controlador;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JOptionPane;

import com.itextpdf.text.DocumentException;

import modelo.Contacto;
import modelo.ContactoIndividual;
import modelo.Descuento;
import modelo.DescuentoFecha;
import modelo.DescuentoMensaje;
import modelo.GeneradorPDF;
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
	    // Buscar si ya existe un contacto con este número
	    Optional<ContactoIndividual> contactoExistente = usuarioActual.getContactos().stream()
	            .filter(c -> c instanceof ContactoIndividual)
	            .map(c -> (ContactoIndividual) c)
	            .filter(c -> c.getMovil().equals(telefono))
	            .findFirst();

	    if (contactoExistente.isPresent()) {
	        ContactoIndividual contacto = contactoExistente.get();

	        // Preguntar al usuario si desea sobrescribir el contacto
	        int respuesta = JOptionPane.showConfirmDialog(
	                null,
	                "El número " + telefono + " ya está registrado como " + contacto.getNombre() +
	                        ". ¿Deseas actualizar el nombre a " + nombre + "?",
	                "Sobrescribir Contacto",
	                JOptionPane.YES_NO_OPTION
	        );

	        if (respuesta == JOptionPane.YES_OPTION) {
	            // Actualizar el nombre del contacto existente
	            contacto.setNombre(nombre);
	            adaptadorContactoIndividual.modificarContacto(contacto);
	            adaptadorUsuario.modificarUsuario(usuarioActual);
	            notificarObservers();
	            return contacto; // Devuelve el contacto actualizado
	        } else {
	            return null; // El usuario decidió no sobrescribir
	        }
	    }

	    // Si no existe, agregar un nuevo contacto
	    Optional<Usuario> usuarioOpt = repoUsuarios.getUsuarioPorTelefono(telefono);

	    if (usuarioOpt.isPresent()) {
	        ContactoIndividual nuevoContacto = new ContactoIndividual(nombre, usuarioOpt.get(), telefono);
	        usuarioActual.addContacto(nuevoContacto);

	        adaptadorContactoIndividual.registrarContacto(nuevoContacto);
	        adaptadorUsuario.modificarUsuario(usuarioActual);

	        contactos.add(nuevoContacto);
	        notificarObservers();
	        return nuevoContacto; // Devuelve el nuevo contacto
	    }

	    return null; // No se pudo agregar el contacto
	}

	
	public Grupo crearGrupo(String nombre, List<ContactoIndividual> participantes) {
		if (usuarioActual.hasGroup(nombre)) {
			return null;
		}

		Grupo nuevoGrupo = new Grupo(nombre, new LinkedList<Mensaje>(), participantes, usuarioActual, "");

		// Se añade el grupo al usuario actual y al resto de participantes
		usuarioActual.addGrupo(nuevoGrupo);
		usuarioActual.addGrupoEmisor(nuevoGrupo);
		participantes.stream().forEach(p -> p.addGrupo(nuevoGrupo));

		// Conexion con persistencia
		adaptadorGrupo.registrarGrupo(nuevoGrupo);

		adaptadorUsuario.modificarUsuario(usuarioActual);

		participantes.stream().forEach(p -> {
			Usuario usuario = p.getUsuario();
			adaptadorUsuario.modificarUsuario(usuario);
		});
		
		contactos.add(nuevoGrupo);
		notificarObservers();

		return nuevoGrupo;
	}
	
	public Grupo modificarGrupo(Grupo grupo, String nombre, List<ContactoIndividual> participantes) {
		grupo.setNombre(nombre);

		// Creo listas para las altas y las bajas
		List<ContactoIndividual> nuevos = new LinkedList<>();
		List<ContactoIndividual> mantenidos = new LinkedList<>();

		for (ContactoIndividual contacto : participantes) {
			if (grupo.hasParticipante(contacto.getUsuario())) {
				mantenidos.add(contacto);
			} else {
				nuevos.add(contacto);
			}
		}

		List<ContactoIndividual> eliminados = new LinkedList<>(grupo.getParticipantes());
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
		grupo.setParticipantes(participantes);

		// Conexion con persistencia
		adaptadorGrupo.modificarGrupo(grupo);

		// Actualiza los usuarios que no estaban antes en el grupo
		nuevos.stream().map(ContactoIndividual::getUsuario).forEach(u -> adaptadorUsuario.modificarUsuario(u));

		return grupo;
	}
	
	public List<Grupo> getGruposEmisorUsuarioActual() {
		// Devuelvo una lista de mis grupos. Saco el código del usuario actual.
		return usuarioActual.getGruposEmisor();
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
	    // Crear el mensaje con los detalles proporcionados
	    Mensaje mensaje = new Mensaje(text, LocalDateTime.now(), usuarioActual, contacto);

	    // Si nunca he hablado con el contacto, es individual, y el receptor no tiene agregado al usuario actual
	    if (getUltimoMensaje(contacto) == null && contacto instanceof ContactoIndividual) {
	        ContactoIndividual c = (ContactoIndividual) contacto;
	        Usuario receptor = c.getUsuario();

	        // Comprobar si el receptor tiene al usuario actual en su lista de contactos
	        boolean usuarioActualNoAgregado = receptor.getContactos().stream()
	            .noneMatch(contactoExistente -> 
	                contactoExistente instanceof ContactoIndividual &&
	                ((ContactoIndividual) contactoExistente).getUsuario().equals(usuarioActual)
	            );

	        if (usuarioActualNoAgregado) {
	            // Crear un nuevo contacto para el receptor con el nombre igual al número de teléfono del usuario actual
	            ContactoIndividual nuevoContacto = new ContactoIndividual(usuarioActual.getTelefono(), usuarioActual, usuarioActual.getTelefono());
	            receptor.addContacto(nuevoContacto);
	            // Registrar el nuevo contacto en el adaptador
	            adaptadorContactoIndividual.registrarContacto(nuevoContacto);
	            adaptadorUsuario.modificarUsuario(receptor);
	            
	        }
	    }

	    // Enviar el mensaje al contacto
	    contacto.enviarMensaje(mensaje);

	    // Registrar el mensaje en el adaptador de mensajes
	    adaptadorMensaje.registrarMensaje(mensaje);

	    // Actualizar la información del contacto o grupo en sus respectivos adaptadores
	    if (contacto instanceof ContactoIndividual) {
	        adaptadorContactoIndividual.modificarContacto((ContactoIndividual) contacto);
	    } else {
	        adaptadorGrupo.modificarGrupo((Grupo) contacto);
	    }
	}


	
	public List<Mensaje> buscarMensajes(String contacto, String telefono, String text) {
	    // Recupero los mensajes que he enviado
	    List<Mensaje> mensajes = Controlador.INSTANCE.getContactosUsuarioActual().stream()
	            .flatMap(c -> Controlador.INSTANCE.getMensajes(c).stream())
	            .distinct()
	            .collect(Collectors.toList());

	    Optional<Contacto> c = getContacto(contacto);
	    ContactoIndividual contact = (ContactoIndividual) c.orElse(null);

	    return mensajes.stream()
	            // Filtro por contacto
	            .filter(m -> contacto.equals("Todos") || 
	                         m.getReceptor().equals(contact) || 
	                         m.getEmisor().equals(contact.getUsuario()))
	            // Filtro por teléfono
	            .filter(m -> telefono.isEmpty() || 
	                         m.getEmisor().getTelefono().equals(telefono) || 
	                         (m.getReceptor() instanceof ContactoIndividual &&
	                          ((ContactoIndividual) m.getReceptor()).getMovil().equals(telefono)))
	            // Filtro por texto
	            .filter(m -> text.isEmpty() || m.getTexto().contains(text))
	            .distinct()
	            .collect(Collectors.toList());
	}


	public Contacto getChatActual() {
		return chatActual;
	}

	public void setChatActual(Contacto chatActual) {
		this.chatActual = chatActual;
	}

	public boolean activarPremium() {
		if (! usuarioActual.isPremium()) {
			usuarioActual.setPremium(true);
			adaptadorUsuario.modificarUsuario(usuarioActual);
			return true;
		}else {
			return false;
		}
	}

	public void crearPDFconversacion(Contacto contacto, String ruta, File archivo) {
	    GeneradorPDF generador = new GeneradorPDF();

	    // Obtener los mensajes ordenados
	    List<Mensaje> mensajes = getMensajes(contacto);

	    // Convertir los mensajes a un formato adecuado para el PDF
	    List<String[]> mensajesFormateados = mensajes.stream()
	            .map(m -> new String[]{
	                    m.getHora().toString(),
	                    m.getTexto(),
	                    m.getEmisor().getUsuario()
	            })
	            .collect(Collectors.toList());

	    try {
	        // Generar el PDF
	        generador.generarPDF(contacto.getNombre(), mensajesFormateados, ruta);
	    } catch (IOException | DocumentException e) {
	        e.printStackTrace();
	    }
	}


	public boolean puedeExportarPDF() {
		
		return usuarioActual.isPremium();
	}

	public void cambiarImagenUsuario(String urlText) {
		usuarioActual.setImagen(urlText);
		adaptadorUsuario.modificarUsuario(usuarioActual);
		
	}

	public Optional<Contacto> mensajeCon(Mensaje mensajeSeleccionado) {
	    // Obtener el usuario actual

	    // Determinar si el emisor o receptor es diferente del usuario actual
	    if (!mensajeSeleccionado.getEmisor().equals(usuarioActual)) {
	        return getContacto(mensajeSeleccionado.getEmisor().getUsuario());
	    } else if (!mensajeSeleccionado.getReceptor().equals(usuarioActual)) {
	        return Optional.of(mensajeSeleccionado.getReceptor());
	    }

	    // Si tanto el emisor como el receptor son el usuario actual, no hay contacto válido
	    return Optional.empty();
	}


	public double getPrecio() {
	    return usuarioActual.getPrecio();
	}
	
	public Optional<Descuento> getDescuento() {
		return usuarioActual.getDescuento();
	}


	

}
