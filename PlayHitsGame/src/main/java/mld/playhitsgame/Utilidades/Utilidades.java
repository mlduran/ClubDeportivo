/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.Utilidades;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mld.playhitsgame.exemplars.Cancion;
import mld.playhitsgame.exemplars.OpcionInterpreteTmp;
import mld.playhitsgame.exemplars.OpcionTituloTmp;
import mld.playhitsgame.exemplars.Partida;
import mld.playhitsgame.exemplars.Ronda;

/**
 *
 * @author miguel
 */
public class Utilidades {
    
    private static final int NUMERO_OPCIONES = 5;
    
    public static int calcularPtsPorAnyo(int anyo, Cancion cancion){
        
        int anyoCancion = cancion.getAnyo();
        int pts = 0;
        
        int x = Math.abs(anyo - anyoCancion);
        
        if (x == 0)
            pts = 30;
        if (x == 1)
            pts = 20;
        if (x == 2)
            pts = 10;
        if (x > 2)
            pts = 10 - x;
        if (pts < 0)
            pts = 0;

        return pts;        
    }
    
    public static int calcularPtsPorTitulo(String titulo, Cancion cancion){
        
        if (titulo != null && titulo.equals(cancion.getTitulo()))
            return 15;
        else 
            return 0;
    }
    
    public static int calcularPtsPorInterprete(String interprete, Cancion cancion){
        
        if (interprete != null && interprete.equals(cancion.getInterprete()))
            return 15;
        else 
            return 0;
    }
     
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
        
        // Las reordenamos aleatoriamente para que la correcta, no este
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
    
    public static List<OpcionTituloTmp> opcionesTitulosCanciones(Ronda ronda){
        
        // de las canciones elije aleatoriamente que une a la correcta y 
        // devuelve una lista con las canciones encriptadas
        
        ArrayList<OpcionTituloTmp>  opciones = new ArrayList();        
        List<Cancion> cancionesParaOpciones = 
                cancionesParaListaOpciones(ronda.getPartida().canciones(), ronda.getCancion(), NUMERO_OPCIONES );
        
        OpcionTituloTmp newObj;
        for (Cancion cancion : cancionesParaOpciones){
            newObj = new OpcionTituloTmp();
            newObj.setPartida(ronda.getPartida().getId());
            newObj.setRonda(ronda.getId());
            newObj.setCancion(cancion.getId());
            newObj.setOpTitulo(encriptarString(cancion.getTitulo()));
            opciones.add(newObj);
        }
       
        return opciones;        
    }
    
    public static List<OpcionInterpreteTmp> opcionesInterpretesCanciones(Ronda ronda){
        
        // de las canciones elije aleatoriamente que une a la correcta y 
        // devuelve una lista con las canciones encriptadas
        
        ArrayList<OpcionInterpreteTmp>  opciones = new ArrayList();        
        List<Cancion> cancionesParaOpciones = 
                cancionesParaListaOpciones(ronda.getPartida().canciones(), 
                        ronda.getCancion(), NUMERO_OPCIONES );
        
        OpcionInterpreteTmp newObj;
        for (Cancion cancion : cancionesParaOpciones){
            newObj = new OpcionInterpreteTmp();
            newObj.setPartida(ronda.getPartida().getId());
            newObj.setRonda(ronda.getId());
            newObj.setCancion(cancion.getId());
            newObj.setOpInterprete(encriptarString(cancion.getInterprete()));
            opciones.add(newObj);
        }
       
        return opciones;        
    }
    
    public static String nombreServidor() {      
      
        InetAddress ip;
        String host = null;
        try {
            ip = InetAddress.getLocalHost();
            host = ip.getHostName();         
        } catch (UnknownHostException ex) {
            System.out.println("No se puede obtener nombre del host");
        }
  
        return host;
    }
}
