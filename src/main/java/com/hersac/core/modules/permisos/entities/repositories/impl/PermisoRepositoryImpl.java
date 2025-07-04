package com.hersac.core.modules.permisos.entities.repositories.impl;

import java.util.List;

import com.hersac.core.modules.permisos.entities.PermisoEntity;
import com.hersac.core.modules.permisos.entities.repositories.PermisoRepository;

import jakarta.persistence.EntityManager;

public class PermisoRepositoryImpl implements PermisoRepository {

    private final EntityManager entityManager;

    public PermisoRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<PermisoEntity> buscarTodos() {
        return entityManager.createQuery("FROM PermisoEntity", PermisoEntity.class).getResultList();
    }

    @Override
    public PermisoEntity buscarPorId(Long id) {
        return entityManager.find(PermisoEntity.class, id);
    }

    @Override
    public PermisoEntity crear(PermisoEntity entidad) {
        entityManager.getTransaction().begin();
        entityManager.persist(entidad);
        entityManager.getTransaction().commit();
        return entidad;
    }

    @Override
    public void actualizar(PermisoEntity entidad) {
        entityManager.getTransaction().begin();
        entityManager.merge(entidad);
        entityManager.getTransaction().commit();
    }

    @Override
    public void eliminar(Long id) {
        entityManager.getTransaction().begin();
        PermisoEntity permiso = entityManager.find(PermisoEntity.class, id);
        if (permiso != null) {
            entityManager.remove(permiso);
        }
        entityManager.getTransaction().commit();
    }

}
