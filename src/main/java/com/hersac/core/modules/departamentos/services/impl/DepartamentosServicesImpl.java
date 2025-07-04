package com.hersac.core.modules.departamentos.services.impl;

import com.hersac.core.modules.departamentos.entities.DepartamentoEntity;
import com.hersac.core.modules.departamentos.services.DepartamentosServices;

import java.util.List;

import com.hersac.core.modules.departamentos.entities.repositories.DepartamentoRepository;

public class DepartamentosServicesImpl implements DepartamentosServices {

    private final DepartamentoRepository departamentoRepository;

    public DepartamentosServicesImpl(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    @Override
    public List<DepartamentoEntity> buscarTodos() {
        return departamentoRepository.buscarTodos();
    }

    @Override
    public DepartamentoEntity buscarPorId(Long departamentoId) {
        DepartamentoEntity departamento = departamentoRepository.buscarPorId(departamentoId);
        if (departamento == null) {
            throw new IllegalArgumentException("Departamento no encontrado con ID: " + departamentoId);
        }
        return departamento;
    }

    @Override
    public DepartamentoEntity crearDepartamento(DepartamentoEntity departamento) {
        return departamentoRepository.crear(departamento);
    }

    @Override
    public void actualizarDepartamento(Long departamentoId, DepartamentoEntity departamento) {
        DepartamentoEntity departamentoExistente = departamentoRepository.buscarPorId(departamentoId);
        if (departamentoExistente == null) {
            throw new IllegalArgumentException("Departamento no encontrado con ID: " + departamentoId);
        }
        departamentoExistente.setNombre(departamento.getNombre());
        departamentoExistente.setDescripcion(departamento.getDescripcion());
        departamentoRepository.actualizar(departamentoExistente);
    }

    @Override
    public void eliminarDepartamento(Long departamentoId) {
        DepartamentoEntity departamentoExistente = departamentoRepository.buscarPorId(departamentoId);
        if (departamentoExistente == null) {
            throw new IllegalArgumentException("Departamento no encontrado con ID: " + departamentoId);
        }
        departamentoRepository.eliminar(departamentoId);
    }
}
