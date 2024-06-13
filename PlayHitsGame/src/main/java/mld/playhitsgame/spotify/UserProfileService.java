/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.spotify;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import se.michaelthelin.spotify.model_objects.specification.User;

@Service
public class UserProfileService {

	@Autowired
	private UserDetailsRepository DAO;

	public UserDetails insertOrUpdateUserDetails(User user, String accessToken, String refreshToken) {
		UserDetails obj = DAO.findByRefId(user.getId());
                
                if (obj == null){
                    obj = new UserDetails();
                    obj.setRefId(user.getId());
                    obj.setAccessToken(accessToken);
                    obj.setRefreshToken(refreshToken);                  
                    
                }else{
                    if(Objects.nonNull(accessToken) && !"".equalsIgnoreCase(accessToken)){
                        obj.setAccessToken(accessToken);
                    }
                    if(Objects.nonNull(refreshToken) && !"".equalsIgnoreCase(refreshToken)){
                        obj.setRefreshToken(refreshToken);
                    }
                }
        
        return DAO.save(obj);
	}
        
        
}

