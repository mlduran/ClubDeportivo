/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.spotify;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mld.playhitsgame.DAO.CancionDAO;
import mld.playhitsgame.exemplars.Cancion;
import mld.playhitsgame.exemplars.Genero;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 *
 * @author miguel
 */
@Service
public class Utilidades {   
    
    @Autowired
    CancionDAO DAO;  
    
    public List<Cancion> obtenerDatosJson(String datos, String anyo, Genero genero){
        
        List<Cancion> lista = new LinkedList<>();
        JSONObject track, grupo, album, imagen; 
        JSONArray artists, imagenes;
  
        JSONObject obJson = new JSONObject(datos);
        JSONArray elems;
        
        try {
            elems = obJson.getJSONArray("items");
        }catch(JSONException ex){
            elems = new JSONArray();// No hay elementos
        }
       
        for (Object elem: elems) {   
       
            try {
                
                Cancion cancion = new Cancion();
                
                JSONObject elemJson = (JSONObject) elem;
                track = elemJson.getJSONObject("track");
                artists = track.getJSONArray("artists");
                album = track.getJSONObject("album");
                imagenes = album.getJSONArray("images");
                grupo = artists.getJSONObject(0);
                
                cancion.setTitulo((String) track.getString("name"));
                cancion.setInterprete((String) grupo.getString("name"));
                cancion.setAnyo(anyo);
                cancion.setGenero(genero);
                cancion.setSpotifyid((String) track.getString("id"));
                cancion.setSpotifyplay((String) track.getString("preview_url"));
                if (!imagenes.isEmpty()){
                    imagen = imagenes.getJSONObject(0);
                    cancion.setSpotifyimagen(imagen.getString("url"));
                }
                    
                  
                lista.add(cancion);
            }catch(JSONException ex){
                System.out.println("Error al obtener cancion json: " + ex);
            }
           
        }
               
       return lista; 
        
    }   
    
    public void grabarListaCanciones(List<Cancion> lista){
        
       
        for (Cancion cancion: lista){
           
            DAO.save(cancion);
           
        }
        
    }
    
    public String procesarLista(String idPlayList, String anyoPlayList, String token){
        
       

        // {'Authorization': 'Bearer {}'.format(access_token), 'Accept': 'application/json', 'Content-Type': 'application/json'}
        boolean acabar = false;
        int offset = 0;
        String err= "";
        HttpResponse<String> response = null; 
        while ( acabar == false) {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.spotify.com/v1/playlists/" + idPlayList + "/tracks?offset=" + Integer.toString(offset))) //&limit=1000")) para mas registros
                .header("Authorization", "Bearer " + token)	
                .header("Accept", "application/json")  
                .header("Content-Type", "application/json") 
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();            

            try {
                response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(SpotifyController.class.getName()).log(Level.SEVERE, null, ex);
            }

            List<Cancion> canciones = obtenerDatosJson(response.body(),anyoPlayList, Genero.Generico);               

            if (!canciones.isEmpty()){
                grabarListaCanciones(canciones);
                offset = offset + 100;
            }
            else{
                acabar = true;
                if (offset == 0){
                    err = "ERROR " + idPlayList + "( " + anyoPlayList + " ) : " + response.body();
                }

            }

        }
        return err;        
        
    }       
               
    
    
}
