package com.niit.mongodbChallenge.repository;

import com.niit.mongodbChallenge.domain.Track;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepository extends MongoRepository<Track,Integer> {

    @Query("{'trackArtist.artistName' :{$in:[?0]}}")
    List<Track> findByTrackArtistName(String artistName);
    List<Track> findByTrackId(int trackId);

    @Query("{'trackRating' : {$gt: ?0}}")
    List<Track> findByTrackRating(float trackRating);

}
