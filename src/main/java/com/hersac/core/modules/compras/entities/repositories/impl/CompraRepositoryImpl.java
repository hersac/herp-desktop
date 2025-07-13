package com.hersac.core.modules.compras.entities.repositories.impl;

import com.hersac.core.modules.compras.entities.CompraEntity;
import com.hersac.core.modules.compras.entities.repositories.CompraRepository;
import jakarta.persistence.EntityManager;
import java.util.List;

public class CompraRepositoryImpl implements CompraRepository {
    private final EntityManager entityManager;

    public CompraRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<CompraEntity> buscarTodos() {
        return entityManager.createQuery("FROM CompraEntity", CompraEntity.class).getResultList();
    }

    @Override
    public CompraEntity buscarPorId(Long id) {
        return entityManager.find(CompraEntity.class, id);
    }

    @Override
    public CompraEntity crear(CompraEntity entidad) {
        entityManager.getTransaction().begin();
        entityManager.persist(entidad);
        entityManager.getTransaction().commit();
        return entidad;
    }

    @Override
    public void actualizar(CompraEntity entidad) {
        entityManager.getTransaction().begin();
        entityManager.merge(entidad);
        entityManager.getTransaction().commit();
    }

    @Override
    public void eliminar(Long id) {
        entityManager.getTransaction().begin();
        CompraEntity compra = entityManager.find(CompraEntity.class, id);
        if (compra != null) {
            entityManager.remove(compra);
        }
        entityManager.getTransaction().commit();
    }
}
