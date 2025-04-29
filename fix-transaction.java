package com.example.dao.impl;

import com.example.dao.GenericDao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public abstract class GenericDaoImpl<T> implements GenericDao<T> {
    @Inject
    protected EntityManager em;

    private final Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public GenericDaoImpl() {
        Class<?> clazz = getClass();
        Type genericSuperclass;
        
        do {
            genericSuperclass = clazz.getGenericSuperclass();
            clazz = clazz.getSuperclass();
        } while (!(genericSuperclass instanceof ParameterizedType) && clazz != null);
        
        if (genericSuperclass instanceof ParameterizedType) {
            this.entityClass = (Class<T>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        } else {
            throw new IllegalStateException("Не удалось определить тип сущности для DAO класса: " + getClass().getName());
        }
    }

    @Override
    public T findById(Long id) {
        return em.find(entityClass, id);
    }

    @Override
    public List<T> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.select(root);
        return em.createQuery(cq).getResultList();
    }

    @Override
    @Transactional
    public void save(T entity) {
        em.persist(entity);
    }

    @Override
    @Transactional
    public void update(T entity) {
        em.merge(entity);
    }

    @Override
    @Transactional
    public void delete(T entity) {
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        T entity = findById(id);
        if (entity != null) {
            delete(entity);
        }
    }
} 