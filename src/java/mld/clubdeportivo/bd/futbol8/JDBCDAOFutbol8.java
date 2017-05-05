
package mld.clubdeportivo.bd.futbol8;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import mld.clubdeportivo.base.Club;
import mld.clubdeportivo.base.EquipoMaestro;
import mld.clubdeportivo.base.Grupo;
import mld.clubdeportivo.base.futbol8.*;
import mld.clubdeportivo.bd.*;


/**
 *
 * @author mlopezd
 */
public class JDBCDAOFutbol8 {


    private JDBCDAOFutbol8(){}

    public static Grupo obtenerDatosGrupo(Grupo grp){


        return grp;

    }
    
    public static int numeroCompeticionesGanadas(Club club, String clase) throws DAOException{
        
        CompeticionFutbol8DAO dao = new CompeticionFutbol8DAO();
        
        return dao.getCompeticionesGanadas(club, clase); 
        
    }
    

    public static CompeticionFutbol8 competicionActiva(Grupo grp, String clase)
            throws DAOException{

        CompeticionFutbol8DAO dao = new CompeticionFutbol8DAO();

        CompeticionFutbol8 comp = dao.getCompeticionActiva(grp, clase);
        if (comp != null) comp.setGrupo(grp);

        return comp;

    }
    
     public static CompeticionFutbol8 competicion(long id)
            throws DAOException{

        CompeticionFutbol8DAO dao = new CompeticionFutbol8DAO();

        CompeticionFutbol8 comp = (CompeticionFutbol8) dao.getObjetoById(id);

        return comp;

    }
    
    public static long idEquipoFutbol8(Club club) throws DAOException{

        EquipoFutbol8DAO dao = new EquipoFutbol8DAO();

        return dao.idEquipoFutbol8(club);

    }
    
    public static VacacionFutbol8 obtenerDatosVacacionesFutbol8(EquipoFutbol8 eq) 
            throws DAOException{
        
        VacacionFutbol8DAO daovac = new VacacionFutbol8DAO();
        return daovac.getByEquipo(eq);
        
    }
    
    public static ArrayList<EquipoFutbol8> listaEquiposFutbol8(Grupo grp, boolean soloActivos)
            throws DAOException{
        // Devuelve lista de equipos con club  y grupo

        EquipoFutbol8DAO dao = new EquipoFutbol8DAO();
       
        ArrayList<EquipoFutbol8> lista  = dao.getEquiposFubtol8();
        
        for (EquipoFutbol8 eq : lista) {
            eq.setClub(JDBCDAOClub.obtenerSimpleClub(eq.getIdClub())); 
        }
        
        ArrayList<EquipoFutbol8> listaFinal = new ArrayList<EquipoFutbol8>();
        
        for (EquipoFutbol8 eq : lista) {
            if (eq.getClub() == null) continue;
            if (eq.getClub().getGrupo().equals(grp) &&
                    (!soloActivos || (soloActivos && eq.isActivo())))  {
                listaFinal.add(eq);
            }
            else 
                continue;
            eq.getClub().setGrupo(grp);            
        }

        return listaFinal;

    }


    public static ArrayList<EquipoFutbol8> listaEquiposFutbol8(Grupo grp)
            throws DAOException{
        // Devuelve lista de equipos con club  y grupo

       return listaEquiposFutbol8(grp, true);

    }
    
    public static EntrenadorFutbol8 obtenerEntrenador(EquipoFutbol8 eq) 
            throws DAOException{
        
        EntrenadorFutbol8DAO daoent = new EntrenadorFutbol8DAO();
        return daoent.getEntrenadorFutbol8(eq);
        
    }
    
    public static EntrenadorFutbol8 obtenerEntrenador(long id) 
            throws DAOException{
        
        EntrenadorFutbol8DAO daoent = new EntrenadorFutbol8DAO();
        return (EntrenadorFutbol8) daoent.getObjetoById(id);
        
    }
    
    public static JugadorFutbol8 obtenerJugador(long id, EquipoFutbol8 eq) 
            throws DAOException{
        
        JugadorFutbol8DAO daojug = new JugadorFutbol8DAO();
        
        JugadorFutbol8 jug = (JugadorFutbol8) daojug.getJugadorByEquipo(id, eq);
        return jug;
        
    }
    
     public static JuvenilFutbol8 obtenerJuvenil(EquipoFutbol8 eq) 
            throws DAOException{
        
        JuvenilFutbol8DAO daojug = new JuvenilFutbol8DAO();
        
        JuvenilFutbol8 jug = (JuvenilFutbol8) daojug.getJuvenilByEquipo(eq);
        if (jug != null){
            eq.setJuvenil(jug);
            jug.setEquipo(eq);
        }
        
        return jug;
        
    }
    
     public static ArrayList<GoleadorFutbol8> obtenerGoleadores(CompeticionFutbol8 comp)
            throws DAOException{
        // Devuelve lista de goleadores

         GoleadorFutbol8DAO dao = new GoleadorFutbol8DAO();
         
         return dao.getByCompeticion(comp);

    }
     
     public static ArrayList<PorteroFutbol8> obtenerPorteros(CompeticionFutbol8 comp)
            throws DAOException{
        // Devuelve lista de goleadores

         PorteroFutbol8DAO dao = new PorteroFutbol8DAO();
         
         return dao.getByCompeticion(comp);

    }
    
    public static boolean existeJugador(String nombre, Grupo grp) 
            throws DAOException{
        
        JugadorFutbol8DAO daojug = new JugadorFutbol8DAO();
        
        JugadorFutbol8 jug = (JugadorFutbol8) daojug.getJugadorByNombre(nombre, grp);
        return jug != null;
        
    }
    
    public static ArrayList<JugadorFutbol8> obtenerJugadores(EquipoFutbol8 eq) 
            throws DAOException{
        
        JugadorFutbol8DAO daojug = new JugadorFutbol8DAO();
        
        ArrayList<JugadorFutbol8> lista = 
                (ArrayList<JugadorFutbol8>) daojug.getJugadoresFutbol8(eq);
        
        eq.setJugadores(lista);
        
        for (JugadorFutbol8 jug : lista) {
            jug.setEquipo(eq);
            jug.setGrupo(eq.getClub().getGrupo());
        }        
        
        return lista;
        
    }
    
     public static ArrayList<JugadorFutbol8> obtenerJugadoresLibresGrupo(Grupo grp) 
            throws DAOException{
         
         JugadorFutbol8DAO daojug = new JugadorFutbol8DAO();
         return (ArrayList<JugadorFutbol8>) daojug.getJugadoresFutbol8Libres(grp);
     }
     
    
    
    public static ArrayList<JugadorFutbol8> obtenerJugadoresGrupo(Grupo grp) 
            throws DAOException{
        
        JugadorFutbol8DAO daojug = new JugadorFutbol8DAO();
        ArrayList<EquipoFutbol8> eqs = listaEquiposFutbol8(grp);        
        
        ArrayList<JugadorFutbol8> lista = new ArrayList<JugadorFutbol8>();                
        
        for (EquipoFutbol8 eq : eqs) {
            lista.addAll(obtenerJugadores(eq));
        }
 
        lista.addAll(obtenerJugadoresLibresGrupo(grp));
        
        ArrayList subastados = daojug.getJugadorEquipoSubasta(grp);
        for (Object  obj : subastados) {
            HashMap<String,Long> datos = (HashMap<String,Long>) obj;
            for (JugadorFutbol8 jug : lista) 
                if (jug.getId() == datos.get("id"))
                    for (EquipoFutbol8 eq : eqs) 
                        if (eq.getId() == datos.get("equipopuja"))
                            jug.setEquipoPuja(eq.getId());                
        }
       
        return lista;
        
    }
    
    public static ArrayList<EntrenadorFutbol8> obtenerEntrenadoresGrupo(Grupo grp) 
            throws DAOException{
        
        EntrenadorFutbol8DAO dao = new EntrenadorFutbol8DAO();
        ArrayList<EntrenadorFutbol8> lista =
                (ArrayList) dao.getEntrenadoresFutbol8(grp);
        
        return lista;
        
    }
    
    public static ArrayList<EntrenadorFutbol8> obtenerEntrenadoresLibresGrupo(Grupo grp) 
            throws DAOException{
        
        EntrenadorFutbol8DAO dao = new EntrenadorFutbol8DAO();
        ArrayList<EntrenadorFutbol8> lista =
                (ArrayList) dao.getEntrenadoresLibresFutbol8(grp);
        
        return lista;
        
    }
    
    public static int obtenerNumeroEntrenadoresGrupo(Grupo grp) 
            throws DAOException{
        
        EntrenadorFutbol8DAO dao = new EntrenadorFutbol8DAO();
        return dao.getNumEntrenadoresFutbol8(grp);
        
    }
           
   
   

    public static EquipoFutbol8 obtenerSimpleEquipoFutbol8(long id)
            throws DAOException{
        // Devuelve equipo futbol8 con sus relaciones directas y grupo

        EquipoFutbol8DAO daoeq = new EquipoFutbol8DAO();
        ClubDAO daoclub = new ClubDAO();
        JugadorFutbol8DAO daojf8 = new JugadorFutbol8DAO();
        EntrenadorFutbol8DAO daoentr = new EntrenadorFutbol8DAO();

        EquipoFutbol8 eq = daoeq.getEquipoFubtol8ById(id);
        
        // Si ya no existe el equipo porque lo hemos eliminado 
        // devolvemos uno sin nada
        if (eq == null){
            eq = new EquipoFutbol8();
            eq.setId(id);
            return eq;
        }
        
        long idclub = daoeq.idClub(eq);
        Club club = JDBCDAOClub.obtenerSimpleClub(idclub);
        long idgrp = daoclub.idGrupo(club);
        Grupo grp = JDBCDAOGrupo.obtenerSimpleGrupo(idgrp);
        eq.setClub(club);
        club.setGrupo(grp);
        grp.getClubs().add(club);

        ArrayList<JugadorFutbol8> jugsf8 =
                (ArrayList<JugadorFutbol8>) daojf8.getJugadoresFutbol8(eq);

        eq.setJugadores(jugsf8);

        for (JugadorFutbol8 jugf8 : jugsf8) {
            jugf8.setEquipo(eq);
            jugf8.setGrupo(grp);
        }

        EntrenadorFutbol8 entr = daoentr.getEntrenadorFutbol8(eq);
        if (entr != null){
            entr.setEquipo(eq);
            eq.setEntrenador(entr);
        }

        return eq;

    }

    public static EquipoFutbol8 obtenerSimpleEquipoFutbol8(Club club)
            throws DAOException{
        // Devuelve equipo futbol8 con sus relaciones directas y grupo


        EquipoFutbol8DAO daoeq = new EquipoFutbol8DAO();
        EquipoFutbol8 eq = daoeq.getEquipoFubtol8ByClub(club);

        return obtenerSimpleEquipoFutbol8(eq.getId());

    }
    
    public static CompeticionFutbol8 obtenerCompeticion(
            Long id) throws DAOException{
        
        CompeticionFutbol8DAO daocomp = new CompeticionFutbol8DAO();
        return daocomp.getCompeticionById(id);    
        
    }
    

    public static CompeticionFutbol8 obtenerDatosCompeticion(
            CompeticionFutbol8 comp) throws DAOException{

        JornadaFutbol8DAO daojor = new JornadaFutbol8DAO();
        PartidoFutbol8DAO daopar = new PartidoFutbol8DAO();

        ArrayList<EquipoFutbol8> eqs =
                listaEquiposFutbol8(comp.getGrupo());
        HashMap mapeoEqs = new HashMap();
        for (EquipoFutbol8 eq : eqs) {
            mapeoEqs.put(eq.getId(), eq);
        }

        ArrayList<JornadaFutbol8> jornadas = daojor.getJornadas(comp);
        comp.setJornadas(jornadas);

        for (JornadaFutbol8 jornada : jornadas) {
            jornada = daopar.obtenerPartidos(jornada, mapeoEqs);
        }

        return comp;
    }
    
     public static CompeticionFutbol8 obtenerDatosHistoCompeticion(
            CompeticionFutbol8 comp) throws DAOException{

        JornadaFutbol8DAO daojor = new JornadaFutbol8DAO();
        PartidoFutbol8DAO daopar = new PartidoFutbol8DAO();
        EstadisticaPartidoFutbol8DAO daoest = new EstadisticaPartidoFutbol8DAO();
        
        ArrayList<JornadaFutbol8> jornadas = daojor.getJornadas(comp);
        comp.setJornadas(jornadas);

        for (JornadaFutbol8 jornada : jornadas) {
            ArrayList<PartidoFutbol8> partidos = daopar.getPartidos(jornada);
            jornada.setPartidos(partidos);
            for (PartidoFutbol8 partido : partidos) {
                partido.setJornada(jornada);
                EstadisticaPartidoFutbol8 est = daoest.getEstadisticaByPartido(partido);
                if (est != null){
                    est.setPartido(partido);
                    partido.setEstadistica(est);
                }
            }
        }

        return comp;
    }
     
    public static ArrayList<CompeticionFutbol8> listaCompetionesFinalizadas(
            Grupo grp) throws DAOException{

        CompeticionFutbol8DAO daocomp = new CompeticionFutbol8DAO();
        
        ArrayList<CompeticionFutbol8> comps = daocomp.getCompeticionesNoActivas(grp);
        
        for (CompeticionFutbol8 comp : comps) {
            comp.setGrupo(grp);
        }
       
        return comps;

    }
    
    public static ArrayList<CompeticionFutbol8> listaCompetionesGrupo(
            Grupo grp) throws DAOException{
        
        CompeticionFutbol8DAO daocomp = new CompeticionFutbol8DAO();

        ArrayList<CompeticionFutbol8> comps = daocomp.getCompeticiones(grp);
        
        for (CompeticionFutbol8 comp : comps) {
            comp.setGrupo(grp);
        }
       
        return comps;

    }
    
    public static ArrayList<CompeticionFutbol8> listaCompetiones(
            ) throws DAOException{
        
        CompeticionFutbol8DAO daocomp = new CompeticionFutbol8DAO();

        ArrayList<CompeticionFutbol8> comps = daocomp.getCompeticiones(); 
       
        return comps;

    }
    
    
    public static ArrayList<EquipoFutbol8> obtenerClasificacion(
             CompeticionFutbol8 comp) throws DAOException{

        PuntuacionFutbol8DAO daopunt = new PuntuacionFutbol8DAO();
        ArrayList<EquipoFutbol8> eqs = new ArrayList<EquipoFutbol8>();
        
        ArrayList<PuntuacionFutbol8> puntos = 
                daopunt.getPuntuacionesCompeticion(comp);
        
        for (PuntuacionFutbol8 punt : puntos) {
            EquipoFutbol8 eq = obtenerSimpleEquipoFutbol8(daopunt.idEquipo(punt));
            if (eq == null)
            {
                Club club = new Club();
                club.setNombre(punt.getClub());
                eq = new EquipoFutbol8();
                eq.setClub(club);
            }  
            eqs.add(eq);
            eq.setPuntuacion(punt);
            punt.setEquipo(eq);
            
        }

        EquipoFutbol8.clasificarEquipos(eqs);

        return eqs;
    }
   
    public static ArrayList<EquipoFutbol8> obtenerClasificacion(
            Grupo grp) throws DAOException{
       
        CompeticionFutbol8 comp = competicionActiva(grp, "Liga");       

        return obtenerClasificacion(comp);
    }
    
    public static JornadaFutbol8 obtenerJornadaSimple(
            int numero, CompeticionFutbol8 comp) throws DAOException{
        
        JornadaFutbol8DAO jordao= new JornadaFutbol8DAO();
        PartidoFutbol8DAO pardao= new PartidoFutbol8DAO();
        JornadaFutbol8 jor = (JornadaFutbol8) jordao.getJornadaByNumero(comp, numero);
        if (jor != null){
            ArrayList<PartidoFutbol8> partidos = pardao.getPartidos(jor);
            jor.setPartidos(partidos);
            for (PartidoFutbol8 partido : partidos)
                partido.setJornada(jor); 
        }
        
        return jor;        
        
    }
    
    public static JornadaFutbol8 obtenerJornada(
            int numero, CompeticionFutbol8 comp) throws DAOException{
        
        JornadaFutbol8DAO jordao= new JornadaFutbol8DAO();
        JornadaFutbol8 jor = (JornadaFutbol8) jordao.getJornadaByNumero(comp, numero);
        ArrayList<EquipoFutbol8> eqs = listaEquiposFutbol8(comp.getGrupo());
        
        return obtenerDatosJornada(jor, comp, eqs);
    }
    
    
    public static JornadaFutbol8 obtenerJornada(
            long id, Grupo grp) throws DAOException{
        
        JornadaFutbol8DAO jordao= new JornadaFutbol8DAO();
        CompeticionFutbol8DAO compdao= new CompeticionFutbol8DAO();
        JornadaFutbol8 jor = (JornadaFutbol8) jordao.getObjetoById(id);
        long idComp = jordao.getIdCompeticion(jor);
        CompeticionFutbol8 comp = compdao.getCompeticionById(idComp);
        ArrayList<EquipoFutbol8> eqs = listaEquiposFutbol8(grp);
        
        return obtenerDatosJornada(jor, comp, eqs);
    }
    
    public static JornadaFutbol8 obtenerJornadaParaDisputar(
            CompeticionFutbol8 comp, ArrayList<EquipoFutbol8> eqs) throws DAOException{
        
        JornadaFutbol8DAO jordao= new JornadaFutbol8DAO();
        JornadaFutbol8 jor = jordao.getProximaJornada(comp);
        
        return obtenerDatosJornada(jor, comp, eqs);
        
    }
    
    public static PartidoFutbol8 obtenerProximoPartido(CompeticionFutbol8 comp,
            EquipoFutbol8 eq) throws DAOException{
        
        PartidoFutbol8DAO daopart = new PartidoFutbol8DAO();
        JornadaFutbol8DAO daojor = new JornadaFutbol8DAO(); 
        PartidoFutbol8 partido = null;
        
        if (comp != null){
            JornadaFutbol8 jornada = daojor.getProximaJornada(comp);
            partido = daopart.getPartido(jornada, eq);
            if (partido != null)
                partido.setJornada(jornada);
            jornada.setCompeticion(comp);
        }
        
        return partido;
        
    }
    
     public static PartidoFutbol8 obtenerDatosPrepararPartido(CompeticionFutbol8 comp,
            EquipoFutbol8 eq, EquipoFutbol8 eqSimul) throws DAOException{

         PartidoFutbol8DAO daopart = new PartidoFutbol8DAO();
         
         PartidoFutbol8 partido = obtenerProximoPartido(comp, eq);
         
         if (partido != null){
             EquipoFutbol8 EqLocal, EqVisitante;
             long idEqLocal = daopart.getIdEqLocal(partido);            
             if (idEqLocal == eq.getId()){
                 EqLocal = eq;
                 if (eqSimul == null){
                     long idEqVisitante = daopart.getIdEqVisitante(partido);
                     eqSimul = obtenerSimpleEquipoFutbol8(idEqVisitante);
                 }
                EqVisitante = eqSimul;
             }else{
                 EqVisitante = eq;
                 if (eqSimul == null)
                     eqSimul = obtenerSimpleEquipoFutbol8(idEqLocal);                
                 EqLocal = eqSimul;
             }
             partido.setEqLocal(EqLocal);
             partido.setEqVisitante(EqVisitante);
             obtenerDatosPartido(partido);
         }
         
         return partido;
         
     }
     
     public static AlineacionFutbol8 obtenerAlineacionPartido(PartidoFutbol8 partido,
            EquipoFutbol8 eq) throws DAOException{
         
         AlineacionFutbol8 alin;
         AlineacionFutbol8DAO alidao = new AlineacionFutbol8DAO();         
         
         alin = alidao.getByEquipo(eq, partido);
         alin.setPartido(partido);
         alin.setEquipo(eq);
         alidao.asignarJugadores(alin);
         alin.setEntrenador(eq.getEntrenador());                 
         eq.setAlineacion(alin);                
         
         return alin;         
         
     }
    
     public static PartidoFutbol8 obtenerDatosPartido(PartidoFutbol8 partido
            ) throws DAOException{
         
         EstadisticaPartidoFutbol8DAO daoest = new EstadisticaPartidoFutbol8DAO(); 
         
         PuntuacionFutbol8DAO puntdao = new PuntuacionFutbol8DAO();
         EquipoFutbol8DAO eqdao = new EquipoFutbol8DAO();       
         EntrenadorFutbol8DAO entdao = new EntrenadorFutbol8DAO();
         
         EquipoFutbol8 eqf8Local, eqf8Visit;
         
        
         EstadisticaPartidoFutbol8 est = daoest.getEstadisticaByPartido(partido);
         if (est == null)// es simulacion le creamos una
             est = new EstadisticaPartidoFutbol8();
         est.setPartido(partido);
         partido.setEstadistica(est);

         ArrayList<AlineacionFutbol8> alins = new ArrayList<AlineacionFutbol8>();
         CompeticionFutbol8 comp = 
                 (CompeticionFutbol8) partido.getJornada().getCompeticion();
         
         eqf8Local = (EquipoFutbol8) partido.getEqLocal(); 
         eqf8Visit = (EquipoFutbol8) partido.getEqVisitante();         
         
         EquipoFutbol8[] eqs = {eqf8Local,eqf8Visit};
         for (EquipoFutbol8 eq : eqs){
             ArrayList<JugadorFutbol8> jugs = 
                     (ArrayList<JugadorFutbol8>) eqdao.getJugadoresFutbol8(eq);
             eq.setJugadores(jugs);
             for (JugadorFutbol8 jug : jugs) {
                 jug.setEquipo(eq);
                 jug.setGrupo(eq.getClub().getGrupo());
             }
             
             if (eq.getAlineacion() == null)
                 alins.add(obtenerAlineacionPartido(partido, eq));
             else
                 alins.add(eq.getAlineacion());
             
             if (comp.getClase().equals("Liga")){
                 PuntuacionFutbol8 punt = puntdao.getPuntosByEquipo(comp, eq);
                 eq.setPuntuacion(punt);
                 punt.setEquipo(eq);
                 punt.setCompeticion(comp);
             }
             
             EntrenadorFutbol8 entr = entdao.getEntrenadorFutbol8(eq);
             eq.setEntrenador(entr);
             entr.setEquipo(eq);
             entr.setGrupo(eq.getClub().getGrupo());
         }
         
         partido.setAlineaciones(alins);
         
         return partido;
         
     }
    
    

    public static JornadaFutbol8 obtenerDatosJornada(JornadaFutbol8 jor,
            CompeticionFutbol8 comp, ArrayList<EquipoFutbol8> eqs) throws DAOException{

        PartidoFutbol8DAO daopar = new PartidoFutbol8DAO();   
   
        jor.setCompeticion(comp);
        
        HashMap mapeoEqs = new HashMap();
        for (EquipoFutbol8 eq : eqs) {
            mapeoEqs.put(eq.getId(), eq);
        }

        daopar.obtenerPartidos(jor, mapeoEqs);
        
        for (PartidoFutbol8 partido : jor.getPartidos()) {
            
            obtenerDatosPartido(partido);
        }
  
        return jor;
    }
    
    public static EstadisticaPartidoFutbol8 obtenerEstadisticaPartido(
            PartidoFutbol8 partido) throws DAOException{
        
        EstadisticaPartidoFutbol8DAO dao = new EstadisticaPartidoFutbol8DAO();
        
        return dao.getEstadisticaByPartido(partido);
        
    }
    
    public static ArrayList<CronicaFutbol8> obtenerCronica(
            PartidoFutbol8 partido) throws DAOException{
        
        CronicaFutbol8DAO dao = new CronicaFutbol8DAO();
        
        return dao.getCronicaByPartido(partido);
        
    }
    public static ArrayList<CronicaFutbol8> obtenerCronica(
            long idPartido) throws DAOException{
        
        CronicaFutbol8DAO dao = new CronicaFutbol8DAO();
        
        return dao.getCronicaByPartido(idPartido);
        
    }

     
    
    public static ArrayList<JugadorMaestroFutbol8> obtenerJugadoresMaestros(
            PosicionJugFutbol8 posicion) throws DAOException{
        
        JugadorMaestroFutbol8DAO DAOJugMaestro = new JugadorMaestroFutbol8DAO();
        ArrayList<JugadorMaestroFutbol8> lista = 
                (ArrayList<JugadorMaestroFutbol8>) DAOJugMaestro.getJugadoresMaestros(posicion);
        
        return lista;
    }
    
    
    public static ArrayList<EquipoMaestro> obtenerEquiposMaestros() throws DAOException{
    
        EquipoMaestroFutbol8DAO DAOEqMaestro = new EquipoMaestroFutbol8DAO();
        ArrayList lista = (ArrayList) DAOEqMaestro.getEquiposMaestros();
        
        return lista;
    }
    
    public static ArrayList<EntrenadorMaestroFutbol8> obtenerEntrenadoresMaestros() 
            throws DAOException{
    
        EntrenadorMaestroFutbol8DAO DAOEntrMaestro = new EntrenadorMaestroFutbol8DAO();
        ArrayList lista = (ArrayList) DAOEntrMaestro.getEntrenadoresMaestros();
        
        return lista;        
        
    }
    

    public static PartidoFutbol8 obtenerUltimoPartido(EquipoFutbol8 eq) throws DAOException{
        
        PartidoFutbol8DAO dao = new PartidoFutbol8DAO();
        
        return dao.getUltimoPartido(eq);
        
    }
    
     public static Date obtenerFechaUltimaJornada() throws DAOException{
        
        JornadaFutbol8DAO dao = new JornadaFutbol8DAO();
        
        JornadaFutbol8 obj = dao.getUltimaJornadaDisputada();
        
        Date fecha = null;
        
        if (obj != null)
            fecha = obj.getFecha();
        
        return fecha;
        
    }
    
    
    
    public static PartidoFutbol8 obtenerPartido(long id) throws DAOException{
        
        PartidoFutbol8DAO dao = new PartidoFutbol8DAO();
        AlineacionFutbol8DAO daoali = new AlineacionFutbol8DAO();
        
        PartidoFutbol8 partido = (PartidoFutbol8) dao.getObjetoById(id);
        
        EquipoFutbol8 eqLocal = obtenerSimpleEquipoFutbol8(dao.getIdEqLocal(partido));
        EquipoFutbol8 eqVisitante = obtenerSimpleEquipoFutbol8(dao.getIdEqVisitante(partido));
        
        
        partido.setEqLocal(eqLocal);
        partido.setEqVisitante(eqVisitante);
        partido.setAlineaciones(daoali.getAlineaciones(partido));  
        partido.setCronica(obtenerCronica(partido));
                
        return partido;     
        
    }
    
    public static ConfigEconomiaFutbol8 obtenerConfig(Grupo grp) throws DAOException{
        
        ConfigEconomiaFutbol8DAO dao = new ConfigEconomiaFutbol8DAO();
        
        ConfigEconomiaFutbol8 conf = dao.getConfigByGrupo(grp);
        
        if (conf != null){
            conf.setGrupo(grp);
            if(conf.getIdEquipoGestor() != null && conf.getIdEquipoGestor() != 0){            
                EquipoFutbol8 eq = obtenerSimpleEquipoFutbol8(conf.getIdEquipoGestor());
                conf.setEquipoGestor(eq);
            }
        }
        
        return conf;
        
    }
    
    public static long idJornada(PartidoFutbol8 partido) throws DAOException{
        
        PartidoFutbol8DAO dao = new PartidoFutbol8DAO();
        return dao.getIdJornada(partido);
    }
   
   
    public static void grabarEquipoFutbol8(EquipoFutbol8 obj) throws DAOException{
        EquipoFutbol8DAO dao = new EquipoFutbol8DAO();
        dao.save(obj);
    }

    public static void grabarJugadorFutbol8(JugadorFutbol8 obj) throws DAOException{
        JugadorFutbol8DAO dao = new JugadorFutbol8DAO();
        dao.save(obj);
    }
    
    public static void grabarGoleadorFutbol8(GoleadorFutbol8 obj) throws DAOException{
        GoleadorFutbol8DAO dao = new GoleadorFutbol8DAO();
        dao.save(obj);
     }
    
    public static void grabarPorteroFutbol8(PorteroFutbol8 obj) throws DAOException{
        PorteroFutbol8DAO dao = new PorteroFutbol8DAO();
        dao.save(obj);
     }
    
    public static void grabarJuvenilFutbol8(JuvenilFutbol8 obj) throws DAOException{
        JuvenilFutbol8DAO dao = new JuvenilFutbol8DAO();
        dao.save(obj);
    }
    
    public static void grabarEntrenadorFutbol8(EntrenadorFutbol8 obj) throws DAOException{
        EntrenadorFutbol8DAO dao = new EntrenadorFutbol8DAO();
        dao.save(obj);
    }

    public static void grabarJugadorMaestroFutbol8(JugadorMaestroFutbol8 obj)
            throws DAOException {
        JugadorMaestroFutbol8DAO dao = new JugadorMaestroFutbol8DAO();
        dao.save(obj);
    }

    public static void grabarEntrenadorMaestroFutbol8(EntrenadorMaestroFutbol8 obj)
            throws DAOException {
        EntrenadorMaestroFutbol8DAO dao = new EntrenadorMaestroFutbol8DAO();
        dao.save(obj);
    }
    
    public static void grabarVacacionFutbol8(VacacionFutbol8 obj)
            throws DAOException {
        VacacionFutbol8DAO dao = new VacacionFutbol8DAO();
        dao.save(obj);
    }

    public static void grabarEquipoMaestroFutbol8(EquipoMaestro obj)
            throws DAOException {
        EquipoMaestroFutbol8DAO dao = new EquipoMaestroFutbol8DAO();
        dao.save(obj);
    }
   
    public static void grabarCompeticionFutbol8(CompeticionFutbol8 obj)
            throws DAOException {
        CompeticionFutbol8DAO dao = new CompeticionFutbol8DAO();
        dao.save(obj);
    }

    public static void grabarJornadaFutbol8(JornadaFutbol8 obj)
            throws DAOException {
        JornadaFutbol8DAO dao = new JornadaFutbol8DAO();
        dao.save(obj);
    }

    public static void grabarPartidoFutbol8(PartidoFutbol8 obj)
            throws DAOException {
        PartidoFutbol8DAO dao = new PartidoFutbol8DAO();
        dao.save(obj);
    }

    public static void grabarPuntuacionFutbol8(PuntuacionFutbol8 obj)
            throws DAOException {
        PuntuacionFutbol8DAO dao = new PuntuacionFutbol8DAO();
        dao.save(obj);
    }

    public static void grabarAlineacionFutbol8(AlineacionFutbol8 obj)
            throws DAOException {
        obj.validarAlineacion();
        AlineacionFutbol8DAO dao = new AlineacionFutbol8DAO();
        dao.save(obj);
    }
    
    public static void grabarEstadisticaPartidoFutbol8(EstadisticaPartidoFutbol8 obj)
            throws DAOException {
        EstadisticaPartidoFutbol8DAO dao = new EstadisticaPartidoFutbol8DAO();
        dao.save(obj);
    }
    
    public static void grabarCronicaFutbol8(CronicaFutbol8 obj)
            throws DAOException {
        CronicaFutbol8DAO dao = new CronicaFutbol8DAO();
        dao.save(obj);
    }
    
    public static void grabarConfigEconomiaFutbol8(ConfigEconomiaFutbol8 obj)
            throws DAOException {
        ConfigEconomiaFutbol8DAO dao = new ConfigEconomiaFutbol8DAO();
        dao.save(obj);
    }
    
    public static void eliminarJugadorFutbol8(JugadorFutbol8 obj) throws DAOException{
        JugadorFutbol8DAO dao = new JugadorFutbol8DAO();
        dao.delete(obj);
    }
    
    public static void eliminarJuvenilFutbol8(JuvenilFutbol8 obj) throws DAOException{
        JuvenilFutbol8DAO dao = new JuvenilFutbol8DAO();
        dao.delete(obj);
    }
    
    public static void eliminarEntrenadorFutbol8(EntrenadorFutbol8 obj) throws DAOException{
        EntrenadorFutbol8DAO dao = new EntrenadorFutbol8DAO();
        dao.delete(obj);
    }
    

    public static int valoracionMediaJugadores(Grupo grp) throws DAOException{
        JugadorFutbol8DAO dao = new JugadorFutbol8DAO();
        return dao.getValoracionMediaJugadores(grp);        
    }
    
  
    

    // METODOS PARA LANZAMIENTO DE JORNADA

    public static void grabarDatosCompeticion(CompeticionFutbol8 comp,
            ArrayList<EquipoFutbol8> eqs) throws DAOException{

        grabarCompeticionFutbol8(comp);
        for (JornadaFutbol8 jornada : comp.getJornadas()) {
            grabarJornadaFutbol8(jornada);
            for (PartidoFutbol8 partido : jornada.getPartidos()) {
                grabarPartidoFutbol8(partido);
                if (partido.getEqLocal() != null && partido.getEqVisitante() != null)
                    for (AlineacionFutbol8 alineacion : partido.getAlineaciones()) 
                        grabarAlineacionFutbol8(alineacion);                
            }
        }

        // solo si es liga creamos sistema de puntuaciones
        if (comp.getClase().equals("Liga")){
            for (EquipoFutbol8 eq : eqs) {
                PuntuacionFutbol8 punt = new PuntuacionFutbol8();
                punt.setCompeticion(comp);
                punt.setEquipo(eq);
                punt.setClub(eq.getNombre());
                eq.setPuntuacion(punt);
                JDBCDAOFutbol8.grabarPuntuacionFutbol8(punt);
            }
        }

    }

    public static void eliminarCompeticionFutbol8(CompeticionFutbol8 comp) throws DAOException{
        
        CompeticionFutbol8DAO daocomp = new CompeticionFutbol8DAO();
        JornadaFutbol8DAO daojor = new JornadaFutbol8DAO();
        PartidoFutbol8DAO daopar = new PartidoFutbol8DAO();
        AlineacionFutbol8DAO daoali = new AlineacionFutbol8DAO();
        EstadisticaPartidoFutbol8DAO daoest = new EstadisticaPartidoFutbol8DAO();
        PuntuacionFutbol8DAO daopunt = new PuntuacionFutbol8DAO();  
        GoleadorFutbol8DAO daogol = new GoleadorFutbol8DAO();
        
        ArrayList<JornadaFutbol8> jors = daojor.getJornadas(comp);
        for (JornadaFutbol8 jor : jors) {
            ArrayList<PartidoFutbol8> partidos = daopar.getPartidos(jor);
            for (PartidoFutbol8 part : partidos) {                    
                ArrayList<AlineacionFutbol8> alins = daoali.getAlineaciones(part);
                for (AlineacionFutbol8 ali : alins) {
                    daoali.delete(ali);
                }
                EstadisticaPartidoFutbol8 est = daoest.getEstadisticaByPartido(part);
                if (est != null) daoest.delete(est);
                daopar.delete(part);
            }
            daojor.delete(jor);
        }
        ArrayList<PuntuacionFutbol8> punts = daopunt.getPuntuacionesCompeticion(comp);
        for (PuntuacionFutbol8 puntuacionFutbol8 : punts) {
            daopunt.delete(puntuacionFutbol8);
        }
        
        ArrayList<GoleadorFutbol8> goleadores = obtenerGoleadores(comp);
        for (GoleadorFutbol8 jug : goleadores) {
            daogol.delete(jug);
        }
            
        daocomp.delete(comp);
        
    }    
    
    
    public static void eliminarCompeticionesFutbol8(Grupo grp) throws DAOException{
        
        CompeticionFutbol8DAO daocomp = new CompeticionFutbol8DAO();
        
        ArrayList<CompeticionFutbol8> comps = daocomp.getCompeticiones(grp);
        
        for (CompeticionFutbol8 comp : comps) {
            eliminarCompeticionFutbol8(comp);
        }        

    }
    
     public static void eliminarEquipoFutbol8(EquipoFutbol8 eq) throws DAOException{
        
        if (JDBCDAOFutbol8.competicionActiva(eq.getClub().getGrupo(), "Liga") != null)
            throw new UnsupportedOperationException("No se puede dar de baja el equipo, hay competiciones activas");
        
        EquipoFutbol8DAO daoeq = new EquipoFutbol8DAO();
        AlineacionFutbol8DAO daoali = new AlineacionFutbol8DAO();
        EntrenadorFutbol8DAO daoent = new EntrenadorFutbol8DAO();
        JugadorFutbol8DAO daojug = new JugadorFutbol8DAO();
        VacacionFutbol8DAO daovac = new VacacionFutbol8DAO();
        
        JDBCDAOMovimiento.eliminarMovimientos(eq);
        daoali.eliminarAlineaciones(eq);
        daoent.eliminarEntrenador(eq);
        daojug.eliminarJugadores(eq);
        
        VacacionFutbol8 vac = obtenerDatosVacacionesFutbol8(eq);
        if (vac != null) daovac.delete(vac);
        
        daoeq.delete(eq);        
        
    }
     
   
    
    

}
