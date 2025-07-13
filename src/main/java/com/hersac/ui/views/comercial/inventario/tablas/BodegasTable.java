package com.hersac.ui.views.comercial.inventario.tablas;

import com.hersac.core.modules.bodegas.entities.BodegaEntity;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.EventObject;
import java.util.List;
import com.hersac.ui.views.comercial.inventario.listeners.BodegasListeners;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

public class BodegasTable extends JPanel {
    private final JTable tablaBodegas;
    private final DefaultTableModel modeloTabla;
    private BodegasListeners bodegasOyente;

    public BodegasTable() {
        setLayout(new BorderLayout());
        String[] columnas = { "ID", "Nombre", "Estado", "Fecha Creación", "Acciones" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return columna == 4;
            }
        };
        tablaBodegas = new JTable(modeloTabla);
        tablaBodegas.setRowHeight(40);
        tablaBodegas.setShowGrid(false);
        tablaBodegas.getColumnModel().getColumn(4).setCellRenderer(new AccionesRenderizador());
        tablaBodegas.getColumnModel().getColumn(4).setCellEditor(new AccionesEditor());
        tablaBodegas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int columna = tablaBodegas.columnAtPoint(e.getPoint());
                int fila = tablaBodegas.rowAtPoint(e.getPoint());
                if (columna == 4 && fila >= 0 && tablaBodegas.isCellEditable(fila, columna)) {
                    tablaBodegas.editCellAt(fila, columna);
                    tablaBodegas.getEditorComponent().requestFocusInWindow();
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(tablaBodegas);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(1200, 400));
        scrollPane.setMaximumSize(new Dimension(1200, Integer.MAX_VALUE));
        add(scrollPane, BorderLayout.CENTER);
    }

    public void setBodegas(List<BodegaEntity> bodegas) {
        bodegas.sort(Comparator.comparingLong(BodegaEntity::getBodegaId));
        modeloTabla.setRowCount(0);
        for (BodegaEntity bodega : bodegas) {
            modeloTabla.addRow(new Object[] {
                bodega.getBodegaId(),
                bodega.getNombre(),
                Boolean.TRUE.equals(bodega.getEstaActiva()) ? "Activa" : "Inactiva",
                bodega.getFechaCreacion(),
                bodega
            });
        }
    }

    public void setActionListener(BodegasListeners oyente) {
        this.bodegasOyente = oyente;
    }

    private class AccionesRenderizador implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionado, boolean tieneFoco, int fila, int columna) {
            if (valor instanceof BodegaEntity bodega) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                panel.setOpaque(false);
                JButton btnVer = crearBotonIcono(FontAwesomeSolid.EYE, "Ver bodega", new Color(60, 130, 200));
                panel.add(btnVer);
                boolean activa = Boolean.TRUE.equals(bodega.getEstaActiva());
                JButton btnToggle = crearBotonIcono(
                    activa ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                    activa ? "Inactivar" : "Activar",
                    activa ? new Color(0, 180, 0) : Color.RED);
                panel.add(btnToggle);
                JButton btnEliminar = crearBotonIcono(FontAwesomeSolid.TRASH_ALT, "Eliminar bodega", Color.RED);
                panel.add(btnEliminar);
                panel.setBackground(seleccionado ? tabla.getSelectionBackground() : tabla.getBackground());
                return panel;
            }
            return new JLabel("");
        }
    }

    private class AccionesEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel;
        private BodegaEntity bodega;
        public AccionesEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setOpaque(false);
        }
        @Override
        public Component getTableCellEditorComponent(JTable tabla, Object valor, boolean seleccionado, int fila, int columna) {
            if (valor instanceof BodegaEntity) {
                bodega = (BodegaEntity) valor;
                panel.removeAll();
                boolean activa = Boolean.TRUE.equals(bodega.getEstaActiva());
                JButton btnVer = crearBotonAccion(FontAwesomeSolid.EYE, "Ver bodega", new Color(60, 130, 200), e -> {
                    if (bodegasOyente != null) bodegasOyente.verBodega(bodega);
                    fireEditingStopped();
                });
                panel.add(btnVer);
                JButton btnToggle = crearBotonAccion(
                    activa ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                    activa ? "Inactivar" : "Activar",
                    activa ? new Color(0, 180, 0) : Color.RED,
                    e -> {
                        bodega.setEstaActiva(!activa);
                        if (bodegasOyente != null) bodegasOyente.actualizarBodega(bodega);
                        fireEditingStopped();
                    });
                panel.add(btnToggle);
                JButton btnEliminar = crearBotonAccion(FontAwesomeSolid.TRASH_ALT, "Eliminar bodega", Color.RED, e -> {
                    if (bodegasOyente != null) bodegasOyente.eliminarBodega(bodega);
                    fireEditingStopped();
                });
                panel.add(btnEliminar);
            }
            return panel;
        }
        @Override
        public Object getCellEditorValue() {
            return bodega;
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
}
