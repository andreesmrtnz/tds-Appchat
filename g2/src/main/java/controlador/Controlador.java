package controlador;

import modelo.RepositorioUsuarios;
import persistencia.DAOException;
import persistencia.FactoriaDAO;
import persistencia.IAdaptadorContactoIndividualDAO;
import persistencia.IAdaptadorGrupoDAO;
import persistencia.IAdaptadorMensajeDAO;
import persistencia.IAdaptadorUsuarioDAO;

public enum Controlador {

	INSTANCE; // uso del patron Singleton para que solo haya en todo el programa una instancia
				// de controlador

	private static Controlador unicaInstancia = null;

	private IAdaptadorUsuarioDAO adaptadorUsuario;
	private IAdaptadorMensajeDAO adaptadorMensaje;
	private IAdaptadorGrupoDAO adaptadorGrupo;
	private IAdaptadorContactoIndividualDAO adaptadorContactoIndividual;
	
	private RepositorioUsuarios repoUsuarios;

	private Controlador() {
		inicializarAdaptadores();
		inicializarRepos();
	}

	void inicializarRepos() {
		repoUsuarios = RepositorioUsuarios.getUnicaInstancia();

	}

	// Métodos para manejar la lógica del chat

	public void inicializarAdaptadores() {
		FactoriaDAO factoria = null;
		try {
			factoria = FactoriaDAO.getFactoriaDAO(FactoriaDAO.DAO_TDS);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		adaptadorUsuario = factoria.getUsuarioDAO();
		adaptadorGrupo = factoria.getGrupoDAO();
		adaptadorContactoIndividual = factoria.getContactoIndividualDAO();
		adaptadorMensaje = factoria.getMensajeDAO();
	}

	public boolean doLogin(String user, char[] passwd) {

		return false;
	}
	
	public boolean doRegister(String user, char[] passwd) {

		return false;
	}
	
	
}
