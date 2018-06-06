package de.uniba.dsg.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 *
 * Release attributes should be
 * - title:String
 * - artist:String (possibly multiple artists concatenated with ", ")
 */
@XmlRootElement(name = "Release")
@XmlType(propOrder = {"title", "artist" }, namespace = "http://jaxws.dsg.uniba.de/")

public class Release {
    private String title;
    private List<String> artist;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getArtist() {
        return artist;
    }

    public void setArtist(List<String> artist) {
        this.artist = artist;
    }
}
