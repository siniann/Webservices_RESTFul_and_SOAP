package de.uniba.dsg.jaxws.resources;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.uniba.dsg.interfaces.SearchApi;
import de.uniba.dsg.jaxws.JaxWsServer;
import de.uniba.dsg.jaxws.MusicApiImpl;
import de.uniba.dsg.jaxws.exceptions.MusicRecommenderFault;
import de.uniba.dsg.models.ErrorMessage;
import de.uniba.dsg.models.Interpret;





import static de.uniba.dsg.jaxws.MusicApiImpl.restServerUri;

public class SearchResource implements SearchApi {

    @Override
    public Interpret searchArtist(String artistName) {
        //check if REST Server is available
        boolean status = JaxWsServer.isPortInUse(restServerUri);
        if (status == false) {
            throw new MusicRecommenderFault("REST Server unavailable", "Server unavailable");
        }

        Client client = ClientBuilder.newClient();
        Response response = client.target(MusicApiImpl.restServerUri)
                .path("/search").queryParam("artist", artistName)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        if (response.getStatus() == 500) {
            throw new MusicRecommenderFault("Please check if rest server is running!!!", "Server is not available");

        }
        if (artistName == null || artistName.isEmpty()) {
            throw new MusicRecommenderFault("Artist name required!", "Empty/ invalid paramter");
        }

        if (response.getStatus() == 200) {
            Interpret artist = response.readEntity(Interpret.class);
            return artist;
        } else if (response.getStatus() == 400) {
            String cause = response.readEntity(ErrorMessage.class).getMessage();
            throw new MusicRecommenderFault("Please check the artist id!A client side error occurred ", cause);
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
