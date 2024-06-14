/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.spotify;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import mld.playhitsgame.DAO.CancionDAO;
import mld.playhitsgame.exemplars.Cancion;
import mld.playhitsgame.exemplars.Genero;
import mld.playhitsgame.services.CancionServicio;
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
        JSONObject track, grupo;  
        JSONArray artists;
  
        JSONObject obJson = new JSONObject(datos);
        JSONArray elems = obJson.getJSONArray("items");
       
        for (Object elem: elems) {   
       
            try {
                
                Cancion cancion = new Cancion();
                
                JSONObject elemJson = (JSONObject) elem;
                track = elemJson.getJSONObject("track");
                artists = track.getJSONArray("artists");
                grupo = artists.getJSONObject(0);
                
                cancion.setTitulo((String) track.getString("name"));
                cancion.setInterprete((String) grupo.getString("name"));
                cancion.setAnyo(anyo);
                cancion.setGenero(genero);
                cancion.setSpotify((String) track.getString("href"));
                  
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
       
               
    
    
}
