/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mld.clubdeportivo.controladores;
import java.util.*;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mld.clubdeportivo.base.*;
import mld.clubdeportivo.base.futbol8.*;
import mld.clubdeportivo.bd.*;
import mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8;
import mld.clubdeportivo.utilidades.Calculos;
import mld.clubdeportivo.utilidades.Correo;
import mld.clubdeportivo.utilidades.UtilGenericas;
import java.util.logging.*;

/**
 *
 * @author mlopezd
 */
public class LanzarJornadaFutbol8 {
    
    private static Logger logger = Logger.getLogger(LanzarJornadaFutbol8.class.getName());

   
    private ServletContext aplicacion;
    private CompeticionFutbol8 compActual;
    private ConfigEconomiaFutbol8 confEconomica;
    
    protected void lanzarJornadaFutbol8(HttpServletRequest req,
            HttpServletResponse resp, ServletContext app) throws DAOException {
        

        aplicacion = app;
        
        String txtCorreo = "";
        
        
        if (!comprobarLanzamiento()) return;
        
                
        for (Grupo grp : JDBCDAOGrupo.obtenerGruposActivos()) {

            confEconomica = JDBCDAOFutbol8.obtenerConfig(grp);
            compActual = null;
            String claseComp = "";
            if (isDiaCompeticionFutbol8("Liga")) claseComp = "Liga";
            else if (isDiaCompeticionFutbol8("Copa")) claseComp = "Copa";

            CompeticionFutbol8 compAct = 
                    JDBCDAOFutbol8.competicionActiva(grp, claseComp);
            
            // Si no hay copa hacemos partido de liga
            if (isDiaCompeticionFutbol8("Copa") && compAct == null){
                claseComp = "Liga";
                compAct = JDBCDAOFutbol8.competicionActiva(grp, claseComp);
            }
            
            ArrayList<EquipoFutbol8> equipos =
                JDBCDAOFutbol8.listaEquiposFutbol8(grp);
            
            ArrayList<EquipoFutbol8> eqs = new ArrayList<EquipoFutbol8>();
            for (EquipoFutbol8 eq : equipos) 
                eqs.add(JDBCDAOFutbol8.obtenerSimpleEquipoFutbol8(eq.getId()));            
            

            if (compAct != null){
               
                compActual = compAct;

                JornadaFutbol8 jor =
                        JDBCDAOFutbol8.obtenerJornadaParaDisputar(compAct, eqs);
                

                if (claseComp.equals("Liga")){
                    
                    txtCorreo = disputarJornada(jor, eqs, claseComp);

                    // si el final de primera vuelta creamos la copa
                    if (jor.getNumero() == eqs.size() - 1){
                        CompeticionFutbol8 comp = CompeticionFutbol8.crearCopa(grp, eqs);
                        //tratarPerdidaPatrocinadores(grp, eqs);                        
                        JDBCDAOFutbol8.grabarDatosCompeticion(comp, eqs);
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
                        
                        JornadaFutbol8 jorIda = 
                                JDBCDAOFutbol8.obtenerJornada(jor.getNumero() - 1, compAct);                        
                        compAct.setJornadas(new ArrayList<JornadaFutbol8>());
                        for (int i = 1; i < 3; i++){
                            JornadaFutbol8 jorProx =
                                    JDBCDAOFutbol8.obtenerJornadaSimple(jor.getNumero() + i, compAct);
                            if (jorProx != null)
                                compAct.getJornadas().add(jorProx);
                                    
                        }
                        
                        ArrayList<EquipoFutbol8> eqsClasif = 
                                clasificarEquiposCopa(jorIda, jor);
                        
                        compAct.crearSigienteRondaDeCopa(eqsClasif, jor.getNumero() + 1);
                        
                        for (JornadaFutbol8 jorSave : compAct.getJornadas()) 
                            for (PartidoFutbol8 partSave : jorSave.getPartidos()){ 
                                JDBCDAOFutbol8.grabarPartidoFutbol8(partSave);
                                for (AlineacionFutbol8 aliSave : partSave.getAlineaciones()) 
                                    JDBCDAOFutbol8.grabarAlineacionFutbol8(aliSave);                                
                            }
                                                
                       
                    }
                    // si es final
                    else if (jor.getDescripcion().equals("Final")){
                        PartidoFutbol8 partido = jor.getPartidos().get(0);
                        if (partido.getGanador() == null){
                            hacerTandaPenalties(partido);
                        }
                        tratarFinalicionCopa(compAct, grp, partido);                        
                    }                        
                    
                }
                
                jor.setFecha(new Date());
                JDBCDAOFutbol8.grabarJornadaFutbol8(jor);
                tratarEquiposVacaciones(eqs);
                tratarConfigFutbol8(grp);
                
                enviarMailJornada(txtCorreo, compAct);                

            }
            else if(UtilGenericas.isDomingo()){
                crearCompeticion(grp);                
            }
        }
        
    }
    
    private static void hacerTandaPenalties(PartidoFutbol8 partido) throws DAOException{
        
        partido.getCronica().clear();
        partido.hacerTandaPenalties();
        JDBCDAOFutbol8.grabarPartidoFutbol8(partido);
        JDBCDAOFutbol8.grabarEstadisticaPartidoFutbol8(
                partido.getEstadistica());
        ArrayList<CronicaFutbol8> cronica = partido.getCronica();
        for (CronicaFutbol8 cronicaFutbol8 : cronica) 
            JDBCDAOFutbol8.grabarCronicaFutbol8(cronicaFutbol8); 
        
    }
    
    public static void crearCompeticiones() throws DAOException{
        
        for (Grupo grp : JDBCDAOGrupo.obtenerGruposActivos()) {
                crearCompeticion(grp); 
        }
        
    }
    
    
    public static void crearCompeticion(Grupo grp)
            throws DAOException{
        
        CompeticionFutbol8 compAct = JDBCDAOFutbol8.competicionActiva(grp, "Liga");   
        
        if (compAct != null) return;
        
        ArrayList<EquipoFutbol8> equipos =
                JDBCDAOFutbol8.listaEquiposFutbol8(grp, false);
        
        // Eliminamos los que no han accedido ultimamente
        ArrayList<EquipoFutbol8> equiposAEliminar = new ArrayList<EquipoFutbol8>();
        for (EquipoFutbol8 eq : equipos) {
            JDBCDAOFutbol8.obtenerJugadores(eq);
            if (eq.isAbandonado()) equiposAEliminar.add(eq);
        }
        
        for (EquipoFutbol8 eq : equiposAEliminar) {
            equipos.remove(eq);
            JDBCDAOFutbol8.eliminarEquipoFutbol8(eq);            
        }
        
        // Si no hay ningun equipo no automatico salimos
        boolean todosAuto = true;
        for (EquipoFutbol8 eq : equipos) {
            if (!eq.isAutomatico()){
                todosAuto = false;
                break;
            }
        }
        if (todosAuto) return;
        
        ArrayList<EquipoFutbol8> eqsElim = EquipoFutbol8.equiposAutoAEliminar(equipos);
        
        for (EquipoFutbol8 eq : eqsElim) {
            equipos.remove(eq);
            JDBCDAOFutbol8.eliminarEquipoFutbol8(eq);
        }
       
                
        if (!equipos.isEmpty()){ 
        
            ArrayList<EquipoFutbol8> eqs = new ArrayList<EquipoFutbol8>();
        
            for (EquipoFutbol8 eq : equipos) 
                eqs.add(JDBCDAOFutbol8.obtenerSimpleEquipoFutbol8(eq.getId()));
            if (eqs.size() < 16){
                int eqsAuto = 0;
                if (eqs.size() < 4) eqsAuto = 4 - eqs.size();
                else if (eqs.size() % 2 != 0) eqsAuto = 1;
                while (eqs.size() + eqsAuto > 16) eqsAuto--;
                for (int i = 1; i <= eqsAuto; i++ ){
                    EquipoFutbol8 eq = UtilesFutbol8.crearEquipoAutomatico(grp);
                    eqs.add(eq);
                }
            }else if (eqs.size() > 16){
                // los ultimos no entran en la liga
                while (eqs.size() > 16){
                    eqs.remove(eqs.size() - 1);
                }
            }
            CompeticionFutbol8 comp = CompeticionFutbol8.crearLiga(grp, eqs);
            regularizarCuentas(eqs);
            JDBCDAOFutbol8.grabarDatosCompeticion(comp, eqs);
            
            /* Si no existe la configuracion economica 
            para el grupola creamos            
            */
            if (JDBCDAOFutbol8.obtenerConfig(grp) == null){
                ConfigEconomiaFutbol8 conf = new ConfigEconomiaFutbol8(grp);
                JDBCDAOFutbol8.grabarConfigEconomiaFutbol8(conf);
            }            
            
            for (EquipoFutbol8 eq : eqs){
                eq.setActivo(true);
                JDBCDAOFutbol8.grabarEquipoFutbol8(eq);
            }                 
            crearJugadorSubasta(grp);            
            enviarMailNuevaCompeticion(comp);
        }        
        
    }
    
    private String disputarJornada(JornadaFutbol8 jor, ArrayList<EquipoFutbol8> eqs,
            String tipo) throws DAOException{
        
        logger.log(Level.INFO, "Inicio pre Jornada");
        ArrayList<EquipoFutbol8> eqsQueJuegan =
                        new ArrayList<EquipoFutbol8>();
        StringBuilder txtCorreo = new StringBuilder();
        txtCorreo.append("RESULTADOS JORNADA ");
        txtCorreo.append(jor.getDescripcion()).append("<br/><br/>");
        txtCorreo.append("<table>");
        
        for (PartidoFutbol8 partido : jor.getPartidos()) { 
         
            partido.setTipo(tipo);
            EquipoFutbol8 eqLocal = (EquipoFutbol8) partido.getEqLocal();
            EquipoFutbol8 eqVisit = (EquipoFutbol8) partido.getEqVisitante();
            
            eqLocal.completarAlineacion();
            eqVisit.completarAlineacion();
            
            EstadisticaPartidoFutbol8 est = new EstadisticaPartidoFutbol8(partido);
            partido.setEstadistica(est);
            logger.log(Level.INFO, "Pre partido");
            
            // calculo de posibilidades
            partido.setIsSimulacion(true);
            int victoriasLocal = 0, victoriasVisit = 0;
            for (int i = 1; i <= PartidoFutbol8.NUMERO_SIMULACIONES; i++){                
                partido.jugarPartido();
                if (partido.getGanador() != null){
                    if (partido.getGanador().equals(eqLocal)) victoriasLocal++;
                    else if (partido.getGanador().equals(eqVisit)) victoriasVisit++;                    
                }
            } 
            est.setVictoriasLocal(victoriasLocal * 100 / PartidoFutbol8.NUMERO_SIMULACIONES);
            est.setVictoriasVisitante(victoriasVisit * 100 / PartidoFutbol8.NUMERO_SIMULACIONES);
            
            // partido real
            partido.setIsSimulacion(false);
            est = partido.jugarPartido();
            
            logger.log(Level.INFO, "Post Partido");
           
            eqsQueJuegan.add(eqLocal);
            eqsQueJuegan.add(eqVisit);
            JDBCDAOFutbol8.grabarPartidoFutbol8(partido);
            ArrayList<CronicaFutbol8> cronica = partido.getCronica();
            for (CronicaFutbol8 cronicaFutbol8 : cronica) 
                JDBCDAOFutbol8.grabarCronicaFutbol8(cronicaFutbol8);   
            
          
            JDBCDAOFutbol8.grabarEstadisticaPartidoFutbol8(est);
            CompeticionFutbol8 comp = (CompeticionFutbol8) jor.getCompeticion();
            if (comp.getClase().equals("Liga")){
                partido.asignarPuntos();
                JDBCDAOFutbol8.grabarPuntuacionFutbol8(eqLocal.getPuntuacion());
                JDBCDAOFutbol8.grabarPuntuacionFutbol8(eqVisit.getPuntuacion());
                if (partido.getGanador() == eqVisit)
                    tratarPerdidaPatrocinador(eqLocal);
                JDBCDAOClub.grabarClub(eqLocal.getClub());
                JDBCDAOClub.grabarClub(eqVisit.getClub());
            }
            gestionEstadio(partido);
            eqLocal.limpiarJugadoresQueJuegan();
            eqVisit.limpiarJugadoresQueJuegan();
            for (JugadorFutbol8 jug : eqLocal.getJugadores()) {                
                JDBCDAOFutbol8.grabarJugadorFutbol8(jug); 
            }
            for (JugadorFutbol8 jug : eqVisit.getJugadores()) {                
                JDBCDAOFutbol8.grabarJugadorFutbol8(jug); 
            }
            
            JDBCDAOFutbol8.grabarEntrenadorFutbol8(eqLocal.getEntrenador());
            JDBCDAOFutbol8.grabarEntrenadorFutbol8(eqVisit.getEntrenador());
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
        
        ArrayList<EquipoFutbol8> eqsNoAuto = 
                EquipoFutbol8.equiposNoAuto(eqs);
        ArrayList<EquipoFutbol8> eqsNoAutoQueJuegan = 
                EquipoFutbol8.equiposNoAuto(eqsQueJuegan);
        ArrayList<EquipoFutbol8> eqsAuto = 
                EquipoFutbol8.equiposAuto(eqs);
        gestionJornadaFutbol8(jor.getCompeticion().getGrupo(), 
                eqsNoAuto, eqsNoAutoQueJuegan, eqsAuto);
        logger.log(Level.INFO, "Fin Jornada");
        
        return txtCorreo.toString();
        
        
    }

    private boolean isDiaCompeticionFutbol8(String clase) {
        // tipo es Liga, Copa etc

        boolean result = false;        

        String confDiasLiga = aplicacion.getInitParameter("diasligafutbol8");
        String confDiasCopa = aplicacion.getInitParameter("diascopafutbol8");

        String[] diasLiga = confDiasLiga.split(",");
        String[] diasCopa = confDiasCopa.split(",");

        Calendar calendario = Calendar.getInstance();
        calendario.setTime(new Date());

        String diaAct = String.valueOf(calendario.get(Calendar.DAY_OF_WEEK));

        for (String dia : diasLiga) {
            if (dia.equals(diaAct) && clase.equals("Liga")) result = true;
        }
        for (String dia : diasCopa) {
            if (dia.equals(diaAct) && clase.equals("Copa")) result = true;
        }
        return result;

    }
    
    private void gestionJornadaFutbol8(Grupo grp, ArrayList<EquipoFutbol8> eqs,
            ArrayList<EquipoFutbol8> eqsQueJuegan, ArrayList<EquipoFutbol8> eqsAuto)
            throws DAOException {
    
        logger.log(Level.INFO, "gestionJornadaFutbol8");        
        
        int numTacs = Calculos.valorAleatorio(5, 10);
        UtilesFutbol8.crearEntrenadorfutbol8(grp, numTacs);
        gestionGeneralParaTodosLosEquipos(grp, eqs);
        gestionGeneralParaLosEquiposQueJuegan(grp, eqsQueJuegan);    
        gestionGeneralParaLosEquiposAuto(grp, eqsAuto);

        for (EquipoFutbol8 eq : eqs) {
            JDBCDAOFutbol8.grabarEquipoFutbol8(eq);
            JDBCDAOFutbol8.grabarEntrenadorFutbol8(eq.getEntrenador());
            for (JugadorFutbol8 jug : eq.getJugadores()) {
                JDBCDAOFutbol8.grabarJugadorFutbol8(jug);
            }            
        }  
        
        for (EquipoFutbol8 eq : eqsAuto) {
            JDBCDAOFutbol8.grabarEquipoFutbol8(eq);
            for (JugadorFutbol8 jug : eq.getJugadores()) {
                JDBCDAOFutbol8.grabarJugadorFutbol8(jug);
            }            
        }    
        
        gestionarJugadoresSubasta(grp, eqs,eqsAuto);

    }
    
    private void gestionarJuveniles(ArrayList<EquipoFutbol8> eqs) throws DAOException{
        logger.log(Level.INFO, "gestionarJuveniles");
        

        for (EquipoFutbol8 eq : eqs) {
            JuvenilFutbol8 juv = JDBCDAOFutbol8.obtenerJuvenil(eq);
            if (juv == null){
                PosicionElegidaFutbol8 posEq = eq.getPosicionJuvenil();
                int x;
                if (posEq == PosicionElegidaFutbol8.Cualquiera)
                    x = Calculos.valorAleatorio(PosicionJugFutbol8.values().length);
                else 
                    x = posEq.ordinal();
                    
                PosicionJugFutbol8 pos = PosicionJugFutbol8.values()[x];
                juv = new JuvenilFutbol8(eq, pos);                
            }                    
            else{
                juv.bajarValoracion();
                juv.setJornadas(juv.getJornadas() + 1);
            }
            JDBCDAOFutbol8.grabarJuvenilFutbol8(juv);
        }        
    }
    
    private void gestionarJugadoresSubasta(Grupo grp, 
            ArrayList<EquipoFutbol8> eqs, ArrayList<EquipoFutbol8> eqsAuto) 
            throws DAOException{
        // se gestiona solo los lunes
        logger.log(Level.INFO, "gestionarJugadoresSubasta");
        if (!UtilGenericas.isLunes()) return;
         
        ArrayList<JugadorFutbol8> jugs = JDBCDAOFutbol8.obtenerJugadoresGrupo(grp);
        int mediaClausula = JugadorFutbol8.clausulaMedia(jugs);
        Movimiento mov;
        for (JugadorFutbol8 jug : jugs) {
            if (!jug.isEnSubasta()) continue;
            EquipoFutbol8 eqVenta = jug.getEquipo();
            EquipoFutbol8 eqPuja = null;
            if (jug.getEquipoPuja() != 0)
                eqPuja = JDBCDAOFutbol8.obtenerSimpleEquipoFutbol8(jug.getEquipoPuja());
            if (eqPuja != null){
                if (jug.getPuja() >= jug.getClausula()){                    
                    if (eqPuja.getJugadores().size() >= EquipoFutbol8.NUMERO_MAX_JUGADORES){
                        // si el equipo tiene mas de 16 jugadores pagamos el 25%
                        // y no asignamos el jugador
                        int compensacion = (int) (jug.getPuja() * 0.25);
                        eqPuja.setPresupuesto(eqPuja.getPresupuesto() - compensacion);
                        mov = new Movimiento(eqPuja, ClaseMovimiento.Fichajes, compensacion, "Gastos Gestion");
                        JDBCDAOMovimiento.grabarRegistro(mov);
                        if (eqVenta != null){
                            eqVenta.setPresupuesto(eqVenta.getPresupuesto() + compensacion);
                            mov = new Movimiento(eqPuja, ClaseMovimiento.Fichajes, compensacion, "Compensación");
                            JDBCDAOMovimiento.grabarRegistro(mov);
                        }
                        String txt = "El equipo " + eqPuja.getNombre() + " ha ganado la " +
                                "puja por el jugador " + jug.getNombre() + " por la cantidad de " + jug.getClausula() +
                                " pero no se ha podido realizar la incorporacion por tener la plantilla completa. Pierde el 25% de la " +
                                "cantidad ofertada en concepto de gastos de gestion";
                        UtilesFutbol8.darNoticia(grp, txt);
                        jug.setEquipoPuja(0);
                        jug.setPuja(jug.getClausula());   
                    }else{
                        int iva = jug.getPuja() * confEconomica.getIva() / 100;
                        String txt = "El equipo " + eqPuja.getNombre() + " ha ganado la " +
                                "puja por el jugador " + jug.getNombre() + " por la cantidad de " + jug.getPuja() +
                                " mas " + iva + " de iva";
                        UtilesFutbol8.darNoticia(grp, txt);
                        eqPuja.setPresupuesto(eqPuja.getPresupuesto() - jug.getPuja() - iva);
                        mov = new Movimiento(eqPuja, ClaseMovimiento.Fichajes, jug.getPuja() + iva, jug.getNombre());
                        JDBCDAOMovimiento.grabarRegistro(mov);
                        JDBCDAOFutbol8.grabarEquipoFutbol8(eqPuja);
                        if (eqVenta != null){
                            eqVenta.setPresupuesto(eqVenta.getPresupuesto() + jug.getPuja());
                            JDBCDAOFutbol8.grabarEquipoFutbol8(eqVenta);
                            mov = new Movimiento(eqVenta, ClaseMovimiento.VentaJugadores, jug.getPuja(), jug.getNombre());
                            JDBCDAOMovimiento.grabarRegistro(mov);
                        }
                        jug.setContrato(Jugador.JORNADAS_CONTRATO);
                        jug.setEnSubasta(false);
                        jug.setEquipo(eqPuja);
                        jug.setGrupo(grp);
                        // aumentamos un 40% la clausula 
                        jug.setClausula(jug.getPuja() + jug.getPuja() * 40 / 100);
                        jug.setEquipoPuja(0);
                        jug.setPuja(0); 
                        JDBCDAOFutbol8.grabarJugadorFutbol8(jug);
                    }                    
                }
            }
            else{
                if (jug.getEquipo() != null){
                    // Equipo extranjero que puja
                    int posibilidad = Math.max(2, jug.getClausula() / 2000);
                    if (Calculos.obtener(posibilidad)){
                        // se paga el 50% y maximo de 10.000
                        int cantidad = Math.min(10000, jug.getClausula() / 2);
                        String txt = "Debido a que ningun equipo del grupo ha pujado por " +
                                "el jugador " + jug.getNombre() + " un equipo internacional se lo ha llevado por " + cantidad;
                        UtilesFutbol8.darNoticia(grp, txt);
                        eqVenta.setPresupuesto(eqVenta.getPresupuesto() + cantidad);
                        JDBCDAOFutbol8.grabarEquipoFutbol8(eqVenta);
                        mov = new Movimiento(eqVenta, ClaseMovimiento.VentaJugadores, cantidad, jug.getNombre());
                        JDBCDAOMovimiento.grabarRegistro(mov);
                        JDBCDAOFutbol8.eliminarJugadorFutbol8(jug);
                    }
                    else
                        jug.bajarSubasta(jug.getEquipo() == null);
                }
                else
                    jug.bajarSubasta(jug.getEquipo() == null);     
            }
        }            
            
        // creamos un jugador nuevo si es lunes
        int numJugs = 0;
        for (EquipoFutbol8 eq : eqs) 
            numJugs = numJugs + eq.getJugadores().size();
        for (EquipoFutbol8 eq : eqsAuto) 
            numJugs = numJugs + eq.getJugadores().size();
        
        
        ArrayList<JugadorFutbol8> jugLibres = 
                JDBCDAOFutbol8.obtenerJugadoresLibresGrupo(grp);
            
        if (jugLibres.size() > JugadorFutbol8.NUMERO_MAX_JUGADORES_SUBASTA)
            JDBCDAOFutbol8.eliminarJugadorFutbol8(jugLibres.get(0));
         
        boolean crear = numJugs < (eqs.size() + eqsAuto.size() + 1) * EquipoFutbol8.NUMERO_MAX_JUGADORES &&
                numJugs + jugLibres.size() < JugadorMaestroFutbol8.NUMERO_MAX_JUGADORES;
            
        if (crear){
            crearJugadorSubasta(grp);
            // si el grupo tiene mas de 8 equipos, creamos un segundo jugador
            if (eqs.size() > 8) crearJugadorSubasta(grp);
        }
        
 
    }
    
    private static void crearJugadorSubasta(Grupo grp) throws DAOException{
                 
        int pos = Calculos.valorAleatorio(PosicionJugFutbol8.values().length);
        int val = Calculos.valorAleatorio(10, 70);
        
        JugadorFutbol8 jug = null;            
        try{
            jug = UtilesFutbol8.crearJugadorSubasta(grp, PosicionJugFutbol8.values()[pos], val);
        }
        catch(Exception ex){}                    
        if (jug != null){
            String txt = "El jugador libre " + jug.getNombre() + " ha entrado en subasta";
            UtilesFutbol8.darNoticia(grp, txt);
        }   
        
    }

   

    public void gestionEstadio(PartidoFutbol8 partido) throws DAOException {

        logger.log(Level.INFO, "gestionEstadio");

        EquipoFutbol8 eq = (EquipoFutbol8) partido.getEqLocal();
        
        if (eq.isAutomatico() || !eq.isJuegaEnCasa()) return;

        int ingreso = partido.getEspectadores() * partido.getPrecioEntradas();
        int hacienda = confEconomica.getRetencionHacienda(ingreso);
        eq.setPresupuesto(eq.getPresupuesto() + ingreso - hacienda);

        JDBCDAOFutbol8.grabarEquipoFutbol8(eq);

        Movimiento mov;
        mov = new Movimiento(eq, ClaseMovimiento.IngresoTaquilla, ingreso);
        JDBCDAOMovimiento.grabarRegistro(mov);
        mov = new Movimiento(eq, ClaseMovimiento.PagoHacienda, hacienda);
        JDBCDAOMovimiento.grabarRegistro(mov);
        compActual.setRecaudacion(compActual.getRecaudacion() + hacienda);
        JDBCDAOFutbol8.grabarCompeticionFutbol8(compActual);

    }

    private void gestionGeneralParaTodosLosEquipos(Grupo grp, 
            ArrayList<EquipoFutbol8> eqs) throws DAOException {
        
        logger.log(Level.INFO, "gestionGeneralParaTodosLosEquipos");
        int fluctuacion = obtenerFluctuacionBolsa(grp);
        
        for (EquipoFutbol8 eq : eqs) {
            eq.reiniciarInversiones();
            eq.aplicarFluctuacion(fluctuacion);
            eq.aplicarInteresCredito(confEconomica.getInteresCredito()); 
            eq.setEntrenamiento(false); 
            if (eq.obtenerOjeado()){
                String txt = "Un ojeador ha captado un jugador para el " + eq.getNombre(); 
                UtilesFutbol8.darNoticia(eq.getClub().getGrupo(), txt);
                txt = "Un ojeador ha captado un jugador para tu equipo, puedes incorporarlo en la pantalla de Plantilla"; 
                Correo.getCorreo().enviarMail("ClubDeportivo Jugador Ojeado Futbol8", 
                        txt, true, eq.getClub().getMail());
            }
            if (eq.perderOjeador()){
                String txt = "Un ojeador ha abandonado el " + eq.getNombre(); 
                UtilesFutbol8.darNoticia(eq.getClub().getGrupo(), txt);
            }
            for (JugadorFutbol8 jug : eq.getJugadores()) {
                jug.bajarValoracion();
                if (jug.getJornadasLesion() > 0)
                    jug.setJornadasLesion(jug.getJornadasLesion() - 1);
            }
            
        }
        gestionarJuveniles(eqs);        
        
    }
    
    private void gestionGeneralParaLosEquiposQueJuegan(Grupo grp, 
            ArrayList<EquipoFutbol8> eqs) throws DAOException {
        logger.log(Level.INFO, "gestionGeneralParaLosEquiposQueJuegan");
        Movimiento mov;
        int cantidad;
        for (EquipoFutbol8 eq : eqs) {
            cantidad = eq.pagarFichas();
            mov = new Movimiento(eq, ClaseMovimiento.Fichas, cantidad);
            JDBCDAOMovimiento.grabarRegistro(mov);
            cantidad = eq.ingresoPublicidad(confEconomica.getPorcentajeRetencionHacienda(cantidad));
            mov = new Movimiento(eq, ClaseMovimiento.IngresoPublicidad, cantidad);
            JDBCDAOMovimiento.grabarRegistro(mov);
            int hacienda = confEconomica.getRetencionHacienda(cantidad);
            mov = new Movimiento(eq, ClaseMovimiento.PagoHacienda, hacienda);
            JDBCDAOMovimiento.grabarRegistro(mov);
            compActual.setRecaudacion(compActual.getRecaudacion() + hacienda);
            JDBCDAOFutbol8.grabarCompeticionFutbol8(compActual);
            cantidad = eq.pagarMantenimientoEstadio();
            if (cantidad > 0){
                mov = new Movimiento(eq, ClaseMovimiento.Mantenimiento, cantidad);
                JDBCDAOMovimiento.grabarRegistro(mov);
            }
            cantidad = eq.pagarGestion();
            if (cantidad > 0){
                mov = new Movimiento(eq, ClaseMovimiento.Gestion, cantidad);
                JDBCDAOMovimiento.grabarRegistro(mov);
            }           
            
            gestionDeContratos(eq);
        }
        
        
    }
    
    private void gestionGeneralParaLosEquiposAuto(Grupo grp, 
            ArrayList<EquipoFutbol8> eqs) throws DAOException {
        
        logger.log(Level.INFO, "gestionGeneralParaLosEquiposAuto");
        
        int valMedia = JDBCDAOFutbol8.valoracionMediaJugadores(grp);
        int subidaBajada;
        int mediaEquipo;
        
               
         for (EquipoFutbol8 eq : eqs) {
             mediaEquipo = eq.getValoracionMediaJugadores();
             if (mediaEquipo > valMedia) subidaBajada = -10;
             else subidaBajada = 10;
             
             for (JugadorFutbol8 jug : eq.getJugadores()) {
                 if (Calculos.obtener(2))
                     jug.setValoracion(jug.getValoracion() + subidaBajada);
                 if (jug.getJornadasLesion() > 0)
                    jug.setJornadasLesion(jug.getJornadasLesion() - 1);
             }
        }
        
    }
    
    private int obtenerFluctuacionBolsa(Grupo grp) throws DAOException{
        logger.log(Level.INFO, "obtenerFluctuacionBolsa");
        boolean tipoFluctuacion = Calculos.obtener(2);
        int fluctuacion;
        if (!tipoFluctuacion)
            fluctuacion = Calculos.valorAleatorio(confEconomica.getSubidaMaxBolsa());
        else{ 
            fluctuacion = -(Calculos.valorAleatorio(confEconomica.getBajadaMaxBolsa()));
            if (confEconomica.isCrackBolsa() && Calculos.obtener(500)){
                // hay un crack
                fluctuacion = fluctuacion * 4;
            }
        }
        
        JDBCDAOBolsa.grabarBolsa(new Bolsa(fluctuacion));
        String txt;
        if (fluctuacion < -(confEconomica.getBajadaMaxBolsa()))
            txt = "La bolsa ha sufrido un ckack y a tenido una fluctuacion del " + fluctuacion + "%";
        else
            txt = "La bolsa a tenido una fluctuacion del " + fluctuacion + "%"; 
        UtilesFutbol8.darNoticia(grp, txt);
        
        return fluctuacion;
        
    }

    
    private void gestionDeContratos(EquipoFutbol8 eq) throws DAOException {
        logger.log(Level.INFO, "gestionDeContratos");
        ArrayList<JugadorFutbol8> jugs = eq.getJugadores();
        ArrayList<JugadorFutbol8> jugsElim = new  ArrayList<JugadorFutbol8>();
        String txtJugs = "";
        for (JugadorFutbol8 jug : jugs) {
            jug.setContrato(jug.getContrato() - 1);
            if (jug.getContrato() == 1) {
                txtJugs = txtJugs + jug.getNombre() + "<br/>";
            }
            else if (jug.getContrato() == 0)                
                jugsElim.add(jug);
        }
        if (!txtJugs.isEmpty()){
            String txt = "Jugadores con contrato a punto de expirar: <br/><br/>" + txtJugs +
                    "<br/>Si no lo renuevas se marcharán de tu club";   
            Correo.getCorreo().enviarMail("ClubDeportivo Renovacion de Jugadores Futbol8", 
                    txt, true, eq.getClub().getMail());
        }
        for (JugadorFutbol8 jug : jugsElim) {
            eq.getJugadores().remove(jug); 
            String txt = "Al jugador " + jug.getNombre() + " del " + 
                    eq.getNombre() + " se le ha acabado el contrato y no ha renovado";
            UtilesFutbol8.darNoticia(eq.getClub().getGrupo(), txt);
            JDBCDAOFutbol8.eliminarJugadorFutbol8(jug);
        }
        
        EntrenadorFutbol8 entr = eq.getEntrenador();
        entr.setContrato(entr.getContrato() - 1);
        if (entr.getContrato() == 1) {
            String txt = "El contrato de tu entrenador " + entr.getNombre() + " esta a punto de expirar, si quieres contratar otro entrenador, es ahora el momento, sino en la próxima jornada se renovará automaticamente por otras 40 jornadas. Recuerda que el cambio de entrenador resetea la moral a 100.";
            Correo.getCorreo().enviarMail("ClubDeportivo Fin Contrato Entrenador Futbol8", 
                    txt, true, eq.getClub().getMail());
        }
        else if (entr.getContrato() == 0) {
            entr.setContrato(EntrenadorFutbol8.JORNADAS_CONTRATO);
            entr.anyadirTacticaNuevoContrato();
            String txt = "Al entrenador " + entr.getNombre() + " del " + 
                    eq.getNombre() + " ha renovado su contrato automaticamente";
            UtilesFutbol8.darNoticia(eq.getClub().getGrupo(), txt);
        }

    }

    private ArrayList<EquipoFutbol8> clasificarEquiposCopa(
            JornadaFutbol8 jorIda, JornadaFutbol8 jorVuelta) throws DAOException {
        // Esta implementado el tema de los valor doble en los goles fuera
        // de casa
        
        ArrayList<EquipoFutbol8> eqs = new ArrayList<EquipoFutbol8>();       
        
        int golesA, golesB;
        
        for (PartidoFutbol8 partidoIda : jorIda.getPartidos()) 
            for (PartidoFutbol8 partidoVuelta : jorVuelta.getPartidos()) 
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
        
        
        ArrayList<String> correos = JDBCDAOClub.mailsClubs(comp.getGrupo(), Deporte.Futbol8);
        
        String txt = "Se ha creado la competicion " + comp.getNombre();          

        Correo.getCorreo().enviarMailMasivo("ClubDeportivo Nueva competicion Futbol8", 
                txt, true, correos);
        
    }
    
    private static void enviarMailJornada(String txt, CompeticionFutbol8 comp 
            ) throws DAOException {
        
        
        ArrayList<String> correos = JDBCDAOClub.mailsClubs(comp.getGrupo(), Deporte.Futbol8);
 
        Correo.getCorreo().enviarMailMasivo("ClubDeportivo Jornada Futbol8", 
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
       
        ArrayList<EquipoFutbol8> eqsClasif = EquipoFutbol8.clasificarEquipos(eqs);
       
        int pos = 1;
        int espectativa, posSegunEspec;
        int result;
        for (Iterator<EquipoFutbol8> it = eqsClasif.iterator(); it.hasNext();) {
            EquipoFutbol8 eq = it.next();
            if (eq.isAutomatico()){
                pos++;
                continue;
            }
            espectativa = eq.getEspectativa().ordinal();
            // obtenemos la posicion que deberia tener para la espectativa actual
            posSegunEspec = obtenerPosicionSegunEspectativa(eqs.size(), espectativa);
            result = posSegunEspec - pos;
            if (result < 0){
                result = eq.perderContratosPublicitarios(Math.abs(result));
                if (result > 0){
                    String txt = "El " + eq.getNombre() + " ha perdido " + result + " patrocinadore/s";
                    UtilesFutbol8.darNoticia(grp, txt);  
                }
            }
            else if (result > 0){
                result = eq.ganarContratosPublicitarios(result);
                if (result > 0){
                    String txt = "El " + eq.getNombre() + " ha conseguido " + result + " patrocinadore/s";
                    UtilesFutbol8.darNoticia(grp, txt);  
                }
            }
           
            // Modificamos espectativa segun nueva posicion
            // Poderamos la posicion al numero de espectativas           
            int numNuevaEspectativa = obtenerEspectativaSegunPosicion(eqs.size(), pos);
            EspectativaFutbol8 nuevaEspectativa = EspectativaFutbol8.values()[numNuevaEspectativa];
            String txt;
            if (!eq.getEspectativa().equals(nuevaEspectativa)){
                txt = "El " + eq.getNombre() + " ha cambiado su espectativa de " + eq.getEspectativa().name() + " a " + nuevaEspectativa.name();
                eq.setEspectativa(nuevaEspectativa);
            }
            else
                txt = "El " + eq.getNombre() + " mantiene su espectativa a " + eq.getEspectativa().name();
           
            UtilesFutbol8.darNoticia(grp, txt);
            JDBCDAOFutbol8.grabarEquipoFutbol8(eq);
           
            pos++;
        }
     
    }

    private void tratarPerdidaPatrocinador(EquipoFutbol8 eqLocal) throws DAOException {
        
        if (eqLocal.isAutomatico()) return;
        
        int espectativas = eqLocal.getEspectativa().ordinal() + 1;
        boolean quitar = Calculos.obtener(espectativas);   
        if (quitar){    
            int result = eqLocal.perderContratosPublicitarios(1);
            if (result > 0){
                String txt = "El " + eqLocal.getNombre() + " ha perdido 1 patrocinador"; 
                UtilesFutbol8.darNoticia(eqLocal.getClub().getGrupo(), txt);   
            }
            JDBCDAOFutbol8.grabarEquipoFutbol8(eqLocal);
        }           
        
    }

    private void tratarEquiposVacaciones(ArrayList<EquipoFutbol8> eqs) throws DAOException {
        
        VacacionFutbol8 vac;
        
        for (EquipoFutbol8 eq : eqs) {
            
             vac = JDBCDAOFutbol8.obtenerDatosVacacionesFutbol8(eq);
             if (vac == null || !vac.isActivo()) continue;
             
             // Renovacion Jugadores
             if (vac.isRenovacion())
                 for (JugadorFutbol8 jug : eq.getJugadores())
                     if (jug.getContrato() == 1)
                         UtilesFutbol8.renovarJugador(jug, eq);
             
             // Entrenar
             if (vac.isActivarEntreno()){
                 UtilesFutbol8.entrenar(eq, vac.getPosicionEntreno());
             }
                 
             // Tactica
             if (vac.isActivarTactica()){
                 ArrayList<Object> datos = UtilesFutbol8.datosProximoPartido(eq, aplicacion);
                 PartidoFutbol8 proxPartido = (PartidoFutbol8) datos.get(0);
                 if (proxPartido != null){
                     AlineacionFutbol8 ali = JDBCDAOFutbol8.obtenerAlineacionPartido(proxPartido, eq);
                     if (ali != null){
                         ali.setTactica(vac.getTactica());
                         JDBCDAOFutbol8.grabarAlineacionFutbol8(ali);
                     }
                 }
             }
             
        }
    }

    private void tratarConfigFutbol8(Grupo grp) throws DAOException {
        
        ConfigEconomiaFutbol8 conf = JDBCDAOFutbol8.obtenerConfig(grp);
        ArrayList<EquipoFutbol8> eqs = JDBCDAOFutbol8.listaEquiposFutbol8(grp, true);
        
        eqs = EquipoFutbol8.equiposNoAuto(eqs);
        String txt;
        
        if (conf == null){
            // La creamos
            conf = new ConfigEconomiaFutbol8(grp);
            JDBCDAOFutbol8.grabarConfigEconomiaFutbol8(conf);
        }
        
        if (conf.getEquipoGestor() == null || conf.getDiasGestion() >= ConfigEconomia.MAX_DIAS_GESTION){
            // asignamos gestor
            int x = Calculos.valorAleatorio(eqs.size());
            EquipoFutbol8 eqGest = eqs.get(x);
            conf.setEquipoGestor(eqGest);
            conf.setDiasGestion(0);
            txt = "El " + eqGest.getNombre() + " se hace cargo de la administración del grupo";           
            UtilesFutbol8.darNoticia(grp, txt);
            txt = "Hola, has sido elegido para gestionar la administración de tu grupo, esto significa que podras cambiar ciertos parametros, que afectan al desarrollo economico del juego.</br></br>";   
            Correo.getCorreo().enviarMail("ClubDeportivo Gestion Administración Futbol8", 
                    txt, true, eqGest.getClub().getMail());
            
            
        }else{
            conf.setDiasGestion(conf.getDiasGestion() + 1);            
            
        }
        JDBCDAOFutbol8.grabarConfigEconomiaFutbol8(conf);
        
    }
    
    private void tratarImpagoCredito(EquipoFutbol8 eq, int cantidad) throws DAOException{
        
        String txt;
        
        if (eq.getCredito() > 0){
            int cantRetenida = cantidad;
            if (cantRetenida > eq.getCredito())
                cantRetenida = eq.getCredito();
            txt = "El banco retiene la cantidad de " + cantRetenida + " al " +
                    eq.getNombre() + " por la deuda  de credito contraida"; 
            UtilesFutbol8.darNoticia(eq.getClub().getGrupo(), txt);
            Movimiento mov = new Movimiento(eq, ClaseMovimiento.DevolucionCredito, cantRetenida);
            JDBCDAOMovimiento.grabarRegistro(mov);
            eq.setCredito(eq.getCredito() - cantRetenida);
            eq.setPresupuesto(eq.getPresupuesto() - cantRetenida);
            
        }         
        
    }

    private void tratarFinalicionLiga(CompeticionFutbol8 compAct, Grupo grp, ArrayList<EquipoFutbol8> eqs) throws DAOException {
        
        eqs  = EquipoFutbol8.clasificarEquipos(eqs);
        compAct.finalizarLiga(eqs);
        String txt = "EL " + eqs.get(0).getNombre() + " SE HA PROCLAMADO CAMPEON DE LIGA"; 
        UtilesFutbol8.darNoticia(grp, txt);
        
        // cobro del IBI
        tratarCobroIBI(eqs);
        
        // Reparto del dinero de la recaudacion
        int recaudacion = compAct.getRecaudacion();
        
        int premioPrimero = (int) (recaudacion * confEconomica.getPorcentajePremioLiga() / 100);
        int reparto =  (recaudacion - premioPrimero) / EquipoFutbol8.equiposNoAuto(eqs).size();
        
        Movimiento mov;
        mov = new Movimiento(eqs.get(0), ClaseMovimiento.PremioCompeticion, premioPrimero);
        JDBCDAOMovimiento.grabarRegistro(mov);
        txt = "El " + eqs.get(0).getNombre() + " recibe un premio de " + premioPrimero; 
        UtilesFutbol8.darNoticia(grp, txt);
        eqs.get(0).setPresupuesto(eqs.get(0).getPresupuesto() + premioPrimero);
        tratarImpagoCredito(eqs.get(0), premioPrimero); 
        
        for (EquipoFutbol8 eq : eqs) {
            if (!eq.isAutomatico()){
                mov = new Movimiento(eq, ClaseMovimiento.DevolucionHacienda, reparto);
                JDBCDAOMovimiento.grabarRegistro(mov);
                eq.setPresupuesto(eq.getPresupuesto() + reparto);
                tratarImpagoCredito(eq, reparto);  
                JDBCDAOFutbol8.grabarEquipoFutbol8(eq);
            }
        }
        
        for (EquipoFutbol8 eq : eqs) 
            JDBCDAOClub.grabarClub(eq.getClub());
        
        tratarPerdidaPatrocinadores(grp, eqs);   
        JDBCDAOFutbol8.grabarCompeticionFutbol8(compAct);
        
        List<JugadorFutbol8> goleadores = UtilesFutbol8.obtenerGoleadores(grp); 
        // los pasamos a historico
        for (JugadorFutbol8 jug : goleadores) {
            GoleadorFutbol8 newGol = new GoleadorFutbol8(compAct, jug);
            JDBCDAOFutbol8.grabarGoleadorFutbol8(newGol);
        }     
       
        
        ArrayList<JugadorFutbol8> jugsSubirVal = new ArrayList<JugadorFutbol8>();
        int maxGoles = 0;
        for (JugadorFutbol8 jug : goleadores) {
            if (jug.getGolesTemporada() >= maxGoles){
                jugsSubirVal.add(jug);
                maxGoles = jug.getGolesTemporada();
                UtilesFutbol8.darNoticia(grp, "El pichichi de la temporada " + jug.getNombre() + " sube su valoración 5 puntos");
            }
            else break;
        }
        
        List<JugadorFutbol8> porteros = UtilesFutbol8.obtenerPorteros(grp); 
        // los pasamos a historico
        for (JugadorFutbol8 jug : porteros) {
            PorteroFutbol8 newPor = new PorteroFutbol8(compAct, jug);
            JDBCDAOFutbol8.grabarPorteroFutbol8(newPor);
        }

        float ratio = 99;
        for (JugadorFutbol8 jug : porteros) {
            if (jug.getRankingPortero() <= ratio){
                jugsSubirVal.add(jug);
                ratio = jug.getRankingPortero();
                UtilesFutbol8.darNoticia(grp, "El mejor portero de la temporada " + jug.getNombre() + " sube su valoración 5 puntos");
            }
            else break;
        }
        
        for (EquipoFutbol8 eq : eqs){ 
            eq.tratarCaracteristicasJugadoresenUltimaJornada(jugsSubirVal); 
            for (JugadorFutbol8 jug : eq.getJugadores()) {
                JDBCDAOFutbol8.grabarJugadorFutbol8(jug);
            }
        }
    }

    private void tratarFinalicionCopa(CompeticionFutbol8 compAct, Grupo grp, 
            PartidoFutbol8 partido) throws DAOException {
                
        compAct.finalizarCopa(partido);
        JDBCDAOFutbol8.grabarCompeticionFutbol8(compAct);
        JDBCDAOFutbol8.grabarEquipoFutbol8(partido.getGanador());
        JDBCDAOFutbol8.grabarEquipoFutbol8(partido.getPerdedor());
        JDBCDAOClub.grabarClub(partido.getGanador().getClub());
        JDBCDAOClub.grabarClub(partido.getPerdedor().getClub());
        String txt = "EL " + partido.getGanador().getNombre() + " SE HA PROCLAMADO CAMPEON DE COPA"; 
        UtilesFutbol8.darNoticia(grp, txt);
        if (!partido.getGanador().isAutomatico()){
            txt = "El " + partido.getGanador().getNombre() + " ha conseguido un nuevo patrocinador"; 
            UtilesFutbol8.darNoticia(grp, txt);
        }
        int recaudacion = compAct.getRecaudacion();
        
        int premioPrimero = (int) (recaudacion * confEconomica.getPorcentajeCampeonCopa() / 100);
        int premioSegundo = (int) (recaudacion * confEconomica.getPorcentajeSubCampeonCopa() / 100);
        Movimiento mov;
        mov = new Movimiento(partido.getGanador(), ClaseMovimiento.PremioCompeticion, premioPrimero);
        JDBCDAOMovimiento.grabarRegistro(mov);
        txt = "El " + partido.getGanador().getNombre() + " recibe un premio de " + premioPrimero; 
        UtilesFutbol8.darNoticia(grp, txt);
        partido.getGanador().setPresupuesto(partido.getGanador().getPresupuesto() + premioPrimero); 
        tratarImpagoCredito(partido.getGanador(), premioPrimero);
        JDBCDAOFutbol8.grabarEquipoFutbol8(partido.getGanador());
        mov = new Movimiento(partido.getPerdedor(), ClaseMovimiento.PremioCompeticion, premioSegundo);
        JDBCDAOMovimiento.grabarRegistro(mov);
        txt = "El " + partido.getPerdedor().getNombre() + " recibe un premio de " + premioSegundo; 
        UtilesFutbol8.darNoticia(grp, txt);
        partido.getPerdedor().setPresupuesto(partido.getPerdedor().getPresupuesto() + premioSegundo);   
        JDBCDAOFutbol8.grabarEquipoFutbol8(partido.getPerdedor());
        tratarImpagoCredito(partido.getPerdedor(), premioSegundo);
        
    }
    
    
     private static void regularizarCuentas(ArrayList<EquipoFutbol8> eqs) throws DAOException {
        
         ArrayList<Movimiento> movs;
         Movimiento newMov;
         for (EquipoFutbol8 eq : eqs) {
            movs = JDBCDAOMovimiento.obtenerMovimientos(eq);
            for (Movimiento mov : movs) {
                if (mov.isPositivo())
                    eq.setTotalIngresos(eq.getTotalIngresos() + mov.getValor());
                else
                    eq.setTotalGastos(eq.getTotalGastos() + mov.getValor());
            }
            if (!eq.isSaldoCuentasOK()){
                int descuadre = eq.getDescuadreCuentas();
                if (descuadre > 0)
                    newMov = new Movimiento(eq, ClaseMovimiento.RegularizacionNegativa, descuadre);
                else 
                    newMov = new Movimiento(eq, ClaseMovimiento.RegularizacionPositiva, Math.abs(descuadre));
                JDBCDAOMovimiento.grabarRegistro(newMov);
            }
            
        }
    }

    private void tratarCobroIBI(ArrayList<EquipoFutbol8> eqs) throws DAOException {
        
        for (EquipoFutbol8 eq : eqs) {
            
            if (eq.isAutomatico()) continue;
            int pagar = (eq.getCampo() * confEconomica.getIBI() / 100) * eqs.size();            
            Movimiento mov = new Movimiento(eq, ClaseMovimiento.IBI, pagar);
            JDBCDAOMovimiento.grabarRegistro(mov);
            eq.setPresupuesto(eq.getPresupuesto() - pagar);
            compActual.setRecaudacion(compActual.getRecaudacion() + pagar);
            JDBCDAOFutbol8.grabarCompeticionFutbol8(compActual);
            
        }
        
    }

    private boolean comprobarLanzamiento() throws DAOException {
        
        // Si ya hemos disputado jornada en el dia ya no lanzamos
        Date ultimoLanzamiento = JDBCDAOFutbol8.obtenerFechaUltimaJornada();
        
        if (ultimoLanzamiento == null) return true;

        Date hoy = new Date();
        GregorianCalendar calHoy = new GregorianCalendar();
        calHoy.setTime(hoy);
        GregorianCalendar calUtimJor = new GregorianCalendar(); 
        calUtimJor.setTime(ultimoLanzamiento); 
        
        return !(calHoy.get(Calendar.DAY_OF_MONTH) ==  calUtimJor.get(Calendar.DAY_OF_MONTH) &&
                calHoy.get(Calendar.MONTH) ==  calUtimJor.get(Calendar.MONTH) &&
                calHoy.get(Calendar.YEAR) ==  calUtimJor.get(Calendar.YEAR));
        
    }
       
    
}
      
    

