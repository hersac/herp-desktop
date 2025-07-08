package com.hersac.ui.views.usuarios.rolesPermisos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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

public class RolesPermisos extends JPanel implements RolesPermisosListener {
    private final RolesPermisosController rolesPermisosController;
    private final RolesController rolesController;
    private final PermisosController permisosController;
    private final RolesPermisosTable tablaRolesPermisos = new RolesPermisosTable();
    private List<RolEntity> listaCompletaRoles;
    private FiltrosForm filtrosForm;
    private JTextField searchField;
    private JFrame frame = new JFrame("Registrar Rol-Permiso");

    public RolesPermisos(DIContainer diContainer) {
        this.rolesPermisosController = diContainer.getRolesPermisosController();
        this.rolesController = diContainer.getRolesController();
        this.permisosController = diContainer.getPermisosController();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new Dimension(800, 600));

        // Obtener permisos del usuario actual
        java.util.Set<Long> permisosUsuario = UserSessionStore.getInstance().getPermisosUsuario();
        boolean puedeVer = permisosUsuario.contains(45L);
        boolean puedeRegistrar = permisosUsuario.contains(46L);
        boolean puedeEditar = permisosUsuario.contains(47L);
        boolean puedeEliminar = permisosUsuario.contains(48L);

        // Pasar permisos a la tabla
        tablaRolesPermisos.setPermisos(puedeVer, puedeEditar, puedeEliminar);

        JLabel titleLabel = new JLabel("Roles y Permisos");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        filtrosForm = new FiltrosForm();

        JButton registrarBtn = new JButton("Registrar Rol");
        registrarBtn.setFont(new Font("Roboto", Font.PLAIN, 14));
        registrarBtn.setBackground(new Color(33, 150, 243));
        registrarBtn.setForeground(Color.WHITE);
        registrarBtn.setEnabled(puedeRegistrar); // Solo habilitar si tiene permiso 46

        searchField = new JTextField(25);
        searchField.setMaximumSize(new Dimension(400, 30));
        searchField.setAlignmentX(CENTER_ALIGNMENT);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarRoles();
            }
        });

        filtrosForm.setOnFiltrosCambiados(this::filtrarRoles);

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

        JScrollPane scrollPane = new JScrollPane(tablaRolesPermisos);
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

        List<RolEntity> listaRoles = rolesController.buscarTodos();
        this.listaCompletaRoles = listaRoles;
        tablaRolesPermisos.setRoles(listaRoles);

        registrarBtn.addActionListener(e -> {
            List<PermisoEntity> permisos = permisosController.buscarTodos();
            RegistrarRolPermiso modal = new RegistrarRolPermiso(frame, permisos);
            modal.setVisible(true);
            if (modal.isGuardado()) {
                RolEntity nuevoRol = modal.getRolCreado();
                List<Long> permisosSeleccionados = modal.getPermisosSeleccionados();
                RolEntity rolGuardado = rolesController.crear(nuevoRol, permisosSeleccionados);
                if (rolGuardado != null && rolGuardado.getRolId() != null) {
                    actualizarTabla();
                    JOptionPane.showMessageDialog(this, "Rol y permisos asociados creados exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error al crear el rol.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        tablaRolesPermisos.setActionListener(this);
    }

    private void actualizarTabla() {
        this.listaCompletaRoles = rolesController.buscarTodos();
        filtrarRoles();
    }

    private void filtrarRoles() {
        final String texto = searchField.getText() != null ? searchField.getText().toLowerCase().trim() : "";
        final String estado = filtrosForm.getEstadoSeleccionado();
        List<RolEntity> filtrados = listaCompletaRoles.stream()
                .filter(rol -> {
                    String nombre = rol.getNombre() != null ? rol.getNombre().toLowerCase() : "";
                    String descripcion = rol.getDescripcion() != null ? rol.getDescripcion().toLowerCase() : "";
                    boolean coincideTexto = texto.isEmpty() || nombre.contains(texto) || descripcion.contains(texto);
                    boolean coincideEstado = estado.equals("Todos")
                            || (estado.equals("Activos") && Boolean.TRUE.equals(rol.getEstaActivo()))
                            || (estado.equals("Inactivos") && Boolean.FALSE.equals(rol.getEstaActivo()));
                    return coincideTexto && coincideEstado;
                })
                .collect(Collectors.toList());
        tablaRolesPermisos.setRoles(filtrados);
    }

    @Override
    public void crearRol(RolEntity rol, List<Long> permisos) {

        System.out.println("AQUI:" + rol + ", PERMISOS:" + permisos);

        RolEntity rolGuardado = rolesController.crear(rol, permisos);
        if (rolGuardado != null && rolGuardado.getRolId() != null) {
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Rol creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error al crear el rol.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void editarRol(RolEntity rol, List<Long> permisos) {
        System.out.println("AQUI 2:" + rol + ", PERMISOS:" + permisos);

        rolesController.actualizar(rol.getRolId(), rol, permisos);
        actualizarTabla();
        JOptionPane.showMessageDialog(this, "Rol actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void eliminarRol(RolEntity rol) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar el rol '" + rol.getNombre() + "'?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            rolesController.eliminar(rol.getRolId());
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Rol eliminado correctamente.",
                    "Eliminación exitosa", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void mostrarModalEditarRol(RolEntity rol) {
        List<PermisoEntity> permisos = permisosController.buscarTodos();
        List<RolPermisoEntity> rolPermisos = rolesPermisosController.buscarPorRolId(rol.getRolId());
        java.util.Map<String, Boolean[]> permisosMap = construirMapaPermisos(rolPermisos, permisos);
        com.hersac.ui.views.usuarios.rolesPermisos.modales.RegistrarRolPermiso modal = new com.hersac.ui.views.usuarios.rolesPermisos.modales.RegistrarRolPermiso(frame, permisos, permisosMap);
        modal.setTitle("Editar Rol");
        modal.nombreField.setText(rol.getNombre());
        modal.descripcionArea.setText(rol.getDescripcion());
        modal.activoCheck.setSelected(Boolean.TRUE.equals(rol.getEstaActivo()));
        modal.setVisible(true);
        if (modal.isGuardado()) {
            RolEntity rolEditado = modal.getRolCreado();
            rol.setNombre(rolEditado.getNombre());
            rol.setDescripcion(rolEditado.getDescripcion());
            rol.setEstaActivo(rolEditado.getEstaActivo());
            editarRol(rol, modal.getPermisosSeleccionados());
        }
    }

    private java.util.Map<String, Boolean[]> construirMapaPermisos(List<RolPermisoEntity> rolPermisos, List<PermisoEntity> permisos) {
        java.util.Map<String, Boolean[]> map = new java.util.HashMap<>();
        java.util.Set<Long> idsSeleccionados = new java.util.HashSet<>();
        for (RolPermisoEntity rp : rolPermisos) {
            if (rp.getPermiso() != null && rp.getPermiso().getPermisoId() != null) {
                idsSeleccionados.add(rp.getPermiso().getPermisoId());
            }
        }
        // Definir los submódulos por módulo, usando nombres únicos para los reportes
        String[][] submodulosPorModulo = {
            {"Clientes", "Ventas", "Compras", "Inventario", "Reportes (Comercial)"},
            {"CxC", "CxP", "Movimientos", "Bancos", "Reportes (Financiero)"},
            {"Gestión de usuarios", "Roles y permisos", "Auditoría"}
        };
        int[] basePermisoPorModulo = {1, 21, 41}; // Comercial inicia en 1, Financiero en 21, Usuarios en 41
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
                map.put(submodulo, checks);
            }
        }
        return map;
    }
}
