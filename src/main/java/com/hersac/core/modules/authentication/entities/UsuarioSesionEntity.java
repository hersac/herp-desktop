package com.hersac.core.modules.authentication.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioSesionEntity {
    private Long usuarioId;
    private String nombre;
    private String correo;
}
