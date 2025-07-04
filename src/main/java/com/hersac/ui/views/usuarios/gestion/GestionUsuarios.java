package com.hersac.ui.views.usuarios.gestion;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
    private final DepartamentosController departamentosCotroller;
    private final RolesPermisosController rolesPemisosController;
    private final UsuariosTable tablaUsuariosTable = new UsuariosTable();

    public GestionUsuarios(DIContainer diContainer) {
        this.usuariosController = diContainer.getUsuariosController();
        this.departamentosCotroller = diContainer.getDepartamentosController();
        this.rolesPemisosController = diContainer.getRolesPermisosController();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new Dimension(800, 600));

        JLabel titleLabel = new JLabel("Gestión de Usuarios");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        FiltrosForm filtrosForm = new FiltrosForm();

        JButton registrarBtn = new JButton("Registrar Usuario");
        FontIcon iconVer = FontIcon.of(FontAwesomeSolid.PLUS, 18, ColorsTheme.TEXT_PRIMARY.get());
        registrarBtn.setFont(new Font("Roboto", Font.PLAIN, 14));
        registrarBtn.setBackground(ColorsTheme.PRIMARY.get());
        registrarBtn.setForeground(ColorsTheme.TEXT_PRIMARY.get());
        registrarBtn.setIcon(iconVer);

        List<DepartamentoEntity> departamentos = obtenerDepartamentos();
        List<RolPermisoEntity> roles = obtenerRoles();

        ejecutarAccion(registrarBtn, () -> {
            new RegistrarUsuario(frame, departamentos, roles, this);
        });

        JTextField searchField = new JTextField(25);
        searchField.setMaximumSize(new Dimension(400, 30));
        searchField.setAlignmentX(CENTER_ALIGNMENT);

        JPanel panelBtn = new JPanel();
        panelBtn.setLayout(new BoxLayout(panelBtn, BoxLayout.X_AXIS));
        panelBtn.setOpaque(false);
        panelBtn.setPreferredSize(new Dimension(800, 40));
        panelBtn.setMaximumSize(new Dimension(800, 40));
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

        tablaUsuariosTable.setAlignmentX(CENTER_ALIGNMENT);
        tablaUsuariosTable.setMinimumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        tablaUsuariosTable.setPreferredSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        tablaUsuariosTable.setMaximumSize(new Dimension(800, Integer.MAX_VALUE));

        add(Box.createRigidArea(new Dimension(0, 10)));
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(filtrosForm);
        add(Box.createRigidArea(new Dimension(0, 100)));
        add(panelBtnExpansible);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(tablaUsuariosTable);
        add(Box.createVerticalGlue());

        List<UsuarioEntity> listaUsuarios = obtenerUsuarios();
        tablaUsuariosTable.setUsuarios(listaUsuarios);
    }

    private void ejecutarAccion(JButton panel, Runnable accion) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                accion.run();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                panel.setBackground(ColorsTheme.PRIMARY_LIGTH.get());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(ColorsTheme.PRIMARY.get());
            }
        });
    }

    @Override
    public void crearUsuario(UsuarioEntity usuario) {
        usuariosController.crear(usuario);

        List<UsuarioEntity> listaActualizada = obtenerUsuarios();
        tablaUsuariosTable.setUsuarios(listaActualizada);

        JOptionPane.showMessageDialog(this,
                "Usuario creado exitosamente:\n\nNombre: " + usuario.getNombre() + "\nCorreo: " + usuario.getCorreo(),
                "Usuario Creado", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void verUsuario(UsuarioEntity usuario) {
        JOptionPane.showMessageDialog(this,
                "Detalles de usuario:\n\nNombre: " + usuario.getNombre() + "\nCorreo: " + usuario.getCorreo(),
                "Ver Usuario", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void actualizarUsuario(UsuarioEntity usuario) {
        System.out.println(
                "Usuario " + usuario.getNombre() + " ahora está " + (usuario.getEstaActivo() ? "activo" : "inactivo"));
        // Aquí podrías guardar el nuevo estado en la base de datos usando el controller
    }

    @Override
    public void eliminarUsuario(UsuarioEntity usuario) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar al usuario " + usuario.getNombre() + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.out.println("Eliminado: " + usuario.getNombre());
            // Eliminar desde el controller y recargar tabla
        }
    }

    private List<UsuarioEntity> obtenerUsuarios() {
        return usuariosController.buscarTodos();
    }

    private List<DepartamentoEntity> obtenerDepartamentos() {
        return departamentosCotroller.buscarTodos();
    }

    private List<RolPermisoEntity> obtenerRoles() {
        return rolesPemisosController.buscarTodos();
    }
}
