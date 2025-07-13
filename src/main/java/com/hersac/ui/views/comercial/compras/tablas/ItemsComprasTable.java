package com.hersac.ui.views.comercial.compras.tablas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;

public class ItemsComprasTable extends JPanel {
    private JTable tablaItems;
    private DefaultTableModel modeloTabla;
    private final DecimalFormat formatoMoneda = new DecimalFormat("#,##0.00");

    public ItemsComprasTable() {
        setLayout(new BorderLayout());
        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Cantidad", "Precio Unitario", "Subtotal"}, 0);
        tablaItems = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaItems);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Productos Agregados"));
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        tablaItems.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        DefaultTableCellRenderer monedaRenderer = new DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                if (value instanceof Number) {
                    setText(formatoMoneda.format(((Number) value).doubleValue()));
                } else {
                    try {
                        setText(formatoMoneda.format(Double.parseDouble(value.toString())));
                    } catch (Exception e) {
                        setText(value != null ? value.toString() : "");
                    }
                }
            }
        };
        tablaItems.getColumnModel().getColumn(3).setCellRenderer(monedaRenderer);
        tablaItems.getColumnModel().getColumn(4).setCellRenderer(monedaRenderer);
        add(scrollTabla, BorderLayout.CENTER);
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }
    public JTable getTablaItems() {
        return tablaItems;
    }
    public void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }
}
