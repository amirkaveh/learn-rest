package com.pinket.learn_rest;

import java.util.List;

public class DocumentMGR {
    public static Document getDocuments(Long id) throws Exception {
        Document document = DocumentDAOJDBC.getDocuments(id);
        if (document == null) {
            throw new Exception("Not Found");
        }
        return document;
    }

    public static List<Document> getDocuments() throws Exception {
        return DocumentDAOJDBC.getDocuments();
    }

    public static Document saveDocument(Document document) throws Exception {
        return DocumentDAOJDBC.saveDocument(document);
    }

    public static Document updateDocument(Document document) throws Exception {
        return DocumentDAOJDBC.updateDocument(document);
    }

    public static Document deleteDocument(Long id) throws Exception {
        return DocumentDAOJDBC.deleteDocument(id);
    }

    public static Document appendDocumentContent(Long id, DocumentContentDTO contentDTO) throws Exception {
        Document document = getDocuments(id);
        document.setContent(document.getContent() + contentDTO.getContent());
        return updateDocument(document);
    }

}
