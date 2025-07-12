package com.hersac.ui.views.comercial.inventario.listeners;

import com.hersac.core.modules.proveedores.entities.ProveedorEntity;

public interface ProveedorListeners {
    void crearProveedor(ProveedorEntity proveedor);
    void verProveedor(ProveedorEntity proveedor);
    void actualizarProveedor(ProveedorEntity proveedor);
    void eliminarProveedor(ProveedorEntity proveedor);
}
