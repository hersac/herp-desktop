package com.hersac.core.modules.rolespermisos.services;

import java.util.List;

import com.hersac.core.modules.rolespermisos.entities.RolPermisoEntity;

public interface RolesPermisosService {
    public List<RolPermisoEntity> buscarTodos();

    public RolPermisoEntity buscarPorId(Long id);

    public RolPermisoEntity crear(RolPermisoEntity entidad);

    public void actualizar(Long id, RolPermisoEntity entidad);

    public void eliminar(Long id);
}
