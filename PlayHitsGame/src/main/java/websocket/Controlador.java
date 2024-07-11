/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miguel
 */


@ServerEndpoint("/websocket")
public class Controlador {
    
     private final Set<Session> sesiones = new HashSet<>();
     
     @OnOpen
     public void onOpen(Session sesion){
         
         sesiones.add(sesion);
         
     }
     
      
     @OnMessage
     public void OnMessage(String message, Session sesion){
         
         String mensaje = message;
         
         
         for (Session s : sesiones){
             try {
                 s.getBasicRemote().sendText("mensaje " + mensaje);
             } catch (IOException ex) {
                 Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
             }             
         }        
     }
     
     @OnClose
     public void OnClose(Session sesion){
         
         sesiones.remove(sesion);
         
     }
    
    
}
