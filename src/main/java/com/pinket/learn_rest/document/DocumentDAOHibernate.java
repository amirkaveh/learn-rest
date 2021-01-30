package com.pinket.learn_rest.document;

import com.pinket.exceptions.DBException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class DocumentDAOHibernate {
    private static SessionFactory factory;

    private static void configureFactory() throws DBException {
        if (factory == null || !factory.isOpen()) {
            try {
                factory = new Configuration().configure().buildSessionFactory();
            } catch (Throwable ex) {
                throw new DBException(ex);
            }
        }
    }

    public static Document getDocuments(Long id) throws DBException {
        configureFactory();
        try (Session session = factory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                Document document = session.get(Document.class, id);
                transaction.commit();
                return document;
            } catch (Throwable t) {
                try {
                    transaction.rollback();
                } catch (Throwable ignored) {
                }
                throw new DBException(t);
            }
        }
    }

    public static List<Document> getDocuments() throws DBException {
        configureFactory();
        try (Session session = factory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                List result = session.createQuery("from Document").list();
                transaction.commit();
                return (List<Document>) result;
            } catch (Throwable t) {
                try {
                    transaction.rollback();
                } catch (Throwable ignored) {
                }
                throw new DBException(t);
            }
        }
    }
}
