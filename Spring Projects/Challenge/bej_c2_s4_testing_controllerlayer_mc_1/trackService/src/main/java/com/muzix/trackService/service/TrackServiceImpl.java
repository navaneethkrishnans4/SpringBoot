package com.muzix.trackService.service;

import com.muzix.trackService.domain.Track;
import com.muzix.trackService.exception.TrackAlreadyExistsException;
import com.muzix.trackService.exception.TrackNotFoundException;
import com.muzix.trackService.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackServiceImpl implements ITrackService{
    private TrackRepository trackRepository;

    @Autowired
    public TrackServiceImpl(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @Override
    public Track saveTrack(Track track) throws TrackAlreadyExistsException {
        if(trackRepository.findById(track.getTrackId()).isPresent())
        {
            throw new TrackAlreadyExistsException();
        }

        return trackRepository.save(track);
    }

    @Override
    public boolean deleteTrack(int trackId) throws TrackNotFoundException {
        if(trackRepository.findById(trackId).isEmpty()){
            throw new TrackNotFoundException();
        }

        trackRepository.deleteById(trackId);
        return true;
    }

    @Override
    public List<Track> getAllTracks(){
        return trackRepository.findAll();
    }

    @Override
    public List<Track> getAllTracksAboveRating(float rating){
        return trackRepository.getAllTracksAboveRating(rating);
    }

    @Override
    public List<Track> getAllTracksByArtist(String artistName){
        return trackRepository.getAllTracksByArtist(artistName);
    }
}
