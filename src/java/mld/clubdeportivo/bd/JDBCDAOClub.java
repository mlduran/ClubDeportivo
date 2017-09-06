
package mld.clubdeportivo.bd;

import java.util.ArrayList;
import mld.clubdeportivo.base.Club;
import mld.clubdeportivo.base.Deporte;
import mld.clubdeportivo.base.Grupo;
import mld.clubdeportivo.base.futbol8.EquipoFutbol8;
import mld.clubdeportivo.base.quinielas.EquipoQuiniela;
import mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8;
import mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela;


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

        ClubDAO daoclub = new ClubDAO();
        GrupoDAO daogrp = new GrupoDAO();

        Club club = daoclub.getClubByUsuario(usuario, password);

        if (club != null){
            long idgrp = daoclub.idGrupo(club);
            Grupo grp = daogrp.getGrupoById(idgrp);
            club.setGrupo(grp);
            if (grp.getClubs() == null) grp.setClubs(new ArrayList<Club>());
            grp.getClubs().add(club);
        }

        return club;

    }

  
    public static boolean existeClub(String nombre) throws DAOException{

        ClubDAO dao = new ClubDAO();

        Club club = dao.getClubByNombre(nombre);

        return club != null;

    }

    public static boolean existeUsuario(String nombre) throws DAOException{

        ClubDAO dao = new ClubDAO();

        Club club = dao.getClubByUsuario(nombre);

        return club != null;

    }

 
    public static Club obtenerSimpleClub(long id) throws DAOException{
        // Devuelve solo club - grupo - depotes 

        ClubDAO daoclub = new ClubDAO();
        GrupoDAO daogrp = new GrupoDAO();

        Club club = (Club) daoclub.getClubById(id);
        if (club != null){
            long idgrp = daoclub.idGrupo(club);
            Grupo grp = daogrp.getGrupoById(idgrp);
            club.setGrupo(grp);
            grp.getClubs().add(club);
        }

        return club;
    }
    
    public static Club obtenerClubPorUsuario(String usuario) throws DAOException{
        // Devuelve solo club - grupo - depotes 

        ClubDAO daoclub = new ClubDAO();
        GrupoDAO daogrp = new GrupoDAO();

        Club club = (Club) daoclub.getClubByUsuario(usuario);
        if (club != null){
            long idgrp = daoclub.idGrupo(club);
            Grupo grp = daogrp.getGrupoById(idgrp);
            club.setGrupo(grp);
            grp.getClubs().add(club);
        }

        return club;
    }


    public static ArrayList<Club> listaClubs() throws DAOException{
        // Devuelve lista de clubs sin relaciones

        ClubDAO dao = new ClubDAO();

        ArrayList<Club> lista  = (ArrayList<Club>) dao.getClubs();

        return lista;

    }
    
    public static ArrayList<Club> listaClubsNoAuto() throws DAOException{
        // Devuelve lista de clubs sin relaciones

        ClubDAO dao = new ClubDAO();

        ArrayList<Club> lista  = (ArrayList<Club>) dao.getClubsNoAuto();

        return lista;

    }

    public static ArrayList<Club> listaClubsGrupo(Grupo grp) throws DAOException{
        // Devuelve lista de clubs sin relaciones

        ClubDAO dao = new ClubDAO();

        ArrayList<Club> lista  = (ArrayList<Club>) dao.getClubsByGrupo(grp);

        return lista;

    }

    public static ArrayList<Club> listaClubsRanking(int maximo, boolean isTitulos) throws DAOException{
        // lista de clubs ordenados por ranking

        ClubDAO dao = new ClubDAO();

        ArrayList<Club> lista  = (ArrayList<Club>) dao.getClubsByRanking(maximo);
        
        if (isTitulos){
            for (Club club : lista) {
                 int ligasFutbol8 = JDBCDAOFutbol8.numeroCompeticionesGanadas(club, "Liga");
                 int copasFutbol8 = JDBCDAOFutbol8.numeroCompeticionesGanadas(club, "Copa");
                 int copasQuiniela = JDBCDAOQuiniela.numeroCompeticionesGanadas(club);
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
            EquipoQuiniela eq = JDBCDAOQuiniela.obtenerEquipo(club);
            JDBCDAOQuiniela.eliminarEquipoQuiniela(eq);
        }
        
        if (club.isFutbol8()){
            EquipoFutbol8 eq = JDBCDAOFutbol8.obtenerSimpleEquipoFutbol8(club);
            JDBCDAOFutbol8.eliminarEquipoFutbol8(eq);
        }
        
        ClubDAO dao = new ClubDAO();
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
        
        ArrayList<String> mails = new ArrayList<String>();
        ArrayList<Club> listaClubs;
        
        if (grp == null) listaClubs = listaClubs();
        else listaClubs = listaClubsGrupo(grp);
        for (Club club : listaClubs) {
            if ((deporte == null) ||                
                    (deporte.equals(Deporte.Futbol8) && club.isFutbol8()) ||
                    (deporte.equals(Deporte.Quiniela) && club.isQuiniela())
                    )
                if (club.getMail() != null && !club.getMail().isEmpty())
                    mails.add(club.getMail());            
        }
               
        return mails;
    }
    
    public static void eliminarClubsGrupo(Grupo grp) throws DAOException{
        
        ClubDAO dao = new ClubDAO();
        ArrayList<Club> clubs = (ArrayList<Club>) dao.getClubsByGrupo(grp);
        
        for (Club club : clubs) {
            eliminarClub(club);
        }
    }
          
    public static void eliminarClubs() throws DAOException{
        
        GrupoDAO dao = new GrupoDAO();
        ArrayList<Grupo> grps = (ArrayList<Grupo>) dao.getGrupos(true);
        grps.addAll(dao.getGrupos(false));
    }
    
  
    public static void grabarClub(Club club) throws DAOException {
        ClubDAO dao = new ClubDAO();
        dao.save(club);
    }

 

}
