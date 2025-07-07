package com.hersac.core.modules.rolespermisos.entities.repositories;

import com.hersac.core.globals.repositories.CrudRepository;
import com.hersac.core.modules.rolespermisos.entities.RolPermisoEntity;
import java.util.List;

public interface RolPermisoRepository extends CrudRepository<RolPermisoEntity, Long> {
    List<RolPermisoEntity> crearMasivo(List<RolPermisoEntity> entidades);
    List<RolPermisoEntity> buscarPorRolId(Long rolId);
    void eliminarPorRolId(Long rolId);
}
