package de.uniba.dsg.jaxws.resources;

import com.google.gson.Gson;
import de.uniba.dsg.interfaces.AlbumApi;
import de.uniba.dsg.jaxws.MusicApiImpl;
import de.uniba.dsg.jaxws.exceptions.MusicRecommenderFault;
import de.uniba.dsg.models.ErrorMessage;
import de.uniba.dsg.models.Release;
import de.uniba.dsg.models.Song;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * User: sini_ann
 * Date: 25/05/18 3:50 PM
 */
public class AlbumResourceWS implements AlbumApi {
    /**
     * TODO:
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
    public List<Release> getNewReleases(String country, int size) {
        Client client = ClientBuilder.newClient();

            Response response = client.target(MusicApiImpl.restServerUri)
                    .path("/albums").path("/new-releases").queryParam("country", country).queryParam("size", String.valueOf(size))
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get();

        System.out.println("Response : "+response.getStatus());

        if (response.getStatus() == 200) {
            String jsonString = (response.readEntity(String.class));
            Gson gson = new Gson();
            Release[] releaseArray = gson.fromJson(jsonString, Release[].class);
            int i;
            int arraySize =  releaseArray.length;
            List<Release> releaseList = new ArrayList<>();

            for (i = 0; i < arraySize; i++) {
                Release release = new Release();
                release=releaseArray[i];
                releaseList.add(release);

            }
            return  releaseList;

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
