package persistencia;



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

	public abstract IAdaptadorUsuarioDAO getUsuarioDAO();
	public abstract IAdaptadorMensajeDAO getMensajeDAO();
	public abstract IAdaptadorContactoIndividualDAO getContactoIndividualDAO();
	public abstract IAdaptadorGrupoDAO getGrupoDAO();

	public final static String DAO_TDS = "tds.dao.TDS_FactoriaDAO";
}
