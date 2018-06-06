package de.uniba.dsg.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 *
 * Playlist attributes should be
 * - title:String
 * - size:int
 * - tracks:List<Song>
 */
@XmlRootElement(name = "Playlist")
@XmlType(propOrder = {"title", "size", "tracks" }, namespace = "http://jaxws.dsg.uniba.de/")

public class Playlist {
    private String title;
    private int size;
    private List<Song> tracks;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<Song> getTracks() {
        return tracks;
    }

    public void setTracks(List<Song> tracks) {
        this.tracks = tracks;
    }
}