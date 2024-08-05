package com.muzix.trackService.repository;

import com.muzix.trackService.domain.Artist;
import com.muzix.trackService.domain.Track;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class TrackRepositoryTests {
    @Autowired
    private TrackRepository trackRepository;
    private Track track1;
    private Track track2;
    private Artist artist;

    @BeforeEach
    public void setUp(){
        artist = new Artist(7, "Iron Maiden");
        track1 = new Track(16, "Fear of the Dark", 4.5f, artist);
        track2 = new Track(17, "Trooper", 4.2f, artist);
    }

    @AfterEach
    public void tearDown(){
        track1 = null;
        track2 = null;
        artist = null;
        trackRepository.deleteAll();
    }

    @Test
    @DisplayName("Saving track object")
    public void givenTrackToSaveShouldReturnSavedTrack() {
        trackRepository.save(track1);
        Track returnedTrack = trackRepository.findById(track1.getTrackId()).get();

        assertNotNull(returnedTrack);
        assertEquals(track1.getTrackId(), returnedTrack.getTrackId());
    }

    @Test
    @DisplayName("Saving existing track object")
    public void givenExistingTrackToSaveShouldLeadToOneDBEntryOnly() {
        trackRepository.save(track1);
        Track returnedTrack = trackRepository.findById(track1.getTrackId()).get();

        assertNotNull(returnedTrack);
        assertEquals(track1.getTrackId(), returnedTrack.getTrackId());

        trackRepository.save(track1);

        assertEquals(1, trackRepository.findAll().size());
    }

    @Test
    @DisplayName("Deleting track object")
    public void givenTrackToDeleteShouldDeleteTrack() {
        trackRepository.save(track1);
        Track returnedTrack = trackRepository.findById(track1.getTrackId()).get();
        trackRepository.deleteById(returnedTrack.getTrackId());
        assertEquals(Optional.empty(), trackRepository.findById(track1.getTrackId()));
    }

    @Test
    @DisplayName("Deleting non-existent track object")
    public void givenNonExistentTrackToDeleteShouldNotAlterDB() {
        trackRepository.save(track1);
        trackRepository.deleteById(1);
        assertEquals(1, trackRepository.findAll().size());
    }

    @Test
    @DisplayName("Retrieving all the track objects")
    public void returnAllTrackDetails() {
        trackRepository.save(track1);
        trackRepository.save(track2);

        List<Track> trackList = trackRepository.findAll();
        assertEquals(2, trackList.size());
        assertEquals(track2.getTrackId(), trackList.get(1).getTrackId());
        assertEquals(track2.getTrackName(), trackList.get(1).getTrackName());
    }

    @Test
    @DisplayName("Retrieving all the track object from an empty collection")
    public void returnAllTrackDetailsFromEmptyDB() {
        List<Track> trackList = trackRepository.findAll();
        assertEquals(0, trackList.size());
    }

    @Test
    @DisplayName("Retrieving all the track objects above the specified rating")
    public void returnAllTracksAboveRating() {
        float minRating = 4.0f;

        trackRepository.save(track1);
        trackRepository.save(track2);

        List<Track> trackList = trackRepository.getAllTracksAboveRating(minRating);
        assertEquals(2, trackList.size());
    }

    @Test
    @DisplayName("Negative scenario for retrieving all the track objects above the specified rating")
    public void returnAllTracksAboveRatingNegative() {
        float minRating = 5.0f;

        trackRepository.save(track1);
        trackRepository.save(track2);

        List<Track> trackList = trackRepository.getAllTracksAboveRating(minRating);
        assertEquals(0, trackList.size());
    }

    @Test
    @DisplayName("Retrieving all the track objects by the specified artist")
    public void returnAllTracksByArtist() {
        String artistName = "Iron Maiden";

        trackRepository.save(track1);
        trackRepository.save(track2);

        List<Track> trackList = trackRepository.getAllTracksByArtist(artistName);
        assertEquals(2, trackList.size());
    }

    @Test
    @DisplayName("Negative scenario for retrieving all the track objects by the specified artist")
    public void returnAllTracksByArtistNegative() {
        String artistName = "Justin Bieber";

        trackRepository.save(track1);
        trackRepository.save(track2);

        List<Track> trackList = trackRepository.getAllTracksByArtist(artistName);
        assertEquals(0, trackList.size());
    }
}
