package com.muzix.trackService.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muzix.trackService.domain.Artist;
import com.muzix.trackService.domain.Track;
import com.muzix.trackService.exception.TrackAlreadyExistsException;
import com.muzix.trackService.exception.TrackNotFoundException;
import com.muzix.trackService.service.TrackServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TrackControllerTests {
    private MockMvc mockMvc;

    @Mock
    private TrackServiceImpl trackService;

    @InjectMocks
    private TrackController trackController;

    private Track track1, track2;
    private Artist artist1, artist2;
    private List<Track> trackList;

    @BeforeEach
    public void setUp() {
        artist1 = new Artist(1, "Iron Maiden");
        artist2 = new Artist(2, "Justin Bieber");

        track1 = new Track(1, "Fear of the Dark", 4.6f, artist1);
        track2 = new Track(2, "Lonely", 4.1f, artist2);

        trackList = new ArrayList<Track>();

        mockMvc = MockMvcBuilders.standaloneSetup(trackController).build();
    }

    @AfterEach
    public void tearDown() {
        track1 = null;
        track2 = null;

        artist1 = null;
        artist2 = null;

        trackList = null;
    }

    @Test
    @DisplayName("Saving track object")
    public void givenTrackToSaveShouldReturnSavedTrackSuccess() throws Exception {
        when(trackService.saveTrack(any())).thenReturn(track1);

        mockMvc.perform(post("/api/v1/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(track1)))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());

        verify(trackService, times(1)).saveTrack(any());
    }

    @Test
    @DisplayName("Saving existing track object")
    public void givenTrackToSaveShouldReturnSavedTrackFailure() throws Exception {
        when(trackService.saveTrack(any())).thenThrow(TrackAlreadyExistsException.class);

        mockMvc.perform(post("/api/v1/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(track1)))
                .andExpect(status().isConflict())
                .andDo(MockMvcResultHandlers.print());

        verify(trackService, times(1)).saveTrack(any());
    }

    @Test
    @DisplayName("Deleting track object")
    public void givenTrackToDeleteShouldDeleteTrackSuccess() throws Exception {
        when(trackService.deleteTrack(anyInt())).thenReturn(true);

        mockMvc.perform(delete("/api/v1/tracks/1"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(trackService, times(1)).deleteTrack(anyInt());
    }

    @Test
    @DisplayName("Deleting non-existent track object")
    public void givenTrackToDeleteShouldDeleteTrackFailure() throws Exception {
        when(trackService.deleteTrack(anyInt())).thenThrow(TrackNotFoundException.class);

        mockMvc.perform(delete("/api/v1/tracks/1"))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

        verify(trackService, times(1)).deleteTrack(anyInt());
    }

    @Test
    @DisplayName("Retrieving all the track objects")
    public void returnAllTrackDetailsSuccess() throws Exception {
        trackList.add(track1);
        trackList.add(track2);

        when(trackService.getAllTracks()).thenReturn(trackList);

        mockMvc.perform(get("/api/v1/tracks"))
                .andExpect(content().json(jsonToString(trackList)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(trackService, times(1)).getAllTracks();
    }

    @Test
    @DisplayName("Retrieving all the track object from an empty collection")
    public void returnAllTrackDetailsFailure() throws Exception {
        trackList.clear();

        when(trackService.getAllTracks()).thenReturn(trackList);

        mockMvc.perform(get("/api/v1/tracks"))
                .andExpect(content().json(jsonToString(trackList)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(trackService, times(1)).getAllTracks();
    }

    @Test
    @DisplayName("Retrieving all the track objects above the specified rating")
    public void returnAllTracksAboveRating() throws Exception {
        float minRating = 4.5f;
        trackList.add(track1);

        when(trackService.getAllTracksAboveRating(anyFloat())).thenReturn(trackList);

        mockMvc.perform(get("/api/v1/tracks/minRating=" + minRating))
                .andExpect(content().json(jsonToString(trackList)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(trackService, times(1)).getAllTracksAboveRating(anyFloat());

    }

    @Test
    @DisplayName("Negative scenario for retrieving all the track objects above the specified rating")
    public void returnAllTracksAboveRatingNegative() throws Exception {
        float minRating = 5.0f;
        trackList.clear();

        when(trackService.getAllTracksAboveRating(anyFloat())).thenReturn(trackList);

        mockMvc.perform(get("/api/v1/tracks/minRating=" + minRating))
                .andExpect(content().json(jsonToString(trackList)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(trackService, times(1)).getAllTracksAboveRating(anyFloat());
    }

    @Test
    @DisplayName("Retrieving all the track objects by the specified artist")
    public void returnAllTracksByArtist() throws Exception {
        String artistName = track1.getTrackArtist().getArtistName();
        trackList.add(track1);

        when(trackService.getAllTracksByArtist(any())).thenReturn(trackList);

        mockMvc.perform(get("/api/v1/tracks/trackArtist=" + artistName))
                .andExpect(content().json(jsonToString(trackList)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(trackService, times(1)).getAllTracksByArtist(any());
    }

    @Test
    @DisplayName("Negative scenario for retrieving all the track objects by the specified artist")
    public void returnAllTracksByArtistNegative() throws Exception {
        String artistName = "Beatles";
        trackList.clear();

        when(trackService.getAllTracksByArtist(any())).thenReturn(trackList);

        mockMvc.perform(get("/api/v1/tracks/trackArtist=" + artistName))
                .andExpect(content().json(jsonToString(trackList)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(trackService, times(1)).getAllTracksByArtist(any());

    }

    private static String jsonToString(final Object ob) {
        String result;
        try {
            ObjectMapper mapper = new ObjectMapper();
            result = mapper.writeValueAsString(ob);
        } catch (JsonProcessingException e) {
            result = "JSON processing error";
        }

        return result;
    }
}
