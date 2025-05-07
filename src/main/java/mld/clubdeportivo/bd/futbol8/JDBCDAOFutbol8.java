
package mld.clubdeportivo.bd.futbol8;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import mld.clubdeportivo.base.Club;
import mld.clubdeportivo.base.EquipoMaestro;
import mld.clubdeportivo.base.Grupo;
import mld.clubdeportivo.base.futbol8.*;
import static mld.clubdeportivo.base.futbol8.EquipoFutbol8.clasificarEquipos;
import mld.clubdeportivo.bd.*;
import static mld.clubdeportivo.bd.JDBCDAOClub.obtenerSimpleClub;
import static mld.clubdeportivo.bd.JDBCDAOGrupo.obtenerSimpleGrupo;
import static mld.clubdeportivo.bd.JDBCDAOMovimiento.eliminarMovimientos;


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
        
        var dao = new CompeticionFutbol8DAO();
        
        return dao.getCompeticionesGanadas(club, clase); 
        
    }
    

    public static CompeticionFutbol8 competicionActiva(Grupo grp, String clase)
            throws DAOException{

        var dao = new CompeticionFutbol8DAO();
        var comp = dao.getCompeticionActiva(grp, clase);
        if (comp != null) comp.setGrupo(grp);

        return comp;

    }
    
     public static CompeticionFutbol8 competicion(long id)
            throws DAOException{

        var dao = new CompeticionFutbol8DAO();
        var comp = (CompeticionFutbol8) dao.getObjetoById(id);

        return comp;

    }
    
    public static long idEquipoFutbol8(Club club) throws DAOException{

        var dao = new EquipoFutbol8DAO();

        return dao.idEquipoFutbol8(club);

    }
    
    public static VacacionFutbol8 obtenerDatosVacacionesFutbol8(EquipoFutbol8 eq) 
            throws DAOException{
        
        var daovac = new VacacionFutbol8DAO();
        return daovac.getByEquipo(eq);
        
    }
    
    public static ArrayList<EquipoFutbol8> listaEquiposFutbol8(Grupo grp, boolean soloActivos)
            throws DAOException{
        // Devuelve lista de equipos con club  y grupo

        var dao = new EquipoFutbol8DAO();
        var lista  = dao.getEquiposFubtol8();
        for (var eq : lista) {
            eq.setClub(obtenerSimpleClub(eq.getIdClub())); 
        }
        var listaFinal = new ArrayList<EquipoFutbol8>();
        for (var eq : lista) {
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
        
        var daoent = new EntrenadorFutbol8DAO();
        return daoent.getEntrenadorFutbol8(eq);
        
    }
    
    public static EntrenadorFutbol8 obtenerEntrenador(long id) 
            throws DAOException{
        
        var daoent = new EntrenadorFutbol8DAO();
        return (EntrenadorFutbol8) daoent.getObjetoById(id);
        
    }
    
    public static JugadorFutbol8 obtenerJugador(long id, EquipoFutbol8 eq) 
            throws DAOException{
        
        var daojug = new JugadorFutbol8DAO();
        var jug = (JugadorFutbol8) daojug.getJugadorByEquipo(id, eq);
        return jug;
        
    }
    
     public static JuvenilFutbol8 obtenerJuvenil(EquipoFutbol8 eq) 
            throws DAOException{
        
        var daojug = new JuvenilFutbol8DAO();
        var jug = (JuvenilFutbol8) daojug.getJuvenilByEquipo(eq);
        if (jug != null){
            eq.setJuvenil(jug);
            jug.setEquipo(eq);
        }
        
        return jug;
        
    }
    
     public static ArrayList<GoleadorFutbol8> obtenerGoleadores(CompeticionFutbol8 comp)
            throws DAOException{
        // Devuelve lista de goleadores

         var dao = new GoleadorFutbol8DAO();
         
         return dao.getByCompeticion(comp);

    }
     
     public static ArrayList<PorteroFutbol8> obtenerPorteros(CompeticionFutbol8 comp)
            throws DAOException{
        // Devuelve lista de goleadores

         var dao = new PorteroFutbol8DAO();
         
         return dao.getByCompeticion(comp);

    }
    
    public static boolean existeJugador(String nombre, Grupo grp) 
            throws DAOException{
        
        var daojug = new JugadorFutbol8DAO();
        var jug = (JugadorFutbol8) daojug.getJugadorByNombre(nombre, grp);
        return jug != null;
        
    }
    
    public static ArrayList<JugadorFutbol8> obtenerJugadores(EquipoFutbol8 eq) 
            throws DAOException{
        
        var daojug = new JugadorFutbol8DAO();
        var lista = 
                (ArrayList<JugadorFutbol8>) daojug.getJugadoresFutbol8(eq);
        
        eq.setJugadores(lista);
        
        for (var jug : lista) {
            jug.setEquipo(eq);
            jug.setGrupo(eq.getClub().getGrupo());
        }        
        
        return lista;
        
    }
    
     public static ArrayList<JugadorFutbol8> obtenerJugadoresLibresGrupo(Grupo grp) 
            throws DAOException{
         
         var daojug = new JugadorFutbol8DAO();
         return (ArrayList<JugadorFutbol8>) daojug.getJugadoresFutbol8Libres(grp);
     }
     
    
    
    public static ArrayList<JugadorFutbol8> obtenerJugadoresGrupo(Grupo grp) 
            throws DAOException{
        
        var daojug = new JugadorFutbol8DAO();
        var eqs = listaEquiposFutbol8(grp);
        var lista = new ArrayList<JugadorFutbol8>();
        for (var eq : eqs) {
            lista.addAll(obtenerJugadores(eq));
        }
 
        lista.addAll(obtenerJugadoresLibresGrupo(grp));
        
        ArrayList subastados = daojug.getJugadorEquipoSubasta(grp);
        for (var  obj : subastados) {
            var datos = (HashMap<String,Long>) obj;
            for (var jug : lista) 
                if (jug.getId() == datos.get("id"))
                    for (var eq : eqs) 
                        if (eq.getId() == datos.get("equipopuja"))
                            jug.setEquipoPuja(eq.getId());                
        }
       
        return lista;
        
    }
    
    public static ArrayList<EntrenadorFutbol8> obtenerEntrenadoresGrupo(Grupo grp) 
            throws DAOException{
        
        var dao = new EntrenadorFutbol8DAO();
        ArrayList<EntrenadorFutbol8> lista =
                (ArrayList) dao.getEntrenadoresFutbol8(grp);
        
        return lista;
        
    }
    
    public static ArrayList<EntrenadorFutbol8> obtenerEntrenadoresLibresGrupo(Grupo grp) 
            throws DAOException{
        
        var dao = new EntrenadorFutbol8DAO();
        ArrayList<EntrenadorFutbol8> lista =
                (ArrayList) dao.getEntrenadoresLibresFutbol8(grp);
        
        return lista;
        
    }
    
    public static int obtenerNumeroEntrenadoresGrupo(Grupo grp) 
            throws DAOException{
        
        var dao = new EntrenadorFutbol8DAO();
        return dao.getNumEntrenadoresFutbol8(grp);
        
    }
           
   
   

    public static EquipoFutbol8 obtenerSimpleEquipoFutbol8(long id)
            throws DAOException{
        // Devuelve equipo futbol8 con sus relaciones directas y grupo

        var daoeq = new EquipoFutbol8DAO();
        var daoclub = new ClubDAO();
        var daojf8 = new JugadorFutbol8DAO();
        var daoentr = new EntrenadorFutbol8DAO();
        var eq = daoeq.getEquipoFubtol8ById(id);
        
        // Si ya no existe el equipo porque lo hemos eliminado 
        // devolvemos uno sin nada
        if (eq == null){
            eq = new EquipoFutbol8();
            eq.setId(id);
            return eq;
        }
        
        var idclub = daoeq.idClub(eq);
        var club = obtenerSimpleClub(idclub);
        var idgrp = daoclub.idGrupo(club);
        var grp = obtenerSimpleGrupo(idgrp);
        eq.setClub(club);
        club.setGrupo(grp);
        grp.getClubs().add(club);

        var jugsf8 =
                (ArrayList<JugadorFutbol8>) daojf8.getJugadoresFutbol8(eq);

        eq.setJugadores(jugsf8);

        for (var jugf8 : jugsf8) {
            jugf8.setEquipo(eq);
            jugf8.setGrupo(grp);
        }
        var entr = daoentr.getEntrenadorFutbol8(eq);
        if (entr != null){
            entr.setEquipo(eq);
            eq.setEntrenador(entr);
        }

        return eq;

    }

    public static EquipoFutbol8 obtenerSimpleEquipoFutbol8(Club club)
            throws DAOException{
        // Devuelve equipo futbol8 con sus relaciones directas y grupo


        var daoeq = new EquipoFutbol8DAO();
        var eq = daoeq.getEquipoFubtol8ByClub(club);

        return obtenerSimpleEquipoFutbol8(eq.getId());

    }
    
    public static CompeticionFutbol8 obtenerCompeticion(
            Long id) throws DAOException{
        
        var daocomp = new CompeticionFutbol8DAO();
        return daocomp.getCompeticionById(id);    
        
    }
    

    public static CompeticionFutbol8 obtenerDatosCompeticion(
            CompeticionFutbol8 comp) throws DAOException{

        var daojor = new JornadaFutbol8DAO();
        var daopar = new PartidoFutbol8DAO();
        var eqs =
                listaEquiposFutbol8(comp.getGrupo());
        var mapeoEqs = new HashMap();
        for (var eq : eqs) {
            mapeoEqs.put(eq.getId(), eq);
        }
        var jornadas = daojor.getJornadas(comp);
        comp.setJornadas(jornadas);

        for (var jornada : jornadas) {
            jornada = daopar.obtenerPartidos(jornada, mapeoEqs);
        }

        return comp;
    }
    
     public static CompeticionFutbol8 obtenerDatosHistoCompeticion(
            CompeticionFutbol8 comp) throws DAOException{

        var daojor = new JornadaFutbol8DAO();
        var daopar = new PartidoFutbol8DAO();
        var daoest = new EstadisticaPartidoFutbol8DAO();
        var jornadas = daojor.getJornadas(comp);
        comp.setJornadas(jornadas);

        for (var jornada : jornadas) {
            var partidos = daopar.getPartidos(jornada);
            jornada.setPartidos(partidos);
            for (var partido : partidos) {
                partido.setJornada(jornada);
                var est = daoest.getEstadisticaByPartido(partido);
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

        var daocomp = new CompeticionFutbol8DAO();
        var comps = daocomp.getCompeticionesNoActivas(grp);
        for (var comp : comps) {
            comp.setGrupo(grp);
        }
       
        return comps;

    }
    
    public static ArrayList<CompeticionFutbol8> listaCompetionesGrupo(
            Grupo grp) throws DAOException{
        
        var daocomp = new CompeticionFutbol8DAO();
        var comps = daocomp.getCompeticiones(grp);
        for (var comp : comps) {
            comp.setGrupo(grp);
        }
       
        return comps;

    }
    
    public static ArrayList<CompeticionFutbol8> listaCompetiones(
            ) throws DAOException{
        
        var daocomp = new CompeticionFutbol8DAO();
        var comps = daocomp.getCompeticiones(); 
       
        return comps;

    }
    
    
    public static ArrayList<EquipoFutbol8> obtenerClasificacion(
             CompeticionFutbol8 comp) throws DAOException{

        var daopunt = new PuntuacionFutbol8DAO();
        var eqs = new ArrayList<EquipoFutbol8>();
        var puntos = 
                daopunt.getPuntuacionesCompeticion(comp);
        for (var punt : puntos) {
            var eq = obtenerSimpleEquipoFutbol8(daopunt.idEquipo(punt));
            if (eq == null)
            {
                var club = new Club();
                club.setNombre(punt.getClub());
                eq = new EquipoFutbol8();
                eq.setClub(club);
            }  
            eqs.add(eq);
            eq.setPuntuacion(punt);
            punt.setEquipo(eq);
            
        }

        clasificarEquipos(eqs);

        return eqs;
    }
   
    public static ArrayList<EquipoFutbol8> obtenerClasificacion(
            Grupo grp) throws DAOException{
       
        var comp = competicionActiva(grp, "Liga");       

        return obtenerClasificacion(comp);
    }
    
    public static JornadaFutbol8 obtenerJornadaSimple(
            int numero, CompeticionFutbol8 comp) throws DAOException{
        
        var jordao= new JornadaFutbol8DAO();
        var pardao= new PartidoFutbol8DAO();
        var jor = (JornadaFutbol8) jordao.getJornadaByNumero(comp, numero);
        if (jor != null){
            var partidos = pardao.getPartidos(jor);
            jor.setPartidos(partidos);
            for (var partido : partidos)
                partido.setJornada(jor); 
        }
        
        return jor;        
        
    }
    
    public static JornadaFutbol8 obtenerJornada(
            int numero, CompeticionFutbol8 comp) throws DAOException{
        
        var jordao= new JornadaFutbol8DAO();
        var jor = (JornadaFutbol8) jordao.getJornadaByNumero(comp, numero);
        var eqs = listaEquiposFutbol8(comp.getGrupo());
        
        return obtenerDatosJornada(jor, comp, eqs);
    }
    
    
    public static JornadaFutbol8 obtenerJornada(
            long id, Grupo grp) throws DAOException{
        
        var jordao= new JornadaFutbol8DAO();
        var compdao= new CompeticionFutbol8DAO();
        var jor = (JornadaFutbol8) jordao.getObjetoById(id);
        var idComp = jordao.getIdCompeticion(jor);
        var comp = compdao.getCompeticionById(idComp);
        var eqs = listaEquiposFutbol8(grp);
        
        return obtenerDatosJornada(jor, comp, eqs);
    }
    
    public static JornadaFutbol8 obtenerJornadaParaDisputar(
            CompeticionFutbol8 comp, ArrayList<EquipoFutbol8> eqs) throws DAOException{
        
        var jordao= new JornadaFutbol8DAO();
        var jor = jordao.getProximaJornada(comp);
        
        return obtenerDatosJornada(jor, comp, eqs);
        
    }
    
    public static PartidoFutbol8 obtenerProximoPartido(CompeticionFutbol8 comp,
            EquipoFutbol8 eq) throws DAOException{
        
        var daopart = new PartidoFutbol8DAO();
        var daojor = new JornadaFutbol8DAO(); 
        PartidoFutbol8 partido = null;
        
        if (comp != null){
            var jornada = daojor.getProximaJornada(comp);
            partido = daopart.getPartido(jornada, eq);
            if (partido != null)
                partido.setJornada(jornada);
            jornada.setCompeticion(comp);
        }
        
        return partido;
        
    }
    
     public static PartidoFutbol8 obtenerDatosPrepararPartido(CompeticionFutbol8 comp,
            EquipoFutbol8 eq, EquipoFutbol8 eqSimul) throws DAOException{

         var daopart = new PartidoFutbol8DAO();
         var partido = obtenerProximoPartido(comp, eq);
         
         if (partido != null){
             EquipoFutbol8 EqLocal, EqVisitante;
             var idEqLocal = daopart.getIdEqLocal(partido);            
             if (idEqLocal == eq.getId()){
                 EqLocal = eq;
                 if (eqSimul == null){
                     var idEqVisitante = daopart.getIdEqVisitante(partido);
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
         var alidao = new AlineacionFutbol8DAO();         
         
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
         
         var daoest = new EstadisticaPartidoFutbol8DAO(); 
         var puntdao = new PuntuacionFutbol8DAO();
         var eqdao = new EquipoFutbol8DAO();
         var entdao = new EntrenadorFutbol8DAO();
         
         EquipoFutbol8 eqf8Local, eqf8Visit;
         
        
         var est = daoest.getEstadisticaByPartido(partido);
         if (est == null)// es simulacion le creamos una
             est = new EstadisticaPartidoFutbol8();
         est.setPartido(partido);
         partido.setEstadistica(est);

         var alins = new ArrayList<AlineacionFutbol8>();
         var comp = 
                 (CompeticionFutbol8) partido.getJornada().getCompeticion();
         
         eqf8Local = (EquipoFutbol8) partido.getEqLocal(); 
         eqf8Visit = (EquipoFutbol8) partido.getEqVisitante();         
         
         EquipoFutbol8[] eqs = {eqf8Local,eqf8Visit};
         for (var eq : eqs){
             var jugs = 
                     (ArrayList<JugadorFutbol8>) eqdao.getJugadoresFutbol8(eq);
             eq.setJugadores(jugs);
             for (var jug : jugs) {
                 jug.setEquipo(eq);
                 jug.setGrupo(eq.getClub().getGrupo());
             }
             
             if (eq.getAlineacion() == null)
                 alins.add(obtenerAlineacionPartido(partido, eq));
             else
                 alins.add(eq.getAlineacion());
             
             if (comp.getClase().equals("Liga")){
                 var punt = puntdao.getPuntosByEquipo(comp, eq);
                 eq.setPuntuacion(punt);
                 punt.setEquipo(eq);
                 punt.setCompeticion(comp);
             }
             
             var entr = entdao.getEntrenadorFutbol8(eq);
             eq.setEntrenador(entr);
             entr.setEquipo(eq);
             entr.setGrupo(eq.getClub().getGrupo());
         }
         
         partido.setAlineaciones(alins);
         
         return partido;
         
     }
    
    

    public static JornadaFutbol8 obtenerDatosJornada(JornadaFutbol8 jor,
            CompeticionFutbol8 comp, ArrayList<EquipoFutbol8> eqs) throws DAOException{

        var daopar = new PartidoFutbol8DAO();   
   
        jor.setCompeticion(comp);
        
        var mapeoEqs = new HashMap();
        for (var eq : eqs) {
            mapeoEqs.put(eq.getId(), eq);
        }

        daopar.obtenerPartidos(jor, mapeoEqs);
        
        for (var partido : jor.getPartidos()) {
            
            obtenerDatosPartido(partido);
        }
  
        return jor;
    }
    
    public static EstadisticaPartidoFutbol8 obtenerEstadisticaPartido(
            PartidoFutbol8 partido) throws DAOException{
        
        var dao = new EstadisticaPartidoFutbol8DAO();
        
        return dao.getEstadisticaByPartido(partido);
        
    }
    
    public static ArrayList<CronicaFutbol8> obtenerCronica(
            PartidoFutbol8 partido) throws DAOException{
        
        var dao = new CronicaFutbol8DAO();
        
        return dao.getCronicaByPartido(partido);
        
    }
    public static ArrayList<CronicaFutbol8> obtenerCronica(
            long idPartido) throws DAOException{
        
        var dao = new CronicaFutbol8DAO();
        
        return dao.getCronicaByPartido(idPartido);
        
    }

     
    
    public static ArrayList<JugadorMaestroFutbol8> obtenerJugadoresMaestros(
            PosicionJugFutbol8 posicion) throws DAOException{
        
        var DAOJugMaestro = new JugadorMaestroFutbol8DAO();
        var lista = 
                (ArrayList<JugadorMaestroFutbol8>) DAOJugMaestro.getJugadoresMaestros(posicion);
        
        return lista;
    }
    
    
    public static ArrayList<EquipoMaestro> obtenerEquiposMaestros() throws DAOException{
    
        var DAOEqMaestro = new EquipoMaestroFutbol8DAO();
        var lista = (ArrayList) DAOEqMaestro.getEquiposMaestros();
        
        return lista;
    }
    
    public static ArrayList<EntrenadorMaestroFutbol8> obtenerEntrenadoresMaestros() 
            throws DAOException{
    
        var DAOEntrMaestro = new EntrenadorMaestroFutbol8DAO();
        var lista = (ArrayList) DAOEntrMaestro.getEntrenadoresMaestros();
        
        return lista;        
        
    }
    

    public static PartidoFutbol8 obtenerUltimoPartido(EquipoFutbol8 eq) throws DAOException{
        
        var dao = new PartidoFutbol8DAO();
        
        return dao.getUltimoPartido(eq);
        
    }
    
     public static Date obtenerFechaUltimaJornada() throws DAOException{
        
        var dao = new JornadaFutbol8DAO();
        var obj = dao.getUltimaJornadaDisputada();
        
        Date fecha = null;
        
        if (obj != null)
            fecha = obj.getFecha();
        
        return fecha;
        
    }
    
    
    
    public static PartidoFutbol8 obtenerPartido(long id) throws DAOException{
        
        var dao = new PartidoFutbol8DAO();
        var daoali = new AlineacionFutbol8DAO();
        var partido = (PartidoFutbol8) dao.getObjetoById(id);
        var eqLocal = obtenerSimpleEquipoFutbol8(dao.getIdEqLocal(partido));
        var eqVisitante = obtenerSimpleEquipoFutbol8(dao.getIdEqVisitante(partido));
        
        
        partido.setEqLocal(eqLocal);
        partido.setEqVisitante(eqVisitante);
        partido.setAlineaciones(daoali.getAlineaciones(partido));  
        partido.setCronica(obtenerCronica(partido));
                
        return partido;     
        
    }
    
    public static ConfigEconomiaFutbol8 obtenerConfig(Grupo grp) throws DAOException{
        
        var dao = new ConfigEconomiaFutbol8DAO();
        var conf = dao.getConfigByGrupo(grp);
        
        if (conf != null){
            conf.setGrupo(grp);
            if(conf.getIdEquipoGestor() != null && conf.getIdEquipoGestor() != 0){            
                var eq = obtenerSimpleEquipoFutbol8(conf.getIdEquipoGestor());
                conf.setEquipoGestor(eq);
            }
        }
        
        return conf;
        
    }
    
    public static long idJornada(PartidoFutbol8 partido) throws DAOException{
        
        var dao = new PartidoFutbol8DAO();
        return dao.getIdJornada(partido);
    }
   
   
    public static void grabarEquipoFutbol8(EquipoFutbol8 obj) throws DAOException{
        var dao = new EquipoFutbol8DAO();
        dao.save(obj);
    }

    public static void grabarJugadorFutbol8(JugadorFutbol8 obj) throws DAOException{
        var dao = new JugadorFutbol8DAO();
        dao.save(obj);
    }
    
    public static void grabarGoleadorFutbol8(GoleadorFutbol8 obj) throws DAOException{
        var dao = new GoleadorFutbol8DAO();
        dao.save(obj);
     }
    
    public static void grabarPorteroFutbol8(PorteroFutbol8 obj) throws DAOException{
        var dao = new PorteroFutbol8DAO();
        dao.save(obj);
     }
    
    public static void grabarJuvenilFutbol8(JuvenilFutbol8 obj) throws DAOException{
        var dao = new JuvenilFutbol8DAO();
        dao.save(obj);
    }
    
    public static void grabarEntrenadorFutbol8(EntrenadorFutbol8 obj) throws DAOException{
        var dao = new EntrenadorFutbol8DAO();
        dao.save(obj);
    }

    public static void grabarJugadorMaestroFutbol8(JugadorMaestroFutbol8 obj)
            throws DAOException {
        var dao = new JugadorMaestroFutbol8DAO();
        dao.save(obj);
    }

    public static void grabarEntrenadorMaestroFutbol8(EntrenadorMaestroFutbol8 obj)
            throws DAOException {
        var dao = new EntrenadorMaestroFutbol8DAO();
        dao.save(obj);
    }
    
    public static void grabarVacacionFutbol8(VacacionFutbol8 obj)
            throws DAOException {
        var dao = new VacacionFutbol8DAO();
        dao.save(obj);
    }

    public static void grabarEquipoMaestroFutbol8(EquipoMaestro obj)
            throws DAOException {
        var dao = new EquipoMaestroFutbol8DAO();
        dao.save(obj);
    }
   
    public static void grabarCompeticionFutbol8(CompeticionFutbol8 obj)
            throws DAOException {
        var dao = new CompeticionFutbol8DAO();
        dao.save(obj);
    }

    public static void grabarJornadaFutbol8(JornadaFutbol8 obj)
            throws DAOException {
        var dao = new JornadaFutbol8DAO();
        dao.save(obj);
    }

    public static void grabarPartidoFutbol8(PartidoFutbol8 obj)
            throws DAOException {
        var dao = new PartidoFutbol8DAO();
        dao.save(obj);
    }

    public static void grabarPuntuacionFutbol8(PuntuacionFutbol8 obj)
            throws DAOException {
        var dao = new PuntuacionFutbol8DAO();
        dao.save(obj);
    }

    public static void grabarAlineacionFutbol8(AlineacionFutbol8 obj)
            throws DAOException {
        obj.validarAlineacion();
        var dao = new AlineacionFutbol8DAO();
        dao.save(obj);
    }
    
    public static void grabarEstadisticaPartidoFutbol8(EstadisticaPartidoFutbol8 obj)
            throws DAOException {
        var dao = new EstadisticaPartidoFutbol8DAO();
        dao.save(obj);
    }
    
    public static void grabarCronicaFutbol8(CronicaFutbol8 obj)
            throws DAOException {
        var dao = new CronicaFutbol8DAO();
        dao.save(obj);
    }
    
    public static void grabarConfigEconomiaFutbol8(ConfigEconomiaFutbol8 obj)
            throws DAOException {
        var dao = new ConfigEconomiaFutbol8DAO();
        dao.save(obj);
    }
    
    public static void eliminarJugadorFutbol8(JugadorFutbol8 obj) throws DAOException{
        var dao = new JugadorFutbol8DAO();
        dao.delete(obj);
    }
    
    public static void eliminarJuvenilFutbol8(JuvenilFutbol8 obj) throws DAOException{
        var dao = new JuvenilFutbol8DAO();
        dao.delete(obj);
    }
    
    public static void eliminarEntrenadorFutbol8(EntrenadorFutbol8 obj) throws DAOException{
        var dao = new EntrenadorFutbol8DAO();
        dao.delete(obj);
    }
    

    public static int valoracionMediaJugadores(Grupo grp) throws DAOException{
        var dao = new JugadorFutbol8DAO();
        return dao.getValoracionMediaJugadores(grp);        
    }
    
  
    

    // METODOS PARA LANZAMIENTO DE JORNADA

    public static void grabarDatosCompeticion(CompeticionFutbol8 comp,
            ArrayList<EquipoFutbol8> eqs) throws DAOException{

        grabarCompeticionFutbol8(comp);
        for (var jornada : comp.getJornadas()) {
            grabarJornadaFutbol8(jornada);
            for (var partido : jornada.getPartidos()) {
                grabarPartidoFutbol8(partido);
                if (partido.getEqLocal() != null && partido.getEqVisitante() != null)
                    for (var alineacion : partido.getAlineaciones()) 
                        grabarAlineacionFutbol8(alineacion);                
            }
        }

        // solo si es liga creamos sistema de puntuaciones
        if (comp.getClase().equals("Liga")){
            for (var eq : eqs) {
                var punt = new PuntuacionFutbol8();
                punt.setCompeticion(comp);
                punt.setEquipo(eq);
                punt.setClub(eq.getNombre());
                eq.setPuntuacion(punt);
                grabarPuntuacionFutbol8(punt);
            }
        }

    }

    public static void eliminarCompeticionFutbol8(CompeticionFutbol8 comp) throws DAOException{
        
        var daocomp = new CompeticionFutbol8DAO();
        var daojor = new JornadaFutbol8DAO();
        var daopar = new PartidoFutbol8DAO();
        var daoali = new AlineacionFutbol8DAO();
        var daoest = new EstadisticaPartidoFutbol8DAO();
        var daopunt = new PuntuacionFutbol8DAO();
        var daogol = new GoleadorFutbol8DAO();
        var jors = daojor.getJornadas(comp);
        for (var jor : jors) {
            var partidos = daopar.getPartidos(jor);
            for (var part : partidos) {                    
                var alins = daoali.getAlineaciones(part);
                for (var ali : alins) {
                    daoali.delete(ali);
                }
                var est = daoest.getEstadisticaByPartido(part);
                if (est != null) daoest.delete(est);
                daopar.delete(part);
            }
            daojor.delete(jor);
        }
        var punts = daopunt.getPuntuacionesCompeticion(comp);
        for (var puntuacionFutbol8 : punts) {
            daopunt.delete(puntuacionFutbol8);
        }
        var goleadores = obtenerGoleadores(comp);
        for (var jug : goleadores) {
            daogol.delete(jug);
        }
            
        daocomp.delete(comp);
        
    }    
    
    
    public static void eliminarCompeticionesFutbol8(Grupo grp) throws DAOException{
        
        var daocomp = new CompeticionFutbol8DAO();
        var comps = daocomp.getCompeticiones(grp);
        for (var comp : comps) {
            eliminarCompeticionFutbol8(comp);
        }        

    }
    
     public static void eliminarEquipoFutbol8(EquipoFutbol8 eq) throws DAOException{
        
        if (competicionActiva(eq.getClub().getGrupo(), "Liga") != null)
            throw new UnsupportedOperationException("No se puede dar de baja el equipo, hay competiciones activas");
        
        var daoeq = new EquipoFutbol8DAO();
        var daoali = new AlineacionFutbol8DAO();
        var daoent = new EntrenadorFutbol8DAO();
        var daojug = new JugadorFutbol8DAO();
        var daovac = new VacacionFutbol8DAO();
        
        eliminarMovimientos(eq);
        daoali.eliminarAlineaciones(eq);
        daoent.eliminarEntrenador(eq);
        daojug.eliminarJugadores(eq);
        
        var vac = obtenerDatosVacacionesFutbol8(eq);
        if (vac != null) daovac.delete(vac);
        
        daoeq.delete(eq);        
        
    }
     
   
    
    

}
