package com.hersac.core.modules.clientes.services.impl;

import com.hersac.core.globals.store.UserSessionStore;
import com.hersac.core.modules.clientes.entities.ClienteEntity;
import com.hersac.core.modules.clientes.entities.repositories.ClienteRepository;
import com.hersac.core.modules.clientes.services.ClientesService;
import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import java.util.List;

public class ClientesServiceImpl implements ClientesService {
    private final ClienteRepository clienteRepository;
    private final UserSessionStore store = UserSessionStore.getInstance();

    public ClientesServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<ClienteEntity> buscarTodos() {
        return clienteRepository.buscarTodos();
    }

    @Override
    public ClienteEntity buscarPorId(Long clienteId) {
        return clienteRepository.buscarPorId(clienteId);
    }

    @Override
    public void crear(ClienteEntity cliente) {
        UsuarioEntity usuarioActual = store.getUsuarioActual();
        cliente.setUsuarioCreacion(usuarioActual);
        cliente.setUsuarioActualizacion(usuarioActual);
        clienteRepository.crear(cliente);
    }

    @Override
    public void actualizar(Long clienteId, ClienteEntity cliente) {
        UsuarioEntity usuarioActual = store.getUsuarioActual();
        ClienteEntity clienteExistente = clienteRepository.buscarPorId(clienteId);
        clienteExistente.setTercero(cliente.getTercero());
        clienteExistente.setEsta_activo(cliente.isEsta_activo());
        clienteExistente.setUsuarioActualizacion(usuarioActual);
        clienteRepository.actualizar(clienteExistente);
    }

    @Override
    public void eliminar(Long clienteId) {
        clienteRepository.eliminar(clienteId);
    }
}
