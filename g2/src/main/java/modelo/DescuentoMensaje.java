package modelo;

public class DescuentoMensaje implements Descuento {
    private int minimoMensajes;

    public DescuentoMensaje(int minimoMensajes) {
        this.minimoMensajes = minimoMensajes;
    }

    public int getMinimoMensajes() {
        return minimoMensajes;
    }

    @Override
    public double getDescuento(double precioInicial) {
        return 0.8 * precioInicial; // Ejemplo: 20% de descuento
    }
}
