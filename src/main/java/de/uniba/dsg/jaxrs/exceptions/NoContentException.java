package de.uniba.dsg.jaxrs.exceptions;

import de.uniba.dsg.models.ErrorMessage;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * User: sini_ann
 * Date: 14/05/18 11:16 AM
 */
public class NoContentException extends WebApplicationException{
    public NoContentException(ErrorMessage message) { super(Response.status(204).entity(message).build());
        System.out.println("Response code:" +getResponse().getStatus() +"\t Message:" + message.getMessage());
    }

}
