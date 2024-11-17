package persistencia;

public class TDSFactoriaDAO extends FactoriaDAO {
	public TDSFactoriaDAO () {
	}

	@Override
	public IAdaptadorUsuarioDAO getUsuarioDAO() {
		// TODO Auto-generated method stub
		return AdaptadorUsuarioTDS.getUnicaInstancia();
	}

	@Override
	public IAdaptadorMensajeDAO getMensajeDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAdaptadorGrupoDAO getGrupoDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAdaptadorContactoIndividualDAO getContactoIndividualDAO() {
		// TODO Auto-generated method stub
		return null;
	}

}
