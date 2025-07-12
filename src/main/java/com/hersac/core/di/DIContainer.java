package com.hersac.core.di;

import com.hersac.core.modules.authentication.servicios.AuthenticationServices;
import com.hersac.core.modules.authentication.servicios.impl.AuthenticationServiceImpl;
import com.hersac.core.modules.clientes.entities.repositories.ClienteRepository;
import com.hersac.core.modules.clientes.entities.repositories.impl.ClienteRepositoryImpl;
import com.hersac.core.modules.clientes.services.ClientesService;
import com.hersac.core.modules.clientes.services.impl.ClientesServiceImpl;
import com.hersac.core.modules.departamentos.entities.repositories.DepartamentoRepository;
import com.hersac.core.modules.departamentos.entities.repositories.impl.DepartamentoRepositoryImpl;
import com.hersac.core.modules.departamentos.services.DepartamentosServices;
import com.hersac.core.modules.departamentos.services.impl.DepartamentosServicesImpl;
import com.hersac.core.modules.items.entities.repositories.ItemRepository;
import com.hersac.core.modules.items.entities.repositories.impl.ItemRepositoryImpl;
import com.hersac.core.modules.items.services.ItemsService;
import com.hersac.core.modules.items.services.impl.ItemsServiceImpl;
import com.hersac.core.modules.permisos.entities.repositories.PermisoRepository;
import com.hersac.core.modules.permisos.entities.repositories.impl.PermisoRepositoryImpl;
import com.hersac.core.modules.permisos.services.PermisosService;
import com.hersac.core.modules.permisos.services.impl.PermisosServiceImpl;
import com.hersac.core.modules.productos.entities.repositories.ProductoRepository;
import com.hersac.core.modules.productos.entities.repositories.impl.ProductoRepositoryImpl;
import com.hersac.core.modules.productos.services.ProductosService;
import com.hersac.core.modules.productos.services.impl.ProductosServiceImpl;
import com.hersac.core.modules.proveedores.entities.repositories.ProveedorRepository;
import com.hersac.core.modules.proveedores.entities.repositories.impl.ProveedorRepositoryImpl;
import com.hersac.core.modules.proveedores.services.ProveedoresService;
import com.hersac.core.modules.proveedores.services.impl.ProveedoresServiceImpl;
import com.hersac.core.modules.roles.entities.repositories.RolRepository;
import com.hersac.core.modules.roles.entities.repositories.impl.RolRepositoryImpl;
import com.hersac.core.modules.roles.services.RolesServices;
import com.hersac.core.modules.roles.services.impl.RolesServicesImpl;
import com.hersac.core.modules.rolespermisos.entities.repositories.RolPermisoRepository;
import com.hersac.core.modules.rolespermisos.entities.repositories.impl.RolPermisoRepositoryImpl;
import com.hersac.core.modules.rolespermisos.services.RolesPermisosService;
import com.hersac.core.modules.rolespermisos.services.impl.RolesPermisosServicesImpl;
import com.hersac.core.modules.terceros.entities.repositories.TerceroRepository;
import com.hersac.core.modules.terceros.entities.repositories.impl.TerceroRepositoryImpl;
import com.hersac.core.modules.terceros.services.TercerosServices;
import com.hersac.core.modules.terceros.services.impl.TercerosServicesImpl;
import com.hersac.core.modules.usuarios.entities.repositories.UsuarioRepository;
import com.hersac.core.modules.usuarios.entities.repositories.impl.UsuarioRepositoryImpl;
import com.hersac.core.modules.usuarios.services.UsuariosServices;
import com.hersac.core.modules.usuarios.services.impl.UsuariosServicesImpl;
import com.hersac.ui.controllers.authentication.AuthenticationController;
import com.hersac.ui.controllers.clientes.ClientesController;
import com.hersac.ui.controllers.departamentos.DepartamentosController;
import com.hersac.ui.controllers.items.ItemsController;
import com.hersac.ui.controllers.permisos.PermisosController;
import com.hersac.ui.controllers.productos.ProductosController;
import com.hersac.ui.controllers.proveedores.ProveedoresController;
import com.hersac.ui.controllers.roles.RolesController;
import com.hersac.ui.controllers.rolesPermisos.RolesPermisosController;
import com.hersac.ui.controllers.terceros.TercerosController;
import com.hersac.ui.controllers.usuarios.UsuariosController;
import com.hersac.ui.views.authentication.login.LoginView;

import com.hersac.core.globals.servicios.PermissionService;
import jakarta.persistence.EntityManager;

public class DIContainer {
    private final EntityManager entityManager;

    public DIContainer(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public AuthenticationController getAuthenticationController() {
        UsuarioRepository usuarioRepository = new UsuarioRepositoryImpl(entityManager);
        UsuariosServices usuariosServices = new UsuariosServicesImpl(usuarioRepository);
        RolPermisoRepository rolPermisoRepository = new RolPermisoRepositoryImpl(entityManager);
        RolesPermisosService rolesPermisosService = new RolesPermisosServicesImpl(rolPermisoRepository);
        AuthenticationServices authenticationServices = new AuthenticationServiceImpl(usuariosServices, rolesPermisosService);

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
        PermisoRepository permisoRepository = new PermisoRepositoryImpl(entityManager);
        PermisosService permisosService = new PermisosServiceImpl(permisoRepository);
        RolPermisoRepository rolPermisoRepository = new RolPermisoRepositoryImpl(entityManager);
        RolesPermisosService rolesPermisosService = new RolesPermisosServicesImpl(rolPermisoRepository);
        RolRepository rolRepository = new RolRepositoryImpl(entityManager);
        RolesServices rolesServices = new RolesServicesImpl(rolRepository, rolesPermisosService, permisosService);

        return new RolesController(rolesServices);
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

    public PermissionService getPermissionService() {
        RolPermisoRepository rolPermisoRepository = new RolPermisoRepositoryImpl(entityManager);
        RolesPermisosService rolesPermisosService = new RolesPermisosServicesImpl(rolPermisoRepository);
        return new PermissionService(rolesPermisosService);
    }

    public TercerosController getTercerosController() {
        TerceroRepository terceroRepository = new TerceroRepositoryImpl(entityManager);
        TercerosServices tercerosServices = new TercerosServicesImpl(terceroRepository);
        return new TercerosController(tercerosServices);
    }

    public ClientesController getClientesController() {
        ClienteRepository clienteRepository = new ClienteRepositoryImpl(entityManager);
        ClientesService clientesService = new ClientesServiceImpl(clienteRepository);
        return new ClientesController(clientesService);
    }

    public ProveedoresController getProveedoresController() {
        ProveedorRepository proveedorRepository = new ProveedorRepositoryImpl(entityManager);
        ProveedoresService proveedoresService = new ProveedoresServiceImpl(proveedorRepository);
        return new ProveedoresController(proveedoresService);
    }

    public ItemsController getItemsController() {
        ItemRepository itemRepository = new ItemRepositoryImpl(entityManager);
        ItemsService itemsService = new ItemsServiceImpl(itemRepository);
        return new ItemsController(itemsService);
    }

    public ProductosController getProductosController() {
        ProductoRepository productoRepository = new ProductoRepositoryImpl(entityManager);
        ProductosService productosService = new ProductosServiceImpl(productoRepository);
        return new ProductosController(productosService);
    }
}
