package com.hersac.ui.views.usuarios.rolesPermisos.tablas;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

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
    public void setSubmodulos(String[] submodulos, Map<String, Boolean[]> seleccionados) {
        model.setSubmodulos(submodulos, seleccionados);
    }

    public PermisosTableModel getModel() {
        return model;
    }

    public boolean[][] getPermisosSeleccionados() {
        return model.getPermisosSeleccionados();
    }
    public String[] getSubmodulos() {
        return model.getSubmodulos();
    }
    public Map<String, Boolean[]> getSeleccionadosMap() {
        return model.getSeleccionadosMap();
    }

    public static class PermisosTableModel extends AbstractTableModel {
        private String[] submodulos;
        private Boolean[][] data;
        private static final String[] COLUMNAS = {"Submódulo", "Ver", "Crear", "Editar", "Eliminar"};

        public PermisosTableModel(String[] submodulos) {
            setSubmodulos(submodulos);
        }
        public void setSubmodulos(String[] submodulos) {
            Map<String, Boolean[]> prevSeleccion = new HashMap<>();
            if (this.submodulos != null && data != null) {
                for (int i = 0; i < this.submodulos.length; i++) {
                    Boolean[] permisos = new Boolean[4];
                    for (int j = 0; j < 4; j++) {
                        permisos[j] = Boolean.TRUE.equals(data[i][j]);
                    }
                    prevSeleccion.put(this.submodulos[i], permisos);
                }
            }
            this.submodulos = submodulos;
            data = new Boolean[submodulos.length][4];
            for (int i = 0; i < submodulos.length; i++) {
                Boolean[] permisos = prevSeleccion.get(submodulos[i]);
                for (int j = 0; j < 4; j++) {
                    if (permisos != null) {
                        data[i][j] = permisos[j];
                    } else {
                        data[i][j] = Boolean.FALSE;
                    }
                }
            }
            fireTableDataChanged();
        }
        public void setSubmodulos(String[] submodulos, Map<String, Boolean[]> seleccionados) {
            this.submodulos = submodulos;
            data = new Boolean[submodulos.length][4];
            for (int i = 0; i < submodulos.length; i++) {
                Boolean[] permisos = seleccionados != null ? seleccionados.get(submodulos[i]) : null;
                for (int j = 0; j < 4; j++) {
                    if (permisos != null) {
                        data[i][j] = permisos[j];
                    } else {
                        data[i][j] = Boolean.FALSE;
                    }
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
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) return submodulos[rowIndex];
            return data[rowIndex][columnIndex - 1];
        }
        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex > 0) {
                data[rowIndex][columnIndex - 1] = (Boolean) aValue;
                fireTableCellUpdated(rowIndex, columnIndex);
            }
        }
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) { return columnIndex > 0; }
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) return String.class;
            return Boolean.class;
        }
        public boolean[][] getPermisosSeleccionados() {
            boolean[][] seleccionados = new boolean[data.length][data[0].length + 1];
            for (int i = 0; i < data.length; i++) {
                seleccionados[i][0] = false;
                for (int j = 1; j < seleccionados[i].length; j++) {
                    if ((j - 1) < data[i].length) {
                        seleccionados[i][j] = Boolean.TRUE.equals(data[i][j - 1]);
                        System.out.println(": Seleccionado[" + i + "][" + j + "] = " + seleccionados[i][j]);
                    } else {
                        seleccionados[i][j] = false;
                    }
                }
            }
            return seleccionados;
        }
        public Object[] getPermisosSeleccionadosLineal() {
            Object[] seleccionados = new Object[data.length * 5];
            for (int i = 0; i < data.length; i++) {
                int base = i * 5;
                seleccionados[base] = submodulos[i];
                for (int j = 1; j <= 4; j++) {
                    int idx = base + j;
                    int idPermiso = idx - (idx / 5);
                    if (idx % 5 != 0) {
                        seleccionados[idx] = Boolean.TRUE.equals(data[i][j - 1]);
                    } else {
                        seleccionados[idx] = null;
                    }
                }
            }
            return seleccionados;
        }
        // Método auxiliar para contar el total de permisos
        private int getTotalPermisos() {
            int total = 0;
            for (int i = 0; i < data.length; i++) {
                total += data[i].length;
            }
            return total;
        }
        public String[] getSubmodulos() {
            return submodulos;
        }
        public Map<String, Boolean[]> getSeleccionadosMap() {
            Map<String, Boolean[]> map = new HashMap<>();
            for (int i = 0; i < submodulos.length; i++) {
                Boolean[] permisos = new Boolean[4];
                for (int j = 0; j < 4; j++) {
                    permisos[j] = Boolean.TRUE.equals(data[i][j]);
                }
                map.put(submodulos[i], permisos);
            }
            return map;
        }
    }
}
