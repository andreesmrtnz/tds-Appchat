package vista;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controlador.Controlador;
import modelo.ContactoIndividual;
import modelo.Mensaje;

public class VentanaBuscar extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private List<ContactoIndividual> misContactos;
    private JTextField textFieldTelefono;
    private JTextField textFieldTexto;
    private DefaultListModel<Mensaje> mensajeModel;
    private JPanel panelMensajes;
    private JList<Mensaje> list;

    /**
     * Create the frame.
     */
    public VentanaBuscar() {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setBounds(100, 100, 606, 462);

        misContactos = Controlador.INSTANCE.getUsuarioActual().getContactosIndividuales();

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[] { 0, 66, 40, 71, 0, 0, 10, 0 };
        gbl_contentPane.rowHeights = new int[] { 62, 64, 10, 0, 10, 0 };
        gbl_contentPane.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE };
        gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
        contentPane.setLayout(gbl_contentPane);

        JLabel lblContacto = new JLabel("Contacto:");
        GridBagConstraints gbc_lblContacto = new GridBagConstraints();
        gbc_lblContacto.anchor = GridBagConstraints.EAST;
        gbc_lblContacto.insets = new Insets(0, 0, 5, 5);
        gbc_lblContacto.gridx = 1;
        gbc_lblContacto.gridy = 0;
        contentPane.add(lblContacto, gbc_lblContacto);

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("Todos");
        misContactos.forEach(c -> model.addElement(c.getNombre()));

        JComboBox<String> comboBox = new JComboBox<>(model);
        GridBagConstraints gbc_comboBox = new GridBagConstraints();
        gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_comboBox.insets = new Insets(0, 0, 5, 5);
        gbc_comboBox.gridx = 2;
        gbc_comboBox.gridy = 0;
        contentPane.add(comboBox, gbc_comboBox);

        JLabel lblTelefono = new JLabel("Telefono:");
        GridBagConstraints gbc_lblTelefono = new GridBagConstraints();
        gbc_lblTelefono.anchor = GridBagConstraints.EAST;
        gbc_lblTelefono.insets = new Insets(0, 0, 5, 5);
        gbc_lblTelefono.gridx = 3;
        gbc_lblTelefono.gridy = 0;
        contentPane.add(lblTelefono, gbc_lblTelefono);

        textFieldTelefono = new JTextField();
        GridBagConstraints gbc_textFieldTelefono = new GridBagConstraints();
        gbc_textFieldTelefono.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldTelefono.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldTelefono.gridx = 4;
        gbc_textFieldTelefono.gridy = 0;
        contentPane.add(textFieldTelefono, gbc_textFieldTelefono);
        textFieldTelefono.setColumns(10);

        JButton btnBuscar = new JButton("Buscar");
        GridBagConstraints gbc_btnBuscar = new GridBagConstraints();
        gbc_btnBuscar.insets = new Insets(0, 0, 5, 5);
        gbc_btnBuscar.gridx = 5;
        gbc_btnBuscar.gridy = 0;
        contentPane.add(btnBuscar, gbc_btnBuscar);

        JLabel lblTexto = new JLabel("Texto:");
        GridBagConstraints gbc_lblTexto = new GridBagConstraints();
        gbc_lblTexto.anchor = GridBagConstraints.EAST;
        gbc_lblTexto.insets = new Insets(0, 0, 5, 5);
        gbc_lblTexto.gridx = 1;
        gbc_lblTexto.gridy = 1;
        contentPane.add(lblTexto, gbc_lblTexto);

        textFieldTexto = new JTextField();
        GridBagConstraints gbc_textFieldTexto = new GridBagConstraints();
        gbc_textFieldTexto.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldTexto.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldTexto.gridx = 2;
        gbc_textFieldTexto.gridy = 1;
        gbc_textFieldTexto.gridwidth = 4;
        contentPane.add(textFieldTexto, gbc_textFieldTexto);

        // Panel para mostrar los mensajes
        panelMensajes = new JPanel(new BorderLayout());
        GridBagConstraints gbc_panelMensajes = new GridBagConstraints();
        gbc_panelMensajes.insets = new Insets(0, 0, 5, 5);
        gbc_panelMensajes.fill = GridBagConstraints.BOTH;
        gbc_panelMensajes.gridwidth = 5;
        gbc_panelMensajes.gridx = 1;
        gbc_panelMensajes.gridy = 3;
        contentPane.add(panelMensajes, gbc_panelMensajes);

        // Modelo de la lista de mensajes
        mensajeModel = new DefaultListModel<>();
        list = new JList<>(mensajeModel);
        list.setCellRenderer(new MensajeCellRenderer()); // Renderizador personalizado
        JScrollPane scrollPane = new JScrollPane(list);
        panelMensajes.add(scrollPane, BorderLayout.CENTER);

        // Acción del botón "Buscar"
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mensajeModel.clear(); // Limpiar los mensajes anteriores

                String contacto = comboBox.getSelectedItem().toString();
                String telefono = textFieldTelefono.getText();
                String texto = textFieldTexto.getText();

                // Obtener mensajes desde el controlador
                List<Mensaje> mensajes = Controlador.INSTANCE.buscarMensajes(contacto, telefono, texto);

                // Agregar los mensajes al modelo
                mensajes.forEach(mensajeModel::addElement);
            }
        });
    }
}
