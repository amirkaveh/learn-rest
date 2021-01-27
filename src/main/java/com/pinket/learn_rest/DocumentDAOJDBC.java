package com.pinket.learn_rest;

import java.util.*;
import java.sql.*;

import static java.lang.Math.abs;

public class DocumentDAOJDBC {
    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static final String DB_URL = Credentials.POSTGRES_DB_URL;
    private static final String DB_USER = Credentials.POSTGRES_DB_USERNAME;
    private static final String DB_PASS = Credentials.POSTGRES_DB_PASSWORD;
    private static Connection connection = null;

    private static void openConnection() throws Exception {
        try {
            if (connection == null) {
                newConnection();
            } else if (connection.isClosed() || !connection.isValid(5)) {
                connection.close();
                newConnection();
            }
        } catch (SQLException e) {
            newConnection();
        }
    }

    private static void newConnection() throws Exception {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (ClassNotFoundException | SQLException e) {
            throw new Exception("DB Problem: " + e.getMessage());
        }
    }

    private static Document makeDocument(ResultSet resultSet) throws Exception {
        Document document = new Document();
        try {
            document.setId(resultSet.getLong("id"));
            document.setName(resultSet.getString("name"));
            document.setContent(resultSet.getString("content"));
        } catch (SQLException e) {
            throw new Exception("DB Problem: " + e.getMessage());
        }
        return document;
    }

    public static Document getDocuments(Long id) throws Exception {
        openConnection();
        Document document = null;
        try (Statement statement = connection.createStatement()){
            String sqlString = "SELECT id, name, content FROM \"Documents\" WHERE id=" + id + ";";
            ResultSet resultSet = statement.executeQuery(sqlString);
            if (resultSet.next()) {
                document = makeDocument(resultSet);
                resultSet.close();
                statement.close();

            }
        } catch (SQLException e) {
            throw new Exception("DB Problem: " + e.getMessage());
        }
        return document;
    }

    public static List<Document> getDocuments() throws Exception {
        openConnection();
        try {
            Statement statement = connection.createStatement();
            String sqlString = "SELECT id, name, content FROM \"Documents\";";
            ResultSet resultSet = statement.executeQuery(sqlString);
            List<Document> documents = new ArrayList<>();
            while (resultSet.next()) {
                documents.add(makeDocument(resultSet));
            }
            resultSet.close();
            statement.close();
            return documents;
        } catch (SQLException e) {
            throw new Exception("DB Problem: " + e.getMessage());
        }
    }

    public static Document saveDocument(Document document) throws Exception {
        if (document.getId() != null && getDocuments(document.getId()) != null) {
            throw new Exception("Conflict with other document");
        }
        if (document.getId() == null) {
            Long id;
            do {
                id = abs(new Random().nextLong());
            } while (getDocuments(id) != null);
            document.setId(id);
        }
        openConnection();
        try {
            Statement statement = connection.createStatement();
            String queryString = "INSERT INTO \"Documents\" (id, name, content) VALUES (" + document.getId() + ",'" + document.getName() + "','" + document.getContent() + "');";
            statement.executeUpdate(queryString);
            statement.close();
        } catch (SQLException e) {
            throw new Exception("DB Problem: " + e.getMessage());
        }
        return document;
    }

    public static Document updateDocument(Document document) throws Exception {
        Document dbDocument = getDocuments(document.getId());
        if (dbDocument == null) {
            throw new Exception("Not Found");
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
            try {
                Statement statement = connection.createStatement();
                StringBuilder queryString = new StringBuilder("UPDATE \"Documents\" SET ");
                for (String update : updates) {
                    queryString.append(update).append(", ");
                }
                queryString.setLength(queryString.length() - 2);
                queryString.append(" WHERE id = ").append(document.getId()).append(";");
                statement.executeUpdate(queryString.toString());
                statement.close();
            } catch (SQLException e) {
                throw new Exception("DB Problem: " + e.getMessage());
            }
        }
        return dbDocument;
    }

    public static Document saveOrUpdateDocument(Document document) throws Exception {
        if (getDocuments(document.getId()) != null) {
            return updateDocument(document);
        }
        else return saveDocument(document);
    }

    public static Document deleteDocument(Long id) throws Exception {
        openConnection();
        Document document = null;
        try {
            Statement statement = connection.createStatement();
            String queryString = "DELETE FROM \"Documents\" WHERE id = " + id + " RETURNING id, name, content;";
            ResultSet resultSet = statement.executeQuery(queryString);
            if (resultSet.next()) {
                document = makeDocument(resultSet);
            }
        } catch (SQLException e) {
            throw new Exception("DB Problem: " + e.getMessage());
        }
        return document;
    }


}
