package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GestorDescuentos {
    private List<Descuento> descuentos;

    public GestorDescuentos() {
        this.descuentos = new ArrayList<>();
    }

    public void registrarDescuento(Descuento descuento) {
        descuentos.add(descuento);
    }

    public Descuento obtenerDescuentoAplicable(Usuario usuario) {
        for (Descuento descuento : descuentos) {
            if (descuento instanceof DescuentoFecha && esFechaValida((DescuentoFecha) descuento)) {
                return descuento;
            } else if (descuento instanceof DescuentoMensaje && cumpleCriterioMensajes(usuario, (DescuentoMensaje) descuento)) {
                return descuento;
            }
        }
        return null; // Sin descuento aplicable
    }

    private boolean esFechaValida(DescuentoFecha descuentoFecha) {
        LocalDate ahora = LocalDate.now();
        return !ahora.isBefore(descuentoFecha.getFechaInicio()) && !ahora.isAfter(descuentoFecha.getFechaFin());
    }

    private boolean cumpleCriterioMensajes(Usuario usuario, DescuentoMensaje descuentoMensaje) {
        return usuario.getNumeroMensajesUltimoMes() > descuentoMensaje.getMinimoMensajes();
    }
}
