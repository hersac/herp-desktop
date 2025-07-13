package com.hersac.ui.views.usuarios.rolesPermisos.modales;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import com.hersac.core.modules.roles.entities.RolEntity;
import com.hersac.core.modules.permisos.entities.PermisoEntity;
import com.hersac.ui.views.usuarios.rolesPermisos.tablas.PermisosTable;

public class RegistrarRolPermiso extends JDialog {
    public JTextField campoNombre;
    public JTextArea areaDescripcion;
    public JCheckBox checkActivo;
    public JButton botonGuardar;
    public boolean guardado = false;
    public RolEntity rolCreado;
    public List<Long> permisosSeleccionados;
    public PermisosTable tablaPermisos;
    public JComboBox<String> selectorModulo;
    private static final String[] MODULOS = {"Comercial", "Financiero", "Usuarios", "Terceros"};
    private static final String[][] SUBMODULOS = {
        {"Clientes", "Ventas", "Compras", "Inventario", "Reportes (Comercial)"},
        {"CxC", "CxP", "Movimientos", "Bancos", "Reportes (Financiero)"},
        {"Gestión de usuarios", "Roles y permisos", "Auditoría"},
        {"Terceros"}
    };
    private Map<String, Boolean[]> mapaPermisosSeleccionados = new HashMap<>();

    public RegistrarRolPermiso(JFrame padre, List<PermisoEntity> permisos) {
        super(padre, "Registrar Rol", true);
        setLayout(new BorderLayout(10, 10));
        setSize(500, 600);
        setLocationRelativeTo(padre);

        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel etiquetaNombre = new JLabel("Nombre del Rol:");
        etiquetaNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        etiquetaNombre.setFont(new Font("Roboto", Font.PLAIN, 14));
        panelFormulario.add(etiquetaNombre);
        campoNombre = new JTextField();
        campoNombre.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        campoNombre.setPreferredSize(new Dimension(200, 30));
        campoNombre.setMinimumSize(new Dimension(100, 30));
        campoNombre.setFont(new Font("Roboto", Font.PLAIN, 14));
        panelFormulario.add(campoNombre);

        JLabel etiquetaDescripcion = new JLabel("Descripción:");
        etiquetaDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        etiquetaDescripcion.setFont(new Font("Roboto", Font.PLAIN, 14));
        panelFormulario.add(etiquetaDescripcion);
        areaDescripcion = new JTextArea(3, 20);
        areaDescripcion.setFont(new Font("Roboto", Font.PLAIN, 14));
        areaDescripcion.setLineWrap(true);
        areaDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDescripcion = new JScrollPane(areaDescripcion);
        scrollDescripcion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        scrollDescripcion.setPreferredSize(new Dimension(200, 30));
        scrollDescripcion.setMinimumSize(new Dimension(100, 30));
        panelFormulario.add(scrollDescripcion);

        JLabel etiquetaModulo = new JLabel("Selecciona módulo:");
        etiquetaModulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        etiquetaModulo.setFont(new Font("Roboto", Font.PLAIN, 14));
        panelFormulario.add(etiquetaModulo);
        selectorModulo = new JComboBox<>(MODULOS);
        selectorModulo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        selectorModulo.setPreferredSize(new Dimension(200, 30));
        selectorModulo.setMinimumSize(new Dimension(100, 30));
        selectorModulo.setFont(new Font("Roboto", Font.PLAIN, 14));
        panelFormulario.add(selectorModulo);

        tablaPermisos = new PermisosTable(SUBMODULOS[0]);
        tablaPermisos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        tablaPermisos.setPreferredSize(new Dimension(400, 200));
        tablaPermisos.setMinimumSize(new Dimension(200, 200));
        panelFormulario.add(tablaPermisos);

        precargarPermisos(permisos);

        selectorModulo.addActionListener(e -> {
            guardarSeleccionActual();
            int idx = selectorModulo.getSelectedIndex();
            tablaPermisos.setSubmodulos(SUBMODULOS[idx], mapaPermisosSeleccionados);
        });

        checkActivo = new JCheckBox("Activo", true);
        checkActivo.setFont(new Font("Roboto", Font.PLAIN, 14));
        panelFormulario.add(checkActivo);

        JPanel panelPadding = new JPanel(new BorderLayout());
        panelPadding.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelPadding.add(panelFormulario, BorderLayout.CENTER);

        add(panelPadding, BorderLayout.CENTER);

        botonGuardar = new JButton("Guardar");
        botonGuardar.setFont(new Font("Roboto", Font.PLAIN, 14));
        botonGuardar.addActionListener(e -> {
            if (campoNombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre del rol es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            guardarSeleccionActual();
            rolCreado = RolEntity.builder()
                    .rolId(null)
                    .nombre(campoNombre.getText().trim())
                    .descripcion(areaDescripcion.getText().trim())
                    .estaActivo(checkActivo.isSelected())
                    .build();
            permisosSeleccionados = obtenerPermisosSeleccionados(permisos);
            guardado = true;
            setVisible(false);
        });

        JPanel panelBoton = new JPanel();
        panelBoton.add(botonGuardar);

        add(panelBoton, BorderLayout.SOUTH);
    }

    public RegistrarRolPermiso(JFrame padre, List<PermisoEntity> permisos, Map<String, Boolean[]> mapaPermisosSeleccionados) {
        this(padre, permisos);
        if (mapaPermisosSeleccionados != null) {
            this.mapaPermisosSeleccionados = new HashMap<>(mapaPermisosSeleccionados);
            tablaPermisos.setSubmodulos(SUBMODULOS[selectorModulo.getSelectedIndex()], this.mapaPermisosSeleccionados);
        }
    }

    public RegistrarRolPermiso(JFrame padre, List<PermisoEntity> permisos, Map<String, Boolean[]> mapaPermisosSeleccionados, boolean esEdicion) {
        this(padre, permisos, mapaPermisosSeleccionados);
        if (!esEdicion) {
            campoNombre.setEnabled(false);
            areaDescripcion.setEnabled(false);
            checkActivo.setEnabled(false);
            botonGuardar.setEnabled(false);
            selectorModulo.setEnabled(false);
            if (tablaPermisos != null) {
                tablaPermisos.setEnabled(false);
            }
        }
    }

    public boolean estaGuardado() { return guardado; }
    public RolEntity obtenerRolCreado() { return rolCreado; }
    private void guardarSeleccionActual() {
        String[] submodulosActuales = tablaPermisos.getSubmodulos();
        Map<String, Boolean[]> seleccionados = tablaPermisos.getSeleccionadosMap();
        for (String sub : submodulosActuales) {
            mapaPermisosSeleccionados.put(sub, seleccionados.get(sub));
        }
    }

    private void precargarPermisos(List<PermisoEntity> permisos) {
        for (String[] submodulosPorModulo : SUBMODULOS) {
            for (String sub : submodulosPorModulo) {
                mapaPermisosSeleccionados.put(sub, new Boolean[]{false, false, false, false});
            }
        }
        int[] basePermisoPorModulo = {1, 21, 41, 53};
        for (int moduloIdx = 0; moduloIdx < SUBMODULOS.length; moduloIdx++) {
            int idPermiso = basePermisoPorModulo[moduloIdx];
            for (String submodulo : SUBMODULOS[moduloIdx]) {
                Boolean[] checks = new Boolean[]{false, false, false, false};
                for (int j = 0; j < 4; j++) {
                    final int idPermisoFinal = idPermiso;
                    boolean tienePermiso = false;
                    for (PermisoEntity p : permisos) {
                        if (p.getPermisoId() != null && p.getPermisoId() == idPermisoFinal) {
                            tienePermiso = true;
                            break;
                        }
                    }
                    checks[j] = tienePermiso;
                    idPermiso++;
                }
                mapaPermisosSeleccionados.put(submodulo, checks);
            }
        }
        if (selectorModulo != null && tablaPermisos != null) {
            int idx = selectorModulo.getSelectedIndex();
            tablaPermisos.setSubmodulos(SUBMODULOS[idx], mapaPermisosSeleccionados);
        }
    }

    private List<Long> obtenerPermisosSeleccionados(List<PermisoEntity> permisos) {
        List<Long> seleccionados = new ArrayList<>();
        int[] basePermisoPorModulo = {1, 21, 41, 53};
        for (int moduloIdx = 0; moduloIdx < MODULOS.length; moduloIdx++) {
            int idPermiso = basePermisoPorModulo[moduloIdx];
            for (String submodulo : SUBMODULOS[moduloIdx]) {
                Boolean[] checks = mapaPermisosSeleccionados.get(submodulo);
                if (checks != null) {
                    for (int j = 0; j < 4; j++) {
                        if (checks[j] != null && checks[j]) {
                            seleccionados.add((long) idPermiso);
                        }
                        idPermiso++;
                    }
                } else {
                    idPermiso += 4;
                }
            }
        }
        return seleccionados;
    }

    public List<Long> obtenerPermisosSeleccionadosLista() {
        return permisosSeleccionados != null ? permisosSeleccionados : new ArrayList<>();
    }
}
