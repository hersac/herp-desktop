package com.hersac.ui.views.comercial.clientes.tablas;

import com.hersac.core.modules.clientes.entities.ClienteEntity;
import com.hersac.core.globals.servicios.PermissionService;
import com.hersac.ui.views.comercial.clientes.listeners.ClientesListeners;

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

public class ClientesTable extends JPanel {
    private final JTable tablaClientes;
    private final DefaultTableModel modeloTabla;
    private ClientesListeners clientesListeners;
    private PermissionService permissionService;

    public ClientesTable() {
        this(null);
    }

    public ClientesTable(PermissionService permissionService) {
        this.permissionService = permissionService;
        setLayout(new BorderLayout());
        String[] columnas = {"ID", "Nombre", "Tipo", "Estado", "Acciones"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        tablaClientes = new JTable(modeloTabla);
        tablaClientes.setRowHeight(40);
        tablaClientes.setShowGrid(false);
        tablaClientes.getColumnModel().getColumn(4).setCellRenderer(new AccionesRenderer());
        tablaClientes.getColumnModel().getColumn(4).setCellEditor(new AccionesEditor());
        tablaClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = tablaClientes.columnAtPoint(e.getPoint());
                int row = tablaClientes.rowAtPoint(e.getPoint());
                if (column == 4 && row >= 0 && tablaClientes.isCellEditable(row, column)) {
                    tablaClientes.editCellAt(row, column);
                    tablaClientes.getEditorComponent().requestFocusInWindow();
                }
            }
        });
        add(new JScrollPane(tablaClientes), BorderLayout.CENTER);
    }

    public void setActionListener(ClientesListeners listener) {
        this.clientesListeners = listener;
    }

    private class AccionesRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            if (value instanceof ClienteEntity cliente) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                panel.setOpaque(false);
                boolean puedeVer = true;
                boolean puedeEditar = true;
                boolean puedeEliminar = true;
                if (puedeVer) {
                    JButton btnVer = crearIconoBoton(FontAwesomeSolid.EYE, "Ver cliente", new Color(60, 130, 200));
                    panel.add(btnVer);
                }
                if (puedeEditar) {
                    boolean activo = Boolean.TRUE.equals(cliente.isEsta_activo());
                    JButton btnToggle = crearIconoBoton(
                        activo ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                        activo ? "Inactivar" : "Activar",
                        activo ? new Color(0, 180, 0) : Color.RED);
                    panel.add(btnToggle);
                }
                if (puedeEliminar) {
                    JButton btnEliminar = crearIconoBoton(FontAwesomeSolid.TRASH_ALT, "Eliminar cliente", Color.RED);
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
        private ClienteEntity cliente;
        public AccionesEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setOpaque(false);
        }
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            if (value instanceof ClienteEntity) {
                cliente = (ClienteEntity) value;
                panel.removeAll();
                boolean activo = Boolean.TRUE.equals(cliente.isEsta_activo());
                boolean puedeVer = true;
                boolean puedeEditar = true;
                boolean puedeEliminar = true;
                if (puedeVer) {
                    JButton btnVer = crearBotonAccion(FontAwesomeSolid.EYE, "Ver cliente", new Color(60, 130, 200), e -> {
                        if (clientesListeners != null)
                            clientesListeners.verCliente(cliente);
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
                            cliente.setEsta_activo(!cliente.isEsta_activo());
                            modeloTabla.setValueAt(cliente.isEsta_activo() ? "Activo" : "Inactivo", row, 3);
                            modeloTabla.setValueAt(cliente, row, 4);
                            if (clientesListeners != null)
                                clientesListeners.actualizarCliente(cliente);
                            fireEditingStopped();
                        });
                    panel.add(btnToggle);
                }
                if (puedeEliminar) {
                    JButton btnEliminar = crearBotonAccion(FontAwesomeSolid.TRASH_ALT, "Eliminar cliente", Color.RED, e -> {
                        if (clientesListeners != null)
                            clientesListeners.eliminarCliente(cliente);
                        fireEditingStopped();
                    });
                    panel.add(btnEliminar);
                }
            }
            return panel;
        }
        @Override
        public Object getCellEditorValue() {
            return cliente;
        }
        @Override
        public boolean isCellEditable(EventObject e) {
            return !(e instanceof MouseEvent) || ((MouseEvent) e).getClickCount() == 1;
        }
    }

    private JPanel crearPanelAcciones(ClienteEntity cliente) {
        boolean activo = Boolean.TRUE.equals(cliente.isEsta_activo());
        JButton btnVer = crearIconoBoton(FontAwesomeSolid.EYE, "Ver cliente", new Color(60, 130, 200));
        JButton btnToggle = crearIconoBoton(
                activo ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                activo ? "Inactivar" : "Activar",
                activo ? new Color(0, 180, 0) : Color.RED);
        JButton btnEliminar = crearIconoBoton(FontAwesomeSolid.TRASH_ALT, "Eliminar cliente", Color.RED);
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

    private ClienteEntity getCliente(int row) {
        if (row >= 0 && row < modeloTabla.getRowCount()) {
            Object id = modeloTabla.getValueAt(row, 0);
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                if (modeloTabla.getValueAt(i, 0).equals(id)) {
                    return (ClienteEntity) modeloTabla.getValueAt(i, 4);
                }
            }
        }
        return null;
    }

    public void setClientes(List<ClienteEntity> clientes) {
        modeloTabla.setRowCount(0);
        for (ClienteEntity cliente : clientes) {
            modeloTabla.addRow(new Object[]{
                cliente.getClienteId(),
                cliente.getTercero() != null ? cliente.getTercero().getNombre() : "",
                cliente.getTercero() != null ? cliente.getTercero().getTipoPersona() : "",
                cliente.isEsta_activo() ? "Activo" : "Inactivo",
                cliente
            });
        }
    }
}
