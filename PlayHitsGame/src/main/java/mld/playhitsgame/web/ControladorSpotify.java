/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.web;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import mld.playhitsgame.spotify.SpotifyController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author miguel
 */
@Controller
@Slf4j
public class ControladorSpotify {

    @Value("${custom.server.ip}")
    private String customIp;

    @GetMapping("/spotifyServicios")
    public String spotifyServicios(Model modelo) {

        return "Spotify";
    }
    
    
    @PostMapping("/spotifyMostrarDatos")
    public String spotifyMostrarDatos(String idplaylist, Model modelo) {

        String info;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(customIp + "/api/spotify/datosPlayList?idPlayList="
                        + idplaylist))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SpotifyController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (response != null) {
            info = response.body();
        } else {
            info = "ERROR EN LA PETICION";
        }

        modelo.addAttribute("info", info);

        return "Spotify";
    }
    
    @PostMapping("/spotifyServicios")
    public String respuestaServicios(String idplaylist, String anyoplaylist, Model modelo) {

        String info;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(customIp + "/api/spotify/playList?idPlayList="
                        + idplaylist + "&anyoPlayList=" + anyoplaylist))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SpotifyController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (response != null) {
            info = response.body();
        } else {
            info = "ERROR EN LA PETICION";
        }

        modelo.addAttribute("info", info);

        return "Spotify";
    }

    @PostMapping("/spotifyServiciosTema")
    public String respuestaServiciosTema(String idplaylist, String temaplaylist, Model modelo) {

        String info;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(customIp + "/api/spotify/playListTema?idPlayList="
                        + idplaylist + "&temaPlayList=" + temaplaylist))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SpotifyController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (response != null) {
            info = response.body();
        } else {
            info = "ERROR EN LA PETICION";
        }

        modelo.addAttribute("info", info);

        return "Spotify";
    }

    @PostMapping("/spotifyServiciosBD")
    public String respuestaServiciosBD(Model modelo) {

        String info;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(customIp + "/api/spotify/playListBD"))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SpotifyController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (response != null) {
            info = response.body();
        } else {
            info = "ERROR EN LA PETICION";
        }

        modelo.addAttribute("info", info);

        return "Spotify";
    }

}
