package de.uniba.dsg.jaxws.resources;

import com.google.gson.Gson;
import de.uniba.dsg.interfaces.ArtistApi;
import de.uniba.dsg.jaxws.MusicApiImpl;
import de.uniba.dsg.jaxws.exceptions.MusicRecommenderFault;
import de.uniba.dsg.models.ErrorMessage;
import de.uniba.dsg.models.Interpret;
import de.uniba.dsg.models.Song;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;




/**
 * User: sini_ann
 * Date: 25/05/18 12:22 PM
 */
public class ArtistResourseWS implements ArtistApi {
    /**
     *
     * Method should return an artist modeled by the interpret model class
     * Method should be available at /artists/[artist-id], e.g., /artists/4gzpq5DPGxSnKTe4SA8HAU
     * Parameter name must be 'artist-id'
     * Handle requests with unknown artist IDs correctly
     *
     * @param artistId
     */
    @Override
    public Interpret getArtist(String artistId) {

        Client client = ClientBuilder.newClient();
        Response response = client.target(MusicApiImpl.restServerUri)
                .path("/artists").path(artistId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        if(artistId== null || artistId.isEmpty()){
            String cause = response.readEntity(ErrorMessage.class).getMessage();
            throw new MusicRecommenderFault("Bad request format", cause);

        }
        System.out.println("Response code : "+response.getStatus());

        if (response.getStatus() == 200) {
            Interpret artist = response.readEntity(Interpret.class);
            return artist;
        } else if (response.getStatus() == 400) {
            String cause = response.readEntity(ErrorMessage.class).getMessage();
            throw new MusicRecommenderFault("A client side error occurred", cause);
        } else if (response.getStatus() == 404) {
            String cause = response.readEntity(ErrorMessage.class).getMessage();
            throw new MusicRecommenderFault("The requested resource was not found", cause);
        } else if (response.getStatus() == 500) {
            String cause = response.readEntity(ErrorMessage.class).getMessage();
            throw new MusicRecommenderFault("An internal server error occurred", cause);
        } else {
            String cause = response.readEntity(ErrorMessage.class).getMessage();
            throw new MusicRecommenderFault("An unknown remote server error occurred", cause);
        }
    }

    /**
     *
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
    public List<Song> getTopTracks(String artistId) {


        Client client = ClientBuilder.newClient();
        Response response = client.target(MusicApiImpl.restServerUri)
                .path("/artists").path(artistId).path("/top-tracks")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        if(artistId== null || artistId.isEmpty()){
            String cause = response.readEntity(ErrorMessage.class).getMessage();
            throw new MusicRecommenderFault("Bad request format", cause);

        }
        if (response.getStatus() == 200) {
            String jsonString = (response.readEntity(String.class));
            Gson gson = new Gson();
            Song[] songsArray = gson.fromJson(jsonString, Song[].class);
            int i;
            int size =  songsArray.length;
            List<Song> songList = new ArrayList<>();

            for (i=0;i<size;i ++) {
                Song song = new Song();
                song=songsArray[i];
                songList.add(song);

            }
            return  songList;

        }

        else if (response.getStatus() == 400) {
            String cause = response.readEntity(ErrorMessage.class).getMessage();
            throw new MusicRecommenderFault("A client side error occurred", cause);
        } else if (response.getStatus() == 404) {
            String cause = response.readEntity(ErrorMessage.class).getMessage();
            throw new MusicRecommenderFault("The requested resource was not found", cause);
        } else if (response.getStatus() == 500) {
            String cause = response.readEntity(ErrorMessage.class).getMessage();
            throw new MusicRecommenderFault("An internal server error occurred", cause);
        } else {
            String cause = response.readEntity(ErrorMessage.class).getMessage();
            throw new MusicRecommenderFault("An unknown remote server error occurred", cause);
        }
    }

    /**
     *
     * Method should return a similar artist modeled by the interpret model class
     * Method should be available at /artists/[artist-id]/similar-artist, e.g., /artists/4gzpq5DPGxSnKTe4SA8HAU/similar-artist
     * Parameter name must be 'artist-id'
     * Subsequent calls should not always return the same similar artist
     * Handle requests with unknown artist IDs correctly
     *
     * @param artistId
     */
    @Override
    public Interpret getSimilarArtist(String artistId) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(MusicApiImpl.restServerUri)
                .path("/artists").path(artistId).path("/similar-artist")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        System.out.println(response);

        if (response.getStatus() == 200) {
            Interpret artist = response.readEntity(Interpret.class);
            System.out.println(artist);
            return artist;

        }

        else if (response.getStatus() == 400) {
            String cause = response.readEntity(ErrorMessage.class).getMessage();
            throw new MusicRecommenderFault("A client side error occurred", cause);
        } else if (response.getStatus() == 404) {
            String cause = response.readEntity(ErrorMessage.class).getMessage();
            throw new MusicRecommenderFault("The requested resource was not found", cause);
        } else if (response.getStatus() == 500) {
            String cause = response.readEntity(ErrorMessage.class).getMessage();
            throw new MusicRecommenderFault("An internal server error occurred", cause);
        } else {
            String cause = response.readEntity(ErrorMessage.class).getMessage();
            throw new MusicRecommenderFault("An unknown remote server error occurred", cause);
        }
    }
}
