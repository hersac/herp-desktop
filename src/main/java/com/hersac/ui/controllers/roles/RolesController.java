package com.hersac.ui.controllers.roles;

import com.hersac.core.modules.roles.entities.RolEntity;
import com.hersac.core.modules.roles.services.RolesServices;

import java.util.List;

public class RolesController {
    private final RolesServices rolesService;

    public RolesController(RolesServices rolesService) {
        this.rolesService = rolesService;
    }

    public List<RolEntity> buscarTodos() {
        return rolesService.buscarTodos();
    }

    public RolEntity buscarPorId(Long permisoId) {
        return rolesService.buscarPorId(permisoId);
    }

    public RolEntity crear(RolEntity permiso) {
        return rolesService.crear(permiso);
    }

    public void actualizar(Long permisoId, RolEntity permiso) {
        rolesService.actualizar(permisoId, permiso);
    }

    public void eliminar(Long permisoId) {
        rolesService.eliminar(permisoId);
    }
}
