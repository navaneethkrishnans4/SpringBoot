package com.muzix.trackService.service;

import com.muzix.trackService.domain.Artist;
import com.muzix.trackService.domain.Track;
import com.muzix.trackService.exception.TrackAlreadyExistsException;
import com.muzix.trackService.exception.TrackNotFoundException;
import com.muzix.trackService.repository.TrackRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(SpringExtension.class)
public class TrackServiceImplTests {
    @Mock
    private TrackRepository trackRepository;

    @InjectMocks
    private TrackServiceImpl trackService;

    private Track track1, track2;
    private Artist artist1, artist2;
    private List<Track> trackList;

    @BeforeEach
    public void setUp(){
        artist1 = new Artist(1, "Iron Maiden");
        artist2 = new Artist(2, "Justin Bieber");

        track1 = new Track(1, "Fear of the Dark", 4.6f, artist1);
        track2 = new Track(2, "Lonely", 4.1f, artist2);

        trackList = new ArrayList<Track>();
    }

    @AfterEach
    public void tearDown(){
        track1 = null;
        track2 = null;

        artist1 = null;
        artist2 = null;

        trackList = null;
    }

    @Test
    @DisplayName("Saving track object")
    public void givenTrackToSaveShouldReturnSavedTrackSuccess() throws TrackAlreadyExistsException {
        when(trackRepository.findById(track1.getTrackId())).thenReturn(Optional.ofNullable(null));
        when(trackRepository.save(any())).thenReturn(track1);

        assertEquals(track1, trackService.saveTrack(track1));

        verify(trackRepository, times(1)).save(any());
        verify(trackRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("Saving existing track object")
    public void givenTrackToSaveShouldReturnSavedTrackFailure() {
        when(trackRepository.findById(track1.getTrackId())).thenReturn(Optional.ofNullable(track1));

        assertThrows(TrackAlreadyExistsException.class, ()-> trackService.saveTrack(track1));

        verify(trackRepository, times(0)).save(any());
        verify(trackRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("Deleting track object")
    public void givenTrackToDeleteShouldDeleteTrackSuccess()  throws TrackNotFoundException {
        when(trackRepository.findById(track1.getTrackId())).thenReturn(Optional.ofNullable(track1));
        assertTrue(trackService.deleteTrack(track1.getTrackId()));

        verify(trackRepository, times(1)).deleteById(any());
        verify(trackRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("Deleting non-existent track object")
    public void givenTrackToDeleteShouldDeleteTrackFailure() {
        when(trackRepository.findById(track1.getTrackId())).thenReturn(Optional.ofNullable(null));

        assertThrows(TrackNotFoundException.class, ()->trackService.deleteTrack(track1.getTrackId()));

        verify(trackRepository, times(0)).deleteById(any());
        verify(trackRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("Retrieving all the track objects")
    public void returnAllTrackDetailsSuccess(){
        trackList.add(track1);
        trackList.add(track2);

        when(trackRepository.findAll()).thenReturn(trackList);

        List<Track> returnedList = trackService.getAllTracks();

        assertEquals(trackList, returnedList);
        verify(trackRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Retrieving all the track object from an empty collection")
    public void returnAllTrackDetailsFailure(){
        trackList.clear();
        when(trackRepository.findAll()).thenReturn(trackList);

        List<Track> returnedList = trackService.getAllTracks();

        assertEquals(trackList, returnedList);
        verify(trackRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Retrieving all the track objects above the specified rating")
    public void returnAllTracksAboveRating(){
        float minRating = 4.5f;
        trackList.add(track1);

        when(trackRepository.getAllTracksAboveRating(minRating)).thenReturn(trackList);

        List<Track> returnedList = trackService.getAllTracksAboveRating(minRating);

        assertEquals(trackList, returnedList);
        verify(trackRepository, times(1)).getAllTracksAboveRating(minRating);
    }

    @Test
    @DisplayName("Negative scenario for retrieving all the track objects above the specified rating")
    public void returnAllTracksAboveRatingNegative() {
        float minRating = 5.0f;
        trackList.clear();

        when(trackRepository.getAllTracksAboveRating(minRating)).thenReturn(trackList);

        List<Track> returnedList = trackService.getAllTracksAboveRating(minRating);

        assertEquals(trackList, returnedList);
        verify(trackRepository, times(1)).getAllTracksAboveRating(minRating);
    }

    @Test
    @DisplayName("Retrieving all the track objects by the specified artist")
    public void returnAllTracksByArtist() {
        String artistName = track1.getTrackArtist().getArtistName();
        trackList.add(track1);

        when(trackRepository.getAllTracksByArtist(artistName)).thenReturn(trackList);

        List<Track> returnedList = trackService.getAllTracksByArtist(artistName);

        assertEquals(trackList, returnedList);
        verify(trackRepository, times(1)).getAllTracksByArtist(artistName);
    }

    @Test
    @DisplayName("Negative scenario for retrieving all the track objects by the specified artist")
    public void returnAllTracksByArtistNegative() {
        String artistName = "Beatles";
        trackList.clear();

        when(trackRepository.getAllTracksByArtist(artistName)).thenReturn(trackList);

        List<Track> returnedList = trackService.getAllTracksByArtist(artistName);

        assertEquals(trackList, returnedList);
        verify(trackRepository, times(1)).getAllTracksByArtist(artistName);
    }
}
