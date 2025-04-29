package com.example.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.logging.Logger;

@ApplicationScoped
public class EntityManagerProducer {
    private static final Logger logger = Logger.getLogger(EntityManagerProducer.class.getName());
    private static final EntityManagerFactory emf = 
            Persistence.createEntityManagerFactory("carRentalPU");

    @Produces
    @RequestScoped
    public EntityManager createEntityManager() {
        logger.info("Создание нового EntityManager");
        EntityManager em = emf.createEntityManager();
        logger.info("EntityManager создан: " + em);
        return em;
    }

    public void closeEntityManager(@Disposes EntityManager em) {
        logger.info("Закрытие EntityManager: " + em);
        if (em.isOpen()) {
            em.close();
            logger.info("EntityManager закрыт");
        } else {
            logger.warning("EntityManager уже закрыт");
        }
    }
} 