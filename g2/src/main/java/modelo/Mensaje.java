package modelo;

public class Mensaje {
	 private String emisor;
	 private String receptor;
	 private String texto;
	 
	public Mensaje(String emisor, String receptor, String texto) {
		super();
		this.emisor = emisor;
		this.receptor = receptor;
		this.texto = texto;
	}
	
	public String getEmisor() {
		return emisor;
	}
	public String getReceptor() {
		return receptor;
	}
	public String getTexto() {
		return texto;
	}
	 
	 
}
