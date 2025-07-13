package com.hersac.core.modules.compras.services.impl;

import com.hersac.core.globals.store.UserSessionStore;
import com.hersac.core.modules.compras.entities.CompraEntity;
import com.hersac.core.modules.compras.entities.repositories.CompraRepository;
import com.hersac.core.modules.compras.services.ComprasService;
import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import java.util.List;

public class ComprasServiceImpl implements ComprasService {
    private final CompraRepository compraRepository;
    private final UserSessionStore store = UserSessionStore.getInstance();

    public ComprasServiceImpl(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;
    }

    @Override
    public List<CompraEntity> buscarTodos() {
        return compraRepository.buscarTodos();
    }

    @Override
    public CompraEntity buscarPorId(Long compraId) {
        return compraRepository.buscarPorId(compraId);
    }

    @Override
    public CompraEntity crear(CompraEntity compra) {
        UsuarioEntity usuarioActual = store.getUsuarioActual();
        compra.setUsuarioCreacion(usuarioActual);
        compra.setUsuarioActualizacion(usuarioActual);
        return compraRepository.crear(compra);
    }

    @Override
    public void actualizar(Long compraId, CompraEntity compra) {
        UsuarioEntity usuarioActual = store.getUsuarioActual();
        CompraEntity compraExistente = compraRepository.buscarPorId(compraId);
        compraExistente.setTotalCompra(compra.getTotalCompra());
        compraExistente.setEstado(compra.getEstado());
        compraExistente.setObservaciones(compra.getObservaciones());
        compraExistente.setProveedor(compra.getProveedor());
        compraExistente.setUsuarioActualizacion(usuarioActual);
        compraRepository.actualizar(compraExistente);
    }

    @Override
    public void eliminar(Long compraId) {
        compraRepository.eliminar(compraId);
    }
}
