package com.hersac.core.modules.productos.services.impl;

import com.hersac.core.globals.store.UserSessionStore;
import com.hersac.core.modules.productos.entities.ProductoEntity;
import com.hersac.core.modules.productos.entities.repositories.ProductoRepository;
import com.hersac.core.modules.productos.services.ProductosService;
import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import java.util.List;

public class ProductosServiceImpl implements ProductosService {
    private final ProductoRepository productoRepository;
    private final UserSessionStore store = UserSessionStore.getInstance();

    public ProductosServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<ProductoEntity> buscarTodos() {
        return productoRepository.buscarTodos();
    }

    @Override
    public ProductoEntity buscarPorId(Long productoId) {
        return productoRepository.buscarPorId(productoId);
    }

    @Override
    public void crear(ProductoEntity producto) {
        UsuarioEntity usuarioActual = store.getUsuarioActual();
        producto.setUsuarioCreacion(usuarioActual);
        producto.setUsuarioActualizacion(usuarioActual);
        productoRepository.crear(producto);
    }

    @Override
    public void actualizar(Long productoId, ProductoEntity producto) {
        UsuarioEntity usuarioActual = store.getUsuarioActual();
        ProductoEntity productoExistente = productoRepository.buscarPorId(productoId);
        productoExistente.setCodigo(producto.getCodigo());
        productoExistente.setNombre(producto.getNombre());
        productoExistente.setDescripcion(producto.getDescripcion());
        productoExistente.setUnidadMedida(producto.getUnidadMedida());
        productoExistente.setPrecioBase(producto.getPrecioBase());
        productoExistente.setEstadoActivo(producto.isEstadoActivo());
        productoExistente.setUsuarioActualizacion(usuarioActual);
        productoRepository.actualizar(productoExistente);
    }

    @Override
    public void eliminar(Long productoId) {
        productoRepository.eliminar(productoId);
    }

    @Override
    public ProductoEntity buscarPorCodigo(String codigo) {
        return productoRepository.buscarPorCodigo(codigo);
    }
}
