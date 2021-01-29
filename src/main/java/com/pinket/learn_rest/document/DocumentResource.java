package com.pinket.learn_rest.document;

import com.pinket.exceptions.ConflictException;
import com.pinket.exceptions.DBException;
import com.pinket.exceptions.NotFoundException;

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
    public List<Document> getDocuments() throws DBException {
        return DocumentMGR.getDocuments();
    }

    @GET
    @Path("{id}")
    public Document getDocuments(@PathParam("id") Long id) throws DBException, NotFoundException {
        return DocumentMGR.getDocuments(id);
    }

    @POST
    public Response createDocument(Document document) throws DBException {
        try {
            DocumentMGR.saveDocument(document);
            return Response.status(Status.CREATED).entity(document).build();
        } catch (ConflictException e) {
            return Response.status(Status.CONFLICT).entity(document).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Document deleteDocument(@PathParam("id") Long id) throws DBException {
        return DocumentMGR.deleteDocument(id);
    }

    @POST
    @Path("{id}/update")
    public Response updateDocument(@PathParam("id") Long id, Document document) throws DBException {
        document.setId(id);
        try {
            document = DocumentMGR.updateDocument(document);
            return Response.status(Status.OK).entity(document).build();
        } catch (NotFoundException e) {
            return Response.status(Status.NOT_FOUND).entity(document).build();
        }
    }
}
