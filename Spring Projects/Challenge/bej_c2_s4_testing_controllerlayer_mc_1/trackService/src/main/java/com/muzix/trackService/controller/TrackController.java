package com.muzix.trackService.controller;

import com.muzix.trackService.domain.Track;
import com.muzix.trackService.exception.TrackAlreadyExistsException;
import com.muzix.trackService.exception.TrackNotFoundException;
import com.muzix.trackService.service.ITrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TrackController {
    private ITrackService trackService;
    private ResponseEntity responseEntity;

    private ResponseEntity get500Response(Exception ex){
        return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Autowired
    public TrackController(ITrackService trackService) {
        this.trackService = trackService;
    }

    @GetMapping("/tracks")
    public ResponseEntity getAllTracks(){
        try{
            responseEntity = new ResponseEntity(trackService.getAllTracks(), HttpStatus.OK);
        }catch (Exception ex){
            responseEntity = new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

    @PostMapping("/tracks")
    public ResponseEntity saveTrack(@RequestBody Track track) throws TrackAlreadyExistsException{
        try {
            responseEntity = new ResponseEntity(trackService.saveTrack(track), HttpStatus.CREATED);
        }catch (TrackAlreadyExistsException ex){
            throw ex;
        }catch (Exception ex){
            responseEntity = get500Response(ex);
        }

        return responseEntity;
    }

    @DeleteMapping("/tracks/{trackId}")
    public ResponseEntity deleteTrack(@PathVariable("trackId") int trackId) throws TrackNotFoundException {
        try {
            responseEntity = new ResponseEntity(trackService.deleteTrack(trackId), HttpStatus.OK);
        }catch (TrackNotFoundException ex){
            throw ex;
        }catch (Exception ex){
            responseEntity = get500Response(ex);
        }

        return responseEntity;
    }

    @GetMapping("/tracks/minRating={minRating}")
    public ResponseEntity getAllTracksAboveRating(@PathVariable("minRating") float minRating){
        try {
            responseEntity = new ResponseEntity(trackService.getAllTracksAboveRating(minRating), HttpStatus.OK);
        }catch (Exception ex){
            responseEntity = get500Response(ex);
        }

        return responseEntity;
    }

    @GetMapping("/tracks/trackArtist={artistName}")
    public ResponseEntity getAllTracksByArtist(@PathVariable("artistName") String artistName){
        try{
            responseEntity = new ResponseEntity(trackService.getAllTracksByArtist(artistName), HttpStatus.OK);
        }catch (Exception ex){
            responseEntity = get500Response(ex);
        }

        return responseEntity;
    }
}
