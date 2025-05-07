
package mld.clubdeportivo.bd;

import mld.clubdeportivo.bd.futbol8.JugadorFutbol8DAO;
import mld.clubdeportivo.bd.futbol8.EquipoFutbol8DAO;
import java.util.*;
import mld.clubdeportivo.base.*;
import mld.clubdeportivo.base.futbol8.*;
import static mld.clubdeportivo.bd.JDBCDAOClub.eliminarClubsGrupo;
import static mld.clubdeportivo.bd.JDBCDAOComentario.eliminarComentarios;
import static mld.clubdeportivo.bd.JDBCDAONoticia.eliminarNoticias;
import mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.eliminarCompeticionesFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.eliminarEntrenadorFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.eliminarJugadorFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerEntrenadoresGrupo;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerJugadoresGrupo;


/**
 *
 * @author mlopezd
 */
public class JDBCDAOGrupo {

    private JDBCDAOGrupo(){}

    public static boolean existeGrupo(String nombre) throws DAOException{

        var dao = new GrupoDAO();
        var grp = dao.getGrupoByNombre(nombre);

        return grp != null;

    }
    
    public static boolean isIPRegistrada(String ip)throws DAOException{

        var dao = new GrupoDAO();
        var grp = dao.getGrupoByIp(ip);

        return grp != null;

    }

    
    public static Grupo obtenerSimpleGrupo(long id) throws DAOException{
        // Devuelve grupo sin relaciones

        var daogrp = new GrupoDAO();
        var grp = daogrp.getGrupoById(id);

        return grp;

    }

    public static ArrayList<Grupo> obtenerGruposDisponibles() throws DAOException{
        // Devuelve lista de grupos disponibles sin relaciones
        var dao = new GrupoDAO();

        return (ArrayList<Grupo>) dao.getGruposDisponibles();

    }

    public static ArrayList<Grupo> obtenerGruposActivos() throws DAOException{
        // Devuelve lista de grupos activos sin relaciones
        var dao = new GrupoDAO();

        return (ArrayList<Grupo>) dao.getGrupos(true);

    }


    protected static void obtenerExtructuraGrupoSimple(Grupo grp) throws DAOException{
        // Devuelve extructura de grupo - clubs

        var daoclub = new ClubDAO();

        grp.setClubs(new ArrayList<>());
        for (var club : daoclub.getClubsByGrupo(grp)) {

            club.setGrupo(grp);
            grp.getClubs().add(club);
            club.setGrupo(grp);            

        }

    }

    public static void eliminarGrupo(Grupo grp) throws DAOException{
        
        var dao = new GrupoDAO();
        eliminarCompeticionesFutbol8(grp);
        eliminarClubsGrupo(grp); 
        
        var jugadores = obtenerJugadoresGrupo(grp);
        for (var jugadorFutbol8 : jugadores) {
            eliminarJugadorFutbol8(jugadorFutbol8);
        }
        var entrenadores = obtenerEntrenadoresGrupo(grp);
        for (var entrenadorFutbol8 : entrenadores) {
            eliminarEntrenadorFutbol8(entrenadorFutbol8);
        }
        
        eliminarComentarios(grp);
        eliminarNoticias(grp);
        dao.delete(grp);
        
    }
    

    public static void grabarGrupo(Grupo grp) throws DAOException {
        var dao = new GrupoDAO();
        dao.save(grp);
    }

   


}
