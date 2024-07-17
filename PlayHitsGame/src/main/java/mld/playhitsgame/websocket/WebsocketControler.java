/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.websocket;


import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import mld.playhitsgame.exemplars.Cancion;
import mld.playhitsgame.exemplars.Partida;
import mld.playhitsgame.exemplars.Respuesta;
import mld.playhitsgame.exemplars.Ronda;
import mld.playhitsgame.exemplars.StatusPartida;
import mld.playhitsgame.exemplars.Usuario;
import mld.playhitsgame.services.CancionServicioMetodos;
import mld.playhitsgame.services.PartidaServicioMetodos;
import mld.playhitsgame.services.RespuestaServicioMetodos;
import mld.playhitsgame.services.RondaServicioMetodos;
import mld.playhitsgame.services.UsuarioServicioMetodos;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Controller
public class WebsocketControler extends TextWebSocketHandler{
    
    @Autowired
    CancionServicioMetodos servCancion;
    @Autowired
    UsuarioServicioMetodos servUsuario;
    @Autowired
    PartidaServicioMetodos servPartida;
    @Autowired
    RespuestaServicioMetodos servRespuesta;
    @Autowired
    RondaServicioMetodos servRonda;
    
    private static final HashMap<Long, Set<UsuarioWS>> partidas = new HashMap(); 

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        for (Set<UsuarioWS> usuariosWS : partidas.values()){
            for (UsuarioWS usuarioWS : usuariosWS)
                if (session == usuarioWS.getSession()){
                    System.out.println("Se ha desconectado del WebSocket el usuario " + usuarioWS.getUsuario());
                    usuariosWS.remove(usuarioWS);
                }
        }        
    }
    
    public static Set<UsuarioWS> usuariosPartida(Long idPartida){
        
        return partidas.get(idPartida);        
    }
    
    public static void anyadirPartidaUsuario(WebSocketSession session, long partida, long idUsuario, String nombre){
        
        if (partidas.get(partida) == null){
            partidas.put(partida, new HashSet());
        }
        UsuarioWS usu = new UsuarioWS();
        usu.setId(idUsuario);
        usu.setUsuario(nombre);
        usu.setSession(session);
        partidas.get(partida).add(usu);        
        
    }
    
    public static void limpiarPartida(long partida){
        
        partidas.remove(partida);
    }
    
    public static String usuariosRespuestasCompletadas(long partida){
        
        String usuarios = "";
        boolean primero = true; 
        for(UsuarioWS usu : usuariosPartida(partida)){
            if (usu.isRespAnyo() && usu.isRespInterprete() && usu.isRespTitulo()){
                if (primero)
                    primero = false;
                else
                    usuarios = usuarios + ",";
                usuarios = usuarios + usu.getUsuario(); 
            }                
        }
        
        return usuarios;       
    }
    
    public static boolean isTodoCompletado(long partida){
        
        boolean completo = true;
        for(UsuarioWS usu : usuariosPartida(partida)){
            if (!usu.isRespAnyo() || !usu.isRespInterprete() || !usu.isRespTitulo()){
                completo = false;
                break;
            }                
        }        
        return completo;       
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        
        JSONObject obJson = new JSONObject(message.getPayload());
        
        if ("alta".equals(obJson.getString("op"))){
            Usuario usuBD = obtenerUsuario(obJson.getLong("idUsuario"));
            anyadirPartidaUsuario(session,
                    obJson.getLong("idPartida"), 
                    obJson.getLong("idUsuario"),
                    usuBD.getNombre()
                    );
        }else{
        
            UsuarioWS usuarioWS = null;
            for (UsuarioWS usu : usuariosPartida(obJson.getLong("idPartida")))
                if (usu.getId() == obJson.getLong("idUsuario"))
                    usuarioWS = usu;
        
            if ("titulo".equals(obJson.getString("op"))){
                responderTitulo(
                        obJson.getLong("idPartida"), 
                        obJson.getLong("idUsuario"),
                        obJson.getLong("idCancion"));
                usuarioWS.setRespTitulo(true);
            }

            if ("interprete".equals(obJson.getString("op"))){
                responderInterprete(
                        obJson.getLong("idPartida"), 
                        obJson.getLong("idUsuario"),
                        obJson.getLong("idCancion"));
                usuarioWS.setRespInterprete(true);
            }

            if ("anyo".equals(obJson.getString("op"))){
                responderAnyo(
                        obJson.getLong("idPartida"), 
                        obJson.getLong("idUsuario"),
                        obJson.getInt("anyo"));
                usuarioWS.setRespAnyo(true);
            }
      
            String respuesta = usuariosRespuestasCompletadas(obJson.getLong("idPartida"));
            TextMessage messageResp = new TextMessage(respuesta);

            for(UsuarioWS usu: usuariosPartida(obJson.getLong("idPartida")))
               usu.getSession().sendMessage(messageResp);
        }        
        
        if (isTodoCompletado(obJson.getLong("idPartida")))
            pasarSiguienteRonda(obJson.getLong("idPartida"));
    }
    
    private Usuario obtenerUsuario(Long idUsuario){
        
        Optional<Usuario> usuBD = servUsuario.findById(idUsuario);
        return usuBD.get();        
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
    
    private Partida obtenerPartida(long idPartida){
        
        Optional<Partida> part = servPartida.findById(idPartida);        
        return part.get();        
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
        resp.setInterprete(cancion.getInterprete());
        servRespuesta.updateRespuesta(resp.getId(), resp);         
    }

    private void responderAnyo(long idPartida, long idUsuario, int anyo) {
        
        Respuesta resp = obtenerRespuesta(idPartida, idUsuario);
        resp.setAnyo(anyo);
        servRespuesta.updateRespuesta(resp.getId(), resp);    
    }

    private void pasarSiguienteRonda(Long idPartida) {
        // A este metodo llegamos si todos los usuarios conectados
        // han respondido, eso no quiere decir que todos esten
        // eso se tendra que tratar mas adelante pero de momento
        // cambiaremos de ronda
        
        Partida partida = obtenerPartida(idPartida);
        
        Ronda rondaActiva = partida.rondaActiva();
        
        boolean acabar = false;
        //if (rondaActiva.isTodasLasRespuestasOK()){
        if (true){ // de momento lo damos por bueno 
            rondaActiva.setCompletada(true);
            servRonda.updateRonda(rondaActiva.getId(), rondaActiva);    
            
            if (partida.hayMasRondas()){
                partida.pasarSiguienteRonda();            
                acabar = false;
            }else{              
                partida.setStatus(StatusPartida.Terminada);
                acabar = true;
            }
            servPartida.updatePartida(partida.getId(), partida); 
        }  
        
        TextMessage messageResp;
        
        if (acabar)
            messageResp = new TextMessage("#acabar#");
        else    
            messageResp = new TextMessage("#nueva#");

        for(UsuarioWS usu: usuariosPartida(idPartida))
            try {
                usu.getSession().sendMessage(messageResp);
            } catch (IOException ex) {
                Logger.getLogger(WebsocketControler.class.getName()).log(Level.SEVERE, null, ex);
            }        
    }
    
}
