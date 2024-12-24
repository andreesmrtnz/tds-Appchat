package persistencia;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import beans.Entidad;
import beans.Propiedad;
import modelo.Contacto;
import modelo.ContactoIndividual;
import modelo.Grupo;
import modelo.Mensaje;
import modelo.Usuario;

public class AdaptadorMensajeTDS implements IAdaptadorMensajeDAO {

	private static ServicioPersistencia servPersistencia;
	private static AdaptadorMensajeTDS unicaInstancia;

	public static AdaptadorMensajeTDS getUnicaInstancia() { // patron
																// singleton
		if (unicaInstancia == null)
			return new AdaptadorMensajeTDS();
		else
			return unicaInstancia;
	}

	private AdaptadorMensajeTDS() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	/*
	 * cuando se registra una linea de venta se le asigna un identificador unico
	 */
	public void registrarMensaje(Mensaje mensaje) {
		Entidad eMensaje = new Entidad();
		boolean existe = true;

		// Si la entidad está registrada no la registra de nuevo
		if (mensaje.getCodigo() <= 0) {
		    existe = false;
		} else {
		    try {
		        eMensaje = servPersistencia.recuperarEntidad(mensaje.getCodigo());
		    } catch (NullPointerException e) {
		        existe = false;
		    }
		}



		// registrar primero los atributos que son objetos
		// registrar usuario emisor del mensaje
		registrarSiNoExisteUsuario(mensaje.getEmisor());

		// registrar contacto receptor del mensaje
		registrarSiNoExistenContactosoGrupos(mensaje.getReceptor());

		// Atributos propios del usuario
		eMensaje.setNombre("mensaje");

		// Se guarda el grupo receptor o el contacto, segun convenga
		boolean grupo = false;
		if (mensaje.getReceptor() instanceof Grupo) {
			grupo = true;
		}

		eMensaje.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(new Propiedad("texto", mensaje.getTexto()),
				new Propiedad("hora", mensaje.getHora().toString()),
				new Propiedad("emoticono", String.valueOf(mensaje.getEmoticono())),
				new Propiedad("receptor", String.valueOf(mensaje.getReceptor().getCodigo())),
				new Propiedad("togroup", String.valueOf(grupo)),
				new Propiedad("emisor", String.valueOf(mensaje.getEmisor().getCodigo())))));

		// Registrar entidad mensaje
		eMensaje = servPersistencia.registrarEntidad(eMensaje);

		// Identificador unico
		mensaje.setCodigo(eMensaje.getId());
		
		// Guardamos en el pool
		PoolDAO.getUnicaInstancia().addObjeto(mensaje.getCodigo(), mensaje);
	}

	@Override
	public Mensaje recuperarMensaje(int codigo) {
		// Si la entidad esta en el pool la devuelve directamente
		if (PoolDAO.getUnicaInstancia().contiene(codigo))
			return (Mensaje) PoolDAO.getUnicaInstancia().getObjeto(codigo);

		// si no, la recupera de la base de datos
		// recuperar entidad
		Entidad eMensaje = servPersistencia.recuperarEntidad(codigo);
		
		// recuperar propiedades que no son objetos
		// fecha
		String texto = servPersistencia.recuperarPropiedadEntidad(eMensaje, "texto");
		LocalDateTime hora = LocalDateTime.parse(servPersistencia.recuperarPropiedadEntidad(eMensaje, "hora"));
		int emoticono = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, "emoticono"));
		Contacto receptor = null;
		Boolean toGroup = Boolean.valueOf(servPersistencia.recuperarPropiedadEntidad(eMensaje, "togroup"));
		Usuario emisor = null;

		Mensaje mensaje = new Mensaje(texto, emoticono, hora);
		mensaje.setCodigo(codigo);

		// Metemos el usuario en el pool antes de llamar a otros
		// adaptadores
		PoolDAO.getUnicaInstancia().addObjeto(codigo, mensaje);

		// recuperar propiedades que son objetos llamando a adaptadores
		// mensaje
		// Usuario emisor
		AdaptadorUsuarioTDS adaptadorU = AdaptadorUsuarioTDS.getUnicaInstancia();
		int codigoUsuario = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, "emisor"));
		emisor = adaptadorU.recuperarUsuario(codigoUsuario);
		mensaje.setEmisor(emisor);

		// Contacto o grupo receptor
		int codigoContacto = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, "receptor"));
		if (toGroup) {
			AdaptadorGrupoTDS adaptadorG = AdaptadorGrupoTDS.getUnicaInstancia();
			//receptor = adaptadorG.recuperarGrupo(codigoContacto);
		} else {
			AdaptadorContactoIndividualTDS adaptadorC = AdaptadorContactoIndividualTDS.getUnicaInstancia();
			receptor = adaptadorC.recuperarContacto(codigoContacto);
		}
		mensaje.setReceptor(receptor);

		// devolver el objeto mensaje con todo
		return mensaje;
	}

	@Override
	public List<Mensaje> recuperarTodosMensajes() {
		List<Mensaje> mensajes = new LinkedList<>();
		List<Entidad> eMensajes = servPersistencia.recuperarEntidades("mensaje");

		for (Entidad eMensaje : eMensajes) {
			mensajes.add(recuperarMensaje(eMensaje.getId()));
		}
		return mensajes;
	}
	//---------------funciones auxiliares----------------

	private void registrarListaSiNoExistenContactosoGrupos(List<Contacto> contactos) {
		AdaptadorContactoIndividualTDS adaptadorContactos = AdaptadorContactoIndividualTDS.getUnicaInstancia();
		//AdaptadorGrupoTDS adaptadorGrupos = AdaptadorGrupoTDS.getUnicaInstancia();
		contactos.stream().forEach(c -> {
			if (c instanceof ContactoIndividual) {
				adaptadorContactos.registrarContacto((ContactoIndividual) c);
			} //else {
				//adaptadorGrupos.registrarGrupo((Group) c);
			//}
		});
	}
	
	private void registrarSiNoExistenContactosoGrupos(Contacto contacto) {
		LinkedList<Contacto> contactos = new LinkedList<>();
		contactos.add(contacto);
		registrarListaSiNoExistenContactosoGrupos(contactos);
	}

	private void registrarSiNoExisteUsuario(Usuario emisor) {
		AdaptadorUsuarioTDS.getUnicaInstancia().registrarUsuario(emisor);
	}

}
