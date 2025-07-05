package com.hersac.ui.views.usuarios.gestion.tablas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import com.hersac.ui.views.usuarios.gestion.listeners.UsuariosListeners;

public class UsuariosTable extends JPanel {

    private final JTable tablaUsuarios;
    private final DefaultTableModel modeloTabla;
    private UsuariosListeners usuariosListeners;

    public UsuariosTable() {
        setLayout(new BorderLayout());

        String[] columnas = { "ID", "Nombre", "Correo", "Departamento", "Rol", "Estado", "Acciones" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };

        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setRowHeight(40);
        tablaUsuarios.setShowGrid(false);
        tablaUsuarios.getColumnModel().getColumn(6).setCellRenderer(new AccionesRenderer());
        tablaUsuarios.getColumnModel().getColumn(6).setCellEditor(new AccionesEditor());
        tablaUsuarios.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = tablaUsuarios.columnAtPoint(e.getPoint());
                int row = tablaUsuarios.rowAtPoint(e.getPoint());

                if (column == 6 && row >= 0 && tablaUsuarios.isCellEditable(row, column)) {
                    tablaUsuarios.editCellAt(row, column);
                    tablaUsuarios.getEditorComponent().requestFocusInWindow();
                }
            }
        });

        add(new JScrollPane(tablaUsuarios), BorderLayout.CENTER);
    }

    public void setUsuarios(List<UsuarioEntity> usuarios) {
        usuarios.sort(Comparator.comparingLong(UsuarioEntity::getUsuarioId));

        modeloTabla.setRowCount(0);
        for (UsuarioEntity usuario : usuarios) {
            String departamento = (usuario.getDepartamento() != null && usuario.getDepartamento().getNombre() != null)
                ? usuario.getDepartamento().getNombre() : "Sin departamento";
            String rol = (usuario.getRolPermiso() != null && usuario.getRolPermiso().getRol() != null && usuario.getRolPermiso().getRol().getNombre() != null)
                ? usuario.getRolPermiso().getRol().getNombre() : "Sin rol";
            modeloTabla.addRow(new Object[] {
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

    public void setActionListener(UsuariosListeners listener) {
        this.usuariosListeners = listener;
    }

    private class AccionesRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            if (value instanceof UsuarioEntity usuario) {
                JPanel panel = crearPanelAcciones(usuario);
                panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                return panel;
            }
            return new JLabel("");
        }
    }

    private class AccionesEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel;
        private UsuarioEntity usuario;

        public AccionesEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setOpaque(false);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            if (value instanceof UsuarioEntity) {
                usuario = (UsuarioEntity) value;
                panel.removeAll();

                boolean activo = Boolean.TRUE.equals(usuario.getEstaActivo());

                JButton btnVer = crearBotonAccion(FontAwesomeSolid.EYE, "Ver usuario", new Color(60, 130, 200), e -> {
                    if (usuariosListeners != null)
                        usuariosListeners.verUsuario(usuario);
                    fireEditingStopped();
                });

                JButton btnToggle = crearBotonAccion(
                        activo ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                        activo ? "Inactivar" : "Activar",
                        activo ? new Color(0, 180, 0) : Color.RED,
                        e -> {
                            usuario.setEstaActivo(!usuario.getEstaActivo());
                            modeloTabla.setValueAt(usuario.getEstaActivo() ? "Activo" : "Inactivo", row, 3);
                            modeloTabla.setValueAt(usuario, row, 4);
                            if (usuariosListeners != null)
                                usuariosListeners.actualizarUsuario(usuario);
                            fireEditingStopped();
                        });

                JButton btnEliminar = crearBotonAccion(FontAwesomeSolid.TRASH_ALT, "Eliminar usuario", Color.RED, e -> {
                    if (usuariosListeners != null)
                        usuariosListeners.eliminarUsuario(usuario);
                    fireEditingStopped();
                });

                panel.add(btnVer);
                panel.add(btnToggle);
                panel.add(btnEliminar);
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

    private JPanel crearPanelAcciones(UsuarioEntity usuario) {
        boolean activo = Boolean.TRUE.equals(usuario.getEstaActivo());

        JButton btnVer = crearIconoBoton(FontAwesomeSolid.EYE, "Ver usuario", new Color(60, 130, 200));
        JButton btnToggle = crearIconoBoton(
                activo ? FontAwesomeSolid.TOGGLE_ON : FontAwesomeSolid.TOGGLE_OFF,
                activo ? "Inactivar" : "Activar",
                activo ? new Color(0, 180, 0) : Color.RED);
        JButton btnEliminar = crearIconoBoton(FontAwesomeSolid.TRASH_ALT, "Eliminar usuario", Color.RED);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.setOpaque(false);
        panel.add(btnVer);
        panel.add(btnToggle);
        panel.add(btnEliminar);
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

    public JTable getTablaUsuarios() {
        return tablaUsuarios;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }
}
