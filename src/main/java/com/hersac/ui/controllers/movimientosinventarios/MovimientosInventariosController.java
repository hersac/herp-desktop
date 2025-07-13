package com.hersac.ui.controllers.movimientosinventarios;

import com.hersac.core.modules.movimientosinventarios.entities.MovimientoInventarioEntity;
import com.hersac.core.modules.movimientosinventarios.services.MovimientosInventariosService;

import java.util.List;

public class MovimientosInventariosController {
    private final MovimientosInventariosService movimientosInventariosService;

    public MovimientosInventariosController(MovimientosInventariosService movimientosInventariosService) {
        this.movimientosInventariosService = movimientosInventariosService;
    }

    public List<MovimientoInventarioEntity> buscarTodos() {
        return movimientosInventariosService.buscarTodos();
    }

    public MovimientoInventarioEntity buscarPorId(Long id) {
        return movimientosInventariosService.buscarPorId(id);
    }

    public MovimientoInventarioEntity crear(MovimientoInventarioEntity movimientoInventario) {
        return movimientosInventariosService.crear(movimientoInventario);
    }

    public void actualizar(Long id, MovimientoInventarioEntity movimientoInventario) {
        movimientosInventariosService.actualizar(id, movimientoInventario);
    }

    public void eliminar(Long id) {
        movimientosInventariosService.eliminar(id);
    }
}
