package com.hersac.core.modules.roles.entities.repositories.impl;

import java.util.List;

import com.hersac.core.modules.roles.entities.RolEntity;
import com.hersac.core.modules.roles.entities.repositories.RolRepository;

import jakarta.persistence.EntityManager;

public class RolRepositoryImpl implements RolRepository {
    private final EntityManager entityManager;

    public RolRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<RolEntity> buscarTodos() {
        return entityManager.createQuery("FROM RolEntity", RolEntity.class).getResultList();
    }

    @Override
    public RolEntity buscarPorId(Long id) {
        return entityManager.find(RolEntity.class, id);
    }

    @Override
    public RolEntity crear(RolEntity entidad) {
        entityManager.getTransaction().begin();
        entityManager.persist(entidad);
        entityManager.getTransaction().commit();
        return entidad;
    }

    @Override
    public void actualizar(RolEntity entidad) {
        entityManager.getTransaction().begin();
        entityManager.merge(entidad);
        entityManager.getTransaction().commit();
    }

    @Override
    public void eliminar(Long id) {
        entityManager.getTransaction().begin();
        RolEntity rol = entityManager.find(RolEntity.class, id);
        if (rol != null) {
            entityManager.remove(rol);
        }
        entityManager.getTransaction().commit();
    }
}
