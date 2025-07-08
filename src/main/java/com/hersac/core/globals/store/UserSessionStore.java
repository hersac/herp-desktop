package com.hersac.core.globals.store;

import com.hersac.core.modules.usuarios.entities.UsuarioEntity;

import java.util.HashSet;
import java.util.Set;

/**
 * Store singleton para mantener la sesión del usuario autenticado.
 */
public class UserSessionStore {
    private static UserSessionStore instance;
    private UsuarioEntity usuarioActual;
    private Set<Long> permisosUsuario = new HashSet<>();

    private UserSessionStore() {}

    public static synchronized UserSessionStore getInstance() {
        if (instance == null) {
            instance = new UserSessionStore();
        }
        return instance;
    }

    public void setUsuarioActual(UsuarioEntity usuario) {
        this.usuarioActual = usuario;
    }

    public UsuarioEntity getUsuarioActual() {
        return usuarioActual;
    }

    public void setPermisosUsuario(Set<Long> permisos) {
        this.permisosUsuario = permisos;
    }

    public Set<Long> getPermisosUsuario() {
        return permisosUsuario;
    }

    public void limpiarSesion() {
        this.usuarioActual = null;
        this.permisosUsuario.clear();
    }
}
