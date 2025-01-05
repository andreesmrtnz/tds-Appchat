package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import controlador.Controlador;
import modelo.Contacto;
import modelo.ContactoIndividual;
import modelo.Grupo;
import modelo.Mensaje;
import modelo.Usuario;

public class MensajeCellRenderer extends JPanel implements ListCellRenderer<Mensaje> {
    private JLabel userLabel;
    private JLabel imageLabel;
    private JTextField messageText;
    private JButton addButton;

    public MensajeCellRenderer() {
        setLayout(new BorderLayout(5, 5));

        userLabel = new JLabel();
        imageLabel = new JLabel();
        messageText = new JTextField();
        addButton = new JButton("+");

        addButton.addActionListener(e -> {
            JFrame ventanaAdd = new AlertaAgregarContacto();
            ventanaAdd.setVisible(true);
        });

        // Evitar que el clic en el botón seleccione la celda
        addButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                e.consume(); // Consume el evento para que no se propague al JList
            }
        });



        // Agrega la imagen a la izquierda
        add(imageLabel, BorderLayout.WEST);

        // Panel para el nombre del usuario y el mensaje
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(userLabel);

        // Configuración de campo de texto para mostrar mensaje
        messageText.setEditable(false);
        panel.add(messageText);

        add(panel, BorderLayout.CENTER);

        // Agregar el botón "+" a la derecha
        add(addButton, BorderLayout.EAST);
    }
    
    public boolean isAddButtonClicked(Point point) {
        // Obtener las coordenadas relativas del botón "+"
        Rectangle buttonBounds = addButton.getBounds();
        return buttonBounds.contains(point);
    }



    public Component getListCellRendererComponent(JList<? extends Mensaje> list, Mensaje mensaje, int index,
            boolean isSelected, boolean cellHasFocus) {
        // Obtener el usuario actual desde el controlador
        Usuario usuarioActual = Controlador.INSTANCE.getUsuarioActual();

        // Determinar el contacto con el que estás hablando
        String imageUrlText = null;
        boolean showAddButton = false;

        if (mensaje.getReceptor() instanceof ContactoIndividual) {
            ContactoIndividual receptor = (ContactoIndividual) mensaje.getReceptor();
            if (!receptor.esUsuario(usuarioActual)) {
                imageUrlText = receptor.getUsuario().getImagen(); // Imagen del receptor si no es el usuario actual
            } else if (!mensaje.getEmisor().equals(usuarioActual)) {
                imageUrlText = mensaje.getEmisor().getImagen(); // Imagen del emisor si no es el usuario actual
            }
            userLabel.setText("De: " + mensaje.getEmisor().getUsuario() + " a: " + receptor.getNombre());

            // Mostrar el botón "+" si el nombre del contacto es igual a su número
            showAddButton = receptor.getNombre().equals(receptor.getMovil());
        } else if (mensaje.getReceptor() instanceof Grupo) {
            setDefaultGroupImage();
        }

        messageText.setText(mensaje.getTexto());

        // Mostrar la imagen del contacto con el que estás hablando
        try {
            if (imageUrlText != null && !imageUrlText.isEmpty()) {
                URL imageUrl = new URL(imageUrlText);
                Image image = ImageIO.read(imageUrl);
                ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
                imageLabel.setIcon(imageIcon);
            } else {
                setDefaultProfileImage(); // Si no hay imagen, usar una imagen por defecto
            }
        } catch (IOException e) {
            e.printStackTrace();
            setDefaultProfileImage(); // Si falla, usar una imagen por defecto
        }

        // Mostrar u ocultar el botón "+" según la condición
        addButton.setVisible(showAddButton);

        // Configuración de colores de fondo y texto según selección
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        return this;
    }

    // Método para configurar una imagen de perfil por defecto
    private void setDefaultProfileImage() {
        ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/imagen/user.png")); // Ruta a la imagen por
                                                                                          // defecto
        imageLabel.setIcon(new ImageIcon(defaultIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
    }

    private void setDefaultGroupImage() {
        ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/imagen/grupo.png")); // Ruta a la imagen por
                                                                                           // defecto
        imageLabel.setIcon(new ImageIcon(defaultIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
    }
}
