package com.hersac.core.modules.usuarios.services;

import com.hersac.core.modules.usuarios.entities.UsuarioEntity;

import java.util.List;

public interface UsuariosServices {
    public List<UsuarioEntity> buscarTodos();
    public UsuarioEntity buscarPorId(Long usuarioId);
    public UsuarioEntity crear(UsuarioEntity usuario);
    public void actualizar(Long usuarioId, UsuarioEntity usuario);
    public void eliminar(Long usuarioId);
    public UsuarioEntity buscarPorCorreo(String correo);
}
