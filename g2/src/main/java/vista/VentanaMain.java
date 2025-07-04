package vista;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.net.URL;
import java.util.List;
import java.util.Optional;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import controlador.Controlador;
import modelo.Contacto;
import modelo.ContactoIndividual;
import modelo.Descuento;
import modelo.DescuentoFecha;
import modelo.DescuentoMensaje;
import modelo.Mensaje;
import tds.BubbleText;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

public class VentanaMain extends JFrame implements Observer {

    private JComboBox<String> comboBox;
	private JPanel contentPane;
	private JList<Mensaje> list; // Cambiar tipo a JList<Mensaje>
	private JTextField textField;
	private Controlador controlador;

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
	
	

	private void loadChat(Optional<Contacto> contact, JPanel chatPanel) {
	    if (contact == null || chatPanel == null) {
	        return; // Validación para evitar errores por parámetros nulos
	    }
	    Contacto contacto = contact.get();

	    // Limpiar el panel antes de agregar nuevos mensajes
	    chatPanel.removeAll();
	    chatPanel.setBackground(new Color(202, 253, 202)); // Color del fondo del chat
	    chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));

	    // Añadir mensajes al panel
	    controlador.getMensajes(contacto).stream().map(m -> {
			String emisor;
			int direccionMensaje;
			Color colorBurbuja;

			if (m.getEmisor().equals(controlador.getUsuarioActual())) {
				colorBurbuja = Color.GREEN;
				emisor = "Tú";
				direccionMensaje = BubbleText.SENT;
			} else {
				colorBurbuja = Color.YELLOW;
				emisor = m.getEmisor().getUsuario();
				direccionMensaje = BubbleText.RECEIVED;
			}

			if (m.getTexto().isEmpty()) {
				return new BubbleText(chatPanel, m.getEmoticono(), colorBurbuja, emisor, direccionMensaje, 12);
			}
			return new BubbleText(chatPanel, m.getTexto(), colorBurbuja, emisor, direccionMensaje, 12);
		}).forEach(b -> chatPanel.add(b));
	    Controlador.INSTANCE.setChatActual(contacto);

	    // Refrescar el panel para mostrar los nuevos mensajes
	    chatPanel.revalidate();
	    chatPanel.repaint();
	}



	/**
	 * Create the frame.
	 */
	public VentanaMain() {
		controlador = Controlador.INSTANCE;
		controlador.agregarObserver(this);  // Registra esta ventana como observadora
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1141, 485);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 412, 0, 598, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 364, 30, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
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

		comboBox = new JComboBox<>();
		cargarComboBox(comboBox);
		panelSuperior.add(comboBox);

		JButton enviarBarraButton = new JButton("");
		enviarBarraButton.setIcon(new ImageIcon(VentanaMain.class.getResource("/imagen/send.png")));
		panelSuperior.add(enviarBarraButton);
		

		JButton searchButton = new JButton("");
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame ventanaBuscar = new VentanaBuscar();
				ventanaBuscar.setVisible(true);
			}
		});
		searchButton.setIcon(new ImageIcon(VentanaMain.class.getResource("/imagen/search-engine.png")));
		panelSuperior.add(searchButton);

		JButton contactosButton = new JButton("Contactos");
		contactosButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame ventanaContactos = new VentanaContactos();
				ventanaContactos.setVisible(true);
			}
		});
		contactosButton.setIcon(new ImageIcon(VentanaMain.class.getResource("/imagen/grupo.png")));
		panelSuperior.add(contactosButton);

		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalGlue.setMinimumSize(new Dimension(150, 0));
		panelSuperior.add(horizontalGlue);

		JButton btnPremium = new JButton("Premium");
		btnPremium.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        if (controlador.activarPremium()) {
		            // Obtener el precio con descuento
		            double precio = controlador.getPrecio();
		            
		            // Obtener el descuento aplicado
		            Optional<Descuento> descuentoAplicado = controlador.getDescuento();
		            
		            // Crear el mensaje a mostrar
		            String mensaje = "¡Felicidades! Ahora eres usuario premium.\n";
		            mensaje += "Has pagado: " + String.format("%.2f", precio) + "€.\n";
		            
		            if (descuentoAplicado.isPresent()) {
		                // Si hay un descuento, mostrarlo
		            	if (descuentoAplicado.get() instanceof DescuentoFecha) {
		            		mensaje += "Se te ha aplicado un descuento de por tu fecha de registro.\n";
		            	}
		            	else if (descuentoAplicado.get() instanceof DescuentoMensaje) {
		            		mensaje += "Se te ha aplicado un descuento de por tu cantidad de mensajes.\n";
		            	}
		                
		            }
		            
		            // Mostrar el mensaje en un JOptionPane
		            JOptionPane.showMessageDialog(null, mensaje, "Premium Activado", JOptionPane.INFORMATION_MESSAGE);
		        } else {
		            // Si ya es premium, mostrar un mensaje de error
		            JOptionPane.showMessageDialog(null, "Ya eres un usuario premium.", "Error", JOptionPane.ERROR_MESSAGE);
		        }
		    }
		});

		
		JButton btnPdf = new JButton("Pdf");
		btnPdf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (controlador.puedeExportarPDF()) {
					JFrame ventanaPDF = new VentanaExportarPDF();
					ventanaPDF.setVisible(true);
				}
				else {
					JOptionPane.showMessageDialog(null, "No tienes disponible esta funcionalidad, hazte PREMIUM.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		btnPdf.setIcon(new ImageIcon(VentanaMain.class.getResource("/imagen/pdf.png")));
		panelSuperior.add(btnPdf);

		btnPremium.setIcon(new ImageIcon(VentanaMain.class.getResource("/imagen/premium.png")));
		panelSuperior.add(btnPremium);

		JLabel imagenPerfilLabel = new JLabel(controlador.getUsuarioActual().getUsuario());
		cargarImagenPerfilURL(imagenPerfilLabel, controlador.getUsuarioActual().getImagen());
		imagenPerfilLabel.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent evt) {
		        // Aquí pones lo que quieres que pase cuando se haga clic en el JLabel
		        
		        // Por ejemplo, abrir una ventana de configuración de usuario
		        JFrame ventanaPerfil = new VentanaPerfil();
		        ventanaPerfil.setVisible(true);
		    }
		});
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

		
	        
		DefaultListModel<Mensaje> mensajeModel = new DefaultListModel<>();
		// Cambiar el modelo del JList para usar objetos Mensaje
		cargarUltimasConersaciones(mensajeModel);
        
		list = new JList<>(mensajeModel); // JList ahora usa el modelo de Mensaje
		list.setCellRenderer(new MensajeCellRenderer()); // Configurar el renderizador personalizado
		conversaciones.add(list, BorderLayout.CENTER);
		

		JScrollBar scrollBar = new JScrollBar();
		conversaciones.add(scrollBar, BorderLayout.EAST);

		JPanel chat = new JPanel();
		chat.setBackground(new Color(202, 253, 202));
		chat.setLayout(new BoxLayout(chat, BoxLayout.Y_AXIS));
		chat.setSize(400, 700);

		// Crear un JScrollPane que envuelva el panel 'chat'
		JScrollPane scrollPane = new JScrollPane(chat);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Siempre mostrar la barra de desplazamiento vertical
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // No mostrar barra de desplazamiento horizontal
		GridBagConstraints gbc_chat = new GridBagConstraints();
		gbc_chat.gridwidth = 2;
		gbc_chat.insets = new Insets(0, 0, 5, 5);
		gbc_chat.fill = GridBagConstraints.BOTH;
		gbc_chat.gridx = 3;
		gbc_chat.gridy = 1;
		contentPane.add(scrollPane, gbc_chat);

		

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
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enviarMensaje(chat, textField, Controlador.INSTANCE.getChatActual());
				
			}
		});
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.insets = new Insets(0, 0, 0, 5);
		gbc_btnSend.gridx = 4;
		gbc_btnSend.gridy = 2;
		contentPane.add(btnSend, gbc_btnSend);
		btnSend.setIcon(new ImageIcon(VentanaMain.class.getResource("/imagen/enviar-button.png")));
		
		// Agregar un MouseListener al JList
		list.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent e) {
		        int index = list.locationToIndex(e.getPoint());
		        if (index != -1) {
		            Rectangle cellBounds = list.getCellBounds(index, index);
		            if (cellBounds != null && cellBounds.contains(e.getPoint())) {
		                Mensaje mensaje = list.getModel().getElementAt(index);
		                
		                // Obtener el renderer para esa celda
		                MensajeCellRenderer renderer = (MensajeCellRenderer) list.getCellRenderer();
		                Point relativePoint = new Point(e.getX() - cellBounds.x, e.getY() - cellBounds.y);
		                
		                if (renderer.isAddButtonClicked(relativePoint)) {
		                    // Acción del botón "+"
		                    JFrame ventanaAdd = new AlertaAgregarContacto();
		                    ventanaAdd.setVisible(true);
		                } else {
		                    // Acción normal de selección de mensaje
		                    Optional<Contacto> contacto = controlador.mensajeCon(mensaje);
		                    contacto.ifPresent(c -> loadChat(Optional.of(c), chat));
		                }
		            }
		        }
		    }
		});


		
		enviarBarraButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        String contactoOTelefono = (String) comboBox.getSelectedItem();
		        if (contactoOTelefono != null && !contactoOTelefono.trim().isEmpty()) {
		            Optional<Contacto> contacto = controlador.getContacto(contactoOTelefono);

		            // Si el contacto no existe, crear un contacto temporal
		            if (contacto.isEmpty()) {
		                int respuesta = JOptionPane.showConfirmDialog(
		                        VentanaMain.this,
		                        "El número " + contactoOTelefono + " no está en tus contactos. ¿Quieres iniciar una conversación?",
		                        "Nuevo Contacto",
		                        JOptionPane.YES_NO_OPTION
		                );

		                if (respuesta == JOptionPane.YES_OPTION) {
		                    ContactoIndividual nuevoContacto = controlador.doAgregar(contactoOTelefono, contactoOTelefono);
		                    if (nuevoContacto != null) {
		                        contacto = Optional.of(nuevoContacto);
		                    } else {
		                        JOptionPane.showMessageDialog(
		                                VentanaMain.this,
		                                "No se pudo crear el contacto.",
		                                "Error",
		                                JOptionPane.ERROR_MESSAGE
		                        );
		                        return;
		                    }
		                } else {
		                    return; // Cancelar si el usuario no quiere iniciar la conversación
		                }
		            }

		            // Cargar el chat en el panel derecho
		            loadChat(contacto, chat);
		            JOptionPane.showMessageDialog(
                            VentanaMain.this,
                            "Chat cargado para: " + contactoOTelefono,
                            "Chat cargado",
                            JOptionPane.PLAIN_MESSAGE
                    );
		        } else {
		        	JOptionPane.showMessageDialog(
                            VentanaMain.this,
                            "Por favor seleccione un contacto valido.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
		        }
		    }
		});
		

	}
	private void cargarUltimasConersaciones(DefaultListModel<Mensaje> mensajeModel) {
		for (Contacto c: controlador.getContactosUsuarioActual()) {
			if (controlador.getUltimoMensaje(c)!= null) {
				mensajeModel.addElement(controlador.getUltimoMensaje(c));
			}
			
		}
		
	}



	@Override
    public void actualizar() {
        // Este método se llama cuando el controlador notifica que se ha agregado un contacto
        cargarComboBox(comboBox);
    }

	public void cargarComboBox(JComboBox<String> comboBox) {
		comboBox.setName("contacto o telefono");
		DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
		Controlador.INSTANCE.getContactosNombre().forEach(comboBoxModel::addElement);
		comboBoxModel.addElement("telefono");
		comboBox.setModel(comboBoxModel);
		comboBox.setEditable(true);
		comboBox.setToolTipText("Seleccione un contacto o escriba un teléfono");
	}



	private void cargarImagenPerfilURL(JLabel lblimage, String url) {
		String urlText = url; // Leer el texto del campo
		if (!urlText.isEmpty()) {
			try {
				URL imageUrl = new URL(urlText); // Intentar cargar la URL
				ImageIcon icon = new ImageIcon(imageUrl);
				lblimage.setIcon(resizeImageIcon(icon, 32, 32)); // Redimensionar la imagen
			} catch (Exception ex) {
				setDefaultImage(lblimage);
			}
		} else {
			setDefaultImage(lblimage); // Si no hay texto, usar la imagen por defecto
		}
	}

	private void setDefaultImage(JLabel lblimage) {
		ImageIcon defaultIcon = new ImageIcon(VentanaRegister.class.getResource("/imagen/user.png"));
		lblimage.setIcon(resizeImageIcon(defaultIcon, 32, 32)); // Redimensionar a 32
	}

	private ImageIcon resizeImageIcon(ImageIcon icon, int width, int height) {
		Image image = icon.getImage();
		Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH); // Escalado suave
		return new ImageIcon(scaledImage);
	}
	
	private void enviarMensaje(JPanel panel, JTextField textField, Contacto contacto) {
		// No permite enviar un mensaje si no hay seleccionado ningún contacto
		if (contacto == null)
			return;

		Controlador.INSTANCE.enviarMensaje(contacto, textField.getText());

		BubbleText burbuja = new BubbleText(panel, textField.getText(), Color.GREEN, "Tú", BubbleText.SENT,
				12);
		panel.add(burbuja);
		textField.setText(null);
	}
}
