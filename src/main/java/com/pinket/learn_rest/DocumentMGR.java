package com.pinket.learn_rest;

import java.util.List;

public class DocumentMGR {
    public static Document getDocuments(Long id) throws Exception {
        return DocumentDAO.getDocuments(id);
    }

    public static List<Document> getDocuments() {
        return DocumentDAO.getDocuments();
    }

    public static Document saveDocument(Document document) throws Exception {
        return DocumentDAO.saveDocument(document);
    }

    public static Document updateDocument(Document document) throws Exception {
        return DocumentDAO.updateDocument(document);
    }

    public static Document deleteDocument(Long id) {
        return DocumentDAO.deleteDocument(id);
    }

    public static Document appendDocumentContent(Long id, DocumentContentDTO contentDTO) throws Exception {
        Document document = getDocuments(id);
        document.setContent(document.getContent() + contentDTO.getContent());
        return updateDocument(document);
    }

}
