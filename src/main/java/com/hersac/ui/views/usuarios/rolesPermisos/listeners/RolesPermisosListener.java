package com.hersac.ui.views.usuarios.rolesPermisos.listeners;

import com.hersac.core.modules.roles.entities.RolEntity;

public interface RolesPermisosListener {
    void crearRol(RolEntity rol);
    void editarRol(RolEntity rol);
    void eliminarRol(RolEntity rol);
    void mostrarModalEditarRol(RolEntity rol);
}
