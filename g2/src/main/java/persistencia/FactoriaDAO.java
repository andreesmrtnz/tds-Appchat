package persistencia;

import umu.tds.apps.persistencia.DAOException;

public abstract class FactoriaDAO {
	private static FactoriaDAO INSTANCE;

// Crea un tipo de factoria DAO. Solo existe el tipo TDS_FactoriaDAO
	public static FactoriaDAO getFactoriaDAO(String nombre) throws DAOException {
		if (INSTANCE == null)
			try {
				INSTANCE = (FactoriaDAO) Class.forName(nombre).getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				throw new DAOException(e.getMessage());
			}
		return INSTANCE;
	}

	public static FactoriaDAO getFactoriaDAO() {
		return INSTANCE;
	}

	protected FactoriaDAO() {
	}

	public abstract UsuarioDAO getUsuarioDAO();

	public abstract MensajeDAO getMensajeDAO();

	public abstract GrupoDAO getGrupoDAO();

	public abstract ContactoIndividualDAO getContactoIndividualDAO();

	public final static String DAO_TDS = "tds.dao.TDS_FactoriaDAO";
}
