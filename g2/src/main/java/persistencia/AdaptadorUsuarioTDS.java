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
	    Entidad eUsuario = null;

	    // Verificar si el usuario ya está registrado para evitar duplicados
	    try {
	        eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
	    } catch (NullPointerException e) {}
	    if (eUsuario != null) return;

	    // Crear una nueva entidad para el usuario
	    eUsuario = new Entidad();
	    eUsuario.setNombre("usuario");
	    eUsuario.setPropiedades(new ArrayList<Propiedad>(
	            Arrays.asList(
	                    new Propiedad("usuario", usuario.getUsuario()),
	                    new Propiedad("contraseña", usuario.getContraseña()),
	                    new Propiedad("telefono", usuario.getTelefono()),
	                    new Propiedad("fechaNacimiento", usuario.getFechaNacimiento().toString()),
	                    new Propiedad("imagen", usuario.getImagen()),
	                    new Propiedad("saludo", usuario.getSaludo())
	            )));

	    // Registrar la entidad del usuario en la base de datos
	    eUsuario = servPersistencia.registrarEntidad(eUsuario);

	    // Asignar el identificador único al usuario (ID de persistencia)
	    usuario.setCodigo(eUsuario.getId());
	    
	    PoolDAO.getUnicaInstancia().addObjeto(usuario.getCodigo(), usuario);
	    
	    
	}
	/*
	public void modificarCliente(Cliente cliente) {

		Entidad eUsuario = servPersistencia.recuperarEntidad(cliente.getCodigo());

		for (Propiedad prop : eUsuario.getPropiedades()) {
			if (prop.getNombre().equals("codigo")) {
				prop.setValor(String.valueOf(cliente.getCodigo()));
			} else if (prop.getNombre().equals("dni")) {
				prop.setValor(cliente.getDni());
			} else if (prop.getNombre().equals("nombre")) {
				prop.setValor(cliente.getNombre());
			} else if (prop.getNombre().equals("ventas")) {
				String ventas = obtenerCodigosVentas(cliente.getVentas());
				prop.setValor(ventas);
			}
			servPersistencia.modificarPropiedad(prop);
		}

	}
	*/

	public Usuario recuperarUsuario(int codigo) {
	    // Si el usuario ya está en el pool, lo devuelve directamente
	    if (PoolDAO.getUnicaInstancia().contiene(codigo))
	        return (Usuario) PoolDAO.getUnicaInstancia().getObjeto(codigo);

	    // Recuperar entidad desde el servicio de persistencia
	    Entidad eUsuario = servPersistencia.recuperarEntidad(codigo);

	    // Recuperar propiedades básicas del usuario
	    String usuario = servPersistencia.recuperarPropiedadEntidad(eUsuario, "usuario");
	    String contraseña = servPersistencia.recuperarPropiedadEntidad(eUsuario, "contraseña");
	    String telefono = servPersistencia.recuperarPropiedadEntidad(eUsuario, "telefono");
	    String fechaNacimientoStr = servPersistencia.recuperarPropiedadEntidad(eUsuario, "fechaNacimiento");
	    String imagen = servPersistencia.recuperarPropiedadEntidad(eUsuario, "imagen");
	    String saludo = servPersistencia.recuperarPropiedadEntidad(eUsuario, "saludo");

	    // Convertir fecha de nacimiento de String a Date
	    Date fechaNacimiento = null;
	    try {
	        fechaNacimiento = new SimpleDateFormat("yyyy-MM-dd").parse(fechaNacimientoStr);
	    } catch (ParseException e) {
	        e.printStackTrace(); // Manejo básico de errores
	    }

	    // Crear instancia del usuario con los datos recuperados
	    Usuario usuarioObj = new Usuario(usuario, contraseña, telefono, fechaNacimiento, imagen, saludo);

	    // IMPORTANTE: Añadir el usuario al pool antes de recuperar contactos o mensajes
	    PoolDAO.getUnicaInstancia().addObjeto(codigo, usuarioObj);

	    // Recuperar contactos del usuario
	    // (Opcional: Implementar lógica para manejar contactos si están relacionados con otra tabla/entidad)

	    // Recuperar mensajes enviados y recibidos del usuario
	    // (Opcional: Implementar lógica adicional si los mensajes están en otra entidad o requieren adaptadores)

	    return usuarioObj;
	}

	/*
	public List<Cliente> recuperarTodosusuarios() {

		List<Entidad> eUsuarios = servPersistencia.recuperarEntidades("cliente");
		List<Cliente> usuarios = new LinkedList<Cliente>();

		for (Entidad eUsuario : eUsuarios) {
			usuarios.add(recuperarCliente(eUsuario.getId()));
		}
		return usuarios;
	}

	// -------------------Funciones auxiliares-----------------------------
	private String obtenerCodigosVentas(List<Venta> listaVentas) {
		String aux = "";
		for (Venta v : listaVentas) {
			aux += v.getCodigo() + " ";
		}
		return aux.trim();
	}

	private List<Venta> obtenerVentasDesdeCodigos(String ventas) {

		List<Venta> listaVentas = new LinkedList<Venta>();
		StringTokenizer strTok = new StringTokenizer(ventas, " ");
		AdaptadorGrupoTDS adaptadorV = AdaptadorGrupoTDS.getUnicaInstancia();
		while (strTok.hasMoreTokens()) {
			listaVentas.add(adaptadorV.recuperarVenta(Integer.valueOf((String) strTok.nextElement())));
		}
		return listaVentas;
	}
	*/
}
