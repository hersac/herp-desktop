package com.hersac.core.modules.departamentos.entities.repositories.impl;

import java.util.List;

import com.hersac.core.modules.departamentos.entities.DepartamentoEntity;
import com.hersac.core.modules.departamentos.entities.repositories.DepartamentoRepository;

import jakarta.persistence.EntityManager;

public class DepartamentoRepositoryImpl implements DepartamentoRepository {
    private final EntityManager entityManager;

    public DepartamentoRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<DepartamentoEntity> buscarTodos() {
        return entityManager.createQuery("FROM DepartamentoEntity", DepartamentoEntity.class).getResultList();
    }

    @Override
    public DepartamentoEntity buscarPorId(Long id) {
        return entityManager.find(DepartamentoEntity.class, id);
    }

    @Override
    public DepartamentoEntity crear(DepartamentoEntity entidad) {
        entityManager.getTransaction().begin();
        entityManager.persist(entidad);
        entityManager.getTransaction().commit();
        return entidad;
    }

    @Override
    public void actualizar(DepartamentoEntity entidad) {
        entityManager.getTransaction().begin();
        entityManager.merge(entidad);
        entityManager.getTransaction().commit();
    }

    @Override
    public void eliminar(Long id) {
        entityManager.getTransaction().begin();
        DepartamentoEntity departamento = entityManager.find(DepartamentoEntity.class, id);
        if (departamento != null) {
            entityManager.remove(departamento);
        }
        entityManager.getTransaction().commit();
    }
}
