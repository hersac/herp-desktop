package com.hersac.ui.controllers.ventas;

import com.hersac.core.modules.ventas.entities.VentaEntity;
import com.hersac.core.modules.ventas.services.impl.VentasService;

import java.util.List;

public class VentasController {
    private VentasService ventasService;

    public VentasController(VentasService ventasService) {
        this.ventasService = ventasService;
    }

    public List<VentaEntity> buscarTodos() {
        return ventasService.buscarTodos();
    }

    public VentaEntity buscarPorId(Long ventaId) {
        return ventasService.buscarPorId(ventaId);
    }

    public VentaEntity crear(VentaEntity venta) {
        return ventasService.crear(venta);
    }

    public void actualizar(Long ventaId, VentaEntity venta) {
        ventasService.actualizar(ventaId, venta);
    }

    public void eliminar(Long ventaId) {
        ventasService.eliminar(ventaId);
    }
}
