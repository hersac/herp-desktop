package com.hersac.core.modules.usuarios.entities.repositories.impl;

import com.hersac.core.modules.usuarios.entities.UsuarioEntity;
import com.hersac.core.modules.usuarios.entities.repositories.UsuarioRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class UsuarioRepositoryImpl implements UsuarioRepository {

    private final EntityManager entityManager;

    public UsuarioRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<UsuarioEntity> buscarTodos() {
        return entityManager.createQuery("FROM UsuarioEntity", UsuarioEntity.class).getResultList();
    }

    @Override
    public UsuarioEntity buscarPorId(Long id) {
        return entityManager.find(UsuarioEntity.class, id);
    }

    @Override
    public UsuarioEntity crear(UsuarioEntity entidad) {
        entityManager.getTransaction().begin();
        entityManager.persist(entidad);
        entityManager.getTransaction().commit();
        return entidad;
    }

    @Override
    public void actualizar(UsuarioEntity entidad) {
        entityManager.getTransaction().begin();
        entityManager.merge(entidad);
        entityManager.getTransaction().commit();
    }

    @Override
    public void eliminar(Long id) {
        entityManager.getTransaction().begin();
        UsuarioEntity usuario = entityManager.find(UsuarioEntity.class, id);
        if (usuario != null) {
            entityManager.remove(usuario);
        }
        entityManager.getTransaction().commit();
    }

    @Override
    public UsuarioEntity buscarPorCorreo(String correo) {
        List<UsuarioEntity> resultados = entityManager.createQuery(
                "SELECT u FROM UsuarioEntity u WHERE u.correo = :correo", UsuarioEntity.class)
            .setParameter("correo", correo)
            .getResultList();

        return resultados.isEmpty() ? null : resultados.getFirst();
    }
}
