package com.hersac.core.di;

import com.hersac.core.modules.authentication.servicios.AuthenticationServices;
import com.hersac.core.modules.authentication.servicios.impl.AuthenticationServiceImpl;
import com.hersac.core.modules.departamentos.entities.repositories.DepartamentoRepository;
import com.hersac.core.modules.departamentos.entities.repositories.impl.DepartamentoRepositoryImpl;
import com.hersac.core.modules.departamentos.services.DepartamentosServices;
import com.hersac.core.modules.departamentos.services.impl.DepartamentosServicesImpl;
import com.hersac.core.modules.permisos.entities.repositories.PermisoRepository;
import com.hersac.core.modules.permisos.entities.repositories.impl.PermisoRepositoryImpl;
import com.hersac.core.modules.permisos.services.PermisosService;
import com.hersac.core.modules.permisos.services.impl.PermisosServiceImpl;
import com.hersac.core.modules.roles.entities.repositories.RolRepository;
import com.hersac.core.modules.roles.entities.repositories.impl.RolRepositoryImpl;
import com.hersac.core.modules.roles.services.RolesServices;
import com.hersac.core.modules.roles.services.impl.RolesServicesImpl;
import com.hersac.core.modules.rolespermisos.entities.repositories.RolPermisoRepository;
import com.hersac.core.modules.rolespermisos.entities.repositories.impl.RolPermisoRepositoryImpl;
import com.hersac.core.modules.rolespermisos.services.RolesPermisosService;
import com.hersac.core.modules.rolespermisos.services.impl.RolesPermisosServicesImpl;
import com.hersac.core.modules.usuarios.entities.repositories.UsuarioRepository;
import com.hersac.core.modules.usuarios.entities.repositories.impl.UsuarioRepositoryImpl;
import com.hersac.core.modules.usuarios.services.UsuariosServices;
import com.hersac.core.modules.usuarios.services.impl.UsuariosServicesImpl;
import com.hersac.ui.controllers.authentication.AuthenticationController;
import com.hersac.ui.controllers.departamentos.DepartamentosController;
import com.hersac.ui.controllers.permisos.PermisosController;
import com.hersac.ui.controllers.roles.RolesController;
import com.hersac.ui.controllers.rolesPermisos.RolesPermisosController;
import com.hersac.ui.controllers.usuarios.UsuariosController;
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

    public UsuariosController getUsuariosController() {
        UsuarioRepository usuarioRepository = new UsuarioRepositoryImpl(entityManager);
        UsuariosServices usuariosServices = new UsuariosServicesImpl(usuarioRepository);

        return new UsuariosController(usuariosServices);
    }

    public DepartamentosController getDepartamentosController() {
        DepartamentoRepository departamentoRepository = new DepartamentoRepositoryImpl(entityManager);
        DepartamentosServices departamentosServices = new DepartamentosServicesImpl(departamentoRepository);

        return new DepartamentosController(departamentosServices);
    }

    public RolesPermisosController getRolesPermisosController() {
        RolPermisoRepository rolPermisoRepository = new RolPermisoRepositoryImpl(entityManager);
        RolesPermisosService rolesPermisosService = new RolesPermisosServicesImpl(rolPermisoRepository);

        return new RolesPermisosController(rolesPermisosService);
    }

    public RolesController getRolesController() {
        RolRepository rolPermisoRepository = new RolRepositoryImpl(entityManager);
        RolesServices rolesPermisosService = new RolesServicesImpl(rolPermisoRepository);

        return new RolesController(rolesPermisosService);
    }

    public PermisosController getPermisosController() {
        PermisoRepository permisoRepository = new PermisoRepositoryImpl(entityManager);
        PermisosService permisosService = new PermisosServiceImpl(permisoRepository);

        return new PermisosController(permisosService);
    }

    public LoginView getLoginView() {
        AuthenticationController authController = getAuthenticationController();
        return new LoginView(authController);
    }
}
