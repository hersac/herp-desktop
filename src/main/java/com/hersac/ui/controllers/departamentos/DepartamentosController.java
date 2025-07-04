package com.hersac.ui.controllers.departamentos;

import java.util.List;

import com.hersac.core.modules.departamentos.entities.DepartamentoEntity;
import com.hersac.core.modules.departamentos.services.DepartamentosServices;

public class DepartamentosController {
    private final DepartamentosServices departamentosServices;

    public DepartamentosController(DepartamentosServices departamentosServices) {
        this.departamentosServices = departamentosServices;
    }

    public List<DepartamentoEntity> buscarTodos() {
        return departamentosServices.buscarTodos();
    }

    public DepartamentoEntity buscarPorId(Long departamentoId) {
        return departamentosServices.buscarPorId(departamentoId);
    }

    public DepartamentoEntity crear(DepartamentoEntity departamento) {
        return departamentosServices.crearDepartamento(departamento);
    }

    public void actualizar(Long departamentoId, DepartamentoEntity departamento) {
        departamentosServices.actualizarDepartamento(departamentoId, departamento);
    }

    public void eliminar(Long departamentoId) {
        departamentosServices.eliminarDepartamento(departamentoId);
    }
}
