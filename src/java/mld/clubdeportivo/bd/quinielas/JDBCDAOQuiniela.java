package mld.clubdeportivo.bd.quinielas;


import java.util.*;
import mld.clubdeportivo.base.*;
import mld.clubdeportivo.base.quinielas.*;
import mld.clubdeportivo.bd.ClubDAO;
import mld.clubdeportivo.bd.DAOException;
import mld.clubdeportivo.bd.JDBCDAOClub;
import mld.clubdeportivo.bd.JDBCDAOGrupo;
import org.apache.log4j.*;

/**
 *
 * @author mlopezd
 */
public class JDBCDAOQuiniela {

    private static Logger logApp
            = LogManager.getLogger(JDBCDAOQuiniela.class);

   
    private JDBCDAOQuiniela(){}
    
    
     public static int numeroCompeticionesGanadas(Club club) throws DAOException{
        
        CompeticionQuinielaDAO dao = new CompeticionQuinielaDAO();
        
        return dao.getCompeticionesGanadas(club); 
        
    }
    
    
     public static long idEquipoQuiniela(Club club) throws DAOException {

        EquipoQuinielaDAO dao = new EquipoQuinielaDAO();

        return dao.idEquipoQuiniela(club);

    }
     
     public static ArrayList<EquipoQuiniela> equiposQuinielaActivos() throws DAOException{
         
         EquipoQuinielaDAO daoeqs = new EquipoQuinielaDAO();
         return daoeqs.getEquiposQuinielaActivos();        
         
     }
    

   

    public static EstadisticaQuiniela obtenerEstadistica(
            EquipoQuiniela eq, CompeticionQuiniela comp, JornadaQuiniela jor) throws DAOException{

        EstadisticaQuinielaDAO daoest = new EstadisticaQuinielaDAO();
        EstadisticaQuiniela est = daoest.getEstadisticaByJornada(eq.getNombre(), 
                comp.getNombre(), jor.getDescripcion());
        return est;

    }
    
      
    public static ArrayList<ApuestaQuiniela> obtenerApuestasPorJornada(
            EquipoQuiniela eq, JornadaQuiniela jornada) throws DAOException{
        
        ApuestaQuinielaDAO daoap = new ApuestaQuinielaDAO();
        return  (ArrayList<ApuestaQuiniela>) daoap.getApuestasByJornada(eq, jornada);
        
        
    }
    
     public static PuntuacionQuiniela obtenerPuntosPorEquipo(
            CompeticionQuiniela comp, EquipoQuiniela eq) throws DAOException{
        
        PuntuacionQuinielaDAO daopt = new PuntuacionQuinielaDAO();
        return  daopt.getPuntosByEquipo(comp, eq);
        
        
    }

   
    
    public static CompeticionQuiniela obtenerCompeticionPorId(long id)
             throws DAOException {

         CompeticionQuinielaDAO dao = new CompeticionQuinielaDAO();

         return dao.getCompeticionById(id);

     }
   
    public static CompeticionQuiniela competicionActiva()
             throws DAOException {

         CompeticionQuinielaDAO dao = new CompeticionQuinielaDAO();

         return dao.getCompeticionActiva();

     }

     public static ArrayList<CompeticionQuiniela> competicionesNoActivas()
             throws DAOException {

         CompeticionQuinielaDAO dao = new CompeticionQuinielaDAO();

         return dao.getCompeticionesNoActivas();

     }
     
     public static ArrayList<EquipoQuiniela> obtenerEquiposActivos() throws DAOException{
         
         EquipoQuinielaDAO daoeq = new EquipoQuinielaDAO();
         
         return daoeq.getEquiposQuinielaActivos();
     }
     
     
     public static EquipoQuiniela obtenerEquipo(Club club) throws DAOException{
         
         EquipoQuinielaDAO daoeq = new EquipoQuinielaDAO();
         return daoeq.getEquipoQuinielaByClub(club);
         
         
     }

     public static EquipoQuiniela obtenerSimpleEquipoQuiniela(long id)
            throws DAOException{
        // Devuelve equipo con sus relaciones directas y grupo

         EquipoQuinielaDAO daoeq = new EquipoQuinielaDAO();
         ClubDAO daoclub = new ClubDAO();

         EquipoQuiniela eq = daoeq.getEquipoQuinielaById(id);
         long idclub = daoeq.idClub(eq);
         Club club = JDBCDAOClub.obtenerSimpleClub(idclub);
         long idgrp = daoclub.idGrupo(club);
         Grupo grp = JDBCDAOGrupo.obtenerSimpleGrupo(idgrp);
         eq.setClub(club);
         club.setGrupo(grp);
         grp.getClubs().add(club);

         return eq;

    }

    public static ArrayList<EquipoQuiniela> listaEquiposQuiniela(Grupo grp)
            throws DAOException {
        // Devuelve lista de equipos sin relaciones

        EquipoQuinielaDAO dao = new EquipoQuinielaDAO();
        ArrayList<EquipoQuiniela> lista =
                dao.getEquiposQuinielaActivos();
        
        ArrayList<EquipoQuiniela> listaQuini = new ArrayList<EquipoQuiniela>();

        for (EquipoQuiniela eq : lista) {
            long idclub = dao.idClub(eq);
            Club club = JDBCDAOClub.obtenerSimpleClub(idclub);
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

        EquipoQuinielaDAO daoeq = new EquipoQuinielaDAO();
        PuntuacionQuinielaDAO daopunt = new PuntuacionQuinielaDAO();
        ArrayList<PuntuacionQuiniela> ptsGeneral = new ArrayList<PuntuacionQuiniela>();
        if (comp != null){
            ArrayList<PuntuacionQuiniela> pts = daopunt.getPuntuacionesCompeticion(comp, isGeneral);
            
            for (PuntuacionQuiniela pt : pts) {
                EquipoQuiniela eq =  daoeq.getEquipoQuinielaById(daopunt.idEquipo(pt));
                if (eq == null) continue;
                ptsGeneral.add(pt);
                pt.setEquipo(eq);
                ArrayList<PuntuacionQuiniela> puntos = new ArrayList<PuntuacionQuiniela>();
                puntos.add(pt);
                eq.setPuntuaciones(puntos);
                Club club = JDBCDAOClub.obtenerSimpleClub(daoeq.idClub(eq));
                eq.setClub(club);
            }
            
        }
        return ptsGeneral;
    }


    public static int obtenerNumeroProximaJornada() throws DAOException {

         JornadaQuinielaDAO dao = new JornadaQuinielaDAO();
         return dao.getProximaJornada();

    }
    
    public static int obtenerNumeroUltimaJornadaValidada() throws DAOException {

         JornadaQuinielaDAO dao = new JornadaQuinielaDAO();
         return dao.getUltimaJornadaValidada();

    }

    public static JornadaQuiniela obtenerProximaJornada(CompeticionQuiniela comp)
            throws DAOException{

        int proxJor =  comp.getProximaJornada();

        JornadaQuinielaDAO daojor = new JornadaQuinielaDAO();
        JornadaQuiniela jornada = daojor.getJornadaByNumero(comp, proxJor);
        if (jornada != null)
            jornada.setCompeticion(comp);

        return jornada;

    }
    
    public static int numeroJornadasDisputadas(CompeticionQuiniela comp)
            throws DAOException{


        JornadaQuinielaDAO daojor = new JornadaQuinielaDAO();
        int num = daojor.getNumeroJornadasValidadas(comp);

        return num;

    }
    
    public static List<ApuestaQuiniela> obtenerApuestas(EquipoQuiniela eq)
            throws DAOException{
        
        ApuestaQuinielaDAO dao = new ApuestaQuinielaDAO();

        return dao.getApuestasByEquipo(eq);
    }

    
    public static List<ApuestaQuiniela> obtenerApuestas(EquipoQuiniela eq,
            JornadaQuiniela jornada) throws DAOException{
        
        ApuestaQuinielaDAO dao = new ApuestaQuinielaDAO();

        ArrayList<ApuestaQuiniela> lista =
                (ArrayList) dao.getApuestasByJornada(eq, jornada);

        for (ApuestaQuiniela apuesta : lista) {
            apuesta.setEquipo(eq);
            apuesta.setJornada(jornada);
        }
        
        return lista;
    }

    public static List<ApuestaQuiniela> obtenerApuestas(JornadaQuiniela jornada)
            throws DAOException{

        ApuestaQuinielaDAO dao = new ApuestaQuinielaDAO();

        ArrayList<ApuestaQuiniela> lista =
                (ArrayList) dao.getApuestasByJornada(jornada);

        for (ApuestaQuiniela apuesta : lista) {
            apuesta.setJornada(jornada);
        }

        return lista;
    }

   
    public static List<JornadaQuiniela> obtenerJornadasNoValidadas(
            CompeticionQuiniela comp) throws DAOException{

        JornadaQuinielaDAO dao = new JornadaQuinielaDAO();
        ArrayList<JornadaQuiniela> jornadas = dao.getJornadasNoValidadas(comp);

        for (JornadaQuiniela jor : jornadas) {
            jor.setCompeticion(comp);
        }

        return jornadas;

    }

    public static List<JornadaQuiniela> obtenerJornadasValidadas(
            CompeticionQuiniela comp) throws DAOException{

        JornadaQuinielaDAO dao = new JornadaQuinielaDAO();
        ArrayList<JornadaQuiniela> jornadas = dao.getJornadasValidadas(comp);

        for (JornadaQuiniela jor : jornadas) {
            jor.setCompeticion(comp);
        }

        return jornadas;

    }

    public static JornadaQuiniela obtenerJornada(
            long id) throws DAOException{

        JornadaQuinielaDAO dao = new JornadaQuinielaDAO();
        JornadaQuiniela jornada = (JornadaQuiniela) dao.getObjetoById(id);

        return jornada;

    }
    
    public static JornadaQuiniela obtenerJornadaPorNumero(
            CompeticionQuiniela comp, int jornada) throws DAOException{

        JornadaQuinielaDAO dao = new JornadaQuinielaDAO();
        return dao.getJornadaByNumero(comp, jornada);

    }
     public static void eliminarEquipoQuiniela(EquipoQuiniela eq) throws DAOException {
        
        PuntuacionQuinielaDAO daoPun = new PuntuacionQuinielaDAO();
        ApuestaQuinielaDAO daoap = new ApuestaQuinielaDAO();
        EquipoQuinielaDAO daoEq = new EquipoQuinielaDAO();
        
        ArrayList<ApuestaQuiniela> apuestas = 
                    (ArrayList<ApuestaQuiniela>) obtenerApuestas(eq);
        for (ApuestaQuiniela ap : apuestas) {
            daoap.delete(ap);
        }
            
        ArrayList<PuntuacionQuiniela> punts = daoPun.getPuntuacionesByEquipo(eq);
        for (PuntuacionQuiniela puntuacionQuiniela : punts) {
            daoPun.delete(puntuacionQuiniela);
        }
                
        daoEq.delete(eq);
        
    }

     public static void grabarCompeticion(CompeticionQuiniela obj) throws DAOException {
        CompeticionQuinielaDAO dao = new CompeticionQuinielaDAO();
        dao.save(obj);
    }

    public static void grabarEquipo(EquipoQuiniela eq) throws DAOException {
        EquipoQuinielaDAO dao = new EquipoQuinielaDAO();
        dao.save(eq);
    }

    public static void grabarApuesta(ApuestaQuiniela ap) throws DAOException {
        ApuestaQuinielaDAO dao = new ApuestaQuinielaDAO();
        dao.save(ap);
    }
    
    public static void grabarJornada(JornadaQuiniela obj) throws DAOException {
        JornadaQuinielaDAO dao = new JornadaQuinielaDAO();
        dao.save(obj);
    }
}
