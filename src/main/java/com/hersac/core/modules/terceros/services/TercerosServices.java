package com.hersac.core.modules.terceros.services;

import com.hersac.core.modules.terceros.entities.TerceroEntity;
import java.util.List;

public interface TercerosServices {
    List<TerceroEntity> buscarTodos();
    TerceroEntity buscarPorId(String terceroId);
    void crear(TerceroEntity tercero);
    void actualizar(String terceroId, TerceroEntity tercero);
    void eliminar(String terceroId);
}

