/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.spotify;


/**
    curl -X POST "https://accounts.spotify.com/api/token" \
    -H "Content-Type: application/x-www-form-urlencoded" \
    -d "grant_type=client_credentials&client_id=your-client-id&client_secret=your-client-secret"

*/

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;

@Service
public class SpotifyConfiguration {
	
	@Value("${redirect.server.ip}")
	private String customIp;
	
	public SpotifyApi getSpotifyObject() {
		 URI redirectedURL =  SpotifyHttpManager.makeUri(customIp + "/api/spotify/get-user-code/");
		 
		 return new SpotifyApi
				 .Builder()
				 .setClientId("")
				 .setClientSecret("")
				 .setRedirectUri(redirectedURL)
				 .build();
	}
}

