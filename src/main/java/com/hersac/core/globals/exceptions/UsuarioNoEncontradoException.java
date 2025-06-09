package com.hersac.core.globals.exceptions;

public class UsuarioNoEncontradoException extends GlobalException {
    public UsuarioNoEncontradoException(Long id) {
        super("Usuario con ID " + id + " no fue encontrado.");
    }

    public UsuarioNoEncontradoException(String correo) {
        super("Usuario con correo '" + correo + "' no fue encontrado.");
    }
}
