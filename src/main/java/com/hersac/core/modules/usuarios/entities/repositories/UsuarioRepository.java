package com.hersac.core.modules.usuarios.entities.repositories;

import com.hersac.core.globals.repositories.CrudRepository;
import com.hersac.core.modules.usuarios.entities.UsuarioEntity;

public interface UsuarioRepository extends CrudRepository<UsuarioEntity, Long> {
    UsuarioEntity buscarPorCorreo(String correo);
}
