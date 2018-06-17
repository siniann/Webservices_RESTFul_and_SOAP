package de.uniba.dsg.jaxrs.resources;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.ArtistSearchRequest;
import com.wrapper.spotify.models.Artist;
import com.wrapper.spotify.models.Page;
import de.uniba.dsg.SpotifyApi;
import de.uniba.dsg.interfaces.SearchApi;
import de.uniba.dsg.jaxrs.exceptions.ClientRequestException;
import de.uniba.dsg.jaxrs.exceptions.ResourceNotFoundException;
import de.uniba.dsg.models.ErrorMessage;
import de.uniba.dsg.models.Interpret;

@Path("search")
public class SearchResource implements SearchApi {

    private static final Logger LOGGER = Logger.getLogger(SearchResource.class.getName());

    @Override
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Interpret searchArtist(@QueryParam("artist") String artistName) throws ClientRequestException, ResourceNotFoundException {

        if (artistName == null || artistName.isEmpty()) {
            LOGGER.log(Level.SEVERE, "Required query parameter is missing: artist");
            throw new ClientRequestException(new ErrorMessage("Required query parameter is missing: artist"));
        }

        ArtistSearchRequest artistRequest = SpotifyApi.getInstance().searchArtists(artistName).limit(1).build();

        try {
            // get search results
            Page<Artist> artistSearchResult = artistRequest.get();
            List<Artist> artists = artistSearchResult.getItems();

            // no artist found
            if (artists.isEmpty()) {
                LOGGER.log(Level.SEVERE, String.format("No matching artist found for query: %s", artistName));
                throw new ResourceNotFoundException(new ErrorMessage(String.format("No matching artist found for query: %s", artistName)));
            }

            Artist artist = artists.get(0);
            Interpret result = new Interpret();
            result.setId(artist.getId());
            result.setName(artist.getName());
            result.setPopularity(artist.getPopularity());
            result.setGenres(artist.getGenres());

            return result;
        } catch (WebApiException | IOException e) {
            LOGGER.log(Level.SEVERE, "Invalid request");

            throw new ClientRequestException(new ErrorMessage("Invalid request"));
        }
    }
}
