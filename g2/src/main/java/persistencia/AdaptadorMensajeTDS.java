package persistencia;

import java.util.ArrayList;
import java.util.Arrays;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import beans.Entidad;
import beans.Propiedad;

import modelo.LineaVenta;
import modelo.Producto;

public class AdaptadorMensajeTDS implements IAdaptadorLineaVentaDAO {

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
	public void registrarLineaVenta(LineaVenta lineaVenta) {
		Entidad eLineaVenta = null;
		try {
			eLineaVenta = servPersistencia.recuperarEntidad(lineaVenta.getCodigo());
		} catch (NullPointerException e) {}
		if (eLineaVenta != null) return;

		// registrar primero los atributos que son objetos
		AdaptadorContactoIndividualTDS adaptadorProducto = AdaptadorContactoIndividualTDS.getUnicaInstancia();
		adaptadorProducto.registrarProducto(lineaVenta.getProducto());

		// crear entidad linea de venta
		eLineaVenta = new Entidad();
		eLineaVenta.setNombre("lineaventa");
		eLineaVenta.setPropiedades(new ArrayList<Propiedad>(
				Arrays.asList(new Propiedad("unidades", String.valueOf(lineaVenta.getUnidades())),
						new Propiedad("producto", String.valueOf(lineaVenta.getProducto().getCodigo())))));

		// registrar entidad linea de venta
		eLineaVenta = servPersistencia.registrarEntidad(eLineaVenta);
		// asignar identificador unico.
		// Se aprovecha el que genera el servicio de persistencia
		lineaVenta.setCodigo(eLineaVenta.getId());
	}

	public void borrarLineaVenta(LineaVenta lineaVenta) {
		// No se comprueba integridad con venta
		Entidad eLineaVenta = servPersistencia.recuperarEntidad(lineaVenta.getCodigo());
		servPersistencia.borrarEntidad(eLineaVenta);
	}

	public void modificarLineaVenta(LineaVenta lineaVenta) {
		Entidad eLineaVenta = servPersistencia.recuperarEntidad(lineaVenta.getCodigo());

		for (Propiedad prop : eLineaVenta.getPropiedades()) {
			if (prop.getNombre().equals("codigo")) {
				prop.setValor(String.valueOf(lineaVenta.getCodigo()));
			} else if (prop.getNombre().equals("unidades")) {
				prop.setValor(String.valueOf(lineaVenta.getUnidades()));
			} else if (prop.getNombre().equals("producto")) {
				prop.setValor(String.valueOf(lineaVenta.getProducto().getCodigo()));
			}
			servPersistencia.modificarPropiedad(prop);
		}
	}

	public LineaVenta recuperarLineaVenta(int codigo) {
		Entidad eLineaVenta;
		int unidades;
		Producto producto;

		eLineaVenta = servPersistencia.recuperarEntidad(codigo);
		unidades = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eLineaVenta, "unidades"));

		// Para recuperar el producto se lo solicita al adaptador producto
		AdaptadorContactoIndividualTDS adaptadorProducto = AdaptadorContactoIndividualTDS.getUnicaInstancia();
		producto = adaptadorProducto.recuperarProducto(
				Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eLineaVenta, "producto")));

		LineaVenta lineaVenta = new LineaVenta(unidades, producto);
		lineaVenta.setCodigo(codigo);
		return lineaVenta;
	}

}
