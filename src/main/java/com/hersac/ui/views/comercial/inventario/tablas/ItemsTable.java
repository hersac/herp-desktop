package com.hersac.ui.views.comercial.inventario.tablas;

import com.hersac.core.modules.items.entities.ItemEntity;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class ItemsTable extends JPanel {
    private final JTable tablaItems;
    private final DefaultTableModel modeloTabla;

    public ItemsTable() {
        setLayout(new BorderLayout());
        String[] columnas = { "ID", "Código", "Nombre", "Descripción", "Precio Unitario", "Stock", "Estado" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaItems = new JTable(modeloTabla);
        tablaItems.setRowHeight(40);
        tablaItems.setShowGrid(false);
        JScrollPane scrollPane = new JScrollPane(tablaItems);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(900, 400));
        scrollPane.setMaximumSize(new Dimension(900, Integer.MAX_VALUE));
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
                item.isEstaActivo() ? "Activo" : "Inactivo"
            });
        }
    }

    public JTable getTablaItems() {
        return tablaItems;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }
}
