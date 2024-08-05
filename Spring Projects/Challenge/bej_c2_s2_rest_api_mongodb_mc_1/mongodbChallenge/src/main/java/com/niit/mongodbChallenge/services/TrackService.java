package com.niit.mongodbChallenge.services;

import com.niit.mongodbChallenge.domain.Track;

import java.util.List;

public interface TrackService {
    Track saveTrack(Track track);
    List<Track> getAllTracks();
    Track updateTrack(Track track, int trackId);
    boolean deleteTrackById(int trackId);
    List<Track> getTrackByTrackArtistName(String artistName);
    List<Track> getTrackByTrackId(int trackId);
    List<Track> getTrackByTrackRating(float trackRating);

}
