package com.example.dao.impl;

import com.example.dao.UserDao;
import com.example.model.User;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class UserDaoImpl extends GenericDaoImpl<User> implements UserDao {
    private static final Logger logger = Logger.getLogger(UserDaoImpl.class.getName());

    @Override
    public User findByEmail(String email) {
        try {
            logger.info("Поиск пользователя по email: " + email);
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            User user = query.getSingleResult();
            logger.info("Найден пользователь: " + (user != null ? user.getEmail() : "null"));
            return user;
        } catch (NoResultException e) {
            logger.warning("Пользователь с email " + email + " не найден");
            return null;
        } catch (Exception e) {
            logger.severe("Ошибка при поиске пользователя: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<User> findByRole(String role) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> root = cq.from(User.class);
            cq.select(root).where(cb.equal(root.get("role"), role));
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            logger.severe("Ошибка при поиске пользователей по роли: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }
} 