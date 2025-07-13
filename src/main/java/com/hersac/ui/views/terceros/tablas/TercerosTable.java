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
    private TercerosListeners escuchadorTerceros;

    public TercerosTable(PermissionService servicioPermisos) {
        setLayout(new BorderLayout());
        String[] columnas = {"ID", "Nombre", "Tipo", "Estado", "Acciones"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return columna == 4;
            }
        };
        tablaTerceros = new JTable(modeloTabla);
        tablaTerceros.setRowHeight(40);
        tablaTerceros.setShowGrid(false);
        tablaTerceros.getColumnModel().getColumn(4).setCellRenderer(new RenderizadorAcciones());
        tablaTerceros.getColumnModel().getColumn(4).setCellEditor(new EditorAcciones());
        tablaTerceros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evento) {
                int columna = tablaTerceros.columnAtPoint(evento.getPoint());
                int fila = tablaTerceros.rowAtPoint(evento.getPoint());
                if (columna == 4 && fila >= 0 && tablaTerceros.isCellEditable(fila, columna)) {
                    tablaTerceros.editCellAt(fila, columna);
                    tablaTerceros.getEditorComponent().requestFocusInWindow();
                }
            }
        });
        JScrollPane panelDesplazamiento = new JScrollPane(tablaTerceros);
        panelDesplazamiento.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panelDesplazamiento.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panelDesplazamiento.setPreferredSize(new Dimension(900, 400));
        panelDesplazamiento.setMaximumSize(new Dimension(900, Integer.MAX_VALUE));
        add(panelDesplazamiento, BorderLayout.CENTER);
        aplicarFuenteRoboto(tablaTerceros);
    }

    public void setTerceros(List<TerceroEntity> listaTerceros) {
        List<TerceroEntity> listaMutable = new ArrayList<>(listaTerceros);
        listaMutable.sort(Comparator.comparing(TerceroEntity::getTerceroId));
        modeloTabla.setRowCount(0);
        for (TerceroEntity tercero : listaMutable) {
            String nombreTipo = "";
            if (tercero.getTipoPersona().getTipoPersonaId() != null) {
                TiposTerceroEnum tipoEnum = TiposTerceroEnum.fromId(tercero.getTipoPersona().getTipoPersonaId());
                if (tipoEnum != null) nombreTipo = tipoEnum.getNombre();
                if (tipoEnum == null) nombreTipo = String.valueOf(tercero.getTipoPersona().getTipoPersonaId());
            }
            modeloTabla.addRow(new Object[]{
                    tercero.getTerceroId(),
                    tercero.getNombre(),
                    nombreTipo,
                    tercero.getEstado(),
                    tercero
            });
        }
    }

    public void setEscuchadorTerceros(TercerosListeners escuchador) {
        this.escuchadorTerceros = escuchador;
    }

    public JTable getTablaTerceros() {
        return tablaTerceros;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    private class RenderizadorAcciones implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor,
                boolean seleccionado, boolean tieneFoco,
                int fila, int columna) {
            if (valor instanceof TerceroEntity tercero) {
                JPanel panel = crearPanelAcciones(tercero);
                panel.setBackground(seleccionado ? tabla.getSelectionBackground() : tabla.getBackground());
                aplicarFuenteRoboto(panel);
                return panel;
            }
            JLabel vacio = new JLabel("");
            aplicarFuenteRoboto(vacio);
            return vacio;
        }
    }

    private class EditorAcciones extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel;
        private TerceroEntity tercero;

        public EditorAcciones() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setOpaque(false);
        }

        @Override
        public Component getTableCellEditorComponent(JTable tabla, Object valor,
                boolean seleccionado, int fila, int columna) {
            if (valor instanceof TerceroEntity) {
                tercero = (TerceroEntity) valor;
                panel.removeAll();
                boolean activo = "Activo".equalsIgnoreCase(tercero.getEstado());
                JButton botonVer = crearBotonAccion(FontAwesomeSolid.EYE, "Ver tercero", new Color(60, 130, 200), e -> {
                    if (escuchadorTerceros != null) escuchadorTerceros.verTercero(tercero);
                    fireEditingStopped();
                });
                panel.add(botonVer);
                JButton botonToggle = crearBotonAccion(
                        activo ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                        activo ? "Inactivar" : "Activar",
                        activo ? new Color(0, 180, 0) : Color.RED,
                        e -> {
                            tercero.setEstado(activo ? "Inactivo" : "Activo");
                            modeloTabla.setValueAt(tercero.getEstado(), fila, 3);
                            modeloTabla.setValueAt(tercero, fila, 4);
                            if (escuchadorTerceros != null) escuchadorTerceros.actualizarTercero(tercero);
                            fireEditingStopped();
                        });
                panel.add(botonToggle);
                JButton botonEliminar = crearBotonAccion(FontAwesomeSolid.TRASH_ALT, "Eliminar tercero", Color.RED, e -> {
                    if (escuchadorTerceros != null) escuchadorTerceros.eliminarTercero(tercero);
                    fireEditingStopped();
                });
                panel.add(botonEliminar);
                aplicarFuenteRoboto(panel);
            }
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return tercero;
        }

        @Override
        public boolean isCellEditable(EventObject evento) {
            return !(evento instanceof MouseEvent) || ((MouseEvent) evento).getClickCount() == 1;
        }
    }

    private JPanel crearPanelAcciones(TerceroEntity tercero) {
        boolean activo = "Activo".equalsIgnoreCase(tercero.getEstado());
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.setOpaque(false);
        JButton botonVer = crearIconoBoton(FontAwesomeSolid.EYE, "Ver tercero", new Color(60, 130, 200));
        panel.add(botonVer);
        JButton botonToggle = crearIconoBoton(
                activo ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                activo ? "Inactivar" : "Activar",
                activo ? new Color(0, 180, 0) : Color.RED);
        panel.add(botonToggle);
        JButton botonEliminar = crearIconoBoton(FontAwesomeSolid.TRASH_ALT, "Eliminar tercero", Color.RED);
        panel.add(botonEliminar);
        aplicarFuenteRoboto(panel);
        return panel;
    }

    private JButton crearIconoBoton(FontAwesomeSolid icono, String tooltip, Color color) {
        FontIcon icon = FontIcon.of(icono, 18, color);
        JButton boton = new JButton(icon);
        boton.setToolTipText(tooltip);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(36, 36));
        aplicarFuenteRoboto(boton);
        return boton;
    }

    private JButton crearBotonAccion(FontAwesomeSolid icono, String tooltip, Color color, ActionListener accion) {
        JButton boton = crearIconoBoton(icono, tooltip, color);
        boton.addActionListener(accion);
        return boton;
    }

    private void aplicarFuenteRoboto(Component componente) {
        Font fuenteRoboto = new Font("Roboto", Font.PLAIN, 14);
        if (componente instanceof JLabel label) {
            label.setFont(fuenteRoboto);
        }
        if (componente instanceof JButton boton) {
            boton.setFont(fuenteRoboto);
        }
        if (componente instanceof JPanel panel) {
            for (Component hijo : panel.getComponents()) {
                aplicarFuenteRoboto(hijo);
            }
        }
        if (componente instanceof JTable tabla) {
            tabla.setFont(fuenteRoboto);
        }
    }
}
