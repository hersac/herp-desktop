package com.hersac.core.modules.ventasdetalles.services;

import com.hersac.core.modules.ventasdetalles.entities.VentaDetalleEntity;

import java.util.List;

public interface VentasDetallesService {
    List<VentaDetalleEntity> buscarTodos();
    VentaDetalleEntity buscarPorId(Long ventaDetalleId);
    VentaDetalleEntity crear(VentaDetalleEntity ventaDetalle);
    void actualizar(Long ventaDetalleId, VentaDetalleEntity ventaDetalle);
    void eliminar(Long ventaDetalleId);
}
