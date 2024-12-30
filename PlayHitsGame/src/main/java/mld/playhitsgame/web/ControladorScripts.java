/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.web;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import mld.playhitsgame.correo.EmailServicioMetodos;
import mld.playhitsgame.exemplars.Partida;
import mld.playhitsgame.exemplars.StatusPartida;
import mld.playhitsgame.services.PartidaServicioMetodos;
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
    PartidaServicioMetodos servPartida;

    private final String tokenValidacion = "a3b2f10c-482d-4d7e-a5ed-2f9b7e8bcfd0";
    
    
    private void tratarBatallas(){
        
        List<Partida> batallas = servPartida.partidasBatalla();
        
        for (Partida batalla : batallas){
            if (batalla.getStatus().equals(StatusPartida.EnEspera)){
                batalla.setStatus(StatusPartida.Creada);
                LocalDateTime newfecha = LocalDateTime.now();
                newfecha = newfecha.plusHours(24);
                batalla.setFecha(Date.from(newfecha.atZone(ZoneId.systemDefault()).toInstant()));
                servPartida.updatePartida(batalla.getId(), batalla);
            }
        }        
    }    
    

    @GetMapping("/lanzarScripts")
    public ResponseEntity<Void> lanzarScripts(Model modelo, HttpServletRequest req) {

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
                try{
                    // METODOS A EJECUTAR
                    tratarBatallas();
                    /////////////////////// FIN
                    txtCorreo = "Lanzamiento de scripts OK";
                }catch(Exception ex){
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
