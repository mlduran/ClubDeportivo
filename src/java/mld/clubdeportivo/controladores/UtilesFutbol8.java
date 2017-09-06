/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mld.clubdeportivo.controladores;

import java.util.*;
import javax.servlet.ServletContext;
import mld.clubdeportivo.base.*;
import mld.clubdeportivo.base.futbol8.*;
import mld.clubdeportivo.bd.DAOException;
import mld.clubdeportivo.bd.JDBCDAOClub;
import mld.clubdeportivo.bd.JDBCDAOMovimiento;
import mld.clubdeportivo.bd.JDBCDAONoticia;
import mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8;
import mld.clubdeportivo.utilidades.Calculos;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Miguel
 */
public class UtilesFutbol8 {
    
    private static Logger logger = 
            LogManager.getLogger(UtilesFutbol8.class);
    
    protected static JugadorFutbol8 crearJugadorfutbol8(Grupo grp, EquipoFutbol8 equipo,
             PosicionJugFutbol8 posicion, int val, boolean subasta) throws DAOException {

        JugadorFutbol8 newJug = null;
        
        if (equipo != null && (equipo.getJugadores().size() >= EquipoFutbol8.NUMERO_MAX_JUGADORES))
                throw new UnsupportedOperationException("Ya tienes " + EquipoFutbol8.NUMERO_MAX_JUGADORES + " jugadores");
        
        ArrayList<JugadorMaestroFutbol8> lista = JDBCDAOFutbol8.obtenerJugadoresMaestros(posicion);
        Collections.shuffle(lista);
        if (grp == null && equipo != null)
            grp = equipo.getClub().getGrupo();                

        JugadorMaestroFutbol8 jug = null;
        boolean existe;
        // comprobamos si ya lo tenemos en el grupo        
        for (JugadorMaestroFutbol8 jugMaestro : lista) {
            jug = jugMaestro;
            existe = JDBCDAOFutbol8.existeJugador(jug.getNombre(), grp);
            if (!existe)            
                break;
        }
        
        // Si en el maestro no tenemos jugadores lo creamos con id
        String nombre;
        while (jug == null){
            nombre = "jugador" + Calculos.valorAleatorio(1111, 9999);
            jug = new JugadorMaestroFutbol8(nombre, posicion);
            existe = JDBCDAOFutbol8.existeJugador(jug.getNombre(), grp);
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

            JDBCDAOFutbol8.grabarJugadorFutbol8(newJug);
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
       
        int npos = Calculos.valorAleatorio(PosicionJugFutbol8.values().length);
        PosicionJugFutbol8 posicion = PosicionJugFutbol8.values()[npos];
        int eqTecnico = equipo.getEqTecnico();
        int min = 30 + eqTecnico * 5;
        int val = min + Calculos.valorAleatorio(10);       
        
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
        ArrayList<Integer> vals = new ArrayList<Integer>();
        
        ArrayList posiciones = new ArrayList<PosicionJugFutbol8>();
        posiciones.add(PosicionJugFutbol8.Portero);
        posiciones.add(PosicionJugFutbol8.Portero);
        vals.addAll(Calculos.listaValoresAleatorios(2, 50, 10, 100));
        
        posiciones.add(PosicionJugFutbol8.Defensa);
        posiciones.add(PosicionJugFutbol8.Defensa);
        posiciones.add(PosicionJugFutbol8.Defensa);
        posiciones.add(PosicionJugFutbol8.Defensa);
        vals.addAll(Calculos.listaValoresAleatorios(4, 50, 10, 100));
        
        posiciones.add(PosicionJugFutbol8.Medio);
        posiciones.add(PosicionJugFutbol8.Medio);
        posiciones.add(PosicionJugFutbol8.Medio);
        posiciones.add(PosicionJugFutbol8.Medio);
        vals.addAll(Calculos.listaValoresAleatorios(4, 50, 10, 100));
        
        posiciones.add(PosicionJugFutbol8.Delantero);
        posiciones.add(PosicionJugFutbol8.Delantero);
        vals.addAll(Calculos.listaValoresAleatorios(2, 50, 10, 100));
        
        if (eq.isAutomatico()) {
            numJugs = 16;
            posiciones.add(PosicionJugFutbol8.Defensa);
            posiciones.add(PosicionJugFutbol8.Medio);
            posiciones.add(PosicionJugFutbol8.Delantero);
            posiciones.add(PosicionJugFutbol8.Delantero);
            vals.addAll(Calculos.listaValoresAleatorios(4, 50, 10, 100));
            
        } else {
            numJugs = 12;
        }

        for (int i = 0; i < numJugs; i++){
            crearJugadorfutbol8(eq,(PosicionJugFutbol8) posiciones.get(i),
                    vals.get(i));
        }

    }
    
    protected static  EquipoFutbol8 altaEquipoFutbol8(Club club, boolean auto)
             throws DAOException{

        EquipoFutbol8 eq = new EquipoFutbol8(club, auto, new ArrayList());

        JDBCDAOFutbol8.grabarEquipoFutbol8(eq);
        eq.setClub(club);

        altaJugadoresEquipoFutbol8(eq);
        crearEntrenadorfutbol8(eq, 5);
        Movimiento mov = new Movimiento(eq, ClaseMovimiento.CapitalInicial, 
                Equipo.PRESUPUESTO_INICIAL);
        JDBCDAOMovimiento.grabarRegistro(mov);

        return eq;

    }    
   

     protected static EquipoFutbol8 crearEquipoAutomatico(Grupo grp) throws DAOException {
        
        ArrayList lista = (ArrayList) JDBCDAOFutbol8.obtenerEquiposMaestros();
        Collections.shuffle(lista);

        ArrayList<String> listaClubs = new ArrayList();
        for (Club club : JDBCDAOClub.listaClubs()) {
            listaClubs.add(club.getNombre());
        }

        EquipoMaestro eq = null;
        // comprobamos si ya lo tenemos
        for (Object eqMaestro : lista) {
            eq = (EquipoMaestro) eqMaestro;
            if (!listaClubs.contains(eq.getNombre()))
                break;
        }

        if (eq == null)
            throw new UnsupportedOperationException("No hay equipos Futbol8 en el maestro");

        String nomEq = eq.getNombre();
        if (nomEq.length() > 20) nomEq = nomEq.substring(1, 20);
        int contr = Calculos.valorAleatorio(100000, 999999);
        
        Club club = new Club(grp, nomEq, "Automatico", String.valueOf(contr), "");
        club.setAuto(true);
        JDBCDAOClub.grabarClub(club);

        return altaEquipoFutbol8(club, true);

    }
     
     protected static  void crearEntrenadorfutbol8(Grupo grp, EquipoFutbol8 equipo,
            int numTacts) throws DAOException {


        ArrayList lista = JDBCDAOFutbol8.obtenerEntrenadoresMaestros();
        Collections.shuffle(lista);
        ArrayList<EntrenadorFutbol8> entrenadoresExistentes =
                JDBCDAOFutbol8.obtenerEntrenadoresGrupo(grp);
        ArrayList<String> listaGrupo = new ArrayList();
        for (EntrenadorFutbol8 entrGrp : entrenadoresExistentes) {
            listaGrupo.add(entrGrp.getNombre());
        }

        EntrenadorMaestroFutbol8 entr = null;

        // comprobamos si ya lo tenemos en el grupo
        for (Object entrMaestro : lista) {

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
                ArrayList<EntrenadorFutbol8> libres = 
                        JDBCDAOFutbol8.obtenerEntrenadoresLibresGrupo(grp);
                if (libres.size() >= Entrenador.NUM_MAX_ENTRENADORES_LIBRES){
                    int x = Calculos.valorAleatorio(libres.size());
                    EntrenadorFutbol8 entrElim = libres.get(x);
                    if (entrElim != null)
                        JDBCDAOFutbol8.eliminarEntrenadorFutbol8(entrElim);
                    else
                        throw new NullPointerException("El entrenador es nulo");
                }
                newEntr = new EntrenadorFutbol8(grp, entr.getNombre());
            }
            else{
                newEntr = new EntrenadorFutbol8(equipo, entr.getNombre());
                newEntr.setGrupo(grp);
            }
            
            newEntr.setFicha(Entrenador.FICHA_INICIAL);            

            ArrayList<TacticaFutbol8> tacticas =
                    (ArrayList<TacticaFutbol8>) TacticaFutbol8.tacticasFutbol8();
            
            newEntr.setTacticas(new ArrayList<TacticaFutbol8>());           

            int[] codTacts = Calculos.valoresAleatoriosSinRepetir(numTacts, 0, tacticas.size() - 1);
            for (int i = 0; i < numTacts; i++){
                newEntr.anyadirTactica(tacticas.get(codTacts[i]));
            }

            try{
            JDBCDAOFutbol8.grabarEntrenadorFutbol8(newEntr);
            }catch (Exception ex){
                logger.error("Error al dar de alta entrenador " + ex.getMessage());
            }
            if (equipo != null)
                equipo.setEntrenador(newEntr);
        }

    }
     
     protected static void crearEntrenadorfutbol8(EquipoFutbol8 equipo,
             int numTacts) throws DAOException {
        
        Grupo grp = equipo.getClub().getGrupo();
        crearEntrenadorfutbol8(grp, equipo, numTacts);

    }
     
     protected static void crearEntrenadorfutbol8(Grupo grp,
             int numTacts) throws DAOException {
        
         if (JDBCDAOFutbol8.obtenerNumeroEntrenadoresGrupo(grp) < 100)
             crearEntrenadorfutbol8(grp, null, numTacts);

    }
     
     protected static ArrayList<String> posicionesJugador(){
         
         ArrayList<String> lista = new ArrayList<String>();
         for (PosicionJugFutbol8 pos : PosicionJugFutbol8.values()) {
             lista.add(pos.name());
         }
         
         return lista;
         
     }
     
     protected static ArrayList<String> posicionesJuvenil(){
         
         ArrayList<String> lista = new ArrayList<String>();
         for (PosicionElegidaFutbol8 pos : PosicionElegidaFutbol8.values()) {
             lista.add(pos.name());
         }
         
         return lista;
         
     }
     
     protected static void darNoticia(Grupo grp, String txt) throws DAOException{
         Noticia noticia = new Noticia(grp, Deporte.Futbol8, txt, false);
         JDBCDAONoticia.grabarNoticia(noticia);
     }
     
     protected static void darNoticia(Grupo grp, String txt, boolean isgeneral) throws DAOException{
         Noticia noticia = new Noticia(grp, Deporte.Futbol8, txt, isgeneral);
         JDBCDAONoticia.grabarNoticia(noticia);
     }
     
     protected static List<JugadorFutbol8> obtenerGoleadores(Grupo grp) throws DAOException {
        
       ArrayList<JugadorFutbol8> lista = JDBCDAOFutbol8.obtenerJugadoresGrupo(grp);
       
       ArrayList<JugadorFutbol8> jugadores = new ArrayList<JugadorFutbol8>();
       for (JugadorFutbol8 jug : lista){ 
           if (jug.getPosicion().equals(PosicionJugFutbol8.Portero)) continue;
           if (jug.getGolesLiga() > 0 || jug.getGolesCopa() > 0 ) 
               jugadores.add(jug);
       }
       
       int max = 50;
        
       Collections.sort(jugadores, new JugadorFutbol8.GolesComparator());
       
       if (jugadores.size() < max) max = jugadores.size();
        
       return jugadores.subList(0, max);
        
   }
     
     protected static List<JugadorFutbol8> obtenerPorteros(Grupo grp) throws DAOException {
        
       ArrayList<JugadorFutbol8> lista = JDBCDAOFutbol8.obtenerJugadoresGrupo(grp);
       
       ArrayList<JugadorFutbol8> jugadores = new ArrayList<JugadorFutbol8>();
       for (JugadorFutbol8 jug : lista){ 
           if (jug.getPosicion().equals(PosicionJugFutbol8.Portero) && jug.getEquipo() != null)
               jugadores.add(jug);
       }
       
       int max = 50;
        
       Collections.sort(jugadores, new JugadorFutbol8.RatioPorteroComparator());
       
       if (jugadores.size() < max) max = jugadores.size();
        
       return jugadores.subList(0, max);
        
   }
     
     protected static ArrayList<String> tipoProximaCompeticion(ServletContext appManager){
       
       ArrayList<String> result = new ArrayList<String>();
       int horaLanzamientoJornada = 13;
       
       //ServletContext appManager = this.getServletContext();

       String confDiasLiga = appManager.getInitParameter("diasligafutbol8");
       String confDiasCopa = appManager.getInitParameter("diascopafutbol8");
       
       logger.info("confDiasLiga " + confDiasLiga);
       logger.info("confDiasCopa " + confDiasCopa);

       Calendar calendario = Calendar.getInstance();
       calendario.setTime(new Date());

       int diaAct = calendario.get(Calendar.DAY_OF_WEEK);
       logger.info("diaAct " + diaAct);
       
       if (calendario.get(Calendar.HOUR_OF_DAY) > horaLanzamientoJornada - 1) diaAct++;
       logger.info("diaAct " + diaAct);
       
       for (int i = diaAct; i != diaAct - 1; i++){
           String dia = String.valueOf(i);
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
         
        ArrayList<String> tipoComp = tipoProximaCompeticion(appManager);
        
        logger.info("Tipos competicion " );         
        logger.info(tipoComp );
        
        CompeticionFutbol8 comp = null;
        PartidoFutbol8 proximoPartido = null;
                
        for (String tipo : tipoComp) {
            logger.info("Buscando competiciones tipo " + tipo);
            logger.info("En grupo " + eq.getClub().getGrupo().getIdTxt());
            comp = JDBCDAOFutbol8.competicionActiva(eq.getClub().getGrupo(), tipo);            
            if (comp != null) {                               
                proximoPartido = JDBCDAOFutbol8.obtenerProximoPartido(comp, eq);
                if (proximoPartido != null)
                    break;
            }                
        } 
        
        ArrayList<Object> datos = new ArrayList<Object>();        
        datos.add(proximoPartido);
        datos.add(comp);
        
        return datos;
        
    }

    static void renovarJugador(JugadorFutbol8 jug, EquipoFutbol8 eq) throws DAOException {
        
         int coste = jug.renovar();                     
         JDBCDAOFutbol8.grabarJugadorFutbol8(jug);
         JDBCDAOFutbol8.grabarEquipoFutbol8(eq);
         Movimiento mov = new Movimiento(eq, 
                 ClaseMovimiento.Renovaciones, 
                 coste, 
                 jug.getNombre());
         JDBCDAOMovimiento.grabarRegistro(mov);
         String txt = "Al jugador " + jug.getNombre() + " se le ha "
                 + " renovado el contrato por el " + eq.getNombre();
         UtilesFutbol8.darNoticia(eq.getClub().getGrupo(), txt);
    }
    
    static ArrayList<EntrenoJugadorFutbol8> jugadoresEntrenables(EquipoFutbol8 eq, String pos) throws DAOException {
        
         ArrayList<JugadorFutbol8> jugadores;
         if (pos.equals("Todas"))
             jugadores = eq.getJugadoresEntrenables();
         else 
             jugadores = eq.getJugadoresEntrenables(PosicionJugFutbol8.valueOf(pos));
         
         JuvenilFutbol8 juv = JDBCDAOFutbol8.obtenerJuvenil(eq);
         eq.setJuvenil(juv);
         
         ArrayList<EntrenoJugadorFutbol8> jugsEntr= new ArrayList<EntrenoJugadorFutbol8>();
         for (JugadorFutbol8 jug : jugadores) {
             EntrenoJugadorFutbol8 newEntr = new EntrenoJugadorFutbol8(jug);
             jugsEntr.add(newEntr);
         }
         if (juv != null && (pos.equals("Todas") || pos.equals(juv.getPosicion().name())))
             jugsEntr.add(new EntrenoJugadorFutbol8(juv));
         
         return jugsEntr;
        
    }
    
    static ArrayList<EntrenoJugadorFutbol8> entrenar(EquipoFutbol8 eq, PosicionElegidaFutbol8 pos) throws DAOException {
        
        String posEntr;
        
        if (pos.equals(PosicionElegidaFutbol8.Cualquiera)) posEntr = "Todas";
        else posEntr = pos.name();                
        
        return entrenar(eq, posEntr);
        
    }
    
    static ArrayList<EntrenoJugadorFutbol8> entrenar(EquipoFutbol8 eq, String pos) 
            throws DAOException {
        
        
        if (!eq.isPuedeEntrenar()) return new ArrayList<EntrenoJugadorFutbol8>();
        
        ArrayList<EntrenoJugadorFutbol8> jugsEntr = jugadoresEntrenables(eq, pos);
        
         if (pos != null && pos.equals("Todas")) pos = null;
        
        jugsEntr = entrenarJugadores(jugsEntr, pos);
                
        eq.setEntrenamiento(true);
        //decidimos si se marcha algun tecnico
        if (Calculos.obtener(10)){
            String txt = "Un Tecnico del " + eq.getNombre() +
                    " ha decidido abandonar el equipo";
            UtilesFutbol8.darNoticia(eq.getClub().getGrupo(), txt);
            eq.setEqTecnico(eq.getEqTecnico() - 1);
        }
        
        JDBCDAOFutbol8.grabarEquipoFutbol8(eq);
        
        return jugsEntr;
        
    }
    
    static ArrayList<EntrenoJugadorFutbol8> entrenarJugadores(
            ArrayList<EntrenoJugadorFutbol8> jugsEntr, String pos) throws DAOException{
        
        ArrayList<EntrenoJugadorFutbol8> jugsEntrenados = 
                new ArrayList<EntrenoJugadorFutbol8>();
        
        // Si la posicion es null pero tenemos mas de 6 jugadores
        // solo entrenamos a 6
        int numMax = 6;
        if (pos != null && jugsEntr.size() > numMax){
            int numJugs = jugsEntr.size();
            Collections.shuffle(jugsEntr);
            for (int i = numMax; i < numJugs; i++)
                jugsEntr.remove(0);            
        }
        
        
        for (EntrenoJugadorFutbol8 jug : jugsEntr) {     
            
            if (!jug.isPuedeEntrenar()) continue;
            if (pos != null && !jug.getPosicion().name().equals(pos)) continue;
        
            if (pos == null && !Calculos.obtener(4)) continue;
            
            jug.entrenar(pos);
            if (jug.getJugador() != null)
                JDBCDAOFutbol8.grabarJugadorFutbol8(jug.getJugador());
            else if (jug.getJuvenil() != null)
                JDBCDAOFutbol8.grabarJuvenilFutbol8(jug.getJuvenil());
            
            jugsEntrenados.add(jug);
        }
        
        return jugsEntrenados;
        
    }
    
    
}
