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
    @GET
    public List<Document> getDocuments() {
        return DocumentMGR.getDocuments();
    }

    @GET
    @Path("{id}")
    public Document getDocuments(@PathParam("id") Long id) throws Exception {
        return DocumentMGR.getDocuments(id);
    }

    @POST
    public Response createDocument(Document document) {
        try {
            DocumentMGR.saveDocument(document);
            return Response.status(Status.CREATED).entity(document).build();
        } catch (Exception e) {
            return Response.status(Status.CONFLICT).entity(document).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Document deleteDocument(@PathParam("id") Long id) {
        return DocumentMGR.deleteDocument(id);
    }

    @POST
    @Path("{id}/update")
    public Response updateDocument(@PathParam("id") Long id, Document document) {
        document.setId(id);
        try {
            DocumentMGR.updateDocument(document);
            return Response.status(Status.OK).entity(document).build();
        } catch (Exception e) {
            return Response.status(Status.NOT_FOUND).entity(document).build();
        }
    }
}
