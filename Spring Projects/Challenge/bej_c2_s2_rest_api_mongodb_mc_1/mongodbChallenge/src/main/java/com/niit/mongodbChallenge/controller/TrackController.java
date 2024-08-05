    package com.niit.mongodbChallenge.controller;

import com.niit.mongodbChallenge.domain.Track;
import com.niit.mongodbChallenge.services.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TrackController {

    private TrackService trackService;

    @Autowired
    public TrackController(TrackService trackService)
    {
        this.trackService = trackService;
    }

    @PostMapping("/track")
    public ResponseEntity<?> saveTrack(@RequestBody Track track)
    {
        Track track1 = trackService.saveTrack(track);
        return new ResponseEntity<>(track1, HttpStatus.CREATED);

    }

    @GetMapping("/track")
    public ResponseEntity<?> getAllTracks()
    {
        List<Track> list = trackService.getAllTracks();
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @GetMapping("/track/artist/{trackArtist}")
    public ResponseEntity<?> getTrackByTrackArtistName(@PathVariable String trackArtist)
    {
        List<Track> list = trackService.getTrackByTrackArtistName(trackArtist);
        return new ResponseEntity<>(list,HttpStatus.FOUND);
    }

    @GetMapping("/track/id/{trackId}")
    public ResponseEntity<?> getTrackByTrackId(@PathVariable int trackId)
    {
        List<Track> list = trackService.getTrackByTrackId(trackId);
        return new ResponseEntity<>(list,HttpStatus.FOUND);
    }

    @DeleteMapping("/track/{trackId}")
    public ResponseEntity<?> deleteTrackById(@PathVariable int trackId)
    {
        return new ResponseEntity<>(trackService.deleteTrackById(trackId),HttpStatus.OK);
    }

    @PutMapping("/track/{trackId}")
    public ResponseEntity<?> updateTrack(@RequestBody Track track, @PathVariable int trackId)
    {
        return new ResponseEntity<>(trackService.updateTrack(track,trackId),HttpStatus.OK);
    }
    @GetMapping("/track/rating/{trackRating}")
    public ResponseEntity<?> getTrackByTrackRating(@PathVariable float trackRating)
    {
        return new ResponseEntity<>(trackService.getTrackByTrackRating(trackRating),HttpStatus.OK);
    }

}
