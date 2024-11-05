package vista;

import java.awt.BorderLayout;
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

public class MensajeCellRenderer extends JPanel 
		implements ListCellRenderer<Mensaje> {
	private JLabel userLabel;
	private JLabel imageLabel;
	private JTextField messageText;

	public MensajeCellRenderer() {
		setLayout(new BorderLayout(5, 5));

		userLabel = new JLabel();
		imageLabel = new JLabel();
		messageText = new JTextField();

		add(imageLabel, BorderLayout.WEST);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		add(messageText);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Mensaje> list, Mensaje mensaje, int index,
			boolean isSelected, boolean cellHasFocus) {
		// Set the text
		nameLabel.setText(mensaje.toString());

		// Load the image from a random URL (for example, using "https://robohash.org")
		try {
			URL imageUrl = new URL("https://robohash.org/" + (mensaje.getEmisor() != null?)+ "?size=50x50");
			Image image = ImageIO.read(imageUrl);
			ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
			imageLabel.setIcon(imageIcon);
		} catch (IOException e) {
			e.printStackTrace();
			imageLabel.setIcon(null); // Default to no image if there was an issue
		}

		// Set background and foreground based on selection
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