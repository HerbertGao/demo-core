package com.herbertgao.demo.core.a.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/demoA")
public interface DemoAService {

    @GET
    @Path("/{name}")
    String sayHello(@PathParam("name") String name);

}
