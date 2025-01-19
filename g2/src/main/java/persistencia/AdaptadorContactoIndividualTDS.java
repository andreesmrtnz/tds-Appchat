package persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import beans.Entidad;
import beans.Propiedad;

import modelo.ContactoIndividual;
import modelo.Mensaje;
import modelo.Usuario;

public class AdaptadorContactoIndividualTDS implements IAdaptadorContactoIndividualDAO {

	private static ServicioPersistencia servPersistencia;
	private static AdaptadorContactoIndividualTDS unicaInstancia = null;

	public static AdaptadorContactoIndividualTDS getUnicaInstancia() { // patrón Singleton
		if (unicaInstancia == null) {
			return new AdaptadorContactoIndividualTDS();
		} else {
			return unicaInstancia;
		}
	}

	private AdaptadorContactoIndividualTDS() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	@Override
	public void registrarContacto(ContactoIndividual contacto) {
		// Intentar recuperar la entidad
		Entidad eContacto = servPersistencia.recuperarEntidad(contacto.getCodigo());
		if (eContacto != null) {
			return; // La entidad ya existe, no registrar nuevamente
		}
		// Registramos primero los atributos que son objetos
		// Registrar los mensajes del contacto
		registrarSiNoExistenMensajes(contacto.getMensajesEnviados());

		// Registramos al usuario correspondiente al contacto si no existe.
		registrarSiNoExisteUsuario(contacto.getUsuario());

		// Crear nueva entidad contacto
		eContacto = new Entidad();
		eContacto.setNombre("contacto");
		eContacto.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(new Propiedad("nombre", contacto.getNombre()),
				new Propiedad("movil", String.valueOf(contacto.getMovil())),
				new Propiedad("mensajes", obtenerCodigosMensajesRecibidos(contacto.getMensajesEnviados()) ),
				new Propiedad("usuario", String.valueOf(contacto.getUsuario().getCodigo())))));

		// Registrar entidad en persistencia
		eContacto = servPersistencia.registrarEntidad(eContacto);

		// Validar que la entidad fue registrada correctamente
		if (eContacto == null) {
			throw new IllegalStateException("Error al registrar la entidad 'contacto'.");
		}

		// Asignar identificador único al contacto
		contacto.setCodigo(eContacto.getId());

		// Guardar en el pool
		PoolDAO.getUnicaInstancia().addObjeto(contacto.getCodigo(), contacto);
		
	}

	@Override
	public void borrarContacto(ContactoIndividual contacto) {
		Entidad eContacto = servPersistencia.recuperarEntidad(contacto.getCodigo());
		servPersistencia.borrarEntidad(eContacto);

		// Si está en el pool, borramos del pool
		if (PoolDAO.getUnicaInstancia().contiene(contacto.getCodigo()))
			PoolDAO.getUnicaInstancia().removeObjeto(contacto.getCodigo());
	}

	@Override
	public void modificarContacto(ContactoIndividual contacto) {
	    Entidad eContact = servPersistencia.recuperarEntidad(contacto.getCodigo());

	    for (Propiedad prop : eContact.getPropiedades()) {
	        switch (prop.getNombre()) {
	            case "nombre":
	                prop.setValor(contacto.getNombre());
	                break;
	            case "movil":
	                prop.setValor(String.valueOf(contacto.getMovil()));
	                break;
	            case "mensajes":
	                prop.setValor(obtenerCodigosMensajesRecibidos(contacto.getMensajesEnviados()));
	                break;
	            case "usuario":
	                prop.setValor(String.valueOf(contacto.getUsuario().getCodigo()));
	                break;
	        }
	        servPersistencia.modificarPropiedad(prop);
	    }
	    
	}

	@Override
	public ContactoIndividual recuperarContacto(int codigo) {
		// Si la entidad esta en el pool la devuelve directamente
		if (PoolDAO.getUnicaInstancia().contiene(codigo))
			return (ContactoIndividual) PoolDAO.getUnicaInstancia().getObjeto(codigo);

		// Sino, la recupera de la base de datos
		// Recuperamos la entidad
		Entidad eContacto = servPersistencia.recuperarEntidad(codigo);

		// recuperar propiedades que no son objetos
		String nombre = servPersistencia.recuperarPropiedadEntidad(eContacto, "nombre");

		String movil = servPersistencia.recuperarPropiedadEntidad(eContacto, "movil");

		ContactoIndividual contacto = new ContactoIndividual(nombre, new LinkedList<Mensaje>(), movil, null);
		contacto.setCodigo(codigo);

		// Metemos al contacto en el pool antes de llamar a otros adaptadores
		PoolDAO.getUnicaInstancia().addObjeto(codigo, contacto);

		// Mensajes que el contacto tiene
		List<Mensaje> mensajes = obtenerMensajesDesdeCodigos(
				servPersistencia.recuperarPropiedadEntidad(eContacto, "mensajes"));
		for (Mensaje m : mensajes)
			contacto.enviarMensaje(m);

		// Obtener usuario del contacto
		contacto.setUsuario(
				obtenerUsuarioDesdeCodigo(servPersistencia.recuperarPropiedadEntidad(eContacto, "usuario")));

		// Devolvemos el objeto contacto
		return contacto;
	}

	@Override
	public List<ContactoIndividual> recuperarTodosContactos() {
		List<ContactoIndividual> contactos = new LinkedList<>();
		List<Entidad> entidades = servPersistencia.recuperarEntidades("contacto");

		for (Entidad eContacto : entidades) {
			contactos.add(recuperarContacto(eContacto.getId()));
		}
		return contactos;
	}

	// --------------

	private void registrarSiNoExisteUsuario(Usuario admin) {
		AdaptadorUsuarioTDS adaptadorUsuarios = AdaptadorUsuarioTDS.getUnicaInstancia();
		adaptadorUsuarios.registrarUsuario(admin);
	}

	private Usuario obtenerUsuarioDesdeCodigo(String codigo) {
		AdaptadorUsuarioTDS adaptadorUsuarios = AdaptadorUsuarioTDS.getUnicaInstancia();
		return adaptadorUsuarios.recuperarUsuario(Integer.valueOf(codigo));
	}

	private void registrarSiNoExistenMensajes(List<Mensaje> mensajes) {
		AdaptadorMensajeTDS adaptadorMensajes = AdaptadorMensajeTDS.getUnicaInstancia();
		mensajes.stream().forEach(m -> adaptadorMensajes.registrarMensaje(m));
	}

	private String obtenerCodigosMensajesRecibidos(List<Mensaje> mensajesRecibidos) {
		return mensajesRecibidos.stream().map(m -> String.valueOf(m.getCodigo())).reduce("", (l, m) -> l + m + " ")
				.trim();
	}

	private List<Mensaje> obtenerMensajesDesdeCodigos(String codigos) {
		List<Mensaje> mensajes = new LinkedList<>();
		if (codigos == null || codigos.isEmpty()) {
            return mensajes; // Si es null o vacío, devolvemos una lista vacía
        }
		StringTokenizer strTok = new StringTokenizer(codigos, " ");
		AdaptadorMensajeTDS adaptadorMensajes = AdaptadorMensajeTDS.getUnicaInstancia();
		while (strTok.hasMoreTokens()) {
			String code = (String) strTok.nextElement();
			mensajes.add(adaptadorMensajes.recuperarMensaje(Integer.valueOf(code)));
		}
		return mensajes;
	}

}
