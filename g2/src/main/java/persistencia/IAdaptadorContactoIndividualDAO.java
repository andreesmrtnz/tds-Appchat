package persistencia;

import java.util.List;

import modelo.ContactoIndividual;

public interface IAdaptadorContactoIndividualDAO {
	public void registrarContacto(ContactoIndividual contacto);
	public void borrarContacto(ContactoIndividual contacto);
	public void modificarContacto(ContactoIndividual contacto);
	public ContactoIndividual recuperarContacto(int codigo);
	public List<ContactoIndividual> recuperarTodosContactos();
}
