package com.hersac.ui.views.usuarios.gestion.tablas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.EventObject;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import com.hersac.core.globals.servicios.PermissionService;
import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import com.hersac.ui.globals.enums.Permiso;
import com.hersac.ui.views.usuarios.gestion.listeners.UsuariosListeners;

public class UsuariosTable extends JPanel {
    private final JTable tabla;
    private final DefaultTableModel modelo;
    private UsuariosListeners oyenteUsuarios;
    private final PermissionService servicioPermisos = new PermissionService(null);

    public UsuariosTable() {
        setLayout(new BorderLayout());
        String[] columnas = { "ID", "Nombre", "Correo", "Departamento", "Rol", "Estado", "Acciones" };
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return columna == 6;
            }
        };
        tabla = new JTable(modelo);
        tabla.setRowHeight(40);
        tabla.setShowGrid(false);
        tabla.getColumnModel().getColumn(6).setCellRenderer(new RenderAcciones());
        tabla.getColumnModel().getColumn(6).setCellEditor(new EditorAcciones());
        tabla.setFont(new Font("Roboto", Font.PLAIN, 14));
        tabla.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 15));
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int columna = tabla.columnAtPoint(e.getPoint());
                int fila = tabla.rowAtPoint(e.getPoint());
                if (columna == 6 && fila >= 0 && tabla.isCellEditable(fila, columna)) {
                    tabla.editCellAt(fila, columna);
                    tabla.getEditorComponent().requestFocusInWindow();
                }
            }
        });
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setPreferredSize(new Dimension(900, 400));
        scroll.setMaximumSize(new Dimension(900, Integer.MAX_VALUE));
        add(scroll, BorderLayout.CENTER);
    }

    public void establecerUsuarios(List<UsuarioEntity> usuarios) {
        usuarios.sort(Comparator.comparingLong(UsuarioEntity::getUsuarioId));
        modelo.setRowCount(0);
        for (UsuarioEntity usuario : usuarios) {
            String departamento = usuario.getDepartamento() != null && usuario.getDepartamento().getNombre() != null ? usuario.getDepartamento().getNombre() : "Sin departamento";
            String rol = usuario.getRol() != null && usuario.getRol().getNombre() != null ? usuario.getRol().getNombre() : "Sin rol";
            modelo.addRow(new Object[] {
                usuario.getUsuarioId(),
                usuario.getNombre(),
                usuario.getCorreo(),
                departamento,
                rol,
                usuario.getEstaActivo() ? "Activo" : "Inactivo",
                usuario
            });
        }
    }

    public void establecerOyente(UsuariosListeners oyente) {
        this.oyenteUsuarios = oyente;
    }

    private class RenderAcciones implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionado, boolean tieneFoco, int fila, int columna) {
            if (valor instanceof UsuarioEntity usuario) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                panel.setOpaque(false);
                boolean puedeVer = servicioPermisos.tienePermiso((long) Permiso.VER_GESTION_USUARIO.getId());
                boolean puedeEditar = servicioPermisos.tienePermiso((long) Permiso.EDITAR_GESTION_USUARIO.getId());
                boolean puedeEliminar = servicioPermisos.tienePermiso((long) Permiso.ELIMINAR_GESTION_USUARIO.getId());
                if (puedeVer) {
                    JButton btnVer = crearBotonIcono(FontAwesomeSolid.EYE, "Ver usuario", new Color(60, 130, 200));
                    panel.add(btnVer);
                }
                if (puedeEditar) {
                    boolean activo = Boolean.TRUE.equals(usuario.getEstaActivo());
                    JButton btnToggle = crearBotonIcono(
                        activo ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                        activo ? "Inactivar" : "Activar",
                        activo ? new Color(0, 180, 0) : Color.RED);
                    panel.add(btnToggle);
                }
                if (puedeEliminar) {
                    JButton btnEliminar = crearBotonIcono(FontAwesomeSolid.TRASH_ALT, "Eliminar usuario", Color.RED);
                    panel.add(btnEliminar);
                }
                panel.setBackground(seleccionado ? tabla.getSelectionBackground() : tabla.getBackground());
                return panel;
            }
            JLabel vacio = new JLabel("");
            vacio.setFont(new Font("Roboto", Font.PLAIN, 14));
            return vacio;
        }
    }

    private class EditorAcciones extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel;
        private UsuarioEntity usuario;

        public EditorAcciones() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setOpaque(false);
        }

        @Override
        public Component getTableCellEditorComponent(JTable tabla, Object valor, boolean seleccionado, int fila, int columna) {
            if (valor instanceof UsuarioEntity) {
                usuario = (UsuarioEntity) valor;
                panel.removeAll();
                boolean activo = Boolean.TRUE.equals(usuario.getEstaActivo());
                boolean puedeVer = servicioPermisos.tienePermiso((long) Permiso.VER_GESTION_USUARIO.getId());
                boolean puedeEditar = servicioPermisos.tienePermiso((long) Permiso.EDITAR_GESTION_USUARIO.getId());
                boolean puedeEliminar = servicioPermisos.tienePermiso((long) Permiso.ELIMINAR_GESTION_USUARIO.getId());
                if (puedeVer) {
                    JButton btnVer = crearBotonAccion(FontAwesomeSolid.EYE, "Ver usuario", new Color(60, 130, 200), e -> {
                        if (oyenteUsuarios != null) oyenteUsuarios.verUsuario(usuario);
                        fireEditingStopped();
                    });
                    panel.add(btnVer);
                }
                if (puedeEditar) {
                    JButton btnToggle = crearBotonAccion(
                        activo ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                        activo ? "Inactivar" : "Activar",
                        activo ? new Color(0, 180, 0) : Color.RED,
                        e -> {
                            usuario.setEstaActivo(!usuario.getEstaActivo());
                            modelo.setValueAt(usuario.getEstaActivo() ? "Activo" : "Inactivo", fila, 5);
                            modelo.setValueAt(usuario, fila, 6);
                            if (oyenteUsuarios != null) oyenteUsuarios.actualizarUsuario(usuario);
                            fireEditingStopped();
                        });
                    panel.add(btnToggle);
                }
                if (puedeEliminar) {
                    JButton btnEliminar = crearBotonAccion(FontAwesomeSolid.TRASH_ALT, "Eliminar usuario", Color.RED, e -> {
                        if (oyenteUsuarios != null) oyenteUsuarios.eliminarUsuario(usuario);
                        fireEditingStopped();
                    });
                    panel.add(btnEliminar);
                }
            }
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return usuario;
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

    public JTable obtenerTabla() {
        return tabla;
    }

    public DefaultTableModel obtenerModelo() {
        return modelo;
    }
}
