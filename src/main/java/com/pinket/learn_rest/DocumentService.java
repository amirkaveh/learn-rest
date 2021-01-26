package com.pinket.learn_rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("documents/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DocumentService {

    @POST
    @Path("{id}/appendContent")
    public void appendContent(@PathParam("id") Long id, DocumentContentDTO contentDTO) {
        Document document = DocumentResource.getDocumentsDB().get(id);
        document.setContent(document.getContent() + contentDTO.getContent());
    }
}
