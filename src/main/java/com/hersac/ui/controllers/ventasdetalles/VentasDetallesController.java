package com.hersac.ui.controllers.ventasdetalles;

import com.hersac.core.modules.ventasdetalles.entities.VentaDetalleEntity;
import com.hersac.core.modules.ventasdetalles.services.VentasDetallesService;

import java.util.List;

public class VentasDetallesController {
    private VentasDetallesService ventasDetallesService;

    public VentasDetallesController(VentasDetallesService ventasDetallesService) {
        this.ventasDetallesService = ventasDetallesService;
    }

    public List<VentaDetalleEntity> buscarTodos() {
        return ventasDetallesService.buscarTodos();
    }

    public VentaDetalleEntity buscarPorId(Long ventaDetalleId) {
        return ventasDetallesService.buscarPorId(ventaDetalleId);
    }

    public VentaDetalleEntity crear(VentaDetalleEntity ventaDetalle) {
        return ventasDetallesService.crear(ventaDetalle);
    }

    public void actualizar(Long ventaDetalleId, VentaDetalleEntity ventaDetalle) {
        ventasDetallesService.actualizar(ventaDetalleId, ventaDetalle);
    }

    public void eliminar(Long ventaDetalleId) {
        ventasDetallesService.eliminar(ventaDetalleId);
    }

}
