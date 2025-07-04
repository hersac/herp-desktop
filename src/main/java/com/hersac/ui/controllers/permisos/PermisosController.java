package com.hersac.ui.controllers.permisos;

import java.util.List;

import com.hersac.core.modules.permisos.entities.PermisoEntity;
import com.hersac.core.modules.permisos.services.PermisosService;

public class PermisosController {
    private final PermisosService permisosService;

    public PermisosController(PermisosService permisosService) {
        this.permisosService = permisosService;
    }

    public List<PermisoEntity> buscarTodos() {
        return permisosService.buscarTodos();
    }

    public PermisoEntity buscarPorId(Long permisoId) {
        return permisosService.buscarPorId(permisoId);
    }

    public PermisoEntity crear(PermisoEntity permiso) {
        return permisosService.crear(permiso);
    }

    public void actualizar(Long permisoId, PermisoEntity permiso) {
        permisosService.actualizar(permisoId, permiso);
    }

    public void eliminar(Long permisoId) {
        permisosService.eliminar(permisoId);
    }
}
