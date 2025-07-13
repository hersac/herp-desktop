package com.hersac.ui.views.usuarios.rolesPermisos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import com.hersac.core.di.DIContainer;
import com.hersac.core.modules.rolespermisos.entities.RolPermisoEntity;
import com.hersac.core.modules.roles.entities.RolEntity;
import com.hersac.core.modules.permisos.entities.PermisoEntity;
import com.hersac.ui.controllers.permisos.PermisosController;
import com.hersac.ui.controllers.roles.RolesController;
import com.hersac.ui.controllers.rolesPermisos.RolesPermisosController;
import com.hersac.ui.views.usuarios.rolesPermisos.forms.FiltrosForm;
import com.hersac.ui.views.usuarios.rolesPermisos.listeners.RolesPermisosListener;
import com.hersac.ui.views.usuarios.rolesPermisos.tablas.RolesPermisosTable;
import com.hersac.ui.views.usuarios.rolesPermisos.modales.RegistrarRolPermiso;
import com.hersac.core.globals.store.UserSessionStore;
import com.hersac.ui.globals.enums.ColorsTheme;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

public class RolesPermisos extends JPanel implements RolesPermisosListener {
    private final RolesPermisosController controladorRolesPermisos;
    private final RolesController controladorRoles;
    private final PermisosController controladorPermisos;
    private final RolesPermisosTable tablaRolesPermisos = new RolesPermisosTable();
    private List<RolEntity> listaRolesCompleta;
    private FiltrosForm formularioFiltros;
    private JTextField campoBusqueda;
    private JFrame ventana = new JFrame("Registrar Rol-Permiso");

    public RolesPermisos(DIContainer contenedorDI) {
        this.controladorRolesPermisos = contenedorDI.getRolesPermisosController();
        this.controladorRoles = contenedorDI.getRolesController();
        this.controladorPermisos = contenedorDI.getPermisosController();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new Dimension(800, 600));

        Set<Long> permisosUsuario = UserSessionStore.getInstance().getPermisosUsuario();
        boolean puedeVer = permisosUsuario.contains(45L);
        boolean puedeRegistrar = permisosUsuario.contains(46L);
        boolean puedeEditar = permisosUsuario.contains(47L);
        boolean puedeEliminar = permisosUsuario.contains(48L);

        tablaRolesPermisos.establecerPermisos(puedeVer, puedeEditar, puedeEliminar);

        JLabel etiquetaTitulo = new JLabel("Roles y Permisos");
        etiquetaTitulo.setFont(new Font("Roboto", Font.BOLD, 24));
        etiquetaTitulo.setAlignmentX(CENTER_ALIGNMENT);

        formularioFiltros = new FiltrosForm();

        JButton botonRegistrar = new JButton("Registrar Rol");
        FontIcon iconoAgregar = FontIcon.of(FontAwesomeSolid.PLUS, 18, ColorsTheme.TEXT_PRIMARY.get());
        botonRegistrar.setFont(new Font("Roboto", Font.PLAIN, 14));
        botonRegistrar.setBackground(ColorsTheme.PRIMARY.get());
        botonRegistrar.setForeground(ColorsTheme.TEXT_PRIMARY.get());
        botonRegistrar.setIcon(iconoAgregar);
        botonRegistrar.setEnabled(puedeRegistrar);

        campoBusqueda = new JTextField(25);
        campoBusqueda.setMaximumSize(new Dimension(400, 30));
        campoBusqueda.setAlignmentX(CENTER_ALIGNMENT);
        campoBusqueda.setFont(new Font("Roboto", Font.PLAIN, 14));
        campoBusqueda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarRoles();
            }
        });

        formularioFiltros.setOnFiltrosCambiados(this::filtrarRoles);

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

        JScrollPane panelDesplazamiento = new JScrollPane(tablaRolesPermisos);
        panelDesplazamiento.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panelDesplazamiento.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panelDesplazamiento.setAlignmentX(CENTER_ALIGNMENT);
        panelDesplazamiento.setPreferredSize(new Dimension(900, 400));
        panelDesplazamiento.setMaximumSize(new Dimension(900, Integer.MAX_VALUE));

        add(Box.createRigidArea(new Dimension(0, 10)));
        add(etiquetaTitulo);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(formularioFiltros);
        add(Box.createRigidArea(new Dimension(0, 100)));
        add(panelBotonesExpansible);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(panelDesplazamiento);
        add(Box.createVerticalGlue());

        List<RolEntity> roles = controladorRoles.buscarTodos();
        this.listaRolesCompleta = roles;
        tablaRolesPermisos.establecerRoles(roles);

        botonRegistrar.addActionListener(e -> mostrarModalRegistrar());
        tablaRolesPermisos.establecerEscuchador(this);
        tablaRolesPermisos.establecerControlador(controladorRolesPermisos);
    }

    private void mostrarModalRegistrar() {
        List<PermisoEntity> permisos = controladorPermisos.buscarTodos();
        RegistrarRolPermiso modal = new RegistrarRolPermiso(ventana, permisos);
        modal.setVisible(true);
        if (!modal.estaGuardado()) return;
        RolEntity nuevoRol = modal.obtenerRolCreado();
        List<Long> permisosSeleccionados = modal.obtenerPermisosSeleccionadosLista();
        RolEntity rolGuardado = controladorRoles.crear(nuevoRol, permisosSeleccionados);
        if (rolGuardado != null && rolGuardado.getRolId() != null) {
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Rol y permisos asociados creados exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Error al crear el rol.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void actualizarTabla() {
        this.listaRolesCompleta = controladorRoles.buscarTodos();
        filtrarRoles();
    }

    private void filtrarRoles() {
        String texto = campoBusqueda.getText() != null ? campoBusqueda.getText().toLowerCase().trim() : "";
        String estado = formularioFiltros.getEstadoSeleccionado();
        List<RolEntity> filtrados = listaRolesCompleta.stream()
                .filter(rol -> {
                    String nombre = rol.getNombre() != null ? rol.getNombre().toLowerCase() : "";
                    String descripcion = rol.getDescripcion() != null ? rol.getDescripcion().toLowerCase() : "";
                    boolean coincideTexto = texto.isEmpty() || nombre.contains(texto) || descripcion.contains(texto);
                    boolean coincideEstado = "Todos".equals(estado) || ("Activos".equals(estado) && Boolean.TRUE.equals(rol.getEstaActivo())) || ("Inactivos".equals(estado) && Boolean.FALSE.equals(rol.getEstaActivo()));
                    return coincideTexto && coincideEstado;
                })
                .collect(Collectors.toList());
        tablaRolesPermisos.establecerRoles(filtrados);
    }

    @Override
    public void crearRol(RolEntity rol, List<Long> permisos) {
        RolEntity rolGuardado = controladorRoles.crear(rol, permisos);
        if (rolGuardado != null && rolGuardado.getRolId() != null) {
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Rol creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Error al crear el rol.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void editarRol(RolEntity rol, List<Long> permisos) {
        controladorRoles.actualizar(rol.getRolId(), rol, permisos);
        actualizarTabla();
        JOptionPane.showMessageDialog(this, "Rol actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void eliminarRol(RolEntity rol) {
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar el rol '" + rol.getNombre() + "'?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            controladorRoles.eliminar(rol.getRolId());
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Rol eliminado correctamente.",
                    "Eliminación exitosa", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void mostrarModalEditarRol(RolEntity rol) {
        List<PermisoEntity> permisos = controladorPermisos.buscarTodos();
        List<RolPermisoEntity> rolPermisos = controladorRolesPermisos.buscarPorRolId(rol.getRolId());
        Map<String, Boolean[]> mapaPermisos = construirMapaPermisos(rolPermisos, permisos);
        boolean puedeEditar = UserSessionStore.getInstance().getPermisosUsuario().contains(47L);
        RegistrarRolPermiso modal = new RegistrarRolPermiso(ventana, permisos, mapaPermisos, true);
        modal.setTitle("Editar Rol");
        modal.campoNombre.setText(rol.getNombre());
        modal.areaDescripcion.setText(rol.getDescripcion());
        modal.checkActivo.setSelected(Boolean.TRUE.equals(rol.getEstaActivo()));
        modal.campoNombre.setFont(new Font("Roboto", Font.PLAIN, 14));
        modal.areaDescripcion.setFont(new Font("Roboto", Font.PLAIN, 14));
        if (!puedeEditar) {
            modal.campoNombre.setEnabled(false);
            modal.areaDescripcion.setEnabled(false);
            modal.checkActivo.setEnabled(false);
            modal.botonGuardar.setEnabled(false);
            modal.selectorModulo.setEnabled(false);
            if (modal.tablaPermisos != null) {
                modal.tablaPermisos.setEnabled(false);
            }
        }
        modal.setVisible(true);
        if (modal.estaGuardado() && puedeEditar) {
            RolEntity rolEditado = modal.obtenerRolCreado();
            rol.setNombre(rolEditado.getNombre());
            rol.setDescripcion(rolEditado.getDescripcion());
            rol.setEstaActivo(rolEditado.getEstaActivo());
            editarRol(rol, modal.obtenerPermisosSeleccionadosLista());
        }
    }

    private Map<String, Boolean[]> construirMapaPermisos(List<RolPermisoEntity> rolPermisos, List<PermisoEntity> permisos) {
        Map<String, Boolean[]> mapa = new HashMap<>();
        Set<Long> idsSeleccionados = new HashSet<>();
        for (RolPermisoEntity rp : rolPermisos) {
            if (rp.getPermiso() != null && rp.getPermiso().getPermisoId() != null) {
                idsSeleccionados.add(rp.getPermiso().getPermisoId());
            }
        }
        String[][] submodulosPorModulo = {
            {"Clientes", "Ventas", "Compras", "Inventario", "Reportes (Comercial)"},
            {"CxC", "CxP", "Movimientos", "Bancos", "Reportes (Financiero)"},
            {"Gestión de usuarios", "Roles y permisos", "Auditoría"},
            {"Terceros"}
        };
        int[] basePermisoPorModulo = {1, 21, 41, 53};
        for (int moduloIdx = 0; moduloIdx < submodulosPorModulo.length; moduloIdx++) {
            String[] submodulos = submodulosPorModulo[moduloIdx];
            int idPermiso = basePermisoPorModulo[moduloIdx];
            for (String submodulo : submodulos) {
                Boolean[] checks = new Boolean[]{false, false, false, false};
                for (int j = 0; j < 4; j++) {
                    if (idsSeleccionados.contains((long) idPermiso)) {
                        checks[j] = true;
                    }
                    idPermiso++;
                }
                mapa.put(submodulo, checks);
            }
        }
        return mapa;
    }
}
