package com.hersac.core.modules.rolespermisos.services;

import java.util.List;

import com.hersac.core.modules.rolespermisos.entities.RolPermisoEntity;

public interface RolesPermisosService {
    public List<RolPermisoEntity> buscarTodos();

    public RolPermisoEntity buscarPorId(Long id);

    public List<RolPermisoEntity> buscarPorRolId(Long rolId);

    public RolPermisoEntity crear(RolPermisoEntity entidad);

    public List<RolPermisoEntity> crearMasivo(List<RolPermisoEntity> entidades);

    public void actualizar(Long id, RolPermisoEntity entidad);

    public void eliminar(Long id);

    public void eliminarPorRolId(Long rolId);
}
