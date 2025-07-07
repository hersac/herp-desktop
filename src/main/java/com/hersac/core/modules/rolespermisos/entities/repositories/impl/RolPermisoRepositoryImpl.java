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
        try {
            return entityManager.createQuery("FROM RolPermisoEntity", RolPermisoEntity.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public RolPermisoEntity buscarPorId(Long id) {
        try {
            return entityManager.find(RolPermisoEntity.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<RolPermisoEntity> buscarPorRolId(Long rolId) {
        try {
            return entityManager.createQuery("FROM RolPermisoEntity rp WHERE rp.rol.id = :rolId", RolPermisoEntity.class)
                                .setParameter("rolId", rolId)
                                .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<RolPermisoEntity> crearMasivo(List<RolPermisoEntity> entidades) {
        System.out.println("Creando masivamente RolPermisoEntity: " + entidades);
        try {
            entityManager.getTransaction().begin();
            for (RolPermisoEntity entidad : entidades) {
                entityManager.persist(entidad);
            }
            entityManager.getTransaction().commit();
            return entidades;
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return null;
        }
    }

    @Override
    public RolPermisoEntity crear(RolPermisoEntity entidad) {
        throw new UnsupportedOperationException("Usar crear(RolPermisoEntity[] entidades)");
    }

    @Override
    public void actualizar(RolPermisoEntity entidad) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(entidad);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    @Override
    public void eliminar(Long id) {
        try {
            entityManager.getTransaction().begin();
            RolPermisoEntity rolPermiso = entityManager.find(RolPermisoEntity.class, id);
            if (rolPermiso != null) {
                entityManager.remove(rolPermiso);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    @Override
    public void eliminarPorRolId(Long rolId) {
        try {
            entityManager.getTransaction().begin();
            List<RolPermisoEntity> rolPermisos = buscarPorRolId(rolId);
            for (RolPermisoEntity rolPermiso : rolPermisos) {
                entityManager.remove(rolPermiso);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

}
