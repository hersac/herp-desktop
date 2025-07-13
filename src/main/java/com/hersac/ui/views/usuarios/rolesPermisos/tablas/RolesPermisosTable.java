package com.hersac.ui.views.usuarios.rolesPermisos.tablas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import com.hersac.core.modules.roles.entities.RolEntity;
import com.hersac.ui.controllers.rolesPermisos.RolesPermisosController;
import com.hersac.ui.views.usuarios.rolesPermisos.listeners.RolesPermisosListener;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

public class RolesPermisosTable extends JPanel {
    private JTable tabla;
    private DefaultTableModel modelo;
    private RolesPermisosListener escuchador;
    private RolesPermisosController controlador;
    private boolean puedeVer = false;
    private boolean puedeEditar = false;
    private boolean puedeEliminar = false;
    private final Font fuenteRoboto = new Font("Roboto", Font.PLAIN, 14);

    public RolesPermisosTable() {
        setLayout(new BorderLayout());
        modelo = new DefaultTableModel(new Object[]{"ID", "Nombre", "Descripción", "Activo", "Acciones"}, 0) {
            public boolean isCellEditable(int fila, int columna) { return columna == 4; }
        };
        tabla = new JTable(modelo);
        tabla.setRowHeight(40);
        tabla.getColumnModel().getColumn(4).setCellRenderer(new RenderizadorAcciones());
        tabla.getColumnModel().getColumn(4).setCellEditor(new EditorAcciones());
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int columna = tabla.columnAtPoint(e.getPoint());
                int fila = tabla.rowAtPoint(e.getPoint());
                if (columna == 4 && fila >= 0 && tabla.isCellEditable(fila, columna)) {
                    tabla.editCellAt(fila, columna);
                    tabla.getEditorComponent().requestFocusInWindow();
                }
            }
        });
        add(tabla, BorderLayout.CENTER);
    }

    public void establecerRoles(List<RolEntity> lista) {
        if (tabla.isEditing()) {
            tabla.getCellEditor().stopCellEditing();
        }
        modelo.setRowCount(0);
        for (RolEntity rol : lista) {
            modelo.addRow(new Object[]{
                rol.getRolId(),
                rol.getNombre(),
                rol.getDescripcion(),
                rol.getEstaActivo() != null && rol.getEstaActivo() ? "Sí" : "No",
                rol
            });
        }
    }

    public void establecerEscuchador(RolesPermisosListener escuchador) {
        this.escuchador = escuchador;
    }

    public void establecerControlador(RolesPermisosController controlador) {
        this.controlador = controlador;
    }

    public void establecerPermisos(boolean ver, boolean editar, boolean eliminar) {
        this.puedeVer = ver;
        this.puedeEditar = editar;
        this.puedeEliminar = eliminar;
        repaint();
    }

    private class RenderizadorAcciones implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionado, boolean tieneFoco, int fila, int columna) {
            if (valor instanceof RolEntity rol) {
                JPanel panel = crearPanelAcciones(rol);
                panel.setBackground(seleccionado ? tabla.getSelectionBackground() : tabla.getBackground());
                return panel;
            }
            JLabel vacio = new JLabel("");
            vacio.setFont(fuenteRoboto);
            return vacio;
        }
    }

    private class EditorAcciones extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel;
        private RolEntity rol;

        public EditorAcciones() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setOpaque(false);
        }

        @Override
        public Component getTableCellEditorComponent(JTable tabla, Object valor, boolean seleccionado, int fila, int columna) {
            if (valor instanceof RolEntity) {
                rol = (RolEntity) valor;
                panel.removeAll();
                boolean activo = Boolean.TRUE.equals(rol.getEstaActivo());
                if (puedeVer) {
                    JButton btnVer = crearBotonAccion(FontAwesomeSolid.EYE, "Ver rol", new Color(60, 130, 200), e -> {
                        if (escuchador != null) escuchador.mostrarModalEditarRol(rol);
                        fireEditingStopped();
                    });
                    btnVer.setFont(fuenteRoboto);
                    panel.add(btnVer);
                }
                if (puedeEditar) {
                    JButton btnToggle = crearBotonAccion(
                            activo ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                            activo ? "Inactivar" : "Activar",
                            activo ? new Color(0, 180, 0) : Color.RED,
                            e -> {
                                rol.setEstaActivo(!rol.getEstaActivo());
                                if (fila >= 0 && fila < modelo.getRowCount()) {
                                    modelo.setValueAt(rol.getEstaActivo() ? "Sí" : "No", fila, 3);
                                    modelo.setValueAt(rol, fila, 4);
                                }
                                if (escuchador != null && controlador != null) {
                                    List<Long> permisos = obtenerPermisosRol(rol);
                                    escuchador.editarRol(rol, permisos);
                                }
                                fireEditingStopped();
                            });
                    btnToggle.setFont(fuenteRoboto);
                    panel.add(btnToggle);
                }
                if (puedeEliminar) {
                    JButton btnEliminar = crearBotonAccion(FontAwesomeSolid.TRASH_ALT, "Eliminar rol", Color.RED, e -> {
                        fireEditingStopped();
                        if (escuchador != null) escuchador.eliminarRol(rol);
                    });
                    btnEliminar.setFont(fuenteRoboto);
                    panel.add(btnEliminar);
                }
            }
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return rol;
        }
    }

    private List<Long> obtenerPermisosRol(RolEntity rol) {
        List<Long> permisos = new ArrayList<>();
        List<com.hersac.core.modules.rolespermisos.entities.RolPermisoEntity> rolPermisos = controlador.buscarPorRolId(rol.getRolId());
        for (com.hersac.core.modules.rolespermisos.entities.RolPermisoEntity rp : rolPermisos) {
            if (rp.getPermiso() != null && rp.getPermiso().getPermisoId() != null) {
                permisos.add(rp.getPermiso().getPermisoId());
            }
        }
        return permisos;
    }

    private JPanel crearPanelAcciones(RolEntity rol) {
        boolean activo = Boolean.TRUE.equals(rol.getEstaActivo());
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.setOpaque(false);
        if (puedeVer) {
            JButton btnVer = crearIconoBoton(FontAwesomeSolid.EYE, "Ver rol", new Color(60, 130, 200));
            btnVer.setFont(fuenteRoboto);
            panel.add(btnVer);
        }
        if (puedeEditar) {
            JButton btnToggle = crearIconoBoton(
                    activo ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                    activo ? "Inactivar" : "Activar",
                    activo ? new Color(0, 180, 0) : Color.RED);
            btnToggle.setFont(fuenteRoboto);
            panel.add(btnToggle);
        }
        if (puedeEliminar) {
            JButton btnEliminar = crearIconoBoton(FontAwesomeSolid.TRASH_ALT, "Eliminar rol", Color.RED);
            btnEliminar.setFont(fuenteRoboto);
            panel.add(btnEliminar);
        }
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
        boton.setFont(fuenteRoboto);
        return boton;
    }

    private JButton crearBotonAccion(FontAwesomeSolid icono, String tooltip, Color color, ActionListener accion) {
        JButton boton = crearIconoBoton(icono, tooltip, color);
        boton.addActionListener(accion);
        boton.setFont(fuenteRoboto);
        return boton;
    }
}
