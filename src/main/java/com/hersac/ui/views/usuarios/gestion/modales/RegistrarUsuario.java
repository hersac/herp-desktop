package com.hersac.ui.views.usuarios.gestion.modales;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import com.hersac.core.modules.departamentos.entities.DepartamentoEntity;
import com.hersac.core.modules.roles.entities.RolEntity;
import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import com.hersac.ui.views.usuarios.gestion.listeners.UsuariosListeners;

public class RegistrarUsuario extends JDialog {
    private JTextField nombreField;
    private JTextField correoField;
    private JPasswordField contrasenaField;
    private JCheckBox activoCheckBox;
    private JComboBox<DepartamentoEntity> departamentoCombo;
    private JComboBox<RolEntity> rolCombo;
    private UsuarioEntity usuarioRegistrado;
    private final UsuariosListeners listener;
    private final boolean esEdicion;

    public RegistrarUsuario(JFrame parent, List<DepartamentoEntity> departamentos, List<RolEntity> roles,
            UsuariosListeners listener, UsuarioEntity usuarioParaEditar) {
        super(parent, usuarioParaEditar != null ? "Actualizar Usuario" : "Registrar Usuario", true);
        this.listener = listener;
        this.usuarioRegistrado = usuarioParaEditar;
        this.esEdicion = usuarioParaEditar != null;
        initComponents(departamentos, roles);
    }

    private void initComponents(List<DepartamentoEntity> departamentos, List<RolEntity> roles) {
        configurarVentana();
        JPanel formPanel = crearCampos(departamentos, roles);
        if (esEdicion && usuarioRegistrado != null) {
            cargarDatosEdicion();
        }
        JPanel buttonPanel = crearPanelBotones();
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void configurarVentana() {
        setSize(600, 400);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private JPanel crearCampos(List<DepartamentoEntity> departamentos, List<RolEntity> roles) {
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        nombreField = new JTextField();
        aplicarFiltroMayusculas(nombreField);
        correoField = new JTextField();
        aplicarFiltroMinusculas(correoField);
        contrasenaField = new JPasswordField();
        activoCheckBox = new JCheckBox("Activo", true);
        departamentoCombo = new JComboBox<>();
        rolCombo = new JComboBox<>();

        departamentos.forEach(departamentoCombo::addItem);
        roles.forEach(rolCombo::addItem);

        formPanel.add(new JLabel("Nombre:"));
        formPanel.add(nombreField);
        formPanel.add(new JLabel("Correo:"));
        formPanel.add(correoField);
        formPanel.add(new JLabel("Contraseña:"));
        formPanel.add(contrasenaField);
        formPanel.add(new JLabel("Departamento:"));
        formPanel.add(departamentoCombo);
        formPanel.add(new JLabel("Rol:"));
        formPanel.add(rolCombo);
        formPanel.add(new JLabel("¿Está activo?:"));
        formPanel.add(activoCheckBox);
        return formPanel;
    }

    private void aplicarFiltroMayusculas(JTextField field) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null) {
                    super.insertString(fb, offset, string.toUpperCase(), attr);
                }
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text != null) {
                    super.replace(fb, offset, length, text.toUpperCase(), attrs);
                }
            }
        });
    }

    private void aplicarFiltroMinusculas(JTextField field) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null) {
                    super.insertString(fb, offset, string.toLowerCase(), attr);
                }
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text != null) {
                    super.replace(fb, offset, length, text.toLowerCase(), attrs);
                }
            }
        });
    }

    private void cargarDatosEdicion() {
        nombreField.setText(usuarioRegistrado.getNombre());
        correoField.setText(usuarioRegistrado.getCorreo());
        contrasenaField.setText(usuarioRegistrado.getContrasena());
        activoCheckBox.setSelected(usuarioRegistrado.getEstaActivo());
        departamentoCombo.setSelectedItem(usuarioRegistrado.getDepartamento());
        rolCombo.setSelectedItem(usuarioRegistrado.getRol());
    }

    private JPanel crearPanelBotones() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton guardarBtn = new JButton(esEdicion ? "Actualizar" : "Guardar");
        JButton cancelarBtn = new JButton("Cancelar");
        guardarBtn.addActionListener(e -> guardarUsuario());
        cancelarBtn.addActionListener(e -> dispose());
        buttonPanel.add(guardarBtn);
        buttonPanel.add(cancelarBtn);
        return buttonPanel;
    }

    private void guardarUsuario() {
        DepartamentoEntity departamentoSeleccionado = (DepartamentoEntity) departamentoCombo.getSelectedItem();
        RolEntity rolSeleccionado = (RolEntity) rolCombo.getSelectedItem();

        if (usuarioRegistrado == null) {
            usuarioRegistrado = new UsuarioEntity();
        }

        usuarioRegistrado.setNombre(nombreField.getText());
        usuarioRegistrado.setCorreo(correoField.getText());
        usuarioRegistrado.setContrasena(new String(contrasenaField.getPassword()));
        usuarioRegistrado.setEstaActivo(activoCheckBox.isSelected());
        usuarioRegistrado.setDepartamento(departamentoSeleccionado);
        usuarioRegistrado.setRol(rolSeleccionado);
        usuarioRegistrado.setUsuarioActualizacion(UsuarioEntity.builder().usuarioId(1L).build());

        if (!esEdicion) {
            usuarioRegistrado.setUsuarioCreacion(UsuarioEntity.builder().usuarioId(1L).build());
            listener.crearUsuario(usuarioRegistrado);
        } else {
            listener.actualizarUsuario(usuarioRegistrado);
        }

        dispose();
    }

    public UsuarioEntity getUsuarioRegistrado() {
        return usuarioRegistrado;
    }
}
