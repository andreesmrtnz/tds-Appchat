package modelo;

import java.util.Date;

public class Mensaje {
    private String texto;
    private String emoticono;
    private Date fecha;
    private String hora;
    private String emisor;
    private String receptor;

   
    public Mensaje(String texto, String emisor, String receptor) {
		super();
		this.texto = texto;
		this.emisor = emisor;
		this.receptor = receptor;
	}
    

	public Mensaje(String texto, String emoticono, Date fecha, String hora, String emisor, String receptor) {
		super();
		this.emoticono = emoticono;
		this.emisor = emisor;
		this.receptor = receptor;
	}


	public String getTexto() {
		return texto;
	}

	public String getEmoticono() {
		return emoticono;
	}

	public Date getFecha() {
		return fecha;
	}

	public String getHora() {
		return hora;
	}

	public String getEmisor() {
		return emisor;
	}

	public String getReceptor() {
		return receptor;
	}

    
    // Getters y Setters
}
