package com.hersac.core.modules.comprasdetalles.services;

import com.hersac.core.modules.comprasdetalles.entities.CompraDetalleEntity;
import java.util.List;

public interface ComprasDetallesService {
    List<CompraDetalleEntity> buscarTodos();
    CompraDetalleEntity buscarPorId(Long compraDetalleId);
    CompraDetalleEntity crear(CompraDetalleEntity compraDetalle);
    void actualizar(Long compraDetalleId, CompraDetalleEntity compraDetalle);
    void eliminar(Long compraDetalleId);
}
