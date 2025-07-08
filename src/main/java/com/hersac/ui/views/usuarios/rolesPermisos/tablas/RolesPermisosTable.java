package com.hersac.ui.views.usuarios.rolesPermisos.tablas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import com.hersac.core.modules.roles.entities.RolEntity;
import com.hersac.ui.controllers.rolesPermisos.RolesPermisosController;
import com.hersac.ui.views.usuarios.rolesPermisos.listeners.RolesPermisosListener;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

public class RolesPermisosTable extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private RolesPermisosListener listener;
    private RolesPermisosController rolesPermisosController;

    public RolesPermisosTable() {
        setLayout(new BorderLayout());
        model = new DefaultTableModel(new Object[]{"ID", "Nombre", "Descripción", "Activo", "Acciones"}, 0) {
            public boolean isCellEditable(int row, int column) { return column == 4; }
        };
        table = new JTable(model);
        table.setRowHeight(40);
        table.getColumnModel().getColumn(4).setCellRenderer(new AccionesRenderer());
        table.getColumnModel().getColumn(4).setCellEditor(new AccionesEditor());
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = table.columnAtPoint(e.getPoint());
                int row = table.rowAtPoint(e.getPoint());
                if (column == 4 && row >= 0 && table.isCellEditable(row, column)) {
                    table.editCellAt(row, column);
                    table.getEditorComponent().requestFocusInWindow();
                }
            }
        });
        // Quitar JScrollPane aquí, solo agregar la tabla directamente
        add(table, BorderLayout.CENTER);
    }

    public void setRoles(List<RolEntity> lista) {
        // Detener edición activa antes de actualizar el modelo
        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }
        model.setRowCount(0);
        for (RolEntity rol : lista) {
            model.addRow(new Object[]{
                rol.getRolId(),
                rol.getNombre(),
                rol.getDescripcion(),
                rol.getEstaActivo() != null && rol.getEstaActivo() ? "Sí" : "No",
                rol
            });
        }
    }

    public void setActionListener(RolesPermisosListener listener) {
        this.listener = listener;
    }

    public void setRolesPermisosController(RolesPermisosController controller) {
        this.rolesPermisosController = controller;
    }

    private class AccionesRenderer implements javax.swing.table.TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof RolEntity rol) {
                JPanel panel = crearPanelAcciones(rol);
                panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                return panel;
            }
            return new JLabel("");
        }
    }

    private class AccionesEditor extends AbstractCellEditor implements javax.swing.table.TableCellEditor {
        private final JPanel panel;
        private RolEntity rol;

        public AccionesEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setOpaque(false);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value instanceof RolEntity) {
                rol = (RolEntity) value;
                panel.removeAll();
                boolean activo = Boolean.TRUE.equals(rol.getEstaActivo());
                JButton btnVer = crearBotonAccion(FontAwesomeSolid.EYE, "Ver rol", new Color(60, 130, 200), e -> {
                    if (listener != null) listener.mostrarModalEditarRol(rol);
                    fireEditingStopped();
                });
                JButton btnToggle = crearBotonAccion(
                        activo ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                        activo ? "Inactivar" : "Activar",
                        activo ? new Color(0, 180, 0) : Color.RED,
                        e -> {
                            rol.setEstaActivo(!rol.getEstaActivo());
                            // Validar que la fila y columnas siguen existiendo antes de modificar el modelo
                            if (row >= 0 && row < model.getRowCount() && 3 < model.getColumnCount() && 4 < model.getColumnCount()) {
                                try {
                                    model.setValueAt(rol.getEstaActivo() ? "Sí" : "No", row, 3);
                                    model.setValueAt(rol, row, 4);
                                } catch (Exception ex) {
                                    // Ignorar si ocurre un error por cambio de modelo
                                }
                            }
                            if (listener != null) {
                                List<Long> permisos = new java.util.ArrayList<>();
                                if (rolesPermisosController != null) {
                                    List<com.hersac.core.modules.rolespermisos.entities.RolPermisoEntity> rolPermisos = rolesPermisosController.buscarPorRolId(rol.getRolId());
                                    for (com.hersac.core.modules.rolespermisos.entities.RolPermisoEntity rp : rolPermisos) {
                                        if (rp.getPermiso() != null && rp.getPermiso().getPermisoId() != null) {
                                            permisos.add(rp.getPermiso().getPermisoId());
                                        }
                                    }
                                }
                                listener.editarRol(rol, permisos);
                            }
                            fireEditingStopped();
                        });
                JButton btnEliminar = crearBotonAccion(FontAwesomeSolid.TRASH_ALT, "Eliminar rol", Color.RED, e -> {
                    fireEditingStopped(); // Detener edición antes de eliminar
                    if (listener != null) listener.eliminarRol(rol);
                });
                panel.add(btnVer);
                panel.add(btnToggle);
                panel.add(btnEliminar);
            }
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return rol;
        }
    }

    private JPanel crearPanelAcciones(RolEntity rol) {
        boolean activo = Boolean.TRUE.equals(rol.getEstaActivo());
        JButton btnVer = crearIconoBoton(FontAwesomeSolid.EYE, "Ver rol", new Color(60, 130, 200));
        JButton btnToggle = crearIconoBoton(
                activo ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                activo ? "Inactivar" : "Activar",
                activo ? new Color(0, 180, 0) : Color.RED);
        JButton btnEliminar = crearIconoBoton(FontAwesomeSolid.TRASH_ALT, "Eliminar rol", Color.RED);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.setOpaque(false);
        panel.add(btnVer);
        panel.add(btnToggle);
        panel.add(btnEliminar);
        return panel;
    }

    private JButton crearIconoBoton(FontAwesomeSolid icono, String tooltip, Color color) {
        FontIcon icon = FontIcon.of(icono, 18, color);
        JButton button = new JButton(icon);
        button.setToolTipText(tooltip);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(36, 36));
        return button;
    }

    private JButton crearBotonAccion(FontAwesomeSolid icono, String tooltip, Color color, ActionListener action) {
        JButton button = crearIconoBoton(icono, tooltip, color);
        button.addActionListener(action);
        return button;
    }
}
