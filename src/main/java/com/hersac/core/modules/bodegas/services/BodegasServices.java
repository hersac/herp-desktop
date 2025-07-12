package com.hersac.core.modules.bodegas.services;

import com.hersac.core.modules.bodegas.entities.BodegaEntity;
import java.util.List;

public interface BodegasServices {
    List<BodegaEntity> buscarTodos();
    BodegaEntity buscarPorId(Long id);
    BodegaEntity crear(BodegaEntity bodega);
    void actualizar(Long BodegaId, BodegaEntity bodega);
    void eliminar(Long id);
}

