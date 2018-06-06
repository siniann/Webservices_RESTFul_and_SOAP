package de.uniba.dsg.models;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 *
 * Song attributes should be
 * - title:String
 * - artist:String (possibly multiple artists concatenated with ", ")
 * - duration:double (in seconds)
 */
@XmlRootElement(name = "Song")
@XmlType(propOrder = {"title", "artist", "duration" }, namespace = "http://jaxws.dsg.uniba.de/")

public class Song {
    private String title;
    private List<String> artist;
    private double duration;

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

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}