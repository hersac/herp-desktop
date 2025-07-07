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
        try {
            return entityManager.createQuery("FROM RolEntity", RolEntity.class).getResultList();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException("Error al buscar todos los roles", e);
        }
    }

    @Override
    public RolEntity buscarPorId(Long id) {
        try {
            return entityManager.find(RolEntity.class, id);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException("Error al buscar el rol por ID", e);
        }
    }

    @Override
    public RolEntity crear(RolEntity entidad) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(entidad);
            entityManager.getTransaction().commit();
            return entidad;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException("Error al crear el rol", e);
        }
    }

    @Override
    public void actualizar(RolEntity entidad) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(entidad);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException("Error al actualizar el rol", e);
        }
    }

    @Override
    public void eliminar(Long id) {
        try {
            entityManager.getTransaction().begin();
            RolEntity rol = entityManager.find(RolEntity.class, id);
            if (rol != null) {
                entityManager.remove(rol);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException("Error al eliminar el rol", e);
        }
    }
}
