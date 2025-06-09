package com.hersac.core.globals.exceptions;

public class UsuarioBloqueadoException extends GlobalException {
    public UsuarioBloqueadoException(String correo) {
        super("Usuario con correo " + correo + " se encuentra bloqueado");
    }
}
