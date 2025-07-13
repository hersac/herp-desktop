package com.hersac.ui.views.comercial.inventario.tablas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.EventObject;
import java.util.List;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;
import com.hersac.core.modules.productos.entities.ProductoEntity;
import com.hersac.ui.views.comercial.inventario.listeners.ProductosListeners;

public class ProductosTable extends JPanel {
    private final JTable tablaProductos;
    private final DefaultTableModel modeloTabla;
    private ProductosListeners productosOyente;

    public ProductosTable() {
        setLayout(new BorderLayout());
        String[] columnas = { "ID", "Código", "Nombre", "Descripción", "Unidad", "Precio Base", "Estado", "Acciones" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return columna == 7;
            }
        };
        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setRowHeight(40);
        tablaProductos.setShowGrid(false);
        tablaProductos.getColumnModel().getColumn(5).setCellRenderer(new PrecioBaseRenderer()); // Formato moneda y alineación derecha
        tablaProductos.getColumnModel().getColumn(7).setCellRenderer(new AccionesRenderizador());
        tablaProductos.getColumnModel().getColumn(7).setCellEditor(new AccionesEditor());
        tablaProductos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int columna = tablaProductos.columnAtPoint(e.getPoint());
                int fila = tablaProductos.rowAtPoint(e.getPoint());
                if (columna == 7 && fila >= 0 && tablaProductos.isCellEditable(fila, columna)) {
                    tablaProductos.editCellAt(fila, columna);
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

    public void setActionListener(ProductosListeners oyente) {
        this.productosOyente = oyente;
    }

    private class AccionesRenderizador implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionado, boolean tieneFoco, int fila, int columna) {
            if (valor instanceof ProductoEntity producto) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                panel.setOpaque(false);
                JButton btnVer = crearBotonIcono(FontAwesomeSolid.EYE, "Ver producto", new Color(60, 130, 200));
                panel.add(btnVer);
                boolean activo = producto.isEstadoActivo();
                JButton btnToggle = crearBotonIcono(
                    activo ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                    activo ? "Inactivar" : "Activar",
                    activo ? new Color(0, 180, 0) : Color.RED);
                panel.add(btnToggle);
                JButton btnEliminar = crearBotonIcono(FontAwesomeSolid.TRASH_ALT, "Eliminar producto", Color.RED);
                panel.add(btnEliminar);
                panel.setBackground(seleccionado ? tabla.getSelectionBackground() : tabla.getBackground());
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
        public Component getTableCellEditorComponent(JTable tabla, Object valor, boolean seleccionado, int fila, int columna) {
            if (valor instanceof ProductoEntity) {
                producto = (ProductoEntity) valor;
                panel.removeAll();
                boolean activo = producto.isEstadoActivo();
                JButton btnVer = crearBotonAccion(FontAwesomeSolid.EYE, "Ver producto", new Color(60, 130, 200), e -> {
                    if (productosOyente != null)
                        productosOyente.verProducto(producto);
                    fireEditingStopped();
                });
                panel.add(btnVer);
                JButton btnToggle = crearBotonAccion(
                    activo ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                    activo ? "Inactivar" : "Activar",
                    activo ? new Color(0, 180, 0) : Color.RED,
                    e -> {
                        producto.setEstadoActivo(!producto.isEstadoActivo());
                        modeloTabla.setValueAt(producto.isEstadoActivo() ? "Activo" : "Inactivo", fila, 6);
                        modeloTabla.setValueAt(producto, fila, 7);
                        if (productosOyente != null)
                            productosOyente.actualizarProducto(producto);
                        fireEditingStopped();
                    });
                panel.add(btnToggle);
                JButton btnEliminar = crearBotonAccion(FontAwesomeSolid.TRASH_ALT, "Eliminar producto", Color.RED, e -> {
                    if (productosOyente != null)
                        productosOyente.eliminarProducto(producto);
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

    public JTable getTablaProductos() {
        return tablaProductos;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    // Renderer personalizado para precio base
    private static class PrecioBaseRenderer extends JLabel implements TableCellRenderer {
        public PrecioBaseRenderer() {
            setHorizontalAlignment(SwingConstants.RIGHT);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Number) {
                setText(String.format("$%,.2f", ((Number) value).doubleValue()));
            } else {
                setText("");
            }
            setOpaque(true);
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            return this;
        }
    }
}
