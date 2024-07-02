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
import java.util.List;
import mld.playhitsgame.exemplars.Cancion;
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

    /**
     * @return the usuarioSpotify
     */
    public String getUsuarioSpotify() {
        return usuarioSpotify;
    }

    /**
     * @param usuarioSpotify the usuarioSpotify to set
     */
    public void setUsuarioSpotify(String usuarioSpotify) {
        this.usuarioSpotify = usuarioSpotify;
    }

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
	private ListasSpotifyRepository listasDAO;
        
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

                    setUsuarioSpotify(userId);
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
        
        
        
        private String token(){
            
            UserDetails userDetails = userDetailsRepository.findByRefId(usuarioSpotify);
            SpotifyApi object = spotifyConfiguration.getSpotifyObject();
            object.setAccessToken(userDetails.getAccessToken());
            object.setRefreshToken(userDetails.getRefreshToken());
            
            return object.getAccessToken();
            
        }
        
        @GetMapping(value = "playList")
	public String playList(@RequestParam String idPlayList, @RequestParam String anyoPlayList) {   
                        
            
            String err = utiles.procesarLista(idPlayList, anyoPlayList, token());
            
            if (err.equals(""))
                err = "PROCESO OK";        
                     
            return err;	
		
	}
        
        @GetMapping(value = "playListTema")
	public String playListTema(@RequestParam String idPlayList, @RequestParam String temaPlayList) {   
                  
                 
            String err = utiles.procesarListaTema(idPlayList, temaPlayList, token());
            
            if (err.equals(""))
                err = "PROCESO OK";        
                     
            return err;	
		
	}
            
        @GetMapping(value = "playListBD")
	public String playListBD() {     
            
            String err = "";   
            String idPlayList, anyoPlayList;
            
            List<ListasSpotify> lista = listasDAO.findAll();
            
            for (ListasSpotify elem : lista){
                
                idPlayList = elem.getCodigo();
                anyoPlayList = elem.getAnyo();
                
                err = err + utiles.procesarLista(idPlayList, anyoPlayList, token());
                
            }   
            
            if (err.equals(""))
                err = "PROCESO OK";        
                     
            return err;		
		
	}
          
}
