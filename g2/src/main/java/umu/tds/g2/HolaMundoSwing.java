package umu.tds.g2;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Color;
import java.awt.Component;
import javax.swing.SwingConstants;
import javax.swing.JPanel;

public class HolaMundoSwing {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		JFrame ventana = new JFrame();
		
		ventana.setTitle("Hola Mundo");
		ventana.setVisible(true);
		ventana.setSize(947, 489);
		ventana.setLocation(500,200);
		
		JLabel lblNewLabel = new JLabel("AppChat");
		lblNewLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		lblNewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblNewLabel.setForeground(new Color(0, 191, 255));
		lblNewLabel.setFont(new Font("Sitka Small", Font.BOLD | Font.ITALIC, 36));
		ventana.getContentPane().add(lblNewLabel, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		ventana.getContentPane().add(panel, BorderLayout.SOUTH);
		
		JButton loginButton = new JButton("Login");
		panel.add(loginButton);
		
		JButton registerButton = new JButton("Register");
		panel.add(registerButton);
		
		
		
	}

}
