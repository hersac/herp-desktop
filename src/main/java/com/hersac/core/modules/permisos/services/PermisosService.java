package com.hersac.core.modules.permisos.services;

import java.util.List;

import com.hersac.core.modules.permisos.entities.PermisoEntity;

public interface PermisosService {
    public List<PermisoEntity> buscarTodos();

    public PermisoEntity buscarPorId(Long permisoId);

    public PermisoEntity crear(PermisoEntity permiso);

    public void actualizar(Long permisoId, PermisoEntity permiso);

    public void eliminar(Long permisoId);

}
