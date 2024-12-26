package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import modelo.Mensaje;

public class MensajeCellRenderer extends JPanel implements ListCellRenderer<Mensaje> {
    private JLabel userLabel;
    private JLabel imageLabel;
    private JTextField messageText;

    public MensajeCellRenderer() {
        setLayout(new BorderLayout(5, 5));

        userLabel = new JLabel();
        imageLabel = new JLabel();
        messageText = new JTextField();

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
    }

    public Component getListCellRendererComponent(JList<? extends Mensaje> list, Mensaje mensaje, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        // Configura el nombre del usuario y el texto del mensaje
    	if (mensaje == null) {
            userLabel.setText("Mensaje no disponible");
            return this;
        }
        userLabel.setText("De: " + mensaje.getEmisor().getUsuario() + " a: " + mensaje.getReceptor().getNombre());
        messageText.setText(mensaje.getTexto());

        // Carga una imagen de ejemplo con base en el emisor
        try {
            URL imageUrl = new URL("https://robohash.org/" + mensaje.getEmisor() + "?size=50x50");
            Image image = ImageIO.read(imageUrl);
            ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
            imageLabel.setIcon(imageIcon);
        } catch (IOException e) {
            e.printStackTrace();
            imageLabel.setIcon(null); // Si falla, no muestra ninguna imagen
        }

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
}
