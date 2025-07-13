package com.hersac.ui.views.comercial.inventario.tablas;

import com.hersac.core.modules.movimientosinventarios.entities.MovimientoInventarioEntity;
import javax.swing.table.AbstractTableModel;
import javax.swing.JTable;
import java.util.List;
import java.time.format.DateTimeFormatter;

public class MovimientosInventarioTable extends JTable {
    private MovimientosInventarioTableModel model;

    public MovimientosInventarioTable() {
        model = new MovimientosInventarioTableModel(List.of());
        setModel(model);
        setRowHeight(28);
    }

    public void setMovimientos(List<MovimientoInventarioEntity> movimientos) {
        model.setMovimientos(movimientos);
    }

    private static class MovimientosInventarioTableModel extends AbstractTableModel {
        private List<MovimientoInventarioEntity> movimientos;
        private final String[] columnas = {"Fecha", "Producto", "Cantidad", "Tipo", "Bodega", "Usuario", "Ref. Tipo", "Ref. ID"};
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        public MovimientosInventarioTableModel(List<MovimientoInventarioEntity> movimientos) {
            this.movimientos = movimientos;
        }

        public void setMovimientos(List<MovimientoInventarioEntity> movimientos) {
            this.movimientos = movimientos;
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return movimientos != null ? movimientos.size() : 0;
        }

        @Override
        public int getColumnCount() {
            return columnas.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnas[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            MovimientoInventarioEntity m = movimientos.get(rowIndex);
            switch (columnIndex) {
                case 0: return m.getFechaCreacion() != null ? m.getFechaCreacion().format(formatter) : "";
                case 1: return m.getItem() != null ? m.getItem().getNombre() : "";
                case 2: return m.getCantidad();
                case 3: return m.getTipoMovimiento();
                case 4: return m.getBodega() != null ? m.getBodega().getNombre() : "";
                case 5: return m.getUsuarioCreacion() != null ? m.getUsuarioCreacion().getNombre() : "";
                case 6:
                    switch(m.getReferenciaTipo()) {
                        case 1: return "COMPRA";
                        case 2: return "VENTA";
                        default: return "";
                    }
                case 7: return m.getReferenciaId();
                default: return "";
            }
        }
    }
}

