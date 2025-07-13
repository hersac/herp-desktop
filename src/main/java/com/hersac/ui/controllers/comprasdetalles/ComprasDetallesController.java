package com.hersac.ui.controllers.comprasdetalles;

import com.hersac.core.modules.comprasdetalles.entities.CompraDetalleEntity;
import com.hersac.core.modules.comprasdetalles.services.ComprasDetallesService;

import java.util.List;

public class ComprasDetallesController {
    private ComprasDetallesService comprasDetallesService;

    public ComprasDetallesController(ComprasDetallesService comprasDetallesService) {
        this.comprasDetallesService = comprasDetallesService;
    }

    public List<CompraDetalleEntity> buscarTodos() {
        return comprasDetallesService.buscarTodos();
    }

    public CompraDetalleEntity buscarPorId(Long id) {
        return comprasDetallesService.buscarPorId(id);
    }

    public CompraDetalleEntity crear(CompraDetalleEntity comprasDetalles) {
        return comprasDetallesService.crear(comprasDetalles);
    }

    public void actualizar(Long id, CompraDetalleEntity comprasDetalles) {
        comprasDetallesService.actualizar(id, comprasDetalles);
    }

    public void eliminar(Long id) {
        comprasDetallesService.eliminar(id);
    }
}
