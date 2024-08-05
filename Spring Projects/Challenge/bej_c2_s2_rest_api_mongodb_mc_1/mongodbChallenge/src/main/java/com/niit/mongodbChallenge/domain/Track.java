package com.niit.mongodbChallenge.domain;



import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document

public class Track {
    @Id
    int trackId;
    String trackName;
    Float trackRating;
    String trackComments;
    Artist trackArtist;


    public Track(){

    }

    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public Float getTrackRating() {
        return trackRating;
    }

    public void setTrackRating(Float trackRating) {
        this.trackRating = trackRating;
    }

    public String getTrackComments() {
        return trackComments;
    }

    public void setTrackComments(String trackComments) {
        this.trackComments = trackComments;
    }

    public Artist getTrackArtist() {
        return trackArtist;
    }

    public void setTrackArtist(Artist trackArtist) {
        this.trackArtist = trackArtist;
    }
}
