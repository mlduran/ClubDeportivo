/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mld.clubdeportivo.controladores;

import jakarta.servlet.ServletContext;
import static java.lang.String.valueOf;
import java.util.*;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.getInstance;
import static java.util.Collections.shuffle;
import static java.util.Collections.sort;
import mld.clubdeportivo.base.*;
import mld.clubdeportivo.base.futbol8.*;
import mld.clubdeportivo.bd.DAOException;
import java.util.logging.*;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static mld.clubdeportivo.base.ClaseMovimiento.CapitalInicial;
import static mld.clubdeportivo.base.ClaseMovimiento.Renovaciones;
import static mld.clubdeportivo.base.Deporte.Futbol8;
import static mld.clubdeportivo.base.Entrenador.FICHA_INICIAL;
import static mld.clubdeportivo.base.Entrenador.NUM_MAX_ENTRENADORES_LIBRES;
import static mld.clubdeportivo.base.Equipo.PRESUPUESTO_INICIAL;
import static mld.clubdeportivo.base.futbol8.EquipoFutbol8.NUMERO_MAX_JUGADORES;
import static mld.clubdeportivo.base.futbol8.PosicionElegidaFutbol8.Cualquiera;
import static mld.clubdeportivo.base.futbol8.PosicionJugFutbol8.Defensa;
import static mld.clubdeportivo.base.futbol8.PosicionJugFutbol8.Delantero;
import static mld.clubdeportivo.base.futbol8.PosicionJugFutbol8.Medio;
import static mld.clubdeportivo.base.futbol8.PosicionJugFutbol8.Portero;
import static mld.clubdeportivo.base.futbol8.PosicionJugFutbol8.valueOf;
import static mld.clubdeportivo.base.futbol8.TacticaFutbol8.tacticasFutbol8;
import static mld.clubdeportivo.bd.JDBCDAOClub.grabarClub;
import static mld.clubdeportivo.bd.JDBCDAOClub.listaClubs;
import static mld.clubdeportivo.bd.JDBCDAOMovimiento.grabarRegistro;
import static mld.clubdeportivo.bd.JDBCDAONoticia.grabarNoticia;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.competicionActiva;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.eliminarEntrenadorFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.existeJugador;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.grabarEntrenadorFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.grabarEquipoFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.grabarJugadorFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.grabarJuvenilFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerEntrenadoresGrupo;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerEntrenadoresLibresGrupo;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerEntrenadoresMaestros;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerEquiposMaestros;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerJugadoresGrupo;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerJugadoresMaestros;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerJuvenil;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerNumeroEntrenadoresGrupo;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerProximoPartido;
import static mld.clubdeportivo.utilidades.Calculos.listaValoresAleatorios;
import static mld.clubdeportivo.utilidades.Calculos.obtener;
import static mld.clubdeportivo.utilidades.Calculos.valorAleatorio;
import static mld.clubdeportivo.utilidades.Calculos.valoresAleatoriosSinRepetir;

/**
 *
 * @author Miguel
 */
public class UtilesFutbol8 {
    
    private static Logger logger = 
            getLogger(UtilesFutbol8.class.getName());
    
    protected static JugadorFutbol8 crearJugadorfutbol8(Grupo grp, EquipoFutbol8 equipo,
             PosicionJugFutbol8 posicion, int val, boolean subasta) throws DAOException {

        JugadorFutbol8 newJug = null;
        
        if (equipo != null && (equipo.getJugadores().size() >= NUMERO_MAX_JUGADORES))
                throw new UnsupportedOperationException("Ya tienes " + NUMERO_MAX_JUGADORES + " jugadores");
        
        var lista = obtenerJugadoresMaestros(posicion);
        shuffle(lista);
        if (grp == null && equipo != null)
            grp = equipo.getClub().getGrupo();                

        JugadorMaestroFutbol8 jug = null;
        boolean existe;
        // comprobamos si ya lo tenemos en el grupo        
        for (var jugMaestro : lista) {
            jug = jugMaestro;
            existe = existeJugador(jug.getNombre(), grp);
            if (!existe)            
                break;
        }
        
        // Si en el maestro no tenemos jugadores lo creamos con id
        String nombre;
        while (jug == null){
            nombre = "jugador" + valorAleatorio(1111, 9999);
            jug = new JugadorMaestroFutbol8(nombre, posicion);
            existe = existeJugador(jug.getNombre(), grp);
            if (existe)            
                jug = null;
        }

        if (jug != null){

            if (equipo == null)
                newJug = new JugadorFutbol8(grp, jug.getNombre(),
                    jug.getPosicion(), val );
            else 
                newJug = new JugadorFutbol8(
                        grp,
                        equipo, jug.getNombre(),
                        jug.getPosicion(), val );
            if (subasta)
                newJug.setBlindado(false);
            
            newJug.setEnSubasta(subasta);

            grabarJugadorFutbol8(newJug);
            if (equipo != null){
                equipo.getJugadores().add(newJug);
                newJug.setEquipo(equipo);
            }

        }
        
        return newJug;

    }
    
    protected static JugadorFutbol8 crearJugadorfutbol8(EquipoFutbol8 equipo,
             PosicionJugFutbol8 posicion, int val) throws DAOException {

        return crearJugadorfutbol8(null, equipo, posicion, val, false);

    }
    
    protected static JugadorFutbol8 crearJugadorOjeadofutbol8(EquipoFutbol8 equipo
            ) throws DAOException {
        // Crea un jugador con valoracion minima de 30 + eqTecnico * 5
       
        var npos = valorAleatorio(PosicionJugFutbol8.values().length);
        var posicion = PosicionJugFutbol8.values()[npos];
        var eqTecnico = equipo.getEqTecnico();
        var min = 30 + eqTecnico * 5;
        var val = min + valorAleatorio(10);       
        
        if (val > 100) val = 100;
        if (val < 10) val = 10;
        return crearJugadorfutbol8(null, equipo, posicion, val, false);
    }

    protected static JugadorFutbol8 crearJugadorSubasta(Grupo grp,
            PosicionJugFutbol8 posicion, int val) throws DAOException {

        return crearJugadorfutbol8(grp, null, posicion, val, true);

    }
    
    protected static void altaJugadoresEquipoFutbol8(EquipoFutbol8 eq
             ) throws DAOException{
        
        int numJugs;        
        var vals = new ArrayList<Integer>();
        
        ArrayList posiciones = new ArrayList<PosicionJugFutbol8>();
        posiciones.add(Portero);
        posiciones.add(Portero);
        vals.addAll(listaValoresAleatorios(2, 50, 10, 100));
        
        posiciones.add(Defensa);
        posiciones.add(Defensa);
        posiciones.add(Defensa);
        posiciones.add(Defensa);
        vals.addAll(listaValoresAleatorios(4, 50, 10, 100));
        
        posiciones.add(Medio);
        posiciones.add(Medio);
        posiciones.add(Medio);
        posiciones.add(Medio);
        vals.addAll(listaValoresAleatorios(4, 50, 10, 100));
        
        posiciones.add(Delantero);
        posiciones.add(Delantero);
        vals.addAll(listaValoresAleatorios(2, 50, 10, 100));
        
        if (eq.isAutomatico()) {
            numJugs = 16;
            posiciones.add(Defensa);
            posiciones.add(Medio);
            posiciones.add(Delantero);
            posiciones.add(Delantero);
            vals.addAll(listaValoresAleatorios(4, 50, 10, 100));
            
        } else {
            numJugs = 12;
        }

        for (var i = 0; i < numJugs; i++){
            crearJugadorfutbol8(eq,(PosicionJugFutbol8) posiciones.get(i),
                    vals.get(i));
        }

    }
    
    protected static  EquipoFutbol8 altaEquipoFutbol8(Club club, boolean auto)
             throws DAOException{

        var eq = new EquipoFutbol8(club, auto, new ArrayList());

        grabarEquipoFutbol8(eq);
        eq.setClub(club);

        altaJugadoresEquipoFutbol8(eq);
        crearEntrenadorfutbol8(eq, 5);
        var mov = new Movimiento(eq, CapitalInicial, PRESUPUESTO_INICIAL);
        grabarRegistro(mov);

        return eq;

    }    
   

     protected static EquipoFutbol8 crearEquipoAutomatico(Grupo grp) throws DAOException {
        
        var lista = (ArrayList) obtenerEquiposMaestros();
        shuffle(lista);

        ArrayList<String> listaClubs = new ArrayList();
        for (var club : listaClubs()) {
            listaClubs.add(club.getNombre());
        }

        EquipoMaestro eq = null;
        // comprobamos si ya lo tenemos
        for (var eqMaestro : lista) {
            eq = (EquipoMaestro) eqMaestro;
            if (!listaClubs.contains(eq.getNombre()))
                break;
        }

        if (eq == null)
            throw new UnsupportedOperationException("No hay equipos Futbol8 en el maestro");

        var nomEq = eq.getNombre();
        if (nomEq.length() > 20) nomEq = nomEq.substring(1, 20);
        var contr = valorAleatorio(100000, 999999);
        var club = new Club(grp, nomEq, "Automatico", valueOf(contr), "");
        club.setAuto(true);
        grabarClub(club);

        return altaEquipoFutbol8(club, true);

    }
     
     protected static  void crearEntrenadorfutbol8(Grupo grp, EquipoFutbol8 equipo,
            int numTacts) throws DAOException {


        ArrayList lista = obtenerEntrenadoresMaestros();
        shuffle(lista);
        var entrenadoresExistentes =
                obtenerEntrenadoresGrupo(grp);
        ArrayList<String> listaGrupo = new ArrayList();
        for (var entrGrp : entrenadoresExistentes) {
            listaGrupo.add(entrGrp.getNombre());
        }

        EntrenadorMaestroFutbol8 entr = null;

        // comprobamos si ya lo tenemos en el grupo
        for (var entrMaestro : lista) {

            entr = (EntrenadorMaestroFutbol8) entrMaestro;
            if (!listaGrupo.contains(entr.getNombre()))
                break;
        }

        if (entr != null){
            
            EntrenadorFutbol8 newEntr;
            if (equipo == null){
                // Si el equipo es null es un nuevo entrenador
                // Comprobamos los que hay en el grupo y si 
                // supera el numero establecido eliminamos uno antes
                var libres = 
                        obtenerEntrenadoresLibresGrupo(grp);
                if (libres.size() >= NUM_MAX_ENTRENADORES_LIBRES){
                    var x = valorAleatorio(libres.size());
                    var entrElim = libres.get(x);
                    if (entrElim != null)
                        eliminarEntrenadorFutbol8(entrElim);
                    else
                        throw new NullPointerException("El entrenador es nulo");
                }
                newEntr = new EntrenadorFutbol8(grp, entr.getNombre());
            }
            else{
                newEntr = new EntrenadorFutbol8(equipo, entr.getNombre());
                newEntr.setGrupo(grp);
            }
            
            newEntr.setFicha(FICHA_INICIAL);            

            var tacticas =
                    (ArrayList<TacticaFutbol8>) tacticasFutbol8();
            
            newEntr.setTacticas(new ArrayList<>());           

            var codTacts = valoresAleatoriosSinRepetir(numTacts, 0, tacticas.size() - 1);
            for (var i = 0; i < numTacts; i++){
                newEntr.anyadirTactica(tacticas.get(codTacts[i]));
            }

            try{
                grabarEntrenadorFutbol8(newEntr);
            }catch (Exception ex){
                logger.log(SEVERE, "Error al dar de alta entrenador " + ex.getMessage());
            }
            if (equipo != null)
                equipo.setEntrenador(newEntr);
        }

    }
     
     protected static void crearEntrenadorfutbol8(EquipoFutbol8 equipo,
             int numTacts) throws DAOException {
        
        var grp = equipo.getClub().getGrupo();
        crearEntrenadorfutbol8(grp, equipo, numTacts);

    }
     
     protected static void crearEntrenadorfutbol8(Grupo grp,
             int numTacts) throws DAOException {
        
         if (obtenerNumeroEntrenadoresGrupo(grp) < 100)
             crearEntrenadorfutbol8(grp, null, numTacts);

    }
     
     protected static ArrayList<String> posicionesJugador(){
         
         var lista = new ArrayList<String>();
         for (var pos : PosicionJugFutbol8.values()) {
             lista.add(pos.name());
         }
         
         return lista;
         
     }
     
     protected static ArrayList<String> posicionesJuvenil(){
         
         var lista = new ArrayList<String>();
         for (var pos : PosicionElegidaFutbol8.values()) {
             lista.add(pos.name());
         }
         
         return lista;
         
     }
     
     protected static void darNoticia(Grupo grp, String txt) throws DAOException{
         var noticia = new Noticia(grp, Futbol8, txt, false);
         grabarNoticia(noticia);
     }
     
     protected static void darNoticia(Grupo grp, String txt, boolean isgeneral) throws DAOException{
         var noticia = new Noticia(grp, Futbol8, txt, isgeneral);
         grabarNoticia(noticia);
     }
     
     protected static List<JugadorFutbol8> obtenerGoleadores(Grupo grp) throws DAOException {
        
        var lista = obtenerJugadoresGrupo(grp);
        var jugadores = new ArrayList<JugadorFutbol8>();
       for (var jug : lista){ 
           if (jug.getPosicion().equals(Portero)) continue;
           if (jug.getGolesLiga() > 0 || jug.getGolesCopa() > 0 ) 
               jugadores.add(jug);
       }
        var max = 50;
        
        sort(jugadores, new JugadorFutbol8.GolesComparator());
       
       if (jugadores.size() < max) max = jugadores.size();
        
       return jugadores.subList(0, max);
        
   }
     
     protected static List<JugadorFutbol8> obtenerPorteros(Grupo grp) throws DAOException {
        
        var lista = obtenerJugadoresGrupo(grp);
        var jugadores = new ArrayList<JugadorFutbol8>();
       for (var jug : lista){ 
           if (jug.getPosicion().equals(Portero) && jug.getEquipo() != null)
               jugadores.add(jug);
       }
        var max = 50;
        
        sort(jugadores, new JugadorFutbol8.RatioPorteroComparator());
       
       if (jugadores.size() < max) max = jugadores.size();
        
       return jugadores.subList(0, max);
        
   }
     
     protected static ArrayList<String> tipoProximaCompeticion(ServletContext appManager){
       
        var result = new ArrayList<String>();
        var horaLanzamientoJornada = 13;
       //ServletContext appManager = this.getServletContext();

       var confDiasLiga = appManager.getInitParameter("diasligafutbol8");
       var confDiasCopa = appManager.getInitParameter("diascopafutbol8");
       
       logger.info("confDiasLiga " + confDiasLiga);
       logger.info("confDiasCopa " + confDiasCopa);

       var calendario = getInstance();
       calendario.setTime(new Date());

        var diaAct = calendario.get(DAY_OF_WEEK);
       logger.info("diaAct " + diaAct);
       
       if (calendario.get(HOUR_OF_DAY) > horaLanzamientoJornada - 1) diaAct++;
       logger.info("diaAct " + diaAct);
       
       for (var i = diaAct; i != diaAct - 1; i++){
           var dia = valueOf(i);
           if (confDiasLiga.contains(dia) && !result.contains("Liga")){
               result.add("Liga");
           }
           else if (confDiasCopa.contains(dia) && !result.contains("Copa")){
               result.add("Copa");
           }   
           if (i == 8) i = 0; 
           // No se porque esta comprobacion no se hace correctamente en
           // el for, a lo mejor la hace despues de hacer el i++
           if (i == diaAct - 1) break;
       }
         
       return result;
       
   }

     
     protected static ArrayList<Object> datosProximoPartido(EquipoFutbol8 eq, 
             ServletContext appManager) throws DAOException{
        // Devuelve el proximo partido y la competicion
        
        logger.info("Buscando datos proximo partido" );
         
        var tipoComp = tipoProximaCompeticion(appManager);
        
        logger.log(INFO, "Tipos competicion " );         
        logger.log(INFO, tipoComp.toString());
        
        CompeticionFutbol8 comp = null;
        PartidoFutbol8 proximoPartido = null;
                
        for (var tipo : tipoComp) {
            logger.info("Buscando competiciones tipo " + tipo);
            logger.info("En grupo " + eq.getClub().getGrupo().getIdTxt());
            comp = competicionActiva(eq.getClub().getGrupo(), tipo);            
            if (comp != null) {                               
                proximoPartido = obtenerProximoPartido(comp, eq);
                if (proximoPartido != null)
                    break;
            }                
        }
        var datos = new ArrayList<Object>();        
        datos.add(proximoPartido);
        datos.add(comp);
        
        return datos;
        
    }

    static void renovarJugador(JugadorFutbol8 jug, EquipoFutbol8 eq) throws DAOException {
        
         var coste = jug.renovar();
         grabarJugadorFutbol8(jug);
         grabarEquipoFutbol8(eq);
         var mov = new Movimiento(eq, Renovaciones, 
                 coste, 
                 jug.getNombre());
         grabarRegistro(mov);
         var txt = "Al jugador " + jug.getNombre() + " se le ha "
                 + " renovado el contrato por el " + eq.getNombre();
         darNoticia(eq.getClub().getGrupo(), txt);
    }
    
    static ArrayList<EntrenoJugadorFutbol8> jugadoresEntrenables(EquipoFutbol8 eq, String pos) throws DAOException {
        
         ArrayList<JugadorFutbol8> jugadores;
         if (pos.equals("Todas"))
             jugadores = eq.getJugadoresEntrenables();
         else 
             jugadores = eq.getJugadoresEntrenables(valueOf(pos));
         
         var juv = obtenerJuvenil(eq);
         eq.setJuvenil(juv);
         
         var jugsEntr= new ArrayList<EntrenoJugadorFutbol8>();
         for (var jug : jugadores) {
             var newEntr = new EntrenoJugadorFutbol8(jug);
             jugsEntr.add(newEntr);
         }
         if (juv != null && (pos.equals("Todas") || pos.equals(juv.getPosicion().name())))
             jugsEntr.add(new EntrenoJugadorFutbol8(juv));
         
         return jugsEntr;
        
    }
    
    static ArrayList<EntrenoJugadorFutbol8> entrenar(EquipoFutbol8 eq, PosicionElegidaFutbol8 pos) throws DAOException {
        
        String posEntr;
        
        if (pos.equals(Cualquiera)) posEntr = "Todas";
        else posEntr = pos.name();                
        
        return entrenar(eq, posEntr);
        
    }
    
    static ArrayList<EntrenoJugadorFutbol8> entrenar(EquipoFutbol8 eq, String pos) 
            throws DAOException {
        
        
        if (!eq.isPuedeEntrenar()) return new ArrayList<>();
        
        var jugsEntr = jugadoresEntrenables(eq, pos);
        
         if (pos != null && pos.equals("Todas")) pos = null;
        
        jugsEntr = entrenarJugadores(jugsEntr, pos);
                
        eq.setEntrenamiento(true);
        //decidimos si se marcha algun tecnico
        if (obtener(10)){
            var txt = "Un Tecnico del " + eq.getNombre() +
                    " ha decidido abandonar el equipo";
            darNoticia(eq.getClub().getGrupo(), txt);
            eq.setEqTecnico(eq.getEqTecnico() - 1);
        }
        
        grabarEquipoFutbol8(eq);
        
        return jugsEntr;
        
    }
    
    static ArrayList<EntrenoJugadorFutbol8> entrenarJugadores(
            ArrayList<EntrenoJugadorFutbol8> jugsEntr, String pos) throws DAOException{
        
        var jugsEntrenados = 
                new ArrayList<EntrenoJugadorFutbol8>();
        // Si la posicion es null pero tenemos mas de 6 jugadores
        // solo entrenamos a 6
        var numMax = 6;
        if (pos != null && jugsEntr.size() > numMax){
            var numJugs = jugsEntr.size();
            shuffle(jugsEntr);
            for (var i = numMax; i < numJugs; i++)
                jugsEntr.remove(0);            
        }
        
        
        for (var jug : jugsEntr) {     
            
            if (!jug.isPuedeEntrenar()) continue;
            if (pos != null && !jug.getPosicion().name().equals(pos)) continue;
        
            if (pos == null && !obtener(4)) continue;
            
            jug.entrenar(pos);
            if (jug.getJugador() != null)
                grabarJugadorFutbol8(jug.getJugador());
            else if (jug.getJuvenil() != null)
                grabarJuvenilFutbol8(jug.getJuvenil());
            
            jugsEntrenados.add(jug);
        }
        
        return jugsEntrenados;
        
    }
    
    
}
