package vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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

import com.toedter.calendar.JDateChooser;

import controlador.Controlador;
import modelo.ContactoIndividual;
import modelo.Mensaje;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.awt.event.ActionEvent;

public class VentanaBuscar extends JFrame {

	private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private List<ContactoIndividual> misContactos;
    private JList<Mensaje> list; 
    private JTextField textField_1;

    /**
     * Create the frame.
     */
    public VentanaBuscar() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 606, 462);
        
        misContactos = Controlador.INSTANCE.getUsuarioActual().getContactosIndividuales();
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{0, 66, 40, 71, 0, 0, 10, 0};
        gbl_contentPane.rowHeights = new int[]{62, 64, 10, 0, 10, 0};
        gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
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
        misContactos.stream().forEach(c -> model.addElement(c.getNombre()));
        
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
        
        
        
        
        
        JButton btnNewButton = new JButton("Buscar");
        
        
        textField_1 = new JTextField();
        GridBagConstraints gbc_textField_1 = new GridBagConstraints();
        gbc_textField_1.insets = new Insets(0, 0, 5, 5);
        gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField_1.gridx = 4;
        gbc_textField_1.gridy = 0;
        contentPane.add(textField_1, gbc_textField_1);
        textField_1.setColumns(10);
        
        GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
        gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
        gbc_btnNewButton.gridx = 5;
        gbc_btnNewButton.gridy = 0;
        contentPane.add(btnNewButton, gbc_btnNewButton);
        
        JLabel lblText = new JLabel("Texto:");
        GridBagConstraints gbc_lblText = new GridBagConstraints();
        gbc_lblText.insets = new Insets(0, 0, 5, 5);
        gbc_lblText.gridx = 1;
        gbc_lblText.gridy = 1;
        contentPane.add(lblText, gbc_lblText);
        
        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridwidth = 4;
        gbc_scrollPane.gridx = 2;
        gbc_scrollPane.gridy = 1;
        contentPane.add(scrollPane, gbc_scrollPane);
        
        JTextField textField = new JTextField();
        scrollPane.setViewportView(textField);
        
        JPanel panelMensajes = new JPanel();
        GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
        gbc_scrollPane_1.insets = new Insets(0, 0, 5, 5);
        gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
        gbc_scrollPane_1.gridwidth = 5;
        gbc_scrollPane_1.gridx = 1;
        gbc_scrollPane_1.gridy = 3;
        contentPane.add(panelMensajes, gbc_scrollPane_1);
        
        DefaultListModel<Mensaje> mensajeModel = new DefaultListModel<>();
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		//vacio lo que se haya podido buscar antes
        		mensajeModel.clear();
        		list = new JList<>(mensajeModel); // JList ahora usa el modelo de Mensaje
				list.setCellRenderer(new MensajeCellRenderer()); // Configurar el renderizador personalizado
				panelMensajes.add(list, BorderLayout.CENTER);
				
        		String contactoMensaje = comboBox.getSelectedItem().toString();
        		String telefonoMensaje = textField_1.getText();
        		String textoMensaje = textField.getText();
				List<Mensaje> mensajes = Controlador.INSTANCE.buscarMensajes(contactoMensaje, telefonoMensaje, textoMensaje);
				
				//lo muestro con mensajes cell renderer
				mensajeModel.addAll(mensajes);
				list = new JList<>(mensajeModel); // JList ahora usa el modelo de Mensaje
				list.setCellRenderer(new MensajeCellRenderer()); // Configurar el renderizador personalizado
				panelMensajes.add(list, BorderLayout.CENTER);
        	}
        });
    }

}
