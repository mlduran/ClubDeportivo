/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mld.clubdeportivo.controladores;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import mld.clubdeportivo.base.quinielas.ApuestaQuiniela;
import mld.clubdeportivo.base.quinielas.CompeticionQuiniela;
import mld.clubdeportivo.bd.DAOException;
import java.util.logging.*;
import static java.util.logging.Logger.getLogger;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.competicionActiva;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.equiposQuinielaActivos;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.grabarJornada;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerApuestas;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerProximaJornada;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerSimpleEquipoQuiniela;
import static mld.clubdeportivo.utilidades.Correo.getCorreo;
import static mld.clubdeportivo.utilidades.UtilGenericas.isJueves;
import static mld.clubdeportivo.utilidades.UtilGenericas.isViernes;

/**
 *
 * @author mlopezd
 */
public class LanzarJornadaQuiniela {
    
    private ServletContext aplicacion;
    private static Logger logger = getLogger(LanzarJornadaQuiniela.class.getName());
    
    protected void lanzarJornadaQuiniela(HttpServletRequest req, HttpServletResponse resp,
            ServletContext appManager) throws DAOException {
    
        aplicacion = appManager;
        var ruta = aplicacion.getInitParameter("rutaficheroscarga");
        var comp = competicionActiva();
        
        if (comp == null) return;
        
        if(isJueves()){
            comprobarCumplimentacionCorrecta(comp);
        }        
        else if (isViernes()){
            var jor = obtenerProximaJornada(comp);
            if (jor != null){
                jor.setBloqueada(true);
                grabarJornada(jor);
            }
        }
        else{
            
            //UtilesQuiniela.cargarJornadasQuiniela(ruta); 
            //UtilesQuiniela.validarJornada(comp);
        }
        
        /*
        if(UtilGenericas.isMartes()){
            try {
                UtilesQuiniela.crearJornadaQuiniela(comp, ruta);
            } catch (Exception ex){
                logger.error("Error al crear jornada quiniela ".concat(ex.getMessage()));
            }
        }   
        */
                
    }

    private void comprobarCumplimentacionCorrecta(CompeticionQuiniela comp) throws DAOException {
        
        if (comp == null) return;
        var eqs = equiposQuinielaActivos();
        var jor = obtenerProximaJornada(comp);
        if (jor == null) return;
        
        for (var eq : eqs) {
            var apuestas = (ArrayList<ApuestaQuiniela>) obtenerApuestas(eq, jor);
            for (var apuesta : apuestas) {
                if (!apuesta.isCumplentadaOK()){
                    var eqQuini = obtenerSimpleEquipoQuiniela(eq.getId());
                    var txt = "Recuerda que hoy es el ultimo dia para cumplimentar tus apuestas en la quiniela, tienes de plazo hasta las 24h.";
                    getCorreo().enviarMail("ClubDeportivo Cumplimentación Quiniela Pendiente", 
                             txt, true, eqQuini.getClub().getMail());
                    break;
                }
            }
        }
        
        
    }
     
}
