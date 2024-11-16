package persistencia;

import java.util.List;

import modelo.Grupo;

public interface IAdaptadorGrupoDAO {
	public void registrarGrupo(Grupo grupo);
	public void borrarGrupo(Grupo grupo);
	public void modificarGrupo(Grupo grupo);
	public Grupo recuperarContacto(int codigo);
	public List<Grupo> recuperarTodosContactos();
}
