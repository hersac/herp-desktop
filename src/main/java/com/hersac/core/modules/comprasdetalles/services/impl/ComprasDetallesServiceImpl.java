package com.hersac.core.modules.comprasdetalles.services.impl;

import com.hersac.core.globals.store.UserSessionStore;
import com.hersac.core.modules.comprasdetalles.entities.CompraDetalleEntity;
import com.hersac.core.modules.comprasdetalles.entities.repositories.CompraDetalleRepository;
import com.hersac.core.modules.comprasdetalles.services.ComprasDetallesService;
import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import java.util.List;

public class ComprasDetallesServiceImpl implements ComprasDetallesService {
    private final CompraDetalleRepository compraDetalleRepository;
    private final UserSessionStore store = UserSessionStore.getInstance();

    public ComprasDetallesServiceImpl(CompraDetalleRepository compraDetalleRepository) {
        this.compraDetalleRepository = compraDetalleRepository;
    }

    @Override
    public List<CompraDetalleEntity> buscarTodos() {
        return compraDetalleRepository.buscarTodos();
    }

    @Override
    public CompraDetalleEntity buscarPorId(Long compraDetalleId) {
        return compraDetalleRepository.buscarPorId(compraDetalleId);
    }

    @Override
    public CompraDetalleEntity crear(CompraDetalleEntity compraDetalle) {
        UsuarioEntity usuarioActual = store.getUsuarioActual();
        compraDetalle.setUsuarioCreacion(usuarioActual);
        compraDetalle.setUsuarioActualizacion(usuarioActual);
        return compraDetalleRepository.crear(compraDetalle);
    }

    @Override
    public void actualizar(Long compraDetalleId, CompraDetalleEntity compraDetalle) {
        UsuarioEntity usuarioActual = store.getUsuarioActual();
        CompraDetalleEntity existente = compraDetalleRepository.buscarPorId(compraDetalleId);
        existente.setCantidad(compraDetalle.getCantidad());
        existente.setPrecioUnitario(compraDetalle.getPrecioUnitario());
        existente.setSubTotal(compraDetalle.getSubTotal());
        existente.setItem(compraDetalle.getItem());
        existente.setCompra(compraDetalle.getCompra());
        existente.setUsuarioActualizacion(usuarioActual);
        compraDetalleRepository.actualizar(existente);
    }

    @Override
    public void eliminar(Long compraDetalleId) {
        compraDetalleRepository.eliminar(compraDetalleId);
    }
}
