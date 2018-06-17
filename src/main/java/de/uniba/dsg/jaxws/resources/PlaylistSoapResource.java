package de.uniba.dsg.jaxws.resources;

import de.uniba.dsg.interfaces.PlaylistApiSOAP;
import de.uniba.dsg.jaxrs.exceptions.ClientRequestException;
import de.uniba.dsg.jaxrs.resources.ArtistResource;
import de.uniba.dsg.jaxws.JaxWsServer;
import de.uniba.dsg.jaxws.exceptions.MusicRecommenderFault;
import de.uniba.dsg.models.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.uniba.dsg.jaxws.MusicApiImpl.restServerUri;

/**
 * User: sini_ann
 * Date: 28/05/18 4:11 PM
 */
public class PlaylistSoapResource implements PlaylistApiSOAP {
    private static final Logger LOGGER = Logger.getLogger(PlaylistSoapResource.class.getName());

    /**
     *
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
    @Override
    @POST
    @Consumes("text/xml")
    @Produces("text/xml")
    public Playlist createPlaylist(PlaylistRequest request) {
        //check if REST Server is available
        boolean status = JaxWsServer.isPortInUse(restServerUri);
        if (status == false) {
            throw new MusicRecommenderFault("REST Server unavailable", "Server unavailable");
        }
        //handling invalid parameters
        if (request.getTitle() == null || request.getTitle().isEmpty()) {
            LOGGER.log(Level.SEVERE, "Required title  parameter is missing.");
            throw new ClientRequestException(new ErrorMessage("Required title  parameter is missing."));
        }
        if (request.getNumberOfSongs() < 0) {
            LOGGER.log(Level.SEVERE, "Invalid size.");
            throw new ClientRequestException(new ErrorMessage("Invalis size."));
        }
        Playlist temp_list = new Playlist();
        List<Song> songs = new ArrayList<>();
        int limit = 0, size = 0;
        //handling maximum request size
        if (request.getNumberOfSongs() >= 50) {
            throw new ClientRequestException(new ErrorMessage("Number of Songs should be less than 50"));
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
        //getting top tracks
        for (String id : request.getArtistSeeds()) {
            songs.add(artistResource.getTopTracks(id).get(0));
            size++;
            if (size >= limit)
                break;
        }
        int index = 0;
        Interpret similarArtist;
        //filling playlist with toptracks of similar artists
        for (int i = size; i < limit; i++) {
            index = (i % (request.getArtistSeeds().size()));
            similarArtist = artistResource.getSimilarArtist(request.getArtistSeeds().get(index));
            songs.add(artistResource.getTopTracks(similarArtist.getId()).get(0));

        }
        //creating playlist
        temp_list.setTracks(songs);
        temp_list.setSize(temp_list.getTracks().size());
        temp_list.setTitle(request.getTitle());
        return temp_list;
    }

}

