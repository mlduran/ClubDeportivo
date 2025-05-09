/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mld.clubdeportivo.controladores;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.String.valueOf;
import java.util.*;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;
import mld.clubdeportivo.base.*;
import mld.clubdeportivo.base.futbol8.*;
import mld.clubdeportivo.bd.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.util.logging.Level.INFO;
import static java.util.logging.Logger.getLogger;
import static mld.clubdeportivo.base.ClaseMovimiento.DevolucionCredito;
import static mld.clubdeportivo.base.ClaseMovimiento.DevolucionHacienda;
import static mld.clubdeportivo.base.ClaseMovimiento.Fichajes;
import static mld.clubdeportivo.base.ClaseMovimiento.Fichas;
import static mld.clubdeportivo.base.ClaseMovimiento.Gestion;
import static mld.clubdeportivo.base.ClaseMovimiento.IBI;
import static mld.clubdeportivo.base.ClaseMovimiento.IngresoPublicidad;
import static mld.clubdeportivo.base.ClaseMovimiento.IngresoTaquilla;
import static mld.clubdeportivo.base.ClaseMovimiento.Mantenimiento;
import static mld.clubdeportivo.base.ClaseMovimiento.PagoHacienda;
import static mld.clubdeportivo.base.ClaseMovimiento.PremioCompeticion;
import static mld.clubdeportivo.base.ClaseMovimiento.RegularizacionNegativa;
import static mld.clubdeportivo.base.ClaseMovimiento.RegularizacionPositiva;
import static mld.clubdeportivo.base.ClaseMovimiento.VentaJugadores;
import static mld.clubdeportivo.base.ConfigEconomia.MAX_DIAS_GESTION;
import static mld.clubdeportivo.base.Deporte.Futbol8;
import static mld.clubdeportivo.base.Jugador.NUMERO_MAX_JUGADORES_SUBASTA;
import static mld.clubdeportivo.base.futbol8.CompeticionFutbol8.crearCopa;
import static mld.clubdeportivo.base.futbol8.CompeticionFutbol8.crearLiga;
import static mld.clubdeportivo.base.futbol8.EquipoFutbol8.clasificarEquipos;
import static mld.clubdeportivo.base.futbol8.EquipoFutbol8.equiposAuto;
import static mld.clubdeportivo.base.futbol8.EquipoFutbol8.equiposAutoAEliminar;
import static mld.clubdeportivo.base.futbol8.EquipoFutbol8.equiposNoAuto;
import static mld.clubdeportivo.base.futbol8.JugadorFutbol8.clausulaMedia;
import static mld.clubdeportivo.base.futbol8.PartidoFutbol8.NUMERO_SIMULACIONES;
import static mld.clubdeportivo.base.futbol8.PosicionElegidaFutbol8.Cualquiera;
import static mld.clubdeportivo.bd.JDBCDAOBolsa.grabarBolsa;
import static mld.clubdeportivo.bd.JDBCDAOClub.grabarClub;
import static mld.clubdeportivo.bd.JDBCDAOClub.mailsClubs;
import static mld.clubdeportivo.bd.JDBCDAOGrupo.obtenerGruposActivos;
import static mld.clubdeportivo.bd.JDBCDAOMovimiento.grabarRegistro;
import static mld.clubdeportivo.bd.JDBCDAOMovimiento.obtenerMovimientos;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.competicionActiva;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.eliminarEquipoFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.eliminarJugadorFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.grabarAlineacionFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.grabarCompeticionFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.grabarConfigEconomiaFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.grabarCronicaFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.grabarDatosCompeticion;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.grabarEntrenadorFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.grabarEquipoFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.grabarEstadisticaPartidoFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.grabarGoleadorFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.grabarJornadaFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.grabarJugadorFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.grabarJuvenilFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.grabarPartidoFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.grabarPorteroFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.grabarPuntuacionFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.listaEquiposFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerAlineacionPartido;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerConfig;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerDatosVacacionesFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerFechaUltimaJornada;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerJornada;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerJornadaParaDisputar;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerJornadaSimple;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerJugadores;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerJugadoresGrupo;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerJugadoresLibresGrupo;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerJuvenil;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerSimpleEquipoFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.valoracionMediaJugadores;
import static mld.clubdeportivo.controladores.UtilesFutbol8.crearEntrenadorfutbol8;
import static mld.clubdeportivo.controladores.UtilesFutbol8.crearEquipoAutomatico;
import static mld.clubdeportivo.controladores.UtilesFutbol8.darNoticia;
import static mld.clubdeportivo.controladores.UtilesFutbol8.datosProximoPartido;
import static mld.clubdeportivo.controladores.UtilesFutbol8.entrenar;
import static mld.clubdeportivo.controladores.UtilesFutbol8.obtenerGoleadores;
import static mld.clubdeportivo.controladores.UtilesFutbol8.obtenerPorteros;
import static mld.clubdeportivo.controladores.UtilesFutbol8.renovarJugador;
import static mld.clubdeportivo.utilidades.Calculos.obtener;
import static mld.clubdeportivo.utilidades.Calculos.valorAleatorio;
import static mld.clubdeportivo.utilidades.Correo.getCorreo;
import static mld.clubdeportivo.utilidades.UtilGenericas.isDomingo;
import static mld.clubdeportivo.utilidades.UtilGenericas.isLunes;

/**
 *
 * @author mlopezd
 */
public class LanzarJornadaFutbol8 {
    
    private static Logger logger = LoggerFactory.getLogger(LanzarJornadaFutbol8.class.getName());

   
    private ServletContext aplicacion;
    private CompeticionFutbol8 compActual;
    private ConfigEconomiaFutbol8 confEconomica;
    
    protected void lanzarJornadaFutbol8(HttpServletRequest req,
            HttpServletResponse resp, ServletContext app) throws DAOException {
        

        aplicacion = app;
        
        var txtCorreo = "";
        
        
        if (!comprobarLanzamiento()) return;
        
                
        for (var grp : obtenerGruposActivos()) {

            confEconomica = obtenerConfig(grp);
            compActual = null;
            var claseComp = "";
            if (isDiaCompeticionFutbol8("Liga")) claseComp = "Liga";
            else if (isDiaCompeticionFutbol8("Copa")) claseComp = "Copa";

            var compAct = 
                    competicionActiva(grp, claseComp);
            
            // Si no hay copa hacemos partido de liga
            if (isDiaCompeticionFutbol8("Copa") && compAct == null){
                claseComp = "Liga";
                compAct = competicionActiva(grp, claseComp);
            }
            
            var equipos =
                listaEquiposFutbol8(grp);
            var eqs = new ArrayList<EquipoFutbol8>();
            for (var eq : equipos) 
                eqs.add(obtenerSimpleEquipoFutbol8(eq.getId()));
            

            if (compAct != null){
               
                compActual = compAct;

                var jor =
                        obtenerJornadaParaDisputar(compAct, eqs);
                

                if (claseComp.equals("Liga")){
                    
                    txtCorreo = disputarJornada(jor, eqs, claseComp);

                    // si el final de primera vuelta creamos la copa
                    if (jor.getNumero() == eqs.size() - 1){
                        var comp = crearCopa(grp, eqs);
                        //tratarPerdidaPatrocinadores(grp, eqs);                        
                        grabarDatosCompeticion(comp, eqs);
                        enviarMailNuevaCompeticion(comp);
                    }
                    // si es la ultima jornada finalizamos competicion
                    else if (jor.getNumero() == (eqs.size() - 1) * 2)
                        tratarFinalicionLiga(compAct, grp, eqs);
                        
                }
                else if (claseComp.equals("Copa")){
                    
                    txtCorreo = disputarJornada(jor, eqs, claseComp);
                    
                    // Si es segunda ronda
                    if ((jor.getNumero() % 2) == 0){
                        
                        var jorIda = 
                                obtenerJornada(jor.getNumero() - 1, compAct);
                        compAct.setJornadas(new ArrayList<>());
                        for (var i = 1; i < 3; i++){
                            var jorProx =
                                    obtenerJornadaSimple(jor.getNumero() + i, compAct);
                            if (jorProx != null)
                                compAct.getJornadas().add(jorProx);
                                    
                        }
                        
                        var eqsClasif = 
                                clasificarEquiposCopa(jorIda, jor);
                        
                        compAct.crearSigienteRondaDeCopa(eqsClasif, jor.getNumero() + 1);
                        
                        for (var jorSave : compAct.getJornadas()) 
                            for (var partSave : jorSave.getPartidos()){ 
                                grabarPartidoFutbol8(partSave);
                                for (var aliSave : partSave.getAlineaciones()) 
                                    grabarAlineacionFutbol8(aliSave);                                
                            }
                                                
                       
                    }
                    // si es final
                    else if (jor.getDescripcion().equals("Final")){
                        var partido = jor.getPartidos().get(0);
                        if (partido.getGanador() == null){
                            hacerTandaPenalties(partido);
                        }
                        tratarFinalicionCopa(compAct, grp, partido);                        
                    }                        
                    
                }
                
                jor.setFecha(new Date());
                grabarJornadaFutbol8(jor);
                tratarEquiposVacaciones(eqs);
                tratarConfigFutbol8(grp);
                
                enviarMailJornada(txtCorreo, compAct);                

            }
            else if(isDomingo()){
                crearCompeticion(grp);                
            }
        }
        
    }
    
    private static void hacerTandaPenalties(PartidoFutbol8 partido) throws DAOException{
        
        partido.getCronica().clear();
        partido.hacerTandaPenalties();
        grabarPartidoFutbol8(partido);
        grabarEstadisticaPartidoFutbol8(
                partido.getEstadistica());
        var cronica = partido.getCronica();
        for (var cronicaFutbol8 : cronica) 
            grabarCronicaFutbol8(cronicaFutbol8); 
        
    }
    
    public static void crearCompeticiones() throws DAOException{
        
        for (var grp : obtenerGruposActivos()) {
                crearCompeticion(grp); 
        }
        
    }
    
    
    public static void crearCompeticion(Grupo grp)
            throws DAOException{
        
        var compAct = competicionActiva(grp, "Liga");   
        
        if (compAct != null) return;
        
        var equipos =
                listaEquiposFutbol8(grp, false);
        // Eliminamos los que no han accedido ultimamente
        var equiposAEliminar = new ArrayList<EquipoFutbol8>();
        for (var eq : equipos) {
            obtenerJugadores(eq);
            if (eq.isAbandonado()) equiposAEliminar.add(eq);
        }
        for (var eq : equiposAEliminar) {
            equipos.remove(eq);
            eliminarEquipoFutbol8(eq);            
        }
        // Si no hay ningun equipo no automatico salimos
        var todosAuto = true;
        for (var eq : equipos) {
            if (!eq.isAutomatico()){
                todosAuto = false;
                break;
            }
        }
        if (todosAuto) return;
        
        var eqsElim = equiposAutoAEliminar(equipos);
        for (var eq : eqsElim) {
            equipos.remove(eq);
            eliminarEquipoFutbol8(eq);
        }
       
                
        if (!equipos.isEmpty()){ 
        
            var eqs = new ArrayList<EquipoFutbol8>();
            for (var eq : equipos) 
                eqs.add(obtenerSimpleEquipoFutbol8(eq.getId()));
            if (eqs.size() < 16){
                var eqsAuto = 0;
                if (eqs.size() < 4) eqsAuto = 4 - eqs.size();
                else if (eqs.size() % 2 != 0) eqsAuto = 1;
                while (eqs.size() + eqsAuto > 16) eqsAuto--;
                for (var i = 1; i <= eqsAuto; i++ ){
                    var eq = crearEquipoAutomatico(grp);
                    eqs.add(eq);
                }
            }else if (eqs.size() > 16){
                // los ultimos no entran en la liga
                while (eqs.size() > 16){
                    eqs.remove(eqs.size() - 1);
                }
            }
            var comp = crearLiga(grp, eqs);
            regularizarCuentas(eqs);
            grabarDatosCompeticion(comp, eqs);
            
            /* Si no existe la configuracion economica 
            para el grupola creamos            
            */
            if (obtenerConfig(grp) == null){
                var conf = new ConfigEconomiaFutbol8(grp);
                grabarConfigEconomiaFutbol8(conf);
            }            
            
            for (var eq : eqs){
                eq.setActivo(true);
                grabarEquipoFutbol8(eq);
            }                 
            crearJugadorSubasta(grp);            
            enviarMailNuevaCompeticion(comp);
        }        
        
    }
    
    private String disputarJornada(JornadaFutbol8 jor, ArrayList<EquipoFutbol8> eqs,
            String tipo) throws DAOException{
        
        logger.info("Inicio pre Jornada");
        var eqsQueJuegan =
                        new ArrayList<EquipoFutbol8>();
        var txtCorreo = new StringBuilder();
        txtCorreo.append("RESULTADOS JORNADA ");
        txtCorreo.append(jor.getDescripcion()).append("<br/><br/>");
        txtCorreo.append("<table>");
        
        for (var partido : jor.getPartidos()) { 
         
            partido.setTipo(tipo);
            var eqLocal = (EquipoFutbol8) partido.getEqLocal();
            var eqVisit = (EquipoFutbol8) partido.getEqVisitante();
            
            eqLocal.completarAlineacion();
            eqVisit.completarAlineacion();
            
            var est = new EstadisticaPartidoFutbol8(partido);
            partido.setEstadistica(est);
            logger.info("Pre partido");
            
            // calculo de posibilidades
            partido.setIsSimulacion(true);
            int victoriasLocal = 0, victoriasVisit = 0;
            for (var i = 1; i <= NUMERO_SIMULACIONES; i++){                
                partido.jugarPartido();
                if (partido.getGanador() != null){
                    if (partido.getGanador().equals(eqLocal)) victoriasLocal++;
                    else if (partido.getGanador().equals(eqVisit)) victoriasVisit++;                    
                }
            } 
            est.setVictoriasLocal(victoriasLocal * 100 / NUMERO_SIMULACIONES);
            est.setVictoriasVisitante(victoriasVisit * 100 / NUMERO_SIMULACIONES);
            
            // partido real
            partido.setIsSimulacion(false);
            est = partido.jugarPartido();
            
            logger.info("Post Partido");
           
            eqsQueJuegan.add(eqLocal);
            eqsQueJuegan.add(eqVisit);
            grabarPartidoFutbol8(partido);
            var cronica = partido.getCronica();
            for (var cronicaFutbol8 : cronica) 
                grabarCronicaFutbol8(cronicaFutbol8);   
            
          
            grabarEstadisticaPartidoFutbol8(est);
            var comp = (CompeticionFutbol8) jor.getCompeticion();
            if (comp.getClase().equals("Liga")){
                partido.asignarPuntos();
                grabarPuntuacionFutbol8(eqLocal.getPuntuacion());
                grabarPuntuacionFutbol8(eqVisit.getPuntuacion());
                if (partido.getGanador() == eqVisit)
                    tratarPerdidaPatrocinador(eqLocal);
                grabarClub(eqLocal.getClub());
                grabarClub(eqVisit.getClub());
            }
            gestionEstadio(partido);
            eqLocal.limpiarJugadoresQueJuegan();
            eqVisit.limpiarJugadoresQueJuegan();
            for (var jug : eqLocal.getJugadores()) {                
                grabarJugadorFutbol8(jug); 
            }
            for (var jug : eqVisit.getJugadores()) {                
                grabarJugadorFutbol8(jug); 
            }
            
            grabarEntrenadorFutbol8(eqLocal.getEntrenador());
            grabarEntrenadorFutbol8(eqVisit.getEntrenador());
            txtCorreo.append("<tr>");
            txtCorreo.append("<td>");
            txtCorreo.append(eqLocal.getNombre()).append(" - ").append(eqVisit.getNombre());
            txtCorreo.append("</td>");
            txtCorreo.append("<td>");
            txtCorreo.append(partido.getGolesLocal()).append(" - ").append(partido.getGolesVisitante());
            txtCorreo.append("</td>");
            txtCorreo.append("</tr>");
            
        }
        
        txtCorreo.append("</table>");
        
        var eqsNoAuto = 
                equiposNoAuto(eqs);
        var eqsNoAutoQueJuegan = 
                equiposNoAuto(eqsQueJuegan);
        var eqsAuto = 
                equiposAuto(eqs);
        gestionJornadaFutbol8(jor.getCompeticion().getGrupo(), 
                eqsNoAuto, eqsNoAutoQueJuegan, eqsAuto);
        logger.info("Fin Jornada");
        
        return txtCorreo.toString();
        
        
    }

    private boolean isDiaCompeticionFutbol8(String clase) {
        // tipo es Liga, Copa etc

        var result = false;
        var confDiasLiga = aplicacion.getInitParameter("diasligafutbol8");
        var confDiasCopa = aplicacion.getInitParameter("diascopafutbol8");
        var diasLiga = confDiasLiga.split(",");
        var diasCopa = confDiasCopa.split(",");
        var calendario = getInstance();
        calendario.setTime(new Date());

        var diaAct = valueOf(calendario.get(DAY_OF_WEEK));
        for (var dia : diasLiga) {
            if (dia.equals(diaAct) && clase.equals("Liga")) result = true;
        }
        for (var dia : diasCopa) {
            if (dia.equals(diaAct) && clase.equals("Copa")) result = true;
        }
        return result;

    }
    
    private void gestionJornadaFutbol8(Grupo grp, ArrayList<EquipoFutbol8> eqs,
            ArrayList<EquipoFutbol8> eqsQueJuegan, ArrayList<EquipoFutbol8> eqsAuto)
            throws DAOException {
    
        logger.info("gestionJornadaFutbol8");        
        
        var numTacs = valorAleatorio(5, 10);
        crearEntrenadorfutbol8(grp, numTacs);
        gestionGeneralParaTodosLosEquipos(grp, eqs);
        gestionGeneralParaLosEquiposQueJuegan(grp, eqsQueJuegan);    
        gestionGeneralParaLosEquiposAuto(grp, eqsAuto);

        for (var eq : eqs) {
            grabarEquipoFutbol8(eq);
            grabarEntrenadorFutbol8(eq.getEntrenador());
            for (var jug : eq.getJugadores()) {
                grabarJugadorFutbol8(jug);
            }            
        }
        for (var eq : eqsAuto) {
            grabarEquipoFutbol8(eq);
            for (var jug : eq.getJugadores()) {
                grabarJugadorFutbol8(jug);
            }            
        }    
        
        gestionarJugadoresSubasta(grp, eqs,eqsAuto);

    }
    
    private void gestionarJuveniles(ArrayList<EquipoFutbol8> eqs) throws DAOException{
        logger.info("gestionarJuveniles");
        

        for (var eq : eqs) {
            var juv = obtenerJuvenil(eq);
            if (juv == null){
                var posEq = eq.getPosicionJuvenil();
                int x;
                if (posEq == Cualquiera)
                    x = valorAleatorio(PosicionJugFutbol8.values().length);
                else 
                    x = posEq.ordinal();
                    
                var pos = PosicionJugFutbol8.values()[x];
                juv = new JuvenilFutbol8(eq, pos);                
            }                    
            else{
                juv.bajarValoracion();
                juv.setJornadas(juv.getJornadas() + 1);
            }
            grabarJuvenilFutbol8(juv);
        }        
    }
    
    private void gestionarJugadoresSubasta(Grupo grp, 
            ArrayList<EquipoFutbol8> eqs, ArrayList<EquipoFutbol8> eqsAuto) 
            throws DAOException{
        // se gestiona solo los lunes
        logger.info("gestionarJugadoresSubasta");
        if (!isLunes()) return;
         
        var jugs = obtenerJugadoresGrupo(grp);
        var mediaClausula = clausulaMedia(jugs);
        Movimiento mov;
        for (var jug : jugs) {
            if (!jug.isEnSubasta()) continue;
            var eqVenta = jug.getEquipo();
            EquipoFutbol8 eqPuja = null;
            if (jug.getEquipoPuja() != 0)
                eqPuja = obtenerSimpleEquipoFutbol8(jug.getEquipoPuja());
            if (eqPuja != null){
                if (jug.getPuja() >= jug.getClausula()){                    
                    if (eqPuja.getJugadores().size() >= EquipoFutbol8.NUMERO_MAX_JUGADORES){
                        // si el equipo tiene mas de 16 jugadores pagamos el 25%
                        // y no asignamos el jugador
                        var compensacion = (int) (jug.getPuja() * 0.25);
                        eqPuja.setPresupuesto(eqPuja.getPresupuesto() - compensacion);
                        mov = new Movimiento(eqPuja, Fichajes, compensacion, "Gastos Gestion");
                        grabarRegistro(mov);
                        if (eqVenta != null){
                            eqVenta.setPresupuesto(eqVenta.getPresupuesto() + compensacion);
                            mov = new Movimiento(eqPuja, Fichajes, compensacion, "Compensación");
                            grabarRegistro(mov);
                        }
                        var txt = "El equipo " + eqPuja.getNombre() + " ha ganado la " +
                                "puja por el jugador " + jug.getNombre() + " por la cantidad de " + jug.getClausula() +
                                " pero no se ha podido realizar la incorporacion por tener la plantilla completa. Pierde el 25% de la " +
                                "cantidad ofertada en concepto de gastos de gestion";
                        darNoticia(grp, txt);
                        jug.setEquipoPuja(0);
                        jug.setPuja(jug.getClausula());   
                    }else{
                        var iva = jug.getPuja() * confEconomica.getIva() / 100;
                        var txt = "El equipo " + eqPuja.getNombre() + " ha ganado la " +
                                "puja por el jugador " + jug.getNombre() + " por la cantidad de " + jug.getPuja() +
                                " mas " + iva + " de iva";
                        darNoticia(grp, txt);
                        eqPuja.setPresupuesto(eqPuja.getPresupuesto() - jug.getPuja() - iva);
                        mov = new Movimiento(eqPuja, Fichajes, jug.getPuja() + iva, jug.getNombre());
                        grabarRegistro(mov);
                        grabarEquipoFutbol8(eqPuja);
                        if (eqVenta != null){
                            eqVenta.setPresupuesto(eqVenta.getPresupuesto() + jug.getPuja());
                            grabarEquipoFutbol8(eqVenta);
                            mov = new Movimiento(eqVenta, VentaJugadores, jug.getPuja(), jug.getNombre());
                            grabarRegistro(mov);
                        }
                        jug.setContrato(Jugador.JORNADAS_CONTRATO);
                        jug.setEnSubasta(false);
                        jug.setEquipo(eqPuja);
                        jug.setGrupo(grp);
                        // aumentamos un 40% la clausula 
                        jug.setClausula(jug.getPuja() + jug.getPuja() * 40 / 100);
                        jug.setEquipoPuja(0);
                        jug.setPuja(0); 
                        grabarJugadorFutbol8(jug);
                    }                    
                }
            }
            else{
                if (jug.getEquipo() != null){
                    // Equipo extranjero que puja
                    var posibilidad = max(2, jug.getClausula() / 2000);
                    if (obtener(posibilidad)){
                        // se paga el 50% y maximo de 10.000
                        var cantidad = min(10000, jug.getClausula() / 2);
                        var txt = "Debido a que ningun equipo del grupo ha pujado por " +
                                "el jugador " + jug.getNombre() + " un equipo internacional se lo ha llevado por " + cantidad;
                        darNoticia(grp, txt);
                        eqVenta.setPresupuesto(eqVenta.getPresupuesto() + cantidad);
                        grabarEquipoFutbol8(eqVenta);
                        mov = new Movimiento(eqVenta, VentaJugadores, cantidad, jug.getNombre());
                        grabarRegistro(mov);
                        eliminarJugadorFutbol8(jug);
                    }
                    else
                        jug.bajarSubasta(jug.getEquipo() == null);
                }
                else
                    jug.bajarSubasta(jug.getEquipo() == null);     
            }
        }
        // creamos un jugador nuevo si es lunes
        var numJugs = 0;
        for (var eq : eqs) 
            numJugs = numJugs + eq.getJugadores().size();
        for (var eq : eqsAuto) 
            numJugs = numJugs + eq.getJugadores().size();
        var jugLibres = 
                obtenerJugadoresLibresGrupo(grp);
            
        if (jugLibres.size() > NUMERO_MAX_JUGADORES_SUBASTA)
            eliminarJugadorFutbol8(jugLibres.get(0));
         
        var crear = numJugs < (eqs.size() + eqsAuto.size() + 1) * EquipoFutbol8.NUMERO_MAX_JUGADORES &&
                numJugs + jugLibres.size() < EquipoFutbol8.NUMERO_MAX_JUGADORES;
            
        if (crear){
            crearJugadorSubasta(grp);
            // si el grupo tiene mas de 8 equipos, creamos un segundo jugador
            if (eqs.size() > 8) crearJugadorSubasta(grp);
        }
        
 
    }
    
    private static void crearJugadorSubasta(Grupo grp) throws DAOException{
                 
        var pos = valorAleatorio(PosicionJugFutbol8.values().length);
        var val = valorAleatorio(10, 70);
        
        JugadorFutbol8 jug = null;            
        try{
            jug = UtilesFutbol8.crearJugadorSubasta(grp, PosicionJugFutbol8.values()[pos], val);
        }
        catch(Exception ex){}                    
        if (jug != null){
            var txt = "El jugador libre " + jug.getNombre() + " ha entrado en subasta";
            darNoticia(grp, txt);
        }   
        
    }

   

    public void gestionEstadio(PartidoFutbol8 partido) throws DAOException {

        logger.info("gestionEstadio");

        var eq = (EquipoFutbol8) partido.getEqLocal();
        
        if (eq.isAutomatico() || !eq.isJuegaEnCasa()) return;

        var ingreso = partido.getEspectadores() * partido.getPrecioEntradas();
        var hacienda = confEconomica.getRetencionHacienda(ingreso);
        eq.setPresupuesto(eq.getPresupuesto() + ingreso - hacienda);

        grabarEquipoFutbol8(eq);

        Movimiento mov;
        mov = new Movimiento(eq, IngresoTaquilla, ingreso);
        grabarRegistro(mov);
        mov = new Movimiento(eq, PagoHacienda, hacienda);
        grabarRegistro(mov);
        compActual.setRecaudacion(compActual.getRecaudacion() + hacienda);
        grabarCompeticionFutbol8(compActual);

    }

    private void gestionGeneralParaTodosLosEquipos(Grupo grp, 
            ArrayList<EquipoFutbol8> eqs) throws DAOException {
        
        logger.info("gestionGeneralParaTodosLosEquipos");
        var fluctuacion = obtenerFluctuacionBolsa(grp);
        for (var eq : eqs) {
            eq.reiniciarInversiones();
            eq.aplicarFluctuacion(fluctuacion);
            eq.aplicarInteresCredito(confEconomica.getInteresCredito()); 
            eq.setEntrenamiento(false); 
            if (eq.obtenerOjeado()){
                var txt = "Un ojeador ha captado un jugador para el " + eq.getNombre();
                darNoticia(eq.getClub().getGrupo(), txt);
                txt = "Un ojeador ha captado un jugador para tu equipo, puedes incorporarlo en la pantalla de Plantilla"; 
                getCorreo().enviarMail("ClubDeportivo Jugador Ojeado Futbol8", 
                        txt, true, eq.getClub().getMail());
            }
            if (eq.perderOjeador()){
                var txt = "Un ojeador ha abandonado el " + eq.getNombre();
                darNoticia(eq.getClub().getGrupo(), txt);
            }
            for (var jug : eq.getJugadores()) {
                jug.bajarValoracion();
                if (jug.getJornadasLesion() > 0)
                    jug.setJornadasLesion(jug.getJornadasLesion() - 1);
            }
            
        }
        gestionarJuveniles(eqs);        
        
    }
    
    private void gestionGeneralParaLosEquiposQueJuegan(Grupo grp, 
            ArrayList<EquipoFutbol8> eqs) throws DAOException {
        logger.info("gestionGeneralParaLosEquiposQueJuegan");
        Movimiento mov;
        int cantidad;
        for (var eq : eqs) {
            cantidad = eq.pagarFichas();
            mov = new Movimiento(eq, Fichas, cantidad);
            grabarRegistro(mov);
            cantidad = eq.ingresoPublicidad(confEconomica.getPorcentajeRetencionHacienda(cantidad));
            mov = new Movimiento(eq, IngresoPublicidad, cantidad);
            grabarRegistro(mov);
            var hacienda = confEconomica.getRetencionHacienda(cantidad);
            mov = new Movimiento(eq, PagoHacienda, hacienda);
            grabarRegistro(mov);
            compActual.setRecaudacion(compActual.getRecaudacion() + hacienda);
            grabarCompeticionFutbol8(compActual);
            cantidad = eq.pagarMantenimientoEstadio();
            if (cantidad > 0){
                mov = new Movimiento(eq, Mantenimiento, cantidad);
                grabarRegistro(mov);
            }
            cantidad = eq.pagarGestion();
            if (cantidad > 0){
                mov = new Movimiento(eq, Gestion, cantidad);
                grabarRegistro(mov);
            }           
            
            gestionDeContratos(eq);
        }
        
        
    }
    
    private void gestionGeneralParaLosEquiposAuto(Grupo grp, 
            ArrayList<EquipoFutbol8> eqs) throws DAOException {
        
        logger.info("gestionGeneralParaLosEquiposAuto");
        
        var valMedia = valoracionMediaJugadores(grp);
        int subidaBajada;
        int mediaEquipo;
        
               
         for (var eq : eqs) {
             mediaEquipo = eq.getValoracionMediaJugadores();
             if (mediaEquipo > valMedia) subidaBajada = -10;
             else subidaBajada = 10;
             
             for (var jug : eq.getJugadores()) {
                 if (obtener(2))
                     jug.setValoracion(jug.getValoracion() + subidaBajada);
                 if (jug.getJornadasLesion() > 0)
                    jug.setJornadasLesion(jug.getJornadasLesion() - 1);
             }
        }
        
    }
    
    private int obtenerFluctuacionBolsa(Grupo grp) throws DAOException{
        logger.info("obtenerFluctuacionBolsa");
        var tipoFluctuacion = obtener(2);
        int fluctuacion;
        if (!tipoFluctuacion)
            fluctuacion = valorAleatorio(confEconomica.getSubidaMaxBolsa());
        else{ 
            fluctuacion = -(valorAleatorio(confEconomica.getBajadaMaxBolsa()));
            if (confEconomica.isCrackBolsa() && obtener(500)){
                // hay un crack
                fluctuacion = fluctuacion * 4;
            }
        }
        
        grabarBolsa(new Bolsa(fluctuacion));
        String txt;
        if (fluctuacion < -(confEconomica.getBajadaMaxBolsa()))
            txt = "La bolsa ha sufrido un ckack y a tenido una fluctuacion del " + fluctuacion + "%";
        else
            txt = "La bolsa a tenido una fluctuacion del " + fluctuacion + "%"; 
        darNoticia(grp, txt);
        
        return fluctuacion;
        
    }

    
    private void gestionDeContratos(EquipoFutbol8 eq) throws DAOException {
        logger.info("gestionDeContratos");
        var jugs = eq.getJugadores();
        var jugsElim = new  ArrayList<JugadorFutbol8>();
        var txtJugs = "";
        for (var jug : jugs) {
            jug.setContrato(jug.getContrato() - 1);
            if (jug.getContrato() == 1) {
                txtJugs = txtJugs + jug.getNombre() + "<br/>";
            }
            else if (jug.getContrato() == 0)                
                jugsElim.add(jug);
        }
        if (!txtJugs.isEmpty()){
            var txt = "Jugadores con contrato a punto de expirar: <br/><br/>" + txtJugs +
                    "<br/>Si no lo renuevas se marcharán de tu club";
            getCorreo().enviarMail("ClubDeportivo Renovacion de Jugadores Futbol8", 
                    txt, true, eq.getClub().getMail());
        }
        for (var jug : jugsElim) {
            eq.getJugadores().remove(jug); 
            var txt = "Al jugador " + jug.getNombre() + " del " + 
                    eq.getNombre() + " se le ha acabado el contrato y no ha renovado";
            darNoticia(eq.getClub().getGrupo(), txt);
            eliminarJugadorFutbol8(jug);
        }
        var entr = eq.getEntrenador();
        entr.setContrato(entr.getContrato() - 1);
        if (entr.getContrato() == 1) {
            var txt = "El contrato de tu entrenador " + entr.getNombre() + " esta a punto de expirar, si quieres contratar otro entrenador, es ahora el momento, sino en la próxima jornada se renovará automaticamente por otras 40 jornadas. Recuerda que el cambio de entrenador resetea la moral a 100.";
            getCorreo().enviarMail("ClubDeportivo Fin Contrato Entrenador Futbol8", 
                    txt, true, eq.getClub().getMail());
        }
        else if (entr.getContrato() == 0) {
            entr.setContrato(Entrenador.JORNADAS_CONTRATO);
            entr.anyadirTacticaNuevoContrato();
            var txt = "Al entrenador " + entr.getNombre() + " del " + 
                    eq.getNombre() + " ha renovado su contrato automaticamente";
            darNoticia(eq.getClub().getGrupo(), txt);
        }

    }

    private ArrayList<EquipoFutbol8> clasificarEquiposCopa(
            JornadaFutbol8 jorIda, JornadaFutbol8 jorVuelta) throws DAOException {
        // Esta implementado el tema de los valor doble en los goles fuera
        // de casa
        
        var eqs = new ArrayList<EquipoFutbol8>();       
        
        int golesA, golesB;
        
        for (var partidoIda : jorIda.getPartidos()) 
            for (var partidoVuelta : jorVuelta.getPartidos()) 
                if (partidoIda.getEqLocal().equals(partidoVuelta.getEqVisitante())){
                    golesA = partidoIda.getGolesLocal() + partidoVuelta.getGolesVisitante();
                    golesB = partidoIda.getGolesVisitante() + partidoVuelta.getGolesLocal();
                    if (golesA > golesB)
                        eqs.add((EquipoFutbol8) partidoIda.getEqLocal());
                    else if (golesB > golesA)
                        eqs.add((EquipoFutbol8) partidoIda.getEqVisitante());
                    else{
                        golesA = partidoIda.getGolesLocal() + partidoVuelta.getGolesVisitante() * 2;
                        golesB = partidoIda.getGolesVisitante() * 2 + partidoVuelta.getGolesLocal();
                        if (golesA > golesB)
                            eqs.add((EquipoFutbol8) partidoIda.getEqLocal());
                        else if (golesB > golesA)
                            eqs.add((EquipoFutbol8) partidoIda.getEqVisitante());
                        else{
                            // Si persiste el empate con el valor doble fuera de casa hacemos penalties
                            golesA = partidoVuelta.getGolesLocal();
                            golesB = partidoVuelta.getGolesVisitante();
                            hacerTandaPenalties(partidoVuelta); 
                            if (partidoVuelta.getGolesLocal() - golesA > partidoVuelta.getGolesVisitante() - golesB)
                                eqs.add((EquipoFutbol8) partidoVuelta.getEqLocal());
                            else 
                                eqs.add((EquipoFutbol8) partidoVuelta.getEqVisitante()); 
                        }                        
                    }
                }
            
                
        return eqs;
        
    }

    private static void enviarMailNuevaCompeticion(CompeticionFutbol8 comp 
            ) throws DAOException {
        
        
        var correos = mailsClubs(comp.getGrupo(), Futbol8);
        var txt = "Se ha creado la competicion " + comp.getNombre();          

        getCorreo().enviarMailMasivo("ClubDeportivo Nueva competicion Futbol8", 
                txt, true, correos);
        
    }
    
    private static void enviarMailJornada(String txt, CompeticionFutbol8 comp 
            ) throws DAOException {
        
        
        var correos = mailsClubs(comp.getGrupo(), Futbol8);
 
        getCorreo().enviarMailMasivo("ClubDeportivo Jornada Futbol8", 
                txt, true, correos);
        
    }

    private int obtenerEspectativaSegunPosicion(int numEqs, int pos){
  
        if (numEqs == 4){
            if (pos == 1) return 2;
            if (pos == 2) return 3;
            if (pos == 3) return 5;
            if (pos == 4) return 6;
        }
        if (numEqs == 6){
            if (pos == 1) return 1;
            if (pos == 2) return 2;
            if (pos == 3) return 3;
            if (pos == 4) return 5;
            if (pos == 5) return 6;
            if (pos == 6) return 7;
        }
        if (numEqs == 8){
            if (pos == 1) return 0;
            if (pos == 2) return 1;
            if (pos == 3) return 2;
            if (pos == 4) return 3;
            if (pos == 5) return 5;
            if (pos == 6) return 6;
            if (pos == 7) return 7;
            if (pos == 8) return 8;
        }
        if (numEqs == 10){
            if (pos == 1) return 0;
            if (pos == 2) return 1;
            if (pos == 3) return 2;
            if (pos == 4) return 3;
            if (pos == 5) return 4;
            if (pos == 6) return 4;
            if (pos == 7) return 5;
            if (pos == 8) return 6;
            if (pos == 9) return 7;
            if (pos == 10) return 8;
        }
        if (numEqs == 12){
            if (pos == 1) return 0;
            if (pos == 2) return 1;
            if (pos == 3) return 2;
            if (pos == 4) return 3;
            if (pos == 5) return 3;
            if (pos == 6) return 4;
            if (pos == 7) return 4;
            if (pos == 8) return 5;
            if (pos == 9) return 5;
            if (pos == 10) return 6;
            if (pos == 11) return 7;
            if (pos == 12) return 8;
        }
        if (numEqs == 14){
            if (pos == 1) return 0;
            if (pos == 2) return 1;
            if (pos == 3) return 2;
            if (pos == 4) return 2;
            if (pos == 5) return 3;
            if (pos == 6) return 3;
            if (pos == 7) return 4;
            if (pos == 8) return 4;
            if (pos == 9) return 5;
            if (pos == 10) return 5;
            if (pos == 11) return 6;
            if (pos == 12) return 6;
            if (pos == 13) return 7;
            if (pos == 14) return 8;
        }
        if (numEqs == 16){
            if (pos == 1) return 0;
            if (pos == 2) return 1;
            if (pos == 3) return 1;
            if (pos == 4) return 2;
            if (pos == 5) return 2;
            if (pos == 6) return 3;
            if (pos == 7) return 3;
            if (pos == 8) return 4;
            if (pos == 9) return 4;
            if (pos == 10) return 5;
            if (pos == 11) return 5;
            if (pos == 12) return 6;
            if (pos == 13) return 6;
            if (pos == 14) return 7;
            if (pos == 15) return 7;
            if (pos == 16) return 8;
        }
        return 0;
 }
 
    private int obtenerPosicionSegunEspectativa(int numEqs, int esp){
 
        if (numEqs == 4){
            if (esp == 0) return 1;
            if (esp == 1) return 1;
            if (esp == 2) return 2;
            if (esp == 3) return 2;
            if (esp == 4) return 3;
            if (esp == 5) return 3;
            if (esp == 6) return 3;
            if (esp == 7) return 4;
            if (esp == 8) return 4;
        }
        if (numEqs == 6){
            if (esp == 0) return 1;
            if (esp == 1) return 2;
            if (esp == 2) return 2;
            if (esp == 3) return 3;
            if (esp == 4) return 3;
            if (esp == 5) return 4;
            if (esp == 6) return 4;
            if (esp == 7) return 5;
            if (esp == 8) return 6;
        }
        if (numEqs == 8){
            if (esp == 0) return 1;
            if (esp == 1) return 2;
            if (esp == 2) return 3;
            if (esp == 3) return 4;
            if (esp == 4) return 4;
            if (esp == 5) return 5;
            if (esp == 6) return 6;
            if (esp == 7) return 7;
            if (esp == 8) return 8;
        }
        if (numEqs == 10){
            if (esp == 0) return 1;
            if (esp == 1) return 2;
            if (esp == 2) return 3;
            if (esp == 3) return 4;
            if (esp == 4) return 5;
            if (esp == 5) return 7;
            if (esp == 6) return 8;
            if (esp == 7) return 9;
            if (esp == 8) return 10;
        }
        if (numEqs == 12){
            if (esp == 0) return 1;
            if (esp == 1) return 2;
            if (esp == 2) return 3;
            if (esp == 3) return 5;
            if (esp == 4) return 6;
            if (esp == 5) return 8;
            if (esp == 6) return 10;
            if (esp == 7) return 11;
            if (esp == 8) return 12;
        }
        if (numEqs == 14){
            if (esp == 0) return 1;
            if (esp == 1) return 2;
            if (esp == 2) return 4;
            if (esp == 3) return 6;
            if (esp == 4) return 7;
            if (esp == 5) return 9;
            if (esp == 6) return 11;
            if (esp == 7) return 13;
            if (esp == 8) return 14;
        }
        if (numEqs == 16){
            if (esp == 0) return 1;
            if (esp == 1) return 3;
            if (esp == 2) return 5;
            if (esp == 3) return 6;
            if (esp == 4) return 7;
            if (esp == 5) return 8;
            if (esp == 6) return 10;
            if (esp == 7) return 12;
            if (esp == 8) return 14;
        }
        return 0;
    }
    
    private void tratarPerdidaPatrocinadores(Grupo grp, ArrayList<EquipoFutbol8> eqs) throws DAOException {
       
        var eqsClasif = clasificarEquipos(eqs);
        var pos = 1;
        int espectativa, posSegunEspec;
        int result;
        for (EquipoFutbol8 eq : eqsClasif) {
            if (eq.isAutomatico()){
                pos++;
                continue;
            }
            espectativa = eq.getEspectativa().ordinal();
            // obtenemos la posicion que deberia tener para la espectativa actual
            posSegunEspec = obtenerPosicionSegunEspectativa(eqs.size(), espectativa);
            result = posSegunEspec - pos;
            if (result < 0) {
                result = eq.perderContratosPublicitarios(abs(result));
                if (result > 0) {
                    var txt = "El " + eq.getNombre() + " ha perdido " + result + " patrocinadore/s";
                    darNoticia(grp, txt);  
                }
            } else if (result > 0) {
                result = eq.ganarContratosPublicitarios(result);
                if (result > 0) {
                    var txt = "El " + eq.getNombre() + " ha conseguido " + result + " patrocinadore/s";
                    darNoticia(grp, txt);  
                }
            }
            // Modificamos espectativa segun nueva posicion
            // Poderamos la posicion al numero de espectativas
            var numNuevaEspectativa = obtenerEspectativaSegunPosicion(eqs.size(), pos);
            var nuevaEspectativa = EspectativaFutbol8.values()[numNuevaEspectativa];
            String txt;
            if (!eq.getEspectativa().equals(nuevaEspectativa)){
                txt = "El " + eq.getNombre() + " ha cambiado su espectativa de " + eq.getEspectativa().name() + " a " + nuevaEspectativa.name();
                eq.setEspectativa(nuevaEspectativa);
            }
            else
                txt = "El " + eq.getNombre() + " mantiene su espectativa a " + eq.getEspectativa().name();
            darNoticia(grp, txt);
            grabarEquipoFutbol8(eq);
            pos++;
        }
     
    }

    private void tratarPerdidaPatrocinador(EquipoFutbol8 eqLocal) throws DAOException {
        
        if (eqLocal.isAutomatico()) return;
        
        var espectativas = eqLocal.getEspectativa().ordinal() + 1;
        var quitar = obtener(espectativas);   
        if (quitar){    
            var result = eqLocal.perderContratosPublicitarios(1);
            if (result > 0){
                var txt = "El " + eqLocal.getNombre() + " ha perdido 1 patrocinador";
                darNoticia(eqLocal.getClub().getGrupo(), txt);   
            }
            grabarEquipoFutbol8(eqLocal);
        }           
        
    }

    private void tratarEquiposVacaciones(ArrayList<EquipoFutbol8> eqs) throws DAOException {
        
        VacacionFutbol8 vac;
        
        for (var eq : eqs) {
            
             vac = obtenerDatosVacacionesFutbol8(eq);
             if (vac == null || !vac.isActivo()) continue;
             
             // Renovacion Jugadores
             if (vac.isRenovacion())
                 for (var jug : eq.getJugadores())
                     if (jug.getContrato() == 1)
                         renovarJugador(jug, eq);
             
             // Entrenar
             if (vac.isActivarEntreno()){
                 entrenar(eq, vac.getPosicionEntreno());
             }
                 
             // Tactica
             if (vac.isActivarTactica()){
                 var datos = datosProximoPartido(eq, aplicacion);
                 var proxPartido = (PartidoFutbol8) datos.get(0);
                 if (proxPartido != null){
                     var ali = obtenerAlineacionPartido(proxPartido, eq);
                     if (ali != null){
                         ali.setTactica(vac.getTactica());
                         grabarAlineacionFutbol8(ali);
                     }
                 }
             }
             
        }
    }

    private void tratarConfigFutbol8(Grupo grp) throws DAOException {
        
        var conf = obtenerConfig(grp);
        var eqs = listaEquiposFutbol8(grp, true);
        
        eqs = equiposNoAuto(eqs);
        String txt;
        
        if (conf == null){
            // La creamos
            conf = new ConfigEconomiaFutbol8(grp);
            grabarConfigEconomiaFutbol8(conf);
        }
        
        if (conf.getEquipoGestor() == null || conf.getDiasGestion() >= MAX_DIAS_GESTION){
            // asignamos gestor
            var x = valorAleatorio(eqs.size());
            var eqGest = eqs.get(x);
            conf.setEquipoGestor(eqGest);
            conf.setDiasGestion(0);
            txt = "El " + eqGest.getNombre() + " se hace cargo de la administración del grupo";           
            darNoticia(grp, txt);
            txt = "Hola, has sido elegido para gestionar la administración de tu grupo, esto significa que podras cambiar ciertos parametros, que afectan al desarrollo economico del juego.</br></br>";   
            getCorreo().enviarMail("ClubDeportivo Gestion Administración Futbol8", 
                    txt, true, eqGest.getClub().getMail());
            
            
        }else{
            conf.setDiasGestion(conf.getDiasGestion() + 1);            
            
        }
        grabarConfigEconomiaFutbol8(conf);
        
    }
    
    private void tratarImpagoCredito(EquipoFutbol8 eq, int cantidad) throws DAOException{
        
        String txt;
        
        if (eq.getCredito() > 0){
            var cantRetenida = cantidad;
            if (cantRetenida > eq.getCredito())
                cantRetenida = eq.getCredito();
            txt = "El banco retiene la cantidad de " + cantRetenida + " al " +
                    eq.getNombre() + " por la deuda  de credito contraida"; 
            darNoticia(eq.getClub().getGrupo(), txt);
            var mov = new Movimiento(eq, DevolucionCredito, cantRetenida);
            grabarRegistro(mov);
            eq.setCredito(eq.getCredito() - cantRetenida);
            eq.setPresupuesto(eq.getPresupuesto() - cantRetenida);
            
        }         
        
    }

    private void tratarFinalicionLiga(CompeticionFutbol8 compAct, Grupo grp, ArrayList<EquipoFutbol8> eqs) throws DAOException {
        
        eqs  = clasificarEquipos(eqs);
        compAct.finalizarLiga(eqs);
        var txt = "EL " + eqs.get(0).getNombre() + " SE HA PROCLAMADO CAMPEON DE LIGA";
        darNoticia(grp, txt);
        
        // cobro del IBI
        tratarCobroIBI(eqs);
        
        // Reparto del dinero de la recaudacion
        var recaudacion = compAct.getRecaudacion();
        var premioPrimero = (int) (recaudacion * confEconomica.getPorcentajePremioLiga() / 100);
        var reparto =  (recaudacion - premioPrimero) / equiposNoAuto(eqs).size();
        
        Movimiento mov;
        mov = new Movimiento(eqs.get(0), PremioCompeticion, premioPrimero);
        grabarRegistro(mov);
        txt = "El " + eqs.get(0).getNombre() + " recibe un premio de " + premioPrimero; 
        darNoticia(grp, txt);
        eqs.get(0).setPresupuesto(eqs.get(0).getPresupuesto() + premioPrimero);
        tratarImpagoCredito(eqs.get(0), premioPrimero); 
        
        for (var eq : eqs) {
            if (!eq.isAutomatico()){
                mov = new Movimiento(eq, DevolucionHacienda, reparto);
                grabarRegistro(mov);
                eq.setPresupuesto(eq.getPresupuesto() + reparto);
                tratarImpagoCredito(eq, reparto);  
                grabarEquipoFutbol8(eq);
            }
        }
        for (var eq : eqs) 
            grabarClub(eq.getClub());
        
        tratarPerdidaPatrocinadores(grp, eqs);   
        grabarCompeticionFutbol8(compAct);
        
        var goleadores = obtenerGoleadores(grp); 
        // los pasamos a historico
        for (var jug : goleadores) {
            var newGol = new GoleadorFutbol8(compAct, jug);
            grabarGoleadorFutbol8(newGol);
        }
        var jugsSubirVal = new ArrayList<JugadorFutbol8>();
        var maxGoles = 0;
        for (var jug : goleadores) {
            if (jug.getGolesTemporada() >= maxGoles){
                jugsSubirVal.add(jug);
                maxGoles = jug.getGolesTemporada();
                darNoticia(grp, "El pichichi de la temporada " + jug.getNombre() + " sube su valoración 5 puntos");
            }
            else break;
        }
        var porteros = obtenerPorteros(grp); 
        // los pasamos a historico
        for (var jug : porteros) {
            var newPor = new PorteroFutbol8(compAct, jug);
            grabarPorteroFutbol8(newPor);
        }

        float ratio = 99;
        for (var jug : porteros) {
            if (jug.getRankingPortero() <= ratio){
                jugsSubirVal.add(jug);
                ratio = jug.getRankingPortero();
                darNoticia(grp, "El mejor portero de la temporada " + jug.getNombre() + " sube su valoración 5 puntos");
            }
            else break;
        }
        for (var eq : eqs){ 
            eq.tratarCaracteristicasJugadoresenUltimaJornada(jugsSubirVal); 
            for (var jug : eq.getJugadores()) {
                grabarJugadorFutbol8(jug);
            }
        }
    }

    private void tratarFinalicionCopa(CompeticionFutbol8 compAct, Grupo grp, 
            PartidoFutbol8 partido) throws DAOException {
                
        compAct.finalizarCopa(partido);
        grabarCompeticionFutbol8(compAct);
        grabarEquipoFutbol8(partido.getGanador());
        grabarEquipoFutbol8(partido.getPerdedor());
        grabarClub(partido.getGanador().getClub());
        grabarClub(partido.getPerdedor().getClub());
        var txt = "EL " + partido.getGanador().getNombre() + " SE HA PROCLAMADO CAMPEON DE COPA";
        darNoticia(grp, txt);
        if (!partido.getGanador().isAutomatico()){
            txt = "El " + partido.getGanador().getNombre() + " ha conseguido un nuevo patrocinador"; 
            darNoticia(grp, txt);
        }
        var recaudacion = compAct.getRecaudacion();
        var premioPrimero = (int) (recaudacion * confEconomica.getPorcentajeCampeonCopa() / 100);
        var premioSegundo = (int) (recaudacion * confEconomica.getPorcentajeSubCampeonCopa() / 100);
        Movimiento mov;
        mov = new Movimiento(partido.getGanador(), PremioCompeticion, premioPrimero);
        grabarRegistro(mov);
        txt = "El " + partido.getGanador().getNombre() + " recibe un premio de " + premioPrimero; 
        darNoticia(grp, txt);
        partido.getGanador().setPresupuesto(partido.getGanador().getPresupuesto() + premioPrimero); 
        tratarImpagoCredito(partido.getGanador(), premioPrimero);
        grabarEquipoFutbol8(partido.getGanador());
        mov = new Movimiento(partido.getPerdedor(), PremioCompeticion, premioSegundo);
        grabarRegistro(mov);
        txt = "El " + partido.getPerdedor().getNombre() + " recibe un premio de " + premioSegundo; 
        darNoticia(grp, txt);
        partido.getPerdedor().setPresupuesto(partido.getPerdedor().getPresupuesto() + premioSegundo);   
        grabarEquipoFutbol8(partido.getPerdedor());
        tratarImpagoCredito(partido.getPerdedor(), premioSegundo);
        
    }
    
    
     private static void regularizarCuentas(ArrayList<EquipoFutbol8> eqs) throws DAOException {
        
         ArrayList<Movimiento> movs;
         Movimiento newMov;
         for (var eq : eqs) {
            movs = obtenerMovimientos(eq);
            for (var mov : movs) {
                if (mov.isPositivo())
                    eq.setTotalIngresos(eq.getTotalIngresos() + mov.getValor());
                else
                    eq.setTotalGastos(eq.getTotalGastos() + mov.getValor());
            }
            if (!eq.isSaldoCuentasOK()){
                var descuadre = eq.getDescuadreCuentas();
                if (descuadre > 0)
                    newMov = new Movimiento(eq, RegularizacionNegativa, descuadre);
                else 
                    newMov = new Movimiento(eq, RegularizacionPositiva, abs(descuadre));
                grabarRegistro(newMov);
            }
            
        }
    }

    private void tratarCobroIBI(ArrayList<EquipoFutbol8> eqs) throws DAOException {
        
        for (var eq : eqs) {
            
            if (eq.isAutomatico()) continue;
            var pagar = (eq.getCampo() * confEconomica.getIBI() / 100) * eqs.size();
            var mov = new Movimiento(eq, IBI, pagar);
            grabarRegistro(mov);
            eq.setPresupuesto(eq.getPresupuesto() - pagar);
            compActual.setRecaudacion(compActual.getRecaudacion() + pagar);
            grabarCompeticionFutbol8(compActual);
            
        }
        
    }

    private boolean comprobarLanzamiento() throws DAOException {
        
        // Si ya hemos disputado jornada en el dia ya no lanzamos
        var ultimoLanzamiento = obtenerFechaUltimaJornada();
        
        if (ultimoLanzamiento == null) return true;

        var hoy = new Date();
        var calHoy = new GregorianCalendar();
        calHoy.setTime(hoy);
        var calUtimJor = new GregorianCalendar(); 
        calUtimJor.setTime(ultimoLanzamiento); 
        
        return !(calHoy.get(DAY_OF_MONTH) ==  calUtimJor.get(DAY_OF_MONTH) &&
                calHoy.get(MONTH) ==  calUtimJor.get(MONTH) &&
                calHoy.get(YEAR) ==  calUtimJor.get(YEAR));
        
    }
       
    
}
      
    

