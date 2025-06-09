package com.hersac.ui.views;

import com.hersac.core.modules.authentication.servicios.AuthenticationServices;
import com.hersac.core.modules.authentication.servicios.impl.AuthenticationServiceImpl;
import com.hersac.core.modules.usuarios.entities.repositories.UsuarioRepository;
import com.hersac.core.modules.usuarios.entities.repositories.impl.UsuarioRepositoryImpl;
import com.hersac.core.modules.usuarios.services.UsuariosServices;
import com.hersac.core.modules.usuarios.services.impl.UsuariosServicesImpl;
import com.hersac.ui.controllers.authentication.AuthenticationController;
import com.hersac.ui.views.authentication.login.LoginView;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.swing.*;
import java.awt.*;

public class AppViews extends JFrame {
    private EntityManager entityManager;

    public AppViews() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistenciaHerp");
        entityManager = entityManagerFactory.createEntityManager();

        UsuarioRepository usuarioRepository = new UsuarioRepositoryImpl(entityManager);
        UsuariosServices usuariosServices = new UsuariosServicesImpl(usuarioRepository);
        AuthenticationServices authServices = new AuthenticationServiceImpl(usuariosServices);
        AuthenticationController authController = new AuthenticationController(authServices);

        setTitle("HERP");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMaximumSize(new Dimension(1920, 1080));
        setMinimumSize(new Dimension(800, 600));
        setPreferredSize(new Dimension(800, 700));
        setLocationRelativeTo(null);

        CardLayout vistaContent = new CardLayout();
        JPanel bg = new JPanel(vistaContent);
        LoginView loginView = new LoginView(authController);
        loginView.setOpaque(true);

        bg.add(loginView);
        setContentPane(bg);

        setVisible(true);
    }
}
