package com.pinket.learn_rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/hello-world")
public class HelloResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Hello hello() {
        return new Hello();
    }

    @GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Hello hello(@PathParam("name") String name) {
        return new Hello(name);
    }
}