package com.hersac.core.modules.items.entities.repositories.impl;

import com.hersac.core.modules.items.entities.ItemEntity;
import com.hersac.core.modules.items.entities.repositories.ItemRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ItemRepositoryImpl implements ItemRepository {
    private final EntityManager entityManager;

    public ItemRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<ItemEntity> buscarTodos() {
        return entityManager.createQuery("FROM ItemEntity", ItemEntity.class).getResultList();
    }

    @Override
    public ItemEntity buscarPorId(Long id) {
        return entityManager.find(ItemEntity.class, id);
    }

    @Override
    public ItemEntity crear(ItemEntity entidad) {
        entityManager.getTransaction().begin();
        entityManager.persist(entidad);
        entityManager.getTransaction().commit();
        return entidad;
    }

    @Override
    public void actualizar(ItemEntity entidad) {
        entityManager.getTransaction().begin();
        entityManager.merge(entidad);
        entityManager.getTransaction().commit();
    }

    @Override
    public void eliminar(Long id) {
        entityManager.getTransaction().begin();
        ItemEntity item = entityManager.find(ItemEntity.class, id);
        if (item != null) {
            entityManager.remove(item);
        }
        entityManager.getTransaction().commit();
    }

    @Override
    public ItemEntity buscarPorCodigo(String codigo) {
        List<ItemEntity> resultados = entityManager.createQuery(
                "SELECT i FROM ItemEntity i WHERE i.codigo = :codigo", ItemEntity.class)
            .setParameter("codigo", codigo)
            .getResultList();
        return resultados.isEmpty() ? null : resultados.get(0);
    }
}
