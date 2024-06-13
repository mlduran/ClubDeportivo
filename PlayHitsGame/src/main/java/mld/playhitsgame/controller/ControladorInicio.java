/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


/**
 *
 * @author miguel
 */
@Controller
public class ControladorInicio {
    
    @Value("${custom.server.ip}")
    private String customIp;
    
    @GetMapping("/")
    public String inicio(Model modelo){
        
        return "Inicio";
        
    }
    
    @GetMapping("/spotify")
    public String spotify(Model modelo){
                
        String urlLogin = null;
        try {
            URL url = new URL(customIp + "/api/spotify/login");
            BufferedReader urlLectura = new BufferedReader(new InputStreamReader(url.openStream()));
            urlLogin = urlLectura.readLine();
        } catch (MalformedURLException ex) {
            Logger.getLogger(ControladorInicio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {        
            Logger.getLogger(ControladorInicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        modelo.addAttribute("url", urlLogin);
        
        return "SpotifyLogin";
        
    }
    
    @GetMapping("/spotifyServicios/{userId}")
    public String spotify(@PathVariable String userId, Model modelo){
        
        modelo.addAttribute("userid", userId);
        
        return "Spotify";
        
    }
    
}
