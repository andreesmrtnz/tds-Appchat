package vista;

import javax.swing.JComboBox;

public interface Subject {
    void agregarObservador(Observer observer);
    void eliminarObservador(Observer observer);
	void notificarObservadores(JComboBox<String> combobox);
}