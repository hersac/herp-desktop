package com.hersac.core.modules.clientes.entities.repositories;

import com.hersac.core.globals.repositories.CrudRepository;
import com.hersac.core.modules.clientes.entities.ClienteEntity;

public interface ClienteRepository extends CrudRepository<ClienteEntity, Long> {
    ClienteEntity buscarPorTerceroId(String terceroId);
}
