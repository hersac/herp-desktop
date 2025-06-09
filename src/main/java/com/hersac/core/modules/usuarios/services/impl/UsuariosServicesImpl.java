package com.hersac.core.modules.usuarios.services.impl;

import com.hersac.core.globals.exceptions.UsuarioNoEncontradoException;
import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import com.hersac.core.modules.usuarios.entities.repositories.UsuarioRepository;
import com.hersac.core.modules.usuarios.services.UsuariosServices;

import java.util.List;

public class UsuariosServicesImpl implements UsuariosServices {

    private final UsuarioRepository usuarioRepository;

    public UsuariosServicesImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<UsuarioEntity> buscarTodos() {
        return usuarioRepository.buscarTodos();
    }

    @Override
    public UsuarioEntity buscarPorId(Long usuarioId) {

        UsuarioEntity usuario = usuarioRepository.buscarPorId(usuarioId);

        if (usuario == null) {
            throw new UsuarioNoEncontradoException(usuarioId);
        }

        return usuario;
    }

    @Override
    public UsuarioEntity crear(UsuarioEntity usuario) {
        return usuarioRepository.crear(usuario);
    }

    @Override
    public void actualizar(Long usuarioId, UsuarioEntity usuario) {
        UsuarioEntity usuarioExistente = usuarioRepository.buscarPorId(usuarioId);

        if (usuarioExistente == null) {
            throw new UsuarioNoEncontradoException(usuarioId);
        }

        usuarioExistente.setNombre(usuario.getNombre());
        usuarioExistente.setCorreo(usuario.getCorreo());
        usuarioExistente.setContrasena(usuario.getContrasena());
        usuarioExistente.setEstaActivo(usuario.getEstaActivo());

        usuarioRepository.actualizar(usuarioExistente);
    }

    @Override
    public void eliminar(Long usuarioId) {
        UsuarioEntity usuarioExistente = usuarioRepository.buscarPorId(usuarioId);

        if (usuarioExistente == null) {
            throw new UsuarioNoEncontradoException(usuarioId);
        }

        usuarioRepository.eliminar(usuarioId);
    }

    @Override
    public UsuarioEntity buscarPorCorreo(String correo) {
        UsuarioEntity usuarioExistente = usuarioRepository.buscarPorCorreo(correo);
        if (usuarioExistente == null) {
            throw new UsuarioNoEncontradoException(correo);
        }

        return usuarioExistente;
    }
}
