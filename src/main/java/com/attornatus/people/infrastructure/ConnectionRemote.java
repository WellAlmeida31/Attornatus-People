package com.attornatus.people.infrastructure;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public interface ConnectionRemote {
    @GET
    @Path("/ws/{cep}/json/")
    @Produces({ MediaType.APPLICATION_JSON})
    Response getCep(@PathParam("cep") String cep);
}
