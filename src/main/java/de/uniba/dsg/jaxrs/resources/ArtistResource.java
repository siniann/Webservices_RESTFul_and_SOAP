package de.uniba.dsg.jaxrs.resources;

import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.ArtistRequest;
import com.wrapper.spotify.methods.RelatedArtistsRequest;
import com.wrapper.spotify.methods.TopTracksRequest;

import com.wrapper.spotify.models.Artist;
import com.wrapper.spotify.models.SimpleArtist;
import com.wrapper.spotify.models.Track;

import de.uniba.dsg.SpotifyApi;
import de.uniba.dsg.interfaces.ArtistApi;

import de.uniba.dsg.jaxrs.exceptions.ClientRequestException;
import de.uniba.dsg.jaxrs.exceptions.ResourceNotFoundException;

import de.uniba.dsg.models.ErrorMessage;
import de.uniba.dsg.models.Interpret;
import de.uniba.dsg.models.Song;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: sini_ann
 * Date: 11/05/18 11:25 AM
 */
@Path("artists")
public class ArtistResource implements ArtistApi {

    private static final Logger LOGGER = Logger.getLogger(ArtistResource.class.getName());

    /**
     * Method should return an artist modeled by the interpret model class
     * Method should be available at /artists/[artist-id], e.g., /artists/4gzpq5DPGxSnKTe4SA8HAU
     * Parameter name must be 'artist-id'
     * Handle requests with unknown artist IDs correctly
     *
     * @param artistId
     */
    @Override
    @GET
    @Path("{artist-id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Interpret getArtist(@PathParam("artist-id") String artistId) throws ClientRequestException, ResourceNotFoundException {
        //handling input parameter
        if (artistId == null || artistId.isEmpty()) {
            LOGGER.log(Level.SEVERE, "Required query parameter is missing: artist-id");
            throw new ClientRequestException(new ErrorMessage("Required query parameter is missing: artist-id"));
        }
        //creating request
        ArtistRequest artistIdRequest = SpotifyApi.getInstance().getArtist(artistId).build();

        try {
            //getting artist
            Artist artist = artistIdRequest.get();
            if (artist == null) {
                LOGGER.log(Level.SEVERE, "No matching artist found for query");
                throw new ResourceNotFoundException(new ErrorMessage("No matching artist found for query"));
            }
            // creating Interpret model
            Interpret result = new Interpret();
            result.setId(artist.getId());
            result.setName(artist.getName());
            result.setPopularity(artist.getPopularity());
            result.setGenres(artist.getGenres());

            return result;

        } catch (WebApiException e) {
            LOGGER.log(Level.SEVERE, "Artist not found for this id.");
            throw new ClientRequestException(new ErrorMessage("Artist not found for this id."));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Some error occurred");
            throw new ClientRequestException(new ErrorMessage(e.getMessage()));
        }
    }


    /**
     * Method should return a collection of songs modeled by the song model class
     * Method should be available at /artists/[artist-id]/top-tracks, e.g., /artists/4gzpq5DPGxSnKTe4SA8HAU/top-tracks
     * Parameter name must be 'artist-id'
     * Handle requests with unknown artist IDs correctly
     * Always return top songs for Germany (DE)
     * Size of the track list must be <= 5
     *
     * @param artistId
     */
    @Override
    @GET
    @Path("{artist-id}/top-tracks")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public List<Song> getTopTracks(@PathParam("artist-id") String artistId) throws ClientRequestException, ResourceNotFoundException {

        //handling input parameter
        if (artistId == null || artistId.isEmpty()) {
            LOGGER.log(Level.SEVERE, "Required query parameter is missing: artist-id");
            throw new ClientRequestException(new ErrorMessage("Required query parameter is missing: artist-id"));
        }
        //creating request
        TopTracksRequest topTracksRequest = SpotifyApi.getInstance().getTopTracksForArtist(artistId, "DE").build();

        try {
            //getting top tracks
            List<Track> tracks = topTracksRequest.get();

            if (tracks.isEmpty()) {
                LOGGER.log(Level.SEVERE, "There are no tracks for the artist with this id. "+artistId);
                throw new ResourceNotFoundException(new ErrorMessage("There is no tracks for artist with id: " + artistId));
            }
            //limiting the size of track to 5
            tracks = tracks.subList(0, tracks.size() < 6 ? tracks.size() : 5);

            List<Song> songs = new ArrayList<>();

            for (Track track : tracks) {
                //getting artist details of song
                List<SimpleArtist> simpleArtists = track.getArtists();
                List<String> artists = new ArrayList<String>();
                for (SimpleArtist simpleArtist : simpleArtists) {
                    artists.add(simpleArtist.getName());
                }
                //creating a song model
                Song song = new Song();
                song.setTitle(track.getName());
                song.setArtist(artists);
                //duration in seconds
                song.setDuration(track.getDuration() / 1000.0);
                //adding to List
                songs.add(song);

            }
            return songs;

        } catch (WebApiException | IOException e) {
            LOGGER.log(Level.SEVERE, "Unknown artist Id");
            throw new ClientRequestException(new ErrorMessage("Unknown artist Id"));
        }


    }

    /**
     * Method should return a similar artist modeled by the interpret model class
     * Method should be available at /artists/[artist-id]/similar-artist, e.g., /artists/4gzpq5DPGxSnKTe4SA8HAU/similar-artist
     * Parameter name must be 'artist-id'
     * Subsequent calls should not always return the same similar artist
     * Handle requests with unknown artist IDs correctly
     *
     * @param artistId
     */
    @Override
    @GET
    @Path("{artist-id}/similar-artist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Interpret getSimilarArtist(@PathParam("artist-id") String artistId) throws ClientRequestException, ResourceNotFoundException {
        // handling  input parameter

        try {
            if (artistId == null || artistId.isEmpty()) {
                LOGGER.log(Level.SEVERE, "Required artist-id  parameter is missing.");
                throw new ClientRequestException(new ErrorMessage("Required query parameter is missing: artist-id"));

            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Required artist-id  parameter is missing.");
            throw new ClientRequestException(new ErrorMessage("Required artist-id  parameter is missing."));

        }
        //creating request
        RelatedArtistsRequest relatedArtistsRequest = SpotifyApi.getInstance().getArtistRelatedArtists(artistId).build();
        try {
            List<Artist> similarArtists = relatedArtistsRequest.get();

            if (similarArtists.isEmpty()) {
                LOGGER.log(Level.SEVERE, "There is no similar artists for this artist id.");
                throw new ResourceNotFoundException(new ErrorMessage("There is no similar artists for this artist id."));
            }
            //selecting  a random artist
            Random randomizer = new Random();
            Artist random = similarArtists.get(randomizer.nextInt(similarArtists.size()));

            //creating an interpret model to return
            Interpret result = new Interpret();
            result.setId(random.getId());
            result.setName(random.getName());
            result.setPopularity(random.getPopularity());
            result.setGenres(random.getGenres());

            return result;

        } catch (WebApiException | IOException e) {
            LOGGER.log(Level.SEVERE, "Invalid artist id.Some client side error occurred!");
            throw new ClientRequestException(new ErrorMessage("Invalid artist id.Some client side error occurred!"));
        }

    }

    @GET
    @Path("similar-artist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Interpret getSimilarArtist() {
        LOGGER.log(Level.SEVERE, "Required query parameter is missing: artist-id");
        throw new ClientRequestException(new ErrorMessage("Required query parameter is missing: artist-id"));

    }
}
