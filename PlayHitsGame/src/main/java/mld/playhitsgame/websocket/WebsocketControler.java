/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.websocket;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import mld.playhitsgame.Utilidades.Utilidades;
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
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private static final HashMap<Long, Integer> nRespuestas = new HashMap();    
    
    public static Set<UsuarioWS> usuariosPartida(Long idPartida){
        
        return partidas.get(idPartida);        
    }    
    
    public static UsuarioWS anyadirPartidaUsuario(WebSocketSession session, long partida, long idUsuario, String nombre){
        
        if (partidas.get(partida) == null){
            partidas.put(partida, new HashSet());
        }
        UsuarioWS usu = new UsuarioWS();
        usu.setId(idUsuario);
        usu.setUsuario(nombre);
        usu.setSession(session);
        partidas.get(partida).add(usu);  
        nRespuestas.put(partida, 0);
        
        return usu;
    }
    
    public static UsuarioWS buscarUsuarioWS(Long idPartida, Long idUsuario){
        
        UsuarioWS usuWS = null;
        
        if (partidas.get(idPartida) != null){        
            for(UsuarioWS usu : usuariosPartida(idPartida)){
                if (usu.getId() == idUsuario){
                usuWS = usu;
                break;
                }                
            }
        }
        return usuWS;        
    }
    
    public static void limpiarPartida(long partida){
        
        partidas.remove(partida);
    }
    
    public static void limpiarDatos(long partida){
        
        for (UsuarioWS usuWS : partidas.get(partida)){
            usuWS.setOrden(0);
            usuWS.setRespAnyo(false);
            usuWS.setRespInterprete(false);
            usuWS.setRespTitulo(false);      
        }

    }
    
    public static String usuariosRespuestasCompletadas(long partida){
        
        String usuarios = "";
        boolean primero = true; 
        for(UsuarioWS usu : usuariosPartida(partida)){
            if (usu.isTodoRespondido()){
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
            if (!usu.isTodoRespondido()){
                completo = false;
                break;
            }                
        }        
        return completo;       
    }    
    
    private UsuarioWS registrarPeticionWS(JSONObject obJson, WebSocketSession session){
        
        UsuarioWS usuWS = buscarUsuarioWS(
                    obJson.getLong("idPartida"),obJson.getLong("idUsuario"));
               
        if (usuWS == null){            
            Usuario usuBD = obtenerUsuario(obJson.getLong("idUsuario"));
            usuWS = anyadirPartidaUsuario(session,
                    obJson.getLong("idPartida"), 
                    obJson.getLong("idUsuario"),
                    usuBD.getNombre()
                    );
            System.out.println(new Date() + " Se ha conectado al WebSocket el usuario " + usuWS.getUsuario() + 
                            " con sesion " + session.getId());        
        }else{
            if (!usuWS.getSession().getId().equals(session.getId())){
                usuWS.setSession(session);
                System.out.println(new Date() + " Se actualiza sesion del usuario " + usuWS.getUsuario() + 
                             " con sesion " + session.getId()); 
            }else
                System.out.println(new Date() + " Peticion " +  obJson.getString("op") +
                        " al WebSocket del usuario " + usuWS.getUsuario() + 
                        " con sesion " + session.getId()); 
        }
        
        return usuWS;        
    }
    
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
     
        JSONObject obJson = null;
        try {
            obJson = new JSONObject(message.getPayload());
        } catch (JSONException jSONException) {
            System.out.println(new Date() + " Error al capturar json : " + message.getPayload()); 
        }
        
        if (obJson == null || obJson.isEmpty()){
            //aqui habria que lanzar un error de momento paramos para ver que pasa
            throw new Exception("ERROR al obtener json");  
        }
        UsuarioWS usuarioWS = registrarPeticionWS(obJson,session);        
        
        if ("alta".equals(obJson.getString("op"))){
            // De momento no hacemos nada, lo mantemos para 
            // hacer el alta en lacarga de la pagina y ahorrarnos
            // luego la consulta a BD del usuario
        }

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

        if (usuarioWS.isTodoRespondido()){
            int nr = nRespuestas.get(obJson.getLong("idPartida"));
            usuarioWS.setOrden(nr +1);
            nRespuestas.put(obJson.getLong("idPartida"), nr + 1);
        }                

        String respuesta = usuariosRespuestasCompletadas(obJson.getLong("idPartida"));
        TextMessage messageResp = new TextMessage(respuesta);

        for(UsuarioWS usu: usuariosPartida(obJson.getLong("idPartida"))){
            try {
                if (usu.getSession().isOpen())
                    usu.getSession().sendMessage(messageResp);
                else
                    System.out.println(new Date() + " La conexion del usuario " + usu.getUsuario() + 
                         " con sesion " + usu.getSession().getId() + " esta cerrada");
            } catch (IOException iOException) {
                System.out.println(new Date() + " No se ha podido entregar notificacion del usuario " + usu.getUsuario() + 
                         " con sesion " + usu.getSession().getId()); 
            }
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
    
    @GetMapping("/forzarAcabarRonda/{idPartida}")
    public void acabarRonda(Model modelo, @PathVariable Long idPartida){
        // esto es para poder llamarse desde el master para forzar acabar la ronda
        pasarSiguienteRonda(idPartida); 
    }

    private void pasarSiguienteRonda(Long idPartida) {
        // A este metodo llegamos si todos los usuarios conectados
        // han respondido, eso no quiere decir que todos esten
        // eso se tendra que tratar mas adelante pero de momento
        // cambiaremos de ronda
        
        Partida partida = obtenerPartida(idPartida);
        
        Ronda rondaActiva = partida.rondaActiva();
        
        boolean acabar;
        //if (rondaActiva.isTodasLasRespuestasOK()){
        if (true){ // de momento lo damos por bueno 
            asignarPuntuacion(rondaActiva);
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
                if (usu.getSession().isOpen())
                    usu.getSession().sendMessage(messageResp);
            } catch (IOException ex) {
                Logger.getLogger(WebsocketControler.class.getName()).log(Level.SEVERE, null, ex);
            }  
        
        if (acabar)
            limpiarPartida(idPartida);
        else
            limpiarDatos(idPartida);
        
    }
    
    private Usuario primerUsuarioAcertarTitulo(Ronda rondaActiva){
        
        List<Respuesta> respuestas = rondaActiva.getRespuestas();
        Set<UsuarioWS> listaUsuariosWS = partidas.get(rondaActiva.getPartida().getId());
        List<UsuarioWS> usuariosWS = new ArrayList<>(listaUsuariosWS);
        Collections.sort(usuariosWS); 
        Usuario usuarioAcertante = null;
        for (UsuarioWS usuWS : usuariosWS){
            for (Respuesta res : respuestas){
                if (res.getTitulo() == null) continue;
                if (res.getTitulo().equals(rondaActiva.getCancion().getTitulo())){
                    if (res.getUsuario().getId().equals(usuWS.getId())){
                        usuarioAcertante = res.getUsuario();
                        break;
                    }
                }
            }
            if (usuarioAcertante != null)
                break;
        }
        return usuarioAcertante;        
    }    
    
    private Usuario primerUsuarioAcertarInterprete(Ronda rondaActiva){
        
        List<Respuesta> respuestas = rondaActiva.getRespuestas();
        Set<UsuarioWS> listaUsuariosWS = partidas.get(rondaActiva.getPartida().getId());
        List<UsuarioWS> usuariosWS = new ArrayList<>(listaUsuariosWS);
        Collections.sort(usuariosWS); 
        Usuario usuarioAcertante = null;
        for (UsuarioWS usuWS : usuariosWS){
            for (Respuesta res : respuestas){
                if (res.getInterprete() == null) continue;
                if (res.getInterprete().equals(rondaActiva.getCancion().getInterprete())){
                    if (res.getUsuario().getId().equals(usuWS.getId())){
                        usuarioAcertante = res.getUsuario();
                        break;
                    }
                }
            }
            if (usuarioAcertante != null)
                break;
        }
        return usuarioAcertante;        
    }    

    private void asignarPuntuacion(Ronda rondaActiva) {

        Cancion cancion = rondaActiva.getCancion();
        int totalPts;
        int ptsTitulo;
        int ptsInterp;
        Usuario primerAcertanteTitulo = primerUsuarioAcertarTitulo(rondaActiva);
        Usuario primerAcertanteInterprete = primerUsuarioAcertarInterprete(rondaActiva);
        for (Respuesta resp : rondaActiva.getRespuestas()){
            totalPts = Utilidades.calcularPtsPorAnyo(resp.getAnyo(), cancion);
            ptsTitulo = Utilidades.calcularPtsPorTitulo(resp.getTitulo(), cancion);
            if (resp.getUsuario().equals(primerAcertanteTitulo))
                ptsTitulo = ptsTitulo * 2;
            totalPts = totalPts + ptsTitulo;
            ptsInterp = Utilidades.calcularPtsPorInterprete(resp.getInterprete(), cancion);
            if (resp.getUsuario().equals(primerAcertanteInterprete))
                ptsInterp = ptsInterp * 2;
            totalPts = totalPts + ptsInterp;
            resp.setPuntos(totalPts);
            servRespuesta.updateRespuesta(resp.getId(), resp);            
        }       


    }    
      
}
