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
import com.hersac.core.globals.servicios.PermissionService;
import com.hersac.ui.globals.enums.Permiso;

public class ItemsTable extends JPanel {
    private final JTable tablaItems;
    private final DefaultTableModel modeloTabla;
    private ItemsListeners itemsListeners;
    private final PermissionService permissionService = new PermissionService(null);

    public ItemsTable() {
        setLayout(new BorderLayout());
        String[] columnas = { "ID", "Código", "Nombre", "Descripción", "Precio Unitario", "Stock", "Estado", "Bodega", "Acciones" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8;
            }
        };
        tablaItems = new JTable(modeloTabla);
        tablaItems.setRowHeight(40);
        tablaItems.setShowGrid(false);
        tablaItems.getColumnModel().getColumn(8).setCellRenderer(new AccionesRenderer());
        tablaItems.getColumnModel().getColumn(8).setCellEditor(new AccionesEditor());
        tablaItems.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = tablaItems.columnAtPoint(e.getPoint());
                int row = tablaItems.rowAtPoint(e.getPoint());
                if (column == 8 && row >= 0 && tablaItems.isCellEditable(row, column)) {
                    tablaItems.editCellAt(row, column);
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

    public void setActionListener(ItemsListeners listener) {
        this.itemsListeners = listener;
    }

    private class AccionesRenderer implements javax.swing.table.TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            if (value instanceof ItemEntity item) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                panel.setOpaque(false);
                JButton btnVer = crearIconoBoton(FontAwesomeSolid.EYE, "Ver item", new Color(60, 130, 200));
                panel.add(btnVer);
                boolean activo = item.isEstaActivo();
                JButton btnToggle = crearIconoBoton(
                    activo ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                    activo ? "Inactivar" : "Activar",
                    activo ? new Color(0, 180, 0) : Color.RED);
                panel.add(btnToggle);
                JButton btnEliminar = crearIconoBoton(FontAwesomeSolid.TRASH_ALT, "Eliminar item", Color.RED);
                panel.add(btnEliminar);
                panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
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
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            if (value instanceof ItemEntity) {
                item = (ItemEntity) value;
                panel.removeAll();
                boolean activo = item.isEstaActivo();
                JButton btnVer = crearBotonAccion(FontAwesomeSolid.EYE, "Ver item", new Color(60, 130, 200), e -> {
                    JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(ItemsTable.this);
                    com.hersac.ui.views.comercial.inventario.contenidos.items.GestionItems gestion = (com.hersac.ui.views.comercial.inventario.contenidos.items.GestionItems)itemsListeners;
                    new com.hersac.ui.views.comercial.inventario.modales.RegistrarItem(parentFrame,
                        gestion.getDIContainer(),
                        itemsListeners, item, true);
                    fireEditingStopped();
                });
                panel.add(btnVer);
                JButton btnToggle = crearBotonAccion(
                    activo ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                    activo ? "Inactivar" : "Activar",
                    activo ? new Color(0, 180, 0) : Color.RED,
                    e -> {
                        item.setEstaActivo(!item.isEstaActivo());
                        if (itemsListeners != null)
                            itemsListeners.actualizarItems(item);
                        fireEditingStopped();
                    });
                panel.add(btnToggle);
                JButton btnEliminar = crearBotonAccion(FontAwesomeSolid.TRASH_ALT, "Eliminar item", Color.RED, e -> {
                    if (itemsListeners != null)
                        itemsListeners.eliminarItems(item);
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

    private JButton crearBotonAccion(FontAwesomeSolid icono, String tooltip, Color color, java.awt.event.ActionListener action) {
        JButton button = crearIconoBoton(icono, tooltip, color);
        button.addActionListener(action);
        return button;
    }
}
