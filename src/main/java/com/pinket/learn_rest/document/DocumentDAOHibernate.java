package com.pinket.learn_rest.document;

import com.pinket.exceptions.ConflictException;
import com.pinket.exceptions.DBException;
import com.pinket.exceptions.NotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;

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

    public static Document saveDocument(Document document) throws DBException, ConflictException {
        if (document.getId() != null && getDocuments(document.getId()) != null) {
            throw new ConflictException();
        }
        if (document.getId() == null) {
            Long id;
            do {
                id = abs(new Random().nextLong());
            } while (getDocuments(id) != null);
            document.setId(id);
        }
        configureFactory();
        try (Session session = factory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(document);
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

    public static Document updateDocument(Document document) throws DBException, NotFoundException {
        Document dbDocument = getDocuments(document.getId());
        if (dbDocument == null) {
            throw new NotFoundException();
        }
        if (document.getName() != null) {
            dbDocument.setName(document.getName());
        }
        if (document.getContent() != null) {
            dbDocument.setContent(document.getContent());
        }
        configureFactory();
        try (Session session = factory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.update(dbDocument);
                transaction.commit();
                return dbDocument;
            } catch (Throwable t) {
                try {
                    transaction.rollback();
                } catch (Throwable ignored) {
                }
                throw new DBException(t);
            }
        }
    }

    public static Document saveOrUpdateDocument(Document document) throws DBException, ConflictException {
        if (document.getId() == null) {
            return saveDocument(document);
        }
        configureFactory();
        try (Session session = factory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.saveOrUpdate(document);
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

    public static Document deleteDocument(Long id) throws DBException {
        configureFactory();
        try (Session session = factory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                Document document = session.get(Document.class, id);
                session.delete(document);
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

    public static Document appendDocumentContent(Long id, String content) throws DBException, NotFoundException {
        Document document = getDocuments(id);
        if (document == null) {
            throw new NotFoundException();
        }
        configureFactory();
        try (Session session = factory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                document.setContent(document.getContent() + content);
                session.update(document);
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
}
