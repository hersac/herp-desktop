package com.hersac.core.modules.terceros.services.impl;

import com.hersac.core.modules.terceros.entities.TerceroEntity;
import com.hersac.core.modules.terceros.entities.repositories.TerceroRepository;
import com.hersac.core.modules.terceros.services.TercerosServices;
import java.util.List;

public class TercerosServicesImpl implements TercerosServices {
    private final TerceroRepository terceroRepository;

    public TercerosServicesImpl(TerceroRepository terceroRepository) {
        this.terceroRepository = terceroRepository;
    }

    @Override
    public List<TerceroEntity> buscarTodos() {
        return terceroRepository.buscarTodos();
    }

    @Override
    public TerceroEntity buscarPorId(String terceroId) {
        return terceroRepository.buscarPorId(terceroId);
    }

    @Override
    public void crear(TerceroEntity tercero) {
        terceroRepository.crear(tercero);
    }

    @Override
    public void actualizar(String terceroId, TerceroEntity tercero) {
        TerceroEntity terceroExistente = terceroRepository.buscarPorId(terceroId);
        terceroExistente.setTipo(tercero.getTipo());
        terceroExistente.setNombre(tercero.getNombre());
        terceroExistente.setDireccion(tercero.getDireccion());
        terceroExistente.setTelefono(tercero.getTelefono());
        terceroExistente.setEmail(tercero.getEmail());
        terceroExistente.setEstaActivo(tercero.getEstaActivo());
        terceroRepository.actualizar(tercero);
    }

    @Override
    public void eliminar(String terceroId) {
        terceroRepository.eliminar(terceroId);
    }
}

