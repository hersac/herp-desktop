package com.hersac.core.modules.proveedores.entities.repositories.impl;

import com.hersac.core.modules.proveedores.entities.ProveedorEntity;
import com.hersac.core.modules.proveedores.entities.repositories.ProveedorRepository;
import jakarta.persistence.EntityManager;
import java.util.List;

public class ProveedorRepositoryImpl implements ProveedorRepository {
    private final EntityManager entityManager;

    public ProveedorRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<ProveedorEntity> buscarTodos() {
        return entityManager.createQuery("FROM ProveedorEntity", ProveedorEntity.class).getResultList();
    }

    @Override
    public ProveedorEntity buscarPorId(Long id) {
        return entityManager.find(ProveedorEntity.class, id);
    }

    @Override
    public ProveedorEntity crear(ProveedorEntity entidad) {
        entityManager.getTransaction().begin();
        entityManager.persist(entidad);
        entityManager.getTransaction().commit();
        return entidad;
    }

    @Override
    public void actualizar(ProveedorEntity entidad) {
        entityManager.getTransaction().begin();
        entityManager.merge(entidad);
        entityManager.getTransaction().commit();
    }

    @Override
    public void eliminar(Long id) {
        entityManager.getTransaction().begin();
        ProveedorEntity proveedor = entityManager.find(ProveedorEntity.class, id);
        if (proveedor != null) {
            entityManager.remove(proveedor);
        }
        entityManager.getTransaction().commit();
    }

    @Override
    public ProveedorEntity buscarPorCodigo(String codigo) {
        List<ProveedorEntity> resultados = entityManager.createQuery(
                "SELECT p FROM ProveedorEntity p WHERE p.codigo = :codigo", ProveedorEntity.class)
            .setParameter("codigo", codigo)
            .getResultList();
        return resultados.isEmpty() ? null : resultados.get(0);
    }

    @Override
    public ProveedorEntity buscarPorTerceroId(String terceroId) {
        List<ProveedorEntity> resultados = entityManager.createQuery(
                "SELECT p FROM ProveedorEntity p WHERE p.tercero.terceroId = :terceroId", ProveedorEntity.class)
            .setParameter("terceroId", terceroId)
            .getResultList();
        return resultados.isEmpty() ? null : resultados.get(0);
    }
}
