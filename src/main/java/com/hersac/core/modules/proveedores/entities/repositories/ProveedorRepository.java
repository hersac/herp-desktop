package com.hersac.core.modules.proveedores.entities.repositories;

import com.hersac.core.globals.repositories.CrudRepository;
import com.hersac.core.modules.proveedores.entities.ProveedorEntity;
import java.util.List;

public interface ProveedorRepository extends CrudRepository<ProveedorEntity, Long> {
    ProveedorEntity buscarPorCodigo(String codigo);
    ProveedorEntity buscarPorTerceroId(String terceroId);
}

