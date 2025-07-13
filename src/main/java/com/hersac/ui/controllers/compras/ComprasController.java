package com.hersac.ui.controllers.compras;

import com.hersac.core.modules.compras.entities.CompraEntity;
import com.hersac.core.modules.compras.services.ComprasService;

import java.util.List;

public class ComprasController {
    private final ComprasService comprasService;

    public ComprasController(ComprasService comprasService) {
        this.comprasService = comprasService;
    }

    public List<CompraEntity> buscarTodos() {
        return comprasService.buscarTodos();
    }

    public CompraEntity buscarPorId(Long id) {
        return comprasService.buscarPorId(id);
    }

    public CompraEntity crear(CompraEntity compra) {
        return comprasService.crear(compra);
    }

    public void actualizar(Long id, CompraEntity compra) {
        comprasService.actualizar(id, compra);
    }

    public void eliminar(Long id) {
        comprasService.eliminar(id);
    }
}
