package com.hersac.ui.controllers.authentication;

import com.hersac.core.modules.authentication.entities.RequestEntity;
import com.hersac.core.modules.authentication.entities.ResponseEntity;
import com.hersac.core.modules.authentication.servicios.AuthenticationServices;

public class AuthenticationController {
    private final AuthenticationServices authenticationServices;

    public AuthenticationController(AuthenticationServices authenticationServices) {
        this.authenticationServices = authenticationServices;
    }

    public ResponseEntity login(RequestEntity request) {
        return authenticationServices.login(request);
    }
}