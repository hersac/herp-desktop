package com.hersac.core.modules.roles.services.impl;

import java.util.List;

import com.hersac.core.modules.roles.entities.RolEntity;
import com.hersac.core.modules.roles.entities.repositories.RolRepository;
import com.hersac.core.modules.roles.services.RolesServices;

public class RolesServicesImpl implements RolesServices {
    private final RolRepository rolRepository;

    public RolesServicesImpl(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    public List<RolEntity> buscarTodos() {
        return rolRepository.buscarTodos();
    }

    @Override
    public RolEntity buscarPorId(Long id) {
        return rolRepository.buscarPorId(id);
    }

    @Override
    public RolEntity crear(RolEntity entidad) {
        return rolRepository.crear(entidad);
    }

    @Override
    public void actualizar(Long id, RolEntity entidad) {
        RolEntity existingRol = rolRepository.buscarPorId(id);
        if (existingRol != null) {
            existingRol.setNombre(entidad.getNombre());
            existingRol.setDescripcion(entidad.getDescripcion());
            rolRepository.actualizar(existingRol);
        }
    }

    @Override
    public void eliminar(Long id) {
        rolRepository.eliminar(id);
    }
}
