package com.hersac.core.modules.bodegas.entities.repositories.impl;

import com.hersac.core.modules.bodegas.entities.BodegaEntity;
import com.hersac.core.modules.bodegas.entities.repositories.BodegaRepository;
import jakarta.persistence.EntityManager;
import java.util.List;

public class BodegaRepositoryImpl implements BodegaRepository {

    private final EntityManager entityManager;

    public BodegaRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<BodegaEntity> buscarTodos() {
        return entityManager.createQuery("FROM BodegaEntity", BodegaEntity.class).getResultList();
    }

    @Override
    public BodegaEntity buscarPorId(Long id) {
        return entityManager.find(BodegaEntity.class, id);
    }

    @Override
    public BodegaEntity crear(BodegaEntity entidad) {
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        entityManager.persist(entidad);
        entityManager.getTransaction().commit();
        return entidad;
    }

    @Override
    public void actualizar(BodegaEntity entidad) {
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        entityManager.merge(entidad);
        entityManager.getTransaction().commit();
    }

    @Override
    public void eliminar(Long id) {
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        BodegaEntity bodega = entityManager.find(BodegaEntity.class, id);
        if (bodega != null) {
            entityManager.remove(bodega);
        }
        entityManager.getTransaction().commit();
    }
}
