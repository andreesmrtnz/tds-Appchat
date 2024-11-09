package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AlertaTelefonoNoExiste extends JFrame {

    public AlertaTelefonoNoExiste() {
        setTitle("Teléfono No Existe");
        setSize(616, 286);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{7, 349, 0, 0};
        gridBagLayout.rowHeights = new int[]{7, 14, 23, 2, 0};
        gridBagLayout.columnWeights = new double[]{1.0, 0.0, 1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
        getContentPane().setLayout(gridBagLayout);
        JButton aceptarButton = new JButton("Aceptar");
        aceptarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        JLabel label = new JLabel("El teléfono indicado no existe.");
        label.setFont(new Font("Tahoma", Font.BOLD, 20));
        GridBagConstraints gbc_label = new GridBagConstraints();
        gbc_label.insets = new Insets(0, 0, 5, 5);
        gbc_label.gridx = 1;
        gbc_label.gridy = 1;
        getContentPane().add(label, gbc_label);
        GridBagConstraints gbc_aceptarButton = new GridBagConstraints();
        gbc_aceptarButton.insets = new Insets(0, 0, 5, 5);
        gbc_aceptarButton.gridx = 1;
        gbc_aceptarButton.gridy = 2;
        getContentPane().add(aceptarButton, gbc_aceptarButton);
    }

    public static void main(String[] args) {
        new AlertaTelefonoNoExiste().setVisible(true);
    }
}
