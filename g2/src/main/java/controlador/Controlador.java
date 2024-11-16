package controlador;

import modelo.RepositorioUsuarios;

public class Controlador {
	
    private static Controlador unicaInstancia = null;

    public Controlador() {
    	
    }

    // Métodos para manejar la lógica del chat
    
    //uso del patron Singleton para que solo haya en todo el programa una instancia de controlador
    public static Controlador getInstancia() {
		if (unicaInstancia == null)
			unicaInstancia = new Controlador();
		return unicaInstancia;
	}
    
    
    public boolean doLogin(String user, char[] passwd) {
    	
    	return false;
    }
}
