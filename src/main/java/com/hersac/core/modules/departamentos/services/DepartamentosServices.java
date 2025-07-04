package com.hersac.core.modules.departamentos.services;

import com.hersac.core.modules.departamentos.entities.DepartamentoEntity;

import java.util.List;

public interface DepartamentosServices {
    public List<DepartamentoEntity> buscarTodos();

    public DepartamentoEntity buscarPorId(Long departamentoId);

    public DepartamentoEntity crearDepartamento(DepartamentoEntity departamento);

    public void actualizarDepartamento(Long departamentoId, DepartamentoEntity departamento);

    public void eliminarDepartamento(Long departamentoId);
}
