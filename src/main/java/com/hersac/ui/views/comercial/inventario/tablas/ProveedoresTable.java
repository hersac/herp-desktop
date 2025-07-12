package com.hersac.ui.views.comercial.inventario.tablas;

import com.hersac.core.modules.proveedores.entities.ProveedorEntity;
import com.hersac.core.globals.servicios.PermissionService;
import com.hersac.ui.views.comercial.inventario.listeners.ProveedorListeners;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.List;

public class ProveedoresTable extends JPanel {
    private final JTable tablaProveedores;
    private final DefaultTableModel modeloTabla;
    private ProveedorListeners proveedorListeners;
    private PermissionService permissionService;

    public ProveedoresTable() {
        this(null);
    }

    public ProveedoresTable(PermissionService permissionService) {
        this.permissionService = permissionService;
        setLayout(new BorderLayout());
        String[] columnas = {"ID", "Nombre", "Tipo", "Estado", "Acciones"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        tablaProveedores = new JTable(modeloTabla);
        tablaProveedores.setRowHeight(40);
        tablaProveedores.setShowGrid(false);
        tablaProveedores.getColumnModel().getColumn(4).setCellRenderer(new AccionesRenderer());
        tablaProveedores.getColumnModel().getColumn(4).setCellEditor(new AccionesEditor());
        tablaProveedores.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = tablaProveedores.columnAtPoint(e.getPoint());
                int row = tablaProveedores.rowAtPoint(e.getPoint());
                if (column == 4 && row >= 0 && tablaProveedores.isCellEditable(row, column)) {
                    tablaProveedores.editCellAt(row, column);
                    tablaProveedores.getEditorComponent().requestFocusInWindow();
                }
            }
        });
        add(new JScrollPane(tablaProveedores), BorderLayout.CENTER);
    }

    public void setActionListener(ProveedorListeners listener) {
        this.proveedorListeners = listener;
    }

    private class AccionesRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            if (value instanceof ProveedorEntity proveedor) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                panel.setOpaque(false);
                boolean puedeVer = true;
                boolean puedeEditar = true;
                boolean puedeEliminar = true;
                if (puedeVer) {
                    JButton btnVer = crearIconoBoton(FontAwesomeSolid.EYE, "Ver proveedor", new Color(60, 130, 200));
                    panel.add(btnVer);
                }
                if (puedeEditar) {
                    boolean activo = Boolean.TRUE.equals(proveedor.isEsta_activo());
                    JButton btnToggle = crearIconoBoton(
                        activo ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                        activo ? "Inactivar" : "Activar",
                        activo ? new Color(0, 180, 0) : Color.RED);
                    panel.add(btnToggle);
                }
                if (puedeEliminar) {
                    JButton btnEliminar = crearIconoBoton(FontAwesomeSolid.TRASH_ALT, "Eliminar proveedor", Color.RED);
                    panel.add(btnEliminar);
                }
                panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                return panel;
            }
            return new JLabel("");
        }
    }

    private class AccionesEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel;
        private ProveedorEntity proveedor;
        public AccionesEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setOpaque(false);
        }
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            if (value instanceof ProveedorEntity) {
                proveedor = (ProveedorEntity) value;
                panel.removeAll();
                boolean activo = Boolean.TRUE.equals(proveedor.isEsta_activo());
                boolean puedeVer = true;
                boolean puedeEditar = true;
                boolean puedeEliminar = true;
                if (puedeVer) {
                    JButton btnVer = crearBotonAccion(FontAwesomeSolid.EYE, "Ver proveedor", new Color(60, 130, 200), e -> {
                        if (proveedorListeners != null)
                            proveedorListeners.verProveedor(proveedor);
                        fireEditingStopped();
                    });
                    panel.add(btnVer);
                }
                if (puedeEditar) {
                    JButton btnToggle = crearBotonAccion(
                        activo ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                        activo ? "Inactivar" : "Activar",
                        activo ? new Color(0, 180, 0) : Color.RED,
                        e -> {
                            proveedor.setEsta_activo(!proveedor.isEsta_activo());
                            modeloTabla.setValueAt(proveedor.isEsta_activo() ? "Activo" : "Inactivo", row, 3);
                            modeloTabla.setValueAt(proveedor, row, 4);
                            if (proveedorListeners != null)
                                proveedorListeners.actualizarProveedor(proveedor);
                            fireEditingStopped();
                        });
                    panel.add(btnToggle);
                }
                if (puedeEliminar) {
                    JButton btnEliminar = crearBotonAccion(FontAwesomeSolid.TRASH_ALT, "Eliminar proveedor", Color.RED, e -> {
                        if (proveedorListeners != null)
                            proveedorListeners.eliminarProveedor(proveedor);
                        fireEditingStopped();
                    });
                    panel.add(btnEliminar);
                }
            }
            return panel;
        }
        @Override
        public Object getCellEditorValue() {
            return proveedor;
        }
        @Override
        public boolean isCellEditable(EventObject e) {
            return !(e instanceof MouseEvent) || ((MouseEvent) e).getClickCount() == 1;
        }
    }

    private JPanel crearPanelAcciones(ProveedorEntity proveedor) {
        boolean activo = Boolean.TRUE.equals(proveedor.isEsta_activo());
        JButton btnVer = crearIconoBoton(FontAwesomeSolid.EYE, "Ver proveedor", new Color(60, 130, 200));
        JButton btnToggle = crearIconoBoton(
                activo ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                activo ? "Inactivar" : "Activar",
                activo ? new Color(0, 180, 0) : Color.RED);
        JButton btnEliminar = crearIconoBoton(FontAwesomeSolid.TRASH_ALT, "Eliminar proveedor", Color.RED);
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

    private ProveedorEntity getProveedor(int row) {
        if (row >= 0 && row < modeloTabla.getRowCount()) {
            Object id = modeloTabla.getValueAt(row, 0);
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                if (modeloTabla.getValueAt(i, 0).equals(id)) {
                    return (ProveedorEntity) modeloTabla.getValueAt(i, 4);
                }
            }
        }
        return null;
    }

    public void setProveedores(List<ProveedorEntity> proveedores) {
        modeloTabla.setRowCount(0);
        for (ProveedorEntity proveedor : proveedores) {
            modeloTabla.addRow(new Object[]{
                proveedor.getProveedorId(),
                proveedor.getTercero() != null ? proveedor.getTercero().getNombre() : "",
                proveedor.getTercero() != null ? proveedor.getTercero().getTipoPersona() : "",
                proveedor.isEsta_activo() ? "Activo" : "Inactivo",
                proveedor
            });
        }
    }
}
