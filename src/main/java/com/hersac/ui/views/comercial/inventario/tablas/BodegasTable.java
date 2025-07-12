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

import com.hersac.ui.views.comercial.inventario.contenidos.bodegas.GestionBodegas;
import com.hersac.ui.views.comercial.inventario.modales.RegistrarBodega;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;
import com.hersac.ui.views.comercial.inventario.listeners.BodegasListeners;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BodegasTable extends JPanel {
    private final JTable tablaBodegas;
    private final DefaultTableModel modeloTabla;
    private BodegasListeners bodegasListeners;

    public BodegasTable() {
        setLayout(new BorderLayout());
        String[] columnas = { "ID", "Nombre", "Estado", "Fecha Creación", "Acciones" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        tablaBodegas = new JTable(modeloTabla);
        tablaBodegas.setRowHeight(40);
        tablaBodegas.setShowGrid(false);
        tablaBodegas.getColumnModel().getColumn(4).setCellRenderer(new AccionesRenderer());
        tablaBodegas.getColumnModel().getColumn(4).setCellEditor(new AccionesEditor());
        tablaBodegas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = tablaBodegas.columnAtPoint(e.getPoint());
                int row = tablaBodegas.rowAtPoint(e.getPoint());
                if (column == 4 && row >= 0 && tablaBodegas.isCellEditable(row, column)) {
                    tablaBodegas.editCellAt(row, column);
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

    public void setActionListener(BodegasListeners listener) {
        this.bodegasListeners = listener;
    }

    private class AccionesRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            if (value instanceof BodegaEntity bodega) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                panel.setOpaque(false);
                JButton btnVer = crearIconoBoton(FontAwesomeSolid.EYE, "Ver bodega", new Color(60, 130, 200));
                panel.add(btnVer);
                boolean activa = Boolean.TRUE.equals(bodega.getEstaActiva());
                JButton btnToggle = crearIconoBoton(
                    activa ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                    activa ? "Inactivar" : "Activar",
                    activa ? new Color(0, 180, 0) : Color.RED);
                panel.add(btnToggle);
                JButton btnEliminar = crearIconoBoton(FontAwesomeSolid.TRASH_ALT, "Eliminar bodega", Color.RED);
                panel.add(btnEliminar);
                panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
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
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            if (value instanceof BodegaEntity) {
                bodega = (BodegaEntity) value;
                panel.removeAll();
                boolean activa = Boolean.TRUE.equals(bodega.getEstaActiva());
                JButton btnVer = crearBotonAccion(FontAwesomeSolid.EYE, "Ver bodega", new Color(60, 130, 200), e -> {
                    JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(BodegasTable.this);
                    GestionBodegas gestion = (GestionBodegas)bodegasListeners;
                    new RegistrarBodega(parentFrame,
                        gestion.getDIContainer(),
                        bodegasListeners, bodega, true);
                    fireEditingStopped();
                });
                panel.add(btnVer);
                JButton btnToggle = crearBotonAccion(
                    activa ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                    activa ? "Inactivar" : "Activar",
                    activa ? new Color(0, 180, 0) : Color.RED,
                    e -> {
                        bodega.setEstaActiva(!activa);
                        if (bodegasListeners != null)
                            bodegasListeners.actualizarBodega(bodega);
                        fireEditingStopped();
                    });
                panel.add(btnToggle);
                JButton btnEliminar = crearBotonAccion(FontAwesomeSolid.TRASH_ALT, "Eliminar bodega", Color.RED, e -> {
                    if (bodegasListeners != null)
                        bodegasListeners.eliminarBodega(bodega);
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

    private JButton crearBotonAccion(FontAwesomeSolid icono, String tooltip, Color color, ActionListener action) {
        JButton button = crearIconoBoton(icono, tooltip, color);
        button.addActionListener(action);
        return button;
    }
}

