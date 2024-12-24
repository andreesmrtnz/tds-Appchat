package modelo;

import java.time.LocalDateTime;
import java.util.Date;

public class Mensaje implements Comparable<Mensaje>{
    private String texto;
    private int emoticono;
    private Date fecha;
    private LocalDateTime hora;
    private Usuario emisor;
    private Contacto receptor;
    private int codigo;

 // Constructor.
 	public Mensaje(String texto, LocalDateTime hora, Usuario emisor, Contacto receptor) {
 		this.texto = texto;
 		this.hora = hora;
 		this.setEmisor(emisor);
 		this.setReceptor(receptor);
 	}

 	public Mensaje(int emoticono, LocalDateTime hora, Usuario emisor, Contacto receptor) {
 		this.texto = "";
 		this.hora = hora;
 		this.emoticono = emoticono;
 		this.setEmisor(emisor);
 		this.setReceptor(receptor);
 	}

 	public Mensaje(String texto, int emoticono, LocalDateTime hora) {
 		this.texto = texto;
 		this.emoticono = emoticono;
 		this.hora = hora;
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
	
	@Override
	public int compareTo(Mensaje mensaje) {
		return hora.compareTo(mensaje.hora);
	}

}
