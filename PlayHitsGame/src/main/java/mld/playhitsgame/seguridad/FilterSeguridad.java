/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.seguridad;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import mld.playhitsgame.exemplars.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 * @author miguel
 */
public class FilterSeguridad extends UsernamePasswordAuthenticationFilter{

    private JwtUtils jwtUtils;
    
    public FilterSeguridad(JwtUtils jwtUtils){
        this.jwtUtils = jwtUtils;
    }
    
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        
        Usuario user = null;
        String username = "";
        String password = "";        

        try {
            user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
            username = user.getUsuario();
            password = user.getContrasenya();
        } catch (IOException ex) {
            Logger.getLogger(FilterSeguridad.class.getName()).log(Level.SEVERE, null, ex);
        }    
        
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        
        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, 
            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        
        User user = (User) authResult.getPrincipal();
        String token = jwtUtils.generateAccesToken(user.getUsername());
        
        response.addHeader("Authorization", token);
        Map<String, Object> httpResponse = new HashMap();
        httpResponse.put("token", token);
        httpResponse.put("Message","Autentificacion Correcta");
        httpResponse.put("Username", user.getUsername());
        
        response.getWriter().write(new ObjectMapper().writeValueAsString(httpResponse));
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().flush();
        
        super.successfulAuthentication(request, response, chain, authResult); 
                
    }
    
    
    
    
    
    
}
