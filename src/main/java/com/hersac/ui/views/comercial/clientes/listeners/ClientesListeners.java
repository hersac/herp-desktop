package com.hersac.ui.views.comercial.clientes.listeners;

import com.hersac.core.modules.clientes.entities.ClienteEntity;

public interface ClientesListeners {
    void crearCliente(ClienteEntity cliente);
    void verCliente(ClienteEntity cliente);
    void actualizarCliente(ClienteEntity cliente);
    void eliminarCliente(ClienteEntity cliente);
}

