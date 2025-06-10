package com.hersac.core.di;

import com.hersac.core.modules.authentication.servicios.AuthenticationServices;
import com.hersac.core.modules.authentication.servicios.impl.AuthenticationServiceImpl;
import com.hersac.core.modules.usuarios.entities.repositories.UsuarioRepository;
import com.hersac.core.modules.usuarios.entities.repositories.impl.UsuarioRepositoryImpl;
import com.hersac.core.modules.usuarios.services.UsuariosServices;
import com.hersac.core.modules.usuarios.services.impl.UsuariosServicesImpl;
import com.hersac.ui.controllers.authentication.AuthenticationController;
import com.hersac.ui.views.authentication.login.LoginView;
import jakarta.persistence.EntityManager;

public class DIContainer {
    private final EntityManager entityManager;

    public DIContainer(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public AuthenticationController getAuthenticationController() {
        UsuarioRepository usuarioRepository = new UsuarioRepositoryImpl(entityManager);
        UsuariosServices usuariosServices = new UsuariosServicesImpl(usuarioRepository);
        AuthenticationServices authenticationServices = new AuthenticationServiceImpl(usuariosServices);

        return new AuthenticationController(authenticationServices);
    }

    public LoginView getLoginView() {
        AuthenticationController authController = getAuthenticationController();
        return new LoginView(authController);
    }
}
