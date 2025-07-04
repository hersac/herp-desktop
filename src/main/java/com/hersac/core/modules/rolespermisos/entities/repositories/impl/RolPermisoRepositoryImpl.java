package com.hersac.core.modules.rolespermisos.entities.repositories.impl;

import java.util.List;

import com.hersac.core.modules.rolespermisos.entities.RolPermisoEntity;
import com.hersac.core.modules.rolespermisos.entities.repositories.RolPermisoRepository;

import jakarta.persistence.EntityManager;

public class RolPermisoRepositoryImpl implements RolPermisoRepository {

    private final EntityManager entityManager;

    public RolPermisoRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<RolPermisoEntity> buscarTodos() {
        return entityManager.createQuery("FROM RolPermisoEntity", RolPermisoEntity.class).getResultList();
    }

    @Override
    public RolPermisoEntity buscarPorId(Long id) {
        return entityManager.find(RolPermisoEntity.class, id);
    }

    @Override
    public RolPermisoEntity crear(RolPermisoEntity entidad) {
        entityManager.getTransaction().begin();
        entityManager.persist(entidad);
        entityManager.getTransaction().commit();
        return entidad;
    }

    @Override
    public void actualizar(RolPermisoEntity entidad) {
        entityManager.getTransaction().begin();
        entityManager.merge(entidad);
        entityManager.getTransaction().commit();
    }

    @Override
    public void eliminar(Long id) {
        entityManager.getTransaction().begin();
        RolPermisoEntity rolPermiso = entityManager.find(RolPermisoEntity.class, id);
        if (rolPermiso != null) {
            entityManager.remove(rolPermiso);
        }
        entityManager.getTransaction().commit();
    }

}
