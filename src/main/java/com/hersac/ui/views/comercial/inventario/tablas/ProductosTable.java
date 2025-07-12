package com.hersac.ui.views.comercial.inventario.tablas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.EventObject;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import com.hersac.core.globals.servicios.PermissionService;
import com.hersac.core.modules.productos.entities.ProductoEntity;
import com.hersac.ui.views.comercial.inventario.listeners.ProductosListeners;

public class ProductosTable extends JPanel {
    private final JTable tablaProductos;
    private final DefaultTableModel modeloTabla;
    private ProductosListeners productosListeners;
    private final PermissionService permissionService = new PermissionService(null);

    public ProductosTable() {
        setLayout(new BorderLayout());

        String[] columnas = { "ID", "Código", "Nombre", "Descripción", "Unidad", "Precio Base", "Estado", "Acciones" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };

        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setRowHeight(40);
        tablaProductos.setShowGrid(false);
        tablaProductos.getColumnModel().getColumn(7).setCellRenderer(new AccionesRenderer());
        tablaProductos.getColumnModel().getColumn(7).setCellEditor(new AccionesEditor());
        tablaProductos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = tablaProductos.columnAtPoint(e.getPoint());
                int row = tablaProductos.rowAtPoint(e.getPoint());

                if (column == 7 && row >= 0 && tablaProductos.isCellEditable(row, column)) {
                    tablaProductos.editCellAt(row, column);
                    tablaProductos.getEditorComponent().requestFocusInWindow();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(1200, 400));
        scrollPane.setMaximumSize(new Dimension(1200, Integer.MAX_VALUE));
        add(scrollPane, BorderLayout.CENTER);
    }

    public void setProductos(List<ProductoEntity> productos) {
        productos.sort(Comparator.comparingLong(ProductoEntity::getProductoId));

        modeloTabla.setRowCount(0);
        for (ProductoEntity producto : productos) {
            modeloTabla.addRow(new Object[] {
                    producto.getProductoId(),
                    producto.getCodigo(),
                    producto.getNombre(),
                    producto.getDescripcion(),
                    producto.getUnidadMedida(),
                    producto.getPrecioBase(),
                    producto.isEstadoActivo() ? "Activo" : "Inactivo",
                    producto
            });
        }
    }

    public void setActionListener(ProductosListeners listener) {
        this.productosListeners = listener;
    }

    private class AccionesRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            if (value instanceof ProductoEntity producto) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                panel.setOpaque(false);
                JButton btnVer = crearIconoBoton(FontAwesomeSolid.EYE, "Ver producto", new Color(60, 130, 200));
                panel.add(btnVer);
                boolean activo = producto.isEstadoActivo();
                JButton btnToggle = crearIconoBoton(
                    activo ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                    activo ? "Inactivar" : "Activar",
                    activo ? new Color(0, 180, 0) : Color.RED);
                panel.add(btnToggle);
                JButton btnEliminar = crearIconoBoton(FontAwesomeSolid.TRASH_ALT, "Eliminar producto", Color.RED);
                panel.add(btnEliminar);
                panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                return panel;
            }
            return new JLabel("");
        }
    }

    private class AccionesEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel;
        private ProductoEntity producto;

        public AccionesEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setOpaque(false);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            if (value instanceof ProductoEntity) {
                producto = (ProductoEntity) value;
                panel.removeAll();
                boolean activo = producto.isEstadoActivo();
                JButton btnVer = crearBotonAccion(FontAwesomeSolid.EYE, "Ver producto", new Color(60, 130, 200), e -> {
                    if (productosListeners != null)
                        productosListeners.verProducto(producto);
                    fireEditingStopped();
                });
                panel.add(btnVer);
                JButton btnToggle = crearBotonAccion(
                    activo ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                    activo ? "Inactivar" : "Activar",
                    activo ? new Color(0, 180, 0) : Color.RED,
                    e -> {
                        producto.setEstadoActivo(!producto.isEstadoActivo());
                        modeloTabla.setValueAt(producto.isEstadoActivo() ? "Activo" : "Inactivo", row, 6);
                        modeloTabla.setValueAt(producto, row, 7);
                        if (productosListeners != null)
                            productosListeners.actualizarProducto(producto);
                        fireEditingStopped();
                    });
                panel.add(btnToggle);
                JButton btnEliminar = crearBotonAccion(FontAwesomeSolid.TRASH_ALT, "Eliminar producto", Color.RED, e -> {
                    if (productosListeners != null)
                        productosListeners.eliminarProducto(producto);
                    fireEditingStopped();
                });
                panel.add(btnEliminar);
            }
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return producto;
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return !(e instanceof MouseEvent) || ((MouseEvent) e).getClickCount() == 1;
        }
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

    public JTable getTablaProductos() {
        return tablaProductos;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }
}
