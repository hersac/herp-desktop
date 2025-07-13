package com.hersac.ui.views.comercial.inventario.tablas;

import com.hersac.core.modules.proveedores.entities.ProveedorEntity;
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
    private ProveedorListeners oyenteProveedor;

    public ProveedoresTable() {
        setLayout(new BorderLayout());
        String[] columnas = {"ID", "Nombre", "Tipo", "Estado", "Acciones"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return columna == 4;
            }
        };
        tablaProveedores = new JTable(modeloTabla);
        tablaProveedores.setRowHeight(40);
        tablaProveedores.setShowGrid(false);
        tablaProveedores.getColumnModel().getColumn(4).setCellRenderer(new AccionesRenderizador());
        tablaProveedores.getColumnModel().getColumn(4).setCellEditor(new AccionesEditor());
        tablaProveedores.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int columna = tablaProveedores.columnAtPoint(e.getPoint());
                int fila = tablaProveedores.rowAtPoint(e.getPoint());
                if (columna == 4 && fila >= 0 && tablaProveedores.isCellEditable(fila, columna)) {
                    tablaProveedores.editCellAt(fila, columna);
                    tablaProveedores.getEditorComponent().requestFocusInWindow();
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(tablaProveedores);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(1200, 400));
        scrollPane.setMaximumSize(new Dimension(1200, Integer.MAX_VALUE));
        add(scrollPane, BorderLayout.CENTER);
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

    public void setActionListener(ProveedorListeners oyente) {
        this.oyenteProveedor = oyente;
    }

    private class AccionesRenderizador implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionado, boolean tieneFoco, int fila, int columna) {
            if (valor instanceof ProveedorEntity proveedor) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                panel.setOpaque(false);
                JButton btnVer = crearBotonIcono(FontAwesomeSolid.EYE, "Ver proveedor", new Color(60, 130, 200));
                panel.add(btnVer);
                boolean activo = proveedor.isEsta_activo();
                JButton btnToggle = crearBotonIcono(
                    activo ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                    activo ? "Inactivar" : "Activar",
                    activo ? new Color(0, 180, 0) : Color.RED);
                panel.add(btnToggle);
                JButton btnEliminar = crearBotonIcono(FontAwesomeSolid.TRASH_ALT, "Eliminar proveedor", Color.RED);
                panel.add(btnEliminar);
                panel.setBackground(seleccionado ? tabla.getSelectionBackground() : tabla.getBackground());
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
        public Component getTableCellEditorComponent(JTable tabla, Object valor, boolean seleccionado, int fila, int columna) {
            if (valor instanceof ProveedorEntity) {
                proveedor = (ProveedorEntity) valor;
                panel.removeAll();
                boolean activo = proveedor.isEsta_activo();
                JButton btnVer = crearBotonAccion(FontAwesomeSolid.EYE, "Ver proveedor", new Color(60, 130, 200), e -> {
                    if (oyenteProveedor != null) oyenteProveedor.verProveedor(proveedor);
                    fireEditingStopped();
                });
                panel.add(btnVer);
                JButton btnToggle = crearBotonAccion(
                    activo ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                    activo ? "Inactivar" : "Activar",
                    activo ? new Color(0, 180, 0) : Color.RED,
                    e -> {
                        proveedor.setEsta_activo(!proveedor.isEsta_activo());
                        modeloTabla.setValueAt(proveedor.isEsta_activo() ? "Activo" : "Inactivo", fila, 3);
                        modeloTabla.setValueAt(proveedor, fila, 4);
                        if (oyenteProveedor != null) oyenteProveedor.actualizarProveedor(proveedor);
                        fireEditingStopped();
                    });
                panel.add(btnToggle);
                JButton btnEliminar = crearBotonAccion(FontAwesomeSolid.TRASH_ALT, "Eliminar proveedor", Color.RED, e -> {
                    if (oyenteProveedor != null) oyenteProveedor.eliminarProveedor(proveedor);
                    fireEditingStopped();
                });
                panel.add(btnEliminar);
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
        return boton;
    }

    private JButton crearBotonAccion(FontAwesomeSolid icono, String tooltip, Color color, ActionListener accion) {
        JButton boton = crearBotonIcono(icono, tooltip, color);
        boton.addActionListener(accion);
        return boton;
    }
}
