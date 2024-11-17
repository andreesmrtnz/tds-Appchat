package vista;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import java.awt.Insets;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JPasswordField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import com.toedter.calendar.JDateChooser;

import controlador.Controlador;

import javax.swing.border.MatteBorder;
import java.awt.event.ActionListener;
import java.util.Date;
import java.awt.event.ActionEvent;

public class VentanaRegister extends JFrame {

	private JPanel contentPane;
	private JTextField nameField;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JTextField saludoField;
	private JDateChooser dateChooser; // Agrega JDateChooser para la selección de fechas
	private JTextField apellidosField;
	private JTextField telefonoField;

	/**
	 * Create the frame.
	 */
	public VentanaRegister() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1024, 626);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 46, 120, 125, 125, 250, 190, 57, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 40, 40, 40, 40, 40, 261, 52, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JLabel lblNewLabel = new JLabel("nombre:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.fill = GridBagConstraints.VERTICAL;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 1;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);

		nameField = new JTextField();
		nameField.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		GridBagConstraints gbc_nameField = new GridBagConstraints();
		gbc_nameField.gridwidth = 4;
		gbc_nameField.insets = new Insets(0, 0, 5, 5);
		gbc_nameField.fill = GridBagConstraints.BOTH;
		gbc_nameField.gridx = 2;
		gbc_nameField.gridy = 1;
		contentPane.add(nameField, gbc_nameField);
		nameField.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("apellidos:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 20));
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.fill = GridBagConstraints.VERTICAL;
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 2;
		contentPane.add(lblNewLabel_1, gbc_lblNewLabel_1);

		apellidosField = new JTextField();
		apellidosField.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		GridBagConstraints gbc_apellidosField = new GridBagConstraints();
		gbc_apellidosField.gridwidth = 4;
		gbc_apellidosField.insets = new Insets(0, 0, 5, 5);
		gbc_apellidosField.fill = GridBagConstraints.BOTH;
		gbc_apellidosField.gridx = 2;
		gbc_apellidosField.gridy = 2;
		contentPane.add(apellidosField, gbc_apellidosField);
		apellidosField.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("telefono:");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 20));
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.fill = GridBagConstraints.VERTICAL;
		gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 1;
		gbc_lblNewLabel_2.gridy = 3;
		contentPane.add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		telefonoField = new JTextField();
		telefonoField.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		GridBagConstraints gbc_telefonoField = new GridBagConstraints();
		gbc_telefonoField.gridwidth = 2;
		gbc_telefonoField.insets = new Insets(0, 0, 5, 5);
		gbc_telefonoField.fill = GridBagConstraints.BOTH;
		gbc_telefonoField.gridx = 2;
		gbc_telefonoField.gridy = 3;
		contentPane.add(telefonoField, gbc_telefonoField);
		telefonoField.setColumns(10);

		JLabel lblNewLabel_3 = new JLabel("contraseña:");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 20));
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.fill = GridBagConstraints.VERTICAL;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 1;
		gbc_lblNewLabel_3.gridy = 4;
		contentPane.add(lblNewLabel_3, gbc_lblNewLabel_3);

		passwordField = new JPasswordField();
		passwordField.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.gridwidth = 2;
		gbc_passwordField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField.fill = GridBagConstraints.BOTH;
		gbc_passwordField.gridx = 2;
		gbc_passwordField.gridy = 4;
		contentPane.add(passwordField, gbc_passwordField);

		JLabel lblNewLabel_4 = new JLabel("confirmar contraseña:");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 20));
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.fill = GridBagConstraints.VERTICAL;
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.gridx = 4;
		gbc_lblNewLabel_4.gridy = 4;
		contentPane.add(lblNewLabel_4, gbc_lblNewLabel_4);

		passwordField_1 = new JPasswordField();
		passwordField_1.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		GridBagConstraints gbc_passwordField_1 = new GridBagConstraints();
		gbc_passwordField_1.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField_1.fill = GridBagConstraints.BOTH;
		gbc_passwordField_1.gridx = 5;
		gbc_passwordField_1.gridy = 4;
		contentPane.add(passwordField_1, gbc_passwordField_1);

		JLabel lblNewLabel_5 = new JLabel("fecha:");
		lblNewLabel_5.setFont(new Font("Tahoma", Font.BOLD, 20));
		GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
		gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_5.gridx = 1;
		gbc_lblNewLabel_5.gridy = 5;
		contentPane.add(lblNewLabel_5, gbc_lblNewLabel_5);

		// Usa JDateChooser para el selector de fechas
		dateChooser = new JDateChooser();
		dateChooser.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		dateChooser.setDateFormatString("dd-MM-yyyy"); // Establece el formato de fecha deseado
		GridBagConstraints gbc_dateChooser = new GridBagConstraints();
		gbc_dateChooser.gridwidth = 2;
		gbc_dateChooser.insets = new Insets(0, 0, 5, 5);
		gbc_dateChooser.fill = GridBagConstraints.BOTH;
		gbc_dateChooser.gridx = 2;
		gbc_dateChooser.gridy = 5;
		contentPane.add(dateChooser, gbc_dateChooser);

		JLabel lblNewLabel_6 = new JLabel("saludo:");
		lblNewLabel_6.setFont(new Font("Tahoma", Font.BOLD, 20));
		GridBagConstraints gbc_lblNewLabel_6 = new GridBagConstraints();
		gbc_lblNewLabel_6.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_6.gridx = 1;
		gbc_lblNewLabel_6.gridy = 6;
		contentPane.add(lblNewLabel_6, gbc_lblNewLabel_6);

		saludoField = new JTextField();
		saludoField.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		GridBagConstraints gbc_saludoField = new GridBagConstraints();
		gbc_saludoField.gridwidth = 2;
		gbc_saludoField.insets = new Insets(0, 0, 5, 5);
		gbc_saludoField.fill = GridBagConstraints.BOTH;
		gbc_saludoField.gridx = 2;
		gbc_saludoField.gridy = 6;
		contentPane.add(saludoField, gbc_saludoField);
		saludoField.setColumns(10);

		JLabel lblNewLabel_7 = new JLabel("imagen:");
		lblNewLabel_7.setFont(new Font("Tahoma", Font.BOLD, 20));
		GridBagConstraints gbc_lblNewLabel_7 = new GridBagConstraints();
		gbc_lblNewLabel_7.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_7.gridx = 4;
		gbc_lblNewLabel_7.gridy = 6;
		contentPane.add(lblNewLabel_7, gbc_lblNewLabel_7);

		JLabel lblimage = new JLabel("");
		lblimage.setIcon(new ImageIcon(VentanaRegister.class.getResource("/imagen/user.png")));
		GridBagConstraints gbc_lblimage = new GridBagConstraints();
		gbc_lblimage.insets = new Insets(0, 0, 5, 5);
		gbc_lblimage.gridx = 5;
		gbc_lblimage.gridy = 6;
		contentPane.add(lblimage, gbc_lblimage);

		JButton cancelButton = new JButton("CANCELAR");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaRegister.this.setVisible(false);
				register();

			}
		});
		cancelButton.setBackground(new Color(254, 190, 190));
		cancelButton.setBorderPainted(false);
		cancelButton.setFont(new Font("Tahoma", Font.BOLD, 20));
		cancelButton.setMargin(new Insets(10, 20, 10, 20));
		GridBagConstraints gbc_cancelButton = new GridBagConstraints();
		gbc_cancelButton.insets = new Insets(0, 0, 5, 5);
		gbc_cancelButton.gridx = 2;
		gbc_cancelButton.gridy = 7;
		contentPane.add(cancelButton, gbc_cancelButton);

		JButton aceptarButton = new JButton("ACEPTAR");
		aceptarButton.setBackground(new Color(163, 252, 187));
		aceptarButton.setBorderPainted(false);
		aceptarButton.setFont(new Font("Tahoma", Font.BOLD, 20));
		aceptarButton.setMargin(new Insets(10, 20, 10, 20));
		GridBagConstraints gbc_aceptarButton = new GridBagConstraints();
		gbc_aceptarButton.insets = new Insets(0, 0, 5, 5);
		gbc_aceptarButton.gridx = 3;
		gbc_aceptarButton.gridy = 7;
		contentPane.add(aceptarButton, gbc_aceptarButton);
	}

	public void register() {
		String usuario = nameField.getText() + " " + apellidosField.getText();
		String telefono = telefonoField.getText();
		char[] password = passwordField.getPassword();
		Date fecha = dateChooser.getDate();
		String saludo = saludoField.getText();
		
		Controlador.INSTANCE.doRegister(usuario, telefono, password, saludo, fecha);

	}

}
