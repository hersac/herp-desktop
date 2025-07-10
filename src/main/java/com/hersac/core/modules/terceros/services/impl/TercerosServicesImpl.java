package com.hersac.core.modules.terceros.services.impl;

import com.hersac.core.globals.store.UserSessionStore;
import com.hersac.core.modules.terceros.entities.TerceroEntity;
import com.hersac.core.modules.terceros.entities.relations.TipoPersonaEntity;
import com.hersac.core.modules.terceros.entities.repositories.TerceroRepository;
import com.hersac.core.modules.terceros.services.TercerosServices;
import com.hersac.core.modules.usuarios.entities.UsuarioEntity;

import java.util.List;

public class TercerosServicesImpl implements TercerosServices {
    private final TerceroRepository terceroRepository;
    private final UserSessionStore store = UserSessionStore.getInstance();

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
        UsuarioEntity usuarioActual = store.getUsuarioActual();
        tercero.setUsuarioCreador(usuarioActual);
        tercero.setUsuarioActualizador(usuarioActual);
        terceroRepository.crear(tercero);
    }

    @Override
    public void actualizar(String terceroId, TerceroEntity tercero) {
        UsuarioEntity usuarioActual = store.getUsuarioActual();
        TerceroEntity terceroExistente = terceroRepository.buscarPorId(terceroId);
        TipoPersonaEntity tipoPersona = new TipoPersonaEntity();
        tipoPersona.setTipoPersonaId(tercero.getTipoPersona().getTipoPersonaId());
        terceroExistente.setTipoPersona(tipoPersona);
        terceroExistente.setNombre(tercero.getNombre());
        terceroExistente.setDireccion(tercero.getDireccion());
        terceroExistente.setTelefono(tercero.getTelefono());
        terceroExistente.setEmail(tercero.getEmail());
        terceroExistente.setEstaActivo(tercero.getEstaActivo());
        terceroExistente.setUsuarioActualizador(usuarioActual);
        terceroRepository.actualizar(tercero);
    }

    @Override
    public void eliminar(String terceroId) {
        terceroRepository.eliminar(terceroId);
    }
}

