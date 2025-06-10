package com.hersac.ui.views;

import com.hersac.core.di.DIContainer;
import com.hersac.ui.views.authentication.login.LoginView;

import javax.swing.*;
import java.awt.*;

public class AppViews extends JFrame {

    public AppViews(DIContainer container) {


        setTitle("HERP");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMaximumSize(new Dimension(1920, 1080));
        setMinimumSize(new Dimension(800, 600));
        setPreferredSize(new Dimension(800, 700));
        setLocationRelativeTo(null);

        CardLayout vistaContent = new CardLayout();
        JPanel bg = new JPanel(vistaContent);
        LoginView loginView = container.getLoginView();
        loginView.setOpaque(true);

        bg.add(loginView);
        setContentPane(bg);

        setVisible(true);
    }
}
