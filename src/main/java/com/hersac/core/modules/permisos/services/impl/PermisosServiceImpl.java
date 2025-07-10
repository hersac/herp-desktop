package com.hersac.core.modules.permisos.services.impl;

import java.util.List;

import com.hersac.core.modules.permisos.entities.PermisoEntity;
import com.hersac.core.modules.permisos.entities.repositories.PermisoRepository;
import com.hersac.core.modules.permisos.services.PermisosService;

public class PermisosServiceImpl implements PermisosService {

    private final PermisoRepository permisoRepository;

    public PermisosServiceImpl(PermisoRepository permisoRepository) {
        this.permisoRepository = permisoRepository;
    }

    @Override
    public List<PermisoEntity> buscarTodos() {
        return permisoRepository.buscarTodos();
    }

    @Override
    public PermisoEntity buscarPorId(Long permisoId) {
        PermisoEntity permiso = permisoRepository.buscarPorId(permisoId);
        if (permiso == null) {
            throw new IllegalArgumentException("Permiso no encontrado con ID: " + permisoId);
        }
        return permiso;
    }

    @Override
    public PermisoEntity crear(PermisoEntity permiso) {
        return permisoRepository.crear(permiso);
    }

    @Override
    public void actualizar(Long permisoId, PermisoEntity permiso) {
        PermisoEntity permisoExistente = permisoRepository.buscarPorId(permisoId);
        if (permisoExistente == null) {
            throw new IllegalArgumentException("Permiso no encontrado con ID: " + permisoId);
        }
        permisoExistente.setNombre(permiso.getNombre());
        permisoExistente.setDescripcion(permiso.getDescripcion());
        permisoExistente.setEstaActivo(permiso.getEstaActivo());
        permisoExistente.setUsuarioActualizacionId(permiso.getUsuarioActualizacionId());
        permisoExistente.setFechaActualizacion(permiso.getFechaActualizacion());
        permisoRepository.actualizar(permisoExistente);
    }

    @Override
    public void eliminar(Long permisoId) {
        PermisoEntity permisoExistente = permisoRepository.buscarPorId(permisoId);
        if (permisoExistente == null) {
            throw new IllegalArgumentException("Permiso no encontrado con ID: " + permisoId);
        }
        permisoRepository.eliminar(permisoId);
    }

}
