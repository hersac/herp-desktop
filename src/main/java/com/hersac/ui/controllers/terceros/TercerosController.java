package com.hersac.ui.controllers.terceros;

import com.hersac.core.modules.terceros.entities.TerceroEntity;
import com.hersac.core.modules.terceros.services.TercerosServices;
import java.util.List;

public class TercerosController {
    private final TercerosServices tercerosServices;

    public TercerosController(TercerosServices tercerosServices) {
        this.tercerosServices = tercerosServices;
    }

    public List<TerceroEntity> buscarTodos() {
        return tercerosServices.buscarTodos();
    }

    public TerceroEntity buscarPorId(String terceroId) {
        return tercerosServices.buscarPorId(terceroId);
    }

    public void crear(TerceroEntity tercero) {
        tercerosServices.crear(tercero);
    }

    public void actualizar(String terceroId, TerceroEntity tercero) {
        tercerosServices.actualizar(terceroId, tercero);
    }

    public void eliminar(String terceroId) {
        tercerosServices.eliminar(terceroId);
    }
}

