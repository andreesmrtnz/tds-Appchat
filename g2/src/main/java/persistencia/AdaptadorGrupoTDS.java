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
import modelo.LineaVenta;

public class AdaptadorGrupoTDS implements IAdaptadorVentaDAO {
	// Usa un pool para evitar problemas doble referencia con cliente

	private static ServicioPersistencia servPersistencia;

	private SimpleDateFormat dateFormat; // para formatear la fecha de venta en
											// la base de datos

	private static AdaptadorGrupoTDS unicaInstancia;

	public static AdaptadorGrupoTDS getUnicaInstancia() { // patron singleton
		if (unicaInstancia == null)
			return new AdaptadorGrupoTDS();
		else
			return unicaInstancia;
	}

	private AdaptadorGrupoTDS() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	}

	/* cuando se registra una venta se le asigna un identificador unico */
	public void registrarVenta(Venta venta) {
		Entidad eVenta = null;
		try {
			eVenta = servPersistencia.recuperarEntidad(venta.getCodigo());
		} catch (NullPointerException e) {}
		if (eVenta != null)	return;

		// registrar primero los atributos que son objetos
		// registrar lineas de venta
		AdaptadorMensajeTDS adaptadorLV = AdaptadorMensajeTDS.getUnicaInstancia();
		for (LineaVenta ldv : venta.getLineasVenta())
			adaptadorLV.registrarLineaVenta(ldv);
		// registrar cliente
		AdaptadorUsuarioTDS adaptadorCliente = AdaptadorUsuarioTDS.getUnicaInstancia();
		adaptadorCliente.registrarCliente(venta.getCliente());

		// Crear entidad venta
		eVenta = new Entidad();

		eVenta.setNombre("venta");
		eVenta.setPropiedades(new ArrayList<Propiedad>(
				Arrays.asList(new Propiedad("cliente", String.valueOf(venta.getCliente().getCodigo())),
						new Propiedad("fecha", dateFormat.format(venta.getFecha())),
						new Propiedad("esCompleta", String.valueOf(venta.isCompleta())),
						new Propiedad("lineasventa", obtenerCodigosLineaVenta(venta.getLineasVenta())))));
		// registrar entidad venta
		eVenta = servPersistencia.registrarEntidad(eVenta);
		// asignar identificador unico
		// Se aprovecha el que genera el servicio de persistencia
		venta.setCodigo(eVenta.getId());
	}

	public void borrarVenta(Venta venta) {
		// No se comprueban restricciones de integridad con Cliente
		Entidad eVenta;
		AdaptadorMensajeTDS adaptadorLV = AdaptadorMensajeTDS.getUnicaInstancia();

		for (LineaVenta lineaVenta : venta.getLineasVenta()) {
			adaptadorLV.borrarLineaVenta(lineaVenta);
		}
		eVenta = servPersistencia.recuperarEntidad(venta.getCodigo());
		servPersistencia.borrarEntidad(eVenta);

	}

	public void modificarVenta(Venta venta) {
		Entidad eVenta = servPersistencia.recuperarEntidad(venta.getCodigo());

		for (Propiedad prop : eVenta.getPropiedades()) {
			if (prop.getNombre().equals("codigo")) {
				prop.setValor(String.valueOf(venta.getCodigo()));
			} else if (prop.getNombre().equals("fecha")) {
				prop.setValor(dateFormat.format(venta.getFecha()));
			} else if (prop.getNombre().equals("esCompleta")) {
				prop.setValor(String.valueOf(venta.isCompleta()));
			} else if (prop.getNombre().equals("cliente")) {
				prop.setValor(String.valueOf(venta.getCliente().getCodigo()));
			} else if (prop.getNombre().equals("lineasventa")) {
				String lineas = obtenerCodigosLineaVenta(venta.getLineasVenta());
				prop.setValor(lineas);
			}
			servPersistencia.modificarPropiedad(prop);
		}
	}

	public Venta recuperarVenta(int codigo) {
		// Si la entidad est� en el pool la devuelve directamente
		if (PoolDAO.getUnicaInstancia().contiene(codigo))
			return (Venta) PoolDAO.getUnicaInstancia().getObjeto(codigo);

		// si no, la recupera de la base de datos
		// recuperar entidad
		Entidad eVenta = servPersistencia.recuperarEntidad(codigo);

		// recuperar propiedades que no son objetos
		// fecha
		Date fecha = null;
		try {
			fecha = dateFormat.parse(servPersistencia.recuperarPropiedadEntidad(eVenta, "fecha"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		boolean esCompleta = Boolean.valueOf(servPersistencia.recuperarPropiedadEntidad(eVenta, "esCompleta"));
		
		Venta venta = new Venta(fecha);
		venta.setCodigo(codigo);
		venta.setCompleta(esCompleta);

		// IMPORTANTE:a�adir la venta al pool antes de llamar a otros
		// adaptadores
		PoolDAO.getUnicaInstancia().addObjeto(codigo, venta);

		// recuperar propiedades que son objetos llamando a adaptadores
		// cliente
		AdaptadorUsuarioTDS adaptadorCliente = AdaptadorUsuarioTDS.getUnicaInstancia();
		int codigoCliente = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eVenta, "cliente"));

		Cliente cliente = adaptadorCliente.recuperarCliente(codigoCliente);
		venta.setCliente(cliente);
		// lineas de venta
		List<LineaVenta> lineasVenta = obtenerLineasVentaDesdeCodigos(
				servPersistencia.recuperarPropiedadEntidad(eVenta, "lineasventa"));

		for (LineaVenta lv : lineasVenta)
			venta.addLineaVenta(lv);

		// devolver el objeto venta
		return venta;
	}

	public List<Venta> recuperarTodasVentas() {
		List<Venta> ventas = new LinkedList<Venta>();
		List<Entidad> eVentas = servPersistencia.recuperarEntidades("venta");

		for (Entidad eVenta : eVentas) {
			ventas.add(recuperarVenta(eVenta.getId()));
		}
		return ventas;
	}

	// -------------------Funciones auxiliares-----------------------------
	private String obtenerCodigosLineaVenta(List<LineaVenta> lineasVenta) {
		String lineas = "";
		for (LineaVenta lineaVenta : lineasVenta) {
			lineas += lineaVenta.getCodigo() + " ";
		}
		return lineas.trim();
	}

	private List<LineaVenta> obtenerLineasVentaDesdeCodigos(String lineas) {
		List<LineaVenta> lineasVenta = new LinkedList<LineaVenta>();
		StringTokenizer strTok = new StringTokenizer(lineas, " ");
		AdaptadorMensajeTDS adaptadorLV = AdaptadorMensajeTDS.getUnicaInstancia();
		while (strTok.hasMoreTokens()) {
			lineasVenta.add(adaptadorLV.recuperarLineaVenta(Integer.valueOf((String) strTok.nextElement())));
		}
		return lineasVenta;
	}

}
