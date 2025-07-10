package com.hersac.core.modules.items.services;

import com.hersac.core.modules.items.entities.ItemEntity;
import java.util.List;

public interface ItemsService {
    List<ItemEntity> buscarTodos();
    ItemEntity buscarPorId(Long itemId);
    void crear(ItemEntity item);
    void actualizar(Long itemId, ItemEntity item);
    void eliminar(Long itemId);
    ItemEntity buscarPorCodigo(String codigo);
}
