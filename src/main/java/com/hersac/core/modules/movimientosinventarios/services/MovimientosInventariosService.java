package com.hersac.core.modules.movimientosinventarios.services;

import com.hersac.core.modules.movimientosinventarios.entities.MovimientoInventarioEntity;
import java.util.List;

public interface MovimientosInventariosService {
    List<MovimientoInventarioEntity> buscarTodos();
    MovimientoInventarioEntity buscarPorId(Long movimientoInventarioId);
    MovimientoInventarioEntity crear(MovimientoInventarioEntity movimiento);
    void actualizar(Long movimientoInventarioId, MovimientoInventarioEntity movimiento);
    void eliminar(Long movimientoInventarioId);
}
