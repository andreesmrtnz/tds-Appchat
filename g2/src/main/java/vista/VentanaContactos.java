package vista;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import controlador.Controlador;
import modelo.Contacto;
import modelo.ContactoIndividual;
import modelo.Grupo;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaContactos extends JFrame {

    private JList<String> listaContactos;
    private JList<String> listaGrupo;
    private JButton btnAgregarContacto;
    private JButton btnAgregarGrupo;
    private JButton btnModificarGrupo;
    private JButton btnMoverDerecha;
    private JButton btnMoverIzquierda;
    private Controlador controlador;
    private JTextField textField;
    private JButton btnAddContacto;

    public VentanaContactos() {
        controlador = Controlador.INSTANCE;
        setTitle("Gestión de Grupos");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(849, 456);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel();
        setContentPane(contentPane);

        // Modelos de las listas
        DefaultListModel<String> modeloContactos = new DefaultListModel<>();
        DefaultListModel<String> modeloGrupo = new DefaultListModel<>();

        // Cargar contactos desde el controlador
        modeloContactos.addAll(controlador.getContactosNombre());

        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{7, 111, 49, 100, 0, 0};
        gbl_contentPane.rowHeights = new int[]{6, 0, 0, 150, 23, 0, 0, 0};
        gbl_contentPane.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0};
        contentPane.setLayout(gbl_contentPane);

        JLabel lblNewLabel = new JLabel("Nombre del grupo:");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel.gridx = 3;
        gbc_lblNewLabel.gridy = 1;
        contentPane.add(lblNewLabel, gbc_lblNewLabel);

        textField = new JTextField();
        GridBagConstraints gbc_textField = new GridBagConstraints();
        gbc_textField.insets = new Insets(0, 0, 5, 5);
        gbc_textField.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField.gridx = 3;
        gbc_textField.gridy = 2;
        contentPane.add(textField, gbc_textField);
        textField.setColumns(10);

        // Listado de contactos
        listaContactos = new JList<>(modeloContactos);
        JScrollPane scrollContactos = new JScrollPane(listaContactos);
        scrollContactos.setPreferredSize(new Dimension(100, 150));
        GridBagConstraints gbc_scrollContactos = new GridBagConstraints();
        gbc_scrollContactos.fill = GridBagConstraints.BOTH;
        gbc_scrollContactos.insets = new Insets(0, 0, 5, 5);
        gbc_scrollContactos.gridx = 1;
        gbc_scrollContactos.gridy = 3;
        contentPane.add(scrollContactos, gbc_scrollContactos);

        // Listado del grupo
        listaGrupo = new JList<>(modeloGrupo);
        JScrollPane scrollGrupo = new JScrollPane(listaGrupo);
        scrollGrupo.setPreferredSize(new Dimension(100, 150));
        GridBagConstraints gbc_scrollGrupo = new GridBagConstraints();
        gbc_scrollGrupo.fill = GridBagConstraints.BOTH;
        gbc_scrollGrupo.insets = new Insets(0, 0, 5, 5);
        gbc_scrollGrupo.gridx = 3;
        gbc_scrollGrupo.gridy = 3;
        contentPane.add(scrollGrupo, gbc_scrollGrupo);

        // Botón Añadir Grupo
        btnAgregarGrupo = new JButton("Añadir Grupo");
        btnAgregarGrupo.addActionListener(e -> {
            String nombreGrupo = textField.getText().trim();
            if (nombreGrupo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre del grupo no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            List<ContactoIndividual> participantes = new ArrayList<>();
            for (int i = 0; i < modeloGrupo.size(); i++) {
                Optional<Contacto> c = controlador.getContacto(modeloGrupo.get(i));
                if (c.isPresent() && c.get() instanceof ContactoIndividual) {
                    ContactoIndividual contacto = (ContactoIndividual) c.get();
                    participantes.add(contacto);
                }
            }
            if (participantes.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El grupo debe tener al menos un contacto.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            controlador.crearGrupo(nombreGrupo, participantes);
            modeloGrupo.clear();
            textField.setText("");
            JOptionPane.showMessageDialog(this, "Grupo creado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        });
        
        btnAddContacto = new JButton("Añadir Contacto");
        btnAddContacto.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		JFrame ventanaAddContacto = new AlertaAgregarContacto();
        		ventanaAddContacto.setVisible(true);
        		dispose();
        	}
        });
        GridBagConstraints gbc_btnAddContacto = new GridBagConstraints();
        gbc_btnAddContacto.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnAddContacto.gridheight = 2;
        gbc_btnAddContacto.insets = new Insets(0, 0, 5, 5);
        gbc_btnAddContacto.gridx = 1;
        gbc_btnAddContacto.gridy = 4;
        contentPane.add(btnAddContacto, gbc_btnAddContacto);
        GridBagConstraints gbc_btnAgregarGrupo = new GridBagConstraints();
        gbc_btnAgregarGrupo.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnAgregarGrupo.gridheight = 2;
        gbc_btnAgregarGrupo.insets = new Insets(0, 0, 5, 5);
        gbc_btnAgregarGrupo.gridx = 3;
        gbc_btnAgregarGrupo.gridy = 4;
        contentPane.add(btnAgregarGrupo, gbc_btnAgregarGrupo);

        // Botón para modificar grupo
        btnModificarGrupo = new JButton("Modificar Grupo");
        btnModificarGrupo.addActionListener(e -> {
            String nombreGrupo = textField.getText().trim();
            if (nombreGrupo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un grupo existente para modificar.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Optional<Contacto> g = controlador.getContacto(nombreGrupo);
            if (g.isEmpty() || !(g.get() instanceof Grupo)) {
                JOptionPane.showMessageDialog(this, "El contacto seleccionado no es un grupo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Grupo grupo = (Grupo) g.get();
            List<ContactoIndividual> participantes = new ArrayList<>();
            for (int i = 0; i < modeloGrupo.size(); i++) {
                Optional<Contacto> c = controlador.getContacto(modeloGrupo.get(i));
                if (c.isPresent() && c.get() instanceof ContactoIndividual) {
                    ContactoIndividual contacto = (ContactoIndividual) c.get();
                    participantes.add(contacto);
                }
            }
            controlador.modificarGrupo(grupo, nombreGrupo, participantes);
            modeloGrupo.clear();
            textField.setText("");
            JOptionPane.showMessageDialog(this, "Grupo modificado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        });
        GridBagConstraints gbc_btnModificarGrupo = new GridBagConstraints();
        gbc_btnModificarGrupo.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnModificarGrupo.insets = new Insets(0, 0, 5, 0);
        gbc_btnModificarGrupo.gridx = 4;
        gbc_btnModificarGrupo.gridy = 4;
        contentPane.add(btnModificarGrupo, gbc_btnModificarGrupo);

        // Botón para mover a la derecha
        btnMoverDerecha = new JButton(">>");
        btnMoverDerecha.addActionListener(e -> {
            String selectedContact = listaContactos.getSelectedValue();
            if (selectedContact != null) {
                modeloContactos.removeElement(selectedContact);
                modeloGrupo.addElement(selectedContact);
            }
        });
        GridBagConstraints gbc_btnMoverDerecha = new GridBagConstraints();
        gbc_btnMoverDerecha.insets = new Insets(0, 0, 5, 5);
        gbc_btnMoverDerecha.gridx = 2;
        gbc_btnMoverDerecha.gridy = 4;
        contentPane.add(btnMoverDerecha, gbc_btnMoverDerecha);

        // Botón para mover a la izquierda
        btnMoverIzquierda = new JButton("<<");
        btnMoverIzquierda.addActionListener(e -> {
            String selectedContact = listaGrupo.getSelectedValue();
            if (selectedContact != null) {
                modeloGrupo.removeElement(selectedContact);
                modeloContactos.addElement(selectedContact);
            }
        });
        GridBagConstraints gbc_btnMoverIzquierda = new GridBagConstraints();
        gbc_btnMoverIzquierda.insets = new Insets(0, 0, 5, 5);
        gbc_btnMoverIzquierda.gridx = 2;
        gbc_btnMoverIzquierda.gridy = 5;
        contentPane.add(btnMoverIzquierda, gbc_btnMoverIzquierda);
    }
}
