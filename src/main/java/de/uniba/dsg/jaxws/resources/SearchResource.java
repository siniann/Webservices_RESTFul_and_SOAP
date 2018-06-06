package de.uniba.dsg.jaxws.resources;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.uniba.dsg.interfaces.SearchApi;
import de.uniba.dsg.jaxws.MusicApiImpl;
import de.uniba.dsg.jaxws.exceptions.MusicRecommenderFault;
import de.uniba.dsg.models.ErrorMessage;
import de.uniba.dsg.models.Interpret;

public class SearchResource implements SearchApi {

	@Override
	public Interpret searchArtist(String artistName) {
	    Client client = ClientBuilder.newClient();
		Response response = client.target(MusicApiImpl.restServerUri)
				.path("/search").queryParam("artist", artistName)
				.request(MediaType.APPLICATION_JSON_TYPE)
				.get();
		System.out.println(response.getStatus());
		if(artistName== null || artistName.isEmpty()){
			String cause = response.readEntity(ErrorMessage.class).getMessage();
			throw new MusicRecommenderFault("Bad request format", cause);

		}


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
}
