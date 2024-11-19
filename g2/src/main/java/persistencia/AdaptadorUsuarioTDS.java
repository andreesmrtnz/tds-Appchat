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

    private final SimpleDateFormat dateFormat; // Para formatear fechas en la base de datos

    // Singleton con doble verificación
    public static AdaptadorUsuarioTDS getUnicaInstancia() {
        if (unicaInstancia == null) {
            synchronized (AdaptadorUsuarioTDS.class) {
                if (unicaInstancia == null) {
                    unicaInstancia = new AdaptadorUsuarioTDS();
                }
            }
        }
        return unicaInstancia;
    }

    private AdaptadorUsuarioTDS() {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    }

    // Registra un usuario en la base de datos, asignando un identificador único
    public void registrarUsuario(Usuario usuario) {
        if (PoolDAO.getUnicaInstancia().contiene(usuario.getCodigo())) {
            return; // Si ya está en el pool, no se registra de nuevo
        }

        Entidad eUsuario = null;

        try {
			eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		} catch (NullPointerException e) {}
		if (eUsuario != null)	return;

        // Crear entidad usuario con sus propiedades
        eUsuario = new Entidad();
        eUsuario.setNombre("usuario");
        eUsuario.setPropiedades(new ArrayList<>(Arrays.asList(
                new Propiedad("nombre", usuario.getUsuario()),
                new Propiedad("fechanacimiento", dateFormat.format(usuario.getFechaNacimiento())),
                new Propiedad("telefono", String.valueOf(usuario.getTelefono())),
                new Propiedad("password", usuario.getContraseña()),
                new Propiedad("imagenes", usuario.getImagen()),
                new Propiedad("saludo", usuario.getSaludo())
        )));

        // Registrar la entidad usuario en el servicio de persistencia
        eUsuario = servPersistencia.registrarEntidad(eUsuario);

        // Asignar el identificador único al usuario
        usuario.setCodigo(eUsuario.getId());

        // Añadir al pool para evitar duplicados en memoria
        PoolDAO.getUnicaInstancia().addObjeto(usuario.getCodigo(), usuario);
    }

    public Usuario recuperarUsuario(int codigo) {
        // Si el usuario ya está en el pool, devolverlo directamente
        if (PoolDAO.getUnicaInstancia().contiene(codigo)) {
            return (Usuario) PoolDAO.getUnicaInstancia().getObjeto(codigo);
        }

        // Recuperar la entidad del servicio de persistencia
        Entidad eUsuario = servPersistencia.recuperarEntidad(codigo);

        // Recuperar las propiedades de la entidad
        String nombre = servPersistencia.recuperarPropiedadEntidad(eUsuario, "nombre");

        Date fechaNacimiento = null;
        try {
            String fechaTexto = servPersistencia.recuperarPropiedadEntidad(eUsuario, "fechanacimiento");
            if (fechaTexto != null && !fechaTexto.isEmpty()) {
                fechaNacimiento = dateFormat.parse(fechaTexto);
            }
        } catch (ParseException e) {
            System.err.println("Error al parsear la fecha de nacimiento para el usuario con código: " + codigo);
            fechaNacimiento = new Date(); // Fecha por defecto si hay error
        }

        String telefono = servPersistencia.recuperarPropiedadEntidad(eUsuario, "telefono");
        String password = servPersistencia.recuperarPropiedadEntidad(eUsuario, "password");
        String saludo = servPersistencia.recuperarPropiedadEntidad(eUsuario, "saludo");
        String pathImages = servPersistencia.recuperarPropiedadEntidad(eUsuario, "imagenes");

        // Crear objeto Usuario
        Usuario usuario = new Usuario(nombre, password, telefono, fechaNacimiento, pathImages, saludo);
        usuario.setCodigo(codigo);

        // Añadir al pool
        PoolDAO.getUnicaInstancia().addObjeto(codigo, usuario);

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