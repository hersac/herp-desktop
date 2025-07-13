package com.hersac.core.modules.ventas.services.impl;

import com.hersac.core.modules.ventas.entities.VentaEntity;

import java.util.List;

public interface VentasService {
    List<VentaEntity> buscarTodos();
    VentaEntity buscarPorId(Long ventaId);
    VentaEntity crear(VentaEntity venta);
    void actualizar(Long ventaId, VentaEntity venta);
    void eliminar(Long ventaId);
}
