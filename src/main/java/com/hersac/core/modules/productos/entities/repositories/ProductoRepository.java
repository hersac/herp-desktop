package com.hersac.core.modules.productos.entities.repositories;

import com.hersac.core.globals.repositories.CrudRepository;
import com.hersac.core.modules.productos.entities.ProductoEntity;

public interface ProductoRepository extends CrudRepository<ProductoEntity, Long> {
    public ProductoEntity buscarPorCodigo(String codigo);
}
