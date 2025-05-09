
package mld.clubdeportivo.controladores;

/**
 *
 * @author Miguel
 */

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static java.lang.Thread.sleep;
import java.text.SimpleDateFormat;
import java.util.Date;
import mld.clubdeportivo.bd.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static mld.clubdeportivo.base.Deporte.Futbol8;
import static mld.clubdeportivo.base.Deporte.Quiniela;
import static mld.clubdeportivo.base.futbol8.EquipoFutbol8.DIAS_DESACTIVAR;
import static mld.clubdeportivo.base.futbol8.EquipoFutbol8.DIAS_WARNING;
import static mld.clubdeportivo.bd.JDBCDAOClub.eliminarClub;
import static mld.clubdeportivo.bd.JDBCDAOClub.listaClubsNoAuto;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.competicionActiva;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.eliminarEquipoFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerSimpleEquipoFutbol8;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.eliminarEquipoQuiniela;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerEquipo;
import static mld.clubdeportivo.controladores.UtilesHttpServlet.isDeporteActivo;
import static mld.clubdeportivo.utilidades.Correo.getCorreo;
import static mld.clubdeportivo.utilidades.UtilGenericas.isDomingo;
import static mld.clubdeportivo.utilidades.UtilGenericas.pilaError;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LanzarJornadaHttpServlet {

    private static Logger logger = LoggerFactory.getLogger(LanzarJornadaHttpServlet.class.getName());
    
    @Value("${custom.deportesactivos}")
    private String deportesactivos;
    @Value("${custom.entornoapp}")
    private String entornoapp;
    @Value("${custom.mailcontacto}")
    private String mailcontacto;
    @Value("${custom.codigoLanzamiento}")
    private String codigoLanzamiento;
    

    @GetMapping("/lanzarJornada")
    public String doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        return processRequest(req, resp);
    }
    

    private String processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {
        
        var ok = false;
        var appManager = req.getServletContext();
        var entorno = entornoapp;
        var dirCorreo = mailcontacto;
        var dir = "/Utiles/lanzarJornada";
        var txtMail = "";

        try {
            
            if ((entorno == null || (entorno != null && !entorno.equals("desarrollo"))) && !comprobarLanzamiento(req, resp)){
                logger.error("Error al lanzar Jornada: La configuracion del lanzamiento no es correcta");
                req.setAttribute("error", "La configuracion del lanzamiento no es correcta");
                dir = "/Utiles/error";
            }
            else{           
                // cerramos acceso a los usuarios
                appManager.setAttribute("simulando", "true");
                
                //UtilesHttpServlet.ejecutaBackup(appManager, "Backup"); 
                
                if (isDeporteActivo(deportesactivos, Futbol8)){
                    var lanzarf8 = new LanzarJornadaFutbol8();            
                    lanzarf8.lanzarJornadaFutbol8(req, resp, appManager);  
                }
                
                if (isDeporteActivo(deportesactivos, Quiniela)){
                    var lanzarq = new LanzarJornadaQuiniela();
                    lanzarq.lanzarJornadaQuiniela(req, resp, appManager);
                }
                
                try{
                    if (isDomingo()) 
                        operacionesMantenimiento();
                }catch (Exception ex) {
                    getCorreo().enviarMail("Mantenimiento Jornada ERROR",
                            ex.getMessage(), true, dirCorreo);  
                }                
                
                appManager.setAttribute("simulando", "false");
                var sdf =new SimpleDateFormat("dd/MM/yyyy");
                var fecha = sdf.format(new Date());
                appManager.setAttribute("fechUltimoLanzamiento", fecha);
                ok = true;
            }
                
        } catch (DAOException ex) {
            logger.error("ERROR DAO en Lanzamiento Jornada : " + ex.getMessage());
            logger.error(pilaError(ex));
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("errorDes", pilaError(ex));
            dir = "/Utiles/error.jsp";
            txtMail = ex.getMessage() + "<br/>" + pilaError(ex);

        } catch (Exception ex) {
            logger.error("ERROR General en Lanzamiento Jornada : " + ex.getMessage());
            logger.error(pilaError(ex));
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("errorDes", pilaError(ex));
            dir = "/Utiles/error";
            txtMail = ex.getMessage()  + "<br/>" + pilaError(ex);
        }        
        
        if (!ok){
            try {                
                // esperamos 10 seg segundos para realizar el restore
                sleep(10000);
            } catch (InterruptedException ex) {}
            // Lanzar el comando para deshacer cambios en BD
            //UtilesHttpServlet.ejecutaBackup(appManager, "Restore"); 
            getCorreo().enviarMail("Lanzamiento Jornada ERROR",
                txtMail, true, dirCorreo);  
        }
        else{
            getCorreo().enviarMail("Lanzamiento Jornada Correcta",
                "No ha habido problemas", true, dirCorreo);         
        }

        return dir;

    }
    

    private boolean comprobarLanzamiento(HttpServletRequest req, 
            HttpServletResponse resp) {              
         
        var ok = false;
        var appManager = req.getServletContext();
        var codigo = (String) req.getParameter("codigo");
        var codigoConf = codigoLanzamiento;
        var fechaUltimoLanzamiento = 
                (String) appManager.getAttribute("fechUltimoLanzamiento");
        if (fechaUltimoLanzamiento == null)
            fechaUltimoLanzamiento = "";
        
        
        var sdf =new SimpleDateFormat("dd/MM/yyyy");
        var fecha = sdf.format(new Date());
        
        if ((appManager.getAttribute("simulando") != null && 
                appManager.getAttribute("simulando").equals("true")) ||
                fecha.equals(fechaUltimoLanzamiento)) {
            // NO HACEMOS NADA
        }
        else{
            if (codigo != null)
                ok = codigo.equals(codigoConf);
        }

        return ok;

    }

    
    
    private void operacionesMantenimiento() throws DAOException {              
        
        var listaClubs = listaClubsNoAuto();
        // Avisos y desactivacion de equipos
        for (var club : listaClubs) {
            
            var txtMail = "Hola, hace dias que no entras a visitarnos y la administración del club, esta pensando en hacerse cargo del mismo hasta la finalización de la temporada, en que se dará de baja, si aun quieres seguir al frente intenta acceder lo antes posible. <br/>" +
                    "Si tienes varias secciones en tu club y concretamente no te interesa mantener alguna, puedes ir a Datos Usuario y darla de baja<br/><br/>Gracias";
            
            if (club.isQuiniela() && club.getDiasSinAcceder() > DIAS_DESACTIVAR){
                var eq = obtenerEquipo(club);
                eliminarEquipoQuiniela(eq);
            }            
            else if (club.isQuiniela() && club.getDiasSinAcceder() > DIAS_WARNING){
                getCorreo().enviarMail("ClubDeportivo Quiniela",
                            txtMail, true, club.getMail());  
            }
            
            if (club.isFutbol8() && club.getDiasSinAcceder() > DIAS_DESACTIVAR){
                var eq = obtenerSimpleEquipoFutbol8(club);
                if (competicionActiva(club.getGrupo(), "Liga") != null){                    
                //    eq.setActivo(false);
                //    eq.setAutomatico(true);
                //    JDBCDAOFutbol8.grabarEquipoFutbol8(eq);
                }
                else{
                    eliminarEquipoFutbol8(eq);
                }
            }            
            else if (club.isFutbol8() && club.getDiasSinAcceder() > DIAS_WARNING){
                getCorreo().enviarMail("ClubDeportivo Futbol8",
                            txtMail, true, club.getMail());  
            }
            
            // Eliminamos Clubs que no tienen secciones y hace mas de 15 dias que no acceden
            if (!club.isQuiniela() && !club.isFutbol8() && !club.isFutbol8() && club.getDiasSinAcceder() > 15){
                eliminarClub(club);
            }
                
            
        }
    
    }
    
       

}
