package persistencia;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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

import modelo.Usuario;

//Usa un pool para evitar problemas doble referencia con ventas
public class AdaptadorUsuarioTDS implements IAdaptadorUsuarioDAO {
	private static ServicioPersistencia servPersistencia;
	private static AdaptadorUsuarioTDS unicaInstancia = null;

	public static AdaptadorUsuarioTDS getUnicaInstancia() { // patron singleton
		if (unicaInstancia == null)
			return new AdaptadorUsuarioTDS();
		else
			return unicaInstancia;
	}

	private AdaptadorUsuarioTDS() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	/* cuando se registra un usuario se le asigna un identificador �nico */
	public void registrarUsuario(Usuario usuario) {
		Entidad eUsuario = new Entidad();
		boolean existe = true;

		// Si la entidad está registrada no la registra de nuevo
		try {
			eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		} catch (NullPointerException e) {
			existe = false;
		}
		if (existe) {
			return;
		}

		// Atributos propios del usuario
		eUsuario.setNombre("usuario");
		eUsuario.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(new Propiedad("nombre", usuario.getUsuario()),
				new Propiedad("fechanacimiento", usuario.getFechaNacimiento().toString()),
				new Propiedad("telefono", String.valueOf(usuario.getTelefono())),
				new Propiedad("password", usuario.getContraseña()), new Propiedad("imagenes", usuario.getImagen()),
				new Propiedad("saludo", usuario.getSaludo()))));

		// Registrar entidad usuario
		eUsuario = servPersistencia.registrarEntidad(eUsuario);

		// Identificador unico
		usuario.setCodigo(eUsuario.getId());

		// Guardamos en el pool
		PoolDAO.getUnicaInstancia().addObjeto(usuario.getCodigo(), usuario);

	}
	/*
	 * public void modificarCliente(Cliente cliente) {
	 * 
	 * Entidad eUsuario = servPersistencia.recuperarEntidad(cliente.getCodigo());
	 * 
	 * for (Propiedad prop : eUsuario.getPropiedades()) { if
	 * (prop.getNombre().equals("codigo")) {
	 * prop.setValor(String.valueOf(cliente.getCodigo())); } else if
	 * (prop.getNombre().equals("dni")) { prop.setValor(cliente.getDni()); } else if
	 * (prop.getNombre().equals("nombre")) { prop.setValor(cliente.getNombre()); }
	 * else if (prop.getNombre().equals("ventas")) { String ventas =
	 * obtenerCodigosVentas(cliente.getVentas()); prop.setValor(ventas); }
	 * servPersistencia.modificarPropiedad(prop); }
	 * 
	 * }
	 */

	public Usuario recuperarUsuario(int codigo) {
		// Si la entidad esta en el pool la devuelve directamente
		if (PoolDAO.getUnicaInstancia().contiene(codigo))
			return (Usuario) PoolDAO.getUnicaInstancia().getObjeto(codigo);

		// si no, la recupera de la base de datos
		// recuperar entidad
		Entidad eUser = servPersistencia.recuperarEntidad(codigo);

		// recuperar propiedades que no son objetos
		// fecha
		String nombre = servPersistencia.recuperarPropiedadEntidad(eUser, "nombre");
		LocalDate fechaNacimiento = LocalDate
				.parse(servPersistencia.recuperarPropiedadEntidad(eUser, "fechanacimiento"));
		String telefono = servPersistencia.recuperarPropiedadEntidad(eUser, "telefono");
		String password = servPersistencia.recuperarPropiedadEntidad(eUser, "password");
		String saludo = servPersistencia.recuperarPropiedadEntidad(eUser, "saludo");
		
		String pathImages = servPersistencia.recuperarPropiedadEntidad(eUser, "imagenes");

		Usuario usuario = new Usuario(nombre, password, telefono, fechaNacimiento, pathImages, saludo);
		usuario.setCodigo(codigo);

		// Metemos el usuario en el pool antes de llamar a otros
		// adaptadores
		PoolDAO.getUnicaInstancia().addObjeto(codigo, usuario);

		// devolver el objeto usuario con todo
		return usuario;
	}

	@Override
	public List<Usuario> recuperarTodosUsuarios() {
		List<Usuario> usuarios = new LinkedList<>();
		List<Entidad> eUsuarios = servPersistencia.recuperarEntidades("usuario");

		for (Entidad eUsuario : eUsuarios) {
			usuarios.add(recuperarUsuario(eUsuario.getId()));
		}
		return usuarios;
	}
}
