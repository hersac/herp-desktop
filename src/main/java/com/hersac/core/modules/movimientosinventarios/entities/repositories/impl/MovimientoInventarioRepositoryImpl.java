package com.hersac.core.modules.movimientosinventarios.entities.repositories.impl;

import com.hersac.core.modules.movimientosinventarios.entities.MovimientoInventarioEntity;
import com.hersac.core.modules.movimientosinventarios.entities.repositories.MovimientoInventarioRepository;
import jakarta.persistence.EntityManager;
import java.util.List;

public class MovimientoInventarioRepositoryImpl implements MovimientoInventarioRepository {
    private final EntityManager entityManager;

    public MovimientoInventarioRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<MovimientoInventarioEntity> buscarTodos() {
        return entityManager.createQuery("FROM MovimientoInventarioEntity", MovimientoInventarioEntity.class).getResultList();
    }

    @Override
    public MovimientoInventarioEntity buscarPorId(Long id) {
        return entityManager.find(MovimientoInventarioEntity.class, id);
    }

    @Override
    public MovimientoInventarioEntity crear(MovimientoInventarioEntity entidad) {
        entityManager.getTransaction().begin();
        entityManager.persist(entidad);
        entityManager.getTransaction().commit();
        return entidad;
    }

    @Override
    public void actualizar(MovimientoInventarioEntity entidad) {
        entityManager.getTransaction().begin();
        entityManager.merge(entidad);
        entityManager.getTransaction().commit();
    }

    @Override
    public void eliminar(Long id) {
        entityManager.getTransaction().begin();
        MovimientoInventarioEntity movimiento = entityManager.find(MovimientoInventarioEntity.class, id);
        if (movimiento != null) {
            entityManager.remove(movimiento);
        }
        entityManager.getTransaction().commit();
    }
}
