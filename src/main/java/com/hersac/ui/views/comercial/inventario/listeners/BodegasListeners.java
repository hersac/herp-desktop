package com.hersac.ui.views.comercial.inventario.listeners;

import com.hersac.core.modules.bodegas.entities.BodegaEntity;

public interface BodegasListeners {
    void crearBodega(BodegaEntity bodega);
    void verBodega(BodegaEntity bodega);
    void actualizarBodega(BodegaEntity bodega);
    void eliminarBodega(BodegaEntity bodega);
}

