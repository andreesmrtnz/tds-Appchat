package modelo;

import java.time.LocalDate;

public class DescuentoFecha implements Descuento {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public DescuentoFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    @Override
    public double getDescuento(double precioInicial) {
        return 0.9 * precioInicial; // 10%
    }
}
