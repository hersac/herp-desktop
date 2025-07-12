package com.hersac.ui.views.comercial.inventario.listeners;

import com.hersac.core.modules.items.entities.ItemEntity;
import com.hersac.core.modules.usuarios.entities.UsuarioEntity;

public interface ItemsListeners {
    void crearItems(ItemEntity item);
    void verItems(ItemEntity item);
    void actualizarItems(ItemEntity item);
    void eliminarItems(ItemEntity item);
}
