package de.uniba.dsg.interfaces;

import javax.jws.WebParam;
import javax.ws.rs.core.Response;

import de.uniba.dsg.models.PlaylistRequest;

/**
 *  Implement this interface for JAX-RS ONLY!
 */
public interface PlaylistApi {
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
     */

    Response createPlaylist(@WebParam(name = "playlist-request") PlaylistRequest request) ;
}
