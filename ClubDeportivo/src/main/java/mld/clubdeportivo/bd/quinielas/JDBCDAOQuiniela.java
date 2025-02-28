package mld.clubdeportivo.bd.quinielas;


import java.util.*;
import mld.clubdeportivo.base.*;
import mld.clubdeportivo.base.quinielas.*;
import mld.clubdeportivo.bd.ClubDAO;
import mld.clubdeportivo.bd.DAOException;
import mld.clubdeportivo.bd.JDBCDAOClub;
import mld.clubdeportivo.bd.JDBCDAOGrupo;
import java.util.logging.*;
import static java.util.logging.Logger.getLogger;
import static mld.clubdeportivo.bd.JDBCDAOClub.obtenerSimpleClub;
import static mld.clubdeportivo.bd.JDBCDAOGrupo.obtenerSimpleGrupo;

/**
 *
 * @author mlopezd
 */
public class JDBCDAOQuiniela {

    private static final Logger logApp
            = getLogger(JDBCDAOQuiniela.class.getName());

   
    private JDBCDAOQuiniela(){}
    
    
     public static int numeroCompeticionesGanadas(Club club) throws DAOException{
        
        var dao = new CompeticionQuinielaDAO();
        
        return dao.getCompeticionesGanadas(club); 
        
    }
    
    
     public static long idEquipoQuiniela(Club club) throws DAOException {

        var dao = new EquipoQuinielaDAO();

        return dao.idEquipoQuiniela(club);

    }
     
     public static ArrayList<EquipoQuiniela> equiposQuinielaActivos() throws DAOException{
         
         var daoeqs = new EquipoQuinielaDAO();
         return daoeqs.getEquiposQuinielaActivos();        
         
     }
    

   

    public static EstadisticaQuiniela obtenerEstadistica(
            EquipoQuiniela eq, CompeticionQuiniela comp, JornadaQuiniela jor) throws DAOException{

        var daoest = new EstadisticaQuinielaDAO();
        var est = daoest.getEstadisticaByJornada(eq.getNombre(), 
                comp.getNombre(), jor.getDescripcion());
        return est;

    }
    
      
    public static ArrayList<ApuestaQuiniela> obtenerApuestasPorJornada(
            EquipoQuiniela eq, JornadaQuiniela jornada) throws DAOException{
        
        var daoap = new ApuestaQuinielaDAO();
        return  (ArrayList<ApuestaQuiniela>) daoap.getApuestasByJornada(eq, jornada);
        
        
    }
    
     public static PuntuacionQuiniela obtenerPuntosPorEquipo(
            CompeticionQuiniela comp, EquipoQuiniela eq) throws DAOException{
        
        var daopt = new PuntuacionQuinielaDAO();
        return  daopt.getPuntosByEquipo(comp, eq);
        
        
    }

   
    
    public static CompeticionQuiniela obtenerCompeticionPorId(long id)
             throws DAOException {

         var dao = new CompeticionQuinielaDAO();

         return dao.getCompeticionById(id);

     }
   
    public static CompeticionQuiniela competicionActiva()
             throws DAOException {

         var dao = new CompeticionQuinielaDAO();

         return dao.getCompeticionActiva();

     }

     public static ArrayList<CompeticionQuiniela> competicionesNoActivas()
             throws DAOException {

         var dao = new CompeticionQuinielaDAO();

         return dao.getCompeticionesNoActivas();

     }
     
     public static ArrayList<EquipoQuiniela> obtenerEquiposActivos() throws DAOException{
         
         var daoeq = new EquipoQuinielaDAO();
         
         return daoeq.getEquiposQuinielaActivos();
     }
     
     
     public static EquipoQuiniela obtenerEquipo(Club club) throws DAOException{
         
         var daoeq = new EquipoQuinielaDAO();
         return daoeq.getEquipoQuinielaByClub(club);
         
         
     }

     public static EquipoQuiniela obtenerSimpleEquipoQuiniela(long id)
            throws DAOException{
        // Devuelve equipo con sus relaciones directas y grupo

         var daoeq = new EquipoQuinielaDAO();
         var daoclub = new ClubDAO();
         var eq = daoeq.getEquipoQuinielaById(id);
         var idclub = daoeq.idClub(eq);
         var club = obtenerSimpleClub(idclub);
         var idgrp = daoclub.idGrupo(club);
         var grp = obtenerSimpleGrupo(idgrp);
         eq.setClub(club);
         club.setGrupo(grp);
         grp.getClubs().add(club);

         return eq;

    }

    public static ArrayList<EquipoQuiniela> listaEquiposQuiniela(Grupo grp)
            throws DAOException {
        // Devuelve lista de equipos sin relaciones

        var dao = new EquipoQuinielaDAO();
        var lista =
                dao.getEquiposQuinielaActivos();
        var listaQuini = new ArrayList<EquipoQuiniela>();
        for (var eq : lista) {
            var idclub = dao.idClub(eq);
            var club = obtenerSimpleClub(idclub);
            eq.setClub(club);
            if ((grp == null) || (grp != null && club.getGrupo().equals(grp)))
                listaQuini.add(eq);
        }        

        return listaQuini;

    }
    
     public static ArrayList<EquipoQuiniela> listaEquiposQuiniela()
            throws DAOException {
         
         return listaEquiposQuiniela(null);
         
     }

    public static ArrayList<PuntuacionQuiniela> clasificacionQuiniela(CompeticionQuiniela comp, boolean isGeneral)
            throws DAOException {

        var daoeq = new EquipoQuinielaDAO();
        var daopunt = new PuntuacionQuinielaDAO();
        var ptsGeneral = new ArrayList<PuntuacionQuiniela>();
        if (comp != null){
            var pts = daopunt.getPuntuacionesCompeticion(comp, isGeneral);
            for (var pt : pts) {
                var eq =  daoeq.getEquipoQuinielaById(daopunt.idEquipo(pt));
                if (eq == null) continue;
                ptsGeneral.add(pt);
                pt.setEquipo(eq);
                var puntos = new ArrayList<PuntuacionQuiniela>();
                puntos.add(pt);
                eq.setPuntuaciones(puntos);
                var club = obtenerSimpleClub(daoeq.idClub(eq));
                eq.setClub(club);
            }
            
        }
        return ptsGeneral;
    }


    public static int obtenerNumeroProximaJornada() throws DAOException {

         var dao = new JornadaQuinielaDAO();
         return dao.getProximaJornada();

    }
    
    public static int obtenerNumeroUltimaJornadaValidada() throws DAOException {

         var dao = new JornadaQuinielaDAO();
         return dao.getUltimaJornadaValidada();

    }

    public static JornadaQuiniela obtenerProximaJornada(CompeticionQuiniela comp)
            throws DAOException{

        var proxJor =  comp.getProximaJornada();
        var daojor = new JornadaQuinielaDAO();
        var jornada = daojor.getJornadaByNumero(comp, proxJor);
        if (jornada != null)
            jornada.setCompeticion(comp);

        return jornada;

    }
    
    public static int numeroJornadasDisputadas(CompeticionQuiniela comp)
            throws DAOException{


        var daojor = new JornadaQuinielaDAO();
        var num = daojor.getNumeroJornadasValidadas(comp);

        return num;

    }
    
    public static List<ApuestaQuiniela> obtenerApuestas(EquipoQuiniela eq)
            throws DAOException{
        
        var dao = new ApuestaQuinielaDAO();

        return dao.getApuestasByEquipo(eq);
    }

    
    public static List<ApuestaQuiniela> obtenerApuestas(EquipoQuiniela eq,
            JornadaQuiniela jornada) throws DAOException{
        
        var dao = new ApuestaQuinielaDAO();

        ArrayList<ApuestaQuiniela> lista =
                (ArrayList) dao.getApuestasByJornada(eq, jornada);

        for (var apuesta : lista) {
            apuesta.setEquipo(eq);
            apuesta.setJornada(jornada);
        }
        
        return lista;
    }

    public static List<ApuestaQuiniela> obtenerApuestas(JornadaQuiniela jornada)
            throws DAOException{

        var dao = new ApuestaQuinielaDAO();

        ArrayList<ApuestaQuiniela> lista =
                (ArrayList) dao.getApuestasByJornada(jornada);

        for (var apuesta : lista) {
            apuesta.setJornada(jornada);
        }

        return lista;
    }

   
    public static List<JornadaQuiniela> obtenerJornadasNoValidadas(
            CompeticionQuiniela comp) throws DAOException{

        var dao = new JornadaQuinielaDAO();
        var jornadas = dao.getJornadasNoValidadas(comp);
        for (var jor : jornadas) {
            jor.setCompeticion(comp);
        }

        return jornadas;

    }

    public static List<JornadaQuiniela> obtenerJornadasValidadas(
            CompeticionQuiniela comp) throws DAOException{

        var dao = new JornadaQuinielaDAO();
        var jornadas = dao.getJornadasValidadas(comp);
        for (var jor : jornadas) {
            jor.setCompeticion(comp);
        }

        return jornadas;

    }

    public static JornadaQuiniela obtenerJornada(
            long id) throws DAOException{

        var dao = new JornadaQuinielaDAO();
        var jornada = (JornadaQuiniela) dao.getObjetoById(id);

        return jornada;

    }
    
    public static JornadaQuiniela obtenerJornadaPorNumero(
            CompeticionQuiniela comp, int jornada) throws DAOException{

        var dao = new JornadaQuinielaDAO();
        return dao.getJornadaByNumero(comp, jornada);

    }
     public static void eliminarEquipoQuiniela(EquipoQuiniela eq) throws DAOException {
        
        var daoPun = new PuntuacionQuinielaDAO();
        var daoap = new ApuestaQuinielaDAO();
        var daoEq = new EquipoQuinielaDAO();
        var apuestas = 
                    (ArrayList<ApuestaQuiniela>) obtenerApuestas(eq);
        for (var ap : apuestas) {
            daoap.delete(ap);
        }
        var punts = daoPun.getPuntuacionesByEquipo(eq);
        for (var puntuacionQuiniela : punts) {
            daoPun.delete(puntuacionQuiniela);
        }
                
        daoEq.delete(eq);
        
    }

     public static void grabarCompeticion(CompeticionQuiniela obj) throws DAOException {
        var dao = new CompeticionQuinielaDAO();
        dao.save(obj);
    }

    public static void grabarEquipo(EquipoQuiniela eq) throws DAOException {
        var dao = new EquipoQuinielaDAO();
        dao.save(eq);
    }

    public static void grabarApuesta(ApuestaQuiniela ap) throws DAOException {
        var dao = new ApuestaQuinielaDAO();
        dao.save(ap);
    }
    
    public static void grabarJornada(JornadaQuiniela obj) throws DAOException {
        var dao = new JornadaQuinielaDAO();
        dao.save(obj);
    }
}
