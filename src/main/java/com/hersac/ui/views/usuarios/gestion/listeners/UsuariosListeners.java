package com.hersac.ui.views.usuarios.gestion.listeners;

import com.hersac.core.modules.usuarios.entities.UsuarioEntity;

public interface UsuariosListeners {
    void onVerUsuario(UsuarioEntity usuario);
    void onToggleEstado(UsuarioEntity usuario);
    void onEliminarUsuario(UsuarioEntity usuario);
}
