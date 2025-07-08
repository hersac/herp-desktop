package com.hersac.ui.views.usuarios.gestion;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.hersac.core.modules.roles.entities.RolEntity;
import com.hersac.ui.controllers.roles.RolesController;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import com.hersac.core.di.DIContainer;
import com.hersac.core.modules.departamentos.entities.DepartamentoEntity;
import com.hersac.core.modules.rolespermisos.entities.RolPermisoEntity;
import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import com.hersac.ui.controllers.departamentos.DepartamentosController;
import com.hersac.ui.controllers.rolesPermisos.RolesPermisosController;
import com.hersac.ui.controllers.usuarios.UsuariosController;
import com.hersac.ui.globals.enums.ColorsTheme;
import com.hersac.ui.views.usuarios.gestion.forms.FiltrosForm;
import com.hersac.ui.views.usuarios.gestion.listeners.UsuariosListeners;
import com.hersac.ui.views.usuarios.gestion.modales.RegistrarUsuario;
import com.hersac.ui.views.usuarios.gestion.tablas.UsuariosTable;

public class GestionUsuarios extends JPanel implements UsuariosListeners {

    private JFrame frame = new JFrame("Registrar Usuario");
    private final UsuariosController usuariosController;
    private final DepartamentosController departamentosController;
    private final RolesController rolesController;
    private final UsuariosTable tablaUsuariosTable = new UsuariosTable();

    private List<UsuarioEntity> listaCompletaUsuarios;
    private FiltrosForm filtrosForm;
    private JTextField searchField;

    public GestionUsuarios(DIContainer diContainer) {
        this.usuariosController = diContainer.getUsuariosController();
        this.departamentosController = diContainer.getDepartamentosController();
        this.rolesController = diContainer.getRolesController();

        this.tablaUsuariosTable.setActionListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new Dimension(800, 600));

        JLabel titleLabel = new JLabel("Gestión de Usuarios");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        filtrosForm = new FiltrosForm();

        JButton registrarBtn = new JButton("Registrar Usuario");
        FontIcon iconVer = FontIcon.of(FontAwesomeSolid.PLUS, 18, ColorsTheme.TEXT_PRIMARY.get());
        registrarBtn.setFont(new Font("Roboto", Font.PLAIN, 14));
        registrarBtn.setBackground(ColorsTheme.PRIMARY.get());
        registrarBtn.setForeground(ColorsTheme.TEXT_PRIMARY.get());
        registrarBtn.setIcon(iconVer);

        searchField = new JTextField(25);
        searchField.setMaximumSize(new Dimension(400, 30));
        searchField.setAlignmentX(CENTER_ALIGNMENT);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarUsuarios();
            }
        });

        filtrosForm.setOnFiltrosCambiados(this::filtrarUsuarios);

        JPanel panelBtn = new JPanel();
        panelBtn.setLayout(new BoxLayout(panelBtn, BoxLayout.X_AXIS));
        panelBtn.setOpaque(false);
        panelBtn.setPreferredSize(new Dimension(900, 40));
        panelBtn.setMaximumSize(new Dimension(900, 40));
        panelBtn.add(searchField);
        panelBtn.add(Box.createHorizontalGlue());
        panelBtn.add(registrarBtn);

        JPanel panelBtnExpansible = new JPanel();
        panelBtnExpansible.setLayout(new BoxLayout(panelBtnExpansible, BoxLayout.X_AXIS));
        panelBtnExpansible.setOpaque(false);
        panelBtnExpansible.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
        panelBtnExpansible.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panelBtnExpansible.add(Box.createHorizontalGlue());
        panelBtnExpansible.add(panelBtn);
        panelBtnExpansible.add(Box.createHorizontalGlue());

        JScrollPane scrollPane = new JScrollPane(tablaUsuariosTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setAlignmentX(CENTER_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(900, 400));
        scrollPane.setMaximumSize(new Dimension(900, Integer.MAX_VALUE));

        add(Box.createRigidArea(new Dimension(0, 10)));
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(filtrosForm);
        add(Box.createRigidArea(new Dimension(0, 100)));
        add(panelBtnExpansible);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(scrollPane);
        add(Box.createVerticalGlue());

        List<UsuarioEntity> listaUsuarios = obtenerUsuarios();
        this.listaCompletaUsuarios = listaUsuarios;
        tablaUsuariosTable.setUsuarios(listaUsuarios);

        // Acción botón registrar
        List<DepartamentoEntity> departamentos = obtenerDepartamentos();
        filtrosForm.setDepartamentos(departamentos);
        List<RolEntity> roles = obtenerRoles();
        ejecutarAccion(registrarBtn, () -> {
            new RegistrarUsuario(frame, departamentos, roles, this, null);
        });
    }

    private void ejecutarAccion(JButton panel, Runnable accion) {
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                accion.run();
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                panel.setBackground(ColorsTheme.PRIMARY_LIGTH.get());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                panel.setBackground(ColorsTheme.PRIMARY.get());
            }
        });
    }

    @Override
    public void crearUsuario(UsuarioEntity usuario) {
        usuariosController.crear(usuario);
        actualizarTabla();
        JOptionPane.showMessageDialog(this,
                "Usuario creado exitosamente:\n\nNombre: " + usuario.getNombre() + "\nCorreo: " + usuario.getCorreo(),
                "Usuario Creado", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void verUsuario(UsuarioEntity usuario) {
        List<DepartamentoEntity> departamentos = obtenerDepartamentos();
        List<RolEntity> roles = obtenerRoles();
        new RegistrarUsuario(frame, departamentos, roles, this, usuario);
    }

    @Override
    public void actualizarUsuario(UsuarioEntity usuario) {
        usuariosController.actualizar(usuario.getUsuarioId(), usuario);
        actualizarTabla();
        JOptionPane.showMessageDialog(this,
                "Usuario actualizado exitosamente:\n\nNombre: " + usuario.getNombre() + "\nCorreo: "
                        + usuario.getCorreo(),
                "Usuario Actualizado", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void eliminarUsuario(UsuarioEntity usuario) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar al usuario " + usuario.getNombre() + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            usuariosController.eliminar(usuario.getUsuarioId());
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.",
                    "Eliminación exitosa", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void actualizarTabla() {
        this.listaCompletaUsuarios = obtenerUsuarios();
        filtrarUsuarios();
    }

    private void filtrarUsuarios() {
        final String texto = searchField.getText() != null ? searchField.getText().toLowerCase().trim() : "";
        final String estado = filtrosForm.getEstadoSeleccionado();
        final String departamento = filtrosForm.getDepartamentoSeleccionado();
        final String desde = filtrosForm.getFechaDesde();
        final String hasta = filtrosForm.getFechaHasta();

        List<UsuarioEntity> filtrados = listaCompletaUsuarios.stream()
                .filter(u -> {
                    String usuarioIdStr = String.valueOf(u.getUsuarioId());
                    String nombre = u.getNombre() != null ? u.getNombre().toLowerCase() : "";
                    String correo = u.getCorreo() != null ? u.getCorreo().toLowerCase() : "";
                    String estadoUsuario = u.getEstaActivo() != null ? (u.getEstaActivo() ? "activo" : "inactivo") : "";
                    String rol = (u.getRol() != null && u.getRol().getNombre() != null)
                        ? u.getRol().getNombre().toLowerCase() : "sin rol";
                    String depNombre = (u.getDepartamento() != null && u.getDepartamento().getNombre() != null)
                        ? u.getDepartamento().getNombre().toLowerCase() : "sin departamento";
                    boolean coincideTexto = texto.isEmpty()
                        || usuarioIdStr.contains(texto)
                        || nombre.contains(texto)
                        || correo.contains(texto)
                        || estadoUsuario.contains(texto)
                        || rol.contains(texto)
                        || depNombre.contains(texto);

                    boolean coincideEstado = estado.equals("Todos")
                            || (estado.equals("Activos") && Boolean.TRUE.equals(u.getEstaActivo()))
                            || (estado.equals("Inactivos") && Boolean.FALSE.equals(u.getEstaActivo()));

                    boolean coincideDep = departamento.equals("Todos") || departamento.equalsIgnoreCase(depNombre);

                    boolean coincideFecha = true;
                    if (desde != null && hasta != null) {
                        if (u.getFechaCreacion() != null) {
                            String fecha = u.getFechaCreacion().toLocalDate().toString();
                            coincideFecha = (fecha.compareTo(desde) >= 0 && fecha.compareTo(hasta) <= 0);
                        } else {
                            coincideFecha = false;
                        }
                    }

                    boolean coincideRol = true;
                    return coincideTexto && coincideEstado && coincideDep && coincideFecha && coincideRol;
                })
                .collect(Collectors.toList());

        tablaUsuariosTable.setUsuarios(filtrados);
    }

    private List<UsuarioEntity> obtenerUsuarios() {
        return usuariosController.buscarTodos();
    }

    private List<DepartamentoEntity> obtenerDepartamentos() {
        return departamentosController.buscarTodos();
    }

    private List<RolEntity> obtenerRoles() {
        return rolesController.buscarTodos();
    }
}
