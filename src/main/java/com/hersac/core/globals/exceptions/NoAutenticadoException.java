package com.hersac.core.globals.exceptions;

public class NoAutenticadoException extends GlobalException {
    public NoAutenticadoException() {
        super("El usuario o contraseña son incorrectos");
    }
}
