package com.hersac.ui.views.terceros.tablas;

import com.hersac.core.modules.terceros.entities.TerceroEntity;
import com.hersac.ui.views.terceros.listeners.TercerosListeners;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class TercerosTable extends JPanel {
    private final JTable tablaTerceros;
    private final DefaultTableModel modeloTabla;
    private TercerosListeners tercerosListeners;

    public TercerosTable() {
        setLayout(new BorderLayout());
        String[] columnas = {"ID", "Nombre", "Tipo", "Estado", "Acciones"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        tablaTerceros = new JTable(modeloTabla);
        tablaTerceros.setRowHeight(40);
        tablaTerceros.setShowGrid(false);
        // TODO: agregar renderizado y editor de acciones
        JScrollPane scrollPane = new JScrollPane(tablaTerceros);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(900, 400));
        scrollPane.setMaximumSize(new Dimension(900, Integer.MAX_VALUE));
        add(scrollPane, BorderLayout.CENTER);
    }

    public void setTerceros(List<TerceroEntity> terceros) {
        terceros.sort(Comparator.comparing(TerceroEntity::getTerceroId));
        modeloTabla.setRowCount(0);
        for (TerceroEntity tercero : terceros) {
            modeloTabla.addRow(new Object[]{
                    tercero.getTerceroId(),
                    tercero.getNombre(),
                    tercero.getTipo(),
                    tercero.getEstado(),
                    tercero
            });
        }
    }

    public void setActionListener(TercerosListeners listener) {
        this.tercerosListeners = listener;
    }

    public JTable getTablaTerceros() {
        return tablaTerceros;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }
}
