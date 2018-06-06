package de.uniba.dsg.models;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * TODO:
 * PlaylistRequest attributes should be
 * - title:String
 * - artistSeeds:List<String>, must be serialized as 'seeds'
 * - numberOfSongs:int, must be serialized as 'size'
 */
@XmlRootElement(name = "PlaylistRequest")

public class PlaylistRequest  {
    private String title;
    private List<String> artistSeeds;
    private int numberOfSongs;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @XmlElement(name = "seeds")
    public List<String> getArtistSeeds() {
        return artistSeeds;
    }

    public void setArtistSeeds(List<String> artistSeeds) {
        this.artistSeeds = artistSeeds;
    }

    @XmlElement(name = "size")
    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    public void setNumberOfSongs(int numberOfSongs) {
        this.numberOfSongs = numberOfSongs;
    }
}
