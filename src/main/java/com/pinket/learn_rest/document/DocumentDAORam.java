package com.pinket.learn_rest.document;

import java.util.*;

import static java.lang.Math.abs;

public class DocumentDAORam {
    private static final Map<Long, Document> documentsDB = new HashMap<>();

    public static Document getDocuments(Long id) throws Exception {
        Document document = documentsDB.get(id);
        if (document == null) {
            throw new Exception("Not Found");
        }
        return document;
    }

    public static List<Document> getDocuments() {
        return new ArrayList<>(documentsDB.values());
    }

    public static Document saveDocument(Document document) throws Exception {
        if (document.getId() == null) {
            Long id;
            do {
                id = abs(new Random().nextLong());
            } while (documentsDB.get(id) != null);
            document.setId(id);
            documentsDB.put(id, document);
        }
        else if (documentsDB.get(document.getId()) != null) {
            throw new Exception("Conflict with other document ID");
        }
        else documentsDB.putIfAbsent(document.getId(), document);
        return document;
    }

    public static Document updateDocument(Document document) throws Exception {
        Document dbDocument = documentsDB.get(document.getId());
        if (dbDocument == null) {
            throw new Exception("Not Found");
        }
        if (document.getName() != null) {
            dbDocument.setName(document.getName());
        }
        if (document.getContent() != null) {
            dbDocument.setContent(document.getContent());
        }
        return dbDocument;
    }

    public static Document saveOrUpdateDocument(Document document) throws Exception {
        if (documentsDB.get(document.getId()) != null) {
            return updateDocument(document);
        }
        else return saveDocument(document);
    }

    public static Document deleteDocument(Long id) {
        return documentsDB.remove(id);
    }


}
