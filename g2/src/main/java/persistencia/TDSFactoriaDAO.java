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
		return AdaptadorMensajeTDS.getUnicaInstancia();
	}

	@Override
	public IAdaptadorGrupoDAO getGrupoDAO() {
		return AdaptadorGrupoTDS.getUnicaInstancia();
	}

	@Override
	public IAdaptadorContactoIndividualDAO getContactoIndividualDAO() {
		// TODO Auto-generated method stub
		return AdaptadorContactoIndividualTDS.getUnicaInstancia();
	}

}
