/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.spotify;

/**
 *
 * @author miguel
 */

import java.io.IOException;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mld.playhitsgame.DAO.CancionDAO;
import mld.playhitsgame.exemplars.Cancion;
import mld.playhitsgame.exemplars.Genero;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.model_objects.specification.User;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

@RestController
@RequestMapping("/api/spotify")
public class SpotifyController {

	@Value("${custom.server.ip}")
	private String customIp;
        
        @Value("${custom.usuariospotify}")
        private String usuarioSpotify;
	
	@Autowired
	private UserProfileService userProfileService;

	@Autowired
	private SpotifyConfiguration spotifyConfiguration;
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;
        
        @Autowired
	private Utilidades utiles;
	
	@GetMapping("login")
	public String spotifyLogin() {
		SpotifyApi object = spotifyConfiguration.getSpotifyObject();
		
		AuthorizationCodeUriRequest authorizationCodeUriRequest = object.authorizationCodeUri()
				.scope("user-library-read app-remote-control streaming playlist-read-private playlist-read-collaborative")
				.show_dialog(true)
				.build();
		
		final URI uri = authorizationCodeUriRequest.execute();
		return uri.toString();

	}

	@GetMapping(value = "get-user-code")
	public void getSpotifyUserCode(@RequestParam("code") String userCode, HttpServletResponse response) throws IOException {
            
		SpotifyApi object = spotifyConfiguration.getSpotifyObject();
		
		AuthorizationCodeRequest authorizationCodeRequest = object.authorizationCode(userCode).build();
		User user = null;
		
		try {
			final AuthorizationCodeCredentials authorizationCode = authorizationCodeRequest.execute();

			object.setAccessToken(authorizationCode.getAccessToken());
			object.setRefreshToken(authorizationCode.getRefreshToken());
			
			final GetCurrentUsersProfileRequest getCurrentUsersProfile = object.getCurrentUsersProfile().build();
			user = getCurrentUsersProfile.execute();

			userProfileService.insertOrUpdateUserDetails(user, authorizationCode.getAccessToken(), authorizationCode.getRefreshToken());
		} catch (IOException | ParseException | SpotifyWebApiException e) {
			System.out.println("Exception occured while getting user code: " + e);
		}

                if (user != null)
                    response.sendRedirect(customIp + "/api/spotify/home?userId="+user.getId());
	}
	
	@GetMapping(value = "home")
	public void home(@RequestParam String userId, HttpServletResponse response) {
		try {

                    usuarioSpotify = userId;
                    response.sendRedirect(customIp + "/spotifyServicios");

		} catch (Exception e) {
			System.out.println("Error en acceso a SPOTIFY: " + e);
		}
		
	}
	
	
	@GetMapping(value = "user-top-songs")
	public Track[] getUserTopTracks(@RequestParam String userId) {
		UserDetails userDetails = userDetailsRepository.findByRefId(userId);
		
		SpotifyApi object = spotifyConfiguration.getSpotifyObject();
		object.setAccessToken(userDetails.getAccessToken());
		object.setRefreshToken(userDetails.getRefreshToken());
		
		final GetUsersTopTracksRequest getUsersTopTracksRequest = object.getUsersTopTracks()
				.time_range("medium_term")
				.limit(10)
				.offset(0)
				.build();

		try {
			final Paging<Track> trackPaging = getUsersTopTracksRequest.execute();

			return trackPaging.getItems();
		} catch (Exception e) {
			System.out.println("Exception occured while fetching top songs: " + e);
		}
		
		return new Track[0];
	}
        
        @GetMapping(value = "playList")
	public String playList(@RequestParam String idPlayList, @RequestParam String anyoPlayList) {           
            
            UserDetails userDetails = userDetailsRepository.findByRefId(usuarioSpotify);
            SpotifyApi object = spotifyConfiguration.getSpotifyObject();
            object.setAccessToken(userDetails.getAccessToken());
            object.setRefreshToken(userDetails.getRefreshToken());
            
            // {'Authorization': 'Bearer {}'.format(access_token), 'Accept': 'application/json', 'Content-Type': 'application/json'}
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.spotify.com/v1/playlists/" + idPlayList + "/tracks")) //?offset=0&limit=1000")) para mas registros
                .header("Authorization", "Bearer " + object.getAccessToken())	
                .header("Accept", "application/json")  
                .header("Content-Type", "application/json") 
		.method("GET", HttpRequest.BodyPublishers.noBody())
		.build();
            
            HttpResponse<String> response = null;   
            try {
                response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(SpotifyController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            List<Cancion> canciones = utiles.obtenerDatosJson(response.body(),anyoPlayList, Genero.Generico);
            
            utiles.grabarListaCanciones(canciones);
        
            
            if (response != null)
                return (response.body());
            else 
                return "ERROR EN LA PETICION";
		
		
	}
            
          
}
