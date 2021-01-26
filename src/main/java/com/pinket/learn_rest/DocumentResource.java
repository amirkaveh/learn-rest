package com.pinket.learn_rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.*;

@Path("documents/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DocumentResource {
    private static final Map<Long, Document> documentsDB = new HashMap<>();

    public static Map<Long, Document> getDocumentsDB() {
        return documentsDB;
    }

    @GET
    public List<Document> getDocuments() {
        return new ArrayList<>(DocumentResource.documentsDB.values());
    }

    @GET
    @Path("{id}")
    public Document getDocuments(@PathParam("id") String id) {
        Long longId = Long.parseLong(id);
        return DocumentResource.documentsDB.get(longId);
    }

    @POST
    public Response createDocument(Document document) {
        if(DocumentResource.documentsDB.get(document.getId()) == null) {
            DocumentResource.documentsDB.put(document.getId(), document);
            return Response.status(Status.CREATED).entity(document).build();
        }
        else {
            return Response.status(Status.CONFLICT).entity(document).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Document deleteDocument(@PathParam("id") Long id) {
        return DocumentResource.documentsDB.remove(id);
    }

    @POST
    @Path("{id}/update")
    public Response updateDocument(@PathParam("id") Long id, Document document) {
        document.setId(id);
        if(DocumentResource.documentsDB.get(id) == null) {
            return Response.status(Status.NOT_FOUND).entity(document).build();
        }
        else {
            DocumentResource.documentsDB.replace(id, document);
            return Response.status(Status.OK).entity(document).build();
        }
    }
}
