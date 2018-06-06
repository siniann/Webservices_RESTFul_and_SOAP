package de.uniba.dsg.interfaces;

import de.uniba.dsg.models.Interpret;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;


public interface SearchApi {
    /**
     * Method does return an artist modeled by the interpret model class
     * Method is available at /search and takes a mandatory query parameter artist=name_of_the_artist_to_look_for
     * Throws an error if the query parameter is not provided by the request
     * Throws an error if no artist can be found
     * Throws an error if the connection or the remote server reports an internal error
     */
    @WebMethod
    @WebResult(name = "interpret")
    Interpret searchArtist(@WebParam(name = "artist") String artistName);
}
