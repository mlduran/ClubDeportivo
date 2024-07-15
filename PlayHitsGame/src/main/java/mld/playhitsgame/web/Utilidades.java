/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mld.playhitsgame.exemplars.Cancion;
import mld.playhitsgame.exemplars.Partida;
import mld.playhitsgame.exemplars.Ronda;

/**
 *
 * @author miguel
 */
public class Utilidades {
    
    private static int NUMERO_OPCIONES = 5;
     
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
    
    
    private static List<Cancion> cancionesParaListaOpciones(List<Cancion> canciones, 
            Cancion cancionCorrecta, int numero){
        
        Map<Long,Cancion> lista = new HashMap();
        lista.put(cancionCorrecta.getId(),cancionCorrecta);
        while (lista.size() < numero){
            Cancion aleatoria = cancionRandom(canciones);
            lista.put(aleatoria.getId(), aleatoria);            
        }
        ArrayList<Cancion> listaFinal = new ArrayList();
        for (Map.Entry<Long,Cancion> elem : lista.entrySet())
            listaFinal.add(elem.getValue());        
        
        // Las reordenamos aleatoria mente para que la correcta, no este
        // siempre la primera
        ArrayList<Cancion> listaDesordenada = new ArrayList();
        while (!listaFinal.isEmpty()){
            int i = (int) (Math.floor(Math.random() * listaFinal.size()));
            listaDesordenada.add(listaFinal.get(i));
            listaFinal.remove(i);
        }
        
        return listaDesordenada;        
    }
    
    private static String encriptarString(String txt){
        
        // Subtituimos la mitad aleatoria de letras por *
        int x = txt.replace(" ","").length() / 2;       
        StringBuilder newText = new StringBuilder(txt);
        
        double s;
        for (int i = 0; i < x; i++){
            s = Math.floor(Math.random() * txt.length());
            if (newText.charAt((int) s) == ' '){
                x = x +1;
                continue;
            }
            newText.setCharAt((int) s, '*');
        }
        
        return newText.toString();        
    }    
    
    public static Map<Long, String> opcionesTitulosCanciones(List<Cancion> canciones, 
            Cancion cancionCorrecta){
        
        // de las canciones elije aleatoriamente que une a la correcta y 
        // devuelve una lista con las canciones encriptadas
        
        HashMap<Long, String> opciones = new HashMap();        
        List<Cancion> cancionesParaOpciones = 
                cancionesParaListaOpciones(canciones, cancionCorrecta, NUMERO_OPCIONES );
        
        for (Cancion cancion : cancionesParaOpciones)
            opciones.put(cancion.getId(), encriptarString(cancion.getTitulo()));       
        
        return opciones;
        
    }
    
    public static Map<Long, String> opcionesInterpretesCanciones(List<Cancion> canciones, 
            Cancion cancionCorrecta){
        
        // de las canciones elije aleatoriamente que une a la correcta y 
        // devuelve una lista con las canciones encriptadas
        
        HashMap<Long, String> opciones = new HashMap();        
        List<Cancion> cancionesParaOpciones = 
                cancionesParaListaOpciones(canciones, cancionCorrecta, NUMERO_OPCIONES );
        
        for (Cancion cancion : cancionesParaOpciones)
            opciones.put(cancion.getId(), encriptarString(cancion.getInterprete()));       
        
        return opciones;
        
    }
}
