package com.pinket.learn_rest.document;

import com.pinket.exceptions.ConflictException;
import com.pinket.exceptions.DBException;
import com.pinket.exceptions.NotFoundException;

import java.util.List;

public class DocumentMGR {
    public static Document getDocuments(Long id) throws DBException, NotFoundException {
        Document document = DocumentDAOHibernate.getDocuments(id);
        if (document == null) {
            throw new NotFoundException();
        }
        return document;
    }

    public static List<Document> getDocuments() throws DBException {
        return DocumentDAOHibernate.getDocuments();
    }

    public static Document saveDocument(Document document) throws DBException, ConflictException {
        return DocumentDAOJDBC.saveDocument(document);
    }

    public static Document updateDocument(Document document) throws DBException, NotFoundException {
        return DocumentDAOJDBC.updateDocument(document);
    }

    public static Document deleteDocument(Long id) throws DBException {
        return DocumentDAOJDBC.deleteDocument(id);
    }

    public static Document appendDocumentContent(Long id, DocumentContentDTO contentDTO) throws DBException, NotFoundException {
        return DocumentDAOJDBC.appendDocumentContent(id, contentDTO.getContent());
    }

}
