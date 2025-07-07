package com.hersac.ui.views.usuarios.rolesPermisos.listeners;

import com.hersac.core.modules.roles.entities.RolEntity;

import java.util.List;

public interface RolesPermisosListener {
    void crearRol(RolEntity rol, List<Long> permisos);
    void editarRol(RolEntity rol, List<Long> permisos);
    void eliminarRol(RolEntity rol);
    void mostrarModalEditarRol(RolEntity rol);
}
