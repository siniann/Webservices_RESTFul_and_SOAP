package de.uniba.dsg.jaxrs.resources;

import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.AlbumRequest;
import com.wrapper.spotify.methods.NewReleasesRequest;
import com.wrapper.spotify.models.*;
import de.uniba.dsg.SpotifyApi;
import de.uniba.dsg.interfaces.AlbumApi;
import de.uniba.dsg.jaxrs.exceptions.ClientRequestException;
import de.uniba.dsg.jaxrs.exceptions.NoContentException;
import de.uniba.dsg.jaxrs.exceptions.RemoteApiException;
import de.uniba.dsg.jaxrs.exceptions.ResourceNotFoundException;
import de.uniba.dsg.models.ErrorMessage;
import de.uniba.dsg.models.Release;

import javax.swing.border.TitledBorder;
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

        NewReleasesRequest newReleasesRequest;
        //creating request based on available parameters
        //both parameters missing
        if (country == null && size == 0) {
            newReleasesRequest = SpotifyApi.getInstance().getNewReleases().build();
        }
        //Only size parameter available
        else if (country == null && size != 0) {
            if (size < 0) {
                LOGGER.log(Level.SEVERE, "Invalid size.Please provide a size greater than 0.");
                throw new ClientRequestException(new ErrorMessage("Invalid size : " + size));
            }
            newReleasesRequest = SpotifyApi.getInstance().getNewReleases().limit(size).build();
        }
        //Only country parameter available
        else if (country != null && size == 0) {
            newReleasesRequest = SpotifyApi.getInstance().getNewReleases().country(country).build();

        }
        //both parameter available
        else {
            if (country.length() != 2) {
                LOGGER.severe("Invalid country.Please try 'DE','IN' etc.");
                throw new ClientRequestException(new ErrorMessage("Invalid country : " + country));
            }
            if (size < 0) {
                LOGGER.log(Level.SEVERE, "Invalid size. Only size greater than 0 is acceptable.");
                throw new ClientRequestException(new ErrorMessage("Invalid size : " + size));
            }
            newReleasesRequest = SpotifyApi.getInstance().getNewReleases().country(country).limit(size).build();
        }

        try {
            //getting the  releases
            NewReleases releasesResult = newReleasesRequest.get();

            if (releasesResult == null) {
                LOGGER.log(Level.SEVERE, "There are no new releases related to the search.");
                throw new NoContentException(new ErrorMessage("Request is valid,but unfortunately there are no new releases related to the search. "));
            }
            //getting albums from  releases
            Page<SimpleAlbum> albums = releasesResult.getAlbums();
            //getting Simple albums from albums
            List<SimpleAlbum> simpleAlbum = albums.getItems();

            String id = "";
            AlbumRequest albumRequest = null;
            List<Release> releaseList = new ArrayList<>();
            //getting albums from List of SimpleAlbum
            for (SimpleAlbum s : simpleAlbum) {
                //creating Release model
                Release release = new Release();
                release.setTitle(s.getName());

                //getting artists from album
                id = s.getId();
                albumRequest = SpotifyApi.getInstance().getAlbum(id).build();
                List<SimpleArtist> simpleArtist = albumRequest.get().getArtists();

                if (simpleArtist.isEmpty()) {
                    LOGGER.log(Level.SEVERE, "There is no artists for album ");
                    throw new ResourceNotFoundException(new ErrorMessage("There is no artists for album with id: " + id));
                }
                List<String> artists = new ArrayList<String>();
                //getting artist names
                for (SimpleArtist sa : simpleArtist) {
                    artists.add(sa.getName());
                }

                release.setArtist(artists);
                releaseList.add(release);
            }

            return releaseList;

        } catch (WebApiException | IOException e) {
            LOGGER.log(Level.SEVERE, "WebApiException or IO Exception occurred");
            throw new RemoteApiException(new ErrorMessage(e.getMessage()));
        }
    }
}
