package com.hersac.ui.controllers.proveedores;

import com.hersac.core.modules.proveedores.entities.ProveedorEntity;
import com.hersac.core.modules.proveedores.services.ProveedoresService;

import java.util.List;

public class ProveedoresController {
    private final ProveedoresService proveedoresService;

    public ProveedoresController(ProveedoresService proveedoresService) {
        this.proveedoresService = proveedoresService;
    }

    public List<ProveedorEntity> buscarTodos() {
        return proveedoresService.buscarTodos();
    }

    public ProveedorEntity buscarPorId(Long proveedorId) {
        return proveedoresService.buscarPorId(proveedorId);
    }

    public void crear(ProveedorEntity proveedor) {
        proveedoresService.crear(proveedor);
    }

    public void actualizar(Long proveedorId, ProveedorEntity proveedor) {
        proveedoresService.actualizar(proveedorId, proveedor);
    }

    public void eliminar(Long proveedorId) {
        proveedoresService.eliminar(proveedorId);
    }

    public ProveedorEntity buscarPorCodigo(String codigo) {
        return proveedoresService.buscarPorCodigo(codigo);
    }
}
