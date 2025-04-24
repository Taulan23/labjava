package com.example.dao.impl;

import com.example.dao.RentalDao;
import com.example.model.Rental;
import com.example.model.User;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@ApplicationScoped
public class RentalDaoImpl extends GenericDaoImpl<Rental> implements RentalDao {

    @Override
    public List<Rental> findActiveRentals() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Rental> cq = cb.createQuery(Rental.class);
        Root<Rental> root = cq.from(Rental.class);
        cq.select(root).where(cb.equal(root.get("active"), true));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public List<Rental> findActiveRentalsByClient(User client) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Rental> cq = cb.createQuery(Rental.class);
        Root<Rental> root = cq.from(Rental.class);
        cq.select(root).where(
                cb.and(
                    cb.equal(root.get("client"), client),
                    cb.equal(root.get("active"), true)
                )
        );
        return em.createQuery(cq).getResultList();
    }
} 