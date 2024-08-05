package com.muzix.trackService.repository;

import com.muzix.trackService.domain.Track;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepository extends MongoRepository<Track, Integer> {
    @Query("{ 'trackRating': { $gt: ?0 }}")
    List<Track> getAllTracksAboveRating(float rating);

    @Query("{ 'trackArtist.artistName': ?0}")
    List<Track> getAllTracksByArtist(String artistName);
}
