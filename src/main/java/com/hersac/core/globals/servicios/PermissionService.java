package com.hersac.core.globals.servicios;

import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import com.hersac.core.modules.roles.entities.RolEntity;
import com.hersac.core.modules.rolespermisos.entities.RolPermisoEntity;
import com.hersac.core.modules.rolespermisos.services.RolesPermisosService;
import com.hersac.core.globals.store.UserSessionStore;
import java.util.List;

/**
 * Servicio para verificar permisos del usuario autenticado.
 */
public class PermissionService {
    private final RolesPermisosService rolesPermisosService;

    public PermissionService(RolesPermisosService rolesPermisosService) {
        this.rolesPermisosService = rolesPermisosService;
    }

    /**
     * Verifica si el usuario tiene un permiso específico por ID.
     * @param usuario Usuario a verificar
     * @param permisoId ID del permiso
     * @return true si tiene el permiso, false si no
     */
    public boolean tienePermiso(UsuarioEntity usuario, Long permisoId) {
        if (usuario == null || usuario.getRol() == null) {
            return false;
        }
        RolEntity rol = usuario.getRol();
        List<RolPermisoEntity> permisos = rolesPermisosService.buscarPorRolId(rol.getRolId());
        if (permisos == null) {
            return false;
        }
        return permisos.stream()
                .anyMatch(rp -> rp.getPermiso() != null && permisoId.equals(rp.getPermiso().getPermisoId()));
    }

    /**
     * Verifica si el usuario autenticado (en el store) tiene un permiso específico por ID.
     * @param permisoId ID del permiso
     * @return true si tiene el permiso, false si no
     */
    public boolean tienePermiso(Long permisoId) {
        UserSessionStore store = UserSessionStore.getInstance();
        return store.getPermisosUsuario().contains(permisoId);
    }
}
