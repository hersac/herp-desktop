package com.hersac.ui.views.usuarios.gestion.modales;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.text.*;
import com.hersac.core.globals.servicios.PermissionService;
import com.hersac.ui.globals.enums.Permiso;
import com.hersac.core.modules.departamentos.entities.DepartamentoEntity;
import com.hersac.core.modules.roles.entities.RolEntity;
import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import com.hersac.ui.views.usuarios.gestion.listeners.UsuariosListeners;

public class RegistrarUsuario extends JDialog {
    private JTextField nombre;
    private JTextField correo;
    private JPasswordField contrasena;
    private JCheckBox activo;
    private JComboBox<DepartamentoEntity> departamento;
    private JComboBox<RolEntity> rol;
    private UsuarioEntity usuario;
    private final UsuariosListeners oyente;
    private final boolean edicion;

    public RegistrarUsuario(JFrame parent, List<DepartamentoEntity> departamentos, List<RolEntity> roles,
            UsuariosListeners oyente, UsuarioEntity usuarioEditar) {
        super(parent, usuarioEditar != null ? "Actualizar Usuario" : "Registrar Usuario", true);
        this.oyente = oyente;
        this.usuario = usuarioEditar;
        this.edicion = usuarioEditar != null;
        inicializar(departamentos, roles);
    }

    private void inicializar(List<DepartamentoEntity> departamentos, List<RolEntity> roles) {
        configurarVentana();
        JPanel panelFormulario = crearCampos(departamentos, roles);
        if (edicion && usuario != null) cargarDatosEdicion();
        JPanel panelBotones = crearPanelBotones();
        add(panelFormulario, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        PermissionService permisoServicio = new PermissionService(null);
        boolean puedeEditar = permisoServicio.tienePermiso((long) Permiso.EDITAR_GESTION_USUARIO.getId());
        if (!puedeEditar) deshabilitarCampos(panelBotones);
        setVisible(true);
    }

    private void configurarVentana() {
        setSize(600, 400);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private JPanel crearCampos(List<DepartamentoEntity> departamentos, List<RolEntity> roles) {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        Font roboto = new Font("Roboto", Font.PLAIN, 14);
        nombre = new JTextField();
        nombre.setFont(roboto);
        aplicarFiltroMayusculas(nombre);
        correo = new JTextField();
        correo.setFont(roboto);
        aplicarFiltroMinusculas(correo);
        contrasena = new JPasswordField();
        contrasena.setFont(roboto);
        activo = new JCheckBox("Activo", true);
        activo.setFont(roboto);
        departamento = new JComboBox<>();
        departamento.setFont(roboto);
        rol = new JComboBox<>();
        rol.setFont(roboto);
        departamentos.forEach(departamento::addItem);
        roles.forEach(rol::addItem);
        panel.add(crearLabel("Nombre:", roboto));
        panel.add(nombre);
        panel.add(crearLabel("Correo:", roboto));
        panel.add(correo);
        panel.add(crearLabel("Contraseña:", roboto));
        panel.add(contrasena);
        panel.add(crearLabel("Departamento:", roboto));
        panel.add(departamento);
        panel.add(crearLabel("Rol:", roboto));
        panel.add(rol);
        panel.add(crearLabel("¿Está activo?:", roboto));
        panel.add(activo);
        return panel;
    }

    private JLabel crearLabel(String texto, Font fuente) {
        JLabel label = new JLabel(texto);
        label.setFont(fuente);
        return label;
    }

    private void aplicarFiltroMayusculas(JTextField campo) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null) super.insertString(fb, offset, string.toUpperCase(), attr);
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text != null) super.replace(fb, offset, length, text.toUpperCase(), attrs);
            }
        });
    }

    private void aplicarFiltroMinusculas(JTextField campo) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null) super.insertString(fb, offset, string.toLowerCase(), attr);
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text != null) super.replace(fb, offset, length, text.toLowerCase(), attrs);
            }
        });
    }

    private void cargarDatosEdicion() {
        nombre.setText(usuario.getNombre());
        correo.setText(usuario.getCorreo());
        contrasena.setText(usuario.getContrasena());
        activo.setSelected(usuario.getEstaActivo());
        departamento.setSelectedItem(usuario.getDepartamento());
        rol.setSelectedItem(usuario.getRol());
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton guardar = new JButton(edicion ? "Actualizar" : "Guardar");
        JButton cancelar = new JButton("Cancelar");
        guardar.addActionListener(e -> guardarUsuario());
        cancelar.addActionListener(e -> dispose());
        panel.add(guardar);
        panel.add(cancelar);
        return panel;
    }

    private void deshabilitarCampos(JPanel panelBotones) {
        nombre.setEnabled(false);
        correo.setEnabled(false);
        contrasena.setEnabled(false);
        activo.setEnabled(false);
        departamento.setEnabled(false);
        rol.setEnabled(false);
        for (Component comp : panelBotones.getComponents()) {
            if (comp instanceof JButton boton && !boton.getText().equals("Cancelar")) boton.setEnabled(false);
        }
    }

    private void guardarUsuario() {
        DepartamentoEntity dep = (DepartamentoEntity) departamento.getSelectedItem();
        RolEntity rolSel = (RolEntity) rol.getSelectedItem();
        if (usuario == null) usuario = new UsuarioEntity();
        usuario.setNombre(nombre.getText());
        usuario.setCorreo(correo.getText());
        usuario.setContrasena(new String(contrasena.getPassword()));
        usuario.setEstaActivo(activo.isSelected());
        usuario.setDepartamento(dep);
        usuario.setRol(rolSel);
        usuario.setUsuarioActualizacion(UsuarioEntity.builder().usuarioId(1L).build());
        if (!edicion) {
            usuario.setUsuarioCreacion(UsuarioEntity.builder().usuarioId(1L).build());
            oyente.crearUsuario(usuario);
        }
        if (edicion) oyente.actualizarUsuario(usuario);
        dispose();
    }

    public UsuarioEntity getUsuarioRegistrado() {
        return usuario;
    }
}
