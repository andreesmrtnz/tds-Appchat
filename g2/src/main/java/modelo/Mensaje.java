package modelo;

import java.time.LocalDateTime;
import java.util.Date;

public class Mensaje {
    private String texto;
    private int emoticono;
    private Date fecha;
    private LocalDateTime hora;
    private Usuario emisor;
    private Contacto receptor;
    private int codigo;


	public Mensaje(String texto, Usuario emisor, Contacto receptor) {
		super();
		this.texto = texto;
		this.emisor = emisor;
		this.receptor = receptor;
	}
	
	public Mensaje(String texto, int emoticono, LocalDateTime hora) {
		this.texto = texto;
		this.emoticono = emoticono;
		this.hora = hora;
		
	}
    

	public Mensaje(String texto, int emoticono, Date fecha, String hora, Usuario emisor, Contacto receptor) {
		super();
		this.emoticono = emoticono;
		this.emisor = emisor;
		this.receptor = receptor;
	}


	public Mensaje(String text, LocalDateTime now, Usuario usuarioActual, Contacto contacto) {
		this.texto = text;
		this.hora= now;
		this.emisor = usuarioActual;
		this.receptor = contacto;
	}

	public String getTexto() {
		return texto;
	}

	public int getEmoticono() {
		return emoticono;
	}

	public Date getFecha() {
		return fecha;
	}

	public LocalDateTime getHora() {
		return hora;
	}

	public Usuario getEmisor() {
		return emisor;
	}

	public Contacto getReceptor() {
		return receptor;
	}
	
	public int getCodigo() {
		return codigo;
	}


	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public void setEmoticono(int emoticono) {
		this.emoticono = emoticono;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public void setHora(LocalDateTime hora) {
		this.hora = hora;
	}

	public void setEmisor(Usuario emisor) {
		this.emisor = emisor;
	}

	public void setReceptor(Contacto receptor) {
		this.receptor = receptor;
	}
	
	

}
