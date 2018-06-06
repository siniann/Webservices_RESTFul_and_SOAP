package de.uniba.dsg.jaxrs;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import de.uniba.dsg.jaxrs.resources.AlbumResource;
import de.uniba.dsg.jaxrs.resources.ArtistResource;
import de.uniba.dsg.jaxrs.resources.PlaylistResource;
import de.uniba.dsg.jaxrs.resources.SearchResource;

@ApplicationPath("/")
/**
 * TODO:
 * The API should always consume JSON
 * The API should always respond with JSON
 */

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MusicApi extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(SearchResource.class);
        resources.add(ArtistResource.class);
        resources.add(PlaylistResource.class);
        resources.add(AlbumResource.class);

        return resources;
    }
}
