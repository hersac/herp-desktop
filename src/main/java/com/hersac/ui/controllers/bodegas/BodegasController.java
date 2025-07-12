package com.hersac.ui.controllers.bodegas;

import com.hersac.core.modules.bodegas.entities.BodegaEntity;
import com.hersac.core.modules.bodegas.services.BodegasServices;

import java.util.List;

public class BodegasController {
    private BodegasServices bodegasServices;

    public BodegasController(BodegasServices bodegasServices) {
        this.bodegasServices = bodegasServices;
    }

    public List<BodegaEntity> buscarTodos() {
        return bodegasServices.buscarTodos();
    }

    public BodegaEntity buscarPorId(Long bodegaId) {
        return bodegasServices.buscarPorId(bodegaId);
    }

    public BodegaEntity crear(BodegaEntity bodega) {
        return bodegasServices.crear(bodega);
    }

    public void actualizar(Long bodegaId, BodegaEntity bodega) {
        bodegasServices.actualizar(bodegaId, bodega);
    }

    public void eliminar(Long bodegaId) {
        bodegasServices.eliminar(bodegaId);
    }
}
