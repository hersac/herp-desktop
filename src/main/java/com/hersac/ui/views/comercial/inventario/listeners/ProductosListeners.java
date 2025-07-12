package com.hersac.ui.views.comercial.inventario.listeners;

import com.hersac.core.modules.productos.entities.ProductoEntity;

public interface ProductosListeners {
    void crearProducto(ProductoEntity producto); // Método para crear un nuevo producto
    void verProducto(ProductoEntity producto); // Método para ver los detalles de un producto
    void actualizarProducto(ProductoEntity producto); // Método para actualizar un producto existente
    void eliminarProducto(ProductoEntity producto); // Método para eliminar un producto
}
