package com.hersac.core.modules.rolespermisos.services.impl;

import java.util.List;

import com.hersac.core.modules.rolespermisos.entities.RolPermisoEntity;
import com.hersac.core.modules.rolespermisos.entities.repositories.RolPermisoRepository;
import com.hersac.core.modules.rolespermisos.services.RolesPermisosService;

public class RolesPermisosServicesImpl implements RolesPermisosService {
    private final RolPermisoRepository rolPermisoRepository;

    public RolesPermisosServicesImpl(RolPermisoRepository rolPermisoRepository) {
        this.rolPermisoRepository = rolPermisoRepository;
    }

    @Override
    public List<RolPermisoEntity> buscarTodos() {
        return rolPermisoRepository.buscarTodos();
    }

    @Override
    public RolPermisoEntity buscarPorId(Long id) {
        return rolPermisoRepository.buscarPorId(id);
    }

    @Override
    public List<RolPermisoEntity> buscarPorRolId(Long rolId) {
        return rolPermisoRepository.buscarPorRolId(rolId);
    }

    @Override
    public RolPermisoEntity crear(RolPermisoEntity entidad) {
        return rolPermisoRepository.crear(entidad);
    }

    @Override
    public List<RolPermisoEntity> crearMasivo(List<RolPermisoEntity> entidades) {
        return rolPermisoRepository.crearMasivo(entidades);
    }


    @Override
    public void actualizar(Long id, RolPermisoEntity entidad) {
        RolPermisoEntity existingRolPermiso = rolPermisoRepository.buscarPorId(id);
        if (existingRolPermiso != null) {
            existingRolPermiso.setRol(entidad.getRol());
            existingRolPermiso.setPermiso(entidad.getPermiso());
            rolPermisoRepository.actualizar(existingRolPermiso);
        }
    }

    @Override
    public void eliminar(Long id) {
        rolPermisoRepository.eliminar(id);
    }

    @Override
    public void eliminarPorRolId(Long rolId) {
        List<RolPermisoEntity> rolPermisos = rolPermisoRepository.buscarPorRolId(rolId);

    }

}
