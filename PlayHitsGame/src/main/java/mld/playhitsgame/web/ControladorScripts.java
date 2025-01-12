/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.web;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import mld.playhitsgame.correo.EmailServicioMetodos;
import mld.playhitsgame.exemplars.Batalla;
import mld.playhitsgame.exemplars.Cancion;
import mld.playhitsgame.exemplars.Config;
import mld.playhitsgame.exemplars.Dificultad;
import mld.playhitsgame.exemplars.OpcionAnyoTmp;
import mld.playhitsgame.exemplars.OpcionInterpreteTmp;
import mld.playhitsgame.exemplars.OpcionTituloTmp;
import mld.playhitsgame.exemplars.Partida;
import mld.playhitsgame.exemplars.PtsUsuario;
import mld.playhitsgame.exemplars.Respuesta;
import mld.playhitsgame.exemplars.Ronda;
import mld.playhitsgame.exemplars.StatusBatalla;
import mld.playhitsgame.exemplars.StatusPartida;
import mld.playhitsgame.exemplars.TipoPartida;
import mld.playhitsgame.exemplars.Usuario;
import mld.playhitsgame.services.BatallaServicioMetodos;
import mld.playhitsgame.services.CancionServicioMetodos;
import mld.playhitsgame.services.ConfigServicioMetodos;
import mld.playhitsgame.services.EstrellaServicioMetodos;
import mld.playhitsgame.services.OpcionAnyoTmpServicioMetodos;
import mld.playhitsgame.services.OpcionInterpreteTmpServicioMetodos;
import mld.playhitsgame.services.OpcionTituloTmpServicioMetodos;
import mld.playhitsgame.services.PartidaServicioMetodos;
import mld.playhitsgame.services.RespuestaServicioMetodos;
import mld.playhitsgame.services.RondaServicioMetodos;
import mld.playhitsgame.services.UsuarioServicioMetodos;
import mld.playhitsgame.utilidades.Utilidades;
import static mld.playhitsgame.utilidades.Utilidades.asignarCancionesAleatorias;
import static mld.playhitsgame.utilidades.Utilidades.cancionRandom;
import static mld.playhitsgame.utilidades.Utilidades.opcionesAnyosCanciones;
import static mld.playhitsgame.utilidades.Utilidades.opcionesInterpretesCanciones;
import static mld.playhitsgame.utilidades.Utilidades.opcionesTitulosCanciones;
import static mld.playhitsgame.utilidades.Utilidades.rangoAnyosCanciones;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author miguel
 */
@Controller
public class ControladorScripts {

    @Value("${custom.mailadmin}")
    private String mailAdmin;
    @Autowired
    EmailServicioMetodos servEmail;
    @Autowired
    UsuarioServicioMetodos servUsuario;
    @Autowired
    CancionServicioMetodos servCancion;
    @Autowired
    BatallaServicioMetodos servBatalla;
    @Autowired
    PartidaServicioMetodos servPartida;
    @Autowired
    RondaServicioMetodos servRonda;
    @Autowired
    RespuestaServicioMetodos servRespuesta;
    @Autowired
    OpcionTituloTmpServicioMetodos servOpTitulo;
    @Autowired
    OpcionInterpreteTmpServicioMetodos servOpInterprete;
    @Autowired
    OpcionAnyoTmpServicioMetodos servOpAnyo;
    @Autowired
    EstrellaServicioMetodos servEstrella;
    @Autowired
    ConfigServicioMetodos servConfig;

    private final String tokenValidacion = "a3b2f10c-482d-4d7e-a5ed-2f9b7e8bcfd0";
    private int numMaxEstrellas;

    @PostConstruct
    public void init() {
        // Código a ejecutar al arrancar la aplicación
        System.out.println("Aplicación iniciada (PostConstruct ControladorScripts). Ejecutando tareas de inicio...");

        Config laConfig = servConfig.getSettings();
        // para que este valor se refresque si se cambia en la BD
        // habria que reiniciar la APP
        numMaxEstrellas = laConfig.getNumMaxEstrellas();
    }

    private void inicializarEstrellas() {

        if (servEstrella.numEstrellas() == 0) {
            for (Usuario usu : servUsuario.findAll()) {
                if (!usu.isActivo()) {
                    continue;
                }
                for (int i = 1; i <= usu.getEstrellas_old(); i++) {
                    servEstrella.darEstrella(usu, numMaxEstrellas);
                }
            }
        }
    }

    private void iniciarBatalla(Batalla batalla) {

        List<Cancion> canciones = servCancion.obtenerCanciones(batalla);

        for (Usuario usu : batalla.getUsuarios()) {
            Partida newPartida = new Partida();
            newPartida.setNombre(batalla.getNombre());
            newPartida.setTipo(TipoPartida.batalla);
            newPartida.setTema(batalla.getTema());
            newPartida.setNCanciones(canciones.size());
            newPartida.setAnyoInicial(batalla.getAnyoInicial());
            newPartida.setAnyoFinal(batalla.getAnyoFinal());
            newPartida.setDificultad(Dificultad.Normal);
            newPartida.setFecha(batalla.getFecha());
            newPartida.setMaster(usu);
            newPartida.setPublica(batalla.isPublica());
            newPartida.setRondaActual(1);
            newPartida.setInvitados(new ArrayList());
            newPartida.setBatalla(batalla);

            newPartida = servPartida.savePartida(newPartida);
            newPartida.setRondas(new ArrayList());
            for (int i = 1; i <= batalla.getNRondas(); i++) {
                Ronda newRonda = new Ronda();
                newRonda.setNumero(i);
                newRonda.setPartida(newPartida);
                newRonda.setRespuestas(new ArrayList());
                Ronda ronda = servRonda.saveRonda(newRonda);

                //Crear las respuestas
                for (Usuario usuario : newPartida.usuariosPartida()) {
                    usuario.setRespuestas(new ArrayList());
                    Respuesta newResp = new Respuesta();
                    newResp.setRonda(ronda);
                    newResp.setUsuario(usuario);
                    servRespuesta.saveRespuesta(newResp);
                }

                servRonda.updateRonda(ronda.getId(), ronda);
                newPartida.getRondas().add(ronda);

            }
            asignarCancionesAleatorias(newPartida, canciones);
            for (Ronda ronda : newPartida.getRondas()) {
                servRonda.updateRonda(ronda.getId(), ronda);
            }
            newPartida.setStatus(StatusPartida.EnCurso);
            servPartida.updatePartida(newPartida.getId(), newPartida);
            batalla.getPartidas().add(newPartida);
            int[] rangoAnyos = rangoAnyosCanciones((ArrayList<Cancion>) canciones);
            // Crear las opciones para las respuestas
            for (Ronda ronda : newPartida.getRondas()) {
                for (OpcionTituloTmp op : opcionesTitulosCanciones(ronda, true)) {
                    servOpTitulo.saveOpcionTituloTmp(op);
                }
                for (OpcionInterpreteTmp op : opcionesInterpretesCanciones(ronda, true)) {
                    servOpInterprete.saveOpcionInterpreteTmp(op);
                }
                for (OpcionAnyoTmp op : opcionesAnyosCanciones(ronda, rangoAnyos[0], rangoAnyos[1])) {
                    servOpAnyo.saveOpcionAnyoTmp(op);
                }
            }
        }
        batalla.setStatus(StatusBatalla.EnCurso);
        batalla.setFase(1);
        servBatalla.update(batalla.getId(), batalla);

    }

    private void finalizarBatalla(Batalla batalla) {

        // ASIGNAMOS PUNTOS
//        for (Usuario usu : batalla.usuariosPartida()) {
//            int pts = Utilidades.calcularPtsUsuario(usu, batalla, true);
//            usu.setPuntos(usu.getPuntos() + pts);
//            servUsuario.update(usu.getId(), usu);
//        }
//        
//        //Damos ESTRELLA al primero
//        PtsUsuario ptsPrimero = Utilidades.resultadosBatalla(batalla).getFirst();
//        servEstrella.darEstrella(ptsPrimero.getUsuario(), numMaxEstrellas);
//
//        batalla.setStatus(StatusPartida.Terminada);
//        servPartida.updatePartida(batalla.getId(), batalla);
//        servOpTitulo.deleteByPartida(batalla.getId());
//        servOpInterprete.deleteByPartida(batalla.getId());
//        servOpAnyo.deleteByPartida(batalla.getId());
    }

    private void tratarBatallas() {

        // para los cambios de status asumimos que este script
        // se lanzara diariamente a la misma hora
        List<Batalla> batallas = servBatalla.findAll();

        for (Batalla batalla : batallas) {
            switch (batalla.getStatus()) {
                case Programada -> {
                    batalla.setStatus(StatusBatalla.Inscripcion);
                    LocalDateTime newfecha = LocalDateTime.now();
                    newfecha = newfecha.plusHours(24);
                    batalla.setFecha(newfecha);
                    servBatalla.update(batalla.getId(), batalla);
                }
                case Inscripcion -> {
                    batalla.setUsuarios(new ArrayList());
                    List<Usuario> usuariosInscritos = batalla.getUsuariosInscritos();
                    for (Usuario usu : usuariosInscritos) {
                        usu.getBatallas().add(batalla);
                        batalla.getUsuarios().add(usu);
                        servUsuario.update(usu.getId(), usu);
                    }
                    servBatalla.update(batalla.getId(), batalla);
                    iniciarBatalla(batalla);
                }
                case EnCurso -> {
                    //finalizarBatalla(batalla);
                }

                default -> {
                }
            }
        }
    }

    @GetMapping("/lanzarScripts")
    public ResponseEntity<Void> lanzarScripts(Model modelo, HttpServletRequest req
    ) {

        String txtCorreo;

        if (req.getParameter("token") == null) {
            String url;
            StringBuffer requestURL = req.getRequestURL(); // Obtiene la URL completa hasta el path
            String queryString = req.getQueryString(); // Obtiene la cadena de consulta (query parameters)
            if (queryString == null) {
                url = requestURL.toString();
            } else {
                url = requestURL.append("?").append(queryString).toString();
            }
            txtCorreo = "Error lanzamiento scripts " + url;
        } else {
            String token = (String) req.getParameter("token");
            if (token.equals(tokenValidacion)) {
                try {
                    //inicializarEstrellas(); // comentar cuando este hecho

                    // METODOS A EJECUTAR  
                    tratarBatallas();
                    /////////////////////// FIN
                    txtCorreo = "Lanzamiento de scripts OK";
                } catch (Exception ex) {
                    txtCorreo = "Error en script : " + ex.getMessage();
                }
            } else {
                txtCorreo = "Error lanzamiento scripts. El token de validacion es incorrecto : " + token;
            }
        }

        Utilidades.enviarMail(servEmail, mailAdmin, "", "Jornada PlayHitsGame",
                txtCorreo, "Correo");

        return ResponseEntity.ok().build();

    }

}
