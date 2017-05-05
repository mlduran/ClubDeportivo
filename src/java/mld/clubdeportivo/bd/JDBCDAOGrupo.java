
package mld.clubdeportivo.bd;

import mld.clubdeportivo.bd.futbol8.JugadorFutbol8DAO;
import mld.clubdeportivo.bd.futbol8.EquipoFutbol8DAO;
import java.util.*;
import mld.clubdeportivo.base.*;
import mld.clubdeportivo.base.futbol8.*;
import mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8;


/**
 *
 * @author mlopezd
 */
public class JDBCDAOGrupo {

    private JDBCDAOGrupo(){}

    public static boolean existeGrupo(String nombre) throws DAOException{

        GrupoDAO dao = new GrupoDAO();

        Grupo grp = dao.getGrupoByNombre(nombre);

        return grp != null;

    }
    
    public static boolean isIPRegistrada(String ip)throws DAOException{

        GrupoDAO dao = new GrupoDAO();

        Grupo grp = dao.getGrupoByIp(ip);

        return grp != null;

    }

    
    public static Grupo obtenerSimpleGrupo(long id) throws DAOException{
        // Devuelve grupo sin relaciones

        GrupoDAO daogrp = new GrupoDAO();

        Grupo grp = daogrp.getGrupoById(id);

        return grp;

    }

    public static ArrayList<Grupo> obtenerGruposDisponibles() throws DAOException{
        // Devuelve lista de grupos disponibles sin relaciones
        GrupoDAO dao = new GrupoDAO();

        return (ArrayList<Grupo>) dao.getGruposDisponibles();

    }

    public static ArrayList<Grupo> obtenerGruposActivos() throws DAOException{
        // Devuelve lista de grupos activos sin relaciones
        GrupoDAO dao = new GrupoDAO();

        return (ArrayList<Grupo>) dao.getGrupos(true);

    }


    protected static void obtenerExtructuraGrupoSimple(Grupo grp) throws DAOException{
        // Devuelve extructura de grupo - clubs

        ClubDAO daoclub = new ClubDAO();

        grp.setClubs(new ArrayList<Club>());
        for (Club club : daoclub.getClubsByGrupo(grp)) {

            club.setGrupo(grp);
            grp.getClubs().add(club);
            club.setGrupo(grp);            

        }

    }

    public static void eliminarGrupo(Grupo grp) throws DAOException{
        
        GrupoDAO dao = new GrupoDAO();
        JDBCDAOFutbol8.eliminarCompeticionesFutbol8(grp);
        JDBCDAOClub.eliminarClubsGrupo(grp); 
        
        ArrayList<JugadorFutbol8> jugadores = JDBCDAOFutbol8.obtenerJugadoresGrupo(grp);
        for (JugadorFutbol8 jugadorFutbol8 : jugadores) {
            JDBCDAOFutbol8.eliminarJugadorFutbol8(jugadorFutbol8);
        }
        
        ArrayList<EntrenadorFutbol8> entrenadores = JDBCDAOFutbol8.obtenerEntrenadoresGrupo(grp);
        for (EntrenadorFutbol8 entrenadorFutbol8 : entrenadores) {
            JDBCDAOFutbol8.eliminarEntrenadorFutbol8(entrenadorFutbol8);
        }
        
        JDBCDAOComentario.eliminarComentarios(grp);
        JDBCDAONoticia.eliminarNoticias(grp);
        dao.delete(grp);
        
    }
    

    public static void grabarGrupo(Grupo grp) throws DAOException {
        GrupoDAO dao = new GrupoDAO();
        dao.save(grp);
    }

   


}
