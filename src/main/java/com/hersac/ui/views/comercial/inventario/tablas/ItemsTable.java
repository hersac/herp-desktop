package com.hersac.ui.views.comercial.inventario.tablas;

import com.hersac.core.modules.items.entities.ItemEntity;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Comparator;
import java.util.List;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;
import com.hersac.ui.views.comercial.inventario.listeners.ItemsListeners;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ItemsTable extends JPanel {
    private final JTable tablaItems;
    private final DefaultTableModel modeloTabla;
    private ItemsListeners oyenteItems;

    public ItemsTable() {
        setLayout(new BorderLayout());
        String[] columnas = { "ID", "Código", "Nombre", "Descripción", "Precio Unitario", "Stock", "Estado", "Bodega", "Acciones" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return columna == 8;
            }
        };
        tablaItems = new JTable(modeloTabla);
        tablaItems.setRowHeight(40);
        tablaItems.setShowGrid(false);
        tablaItems.getColumnModel().getColumn(8).setCellRenderer(new AccionesRenderizador());
        tablaItems.getColumnModel().getColumn(8).setCellEditor(new AccionesEditor());
        tablaItems.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int columna = tablaItems.columnAtPoint(e.getPoint());
                int fila = tablaItems.rowAtPoint(e.getPoint());
                if (columna == 8 && fila >= 0 && tablaItems.isCellEditable(fila, columna)) {
                    tablaItems.editCellAt(fila, columna);
                    tablaItems.getEditorComponent().requestFocusInWindow();
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(tablaItems);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(1200, 400));
        scrollPane.setMaximumSize(new Dimension(1200, Integer.MAX_VALUE));
        add(scrollPane, BorderLayout.CENTER);
    }

    public void setItems(List<ItemEntity> items) {
        items.sort(Comparator.comparingLong(ItemEntity::getItemId));
        modeloTabla.setRowCount(0);
        for (ItemEntity item : items) {
            modeloTabla.addRow(new Object[] {
                item.getItemId(),
                item.getCodigo(),
                item.getNombre(),
                item.getDescripcion(),
                item.getPrecioUnitario(),
                item.getStock(),
                item.isEstaActivo() ? "Activo" : "Inactivo",
                item.getBodega() != null ? item.getBodega().getNombre() : "",
                item
            });
        }
    }

    public void setActionListener(ItemsListeners oyente) {
        this.oyenteItems = oyente;
    }

    private class AccionesRenderizador implements javax.swing.table.TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionado, boolean tieneFoco, int fila, int columna) {
            if (valor instanceof ItemEntity item) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                panel.setOpaque(false);
                JButton btnVer = crearBotonIcono(FontAwesomeSolid.EYE, "Ver item", new Color(60, 130, 200));
                panel.add(btnVer);
                boolean activo = item.isEstaActivo();
                JButton btnToggle = crearBotonIcono(
                    activo ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                    activo ? "Inactivar" : "Activar",
                    activo ? new Color(0, 180, 0) : Color.RED);
                panel.add(btnToggle);
                JButton btnEliminar = crearBotonIcono(FontAwesomeSolid.TRASH_ALT, "Eliminar item", Color.RED);
                panel.add(btnEliminar);
                panel.setBackground(seleccionado ? tabla.getSelectionBackground() : tabla.getBackground());
                return panel;
            }
            return new JLabel("");
        }
    }

    private class AccionesEditor extends AbstractCellEditor implements javax.swing.table.TableCellEditor {
        private final JPanel panel;
        private ItemEntity item;
        public AccionesEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setOpaque(false);
        }
        @Override
        public Component getTableCellEditorComponent(JTable tabla, Object valor, boolean seleccionado, int fila, int columna) {
            if (valor instanceof ItemEntity) {
                item = (ItemEntity) valor;
                panel.removeAll();
                boolean activo = item.isEstaActivo();
                JButton btnVer = crearBotonAccion(FontAwesomeSolid.EYE, "Ver item", new Color(60, 130, 200), e -> {
                    if (oyenteItems != null) oyenteItems.verItems(item);
                    fireEditingStopped();
                });
                panel.add(btnVer);
                JButton btnToggle = crearBotonAccion(
                    activo ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                    activo ? "Inactivar" : "Activar",
                    activo ? new Color(0, 180, 0) : Color.RED,
                    e -> {
                        item.setEstaActivo(!item.isEstaActivo());
                        if (oyenteItems != null) oyenteItems.actualizarItems(item);
                        fireEditingStopped();
                    });
                panel.add(btnToggle);
                JButton btnEliminar = crearBotonAccion(FontAwesomeSolid.TRASH_ALT, "Eliminar item", Color.RED, e -> {
                    if (oyenteItems != null) oyenteItems.eliminarItems(item);
                    fireEditingStopped();
                });
                panel.add(btnEliminar);
            }
            return panel;
        }
        @Override
        public Object getCellEditorValue() {
            return item;
        }
        @Override
        public boolean isCellEditable(java.util.EventObject e) {
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

    private JButton crearBotonAccion(FontAwesomeSolid icono, String tooltip, Color color, java.awt.event.ActionListener accion) {
        JButton boton = crearBotonIcono(icono, tooltip, color);
        boton.addActionListener(accion);
        return boton;
    }
}
