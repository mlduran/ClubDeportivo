/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import mld.playhitsgame.utilidades.Utilidades;
import mld.playhitsgame.exemplars.Cancion;
import mld.playhitsgame.exemplars.Dificultad;
import mld.playhitsgame.exemplars.Partida;
import mld.playhitsgame.exemplars.Respuesta;
import mld.playhitsgame.exemplars.Ronda;
import mld.playhitsgame.exemplars.StatusPartida;
import mld.playhitsgame.exemplars.Usuario;
import mld.playhitsgame.services.CancionServicioMetodos;
import mld.playhitsgame.services.OpcionAnyoTmpServicioMetodos;
import mld.playhitsgame.services.OpcionInterpreteTmpServicioMetodos;
import mld.playhitsgame.services.OpcionTituloTmpServicioMetodos;
import mld.playhitsgame.services.PartidaServicioMetodos;
import mld.playhitsgame.services.RespuestaServicioMetodos;
import mld.playhitsgame.services.RondaServicioMetodos;
import mld.playhitsgame.services.UsuarioServicioMetodos;
import mld.playhitsgame.websocket.UsuarioWS;
import mld.playhitsgame.seguridad.UsuarioRol;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class WebsocketControler {

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
    @Autowired
    OpcionTituloTmpServicioMetodos servOpTitulo;
    @Autowired
    OpcionInterpreteTmpServicioMetodos servOpInterprete;
    @Autowired
    OpcionAnyoTmpServicioMetodos servOpAnyo;

    private static final HashMap<Long, Set<UsuarioWS>> partidas = new HashMap();
    private static final HashMap<Long, Integer> nRespuestas = new HashMap();
    private static final HashMap<Long, Boolean> cuentaAtras = new HashMap();

    @MessageMapping("/partida/{id_partida}/usuario/{id_usuario}")
    @SendTo("/tema/partida/{id_partida}")
    public String getMessage(@DestinationVariable String id_usuario,
            @DestinationVariable String id_partida, String message) {

        // Respuestas en op : 
        // "respuestas" --> "usuarios" lista usuarios que han respondido
        // "acabar" -->     "primeroCancion" y "primeroInterprete"
        // "nueva" -->      "primeroCancion" y "primeroInterprete"
        JSONObject obJson = null;
        JSONObject obJsonSalida = new JSONObject();
        try {
            obJson = new JSONObject(message);
        } catch (JSONException jSONException) {
            log.info(new Date() + " Error al obtener json : " + message);
        }

        if (obJson == null || obJson.isEmpty()) {
            return obJsonSalida.toString();
        }

        obJsonSalida = tratarEntradaWS(obJson, obJsonSalida);

        return obJsonSalida.toString();

    }

    public static Set<UsuarioWS> usuariosPartida(Long idPartida) {

        return partidas.get(idPartida);
    }

    public static UsuarioWS anyadirPartidaUsuario(long partida, long idUsuario, String nombre) {

        if (partidas.get(partida) == null) {
            partidas.put(partida, new HashSet());
        }
        UsuarioWS usu = new UsuarioWS();
        usu.setId(idUsuario);
        usu.setUsuario(nombre);
        partidas.get(partida).add(usu);
        nRespuestas.put(partida, 0);

        return usu;
    }

    public static UsuarioWS buscarUsuarioWS(Long idPartida, Long idUsuario) {

        UsuarioWS usuWS = null;

        if (partidas.get(idPartida) != null) {
            for (UsuarioWS usu : usuariosPartida(idPartida)) {
                if (usu.getId() == idUsuario) {
                    usuWS = usu;
                    break;
                }
            }
        }
        return usuWS;
    }

    public static void limpiarPartida(long partida) {

        partidas.remove(partida);
    }

    public static void limpiarDatos(long partida) {

        for (UsuarioWS usuWS : partidas.get(partida)) {
            usuWS.setOrden(0);
            usuWS.setRespAnyo(false);
            usuWS.setRespInterprete(false);
            usuWS.setRespTitulo(false);
        }

    }

    public static JSONObject usuariosRespuestasCompletadas(long partida, JSONObject obJsonSalida) {

        String usuarios = "";
        boolean primero = true;
        for (UsuarioWS usu : usuariosPartida(partida)) {
            if (usu.isTodoRespondido()) {
                if (primero) {
                    primero = false;
                } else {
                    usuarios = usuarios + ", ";
                }
                usuarios = usuarios + usu.getUsuario();
            }
        }

        obJsonSalida.put("op", "respuestas");
        obJsonSalida.put("usuarios", usuarios);
        return obJsonSalida;
    }

    public static boolean isTodoCompletado(long partida) {

        boolean completo = true;
        for (UsuarioWS usu : usuariosPartida(partida)) {
            if (!usu.isTodoRespondido()) {
                completo = false;
                break;
            }
        }
        return completo;
    }

    private UsuarioWS registrarPeticionWS(JSONObject obJson) {

        UsuarioWS usuWS = buscarUsuarioWS(
                obJson.getLong("idPartida"), obJson.getLong("idUsuario"));

        if (usuWS == null) {
            Usuario usuBD = obtenerUsuario(obJson.getLong("idUsuario"));
            usuWS = anyadirPartidaUsuario(
                    obJson.getLong("idPartida"),
                    obJson.getLong("idUsuario"),
                    usuBD.getNombre()
            );
            log.info(new Date() + " Se ha conectado al WebSocket el usuario " + usuWS.getUsuario());
        }

        return usuWS;
    }

    private JSONObject tratarEntradaWS(JSONObject obJson, JSONObject obJsonSalida) {

        UsuarioWS usuarioWS = registrarPeticionWS(obJson);

        if ("alta".equals(obJson.getString("op"))) {
            cuentaAtras.put(obJson.getLong("idPartida"), false);
        }
        if ("playerOFF".equals(obJson.getString("op"))) {
            obJsonSalida.put("op", "playerOFF");
            actualizarPlay(obJson.getLong("idPartida"), false);
            return obJsonSalida;
        }
        if ("playerON".equals(obJson.getString("op"))) {
            obJsonSalida.put("op", "playerON");
            actualizarPlay(obJson.getLong("idPartida"), true);
            return obJsonSalida;
        }
        if ("cuentaAtrasResponder".equals(obJson.getString("op"))) {
            if (!cuentaAtras.get(obJson.getLong("idPartida"))) {
                obJsonSalida.put("op", "cuentaAtrasResponder");
                cuentaAtras.put(obJson.getLong("idPartida"), true);
            }
            return obJsonSalida;
        }
        if ("mostrarOpciones".equals(obJson.getString("op"))) {
            obJsonSalida.put("op", "mostrarOpciones");
            return obJsonSalida;
        }
        if ("acabaronda".equals(obJson.getString("op"))) {
            pasarSiguienteRonda(obJson.getLong("idPartida"), obJsonSalida);
            return obJsonSalida;
        }
        if ("salirdepartida".equals(obJson.getString("op"))) {
            forzarSalirDePartida(obJson.getLong("idPartida"), obJson.getLong("idUsuario"), obJsonSalida);
            return obJsonSalida;
        }

        if ("titulo".equals(obJson.getString("op"))) {
            responderTitulo(
                    obJson.getLong("idPartida"),
                    obJson.getLong("idUsuario"),
                    obJson.getLong("valor"));
            usuarioWS.setRespTitulo(true);
        }

        if ("interprete".equals(obJson.getString("op"))) {
            responderInterprete(
                    obJson.getLong("idPartida"),
                    obJson.getLong("idUsuario"),
                    obJson.getLong("valor"));
            usuarioWS.setRespInterprete(true);
        }

        if ("anyo".equals(obJson.getString("op"))) {
            responderAnyo(
                    obJson.getLong("idPartida"),
                    obJson.getLong("idUsuario"),
                    obJson.getInt("valor"));
            usuarioWS.setRespAnyo(true);
        }

        if (usuarioWS.isTodoRespondido()) {
            int nr = nRespuestas.get(obJson.getLong("idPartida"));
            usuarioWS.setOrden(nr + 1);
            nRespuestas.put(obJson.getLong("idPartida"), nr + 1);
        }

        if (isTodoCompletado(obJson.getLong("idPartida"))) {
            obJsonSalida = pasarSiguienteRonda(obJson.getLong("idPartida"), obJsonSalida);
        } else {
            obJsonSalida = usuariosRespuestasCompletadas(obJson.getLong("idPartida"), obJsonSalida);
        }

        return obJsonSalida;
    }

    private Usuario obtenerUsuario(Long idUsuario) {

        Optional<Usuario> usuBD = servUsuario.findById(idUsuario);
        return usuBD.get();
    }

    private Cancion obtenerCancion(long idCancion) {

        Optional<Cancion> can = servCancion.findById(idCancion);
        Cancion cancion = can.get();
        return cancion;
    }

    private Respuesta obtenerRespuesta(long idPartida, long idUsuario) {

        Optional<Usuario> usu = servUsuario.findById(idUsuario);
        Usuario usuario = usu.get();
        Optional<Partida> part = servPartida.findById(idPartida);
        Partida partida = part.get();
        Ronda rondaActiva = partida.rondaActiva();
        Respuesta resp = servRespuesta.buscarPorRondaUsuario(rondaActiva.getId(), usuario.getId());
        return resp;
    }

    private Partida obtenerPartida(long idPartida) {

        Optional<Partida> part = servPartida.findById(idPartida);
        return part.get();
    }

    private void responderTitulo(long idPartida, long idUsuario, long idCancion) {

        Respuesta resp = obtenerRespuesta(idPartida, idUsuario);
        Cancion cancion = obtenerCancion(idCancion);
        resp.setTitulo(cancion.getTitulo());
        servRespuesta.updateRespuesta(resp.getId(), resp);
    }

    private void responderInterprete(long idPartida, long idUsuario, long idCancion) {

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

    private JSONObject pasarSiguienteRonda(Long idPartida, JSONObject obJsonSalida) {
        // A este metodo llegamos si todos los usuarios conectados
        // han respondido, eso no quiere decir que todos esten
        // eso se tendra que tratar mas adelante pero de momento
        // cambiaremos de ronda

        Partida partida = obtenerPartida(idPartida);

        Ronda rondaActiva = partida.rondaActiva();

        boolean acabar;
        //if (rondaActiva.isTodasLasRespuestasOK()){
        if (true) { // de momento lo damos por bueno 
            obJsonSalida = asignarPuntuacion(rondaActiva, obJsonSalida);
            rondaActiva.setCompletada(true);
            servRonda.updateRonda(rondaActiva.getId(), rondaActiva);

            if (partida.hayMasRondas()) {
                partida.pasarSiguienteRonda();
                acabar = false;
            } else {
                partida.setStatus(StatusPartida.Terminada);
                eliminarOpcionesPartida(partida);
                acabar = true;
            }
            servPartida.updatePartida(partida.getId(), partida);
        }

        if (acabar) {
            //asignarPuntuacionesUsuario(idPartida);
            asignarGanador(idPartida);
            limpiarPartida(idPartida);
            obJsonSalida.put("op", "acabar");
            obJsonSalida.put("idPartida", idPartida);
        } else {
            limpiarDatos(idPartida);
            obJsonSalida.put("op", "nueva");
        }

        return obJsonSalida;
    }

    private JSONObject forzarSalirDePartida(Long idPartida, Long idUsuario, JSONObject obJsonSalida) {
        // A este metodo llegamos si todos los usuarios conectados
        // han respondido, eso no quiere decir que todos esten
        // eso se tendra que tratar mas adelante pero de momento
        // cambiaremos de ronda

        Partida partida = obtenerPartida(idPartida);
        Usuario usuario = obtenerUsuario(idUsuario);
        
        for (Ronda ronda : partida.getRondas()){
            for (Respuesta resp : ronda.getRespuestas()){
                if (resp.getUsuario().equals(usuario))
                    servRespuesta.deleteRespuesta(resp.getId());
                
            }            
        }        
        
        partida.getInvitados().remove(usuario);
        servPartida.updatePartida(partida.getId(), partida);
        //usuario.getPartidasInvitado().remove(partida);
        //servUsuario.update(usuario.getId(), usuario);

        obJsonSalida.put("op", "salirdepartida");
        obJsonSalida.put("idPartida", idPartida);

        return obJsonSalida;
    }

    private Usuario primerUsuarioAcertarAnyo(Ronda rondaActiva) {

        List<Respuesta> respuestas = rondaActiva.getRespuestas();
        Set<UsuarioWS> listaUsuariosWS = partidas.get(rondaActiva.getPartida().getId());
        List<UsuarioWS> usuariosWS = new ArrayList<>(listaUsuariosWS);
        Collections.sort(usuariosWS);
        Usuario usuarioAcertante = null;
        if (usuariosWS.size() > 1) {
            for (UsuarioWS usuWS : usuariosWS) {
                for (Respuesta res : respuestas) {
                    if (res.getAnyo() == 0) {
                        continue;
                    }
                    if (res.getAnyo() == rondaActiva.getCancion().getAnyo()) {
                        if (res.getUsuario().getId().equals(usuWS.getId())) {
                            usuarioAcertante = res.getUsuario();
                            break;
                        }
                    }
                }
                if (usuarioAcertante != null) {
                    break;
                }
            }
        }
        return usuarioAcertante;
    }

    private Usuario primerUsuarioAcertarTitulo(Ronda rondaActiva) {

        List<Respuesta> respuestas = rondaActiva.getRespuestas();
        Set<UsuarioWS> listaUsuariosWS = partidas.get(rondaActiva.getPartida().getId());
        List<UsuarioWS> usuariosWS = new ArrayList<>(listaUsuariosWS);
        Collections.sort(usuariosWS);
        Usuario usuarioAcertante = null;
        if (usuariosWS.size() > 1) {
            for (UsuarioWS usuWS : usuariosWS) {
                for (Respuesta res : respuestas) {
                    if (res.getTitulo() == null) {
                        continue;
                    }
                    if (res.getTitulo().equals(rondaActiva.getCancion().getTitulo())) {
                        if (res.getUsuario().getId().equals(usuWS.getId())) {
                            usuarioAcertante = res.getUsuario();
                            break;
                        }
                    }
                }
                if (usuarioAcertante != null) {
                    break;
                }
            }
        }
        return usuarioAcertante;
    }

    private Usuario primerUsuarioAcertarInterprete(Ronda rondaActiva) {

        List<Respuesta> respuestas = rondaActiva.getRespuestas();
        Set<UsuarioWS> listaUsuariosWS = partidas.get(rondaActiva.getPartida().getId());
        List<UsuarioWS> usuariosWS = new ArrayList<>(listaUsuariosWS);
        Collections.sort(usuariosWS);
        Usuario usuarioAcertante = null;
        if (usuariosWS.size() > 1) {
            for (UsuarioWS usuWS : usuariosWS) {
                for (Respuesta res : respuestas) {
                    if (res.getInterprete() == null) {
                        continue;
                    }
                    if (res.getInterprete().equals(rondaActiva.getCancion().getInterprete())) {
                        if (res.getUsuario().getId().equals(usuWS.getId())) {
                            usuarioAcertante = res.getUsuario();
                            break;
                        }
                    }
                }
                if (usuarioAcertante != null) {
                    break;
                }
            }
        }
        return usuarioAcertante;
    }

    private JSONObject asignarPuntuacion(Ronda rondaActiva, JSONObject jsonRespuesta) {

        Cancion cancion = rondaActiva.getCancion();
        int totalPts;
        int ptsAnyo;
        int ptsTitulo;
        int ptsInterp;

        Usuario primerAcertanteTitulo = primerUsuarioAcertarTitulo(rondaActiva);
        Usuario primerAcertanteInterprete = primerUsuarioAcertarInterprete(rondaActiva);
        Usuario primerAcertanteAnyo = primerUsuarioAcertarAnyo(rondaActiva);

        if (primerAcertanteTitulo != null) {
            jsonRespuesta.put("primeroCancion", primerAcertanteTitulo.getNombre());
        }
        if (primerAcertanteInterprete != null) {
            jsonRespuesta.put("primeroInterprete", primerAcertanteInterprete.getNombre());
        }
        if (primerAcertanteAnyo != null) {
            jsonRespuesta.put("primeroAnyo", primerAcertanteAnyo.getNombre());
        }

        for (Respuesta resp : rondaActiva.getRespuestas()) {
            ptsAnyo = Utilidades.calcularPtsPorAnyo(resp.getAnyo(), cancion, Dificultad.Normal);
            if (resp.getUsuario().equals(primerAcertanteAnyo)) {
                ptsAnyo = ptsAnyo * 2;
            }
            if (ptsAnyo > 0) {
                resp.setAnyoOk(true);
            }
            ptsTitulo = Utilidades.calcularPtsPorTitulo(resp.getTitulo(), cancion, Dificultad.Normal, false);
            if (resp.getUsuario().equals(primerAcertanteTitulo)) {
                ptsTitulo = ptsTitulo * 2;
            }
            if (ptsTitulo > 0) {
                resp.setTituloOk(true);
            }
            ptsInterp = Utilidades.calcularPtsPorInterprete(resp.getInterprete(), cancion, Dificultad.Normal, false);
            if (resp.getUsuario().equals(primerAcertanteInterprete)) {
                ptsInterp = ptsInterp * 2;
            }
            if (ptsInterp > 0) {
                resp.setInterpreteOk(true);
            }
            totalPts = ptsAnyo + ptsInterp + ptsTitulo;
            resp.setPuntos(totalPts);
            servRespuesta.updateRespuesta(resp.getId(), resp);
        }
        return jsonRespuesta;
    }

    private void asignarGanador(Long idPartida) {

        Optional<Partida> partida = servPartida.findById(idPartida);
        Partida p = partida.get();
        p.asignarGanador();
        servPartida.updatePartida(idPartida, p);

    }

    private void eliminarOpcionesPartida(Partida partida) {

        servOpTitulo.deleteByPartida(partida.getId());
        servOpInterprete.deleteByPartida(partida.getId());
        servOpAnyo.deleteByPartida(partida.getId());

    }

    private void actualizarPlay(Long idPartida, boolean valor) {

        Optional<Partida> partida = servPartida.findById(idPartida);
        if (partida.isPresent()) {
            partida.get().setActivarPlay(valor);
            servPartida.updatePartida(idPartida, partida.get());
        }

    }

}
