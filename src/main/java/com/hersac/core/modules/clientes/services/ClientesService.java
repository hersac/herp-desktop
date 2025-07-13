package com.hersac.core.modules.clientes.services;

import com.hersac.core.modules.clientes.entities.ClienteEntity;
import java.util.List;

public interface ClientesService {
    List<ClienteEntity> buscarTodos();
    ClienteEntity buscarPorId(Long clienteId);
    void crear(ClienteEntity cliente);
    void actualizar(Long clienteId, ClienteEntity cliente);
    void eliminar(Long clienteId);
    ClienteEntity buscarPorTerceroId(String terceroId);
}
