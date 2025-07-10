package com.hersac.ui.views.terceros.tablas;

import com.hersac.core.modules.terceros.entities.TerceroEntity;
import com.hersac.core.globals.servicios.PermissionService;
import com.hersac.ui.globals.enums.Permiso;
import com.hersac.ui.views.terceros.constantes.TiposTerceroEnum;
import com.hersac.ui.views.terceros.listeners.TercerosListeners;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EventObject;
import java.util.List;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class TercerosTable extends JPanel {
    private final JTable tablaTerceros;
    private final DefaultTableModel modeloTabla;
    private TercerosListeners tercerosListeners;
    private PermissionService permissionService;

    public TercerosTable() {
        this(null);
    }

    public TercerosTable(PermissionService permissionService) {
        this.permissionService = permissionService;
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
        tablaTerceros.getColumnModel().getColumn(4).setCellRenderer(new AccionesRenderer());
        tablaTerceros.getColumnModel().getColumn(4).setCellEditor(new AccionesEditor());
        tablaTerceros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = tablaTerceros.columnAtPoint(e.getPoint());
                int row = tablaTerceros.rowAtPoint(e.getPoint());
                if (column == 4 && row >= 0 && tablaTerceros.isCellEditable(row, column)) {
                    tablaTerceros.editCellAt(row, column);
                    tablaTerceros.getEditorComponent().requestFocusInWindow();
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(tablaTerceros);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(900, 400));
        scrollPane.setMaximumSize(new Dimension(900, Integer.MAX_VALUE));
        add(scrollPane, BorderLayout.CENTER);
    }

    public void setTerceros(List<TerceroEntity> terceros) {
        List<TerceroEntity> listaMutable = new ArrayList<>(terceros);
        listaMutable.sort(Comparator.comparing(TerceroEntity::getTerceroId));
        modeloTabla.setRowCount(0);
        for (TerceroEntity tercero : listaMutable) {
            String tipoNombre = "";
            if (tercero.getTipoPersona().getTipoPersonaId() != null) {
                TiposTerceroEnum tipoEnum = TiposTerceroEnum.fromId(tercero.getTipoPersona().getTipoPersonaId());
                tipoNombre = tipoEnum != null ? tipoEnum.getNombre() : String.valueOf(tercero.getTipoPersona().getTipoPersonaId());
            }
            modeloTabla.addRow(new Object[]{
                    tercero.getTerceroId(),
                    tercero.getNombre(),
                    tipoNombre,
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

    private class AccionesRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            if (value instanceof TerceroEntity tercero) {
                JPanel panel = crearPanelAcciones(tercero, false);
                panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                return panel;
            }
            return new JLabel("");
        }
    }

    private class AccionesEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel;
        private TerceroEntity tercero;

        public AccionesEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setOpaque(false);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            if (value instanceof TerceroEntity) {
                tercero = (TerceroEntity) value;
                panel.removeAll();
                boolean activo = "Activo".equalsIgnoreCase(tercero.getEstado());
                if (permissionService == null || permissionService.tienePermiso((long) Permiso.VER_TERCERO.getId())) {
                    JButton btnVer = crearBotonAccion(FontAwesomeSolid.EYE, "Ver tercero", new Color(60, 130, 200), e -> {
                        if (tercerosListeners != null)
                            tercerosListeners.verTercero(tercero);
                        fireEditingStopped();
                    });
                    panel.add(btnVer);
                }
                if (permissionService == null || permissionService.tienePermiso((long) Permiso.EDITAR_TERCERO.getId())) {
                    JButton btnToggle = crearBotonAccion(
                            activo ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                            activo ? "Inactivar" : "Activar",
                            activo ? new Color(0, 180, 0) : Color.RED,
                            e -> {
                                tercero.setEstado(activo ? "Inactivo" : "Activo");
                                modeloTabla.setValueAt(tercero.getEstado(), row, 3);
                                modeloTabla.setValueAt(tercero, row, 4);
                                if (tercerosListeners != null)
                                    tercerosListeners.actualizarTercero(tercero);
                                fireEditingStopped();
                            });
                    panel.add(btnToggle);
                }
                if (permissionService == null || permissionService.tienePermiso((long) Permiso.ELIMINAR_TERCERO.getId())) {
                    JButton btnEliminar = crearBotonAccion(FontAwesomeSolid.TRASH_ALT, "Eliminar tercero", Color.RED, e -> {
                        if (tercerosListeners != null)
                            tercerosListeners.eliminarTercero(tercero);
                        fireEditingStopped();
                    });
                    panel.add(btnEliminar);
                }
            }
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return tercero;
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return !(e instanceof MouseEvent) || ((MouseEvent) e).getClickCount() == 1;
        }
    }

    private JPanel crearPanelAcciones(TerceroEntity tercero, boolean conListeners) {
        boolean activo = "Activo".equalsIgnoreCase(tercero.getEstado());
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.setOpaque(false);
        if (permissionService == null || permissionService.tienePermiso((long) Permiso.VER_TERCERO.getId())) {
            JButton btnVer = crearIconoBoton(FontAwesomeSolid.EYE, "Ver tercero", new Color(60, 130, 200));
            panel.add(btnVer);
        }
        if (permissionService == null || permissionService.tienePermiso((long) Permiso.EDITAR_TERCERO.getId())) {
            JButton btnToggle = crearIconoBoton(
                    activo ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                    activo ? "Inactivar" : "Activar",
                    activo ? new Color(0, 180, 0) : Color.RED);
            panel.add(btnToggle);
        }
        if (permissionService == null || permissionService.tienePermiso((long) Permiso.ELIMINAR_TERCERO.getId())) {
            JButton btnEliminar = crearIconoBoton(FontAwesomeSolid.TRASH_ALT, "Eliminar tercero", Color.RED);
            panel.add(btnEliminar);
        }
        return panel;
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
