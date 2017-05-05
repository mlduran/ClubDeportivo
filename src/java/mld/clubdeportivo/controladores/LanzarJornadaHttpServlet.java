
package mld.clubdeportivo.controladores;

/**
 *
 * @author Miguel
 */

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mld.clubdeportivo.base.Club;
import mld.clubdeportivo.base.Deporte;
import mld.clubdeportivo.base.futbol8.EquipoFutbol8;
import mld.clubdeportivo.base.quinielas.EquipoQuiniela;
import mld.clubdeportivo.bd.DAOException;
import mld.clubdeportivo.bd.JDBCDAOClub;
import mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8;
import mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela;
import mld.clubdeportivo.utilidades.Correo;
import mld.clubdeportivo.utilidades.UtilGenericas;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class LanzarJornadaHttpServlet extends HttpServlet{

    private static Logger logger = LogManager.getLogger(LanzarJornadaHttpServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {
        
        boolean ok = false;
        
        ServletContext appManager = req.getServletContext();
                    
        String entorno = appManager.getInitParameter("entornoapp");
        String dirCorreo = appManager.getInitParameter("mailcontacto");
        String dir = "/Utiles/lanzarJornada.jsp";
        String txtMail = "";

        try {
            
            if ((entorno == null || (entorno != null && !entorno.equals("desarrollo"))) && !comprobarLanzamiento(req, resp)){
                logger.error("Error al lanzar Jornada: La configuracion del lanzamiento no es correcta");
                req.setAttribute("error", "La configuracion del lanzamiento no es correcta");
                dir = "/Utiles/error.jsp";
            }
            else{           
                // cerramos acceso a los usuarios
                appManager.setAttribute("simulando", "true");
                
                //UtilesHttpServlet.ejecutaBackup(appManager, "Backup"); 
                
                if (UtilesHttpServlet.isDeporteActivo(appManager, Deporte.Futbol8)){
                    LanzarJornadaFutbol8 lanzarf8 = new LanzarJornadaFutbol8();            
                    lanzarf8.lanzarJornadaFutbol8(req, resp, appManager);  
                }
                
                if (UtilesHttpServlet.isDeporteActivo(appManager, Deporte.Quiniela)){
                    LanzarJornadaQuiniela lanzarq = new LanzarJornadaQuiniela();
                    lanzarq.lanzarJornadaQuiniela(req, resp, appManager);
                }
                
                try{
                    if (UtilGenericas.isDomingo()) 
                        operacionesMantenimiento();
                }catch (Exception ex) {
                    Correo.getCorreo().enviarMail("Mantenimiento Jornada ERROR",
                            ex.getMessage(), true, dirCorreo);  
                }                
                
                appManager.setAttribute("simulando", "false");
                SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
                String fecha = sdf.format(new Date());
                appManager.setAttribute("fechUltimoLanzamiento", fecha);
                ok = true;
            }
                
        } catch (DAOException ex) {
            logger.error("ERROR DAO en Lanzamiento Jornada : " + ex.getMessage());
            logger.error(UtilGenericas.pilaError(ex));
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("errorDes", UtilGenericas.pilaError(ex));
            dir = "/Utiles/error.jsp";
            txtMail = ex.getMessage() + "<br/>" + UtilGenericas.pilaError(ex);

        } catch (Exception ex) {
            logger.error("ERROR General en Lanzamiento Jornada : " + ex.getMessage());
            logger.error(UtilGenericas.pilaError(ex));
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("errorDes", UtilGenericas.pilaError(ex));
            dir = "/Utiles/error.jsp";
            txtMail = ex.getMessage()  + "<br/>" + UtilGenericas.pilaError(ex);
        }        
        
        if (!ok){
            try {                
                // esperamos 10 seg segundos para realizar el restore
                Thread.sleep(10000);
            } catch (InterruptedException ex) {}
            // Lanzar el comando para deshacer cambios en BD
            //UtilesHttpServlet.ejecutaBackup(appManager, "Restore"); 
            Correo.getCorreo().enviarMail("Lanzamiento Jornada ERROR",
                txtMail, true, dirCorreo);  
        }
        else{
            Correo.getCorreo().enviarMail("Lanzamiento Jornada Correcta",
                "No ha habido problemas", true, dirCorreo);         
        }

        RequestDispatcher view =
                    req.getRequestDispatcher(dir);
        try {
            view.forward(req, resp);
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }

    }
    

    private boolean comprobarLanzamiento(HttpServletRequest req, 
            HttpServletResponse resp) {              
         
        boolean ok = false;
        ServletContext appManager = req.getServletContext();
        
        String codigo = (String) req.getParameter("codigo");
        String codigoConf = this.getInitParameter("codigoLanzamiento");
        String fechaUltimoLanzamiento = 
                (String) appManager.getAttribute("fechUltimoLanzamiento");
        if (fechaUltimoLanzamiento == null)
            fechaUltimoLanzamiento = "";
        
        
        SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
        String fecha = sdf.format(new Date());
        
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
        
        ArrayList<Club> listaClubs = JDBCDAOClub.listaClubs();
          
        
        // Avisos y desactivacion de equipos
        for (Club club : listaClubs) {
            
            String txtMail = "Hola, hace dias que no entras a visitarnos y la administración del club, esta pensando en hacerse cargo del mismo hasta la finalización de la temporada, en que se dará de baja, si aun quieres seguir al frente intenta acceder lo antes posible. <br/>" +
                    "Si tienes varias secciones en tu club y concretamente no te interesa mantener alguna, puedes ir a Datos Usuario y darla de baja<br/><br/>Gracias";
            
            if (club.isQuiniela() && club.getDiasSinAcceder() > EquipoQuiniela.DIAS_DESACTIVAR){
                EquipoQuiniela eq = JDBCDAOQuiniela.obtenerEquipo(club);
                JDBCDAOQuiniela.eliminarEquipoQuiniela(eq);
            }            
            else if (club.isQuiniela() && club.getDiasSinAcceder() > EquipoQuiniela.DIAS_WARNING){
                Correo.getCorreo().enviarMail("ClubDeportivo Quiniela",
                            txtMail, true, club.getMail());  
            }
            
            if (club.isFutbol8() && club.getDiasSinAcceder() > EquipoFutbol8.DIAS_DESACTIVAR){
                EquipoFutbol8 eq = JDBCDAOFutbol8.obtenerSimpleEquipoFutbol8(club);
                if (JDBCDAOFutbol8.competicionActiva(club.getGrupo(), "Liga") != null){                    
                //    eq.setActivo(false);
                //    eq.setAutomatico(true);
                //    JDBCDAOFutbol8.grabarEquipoFutbol8(eq);
                }
                else{
                    JDBCDAOFutbol8.eliminarEquipoFutbol8(eq);
                }
            }            
            else if (club.isFutbol8() && club.getDiasSinAcceder() > EquipoFutbol8.DIAS_WARNING){
                Correo.getCorreo().enviarMail("ClubDeportivo Futbol8",
                            txtMail, true, club.getMail());  
            }
            
            // Eliminamos Clubs que no tienen secciones y hace mas de 15 dias que no acceden
            if (!club.isQuiniela() && !club.isFutbol8() && !club.isBasket() && club.getDiasSinAcceder() > 15){
                JDBCDAOClub.eliminarClub(club);
            }
                
            
        }
    
    }
    
       

}
