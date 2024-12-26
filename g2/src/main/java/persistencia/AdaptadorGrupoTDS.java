package persistencia;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import beans.Entidad;
import beans.Propiedad;

import modelo.Venta;
import modelo.Cliente;
import modelo.ContactoIndividual;
import modelo.Grupo;
import modelo.LineaVenta;
import modelo.Mensaje;
import modelo.Usuario;

public class AdaptadorGrupoTDS implements IAdaptadorGrupoDAO {
	// Usa un pool para evitar problemas doble referencia con cliente

	private static ServicioPersistencia servPersistencia;

	private static AdaptadorGrupoTDS unicaInstancia;

	public static AdaptadorGrupoTDS getUnicaInstancia() { // patron singleton
		if (unicaInstancia == null)
			return new AdaptadorGrupoTDS();
		else
			return unicaInstancia;
	}

	private AdaptadorGrupoTDS() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	@Override
	public void registrarGrupo(Grupo group) {
		Entidad eGroup = new Entidad();
		boolean existe = true;

		// Si la entidad está registrada no la registra de nuevo
		try {
			eGroup = servPersistencia.recuperarEntidad(group.getCodigo());
		} catch (NullPointerException e) {
			existe = false;
		}
		if (existe)
			return;

		// Registramos primero los atributos que son objetos
		// Registrar los mensajes del grupo
		registrarSiNoExistenMensajes(group.getMensajesEnviados());

		// Registramos los contactos del grupo si no existen (Integrantes)
		registrarSiNoExistenContactos(group.getParticipantes());

		// Registramos a nuestro usuario administrador si no existe.
		registrarSiNoExisteAdmin(group.getAdmin());

		// Atributos propios del grupo
		eGroup.setNombre("grupo");
		eGroup.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(new Propiedad("nombre", group.getNombre()),
				new Propiedad("mensajesRecibidos", obtenerCodigosMensajesRecibidos(group.getMensajesEnviados())),
				new Propiedad("integrantes", obtenerCodigosContactosIndividual(group.getParticipantes())),
				new Propiedad("admin", String.valueOf(group.getAdmin().getCodigo())))));

		// Registrar entidad usuario
		eGroup = servPersistencia.registrarEntidad(eGroup);

		// Identificador unico
		group.setCodigo(eGroup.getId());
		
		// Guardamos en el pool
		PoolDAO.getUnicaInstancia().addObjeto(group.getCodigo(), group);
	}

	@Override
	public void modificarGrupo(Grupo group) {
		Entidad eGroup = servPersistencia.recuperarEntidad(group.getCodigo());

		// Se da el cambiazo a las propiedades del grupo
		servPersistencia.eliminarPropiedadEntidad(eGroup, "nombre");
		servPersistencia.anadirPropiedadEntidad(eGroup, "nombre", group.getNombre());
		servPersistencia.eliminarPropiedadEntidad(eGroup, "mensajesRecibidos");
		servPersistencia.anadirPropiedadEntidad(eGroup, "mensajesRecibidos",
				obtenerCodigosMensajesRecibidos(group.getMensajesEnviados()));
		servPersistencia.eliminarPropiedadEntidad(eGroup, "integrantes");
		servPersistencia.anadirPropiedadEntidad(eGroup, "integrantes",
				obtenerCodigosContactosIndividual(group.getParticipantes()));
		servPersistencia.eliminarPropiedadEntidad(eGroup, "admin");
		servPersistencia.anadirPropiedadEntidad(eGroup, "admin", String.valueOf(group.getAdmin().getCodigo()));
	}

	@Override
	public Grupo recuperarGrupo(int codigo) {
		// Si la entidad esta en el pool la devuelve directamente
		if (PoolDAO.getUnicaInstancia().contiene(codigo))
			return (Grupo) PoolDAO.getUnicaInstancia().getObjeto(codigo);

		// Sino, la recupera de la base de datos
		// Recuperamos la entidad
		Entidad eGroup = servPersistencia.recuperarEntidad(codigo);

		// recuperar propiedades que no son objetos
		String nombre = null;
		nombre = servPersistencia.recuperarPropiedadEntidad(eGroup, "nombre");

		Grupo group = new Grupo(nombre, new LinkedList<Mensaje>(), new LinkedList<ContactoIndividual>(), null);
		group.setCodigo(codigo);

		// Metemos al grupo en el pool antes de llamar a otros adaptadores
		PoolDAO.getUnicaInstancia().addObjeto(codigo, group);
		
		// Mensajes que el grupo tiene
		List<Mensaje> mensajes = obtenerMensajesDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eGroup, "mensajesRecibidos"));
		for (Mensaje m : mensajes)
			group.sendMessage(m);
				
		// Contactos que el grupo tiene
		List<ContactoIndividual> contactos = obtenerIntegrantesDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eGroup, "integrantes"));
		for (ContactoIndividual c : contactos)
			group.addIntegrante(c);

		// Obtener admin
		group.cambiarAdmin(obtenerUsuarioDesdeCodigo(servPersistencia.recuperarPropiedadEntidad(eGroup, "admin")));

		// Devolvemos el objeto grupo
		return group;
	}

	@Override
	public List<Grupo> recuperarTodosGrupos() {
		List<Grupo> grupos = new LinkedList<>();
		List<Entidad> eGroups = servPersistencia.recuperarEntidades("grupo");

		for (Entidad eGroup : eGroups) {
			grupos.add(recuperarGrupo(eGroup.getId()));
		}
		
		return grupos;
	}

	
	// Funciones auxiliares.
	private void registrarSiNoExistenMensajes(List<Mensaje> messages) {
		AdaptadorMensajeTDS adaptadorMensajes = AdaptadorMensajeTDS.getUnicaInstancia();
		messages.stream().forEach(m -> adaptadorMensajes.registrarMensaje(m));
	}

	private void registrarSiNoExistenContactos(List<ContactoIndividual> contacts) {
		AdaptadorContactoIndividualTDS adaptadorContactos = AdaptadorContactoIndividualTDS.getUnicaInstancia();
		contacts.stream().forEach(c -> adaptadorContactos.registrarContacto(c));
	}

	private void registrarSiNoExisteAdmin(Usuario admin) {
		AdaptadorUsuarioTDS adaptadorUsuarios = AdaptadorUsuarioTDS.getUnicaInstancia();
		adaptadorUsuarios.registrarUsuario(admin);
	}

	private String obtenerCodigosContactosIndividual(List<ContactoIndividual> contactosIndividuales) {
		return contactosIndividuales.stream().map(c -> String.valueOf(c.getCodigo())).reduce("", (l, c) -> l + c + " ")
				.trim();
	}

	private String obtenerCodigosMensajesRecibidos(List<Mensaje> mensajesRecibidos) {
		return mensajesRecibidos.stream().map(m -> String.valueOf(m.getCodigo())).reduce("", (l, m) -> l + m + " ")
				.trim();
	}
	
	private List<Mensaje> obtenerMensajesDesdeCodigos(String codigos) {
		List<Mensaje> mensajes = new LinkedList<>();
		StringTokenizer strTok = new StringTokenizer(codigos, " ");
		AdaptadorMensajeTDS adaptadorMensajes = AdaptadorMensajeTDS.getUnicaInstancia();
		while (strTok.hasMoreTokens()) {
			mensajes.add(adaptadorMensajes.recuperarMensaje(Integer.valueOf((String) strTok.nextElement())));
		}
		return mensajes;
	}
	
	private List<ContactoIndividual> obtenerIntegrantesDesdeCodigos(String codigos) {
		List<ContactoIndividual> contactos = new LinkedList<>();
		StringTokenizer strTok = new StringTokenizer(codigos, " ");
		AdaptadorContactoIndividualTDS adaptadorC = AdaptadorContactoIndividualTDS.getUnicaInstancia();
		while (strTok.hasMoreTokens()) {
			contactos.add(adaptadorC.recuperarContacto(Integer.valueOf((String) strTok.nextElement())));
		}
		return contactos;
	}
	
	private Usuario obtenerUsuarioDesdeCodigo(String codigo) {
		AdaptadorUsuarioTDS adaptadorUsuarios = AdaptadorUsuarioTDS.getUnicaInstancia();
		return adaptadorUsuarios.recuperarUsuario(Integer.valueOf(codigo));
	}
}
