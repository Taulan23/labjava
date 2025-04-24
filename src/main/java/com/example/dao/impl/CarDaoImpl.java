package com.example.dao.impl;

import com.example.dao.CarDao;
import com.example.model.Car;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@ApplicationScoped
public class CarDaoImpl extends GenericDaoImpl<Car> implements CarDao {

    @Override
    public List<Car> findAvailable() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Car> cq = cb.createQuery(Car.class);
        Root<Car> root = cq.from(Car.class);
        cq.select(root).where(cb.equal(root.get("available"), true));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public Car findBySerialNumber(String serialNumber) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Car> cq = cb.createQuery(Car.class);
            Root<Car> root = cq.from(Car.class);
            cq.select(root).where(cb.equal(root.get("serialNumber"), serialNumber));
            TypedQuery<Car> query = em.createQuery(cq);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
} 