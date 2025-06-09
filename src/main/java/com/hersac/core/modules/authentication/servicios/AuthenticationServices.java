package com.hersac.core.modules.authentication.servicios;

import com.hersac.core.modules.authentication.entities.RequestEntity;
import com.hersac.core.modules.authentication.entities.ResponseEntity;

public interface AuthenticationServices {
    public ResponseEntity login(RequestEntity request);
}
