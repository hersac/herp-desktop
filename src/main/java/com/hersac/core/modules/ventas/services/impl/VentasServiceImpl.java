package com.hersac.core.modules.ventas.services.impl;

import com.hersac.core.globals.store.UserSessionStore;
import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import com.hersac.core.modules.ventas.entities.VentaEntity;
import com.hersac.core.modules.ventas.entities.repositories.VentaRepository;

import java.util.List;

public class VentasServiceImpl implements VentasService{
    private final VentaRepository ventaRepository;
    private final UserSessionStore store = UserSessionStore.getInstance();

    public VentasServiceImpl(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    public List<VentaEntity> buscarTodos() {
        return ventaRepository.buscarTodos();
    }

    @Override
    public VentaEntity buscarPorId(Long ventaId) {
        return ventaRepository.buscarPorId(ventaId);
    }

    @Override
    public VentaEntity crear(VentaEntity venta) {
        UsuarioEntity usuarioActual = store.getUsuarioActual();
        venta.setUsuarioCreacion(usuarioActual);
        venta.setUsuarioActualizacion(usuarioActual);
        return ventaRepository.crear(venta);
    }

    @Override
    public void actualizar(Long ventaId, VentaEntity venta) {
        UsuarioEntity usuarioActual = store.getUsuarioActual();
        VentaEntity ventaExistente = ventaRepository.buscarPorId(ventaId);
        ventaExistente.setTotalVenta(venta.getTotalVenta());
        ventaExistente.setEstado(venta.getEstado());
        ventaExistente.setObservaciones(venta.getObservaciones());
        ventaExistente.setCliente(venta.getCliente());
        ventaExistente.setUsuarioActualizacion(usuarioActual);
        ventaRepository.actualizar(ventaExistente);
    }

    @Override
    public void eliminar(Long ventaId) {
        ventaRepository.eliminar(ventaId);
    }
}
