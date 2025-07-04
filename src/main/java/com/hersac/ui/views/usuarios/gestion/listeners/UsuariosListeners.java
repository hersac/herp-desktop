package com.hersac.ui.views.usuarios.gestion.listeners;

import com.hersac.core.modules.usuarios.entities.UsuarioEntity;

public interface UsuariosListeners {
    void crearUsuario(UsuarioEntity usuario);

    void verUsuario(UsuarioEntity usuario);

    void actualizarUsuario(UsuarioEntity usuario);

    void eliminarUsuario(UsuarioEntity usuario);
}
