package de.uniba.dsg.jaxrs.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import de.uniba.dsg.models.ErrorMessage;

public class RemoteApiException extends WebApplicationException {
    public RemoteApiException(ErrorMessage message) { super(Response.status(500).entity(message).build());
        System.out.println("Response code:" +getResponse().getStatus() +"\t Message:" + message.getMessage());

    }
}
