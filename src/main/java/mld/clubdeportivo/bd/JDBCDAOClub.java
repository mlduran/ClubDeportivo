
package mld.clubdeportivo.bd;

import java.util.ArrayList;
import mld.clubdeportivo.base.Club;
import mld.clubdeportivo.base.Deporte;
import static mld.clubdeportivo.base.Deporte.Futbol8;
import static mld.clubdeportivo.base.Deporte.Quiniela;
import mld.clubdeportivo.base.Grupo;
import mld.clubdeportivo.base.futbol8.EquipoFutbol8;
import mld.clubdeportivo.base.quinielas.EquipoQuiniela;
import mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.eliminarEquipoFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.numeroCompeticionesGanadas;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerSimpleEquipoFutbol8;
import mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.eliminarEquipoQuiniela;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.numeroCompeticionesGanadas;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerEquipo;


/**
 *
 * @author mlopezd
 */
public class JDBCDAOClub {

    private JDBCDAOClub(){}

    public static Club validarLogin(String usuario, String password)
            throws DAOException{

        if (usuario == null || password == null)
            return null;

        var daoclub = new ClubDAO();
        var daogrp = new GrupoDAO();
        var club = daoclub.getClubByUsuario(usuario, password);

        if (club != null){
            var idgrp = daoclub.idGrupo(club);
            var grp = daogrp.getGrupoById(idgrp);
            club.setGrupo(grp);
            if (grp.getClubs() == null) grp.setClubs(new ArrayList<>());
            grp.getClubs().add(club);
        }

        return club;

    }

  
    public static boolean existeClub(String nombre) throws DAOException{

        var dao = new ClubDAO();
        var club = dao.getClubByNombre(nombre);

        return club != null;

    }

    public static boolean existeUsuario(String nombre) throws DAOException{

        var dao = new ClubDAO();
        var club = dao.getClubByUsuario(nombre);

        return club != null;

    }

 
    public static Club obtenerSimpleClub(long id) throws DAOException{
        // Devuelve solo club - grupo - depotes 

        var daoclub = new ClubDAO();
        var daogrp = new GrupoDAO();
        var club = (Club) daoclub.getClubById(id);
        if (club != null){
            var idgrp = daoclub.idGrupo(club);
            var grp = daogrp.getGrupoById(idgrp);
            club.setGrupo(grp);
            grp.getClubs().add(club);
        }

        return club;
    }
    
    public static Club obtenerClubPorUsuario(String usuario) throws DAOException{
        // Devuelve solo club - grupo - depotes 

        var daoclub = new ClubDAO();
        var daogrp = new GrupoDAO();
        var club = (Club) daoclub.getClubByUsuario(usuario);
        if (club != null){
            var idgrp = daoclub.idGrupo(club);
            var grp = daogrp.getGrupoById(idgrp);
            club.setGrupo(grp);
            grp.getClubs().add(club);
        }

        return club;
    }


    public static ArrayList<Club> listaClubs() throws DAOException{
        // Devuelve lista de clubs sin relaciones

        var dao = new ClubDAO();
        var lista  = (ArrayList<Club>) dao.getClubs();

        return lista;

    }
    
    public static ArrayList<Club> listaClubsNoAuto() throws DAOException{
        // Devuelve lista de clubs sin relaciones

        var dao = new ClubDAO();
        var lista  = (ArrayList<Club>) dao.getClubsNoAuto();

        return lista;

    }

    public static ArrayList<Club> listaClubsGrupo(Grupo grp) throws DAOException{
        // Devuelve lista de clubs sin relaciones

        var dao = new ClubDAO();
        var lista  = (ArrayList<Club>) dao.getClubsByGrupo(grp);

        return lista;

    }

    public static ArrayList<Club> listaClubsRanking(int maximo, boolean isTitulos) throws DAOException{
        // lista de clubs ordenados por ranking

        var dao = new ClubDAO();
        var lista  = (ArrayList<Club>) dao.getClubsByRanking(maximo);
        
        if (isTitulos){
            for (var club : lista) {
                 var ligasFutbol8 = numeroCompeticionesGanadas(club, "Liga");
                 var copasFutbol8 = numeroCompeticionesGanadas(club, "Copa");
                 var copasQuiniela = numeroCompeticionesGanadas(club);
                 club.setTitulosLigaFutbol8(ligasFutbol8);
                 club.setTitulosCopaFutbol8(copasFutbol8);
                 club.setTitulosQuiniela(copasQuiniela);
            } 
        }        

        return lista;

    }

  
   
    public static void eliminarClub(Club club) throws DAOException{
        // Elimina el club y todas sus relaciones  
        
        
        if (club.isQuiniela()){            
            var eq = obtenerEquipo(club);
            eliminarEquipoQuiniela(eq);
        }
        
        if (club.isFutbol8()){
            var eq = obtenerSimpleEquipoFutbol8(club);
            eliminarEquipoFutbol8(eq);
        }
        
        var dao = new ClubDAO();
        dao.delete(club);        
        
    }
    
    public static ArrayList<String> mailsClubs(Deporte deporte) 
            throws DAOException{
        
        return mailsClubs(null, deporte);
        
    }
    
    public static ArrayList<String> mailsClubs(Grupo grp) 
            throws DAOException{
        
        return mailsClubs(grp, null);
        
    }
    
    public static ArrayList<String> mailsClubs() 
            throws DAOException{
        
        return mailsClubs(null, null);
        
    }
    
    public static ArrayList<String> mailsClubs(Grupo grp, Deporte deporte) 
            throws DAOException{
        
        var mails = new ArrayList<String>();
        ArrayList<Club> listaClubs;
        
        if (grp == null) listaClubs = listaClubs();
        else listaClubs = listaClubsGrupo(grp);
        for (var club : listaClubs) {
            if ((deporte == null) ||                
                    (deporte.equals(Futbol8) && club.isFutbol8()) ||
                    (deporte.equals(Quiniela) && club.isQuiniela())
                    )
                if (club.getMail() != null && !club.getMail().isEmpty())
                    mails.add(club.getMail());            
        }
               
        return mails;
    }
    
    public static void eliminarClubsGrupo(Grupo grp) throws DAOException{
        
        var dao = new ClubDAO();
        var clubs = (ArrayList<Club>) dao.getClubsByGrupo(grp);
        for (var club : clubs) {
            eliminarClub(club);
        }
    }
          
    public static void eliminarClubs() throws DAOException{
        
        var dao = new GrupoDAO();
        var grps = (ArrayList<Grupo>) dao.getGrupos(true);
        grps.addAll(dao.getGrupos(false));
    }
    
  
    public static void grabarClub(Club club) throws DAOException {
        var dao = new ClubDAO();
        dao.save(club);
    }

 

}
