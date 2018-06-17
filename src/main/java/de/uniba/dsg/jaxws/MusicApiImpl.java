package de.uniba.dsg.jaxws;

import java.net.*;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.jws.WebService;

import de.uniba.dsg.Configuration;

import de.uniba.dsg.jaxws.resources.AlbumResourceWS;
import de.uniba.dsg.jaxws.resources.ArtistResourseWS;
import de.uniba.dsg.jaxws.resources.PlaylistSoapResource;
import de.uniba.dsg.jaxws.resources.SearchResource;
import de.uniba.dsg.models.Interpret;
import de.uniba.dsg.models.Playlist;
import de.uniba.dsg.models.PlaylistRequest;
import de.uniba.dsg.models.Release;
import de.uniba.dsg.models.Song;

@WebService(endpointInterface = "de.uniba.dsg.jaxws.MusicApi")
public class MusicApiImpl implements MusicApi {
    public static URI restServerUri;

    private static final Logger LOGGER = Logger.getLogger(MusicApiImpl.class.getName());

    static {
        Properties properties = Configuration.loadProperties();
        try {
            restServerUri = new URI(properties.getProperty("restServerUri"));
        } catch (URISyntaxException e) {
            LOGGER.severe("Invalid URI for RESTful web service");
        }
    }

    @Override
    public Interpret searchArtist(String artistName) {
        return new SearchResource().searchArtist(artistName);
    }

    @Override
    public Interpret getArtist(String artistId) {
        return new ArtistResourseWS().getArtist(artistId);
    }

    @Override
    public List<Song> getTopTracks(String artistId) {
        return new ArtistResourseWS().getTopTracks(artistId);
    }

    @Override
    public Interpret getSimilarArtist(String artistId) {
        return new ArtistResourseWS().getSimilarArtist(artistId);
    }

    @Override
    public List<Release> getNewReleases(String country, int size) {
        return new AlbumResourceWS().getNewReleases(country, size);
    }

    @Override
    public Playlist createPlaylist(PlaylistRequest request) {
        return new PlaylistSoapResource().createPlaylist(request);

    }

}
