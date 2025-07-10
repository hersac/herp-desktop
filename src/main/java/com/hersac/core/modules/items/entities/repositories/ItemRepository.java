package com.hersac.core.modules.items.entities.repositories;

import com.hersac.core.globals.repositories.CrudRepository;
import com.hersac.core.modules.items.entities.ItemEntity;

public interface ItemRepository extends CrudRepository<ItemEntity, Long> {
    public ItemEntity buscarPorCodigo(String codigo);
}
