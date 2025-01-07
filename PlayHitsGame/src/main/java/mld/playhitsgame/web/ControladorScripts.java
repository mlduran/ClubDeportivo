/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.web;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mld.playhitsgame.correo.EmailServicioMetodos;
import mld.playhitsgame.exemplars.Config;
import mld.playhitsgame.exemplars.Partida;
import mld.playhitsgame.exemplars.PtsUsuario;
import mld.playhitsgame.exemplars.Respuesta;
import mld.playhitsgame.exemplars.Ronda;
import mld.playhitsgame.exemplars.StatusPartida;
import mld.playhitsgame.exemplars.Usuario;
import mld.playhitsgame.services.ConfigServicioMetodos;
import mld.playhitsgame.services.EstrellaServicioMetodos;
import mld.playhitsgame.services.OpcionAnyoTmpServicioMetodos;
import mld.playhitsgame.services.OpcionInterpreteTmpServicioMetodos;
import mld.playhitsgame.services.OpcionTituloTmpServicioMetodos;
import mld.playhitsgame.services.PartidaServicioMetodos;
import mld.playhitsgame.services.RespuestaServicioMetodos;
import mld.playhitsgame.services.UsuarioServicioMetodos;
import mld.playhitsgame.utilidades.Utilidades;
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
    PartidaServicioMetodos servPartida;
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
    
    private void inicializarEstrellas(){
        
        if (servEstrella.numEstrellas() == 0){
            for (Usuario usu : servUsuario.findAll()){
                if (!usu.isActivo()) continue;
                for (int i = 1; i <= usu.getEstrellas(); i++) {
                    servEstrella.darEstrella(usu.getId(), numMaxEstrellas);
                }
            }
        }        
    }
    
    

    private void finalizarBatalla(Partida batalla) {
        
        // ASIGNAMOS PUNTOS
        for (Usuario usu : batalla.usuariosPartida()) {
            int pts = Utilidades.calcularPtsUsuario(usu, batalla, true);
            usu.setPuntos(usu.getPuntos() + pts);
            servUsuario.update(usu.getId(), usu);
        }
        
        //Damos ESTRELLA al primero
        PtsUsuario ptsPrimero = Utilidades.resultadosBatalla(batalla).getFirst();
        servEstrella.darEstrella(ptsPrimero.getUsuario().getId(), numMaxEstrellas);

        batalla.setStatus(StatusPartida.Terminada);
        servPartida.updatePartida(batalla.getId(), batalla);
        servOpTitulo.deleteByPartida(batalla.getId());
        servOpInterprete.deleteByPartida(batalla.getId());
        servOpAnyo.deleteByPartida(batalla.getId());

    }

    private void tratarBatallas() {

        // para los cambios de status asumimos que este script
        // se lanzara diariamente a la misma hora
        List<Partida> batallas = servPartida.partidasBatalla();

        for (Partida batalla : batallas) {
            switch (batalla.getStatus()) {
                case EnEspera -> {
                    batalla.setStatus(StatusPartida.Creada);
                    LocalDateTime newfecha = LocalDateTime.now();
                    newfecha = newfecha.plusHours(24);
                    batalla.setFecha(newfecha);
                    servPartida.updatePartida(batalla.getId(), batalla);
                }
                case Creada -> {
                    for (Ronda ronda : batalla.getRondas()) {
                        for (Usuario usuario : batalla.getInvitados()) {
                            usuario.setRespuestas(new ArrayList());
                            Respuesta newResp = new Respuesta();
                            newResp.setRonda(ronda);
                            newResp.setUsuario(usuario);
                            servRespuesta.saveRespuesta(newResp);
                        }
                    }
                    batalla.setStatus(StatusPartida.EnCurso);
                    servPartida.updatePartida(batalla.getId(), batalla);
                }
                case EnCurso -> {
                    finalizarBatalla(batalla);
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
                    // METODOS A EJECUTAR                    
                    inicializarEstrellas(); // comentar cuando este hecho
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
