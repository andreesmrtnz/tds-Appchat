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

import modelo.Cliente;
import modelo.Venta;

//Usa un pool para evitar problemas doble referencia con ventas
public class AdaptadorUsuarioTDS implements IAdaptadorClienteDAO {
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

	/* cuando se registra un cliente se le asigna un identificador �nico */
	public void registrarCliente(Cliente cliente) {
		Entidad eCliente = null;

		// Si la entidad esta registrada no la registra de nuevo
		try {
			eCliente = servPersistencia.recuperarEntidad(cliente.getCodigo());
		} catch (NullPointerException e) {}
		if (eCliente != null) return;

		// registrar primero los atributos que son objetos
		AdaptadorGrupoTDS adaptadorVenta = AdaptadorGrupoTDS.getUnicaInstancia();
		for (Venta v : cliente.getVentas())
			adaptadorVenta.registrarVenta(v);

		// crear entidad Cliente
		eCliente = new Entidad();
		eCliente.setNombre("cliente");
		eCliente.setPropiedades(new ArrayList<Propiedad>(
				Arrays.asList(new Propiedad("dni", cliente.getDni()), new Propiedad("nombre", cliente.getNombre()),
						new Propiedad("ventas", obtenerCodigosVentas(cliente.getVentas())))));

		// registrar entidad cliente
		eCliente = servPersistencia.registrarEntidad(eCliente);
		// asignar identificador unico
		// Se aprovecha el que genera el servicio de persistencia
		cliente.setCodigo(eCliente.getId());
	}

	public void borrarCliente(Cliente cliente) {
		// No se comprueban restricciones de integridad con Venta
		Entidad eCliente = servPersistencia.recuperarEntidad(cliente.getCodigo());

		servPersistencia.borrarEntidad(eCliente);
	}

	public void modificarCliente(Cliente cliente) {

		Entidad eCliente = servPersistencia.recuperarEntidad(cliente.getCodigo());

		for (Propiedad prop : eCliente.getPropiedades()) {
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

	public Cliente recuperarCliente(int codigo) {

		// Si la entidad est� en el pool la devuelve directamente
		if (PoolDAO.getUnicaInstancia().contiene(codigo))
			return (Cliente) PoolDAO.getUnicaInstancia().getObjeto(codigo);

		// si no, la recupera de la base de datos
		Entidad eCliente;
		List<Venta> ventas = new LinkedList<Venta>();
		String dni;
		String nombre;

		// recuperar entidad
		eCliente = servPersistencia.recuperarEntidad(codigo);

		// recuperar propiedades que no son objetos
		dni = servPersistencia.recuperarPropiedadEntidad(eCliente, "dni");
		nombre = servPersistencia.recuperarPropiedadEntidad(eCliente, "nombre");

		Cliente cliente = new Cliente(dni, nombre);
		cliente.setCodigo(codigo);

		// IMPORTANTE:a�adir el cliente al pool antes de llamar a otros
		// adaptadores
		PoolDAO.getUnicaInstancia().addObjeto(codigo, cliente);

		// recuperar propiedades que son objetos llamando a adaptadores
		// ventas
		ventas = obtenerVentasDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eCliente, "ventas"));

		for (Venta v : ventas)
			cliente.addVenta(v);

		return cliente;
	}

	public List<Cliente> recuperarTodosClientes() {

		List<Entidad> eClientes = servPersistencia.recuperarEntidades("cliente");
		List<Cliente> clientes = new LinkedList<Cliente>();

		for (Entidad eCliente : eClientes) {
			clientes.add(recuperarCliente(eCliente.getId()));
		}
		return clientes;
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
}
