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
            public boolean isCellEditable(int fila, int columna) {
                return columna == 4;
            }
        };
        tablaClientes = new JTable(modeloTabla);
        tablaClientes.setRowHeight(40);
        tablaClientes.setShowGrid(false);
        tablaClientes.getColumnModel().getColumn(4).setCellRenderer(new RenderAcciones());
        tablaClientes.getColumnModel().getColumn(4).setCellEditor(new EditorAcciones());
        tablaClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evento) {
                int columna = tablaClientes.columnAtPoint(evento.getPoint());
                int fila = tablaClientes.rowAtPoint(evento.getPoint());
                if (columna == 4 && fila >= 0 && tablaClientes.isCellEditable(fila, columna)) {
                    tablaClientes.editCellAt(fila, columna);
                    tablaClientes.getEditorComponent().requestFocusInWindow();
                }
            }
        });
        add(new JScrollPane(tablaClientes), BorderLayout.CENTER);
        aplicarFuenteRoboto(tablaClientes);
    }

    public void setActionListener(ClientesListeners listener) {
        this.clientesListeners = listener;
    }

    private class RenderAcciones implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionado, boolean tieneFoco, int fila, int columna) {
            if (valor instanceof ClienteEntity cliente) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                panel.setOpaque(false);
                JButton btnVer = crearBotonIcono(FontAwesomeSolid.EYE, "Ver cliente", new Color(60, 130, 200));
                JButton btnToggle = crearBotonIcono(
                        cliente.isEsta_activo() ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                        cliente.isEsta_activo() ? "Inactivar" : "Activar",
                        cliente.isEsta_activo() ? new Color(0, 180, 0) : Color.RED);
                JButton btnEliminar = crearBotonIcono(FontAwesomeSolid.TRASH_ALT, "Eliminar cliente", Color.RED);
                panel.add(btnVer);
                panel.add(btnToggle);
                panel.add(btnEliminar);
                panel.setBackground(seleccionado ? tabla.getSelectionBackground() : tabla.getBackground());
                aplicarFuenteRoboto(panel);
                return panel;
            }
            JLabel label = new JLabel("");
            aplicarFuenteRoboto(label);
            return label;
        }
    }

    private class EditorAcciones extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel;
        private ClienteEntity cliente;
        public EditorAcciones() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setOpaque(false);
        }
        @Override
        public Component getTableCellEditorComponent(JTable tabla, Object valor, boolean seleccionado, int fila, int columna) {
            if (valor instanceof ClienteEntity) {
                cliente = (ClienteEntity) valor;
                panel.removeAll();
                JButton btnVer = crearBotonAccion(FontAwesomeSolid.EYE, "Ver cliente", new Color(60, 130, 200), e -> {
                    if (clientesListeners != null) clientesListeners.verCliente(cliente);
                    fireEditingStopped();
                });
                JButton btnToggle = crearBotonAccion(
                        cliente.isEsta_activo() ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                        cliente.isEsta_activo() ? "Inactivar" : "Activar",
                        cliente.isEsta_activo() ? new Color(0, 180, 0) : Color.RED,
                        e -> {
                            cliente.setEsta_activo(!cliente.isEsta_activo());
                            modeloTabla.setValueAt(cliente.isEsta_activo() ? "Activo" : "Inactivo", fila, 3);
                            modeloTabla.setValueAt(cliente, fila, 4);
                            if (clientesListeners != null) clientesListeners.actualizarCliente(cliente);
                            fireEditingStopped();
                        });
                JButton btnEliminar = crearBotonAccion(FontAwesomeSolid.TRASH_ALT, "Eliminar cliente", Color.RED, e -> {
                    if (clientesListeners != null) clientesListeners.eliminarCliente(cliente);
                    fireEditingStopped();
                });
                panel.add(btnVer);
                panel.add(btnToggle);
                panel.add(btnEliminar);
                aplicarFuenteRoboto(panel);
            }
            return panel;
        }
        @Override
        public Object getCellEditorValue() {
            return cliente;
        }
        @Override
        public boolean isCellEditable(java.util.EventObject evento) {
            return !(evento instanceof MouseEvent) || ((MouseEvent) evento).getClickCount() == 1;
        }
    }

    private JButton crearBotonIcono(FontAwesomeSolid icono, String tooltip, Color color) {
        FontIcon icon = FontIcon.of(icono, 18, color);
        JButton boton = new JButton(icon);
        boton.setToolTipText(tooltip);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(36, 36));
        aplicarFuenteRoboto(boton);
        return boton;
    }

    private JButton crearBotonAccion(FontAwesomeSolid icono, String tooltip, Color color, ActionListener accion) {
        JButton boton = crearBotonIcono(icono, tooltip, color);
        boton.addActionListener(accion);
        return boton;
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

    private void aplicarFuenteRoboto(Component componente) {
        Font fuenteRoboto = new Font("Roboto", Font.PLAIN, 14);
        if (componente instanceof JLabel label) {
            label.setFont(fuenteRoboto);
        } else if (componente instanceof JButton boton) {
            boton.setFont(fuenteRoboto);
        } else if (componente instanceof JPanel panel) {
            for (Component hijo : panel.getComponents()) {
                aplicarFuenteRoboto(hijo);
            }
        } else if (componente instanceof JTable tabla) {
            tabla.setFont(fuenteRoboto);
        }
    }
}
