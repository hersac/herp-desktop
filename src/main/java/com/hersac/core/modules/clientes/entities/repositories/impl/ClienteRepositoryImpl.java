package com.hersac.core.modules.clientes.entities.repositories.impl;

import com.hersac.core.modules.clientes.entities.ClienteEntity;
import com.hersac.core.modules.clientes.entities.repositories.ClienteRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ClienteRepositoryImpl implements ClienteRepository {
    private final EntityManager entityManager;

    public ClienteRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<ClienteEntity> buscarTodos() {
        return entityManager.createQuery("FROM ClienteEntity", ClienteEntity.class).getResultList();
    }

    @Override
    public ClienteEntity buscarPorId(Long id) {
        return entityManager.find(ClienteEntity.class, id);
    }

    @Override
    public ClienteEntity crear(ClienteEntity entidad) {
        entityManager.getTransaction().begin();
        entityManager.persist(entidad);
        entityManager.getTransaction().commit();
        return entidad;
    }

    @Override
    public void actualizar(ClienteEntity entidad) {
        entityManager.getTransaction().begin();
        entityManager.merge(entidad);
        entityManager.getTransaction().commit();
    }

    @Override
    public void eliminar(Long id) {
        entityManager.getTransaction().begin();
        ClienteEntity cliente = entityManager.find(ClienteEntity.class, id);
        if (cliente != null) {
            entityManager.remove(cliente);
        }
        entityManager.getTransaction().commit();
    }
}
