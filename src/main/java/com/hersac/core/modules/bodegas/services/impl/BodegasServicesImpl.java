package com.hersac.core.modules.bodegas.services.impl;

import com.hersac.core.globals.store.UserSessionStore;
import com.hersac.core.modules.bodegas.entities.BodegaEntity;
import com.hersac.core.modules.bodegas.entities.repositories.BodegaRepository;
import com.hersac.core.modules.bodegas.services.BodegasServices;
import com.hersac.core.globals.exceptions.BodegaNoEncontradaException;
import com.hersac.core.modules.usuarios.entities.UsuarioEntity;

import java.util.List;

public class BodegasServicesImpl implements BodegasServices {

    private final BodegaRepository bodegaRepository;
    private final UserSessionStore store = UserSessionStore.getInstance();


    public BodegasServicesImpl(BodegaRepository bodegaRepository) {
        this.bodegaRepository = bodegaRepository;
    }

    @Override
    public List<BodegaEntity> buscarTodos() {
        return bodegaRepository.buscarTodos();
    }

    @Override
    public BodegaEntity buscarPorId(Long bodegaId) {
        BodegaEntity bodega = bodegaRepository.buscarPorId(bodegaId);
        if (bodega == null) {
            throw new BodegaNoEncontradaException("Bodega con ID " + bodegaId + " no encontrada.");
        }
        return bodega;
    }

    @Override
    public BodegaEntity crear(BodegaEntity bodega) {
        UsuarioEntity usuarioActual = store.getUsuarioActual();
        bodega.setCreadaPor(usuarioActual);
        return bodegaRepository.crear(bodega);
    }

    @Override
    public void actualizar(Long bodegaId, BodegaEntity bodega) {
        BodegaEntity bodegaExistente = bodegaRepository.buscarPorId(bodegaId);
        if (bodegaExistente == null) {
            throw new BodegaNoEncontradaException("Bodega con ID " + bodegaId + " no encontrada.");
        }
        bodegaExistente.setNombre(bodega.getNombre());
        bodegaExistente.setEstaActiva(bodega.getEstaActiva());
        bodegaExistente.setCreadaPor(bodega.getCreadaPor());
        bodegaRepository.actualizar(bodegaExistente);
    }

    @Override
    public void eliminar(Long bodegaId) {
        BodegaEntity bodegaExistente = bodegaRepository.buscarPorId(bodegaId);
        if (bodegaExistente == null) {
            throw new BodegaNoEncontradaException("Bodega con ID " + bodegaId + " no encontrada.");
        }
        bodegaRepository.eliminar(bodegaId);
    }
}
