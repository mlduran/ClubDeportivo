/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.websocket;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import mld.playhitsgame.exemplars.Cancion;
import mld.playhitsgame.exemplars.Partida;
import mld.playhitsgame.exemplars.Respuesta;
import mld.playhitsgame.exemplars.Ronda;
import mld.playhitsgame.exemplars.Usuario;
import mld.playhitsgame.services.CancionServicioMetodos;
import mld.playhitsgame.services.PartidaServicioMetodos;
import mld.playhitsgame.services.RespuestaServicioMetodos;
import mld.playhitsgame.services.UsuarioServicioMetodos;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@Service
public class WebsocketHandler extends TextWebSocketHandler{
    
    @Autowired
    CancionServicioMetodos servCancion;
    @Autowired
    UsuarioServicioMetodos servUsuario;
    @Autowired
    PartidaServicioMetodos servPartida;
    @Autowired
    RespuestaServicioMetodos servRespuesta;
    
    private static final HashMap<Long, Set<Long>> partidas = new HashMap(); 

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        //sessions.remove(session);
    }
    
    public static Set usuariosPartida(Long idPartida){
        
        return partidas.get(idPartida);
        
    }
    
    public static void anyadirPartidaUsuario(long partida, long usuario){
        
        if (partidas.get(partida) == null){
            partidas.put(partida, new HashSet());
        }
        partidas.get(partida).add(usuario);        
        
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        
        JSONObject obJson = new JSONObject(message.getPayload());
        if ("alta".equals(obJson.getString("op"))){
            anyadirPartidaUsuario(
                    obJson.getLong("idPartida"), 
                    obJson.getLong("idUsuario"));
        }
        
        if ("titulo".equals(obJson.getString("op"))){
            responderTitulo(
                    obJson.getLong("idPartida"), 
                    obJson.getLong("idUsuario"),
                    obJson.getLong("idCancion"));
        }
        
        if ("interprete".equals(obJson.getString("op"))){
            responderInterprete(
                    obJson.getLong("idPartida"), 
                    obJson.getLong("idUsuario"),
                    obJson.getLong("idCancion"));
        }
        
        if ("anyo".equals(obJson.getString("op"))){
            responderAnyo(
                    obJson.getLong("idPartida"), 
                    obJson.getLong("idUsuario"),
                    obJson.getInt("anyo"));
        }
      
        System.out.println(usuariosPartida(obJson.getLong("idPartida")));
        //for(WebSocketSession ws : sessions)
        //    ws.sendMessage(message);
    }
    
    private Cancion obtenerCancion(long idCancion){
        
        Optional<Cancion> can = servCancion.findById(idCancion);
        Cancion cancion = can.get();        
        return cancion;        
    }    
    
    private Respuesta obtenerRespuesta(long idPartida, long idUsuario){
        
        Optional<Usuario> usu = servUsuario.findById(idUsuario);
        Usuario usuario = usu.get();
        Optional<Partida> part = servPartida.findById(idPartida);
        Partida partida = part.get();       
        Ronda rondaActiva = partida.rondaActiva();
        Respuesta resp = servRespuesta.buscarPorRondaUsuario(rondaActiva.getId(), usuario.getId());        
        return resp;        
    }
    
    private void responderTitulo(long idPartida, long idUsuario, long idCancion){
        
        Respuesta resp = obtenerRespuesta(idPartida, idUsuario);
        Cancion cancion = obtenerCancion(idCancion);
        resp.setTitulo(cancion.getTitulo());
        servRespuesta.updateRespuesta(resp.getId(), resp);         
    }
    
    private void responderInterprete(long idPartida, long idUsuario, long idCancion){
        
        Respuesta resp = obtenerRespuesta(idPartida, idUsuario);
        Cancion cancion = obtenerCancion(idCancion);
        resp.setInterprete(cancion.getTitulo());
        servRespuesta.updateRespuesta(resp.getId(), resp);         
    }

    private void responderAnyo(long idPartida, long idUsuario, int anyo) {
        
        Respuesta resp = obtenerRespuesta(idPartida, idUsuario);
        resp.setAnyo(anyo);
        servRespuesta.updateRespuesta(resp.getId(), resp);    
    }
    
}
