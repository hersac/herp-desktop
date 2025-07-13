package com.hersac.ui.views.comercial.inventario.tablas;

import com.hersac.core.modules.movimientosinventarios.entities.MovimientoInventarioEntity;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;
import java.time.format.DateTimeFormatter;

public class MovimientosInventarioTable extends JTable {
    private ModeloTablaMovimientos modelo;

    public MovimientosInventarioTable() {
        modelo = new ModeloTablaMovimientos(List.of());
        setModel(modelo);
        setRowHeight(28);
        getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
        setFont(new Font("Roboto", Font.PLAIN, 13));
    }

    public void establecerMovimientos(List<MovimientoInventarioEntity> movimientos) {
        modelo.establecerMovimientos(movimientos);
    }

    private static class ModeloTablaMovimientos extends AbstractTableModel {
        private List<MovimientoInventarioEntity> movimientos;
        private final String[] columnas = {"Fecha", "Producto", "Cantidad", "Tipo", "Bodega", "Usuario", "Ref. Tipo", "Ref. ID"};
        private final DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        public ModeloTablaMovimientos(List<MovimientoInventarioEntity> movimientos) {
            this.movimientos = movimientos;
        }

        public void establecerMovimientos(List<MovimientoInventarioEntity> movimientos) {
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
        public String getColumnName(int columna) {
            return columnas[columna];
        }

        @Override
        public Object getValueAt(int fila, int columna) {
            MovimientoInventarioEntity m = movimientos.get(fila);
            if (columna == 0) return m.getFechaCreacion() != null ? m.getFechaCreacion().format(formato) : "";
            if (columna == 1) return m.getItem() != null ? m.getItem().getNombre() : "";
            if (columna == 2) return m.getCantidad();
            if (columna == 3) return m.getTipoMovimiento();
            if (columna == 4) return m.getBodega() != null ? m.getBodega().getNombre() : "";
            if (columna == 5) return m.getUsuarioCreacion() != null ? m.getUsuarioCreacion().getNombre() : "";
            if (columna == 6) {
                if (m.getReferenciaTipo() == 1) return "COMPRA";
                if (m.getReferenciaTipo() == 2) return "VENTA";
                return "";
            }
            if (columna == 7) return m.getReferenciaId();
            return "";
        }
    }
}
