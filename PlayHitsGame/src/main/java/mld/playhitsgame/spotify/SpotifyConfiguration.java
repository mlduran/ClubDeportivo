/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.spotify;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;

@Service
public class SpotifyConfiguration {
	
	@Value("${redirect.server.ip}")
	private String customIp;
        @Autowired
	private UserDetailsRepository userDetailsRepository;
	
	public SpotifyApi getSpotifyObject() {
            
            UserDetails userDetails = userDetailsRepository.findByRefId("spotifyplayhitsgame");
            
            String clientId = userDetails.getAccessToken();
            String clientSecret = userDetails.getRefSecret();
            URI redirectedURL =  SpotifyHttpManager.makeUri(customIp + "/api/spotify/get-user-code" );          
		 
		 return new SpotifyApi
				 .Builder()
				 .setClientId(clientId)
				 .setClientSecret(clientSecret)
				 .setRedirectUri(redirectedURL)
				 .build();
	}
}

