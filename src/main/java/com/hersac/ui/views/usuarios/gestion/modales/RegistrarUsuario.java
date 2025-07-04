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

import com.hersac.core.modules.departamentos.entities.DepartamentoEntity;
import com.hersac.core.modules.rolespermisos.entities.RolPermisoEntity;
import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import com.hersac.ui.views.usuarios.gestion.listeners.UsuariosListeners;

public class RegistrarUsuario extends JDialog {

    private JTextField nombreField;
    private JTextField correoField;
    private JPasswordField contrasenaField;
    private JCheckBox activoCheckBox;
    private JComboBox<DepartamentoEntity> departamentoCombo;
    private JComboBox<RolPermisoEntity> rolCombo;

    private UsuarioEntity usuarioRegistrado;
    private final UsuariosListeners listener;

    public RegistrarUsuario(JFrame parent, List<DepartamentoEntity> departamentos, List<RolPermisoEntity> roles,
            UsuariosListeners listener) {
        super(parent, "Registrar Usuario", true);
        this.listener = listener;
        initComponents(departamentos, roles);
    }

    private void initComponents(List<DepartamentoEntity> departamentos, List<RolPermisoEntity> roles) {
        setSize(600, 400);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        nombreField = new JTextField();
        correoField = new JTextField();
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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton guardarBtn = new JButton("Guardar");
        JButton cancelarBtn = new JButton("Cancelar");

        guardarBtn.addActionListener(e -> guardarUsuario());
        cancelarBtn.addActionListener(e -> dispose());

        buttonPanel.add(guardarBtn);
        buttonPanel.add(cancelarBtn);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void guardarUsuario() {
        DepartamentoEntity departamentoSeleccionado = (DepartamentoEntity) departamentoCombo.getSelectedItem();
        RolPermisoEntity rolSeleccionado = (RolPermisoEntity) rolCombo.getSelectedItem();

        usuarioRegistrado = UsuarioEntity.builder()
                .nombre(nombreField.getText())
                .correo(correoField.getText())
                .contrasena(new String(contrasenaField.getPassword()))
                .estaActivo(activoCheckBox.isSelected())
                .departamento(departamentoSeleccionado)
                .rolPermiso(rolSeleccionado)
                .usuarioCreacion(UsuarioEntity.builder().usuarioId(1L).build()) // ⚠ ID mínimo necesario
                .usuarioActualizacion(UsuarioEntity.builder().usuarioId(1L).build()) // ⚠ lo mismo aquí
                .build();

        listener.crearUsuario(usuarioRegistrado);
        dispose();
    }

    public UsuarioEntity getUsuarioRegistrado() {
        return usuarioRegistrado;
    }
}
