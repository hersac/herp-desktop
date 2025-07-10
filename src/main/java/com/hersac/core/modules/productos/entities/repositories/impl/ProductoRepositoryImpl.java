package com.hersac.core.modules.productos.entities.repositories.impl;

import com.hersac.core.modules.productos.entities.ProductoEntity;
import com.hersac.core.modules.productos.entities.repositories.ProductoRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ProductoRepositoryImpl implements ProductoRepository {
    private final EntityManager entityManager;

    public ProductoRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<ProductoEntity> buscarTodos() {
        return entityManager.createQuery("FROM ProductoEntity", ProductoEntity.class).getResultList();
    }

    @Override
    public ProductoEntity buscarPorId(Long id) {
        return entityManager.find(ProductoEntity.class, id);
    }

    @Override
    public ProductoEntity crear(ProductoEntity entidad) {
        entityManager.getTransaction().begin();
        entityManager.persist(entidad);
        entityManager.getTransaction().commit();
        return entidad;
    }

    @Override
    public void actualizar(ProductoEntity entidad) {
        entityManager.getTransaction().begin();
        entityManager.merge(entidad);
        entityManager.getTransaction().commit();
    }

    @Override
    public void eliminar(Long id) {
        entityManager.getTransaction().begin();
        ProductoEntity producto = entityManager.find(ProductoEntity.class, id);
        if (producto != null) {
            entityManager.remove(producto);
        }
        entityManager.getTransaction().commit();
    }

    @Override
    public ProductoEntity buscarPorCodigo(String codigo) {
        List<ProductoEntity> resultados = entityManager.createQuery(
                "SELECT p FROM ProductoEntity p WHERE p.codigo = :codigo", ProductoEntity.class)
            .setParameter("codigo", codigo)
            .getResultList();
        return resultados.isEmpty() ? null : resultados.get(0);
    }
}
