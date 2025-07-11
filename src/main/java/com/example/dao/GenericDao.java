package com.example.dao;

import java.util.List;

public interface GenericDao<T> {
    T findById(Long id);
    List<T> findAll();
    void save(T entity);
    void update(T entity);
    void delete(T entity);
    void deleteById(Long id);
} 