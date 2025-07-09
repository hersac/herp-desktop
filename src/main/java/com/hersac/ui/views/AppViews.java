package com.hersac.ui.views;

import com.hersac.core.di.DIContainer;
import com.hersac.ui.views.authentication.login.LoginView;
import com.hersac.ui.components.NavbarComponent;
import com.hersac.ui.components.SidebarComponent;
import com.hersac.ui.views.usuarios.auditorias.Auditorias;
import com.hersac.ui.views.usuarios.gestion.GestionUsuarios;
import com.hersac.ui.views.usuarios.rolesPermisos.RolesPermisos;

import javax.swing.*;
import java.awt.*;

public class AppViews extends JFrame {

    private final JPanel bg;
    private JPanel contenido;
    private final DIContainer container;

    public AppViews(DIContainer container) {
        this.container = container;

        setTitle("HERP");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMaximumSize(new Dimension(1920, 1080));
        setMinimumSize(new Dimension(800, 600));
        setPreferredSize(new Dimension(800, 700));
        setLocationRelativeTo(null);

        bg = new JPanel(new BorderLayout());
        setContentPane(bg);

        LoginView loginView = container.getLoginView();
        bg.add(loginView, BorderLayout.CENTER);

        loginView.addLoginListener(token -> {
            if (token != null) {
                bg.removeAll();

                NavbarComponent navbar = new NavbarComponent(container.getPermissionService());
                SidebarComponent sidebar = new SidebarComponent();

                contenido = new JPanel(new BorderLayout());
                contenido.setBackground(Color.WHITE);
                contenido.add(new JLabel("Bienvenido al sistema"), BorderLayout.NORTH);

                navbar.setNavigationListener(destino -> {
                    contenido.removeAll();

                    switch (destino) {
                        case "gestion-usuarios" -> contenido.add(new GestionUsuarios(container), BorderLayout.CENTER);
                        case "roles-permisos" -> contenido.add(new RolesPermisos(container), BorderLayout.CENTER);
                        case "auditorias" -> contenido.add(new Auditorias(), BorderLayout.CENTER);
                    }

                    contenido.revalidate();
                    contenido.repaint();
                });

                navbar.setLogoutListener(() -> {
                    bg.removeAll();
                    LoginView newLoginView = container.getLoginView();
                    bg.add(newLoginView, BorderLayout.CENTER);
                    bg.revalidate();
                    bg.repaint();
                    newLoginView.addLoginListener(token2 -> {
                        if (token2 != null) {
                            bg.removeAll();
                            NavbarComponent newNavbar = new NavbarComponent(container.getPermissionService());
                            SidebarComponent newSidebar = new SidebarComponent();
                            contenido = new JPanel(new BorderLayout());
                            contenido.setBackground(Color.WHITE);
                            contenido.add(new JLabel("Bienvenido al sistema"), BorderLayout.NORTH);
                            newNavbar.setNavigationListener(navbar.getNavigationListener());
                            newNavbar.setLogoutListener(this::reiniciarLogin);
                            bg.add(newNavbar, BorderLayout.NORTH);
                            bg.add(newSidebar, BorderLayout.WEST);
                            bg.add(contenido, BorderLayout.CENTER);
                            bg.revalidate();
                            bg.repaint();
                        }
                    });
                });

                bg.add(navbar, BorderLayout.NORTH);
                bg.add(sidebar, BorderLayout.WEST);
                bg.add(contenido, BorderLayout.CENTER);

                bg.revalidate();
                bg.repaint();
            }
        });

        setVisible(true);
    }

    // Método auxiliar para reiniciar el login correctamente
    private void reiniciarLogin() {
        bg.removeAll();
        LoginView loginView = container.getLoginView();
        bg.add(loginView, BorderLayout.CENTER);
        bg.revalidate();
        bg.repaint();
        loginView.addLoginListener(token -> {
            if (token != null) {
                bg.removeAll();
                NavbarComponent navbar = new NavbarComponent(container.getPermissionService());
                SidebarComponent sidebar = new SidebarComponent();
                contenido = new JPanel(new BorderLayout());
                contenido.setBackground(Color.WHITE);
                contenido.add(new JLabel("Bienvenido al sistema"), BorderLayout.NORTH);
                navbar.setNavigationListener(navbar.getNavigationListener());
                navbar.setLogoutListener(this::reiniciarLogin);
                bg.add(navbar, BorderLayout.NORTH);
                bg.add(sidebar, BorderLayout.WEST);
                bg.add(contenido, BorderLayout.CENTER);
                bg.revalidate();
                bg.repaint();
            }
        });
    }
}
