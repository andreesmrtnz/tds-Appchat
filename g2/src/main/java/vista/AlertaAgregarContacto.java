package vista;

import javax.swing.*;

import controlador.Controlador;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AlertaAgregarContacto extends JFrame {

    private JTextField nombreField;
    private JTextField telefonoField;
    private Controlador controlador;

    public AlertaAgregarContacto() {
    	controlador = Controlador.INSTANCE;
        setTitle("Agregar Contacto");
        setSize(818, 364);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{1, 118, 407, 0, 0};
        gridBagLayout.rowHeights = new int[]{6, 14, 20, 20, 23, 0, 0};
        gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
        getContentPane().setLayout(gridBagLayout);
        JLabel label = new JLabel("Introduzca el nombre del contacto y su teléfono:");
        label.setFont(new Font("Tahoma", Font.BOLD, 20));
        GridBagConstraints gbc_label = new GridBagConstraints();
        gbc_label.insets = new Insets(0, 0, 5, 5);
        gbc_label.gridwidth = 2;
        gbc_label.gridx = 1;
        gbc_label.gridy = 1;
        getContentPane().add(label, gbc_label);
        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        GridBagConstraints gbc_nombreLabel = new GridBagConstraints();
        gbc_nombreLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_nombreLabel.insets = new Insets(0, 0, 5, 5);
        gbc_nombreLabel.gridx = 1;
        gbc_nombreLabel.gridy = 2;
        getContentPane().add(nombreLabel, gbc_nombreLabel);
        nombreField = new JTextField(10);
        GridBagConstraints gbc_nombreField = new GridBagConstraints();
        gbc_nombreField.fill = GridBagConstraints.HORIZONTAL;
        gbc_nombreField.insets = new Insets(0, 0, 5, 5);
        gbc_nombreField.gridx = 2;
        gbc_nombreField.gridy = 2;
        getContentPane().add(nombreField, gbc_nombreField);
        JLabel telefonoLabel = new JLabel("Teléfono:");
        telefonoLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        GridBagConstraints gbc_telefonoLabel = new GridBagConstraints();
        gbc_telefonoLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_telefonoLabel.insets = new Insets(0, 0, 5, 5);
        gbc_telefonoLabel.gridx = 1;
        gbc_telefonoLabel.gridy = 3;
        getContentPane().add(telefonoLabel, gbc_telefonoLabel);
        telefonoField = new JTextField(10);
        GridBagConstraints gbc_telefonoField = new GridBagConstraints();
        gbc_telefonoField.fill = GridBagConstraints.HORIZONTAL;
        gbc_telefonoField.insets = new Insets(0, 0, 5, 5);
        gbc_telefonoField.gridx = 2;
        gbc_telefonoField.gridy = 3;
        getContentPane().add(telefonoField, gbc_telefonoField);
        
        JPanel panel = new JPanel();
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.gridwidth = 2;
        gbc_panel.insets = new Insets(0, 0, 5, 5);
        gbc_panel.fill = GridBagConstraints.BOTH;
        gbc_panel.gridx = 1;
        gbc_panel.gridy = 4;
        getContentPane().add(panel, gbc_panel);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton aceptarButton = new JButton("Aceptar");
        aceptarButton.setFont(new Font("Tahoma", Font.BOLD, 20));
        panel.add(aceptarButton);
        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.setFont(new Font("Tahoma", Font.BOLD, 20));
        panel.add(cancelarButton);
        cancelarButton.addActionListener(e -> dispose());
        aceptarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombre = nombreField.getText();
                String telefono = telefonoField.getText();
                controlador.doAgregar(nombre,  telefono);
            }
        });
    }

    public static void main(String[] args) {
        new AlertaAgregarContacto().setVisible(true);
    }
}
