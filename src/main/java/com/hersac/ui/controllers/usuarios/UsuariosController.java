package com.hersac.ui.controllers.usuarios;

import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import com.hersac.core.modules.usuarios.services.UsuariosServices;

import java.util.List;

public class UsuariosController {
    private final UsuariosServices usuariosServices;

    public UsuariosController(UsuariosServices usuariosServices) {
        this.usuariosServices = usuariosServices;
    }

    public List<UsuarioEntity> buscarTodos() {
        return usuariosServices.buscarTodos();
    }

    public UsuarioEntity buscarPorId(Long usuarioId) {
        return usuariosServices.buscarPorId(usuarioId);
    }

    public UsuarioEntity crear(UsuarioEntity usuario) {
        return usuariosServices.crear(usuario);
    }

    public void actualizar(Long usuarioId, UsuarioEntity usuario) {
        usuariosServices.actualizar(usuarioId, usuario);
    }

    public void eliminar(Long usuarioId) {
        usuariosServices.eliminar(usuarioId);
    }
}
