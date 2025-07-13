package com.hersac.core.modules.ventas.entities.repositories.impl;

import com.hersac.core.modules.compras.entities.CompraEntity;
import com.hersac.core.modules.ventas.entities.VentaEntity;
import com.hersac.core.modules.ventas.entities.repositories.VentaRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class VentaRepositoryImpl implements VentaRepository {
    private final EntityManager entityManager;

    public VentaRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<VentaEntity> buscarTodos() {
        return entityManager.createQuery("FROM CompraEntity", VentaEntity.class).getResultList();
    }

    @Override
    public VentaEntity buscarPorId(Long id) {
        return entityManager.find(VentaEntity.class, id);
    }

    @Override
    public VentaEntity crear(VentaEntity entidad) {
        entityManager.getTransaction().begin();
        entityManager.persist(entidad);
        entityManager.getTransaction().commit();
        return entidad;
    }

    @Override
    public void actualizar(VentaEntity entidad) {
        entityManager.getTransaction().begin();
        entityManager.merge(entidad);
        entityManager.getTransaction().commit();
    }

    @Override
    public void eliminar(Long id) {
        entityManager.getTransaction().begin();
        VentaEntity venta = entityManager.find(VentaEntity.class, id);
        if (venta != null) {
            entityManager.remove(venta);
        }
        entityManager.getTransaction().commit();
    }
}
