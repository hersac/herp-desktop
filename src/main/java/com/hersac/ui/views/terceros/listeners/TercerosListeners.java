package com.hersac.ui.views.terceros.listeners;

import com.hersac.core.modules.terceros.entities.TerceroEntity;

public interface TercerosListeners {
    void crearTercero(TerceroEntity tercero);
    void verTercero(TerceroEntity tercero);
    void actualizarTercero(TerceroEntity tercero);
    void eliminarTercero(TerceroEntity tercero);
}
