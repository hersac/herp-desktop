package com.hersac.core.modules.ventasdetalles.entities.repositories.impl;

import com.hersac.core.modules.ventasdetalles.entities.VentaDetalleEntity;
import com.hersac.core.modules.ventasdetalles.entities.repositories.VentaDetalleRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class VentaDetalleRepositoryImpl implements VentaDetalleRepository {
    private final EntityManager entityManager;

    public VentaDetalleRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<VentaDetalleEntity> buscarTodos() {
        return entityManager.createQuery("FROM VentaDetalleEntity", VentaDetalleEntity.class).getResultList();
    }

    @Override
    public VentaDetalleEntity buscarPorId(Long id) {
        return entityManager.find(VentaDetalleEntity.class, id);
    }

    @Override
    public VentaDetalleEntity crear(VentaDetalleEntity entidad) {
        entityManager.getTransaction().begin();
        entityManager.persist(entidad);
        entityManager.getTransaction().commit();
        return entidad;
    }

    @Override
    public void actualizar(VentaDetalleEntity entidad) {
        entityManager.getTransaction().begin();
        entityManager.merge(entidad);
        entityManager.getTransaction().commit();
    }

    @Override
    public void eliminar(Long id) {
        entityManager.getTransaction().begin();
        VentaDetalleEntity detalle = entityManager.find(VentaDetalleEntity.class, id);
        if (detalle != null) {
            entityManager.remove(detalle);
        }
        entityManager.getTransaction().commit();
    }
}
