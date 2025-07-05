package com.hersac.ui.views.usuarios.rolesPermisos.modales;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import com.hersac.core.modules.roles.entities.RolEntity;
import com.hersac.core.modules.permisos.entities.PermisoEntity;

public class RegistrarRolPermiso extends JDialog {
    public JTextField nombreField;
    public JTextArea descripcionArea;
    public JCheckBox activoCheck;
    public JList<PermisoEntity> permisosList;
    public JButton guardarBtn;
    public boolean guardado = false;
    public RolEntity rolCreado;
    public List<PermisoEntity> permisosSeleccionados;

    public RegistrarRolPermiso(JFrame parent, List<PermisoEntity> permisos) {
        super(parent, "Registrar Rol", true);
        setLayout(new BorderLayout(10, 10));
        setSize(400, 400);
        setLocationRelativeTo(parent);

        JPanel formPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        nombreField = new JTextField();
        descripcionArea = new JTextArea(3, 20);
        activoCheck = new JCheckBox("Activo", true);
        permisosList = new JList<>(permisos.toArray(new PermisoEntity[0]));
        permisosList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        permisosList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof PermisoEntity) {
                    value = ((PermisoEntity) value).getNombre();
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        JScrollPane permisosScroll = new JScrollPane(permisosList);
        permisosScroll.setPreferredSize(new Dimension(200, 80));

        formPanel.add(new JLabel("Nombre del Rol:"));
        formPanel.add(nombreField);
        formPanel.add(new JLabel("Descripción:"));
        formPanel.add(new JScrollPane(descripcionArea));
        formPanel.add(activoCheck);
        formPanel.add(new JLabel("Permisos asociados:"));
        formPanel.add(permisosScroll);

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

        add(formPanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    public boolean isGuardado() { return guardado; }
    public RolEntity getRolCreado() { return rolCreado; }
    public List<PermisoEntity> getPermisosSeleccionados() { return permisosSeleccionados; }
}
