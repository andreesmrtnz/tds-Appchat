package persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import beans.Entidad;
import beans.Propiedad;

import modelo.ContactoIndividual;
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

        // Crear nueva entidad contacto
        eContacto = new Entidad();
        eContacto.setNombre("contacto");
        eContacto.setPropiedades(new ArrayList<>(Arrays.asList(
            new Propiedad("nombre", contacto.getNombre()),
            new Propiedad("movil", String.valueOf(contacto.getMovil())),
            new Propiedad("usuario", String.valueOf(contacto.getUsuario().getCodigo()))
        )));

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

		// Se da el cambiazo a las propiedades del contacto
		servPersistencia.eliminarPropiedadEntidad(eContact, "nombre");
		servPersistencia.anadirPropiedadEntidad(eContact, "nombre", contacto.getNombre());
		servPersistencia.eliminarPropiedadEntidad(eContact, "movil");
		servPersistencia.anadirPropiedadEntidad(eContact, "movil", String.valueOf(contacto.getMovil()));
		servPersistencia.eliminarPropiedadEntidad(eContact, "usuario");
		servPersistencia.anadirPropiedadEntidad(eContact, "usuario", String.valueOf(contacto.getUsuario().getCodigo()));
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
    			
    			ContactoIndividual contacto = new ContactoIndividual(nombre, null, movil);
    			contacto.setCodigo(codigo);

    			// Metemos al contacto en el pool antes de llamar a otros adaptadores
    			PoolDAO.getUnicaInstancia().addObjeto(codigo, contacto);

    			// Obtener usuario del contacto
    			AdaptadorUsuarioTDS adaptadorUsuarios = AdaptadorUsuarioTDS.getUnicaInstancia();
    			contacto.setUsuario(adaptadorUsuarios.recuperarUsuario(Integer.valueOf(servPersistencia.recuperarPropiedadEntidad(eContacto, "usuario"))));

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
}
