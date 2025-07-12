package com.hersac.core.modules.items.services.impl;

import com.hersac.core.globals.store.UserSessionStore;
import com.hersac.core.modules.items.entities.ItemEntity;
import com.hersac.core.modules.items.entities.repositories.ItemRepository;
import com.hersac.core.modules.items.services.ItemsService;
import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import java.util.List;

public class ItemsServiceImpl implements ItemsService {
    private final ItemRepository itemRepository;
    private final UserSessionStore store = UserSessionStore.getInstance();

    public ItemsServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<ItemEntity> buscarTodos() {
        return itemRepository.buscarTodos();
    }

    @Override
    public ItemEntity buscarPorId(Long itemId) {
        return itemRepository.buscarPorId(itemId);
    }

    @Override
    public void crear(ItemEntity item) {
        UsuarioEntity usuarioActual = store.getUsuarioActual();
        item.setUsuarioCreacion(usuarioActual);
        item.setUsuarioActualizacion(usuarioActual);
        itemRepository.crear(item);
    }

    @Override
    public void actualizar(Long itemId, ItemEntity item) {
        UsuarioEntity usuarioActual = store.getUsuarioActual();
        ItemEntity itemExistente = itemRepository.buscarPorId(itemId);
        itemExistente.setCodigo(item.getCodigo());
        itemExistente.setNombre(item.getNombre());
        itemExistente.setDescripcion(item.getDescripcion());
        itemExistente.setCategoria(item.getCategoria());
        itemExistente.setPrecioUnitario(item.getPrecioUnitario());
        itemExistente.setStock(item.getStock());
        itemExistente.setEstaActivo(item.isEstaActivo());
        itemExistente.setProducto(item.getProducto());
        itemExistente.setUsuarioActualizacion(usuarioActual);
        itemRepository.actualizar(itemExistente);
    }

    @Override
    public void eliminar(Long itemId) {
        itemRepository.eliminar(itemId);
    }

    @Override
    public ItemEntity buscarPorCodigo(String codigo) {
        return itemRepository.buscarPorCodigo(codigo);
    }
}
