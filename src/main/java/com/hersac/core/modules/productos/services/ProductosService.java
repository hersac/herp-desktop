package com.hersac.core.modules.productos.services;

import com.hersac.core.modules.productos.entities.ProductoEntity;
import java.util.List;

public interface ProductosService {
    List<ProductoEntity> buscarTodos();
    ProductoEntity buscarPorId(Long productoId);
    void crear(ProductoEntity producto);
    void actualizar(Long productoId, ProductoEntity producto);
    void eliminar(Long productoId);
    ProductoEntity buscarPorCodigo(String codigo);
}
