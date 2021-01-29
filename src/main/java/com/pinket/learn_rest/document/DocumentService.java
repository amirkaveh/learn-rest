package com.pinket.learn_rest.document;

import com.pinket.exceptions.DBException;
import com.pinket.exceptions.NotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("documents/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DocumentService {

    @POST
    @Path("{id}/appendContent")
    public Document appendContent(@PathParam("id") Long id, DocumentContentDTO contentDTO) throws DBException, NotFoundException {
        return DocumentMGR.appendDocumentContent(id, contentDTO);
    }
}
