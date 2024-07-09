/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mld.playhitsgame.exemplars.Cancion;
import mld.playhitsgame.exemplars.Partida;
import mld.playhitsgame.exemplars.Ronda;

/**
 *
 * @author miguel
 */
public class Utilidades {
     
    private static Cancion cancionRandom(List<Cancion> lista){
      int i;  
      i = (int) (Math.floor(Math.random() * lista.size()));

      return lista.get(i);        
    }   
    
    public static void asignarCancionesAleatorias(Partida partida, List<Cancion> canciones) {
                
        HashMap<Long,Cancion> listaCanciones =  new HashMap();        
            
        while (listaCanciones.size() < partida.getRondas().size() + 1){
           
            Cancion cancion = cancionRandom(canciones);
            listaCanciones.put(cancion.getId(), cancion); 

        }
           
        ArrayList<Cancion> lista = new ArrayList();
        for (HashMap.Entry<Long, Cancion> elem : listaCanciones.entrySet()){
            lista.add(elem.getValue());
        }

        int i = 0;
        for (Ronda ronda : partida.getRondas()){
            ronda.setCancion(lista.get(i));
            i = i + 1;
        }       
        
    }
        
    public static int calcularPtsPorAnyo(int anyo, Cancion cancion){
        
        int anyoCancion = cancion.getAnyo();
        int pts = 0;
        
        int x = Math.abs(anyo - anyoCancion);
        
        if (x == 0)
            pts = 25;
        if (x == 1)
            pts = 15;
        if (x == 2)
            pts = 10;
        if (x > 2)
            pts = 10 - x;
        if (pts < 0)
            pts = 0;

        return pts;
        
    }
}
