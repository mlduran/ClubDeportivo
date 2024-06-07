/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import se.michaelthelin.spotify.model_objects.specification.User;

@Service
public class UserProfileService {

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	public UserDetails insertOrUpdateUserDetails(User user, String accessToken, String refreshToken) {
		return null;
		// Create Your logic
	}
}

