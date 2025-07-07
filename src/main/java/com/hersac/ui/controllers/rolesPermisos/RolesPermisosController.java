package com.hersac.ui.controllers.rolesPermisos;

import java.util.List;

import com.hersac.core.modules.rolespermisos.entities.RolPermisoEntity;
import com.hersac.core.modules.rolespermisos.services.RolesPermisosService;

public class RolesPermisosController {
    private final RolesPermisosService rolesPermisosService;

    public RolesPermisosController(RolesPermisosService rolesPermisosService) {
        this.rolesPermisosService = rolesPermisosService;
    }

    public List<RolPermisoEntity> buscarTodos() {
        return rolesPermisosService.buscarTodos();
    }

    public RolPermisoEntity obtenerPorId(Long id) {
        return rolesPermisosService.buscarPorId(id);
    }

    public List<RolPermisoEntity> buscarPorRolId(Long rolId) {
        return rolesPermisosService.buscarPorRolId(rolId);
    }

    public RolPermisoEntity crear(RolPermisoEntity entidad) {
        return rolesPermisosService.crear(entidad);
    }

    public List<RolPermisoEntity> crearMasivo(List<RolPermisoEntity> entidades) {
        return rolesPermisosService.crearMasivo(entidades);
    }

    public void actualizar(Long id, RolPermisoEntity entidad) {
        rolesPermisosService.actualizar(id, entidad);
    }

    public void eliminar(Long id) {
        rolesPermisosService.eliminar(id);
    }
}
