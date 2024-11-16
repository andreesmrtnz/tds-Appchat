package persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import beans.Entidad;
import beans.Propiedad;

import modelo.Producto;

public class AdaptadorContactoIndividualTDS implements IAdaptadorProductoDAO {

	private static ServicioPersistencia servPersistencia;
	private static AdaptadorContactoIndividualTDS unicaInstancia = null;

	public static AdaptadorContactoIndividualTDS getUnicaInstancia() { // patron singleton
		if (unicaInstancia == null) {
			return new AdaptadorContactoIndividualTDS();
		} else
			return unicaInstancia;
	}

	private AdaptadorContactoIndividualTDS() { 
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	/* cuando se registra un producto se le asigna un identificador unico */
	public void registrarProducto(Producto producto) {
		Entidad eProducto = null;
		try {
			eProducto = servPersistencia.recuperarEntidad(producto.getCodigo());
		} catch (NullPointerException e) {}
		if (eProducto != null) return;
		
		// crear entidad producto
		eProducto = new Entidad();
		eProducto.setNombre("producto");
		eProducto.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(new Propiedad("nombre", producto.getNombre()),
				new Propiedad("descripcion", producto.getDescripcion()),
				new Propiedad("precio", String.valueOf(producto.getPrecio())))));
		
		// registrar entidad producto
		eProducto = servPersistencia.registrarEntidad(eProducto);
		// asignar identificador unico
		// Se aprovecha el que genera el servicio de persistencia
		producto.setCodigo(eProducto.getId());  
	}

	public void borrarProducto(Producto producto) {
		// No se comprueba integridad con lineas de venta
		Entidad eProducto = servPersistencia.recuperarEntidad(producto.getCodigo());
		servPersistencia.borrarEntidad(eProducto);
	}

	public void modificarProducto(Producto producto) {
		Entidad eProducto = servPersistencia.recuperarEntidad(producto.getCodigo());

		for (Propiedad prop : eProducto.getPropiedades()) {
			if (prop.getNombre().equals("codigo")) {
				prop.setValor(String.valueOf(producto.getCodigo()));
			} else if (prop.getNombre().equals("precio")) {
				prop.setValor(String.valueOf(producto.getPrecio()));
			} else if (prop.getNombre().equals("nombre")) {
				prop.setValor(producto.getNombre());
			} else if (prop.getNombre().equals("descripcion")) {
				prop.setValor(producto.getDescripcion());
			} 
			servPersistencia.modificarPropiedad(prop);
		}
	}

	public Producto recuperarProducto(int codigo) {
		Entidad eProducto;
		double precio;
		String nombre;
		String descripcion;

		eProducto = servPersistencia.recuperarEntidad(codigo);
		precio = Double.parseDouble(servPersistencia.recuperarPropiedadEntidad(eProducto, "precio"));
		nombre = servPersistencia.recuperarPropiedadEntidad(eProducto, "nombre");
		descripcion = servPersistencia.recuperarPropiedadEntidad(eProducto, "descripcion");

		Producto producto = new Producto(precio, nombre, descripcion);
		producto.setCodigo(codigo);
		return producto;
	}

	public List<Producto> recuperarTodosProductos() {
		List<Producto> productos = new LinkedList<Producto>();
		List<Entidad> entidades = servPersistencia.recuperarEntidades("producto");

		for (Entidad eProducto : entidades) {
			productos.add(recuperarProducto(eProducto.getId()));
		}
		return productos;
	}

}
