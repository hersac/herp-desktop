package com.hersac.core.modules.roles.services.impl;

import java.util.ArrayList;
import java.util.List;

import com.hersac.core.modules.permisos.entities.PermisoEntity;
import com.hersac.core.modules.permisos.services.PermisosService;
import com.hersac.core.modules.roles.entities.RolEntity;
import com.hersac.core.modules.roles.entities.repositories.RolRepository;
import com.hersac.core.modules.roles.services.RolesServices;
import com.hersac.core.modules.rolespermisos.entities.RolPermisoEntity;
import com.hersac.core.modules.rolespermisos.services.RolesPermisosService;

public class RolesServicesImpl implements RolesServices {
    private final RolRepository rolRepository;
    private final PermisosService permisosService;
    private final RolesPermisosService rolesPermisosService;

    public RolesServicesImpl(RolRepository rolRepository, RolesPermisosService rolesPermisosService, PermisosService permisosService) {
        this.rolRepository = rolRepository;
        this.rolesPermisosService = rolesPermisosService;
        this.permisosService = permisosService;
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
    public RolEntity crear(RolEntity entidad, List<Long> permisos) {

        System.out.println("ENTIDAD:" + entidad);
        System.out.println("PERMISOS:" + permisos);

        Long usuarioId = 1L;
        entidad.setUsuarioCreacionId(usuarioId);
        entidad.setUsuarioActualizacionId(usuarioId);
        RolEntity nuevoRol = rolRepository.crear(entidad);

        System.out.println("NUEVO ROL:" + nuevoRol);

        List<RolPermisoEntity> rolesPermisos = new ArrayList<>();
        for (Long permisoId : permisos) {
            PermisoEntity permiso = permisosService.buscarPorId(permisoId);
            RolPermisoEntity rolPermiso = new RolPermisoEntity();
            rolPermiso.setPermiso(permiso);
            rolPermiso.setRol(nuevoRol);
            rolesPermisos.add(rolPermiso);
        }

        System.out.println("ROLES PERMISOS:" + rolesPermisos);

        rolesPermisosService.crearMasivo(rolesPermisos);
        return nuevoRol;
    }

    @Override
    public void actualizar(Long id, RolEntity entidad, List<Long> permisos) {
        System.out.println("ID:" + id);
        System.out.println("ENTIDAD:" + entidad);
        System.out.println("PERMISOS:" + permisos);

        RolEntity existingRol = rolRepository.buscarPorId(id);
        if (existingRol != null) {
            existingRol.setNombre(entidad.getNombre());
            existingRol.setDescripcion(entidad.getDescripcion());
            existingRol.setUsuarioActualizacionId(entidad.getUsuarioActualizacionId());
            existingRol.setFechaActualizacion(entidad.getFechaActualizacion());
            rolRepository.actualizar(existingRol);

            // Eliminar permisos actuales del rol
            rolesPermisosService.eliminarPorRolId(id);

            // Asociar nuevos permisos
            List<RolPermisoEntity> rolesPermisos = new ArrayList<>();
            for (Long permisoId : permisos) {
                PermisoEntity permiso = permisosService.buscarPorId(permisoId);
                RolPermisoEntity rolPermiso = new RolPermisoEntity();
                rolPermiso.setPermiso(permiso);
                rolPermiso.setRol(existingRol);
                rolesPermisos.add(rolPermiso);
            }
            rolesPermisosService.crearMasivo(rolesPermisos);
        }
    }

    @Override
    public void eliminar(Long id) {
        rolRepository.eliminar(id);
    }
}
