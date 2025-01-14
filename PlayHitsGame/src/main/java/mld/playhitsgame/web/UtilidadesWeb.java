/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import mld.playhitsgame.exemplars.Partida;
import mld.playhitsgame.exemplars.Puntuacion;
import mld.playhitsgame.exemplars.Respuesta;
import mld.playhitsgame.exemplars.Ronda;
import mld.playhitsgame.exemplars.StatusPartida;
import mld.playhitsgame.exemplars.Tema;
import mld.playhitsgame.exemplars.TipoPartida;
import mld.playhitsgame.exemplars.Usuario;
import org.springframework.ui.Model;

/**
 *
 * @author miguel
 */
public class UtilidadesWeb {

    public static void eliminarOpcionesPartida(Partida partida,
            ControladorVista ctrlVista
    ) {

        ctrlVista.servOpTitulo.deleteByPartida(partida.getId());
        ctrlVista.servOpInterprete.deleteByPartida(partida.getId());
        ctrlVista.servOpAnyo.deleteByPartida(partida.getId());

    }

    public static void altaPuntuacion(Tema elTema, Usuario usuario,
            Partida partida, int pts,
            ControladorVista ctrlVista
    ) {

        // de momento solo damos da talta la puntuacion 
        // si no exite esa puntuacion y usuario, mas adelante se 
        // podria hacer solo con un usuario
        List<Puntuacion> listaPts = ctrlVista.sevrPuntuacion.obtenerPuntuacionesUsuario(
                elTema.getId(), usuario.getId());

        if (!listaPts.isEmpty()) {
            for (Puntuacion puntuacion : listaPts) {
                if (puntuacion.getPuntos() == pts) {
                    return; // si ya esxite salimos
                }
            }
        }
        Puntuacion newPts = new Puntuacion();
        newPts.setTema(elTema);
        newPts.setPuntos(pts);
        newPts.setTipoPartida(TipoPartida.personal);
        newPts.setIdUsuario(usuario.getId());
        newPts.setDificultad(partida.getDificultad());
        newPts.setAnyoInicial(partida.getAnyoInicial());
        newPts.setAnyoFinal(partida.getAnyoFinal());
        ctrlVista.sevrPuntuacion.save(newPts);
    }

    public static boolean finalizarPartidaPersonal(Partida partida, Usuario usuario,
            ControladorVista ctrlVista
    ) {

        // Devuelve true si se ha conseguido un record
        boolean esRecord = false;

        partida.setStatus(StatusPartida.Terminada);
        ctrlVista.servPartida.updatePartida(partida.getId(), partida);
        eliminarOpcionesPartida(partida, ctrlVista);

        if (partida.isEntreno()) {
            return esRecord;
        }

        int pts = partida.ptsUsuario(usuario);

        if (partida.getTema() != null && !"".equals(partida.getTema())) {
            Optional<Tema> tema = ctrlVista.servTema.findBytema(partida.getTema());
            if (tema.isPresent()) {
                Tema elTema = tema.get();
                if (elTema.getPuntos() < pts) {
                    elTema.setPuntos(pts);
                    elTema.setUsuarioRecord(usuario.getId());
                    ctrlVista.servTema.update(elTema.getId(), elTema);
                    ctrlVista.servEstrella.darEstrella(usuario, ctrlVista.numMaxEstrellas);
                    ctrlVista.servUsuario.update(usuario.getId(), usuario);
                    esRecord = true;
                }
                if (pts > 0) {
                    try {
                        altaPuntuacion(elTema, usuario, partida, pts, ctrlVista);
                    } catch (Exception e) {
                        Logger.getLogger(ControladorVista.class.getName()).
                                log(Level.SEVERE, "Error en alta de puntacion", e);
                    }
                }
            }
        }
        return esRecord;
    }

    public static void resultadosPartida(Partida partidaSesion, Model modelo) {

        HashMap<String, List<Respuesta>> resultadosPartida = new HashMap();
        HashMap<String, Integer> totales = new HashMap();
        String nomUsu;
        ArrayList lista;

        for (Ronda ronda : partidaSesion.getRondas()) {
            for (Respuesta respuesta : ronda.getRespuestas()) {
                nomUsu = respuesta.getUsuario().getNombre();
                if (!resultadosPartida.containsKey(nomUsu)) {
                    resultadosPartida.put(nomUsu, new ArrayList());
                }
                lista = (ArrayList) resultadosPartida.get(nomUsu);
                lista.add(respuesta);
                resultadosPartida.put(nomUsu, lista);
            }
        }
        //Crear la suma total
        for (String usu : resultadosPartida.keySet()) {
            int total = 0;
            for (Respuesta resp : resultadosPartida.get(usu)) {
                total = total + resp.getPuntos();
            }
            totales.put(usu, total);
        }

        modelo.addAttribute("ptstotales", totales);
        modelo.addAttribute("resultados", resultadosPartida);
    }    

}
