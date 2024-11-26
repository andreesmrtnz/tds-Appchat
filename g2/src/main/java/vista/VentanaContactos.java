package vista;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaContactos extends JFrame {

    private JList<String> listaContactos;
    private JList<String> listaGrupo;
    private JButton btnAgregarContacto;
    private JButton btnAgregarGrupo;
    private JButton btnMoverDerecha;
    private JButton btnMoverIzquierda;

    public VentanaContactos() {
        setTitle("Gestión de Grupos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel();
        setContentPane(contentPane);

        // Listado de contactos
        DefaultListModel<String> modeloContactos = new DefaultListModel<>();
        modeloContactos.addElement("contacto1");
        modeloContactos.addElement("contacto2");
        modeloContactos.addElement("grupo1");
        modeloContactos.addElement("contacto3");
        modeloContactos.addElement("grupo2");
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{7, 111, 49, 100, 0, 0};
        gbl_contentPane.rowHeights = new int[]{6, 150, 23, 0, 0};
        gbl_contentPane.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
        contentPane.setLayout(gbl_contentPane);
                        
                                listaContactos = new JList<>(modeloContactos);
                                JScrollPane scrollContactos = new JScrollPane(listaContactos);
                                scrollContactos.setPreferredSize(new Dimension(100, 150));
                                GridBagConstraints gbc_scrollContactos = new GridBagConstraints();
                                gbc_scrollContactos.fill = GridBagConstraints.HORIZONTAL;
                                gbc_scrollContactos.insets = new Insets(0, 0, 5, 5);
                                gbc_scrollContactos.gridx = 1;
                                gbc_scrollContactos.gridy = 1;
                                contentPane.add(scrollContactos, gbc_scrollContactos);
                        
                                // Botón para mover a la derecha
                                btnMoverDerecha = new JButton(">>");
                                GridBagConstraints gbc_btnMoverDerecha = new GridBagConstraints();
                                gbc_btnMoverDerecha.insets = new Insets(0, 0, 5, 5);
                                gbc_btnMoverDerecha.gridx = 2;
                                gbc_btnMoverDerecha.gridy = 1;
                                contentPane.add(btnMoverDerecha, gbc_btnMoverDerecha);
                
                        //listaGrupo = new JList<>(modeloGrupo);
                        JScrollPane scrollGrupo = new JScrollPane();
                        scrollGrupo.setPreferredSize(new Dimension(100, 150));
                        GridBagConstraints gbc_scrollGrupo = new GridBagConstraints();
                        gbc_scrollGrupo.fill = GridBagConstraints.HORIZONTAL;
                        gbc_scrollGrupo.insets = new Insets(0, 0, 5, 5);
                        gbc_scrollGrupo.gridx = 3;
                        gbc_scrollGrupo.gridy = 1;
                        contentPane.add(scrollGrupo, gbc_scrollGrupo);
                
                        // Botón Añadir Contacto
                        btnAgregarContacto = new JButton("Añadir Contacto");
                        btnAgregarContacto.addActionListener(new ActionListener() {
                        	public void actionPerformed(ActionEvent e) {
                        		JFrame agregarContacto = new AlertaAgregarContacto();
                        		agregarContacto.setVisible(true);
                        		setVisible(false);
                        		
                        	}
                        });
                        GridBagConstraints gbc_btnAgregarContacto = new GridBagConstraints();
                        gbc_btnAgregarContacto.fill = GridBagConstraints.HORIZONTAL;
                        gbc_btnAgregarContacto.insets = new Insets(0, 0, 5, 5);
                        gbc_btnAgregarContacto.gridx = 1;
                        gbc_btnAgregarContacto.gridy = 2;
                        contentPane.add(btnAgregarContacto, gbc_btnAgregarContacto);
        
                // Botón para mover a la izquierda
                btnMoverIzquierda = new JButton("<<");
                GridBagConstraints gbc_btnMoverIzquierda = new GridBagConstraints();
                gbc_btnMoverIzquierda.insets = new Insets(0, 0, 5, 5);
                gbc_btnMoverIzquierda.gridx = 2;
                gbc_btnMoverIzquierda.gridy = 2;
                contentPane.add(btnMoverIzquierda, gbc_btnMoverIzquierda);
        
                // Botón Añadir Grupo
                btnAgregarGrupo = new JButton("Añadir Grupo");
                GridBagConstraints gbc_btnAgregarGrupo = new GridBagConstraints();
                gbc_btnAgregarGrupo.fill = GridBagConstraints.HORIZONTAL;
                gbc_btnAgregarGrupo.insets = new Insets(0, 0, 5, 5);
                gbc_btnAgregarGrupo.gridx = 3;
                gbc_btnAgregarGrupo.gridy = 2;
                contentPane.add(btnAgregarGrupo, gbc_btnAgregarGrupo);

        // Listado del grupo
        DefaultListModel<String> modeloGrupo = new DefaultListModel<>();
        modeloGrupo.addElement("contacto4");
        modeloGrupo.addElement("contacto5");
    }

    public static void main(String[] args) {
        VentanaContactos ventana = new VentanaContactos();
        ventana.setVisible(true);
    }
}
