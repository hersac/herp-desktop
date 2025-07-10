package com.hersac.ui.controllers.productos;

import com.hersac.core.modules.productos.entities.ProductoEntity;
import com.hersac.core.modules.productos.services.ProductosService;

import java.util.List;

public class ProductosController {
    private final ProductosService productosService;

    public ProductosController(ProductosService productosService) {
        this.productosService = productosService;
    }

    public List<ProductoEntity> buscarTodos() {
        return productosService.buscarTodos();
    }

    public ProductoEntity buscarPorId(Long productoId) {
        return productosService.buscarPorId(productoId);
    }

    public void crear(ProductoEntity producto) {
        productosService.crear(producto);
    }

    public void actualizar(Long productoId, ProductoEntity producto) {
        productosService.actualizar(productoId, producto);
    }

    public void eliminar(Long productoId) {
        productosService.eliminar(productoId);
    }

    public ProductoEntity buscarPorCodigo(String codigo) {
        return productosService.buscarPorCodigo(codigo);
    }
}
