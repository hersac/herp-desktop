package com.hersac.ui.controllers.clientes;

import com.hersac.core.modules.clientes.entities.ClienteEntity;
import com.hersac.core.modules.clientes.services.ClientesService;
import java.util.List;

public class ClientesController {
    private final ClientesService clientesService;

    public ClientesController(ClientesService clientesService) {
        this.clientesService = clientesService;
    }

    public List<ClienteEntity> buscarTodos() {
        return clientesService.buscarTodos();
    }

    public ClienteEntity buscarPorId(Long clienteId) {
        return clientesService.buscarPorId(clienteId);
    }

    public void crear(ClienteEntity cliente) {
        clientesService.crear(cliente);
    }

    public void actualizar(Long clienteId, ClienteEntity cliente) {
        clientesService.actualizar(clienteId, cliente);
    }

    public void eliminar(Long clienteId) {
        clientesService.eliminar(clienteId);
    }
}
