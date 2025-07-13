package com.hersac.core.modules.movimientosinventarios.services.impl;

import com.hersac.core.globals.store.UserSessionStore;
import com.hersac.core.modules.movimientosinventarios.entities.MovimientoInventarioEntity;
import com.hersac.core.modules.movimientosinventarios.entities.repositories.MovimientoInventarioRepository;
import com.hersac.core.modules.movimientosinventarios.services.MovimientosInventariosService;
import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import java.util.List;

public class MovimientosInventariosServiceImpl implements MovimientosInventariosService {
    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final UserSessionStore store = UserSessionStore.getInstance();

    public MovimientosInventariosServiceImpl(MovimientoInventarioRepository movimientoInventarioRepository) {
        this.movimientoInventarioRepository = movimientoInventarioRepository;
    }

    @Override
    public List<MovimientoInventarioEntity> buscarTodos() {
        return movimientoInventarioRepository.buscarTodos();
    }

    @Override
    public MovimientoInventarioEntity buscarPorId(Long movimientoInventarioId) {
        return movimientoInventarioRepository.buscarPorId(movimientoInventarioId);
    }

    @Override
    public MovimientoInventarioEntity crear(MovimientoInventarioEntity movimiento) {
        UsuarioEntity usuarioActual = store.getUsuarioActual();
        movimiento.setUsuarioCreacion(usuarioActual);
        movimiento.setUsuarioActualizacion(usuarioActual);
        return movimientoInventarioRepository.crear(movimiento);
    }

    @Override
    public void actualizar(Long movimientoInventarioId, MovimientoInventarioEntity movimiento) {
        UsuarioEntity usuarioActual = store.getUsuarioActual();
        MovimientoInventarioEntity existente = movimientoInventarioRepository.buscarPorId(movimientoInventarioId);
        existente.setTipoMovimiento(movimiento.getTipoMovimiento());
        existente.setCantidad(movimiento.getCantidad());
        existente.setReferenciaId(movimiento.getReferenciaId());
        existente.setReferenciaTipo(movimiento.getReferenciaTipo());
        existente.setItem(movimiento.getItem());
        existente.setBodega(movimiento.getBodega());
        existente.setUsuarioActualizacion(usuarioActual);
        movimientoInventarioRepository.actualizar(existente);
    }

    @Override
    public void eliminar(Long movimientoInventarioId) {
        movimientoInventarioRepository.eliminar(movimientoInventarioId);
    }
}
