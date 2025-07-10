package com.hersac.ui.controllers.items;

import com.hersac.core.modules.items.entities.ItemEntity;
import com.hersac.core.modules.items.services.ItemsService;
import java.util.List;

public class ItemsController {
    private final ItemsService itemsService;

    public ItemsController(ItemsService itemsService) {
        this.itemsService = itemsService;
    }

    public List<ItemEntity> buscarTodos() {
        return itemsService.buscarTodos();
    }

    public ItemEntity buscarPorId(Long itemId) {
        return itemsService.buscarPorId(itemId);
    }

    public void crear(ItemEntity item) {
        itemsService.crear(item);
    }

    public void actualizar(Long itemId, ItemEntity item) {
        itemsService.actualizar(itemId, item);
    }

    public void eliminar(Long itemId) {
        itemsService.eliminar(itemId);
    }

    public ItemEntity buscarPorCodigo(String codigo) {
        return itemsService.buscarPorCodigo(codigo);
    }
}
