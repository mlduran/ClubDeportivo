package mld.playhitsgame.spotify;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ListasSpotifyRepository extends JpaRepository<ListasSpotify, Integer> {
	
	//ListasSpotify findByRefId(String refId);

}
