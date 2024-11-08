package vista;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.border.EmptyBorder;

import modelo.Mensaje;

public class VentanaMain extends JFrame {

    private JPanel contentPane;
    private JList<Mensaje> list; // Cambiar tipo a JList<Mensaje>

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    VentanaMain frame = new VentanaMain();
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
    public VentanaMain() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1141, 485);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{0, 412, 0, 598, 0, 0};
        gbl_contentPane.rowHeights = new int[]{0, 364, 30, 0};
        gbl_contentPane.columnWeights = new double[]{1.0, 1.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
        contentPane.setLayout(gbl_contentPane);

        JPanel panel_2 = new JPanel();
        GridBagConstraints gbc_panel_2 = new GridBagConstraints();
        gbc_panel_2.gridwidth = 3;
        gbc_panel_2.insets = new Insets(0, 0, 5, 5);
        gbc_panel_2.fill = GridBagConstraints.BOTH;
        gbc_panel_2.gridx = 1;
        gbc_panel_2.gridy = 0;
        contentPane.add(panel_2, gbc_panel_2);
        panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.X_AXIS));

        JComboBox comboBox = new JComboBox();
        comboBox.setName("contacto o telefono");
        comboBox.setModel(new DefaultComboBoxModel(new String[]{"contacto", "telefono"}));
        comboBox.setToolTipText("contacto o telefono");
        panel_2.add(comboBox);

        JButton enviarBarraButton = new JButton("");
        enviarBarraButton.setIcon(new ImageIcon(VentanaMain.class.getResource("/imagen/enviar.png")));
        panel_2.add(enviarBarraButton);

        JButton searchButton = new JButton("");
        searchButton.setIcon(new ImageIcon(VentanaMain.class.getResource("/imagen/search-engine-optimization.png")));
        panel_2.add(searchButton);

        JButton contactosButton = new JButton("Contactos");
        contactosButton.setIcon(new ImageIcon(VentanaMain.class.getResource("/imagen/trabajo-en-equipo.png")));
        panel_2.add(contactosButton);

        Component horizontalGlue = Box.createHorizontalGlue();
        horizontalGlue.setMinimumSize(new Dimension(150, 0));
        panel_2.add(horizontalGlue);

        JButton btnNewButton_4 = new JButton("Premium");
        btnNewButton_4.setIcon(new ImageIcon(VentanaMain.class.getResource("/imagen/calidad-premium.png")));
        panel_2.add(btnNewButton_4);

        JLabel imagenPerfilLabel = new JLabel("IMAGEN");
        imagenPerfilLabel.setIcon(new ImageIcon(VentanaMain.class.getResource("/imagen/contrasena.png")));
        panel_2.add(imagenPerfilLabel);

        JPanel panel_1 = new JPanel();
        GridBagConstraints gbc_panel_1 = new GridBagConstraints();
        gbc_panel_1.insets = new Insets(0, 0, 5, 5);
        gbc_panel_1.fill = GridBagConstraints.BOTH;
        gbc_panel_1.gridx = 1;
        gbc_panel_1.gridy = 1;
        contentPane.add(panel_1, gbc_panel_1);
        panel_1.setLayout(new BorderLayout(0, 0));

        // Cambiar el modelo del JList para usar objetos Mensaje
        DefaultListModel<Mensaje> mensajeModel = new DefaultListModel<Mensaje>();
        mensajeModel.addElement(new Mensaje("Alice", "Bob", "Hola Bob, ¿cómo estás?"));
        mensajeModel.addElement(new Mensaje("Bob", "Alice", "¡Hola Alice! Todo bien, ¿y tú?"));
        mensajeModel.addElement(new Mensaje("Charlie", "Alice", "¿Listo para la reunión?"));

        list = new JList<Mensaje>(mensajeModel); // JList ahora usa el modelo de Mensaje
        list.setCellRenderer(new MensajeCellRenderer()); // Configurar el renderizador personalizado
        panel_1.add(list, BorderLayout.CENTER);

        JScrollBar scrollBar = new JScrollBar();
        panel_1.add(scrollBar, BorderLayout.EAST);

        JPanel panel = new JPanel();
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.insets = new Insets(0, 0, 5, 5);
        gbc_panel.fill = GridBagConstraints.BOTH;
        gbc_panel.gridx = 3;
        gbc_panel.gridy = 1;
        contentPane.add(panel, gbc_panel);
    }
}
