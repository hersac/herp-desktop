package com.hersac.ui.views;

import com.hersac.core.di.DIContainer;
import com.hersac.ui.views.authentication.login.LoginView;
import com.hersac.ui.views.components.NavbarComponent;
import com.hersac.ui.views.components.SidebarComponent;

import javax.swing.*;
import java.awt.*;

public class AppViews extends JFrame {

    private final JPanel bg;

    public AppViews(DIContainer container) {

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

        loginView.setLoginListener(token -> {
            if (token != null) {
                bg.removeAll();

                NavbarComponent navbar = new NavbarComponent();
                SidebarComponent sidebar = new SidebarComponent();
                JPanel contenido = new JPanel();
                contenido.setBackground(Color.WHITE);
                contenido.add(new JLabel("Bienvenido al sistema"));

                bg.add(navbar, BorderLayout.NORTH);
                bg.add(sidebar, BorderLayout.WEST);
                bg.add(contenido, BorderLayout.CENTER);

                bg.revalidate();
                bg.repaint();
            }
        });

        setVisible(true);
    }
}
