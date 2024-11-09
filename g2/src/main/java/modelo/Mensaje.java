package modelo;

import java.util.Date;

public class Mensaje {
    private String texto;
    private String emoticono;
    private Date fecha;
    private String hora;
    private Usuario emisor;
    private Usuario receptor;

   
    public Mensaje(String texto, Usuario emisor, Usuario receptor) {
		super();
		this.texto = texto;
		this.emisor = emisor;
		this.receptor = receptor;
	}
    

	public Mensaje(String texto, String emoticono, Date fecha, String hora, Usuario emisor, Usuario receptor) {
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

	public Usuario getEmisor() {
		return emisor;
	}

	public Usuario getReceptor() {
		return receptor;
	}

    
    // Getters y Setters
}
