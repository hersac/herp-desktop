package com.hersac.core.modules.terceros.entities.repositories.impl;

import com.hersac.core.modules.terceros.entities.TerceroEntity;
import com.hersac.core.modules.terceros.entities.repositories.TerceroRepository;
import jakarta.persistence.EntityManager;
import java.util.List;

public class TerceroRepositoryImpl implements TerceroRepository {
    private final EntityManager entityManager;

    public TerceroRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<TerceroEntity> buscarTodos() {
        try {
            return entityManager.createQuery("FROM TerceroEntity", TerceroEntity.class).getResultList();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error al buscar todos los terceros", e);
        }
    }

    @Override
    public TerceroEntity buscarPorId(String terceroId) {
        try {
            return entityManager.find(TerceroEntity.class, terceroId);
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error al buscar el tercero por ID", e);
        }
    }

    @Override
    public TerceroEntity crear(TerceroEntity entidad) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(entidad);
            entityManager.getTransaction().commit();
            return entidad;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error al crear el tercero", e);
        }
    }

    @Override
    public void actualizar(TerceroEntity entidad) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(entidad);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error al actualizar el tercero", e);
        }
    }

    @Override
    public void eliminar(String terceroId) {
        try {
            entityManager.getTransaction().begin();
            TerceroEntity tercero = entityManager.find(TerceroEntity.class, terceroId);
            if (tercero != null) {
                entityManager.remove(tercero);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar el tercero", e);
        }
    }
}