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

    public RolEntity buscarPorId(Long rolId) {
        return rolesService.buscarPorId(rolId);
    }

    public RolEntity crear(RolEntity rol, List<Long> permisos) {
        return rolesService.crear(rol, permisos);
    }

    public void actualizar(Long rolId, RolEntity rol, List<Long> permisos) {
        rolesService.actualizar(rolId, rol, permisos);
    }

    public void eliminar(Long rolId) {
        rolesService.eliminar(rolId);
    }
}
