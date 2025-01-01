package vista;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controlador.Controlador;
import modelo.Contacto;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Optional;

public class VentanaExportarPDF extends JFrame {

    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    VentanaExportarPDF frame = new VentanaExportarPDF();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public VentanaExportarPDF() {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setBounds(100, 100, 855, 423);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0};
        gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0};
        gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
        contentPane.setLayout(gbl_contentPane);

        JLabel lblNewLabel = new JLabel("Selecciona un contacto");
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel.gridx = 1;
        gbc_lblNewLabel.gridy = 1;
        contentPane.add(lblNewLabel, gbc_lblNewLabel);

        JComboBox<String> comboBox = new JComboBox<>();
        cargarComboBox(comboBox);
        GridBagConstraints gbc_comboBox = new GridBagConstraints();
        gbc_comboBox.insets = new Insets(0, 0, 5, 5);
        gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_comboBox.gridx = 2;
        gbc_comboBox.gridy = 1;
        contentPane.add(comboBox, gbc_comboBox);

        JButton btnExportar = new JButton("Exportar a PDF");
        GridBagConstraints gbc_btnExportar = new GridBagConstraints();
        gbc_btnExportar.insets = new Insets(0, 0, 5, 5);
        gbc_btnExportar.gridx = 2;
        gbc_btnExportar.gridy = 2;
        contentPane.add(btnExportar, gbc_btnExportar);

        // Acción del botón para seleccionar archivo y exportar
        btnExportar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String contactoSeleccionado = (String) comboBox.getSelectedItem();
                if (contactoSeleccionado == null) {
                    JOptionPane.showMessageDialog(contentPane, "Por favor, selecciona un contacto.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Optional<Contacto> contacto = Controlador.INSTANCE.getContacto(contactoSeleccionado);

                // Selector de archivo
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Selecciona la ubicación para guardar el PDF");
                fileChooser.setSelectedFile(new File(contactoSeleccionado + "_Conversacion.pdf"));

                int userSelection = fileChooser.showSaveDialog(VentanaExportarPDF.this);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File archivoSeleccionado = fileChooser.getSelectedFile();
                    String rutaArchivo = archivoSeleccionado.getAbsolutePath();

                    Controlador.INSTANCE.crearPDFconversacion(contacto.get(),rutaArchivo, archivoSeleccionado);

                    JOptionPane.showMessageDialog(contentPane, "PDF generado correctamente en: " + rutaArchivo,
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }
    
    public void cargarComboBox(JComboBox<String> comboBox) {
		comboBox.setName("contacto o telefono");
		DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
		Controlador.INSTANCE.getContactosNombre().forEach(comboBoxModel::addElement);
		comboBoxModel.addElement("telefono");
		comboBox.setModel(comboBoxModel);
		comboBox.setEditable(true);
		comboBox.setToolTipText("Seleccione un contacto o escriba un teléfono");
	}
}
