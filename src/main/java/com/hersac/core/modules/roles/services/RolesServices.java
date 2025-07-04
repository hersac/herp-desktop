package com.hersac.core.modules.roles.services;

import java.util.List;

import com.hersac.core.modules.roles.entities.RolEntity;

public interface RolesServices {
    public List<RolEntity> buscarTodos();

    public RolEntity buscarPorId(Long id);

    public RolEntity crear(RolEntity entidad);

    public void actualizar(Long id, RolEntity entidad);

    public void eliminar(Long id);

}
