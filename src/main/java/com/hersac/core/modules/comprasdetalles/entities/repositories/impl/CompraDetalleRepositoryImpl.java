package com.hersac.core.modules.comprasdetalles.entities.repositories.impl;

import com.hersac.core.modules.comprasdetalles.entities.CompraDetalleEntity;
import com.hersac.core.modules.comprasdetalles.entities.repositories.CompraDetalleRepository;
import jakarta.persistence.EntityManager;
import java.util.List;

public class CompraDetalleRepositoryImpl implements CompraDetalleRepository {
    private final EntityManager entityManager;

    public CompraDetalleRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<CompraDetalleEntity> buscarTodos() {
        return entityManager.createQuery("FROM CompraDetalleEntity", CompraDetalleEntity.class).getResultList();
    }

    @Override
    public CompraDetalleEntity buscarPorId(Long id) {
        return entityManager.find(CompraDetalleEntity.class, id);
    }

    @Override
    public CompraDetalleEntity crear(CompraDetalleEntity entidad) {
        entityManager.getTransaction().begin();
        entityManager.persist(entidad);
        entityManager.getTransaction().commit();
        return entidad;
    }

    @Override
    public void actualizar(CompraDetalleEntity entidad) {
        entityManager.getTransaction().begin();
        entityManager.merge(entidad);
        entityManager.getTransaction().commit();
    }

    @Override
    public void eliminar(Long id) {
        entityManager.getTransaction().begin();
        CompraDetalleEntity detalle = entityManager.find(CompraDetalleEntity.class, id);
        if (detalle != null) {
            entityManager.remove(detalle);
        }
        entityManager.getTransaction().commit();
    }
}
