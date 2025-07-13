package com.hersac.ui.views.comercial.compras.listeners;

import com.hersac.core.modules.proveedores.entities.ProveedorEntity;
import com.hersac.core.modules.items.entities.ItemEntity;

public interface ComprasListerns {
    void onProveedorSeleccionado(String proveedorId);
    void onItemSeleccionado(String itemCodigo);
    void onAgregarItem(String codigo, String nombre, int cantidad, String precioUnitario);
}
