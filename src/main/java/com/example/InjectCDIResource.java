package com.example;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/cdiresource")
public class InjectCDIResource implements Serializable{
	private static final long serialVersionUID = -2588462892904950684L;

	@EJB
	private CDIResource cdiResource;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it! " + cdiResource.getTexto();
    }
}
