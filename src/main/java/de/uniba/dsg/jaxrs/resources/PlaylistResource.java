package de.uniba.dsg.jaxrs.resources;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.wrapper.spotify.methods.PlaylistCreationRequest;
import de.uniba.dsg.SpotifyApi;
import de.uniba.dsg.interfaces.PlaylistApi;
import de.uniba.dsg.jaxrs.exceptions.ClientRequestException;
import de.uniba.dsg.jaxws.MusicApiImpl;
import de.uniba.dsg.models.*;


import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response createPlaylist(PlaylistRequest request) throws ClientRequestException {
        String title = request.getTitle();
        int size = request.getNumberOfSongs();
        List<String> seeds = request.getArtistSeeds();
        System.out.println("...........");


        if (title == null || title.isEmpty()) {
            LOGGER.log(Level.SEVERE, "Title attribute missing in the request");
            throw new ClientRequestException(new ErrorMessage("Please specify the 'title' in the request"));
        }
        if (size < 0) {
            LOGGER.log(Level.SEVERE, "Invalid size attribute.");
            throw new ClientRequestException(new ErrorMessage("Invalid size attribute."));
        }
        if (size == 0) {
            size = 10;
        }

        List<String> validSeeds = null;
        //selecting valid seeds
        for (String seed : seeds) {
            System.out.println(seed);

            if (seed.isEmpty() || seed == null) {
                LOGGER.log(Level.SEVERE, "if");

            } else {
                LOGGER.log(Level.SEVERE, "else");
                validSeeds.add(seed);
            }
        }
        System.out.println(validSeeds.toString());
        List<Song> songList = null;
        Song toptrack = null;
        //creating playlist
        ArtistResource artistResource = new ArtistResource();
        for (String id : validSeeds) {
            toptrack = artistResource.getTopTracks(id).get(0);
            songList.add(toptrack);

        }
        System.out.println(songList.toString());
        if (size > songList.size()) {
            int moreSongsCount = size - songList.size();
            String similarArtistId = null;
            for (int i = 0; i < moreSongsCount; i++) {
                similarArtistId = artistResource.getSimilarArtist(validSeeds.get(i)).getId();
                toptrack = artistResource.getTopTracks(similarArtistId).get(0);
                songList.add(toptrack);
            }
        }
        System.out.println(songList.toString());

        System.out.println(songList);
        Response response = Response.ok().build();


        return response;
    }
}
