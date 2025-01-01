package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.net.URL;
import java.util.Date;

import controlador.Controlador;

public class VentanaPerfil extends JFrame {

    private JPanel contentPane;
    private JLabel lblNombre, lblApellidos, lblTelefono, lblSaludo, lblFecha, lblImage;
    private JTextField nameField, apellidosField, telefonoField, saludoField, urlField;
    private JDateChooser dateChooser;
    private JButton btnUpdateImage;

    public VentanaPerfil() {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setBounds(100, 100, 733, 405);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new GridBagLayout());

        // Crear un objeto GridBagConstraints por cada componente
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Nombre
        lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Tahoma", Font.BOLD, 16));
        GridBagConstraints gbcNombre = new GridBagConstraints();
        gbcNombre.gridx = 0;
        gbcNombre.gridy = 0;
        contentPane.add(lblNombre, gbcNombre);

        nameField = new JTextField(20);
        nameField.setEditable(false);
        GridBagConstraints gbcNameField = new GridBagConstraints();
        gbcNameField.gridx = 1;
        gbcNameField.gridy = 0;
        contentPane.add(nameField, gbcNameField);

        // Teléfono
        lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setFont(new Font("Tahoma", Font.BOLD, 16));
        GridBagConstraints gbcTelefono = new GridBagConstraints();
        gbcTelefono.gridx = 0;
        gbcTelefono.gridy = 1;  // Cambiar la fila
        contentPane.add(lblTelefono, gbcTelefono);

        telefonoField = new JTextField(20);
        telefonoField.setEditable(false);
        GridBagConstraints gbcTelefonoField = new GridBagConstraints();
        gbcTelefonoField.gridx = 1;
        gbcTelefonoField.gridy = 1;  // Cambiar la fila
        contentPane.add(telefonoField, gbcTelefonoField);

        // Saludo
        lblSaludo = new JLabel("Saludo:");
        lblSaludo.setFont(new Font("Tahoma", Font.BOLD, 16));
        GridBagConstraints gbcSaludo = new GridBagConstraints();
        gbcSaludo.gridx = 0;
        gbcSaludo.gridy = 2;  // Cambiar la fila
        contentPane.add(lblSaludo, gbcSaludo);

        saludoField = new JTextField(20);
        saludoField.setEditable(false);
        GridBagConstraints gbcSaludoField = new GridBagConstraints();
        gbcSaludoField.gridx = 1;
        gbcSaludoField.gridy = 2;  // Cambiar la fila
        contentPane.add(saludoField, gbcSaludoField);

        // Fecha de nacimiento
        lblFecha = new JLabel("Fecha de Nacimiento:");
        lblFecha.setFont(new Font("Tahoma", Font.BOLD, 16));
        GridBagConstraints gbcFecha = new GridBagConstraints();
        gbcFecha.gridx = 0;
        gbcFecha.gridy = 3;  // Cambiar la fila
        contentPane.add(lblFecha, gbcFecha);

        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd-MM-yyyy");
        dateChooser.setEnabled(false); // Deshabilitar la edición
        GridBagConstraints gbcDateChooser = new GridBagConstraints();
        gbcDateChooser.gridx = 1;
        gbcDateChooser.gridy = 3;  // Cambiar la fila
        contentPane.add(dateChooser, gbcDateChooser);

        // Imagen
        lblImage = new JLabel();
        lblImage.setPreferredSize(new Dimension(128, 128));
        setDefaultImage(lblImage);
        GridBagConstraints gbcImage = new GridBagConstraints();
        gbcImage.gridx = 2;
        gbcImage.gridy = 0;
        gbcImage.gridheight = 5;
        contentPane.add(lblImage, gbcImage);

        // Campo para URL de la imagen
        urlField = new JTextField(20);
        GridBagConstraints gbcUrlField = new GridBagConstraints();
        gbcUrlField.gridx = 3;
        gbcUrlField.gridy = 0;
        contentPane.add(urlField, gbcUrlField);

        // Botón para actualizar la imagen
        btnUpdateImage = new JButton("Actualizar Imagen");
        btnUpdateImage.addActionListener(e -> updateImageFromURL(lblImage));
        GridBagConstraints gbcBtnUpdateImage = new GridBagConstraints();
        gbcBtnUpdateImage.gridx = 3;
        gbcBtnUpdateImage.gridy = 1;
        contentPane.add(btnUpdateImage, gbcBtnUpdateImage);

        // Cargar datos del usuario
        cargarDatosUsuario();
    }

    private void cargarDatosUsuario() {
        // Aquí debes obtener los datos del usuario desde el controlador
        String usuario = Controlador.INSTANCE.getUsuarioActual().getUsuario();
        String telefono = Controlador.INSTANCE.getUsuarioActual().getTelefono();
        String saludo = Controlador.INSTANCE.getUsuarioActual().getSaludo();
        Date fechaNacimiento = Controlador.INSTANCE.getUsuarioActual().getFechaNacimiento();
        String urlImagen = Controlador.INSTANCE.getUsuarioActual().getImagen();

        nameField.setText(usuario);
        telefonoField.setText(telefono);
        saludoField.setText(saludo);
        dateChooser.setDate(fechaNacimiento);
        urlField.setText(urlImagen);

        if (urlImagen != null && !urlImagen.isEmpty()) {
            updateImageFromURL(lblImage); // Actualiza la imagen si hay una URL
        } else {
            setDefaultImage(lblImage); // Usa la imagen por defecto si no hay URL
        }
    }

    private void setDefaultImage(JLabel lblImage) {
        ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/imagen/user.png"));
        lblImage.setIcon(resizeImageIcon(defaultIcon, 128, 128)); // Redimensiona la imagen
    }

    private void updateImageFromURL(JLabel lblImage) {
        String urlText = urlField.getText().trim();
        if (!urlText.isEmpty()) {
            try {
                URL imageUrl = new URL(urlText);
                ImageIcon icon = new ImageIcon(imageUrl);
                lblImage.setIcon(resizeImageIcon(icon, 128, 128));
                Controlador.INSTANCE.cambiarImagenUsuario(urlText);
            } catch (Exception ex) {
            }
        }
        // si no hay url no se cambia
    }

    private ImageIcon resizeImageIcon(ImageIcon icon, int width, int height) {
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}
