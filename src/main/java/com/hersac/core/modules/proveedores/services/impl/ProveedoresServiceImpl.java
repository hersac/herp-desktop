package com.hersac.core.modules.proveedores.services.impl;

import com.hersac.core.globals.store.UserSessionStore;
import com.hersac.core.modules.proveedores.entities.ProveedorEntity;
import com.hersac.core.modules.proveedores.entities.repositories.ProveedorRepository;
import com.hersac.core.modules.proveedores.services.ProveedoresService;
import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import java.util.List;

public class ProveedoresServiceImpl implements ProveedoresService {
    private final ProveedorRepository proveedorRepository;
    private final UserSessionStore store = UserSessionStore.getInstance();

    public ProveedoresServiceImpl(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    @Override
    public List<ProveedorEntity> buscarTodos() {
        return proveedorRepository.buscarTodos();
    }

    @Override
    public ProveedorEntity buscarPorId(Long proveedorId) {
        return proveedorRepository.buscarPorId(proveedorId);
    }

    @Override
    public void crear(ProveedorEntity proveedor) {
        UsuarioEntity usuarioActual = store.getUsuarioActual();
        proveedor.setUsuarioCreacion(usuarioActual);
        proveedor.setUsuarioActualizacion(usuarioActual);
        proveedorRepository.crear(proveedor);
    }

    @Override
    public void actualizar(Long proveedorId, ProveedorEntity proveedor) {
        UsuarioEntity usuarioActual = store.getUsuarioActual();
        ProveedorEntity proveedorExistente = proveedorRepository.buscarPorId(proveedorId);
        proveedorExistente.setTercero(proveedor.getTercero());
        proveedorExistente.setEsta_activo(proveedor.isEsta_activo());
        proveedorExistente.setUsuarioActualizacion(usuarioActual);
        proveedorRepository.actualizar(proveedorExistente);
    }

    @Override
    public void eliminar(Long proveedorId) {
        proveedorRepository.eliminar(proveedorId);
    }

    @Override
    public ProveedorEntity buscarPorCodigo(String codigo) {
        return proveedorRepository.buscarPorCodigo(codigo);
    }

    @Override
    public ProveedorEntity buscarPorTerceroId(String terceroId) {
        return proveedorRepository.buscarPorTerceroId(terceroId);
    }
}
