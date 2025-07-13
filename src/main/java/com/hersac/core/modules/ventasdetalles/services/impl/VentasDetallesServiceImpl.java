package com.hersac.core.modules.ventasdetalles.services.impl;


import com.hersac.core.globals.store.UserSessionStore;
import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import com.hersac.core.modules.ventasdetalles.entities.VentaDetalleEntity;
import com.hersac.core.modules.ventasdetalles.entities.repositories.VentaDetalleRepository;
import com.hersac.core.modules.ventasdetalles.services.VentasDetallesService;

import java.util.List;

public class VentasDetallesServiceImpl implements VentasDetallesService {
    private final VentaDetalleRepository ventaDetalleRepository;
    private final UserSessionStore store = UserSessionStore.getInstance();

    public VentasDetallesServiceImpl(VentaDetalleRepository ventaDetalleRepository) {
        this.ventaDetalleRepository = ventaDetalleRepository;
    }

    @Override
    public List<VentaDetalleEntity> buscarTodos() {
        return ventaDetalleRepository.buscarTodos();
    }

    @Override
    public VentaDetalleEntity buscarPorId(Long ventaDetalleId) {
        return ventaDetalleRepository.buscarPorId(ventaDetalleId);
    }

    @Override
    public VentaDetalleEntity crear(VentaDetalleEntity ventaDetalle) {
        UsuarioEntity usuarioActual = store.getUsuarioActual();
        ventaDetalle.setUsuarioCreacion(usuarioActual);
        ventaDetalle.setUsuarioActualizacion(usuarioActual);
        return ventaDetalleRepository.crear(ventaDetalle);
    }

    @Override
    public void actualizar(Long ventaDetalleId, VentaDetalleEntity ventaDetalle) {
        UsuarioEntity usuarioActual = store.getUsuarioActual();
        VentaDetalleEntity existente = ventaDetalleRepository.buscarPorId(ventaDetalleId);
        existente.setCantidad(ventaDetalle.getCantidad());
        existente.setPrecioUnitario(ventaDetalle.getPrecioUnitario());
        existente.setSubTotal(ventaDetalle.getSubTotal());
        existente.setItem(ventaDetalle.getItem());
        existente.setVenta(ventaDetalle.getVenta());
        existente.setUsuarioActualizacion(usuarioActual);
        ventaDetalleRepository.actualizar(existente);
    }

    @Override
    public void eliminar(Long ventaDetalleId) {
        ventaDetalleRepository.eliminar(ventaDetalleId);
    }
}
