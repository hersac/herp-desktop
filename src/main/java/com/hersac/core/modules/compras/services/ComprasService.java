package com.hersac.core.modules.compras.services;

import com.hersac.core.modules.compras.entities.CompraEntity;
import java.util.List;

public interface ComprasService {
    List<CompraEntity> buscarTodos();
    CompraEntity buscarPorId(Long compraId);
    CompraEntity crear(CompraEntity compra);
    void actualizar(Long compraId, CompraEntity compra);
    void eliminar(Long compraId);
}
