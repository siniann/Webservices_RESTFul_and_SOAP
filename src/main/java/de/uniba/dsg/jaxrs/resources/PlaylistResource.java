package de.uniba.dsg.jaxrs.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import de.uniba.dsg.interfaces.PlaylistApi;
import de.uniba.dsg.jaxrs.exceptions.ClientRequestException;
import de.uniba.dsg.jaxrs.exceptions.RemoteApiException;
import de.uniba.dsg.models.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;


/**
 * User: sini_ann
 * Date: 11/05/18 4:08 PM
 */
@Path("playlists")
public class PlaylistResource implements PlaylistApi {

    private static final Logger LOGGER = Logger.getLogger(PlaylistResource.class.getName());

    /**
     * TODO:
     * Method should create a new playlist based on a PlaylistRequest modeled by the playlist model class
     * Method should be available at /playlists
     * Specify the name of a new playlist via the title attribute of a PlaylistRequest (mandatory)
     * Trigger an automatic creation of a playlist via a collection of artists 'artistSeeds' and an optional query parameter, i.e., 'numberOfSongs'
     * 'artistSeeds' are Spotify artist ids, e.g., 3WrFJ7ztbogyGnTHbHJFl2
     * Empty or unknown artistSeeds should be ignored as long as valid seeds exist
     * Default size for an automatic playlist should be 10
     * Signal a bad request for no request body or invalid configurations (according to aforementioned rules)
     * Handle requests with invalid artistSeeds and numberOfSongs attributes correctly, i.e., signal a bad request
     * Return a status code of 201 upon the creation of a new playlist together with the new playlist entity
     *
     * @param request
     */

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response createPlaylist(PlaylistRequest request) throws ClientRequestException {
        try {
            //handling invalid parameters
            if (request.getTitle() == null || request.getTitle().isEmpty()) {
                LOGGER.log(Level.SEVERE, "Required title  parameter is missing.");
                throw new ClientRequestException(new ErrorMessage("Required title  parameter is missing."));
            }
            if (request.getNumberOfSongs() < 0) {
                LOGGER.log(Level.SEVERE, "Invalid size:"+request.getNumberOfSongs());
                throw new ClientRequestException(new ErrorMessage("Invalis size."+request.getNumberOfSongs()));

            }

            Playlist temp_list = new Playlist();
            List<Song> songs = new ArrayList<>();
            int limit = 0, size = 0;

            //handling maximum request size
            if (request.getNumberOfSongs() >= 50) {
                LOGGER.log(Level.SEVERE, "Number of Songs should be less than 50.");
                throw new ClientRequestException(new ErrorMessage("Number of Songs should be less than 50."));
            }
            if (request.getNumberOfSongs() != 0) {
                limit = request.getNumberOfSongs();
            } else {
                limit = 10;
            }
            //resize playlist size for request with more seeds
            if (request.getArtistSeeds().size() > request.getNumberOfSongs()) {
                limit = request.getArtistSeeds().size();
            }
            ArtistResource artistResource = new ArtistResource();

            for (String id : request.getArtistSeeds()) {
                songs.add(artistResource.getTopTracks(id).get(0));
                size++;
                if (size >= limit)
                    break;
            }
            int index = 0;
            Interpret similarArtist;
            for (int i = size; i < limit; i++) {
                index = (i % (request.getArtistSeeds().size()));
                similarArtist = artistResource.getSimilarArtist(request.getArtistSeeds().get(index));
                songs.add(artistResource.getTopTracks(similarArtist.getId()).get(0));

            }
            //creating Playlist model
            temp_list.setTracks(songs);
            temp_list.setSize(temp_list.getTracks().size());
            temp_list.setTitle(request.getTitle());

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = "error";
            try {
                //creating JSON for playlist
                json = ow.writeValueAsString(temp_list);
            } catch (JsonProcessingException e) {
                LOGGER.log(Level.SEVERE, "Some JSON processing error occurred!");
            }
            return Response.status(201).entity(json).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Some error occurred!");
            throw new RemoteApiException(new ErrorMessage(e.getMessage()));
        }
    }
}

