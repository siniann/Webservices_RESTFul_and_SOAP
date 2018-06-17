package de.uniba.dsg.jaxrs.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wrapper.spotify.exceptions.WebApiException;

import de.uniba.dsg.SpotifyApi;
import de.uniba.dsg.interfaces.AlbumApi;

import de.uniba.dsg.jaxrs.exceptions.ClientRequestException;
import de.uniba.dsg.jaxrs.exceptions.NoContentException;
import de.uniba.dsg.jaxrs.exceptions.ResourceNotFoundException;

import de.uniba.dsg.models.ErrorMessage;
import de.uniba.dsg.models.Release;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: sini_ann
 * Date: 11/05/18 4:58 PM
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("albums")
public class AlbumResource implements AlbumApi {

    private static final Logger LOGGER = Logger.getLogger(AlbumResource.class.getName());

    /**
     * Method should return a collection of new releases modeled by the releases model class
     * Method should be available at /albums/new-releases
     * Specify the country of the new releases and the size of the returned collection via query parameters, i.e., named 'country' and 'size'
     * Both parameters 'country' and 'size' should be optional
     * Handle requests with invalid country and size parameters correctly, i.e., signal a bad request
     *
     * @param country
     * @param size
     */
    @Override
    @GET
    @Path("new-releases")
    public List<Release> getNewReleases(@QueryParam("country") String country, @QueryParam("size") int size) throws ClientRequestException, ResourceNotFoundException, NoContentException {
        List<Release> releases = new ArrayList<>();
        //handling parameters
        try {
            if (country.length() == 0 || country == null)
                //default country
                country = "DE";
        } catch (Exception e) {
            country = "DE";
        }
        if (size < 0) {
            LOGGER.log(Level.SEVERE, "Invalid size!Size should not be negative");
            throw new ClientRequestException(new ErrorMessage("Invalid size!Size should not be negative"));
        }
        if (size > 50) {
            LOGGER.log(Level.SEVERE, "Size limit should be less than 50.");
            throw new ClientRequestException(new ErrorMessage("Size limit should be less than 50."));
        }
        //setting default size
        if (size == 0)
            size = 10;

        try {
            //getting new releases JSON string based on parameters
            String ab = SpotifyApi.getInstance().getNewReleases().country(country).limit(size).build().getJson();
            ObjectMapper mapper = new ObjectMapper();
            //converting to JSON Object
            JsonNode actualObj = mapper.readTree(ab);

            for (int i = 0; i < size; i++) {
                int artistnumber = actualObj.get("albums").get("items").get(i).get("artists").size();
                String artist = "", album;
                //adding artist name
                for (int j = 0; j < artistnumber; j++) {
                    artist = artist + actualObj.get("albums").get("items").get(i).get("artists").get(j).get("name");
                }
                album = String.valueOf(actualObj.get("albums").get("items").get(i).get("name"));
                //temporary release
                Release temp = new Release();
                temp.setArtist(artist);
                temp.setTitle(album);
                //adding to list of Releases
                releases.add(temp);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Some IO exception occurred!Retry!");
            throw new ClientRequestException(new ErrorMessage("Some IO exception occurred!Retry!"));

        } catch (WebApiException e2) {
            LOGGER.log(Level.SEVERE, "Invalid country code! Please provide valid countries like DE,AT,IN...");
            throw new ClientRequestException(new ErrorMessage("Invalid country code! Please provide valid countries like DE,AT,IN..."));
        }
        return releases;
    }
}
