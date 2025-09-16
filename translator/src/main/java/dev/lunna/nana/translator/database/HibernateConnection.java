package dev.lunna.nana.translator.database;

import jakarta.inject.Singleton;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class HibernateConnection {
    private static final Logger log = LoggerFactory.getLogger(HibernateConnection.class);
    private SessionFactory sessionFactory;

    public void initialize() {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
            log.info("Hibernate SessionFactory initialized");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Failed to initialize Hibernate SessionFactory", e);
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
