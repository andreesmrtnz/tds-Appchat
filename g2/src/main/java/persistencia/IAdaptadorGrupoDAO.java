package persistencia;

import java.util.List;

import modelo.Grupo;

public interface IAdaptadorGrupoDAO {
	public void registrarGrupo(Grupo grupo);
	public void modificarGrupo(Grupo grupo);
	public Grupo recuperarGrupo(int codigo);
	public List<Grupo> recuperarTodosGrupos();
}
