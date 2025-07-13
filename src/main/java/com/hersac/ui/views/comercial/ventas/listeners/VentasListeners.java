package com.hersac.ui.views.comercial.ventas.listeners;

import com.hersac.core.modules.clientes.entities.ClienteEntity;
import com.hersac.core.modules.items.entities.ItemEntity;

public interface VentasListeners {
    void onClienteSeleccionado(String clienteId);
    void onItemSeleccionado(String itemCodigo);
    void onAgregarItem(String codigo, String nombre, int cantidad, String precioUnitario);
}
