package com.hersac.core.modules.proveedores.services;

import com.hersac.core.modules.proveedores.entities.ProveedorEntity;

import java.util.List;

public interface ProveedoresService {
    List<ProveedorEntity> buscarTodos();
    ProveedorEntity buscarPorId(Long proveedorId);
    void crear(ProveedorEntity proveedor);
    void actualizar(Long proveedorId, ProveedorEntity proveedor);
    void eliminar(Long proveedorId);
    ProveedorEntity buscarPorCodigo(String codigo);
    ProveedorEntity buscarPorTerceroId(String terceroId);
}
