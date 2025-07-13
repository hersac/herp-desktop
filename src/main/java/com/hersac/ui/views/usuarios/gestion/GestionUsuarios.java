package com.hersac.ui.views.usuarios.gestion;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

import com.hersac.core.globals.servicios.PermissionService;
import com.hersac.core.modules.roles.entities.RolEntity;
import com.hersac.ui.controllers.roles.RolesController;
import com.hersac.ui.globals.enums.Permiso;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import com.hersac.core.di.DIContainer;
import com.hersac.core.modules.departamentos.entities.DepartamentoEntity;
import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import com.hersac.ui.controllers.departamentos.DepartamentosController;
import com.hersac.ui.controllers.usuarios.UsuariosController;
import com.hersac.ui.globals.enums.ColorsTheme;
import com.hersac.ui.views.usuarios.gestion.forms.FiltrosForm;
import com.hersac.ui.views.usuarios.gestion.listeners.UsuariosListeners;
import com.hersac.ui.views.usuarios.gestion.modales.RegistrarUsuario;
import com.hersac.ui.views.usuarios.gestion.tablas.UsuariosTable;

public class GestionUsuarios extends JPanel implements UsuariosListeners {
    private JFrame ventana = new JFrame("Registrar Usuario");
    private final UsuariosController controladorUsuarios;
    private final DepartamentosController controladorDepartamentos;
    private final RolesController controladorRoles;
    private final UsuariosTable tablaUsuarios = new UsuariosTable();
    private List<UsuarioEntity> listaUsuariosCompleta;
    private FiltrosForm formularioFiltros;
    private JTextField campoBusqueda;

    public GestionUsuarios(DIContainer contenedorDI) {
        this.controladorUsuarios = contenedorDI.getUsuariosController();
        this.controladorDepartamentos = contenedorDI.getDepartamentosController();
        this.controladorRoles = contenedorDI.getRolesController();
        this.tablaUsuarios.establecerOyente(this);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new Dimension(800, 600));
        JLabel etiquetaTitulo = new JLabel("Gestión de Usuarios");
        etiquetaTitulo.setFont(new Font("Roboto", Font.BOLD, 24));
        etiquetaTitulo.setAlignmentX(CENTER_ALIGNMENT);
        formularioFiltros = new FiltrosForm();
        JButton botonRegistrar = new JButton("Registrar Usuario");
        FontIcon iconoAgregar = FontIcon.of(FontAwesomeSolid.PLUS, 18, ColorsTheme.TEXT_PRIMARY.get());
        botonRegistrar.setFont(new Font("Roboto", Font.PLAIN, 14));
        botonRegistrar.setBackground(ColorsTheme.PRIMARY.get());
        botonRegistrar.setForeground(ColorsTheme.TEXT_PRIMARY.get());
        botonRegistrar.setIcon(iconoAgregar);
        campoBusqueda = new JTextField(25);
        campoBusqueda.setMaximumSize(new Dimension(400, 30));
        campoBusqueda.setAlignmentX(CENTER_ALIGNMENT);
        campoBusqueda.setFont(new Font("Roboto", Font.PLAIN, 14));
        campoBusqueda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarUsuarios();
            }
        });
        formularioFiltros.setOnFiltrosCambiados(this::filtrarUsuarios);
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));
        panelBotones.setOpaque(false);
        panelBotones.setPreferredSize(new Dimension(900, 40));
        panelBotones.setMaximumSize(new Dimension(900, 40));
        panelBotones.add(campoBusqueda);
        panelBotones.add(Box.createHorizontalGlue());
        panelBotones.add(botonRegistrar);
        JPanel panelBotonesExpansible = new JPanel();
        panelBotonesExpansible.setLayout(new BoxLayout(panelBotonesExpansible, BoxLayout.X_AXIS));
        panelBotonesExpansible.setOpaque(false);
        panelBotonesExpansible.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
        panelBotonesExpansible.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panelBotonesExpansible.add(Box.createHorizontalGlue());
        panelBotonesExpansible.add(panelBotones);
        panelBotonesExpansible.add(Box.createHorizontalGlue());
        JScrollPane panelScroll = new JScrollPane(tablaUsuarios);
        panelScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panelScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panelScroll.setAlignmentX(CENTER_ALIGNMENT);
        panelScroll.setPreferredSize(new Dimension(900, 400));
        panelScroll.setMaximumSize(new Dimension(900, Integer.MAX_VALUE));
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(etiquetaTitulo);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(formularioFiltros);
        add(Box.createRigidArea(new Dimension(0, 100)));
        add(panelBotonesExpansible);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(panelScroll);
        add(Box.createVerticalGlue());
        List<UsuarioEntity> usuarios = obtenerUsuarios();
        this.listaUsuariosCompleta = usuarios;
        tablaUsuarios.establecerUsuarios(usuarios);
        List<DepartamentoEntity> departamentos = obtenerDepartamentos();
        formularioFiltros.setDepartamentos(departamentos);
        List<RolEntity> roles = obtenerRoles();
        PermissionService servicioPermisos = new PermissionService(null);
        boolean puedeRegistrar = servicioPermisos.tienePermiso((long) Permiso.CREAR_GESTION_USUARIO.getId());
        botonRegistrar.setVisible(puedeRegistrar);
        if (puedeRegistrar) {
            agregarAccion(botonRegistrar, () -> {
                new RegistrarUsuario(ventana, departamentos, roles, this, null);
            });
        }
    }

    private void agregarAccion(JButton boton, Runnable accion) {
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                accion.run();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                boton.setBackground(ColorsTheme.PRIMARY_LIGTH.get());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(ColorsTheme.PRIMARY.get());
            }
        });
    }

    @Override
    public void crearUsuario(UsuarioEntity usuario) {
        controladorUsuarios.crear(usuario);
        actualizarTabla();
        JOptionPane.showMessageDialog(this,
                "Usuario creado exitosamente:\n\nNombre: " + usuario.getNombre() + "\nCorreo: " + usuario.getCorreo(),
                "Usuario Creado", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void verUsuario(UsuarioEntity usuario) {
        List<DepartamentoEntity> departamentos = obtenerDepartamentos();
        List<RolEntity> roles = obtenerRoles();
        new RegistrarUsuario(ventana, departamentos, roles, this, usuario);
    }

    @Override
    public void actualizarUsuario(UsuarioEntity usuario) {
        controladorUsuarios.actualizar(usuario.getUsuarioId(), usuario);
        actualizarTabla();
        JOptionPane.showMessageDialog(this,
                "Usuario actualizado exitosamente:\n\nNombre: " + usuario.getNombre() + "\nCorreo: " + usuario.getCorreo(),
                "Usuario Actualizado", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void eliminarUsuario(UsuarioEntity usuario) {
        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar al usuario " + usuario.getNombre() + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            controladorUsuarios.eliminar(usuario.getUsuarioId());
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.",
                    "Eliminación exitosa", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void actualizarTabla() {
        this.listaUsuariosCompleta = obtenerUsuarios();
        filtrarUsuarios();
    }

    private void filtrarUsuarios() {
        String texto = campoBusqueda.getText() != null ? campoBusqueda.getText().toLowerCase().trim() : "";
        String estado = formularioFiltros.obtenerEstadoSeleccionado();
        String departamento = formularioFiltros.obtenerDepartamentoSeleccionado();
        String desde = formularioFiltros.obtenerFechaDesde();
        String hasta = formularioFiltros.obtenerFechaHasta();
        List<UsuarioEntity> filtrados = listaUsuariosCompleta.stream()
                .filter(u -> {
                    String idUsuario = String.valueOf(u.getUsuarioId());
                    String nombre = u.getNombre() != null ? u.getNombre().toLowerCase() : "";
                    String correo = u.getCorreo() != null ? u.getCorreo().toLowerCase() : "";
                    String estadoUsuario = u.getEstaActivo() != null ? (u.getEstaActivo() ? "activo" : "inactivo") : "";
                    String rol = (u.getRol() != null && u.getRol().getNombre() != null)
                        ? u.getRol().getNombre().toLowerCase() : "sin rol";
                    String nombreDep = (u.getDepartamento() != null && u.getDepartamento().getNombre() != null)
                        ? u.getDepartamento().getNombre().toLowerCase() : "sin departamento";
                    boolean coincideTexto = texto.isEmpty()
                        || idUsuario.contains(texto)
                        || nombre.contains(texto)
                        || correo.contains(texto)
                        || estadoUsuario.contains(texto)
                        || rol.contains(texto)
                        || nombreDep.contains(texto);
                    boolean coincideEstado = estado.equals("Todos") || estado.equals("Activos") && Boolean.TRUE.equals(u.getEstaActivo()) || estado.equals("Inactivos") && Boolean.FALSE.equals(u.getEstaActivo());
                    boolean coincideDep = departamento.equals("Todos") || departamento.equalsIgnoreCase(nombreDep);
                    boolean coincideFecha = true;
                    if (desde != null && hasta != null && u.getFechaCreacion() != null) {
                        String fecha = u.getFechaCreacion().toLocalDate().toString();
                        coincideFecha = fecha.compareTo(desde) >= 0 && fecha.compareTo(hasta) <= 0;
                    }
                    return coincideTexto && coincideEstado && coincideDep && coincideFecha;
                })
                .collect(Collectors.toList());
        tablaUsuarios.establecerUsuarios(filtrados);
    }

    private List<UsuarioEntity> obtenerUsuarios() {
        return controladorUsuarios.buscarTodos();
    }

    private List<DepartamentoEntity> obtenerDepartamentos() {
        return controladorDepartamentos.buscarTodos();
    }

    private List<RolEntity> obtenerRoles() {
        return controladorRoles.buscarTodos();
    }
}
