package com.hersac.core.modules.authentication.servicios.impl;

import com.hersac.core.globals.exceptions.NoAutenticadoException;
import com.hersac.core.globals.exceptions.UsuarioBloqueadoException;
import com.hersac.core.globals.exceptions.UsuarioNoEncontradoException;
import com.hersac.core.modules.authentication.entities.RequestEntity;
import com.hersac.core.modules.authentication.entities.ResponseEntity;
import com.hersac.core.modules.authentication.entities.UsuarioSesionEntity;
import com.hersac.core.modules.authentication.servicios.AuthenticationServices;
import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import com.hersac.core.modules.usuarios.services.UsuariosServices;
import com.hersac.core.globals.store.UserSessionStore;
import com.hersac.core.modules.rolespermisos.entities.RolPermisoEntity;
import com.hersac.core.modules.rolespermisos.services.RolesPermisosService;
import java.util.Set;
import java.util.stream.Collectors;

import java.util.UUID;

public class AuthenticationServiceImpl implements AuthenticationServices {
    private final UsuariosServices usuariosServices;
    private final RolesPermisosService rolesPermisosService;

    public AuthenticationServiceImpl(UsuariosServices usuariosServices, RolesPermisosService rolesPermisosService) {
        this.usuariosServices = usuariosServices;
        this.rolesPermisosService = rolesPermisosService;
    }

    public ResponseEntity login(RequestEntity request) {
        UsuarioEntity usuario = usuariosServices.buscarPorCorreo(request.getCorreo());

        validarUsuario(usuario, request);

        // Guardar usuario y permisos en el store
        UserSessionStore store = UserSessionStore.getInstance();
        store.setUsuarioActual(usuario);
        Set<Long> permisos = rolesPermisosService.buscarPorRolId(usuario.getRol().getRolId())
                .stream()
                .filter(rp -> rp.getPermiso() != null)
                .map(rp -> rp.getPermiso().getPermisoId())
                .collect(Collectors.toSet());
        store.setPermisosUsuario(permisos);

        UsuarioSesionEntity usuarioSesion = new UsuarioSesionEntity();
        usuarioSesion.setUsuarioId(usuario.getUsuarioId());
        usuarioSesion.setNombre(usuario.getNombre());
        usuarioSesion.setCorreo(usuario.getCorreo());

        ResponseEntity response = new ResponseEntity();
        response.setUsuarioSession(usuarioSesion);
        response.setToken(UUID.randomUUID().toString());

        return response;
    }

    private void validarUsuario(UsuarioEntity usuario, RequestEntity request) {
        if (usuario == null) {
            throw new UsuarioNoEncontradoException(request.getCorreo());
        }

        if (!usuario.getEstaActivo()) {
            throw new UsuarioBloqueadoException(request.getCorreo());
        }

        if (!usuario.getContrasena().equals(request.getContrasena())) {
            throw new NoAutenticadoException();
        }
    }
}
