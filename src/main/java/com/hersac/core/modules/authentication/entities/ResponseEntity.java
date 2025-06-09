package com.hersac.core.modules.authentication.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
@AllArgsConstructor
public class ResponseEntity {
    private String token;
    private UsuarioSesionEntity usuarioSession;
}
