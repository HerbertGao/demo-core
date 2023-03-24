package com.herbertgao.demo.core.b.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/demoB")
public interface DemoBService {

    @GET
    @Path("/{name}")
    String sayHello(@PathParam("name") String name);

}
