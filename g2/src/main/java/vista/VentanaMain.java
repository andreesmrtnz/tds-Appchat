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
import tds.BubbleText;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JTextField;

public class VentanaMain extends JFrame {

    private JPanel contentPane;
    private JList<Mensaje> list; // Cambiar tipo a JList<Mensaje>
    private JTextField textField;

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
        gbl_contentPane.columnWidths = new int[]{0, 412, 0, 598, 0, 0, 0};
        gbl_contentPane.rowHeights = new int[]{0, 364, 30, 0};
        gbl_contentPane.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        contentPane.setLayout(gbl_contentPane);

        JPanel panelSuperior = new JPanel();
        GridBagConstraints gbc_panelSuperior = new GridBagConstraints();
        gbc_panelSuperior.gridwidth = 4;
        gbc_panelSuperior.insets = new Insets(0, 0, 5, 5);
        gbc_panelSuperior.fill = GridBagConstraints.BOTH;
        gbc_panelSuperior.gridx = 1;
        gbc_panelSuperior.gridy = 0;
        contentPane.add(panelSuperior, gbc_panelSuperior);
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.X_AXIS));

        JComboBox comboBox = new JComboBox();
        comboBox.setName("contacto o telefono");
        comboBox.setModel(new DefaultComboBoxModel(new String[]{"contacto", "telefono"}));
        comboBox.setToolTipText("contacto o telefono");
        panelSuperior.add(comboBox);

        JButton enviarBarraButton = new JButton("");
        enviarBarraButton.setIcon(new ImageIcon(VentanaMain.class.getResource("/imagen/enviar.png")));
        panelSuperior.add(enviarBarraButton);

        JButton searchButton = new JButton("");
        searchButton.setIcon(new ImageIcon(VentanaMain.class.getResource("/imagen/search-engine-optimization.png")));
        panelSuperior.add(searchButton);

        JButton contactosButton = new JButton("Contactos");
        contactosButton.setIcon(new ImageIcon(VentanaMain.class.getResource("/imagen/trabajo-en-equipo.png")));
        panelSuperior.add(contactosButton);

        Component horizontalGlue = Box.createHorizontalGlue();
        horizontalGlue.setMinimumSize(new Dimension(150, 0));
        panelSuperior.add(horizontalGlue);

        JButton btnNewButton_4 = new JButton("Premium");
        btnNewButton_4.setIcon(new ImageIcon(VentanaMain.class.getResource("/imagen/calidad-premium.png")));
        panelSuperior.add(btnNewButton_4);

        JLabel imagenPerfilLabel = new JLabel("IMAGEN");
        imagenPerfilLabel.setIcon(new ImageIcon(VentanaMain.class.getResource("/imagen/contrasena.png")));
        panelSuperior.add(imagenPerfilLabel);

        JPanel conversaciones = new JPanel();
        GridBagConstraints gbc_conversaciones = new GridBagConstraints();
        gbc_conversaciones.gridheight = 2;
        gbc_conversaciones.insets = new Insets(0, 0, 0, 5);
        gbc_conversaciones.fill = GridBagConstraints.BOTH;
        gbc_conversaciones.gridx = 1;
        gbc_conversaciones.gridy = 1;
        contentPane.add(conversaciones, gbc_conversaciones);
        conversaciones.setLayout(new BorderLayout(0, 0));

        // Cambiar el modelo del JList para usar objetos Mensaje
        DefaultListModel<Mensaje> mensajeModel = new DefaultListModel<>();
        mensajeModel.addElement(new Mensaje("Alice", "Bob", "Hola Bob, ¿cómo estás?"));
        mensajeModel.addElement(new Mensaje("Bob", "Alice", "¡Hola Alice! Todo bien, ¿y tú?"));
        mensajeModel.addElement(new Mensaje("Charlie", "Alice", "¿Listo para la reunión?"));

        list = new JList<>(mensajeModel); // JList ahora usa el modelo de Mensaje
        list.setCellRenderer(new MensajeCellRenderer()); // Configurar el renderizador personalizado
        conversaciones.add(list, BorderLayout.CENTER);

        JScrollBar scrollBar = new JScrollBar();
        conversaciones.add(scrollBar, BorderLayout.EAST);

        JPanel chat = new JPanel();
        chat.setBackground(new Color(202, 253, 202));
        GridBagConstraints gbc_chat = new GridBagConstraints();
        gbc_chat.gridwidth = 2;
        gbc_chat.insets = new Insets(0, 0, 5, 5);
        gbc_chat.fill = GridBagConstraints.BOTH;
        gbc_chat.gridx = 3;
        gbc_chat.gridy = 1;
        contentPane.add(chat, gbc_chat);
        chat.setLayout(new BoxLayout(chat, BoxLayout.Y_AXIS));
        
		chat.setSize(400,700);
		BubbleText burbuja;
		burbuja=new BubbleText(chat,"Hola grupo!!", Color.GREEN, "J.Ramón", BubbleText.SENT);
		chat.add(burbuja);
		BubbleText burbuja2;
		burbuja2=new BubbleText(chat,
		"Hola, ¿Está seguro de que la burbuja usa varias lineas si es necesario?",
		Color.LIGHT_GRAY, "Alumno", BubbleText.RECEIVED);
		chat.add(burbuja2);
		BubbleText burbuja3;
		burbuja3=new BubbleText(chat,"No estoy seguro",  
		Color.GREEN, "J.Ramón", BubbleText.SENT, 24);
		chat.add(burbuja3);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 3;
		gbc_panel.gridy = 2;
		contentPane.add(panel, gbc_panel);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		textField = new JTextField();
		panel.add(textField);
		textField.setColumns(10);
		
		JButton btnSend = new JButton("");
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.insets = new Insets(0, 0, 0, 5);
		gbc_btnSend.gridx = 4;
		gbc_btnSend.gridy = 2;
		contentPane.add(btnSend, gbc_btnSend);
		btnSend.setIcon(new ImageIcon(VentanaMain.class.getResource("/imagen/enviar-button.png")));
    }
}
