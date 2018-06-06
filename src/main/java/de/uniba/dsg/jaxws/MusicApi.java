package de.uniba.dsg.jaxws;

import javax.jws.WebService;

import de.uniba.dsg.interfaces.AlbumApi;
import de.uniba.dsg.interfaces.ArtistApi;
import de.uniba.dsg.interfaces.PlaylistApiSOAP;
import de.uniba.dsg.interfaces.SearchApi;

@WebService
public interface MusicApi extends AlbumApi, ArtistApi, PlaylistApiSOAP, SearchApi {

}
