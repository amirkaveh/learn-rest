package com.pinket.learn_rest.document;

import com.pinket.exceptions.ConflictException;
import com.pinket.exceptions.DBException;
import com.pinket.exceptions.NotFoundException;
import com.pinket.learn_rest.Credentials;

import java.util.*;
import java.sql.*;

import static java.lang.Math.abs;

public class DocumentDAOJDBC {
    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static final String DB_URL = Credentials.POSTGRES_DB_URL;
    /*
    format:     "jdbc:DATABASE://IP:PORT/DB_NAME"
    example:    "jdbc:postgresql://localhost:5432/example_db"
     */
    private static final String DB_USER = Credentials.POSTGRES_DB_USERNAME;
    private static final String DB_PASS = Credentials.POSTGRES_DB_PASSWORD;
    private static Connection connection = null;

    private static void openConnection() throws DBException {
        if (connection == null) {
            newConnection();
            return;
        }
        try {
            if (connection.isClosed() || !connection.isValid(5)) {
                connection.close();
            }
        } catch (SQLException ignored) {
        } finally {
            newConnection();
        }
    }

    private static void newConnection() throws DBException {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (ClassNotFoundException | SQLException e) {
            throw new DBException(e);
        }
    }

    private static Document makeDocument(ResultSet resultSet) throws DBException {
        Document document = new Document();
        try {
            document.setId(resultSet.getLong("id"));
            document.setName(resultSet.getString("name"));
            document.setContent(resultSet.getString("content"));
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return document;
    }

    public static Document getDocuments(Long id) throws DBException {
        openConnection();
        Document document = null;
        String sqlString = "SELECT id, name, content FROM \"Documents\" WHERE id=" + id + ";";
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlString)
        ) {
            if (resultSet.next()) {
                document = makeDocument(resultSet);
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return document;
    }

    public static List<Document> getDocuments() throws DBException {
        openConnection();
        String sqlString = "SELECT id, name, content FROM \"Documents\";";
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlString)
        ) {
            List<Document> documents = new ArrayList<>();
            while (resultSet.next()) {
                documents.add(makeDocument(resultSet));
            }
            return documents;
        } catch (SQLException e) {
            throw new DBException(e);
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
        openConnection();
        String queryString = "INSERT INTO \"Documents\" (id, name, content) VALUES (" + document.getId() + ",'" + document.getName() + "','" + document.getContent() + "');";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(queryString);
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return document;
    }

    public static Document updateDocument(Document document) throws DBException, NotFoundException {
        Document dbDocument = getDocuments(document.getId());
        if (dbDocument == null) {
            throw new NotFoundException();
        }
        List<String> updates = new ArrayList<>();
        if (document.getName() != null) {
            updates.add("name = '" + document.getName() + "'");
            dbDocument.setName(document.getName());
        }
        if (document.getContent() != null) {
            updates.add("content = '" + document.getContent() + "'");
            dbDocument.setContent(document.getContent());
        }
        if (updates.size() != 0) {
            openConnection();
            StringBuilder queryString = new StringBuilder("UPDATE \"Documents\" SET ");
            for (String update : updates) {
                queryString.append(update).append(", ");
            }
            queryString.setLength(queryString.length() - 2);
            queryString.append(" WHERE id = ").append(document.getId()).append(";");

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(queryString.toString());
            } catch (SQLException e) {
                throw new DBException(e);
            }
        }
        return dbDocument;
    }

    public static Document saveOrUpdateDocument(Document document) throws DBException, NotFoundException, ConflictException {
        if (getDocuments(document.getId()) != null) {
            return updateDocument(document);
        } else return saveDocument(document);
    }

    public static Document deleteDocument(Long id) throws DBException {
        openConnection();
        String queryString = "DELETE FROM \"Documents\" WHERE id = " + id + " RETURNING id, name, content;";
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryString)
        ) {
            if (resultSet.next()) {
                return makeDocument(resultSet);
            }
            return null;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }


}
