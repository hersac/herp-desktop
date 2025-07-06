package com.hersac.ui.views.usuarios.rolesPermisos;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

public class PermisosTable extends JPanel {
    private JTable table;
    private PermisosTableModel model;

    public PermisosTable(String[] submodulos) {
        setLayout(new BorderLayout(10, 10));
        model = new PermisosTableModel(submodulos);
        table = new JTable(model);
        table.setRowHeight(28);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void setSubmodulos(String[] submodulos) {
        model.setSubmodulos(submodulos);
    }

    public PermisosTableModel getModel() {
        return model;
    }

    public static class PermisosTableModel extends AbstractTableModel {
        private String[] submodulos;
        private Object[][] data;
        private static final String[] COLUMNAS = {"Submódulo", "Ver", "Crear", "Editar", "Eliminar"};

        public PermisosTableModel(String[] submodulos) {
            setSubmodulos(submodulos);
        }
        public void setSubmodulos(String[] submodulos) {
            this.submodulos = submodulos;
            data = new Object[submodulos.length][5];
            for (int i = 0; i < submodulos.length; i++) {
                data[i][0] = submodulos[i];
                for (int j = 1; j < 5; j++) {
                    data[i][j] = false;
                }
            }
            fireTableDataChanged();
        }
        @Override
        public int getRowCount() { return data.length; }
        @Override
        public int getColumnCount() { return COLUMNAS.length; }
        @Override
        public String getColumnName(int column) { return COLUMNAS[column]; }
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) { return data[rowIndex][columnIndex]; }
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) { return columnIndex > 0; }
        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            data[rowIndex][columnIndex] = aValue;
            fireTableCellUpdated(rowIndex, columnIndex);
        }
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) return String.class;
            return Boolean.class;
        }
    }
}
