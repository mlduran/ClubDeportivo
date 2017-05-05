/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mld.clubdeportivo.controladores;

import java.util.ArrayList;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mld.clubdeportivo.base.quinielas.ApuestaQuiniela;
import mld.clubdeportivo.base.quinielas.CompeticionQuiniela;
import mld.clubdeportivo.base.quinielas.EquipoQuiniela;
import mld.clubdeportivo.base.quinielas.JornadaQuiniela;
import mld.clubdeportivo.bd.DAOException;
import mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela;
import mld.clubdeportivo.utilidades.Correo;
import mld.clubdeportivo.utilidades.UtilGenericas;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author mlopezd
 */
public class LanzarJornadaQuiniela {
    
    private ServletContext aplicacion;
    private static Logger logger = LogManager.getLogger(LanzarJornadaQuiniela.class);
    
    protected void lanzarJornadaQuiniela(HttpServletRequest req, HttpServletResponse resp,
            ServletContext appManager) throws DAOException {
    
        aplicacion = appManager;
        String ruta = aplicacion.getInitParameter("rutaficheroscarga");
        
        CompeticionQuiniela comp = JDBCDAOQuiniela.competicionActiva();
        
        if (comp == null) return;
        
        if(UtilGenericas.isJueves()){
            comprobarCumplimentacionCorrecta(comp);
        }        
        else if (UtilGenericas.isViernes()){
            JornadaQuiniela jor = JDBCDAOQuiniela.obtenerProximaJornada(comp);
            if (jor != null){
                jor.setBloqueada(true);
                JDBCDAOQuiniela.grabarJornada(jor);
            }
        }
        else{
            
            UtilesQuiniela.cargarJornadasQuiniela(ruta); 
            UtilesQuiniela.validarJornada(comp, ruta);
        }
        
        if(UtilGenericas.isMartes()){
            try {
                UtilesQuiniela.crearJornadaQuiniela(comp, ruta);
            } catch (Exception ex){
                logger.error("Error al crear jornada quiniela ".concat(ex.getMessage()));
            }
        }   
                
    }

    private void comprobarCumplimentacionCorrecta(CompeticionQuiniela comp) throws DAOException {
        
        if (comp == null) return;
        ArrayList<EquipoQuiniela> eqs = JDBCDAOQuiniela.equiposQuinielaActivos();
        JornadaQuiniela jor = JDBCDAOQuiniela.obtenerProximaJornada(comp);
        if (jor == null) return;
        
        for (EquipoQuiniela eq : eqs) {
            ArrayList<ApuestaQuiniela> apuestas = (ArrayList<ApuestaQuiniela>) JDBCDAOQuiniela.obtenerApuestas(eq, jor);
            for (ApuestaQuiniela apuesta : apuestas) {
                if (!apuesta.isCumplentadaOK()){
                    EquipoQuiniela eqQuini = JDBCDAOQuiniela.obtenerSimpleEquipoQuiniela(eq.getId());
                    String txt = "Recuerda que hoy es el ultimo dia para cumplimentar tus apuestas en la quiniela, tienes de plazo hasta las 24h.";
                    Correo.getCorreo().enviarMail("ClubDeportivo Cumplimentación Quiniela Pendiente", 
                             txt, true, eqQuini.getClub().getMail());
                    break;
                }
            }
        }
        
        
    }
     
}
