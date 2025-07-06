package com.hersac.ui.views.usuarios.rolesPermisos.modales;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import com.hersac.core.modules.roles.entities.RolEntity;
import com.hersac.core.modules.permisos.entities.PermisoEntity;
import com.hersac.ui.views.usuarios.rolesPermisos.PermisosTable;

public class RegistrarRolPermiso extends JDialog {
    public JTextField nombreField;
    public JTextArea descripcionArea;
    public JCheckBox activoCheck;
    public JList<PermisoEntity> permisosList;
    public JButton guardarBtn;
    public boolean guardado = false;
    public RolEntity rolCreado;
    public List<PermisoEntity> permisosSeleccionados;
    private PermisosTable permisosTable;
    private JComboBox<String> moduloSelector;
    private static final String[] MODULOS = {"Comercial", "Financiero", "Usuarios"};
    private static final String[][] SUBMODULOS = {
        {"Clientes", "Ventas", "Compras", "Inventario", "Reportes"},
        {"CxC", "CxP", "Movimientos", "Bancos", "Reportes"},
        {"Gestión de usuarios", "Roles y permisos", "Auditoría"}
    };

    public RegistrarRolPermiso(JFrame parent, List<PermisoEntity> permisos) {
        super(parent, "Registrar Rol", true);
        setLayout(new BorderLayout(10, 10));
        setSize(500, 600);
        setLocationRelativeTo(parent);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nombreLabel = new JLabel("Nombre del Rol:");
        nombreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(nombreLabel);
        nombreField = new JTextField();
        nombreField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        nombreField.setPreferredSize(new Dimension(200, 30));
        nombreField.setMinimumSize(new Dimension(100, 30));
        formPanel.add(nombreField);

        JLabel descLabel = new JLabel("Descripción:");
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(descLabel);
        descripcionArea = new JTextArea(3, 20);
        descripcionArea.setFont(nombreField.getFont());
        descripcionArea.setLineWrap(true);
        descripcionArea.setWrapStyleWord(true);
        JScrollPane descripcionScroll = new JScrollPane(descripcionArea);
        descripcionScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        descripcionScroll.setPreferredSize(new Dimension(200, 30));
        descripcionScroll.setMinimumSize(new Dimension(100, 30));
        formPanel.add(descripcionScroll);

        JLabel moduloLabel = new JLabel("Selecciona módulo:");
        moduloLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(moduloLabel);
        moduloSelector = new JComboBox<>(MODULOS);
        moduloSelector.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        moduloSelector.setPreferredSize(new Dimension(200, 30));
        moduloSelector.setMinimumSize(new Dimension(100, 30));
        formPanel.add(moduloSelector);

        permisosTable = new PermisosTable(SUBMODULOS[0]);
        permisosTable.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        permisosTable.setPreferredSize(new Dimension(400, 200));
        permisosTable.setMinimumSize(new Dimension(200, 200));
        formPanel.add(permisosTable);

        moduloSelector.addActionListener(e -> {
            int idx = moduloSelector.getSelectedIndex();
            permisosTable.setSubmodulos(SUBMODULOS[idx]);
        });

        activoCheck = new JCheckBox("Activo", true);
        formPanel.add(activoCheck);

        JPanel paddingPanel = new JPanel(new BorderLayout());
        paddingPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        paddingPanel.add(formPanel, BorderLayout.CENTER);

        add(paddingPanel, BorderLayout.CENTER);

        guardarBtn = new JButton("Guardar");
        guardarBtn.addActionListener(e -> {
            if (nombreField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre del rol es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            rolCreado = RolEntity.builder()
                    .nombre(nombreField.getText().trim())
                    .descripcion(descripcionArea.getText().trim())
                    .estaActivo(activoCheck.isSelected())
                    .build();
            permisosSeleccionados = permisosList.getSelectedValuesList();
            if (permisosSeleccionados.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar al menos un permiso.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            guardado = true;
            setVisible(false);
        });

        JPanel btnPanel = new JPanel();
        btnPanel.add(guardarBtn);

        add(btnPanel, BorderLayout.SOUTH);
    }

    public boolean isGuardado() { return guardado; }
    public RolEntity getRolCreado() { return rolCreado; }
    public List<PermisoEntity> getPermisosSeleccionados() { return permisosSeleccionados; }
}
