package com.hersac.core.globals.repositories;

import java.util.List;

public interface CrudRepository<T, I> {
    List<T> buscarTodos();
    T buscarPorId(I id);
    T crear(T entidad);
    void actualizar(T entidad);
    void eliminar(I id);
}
