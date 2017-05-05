
package mld.clubdeportivo.controladores;

import java.io.IOException;
import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mld.clubdeportivo.base.*;
import mld.clubdeportivo.base.futbol8.JugadorFutbol8.PosicionComparator;
import mld.clubdeportivo.base.futbol8.*;
import mld.clubdeportivo.bd.*;
import mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8;
import mld.clubdeportivo.utilidades.Correo;
import mld.clubdeportivo.utilidades.StringUtil;
import mld.clubdeportivo.utilidades.UtilGenericas;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Miguel
 */
public class PanelControlFutbol8HttpServlet extends HttpServlet {

    private static Logger logger = 
            LogManager.getLogger(PanelControlFutbol8HttpServlet.class);
   
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!UtilesHttpServlet.comprobarEstado(req, resp)) return;
        
        req.setAttribute("path", "/panelControl/Futbol8/inicio");
        
        String dir = "/PanelControl/panelControlFutbol8.jsp";

        try {

            long id = (Long) req.getSession().getAttribute("idEquipo");
            EquipoFutbol8 eq = JDBCDAOFutbol8.obtenerSimpleEquipoFutbol8(id);
            obtenerListaEquipos(req, eq.getClub().getGrupo());

            String accion = req.getPathInfo();           

            if (accion.equals("/inicio"))
                inicio(req, eq);
            else if (accion.equals("/fichaEquipo")) 
                fichaEquipo(req);
            else if (accion.equals("/gestion"))
                gestion(req, eq);
            else if (accion.equals("/prepararPartido"))
                prepararPartido(req);
            else if (accion.equals("/entrenamiento"))
                entrenamiento(req, eq);
            else if (accion.equals("/entrenador"))
                entrenador(req, eq);
            else if (accion.equals("/cursoEntrenador"))
                cursoEntrenador(req, eq);
            else if (accion.equals("/calendario"))
                calendario(req, eq);
            else if (accion.equals("/clasificacion"))
                clasificacion(req, eq);
            else if (accion.equals("/movimientos"))
                movimientos(req, eq);
            else if (accion.equals("/mercado"))
                mercado(req, eq);
            else if (accion.equals("/plantilla"))
                plantilla(req, eq);
            else if (accion.equals("/jugador"))
                jugador(req, eq);
            else if (accion.equals("/partido"))
                partido(req);
            else if (accion.equals("/goleadores"))
                goleadores(req, eq);
            else if (accion.equals("/porteros"))
                porteros(req, eq);
            else if (accion.equals("/competiciones"))
                competiciones(req, eq, true);
            else if (accion.equals("/competicionesGrupos"))
                competiciones(req, eq, false);
            else if (accion.equals("/noticias"))
                noticias(req, eq.getClub().getGrupo());
            else if (accion.equals("/comentarios"))
                comentarios(req, eq.getClub());
            else if (accion.equals("/auditoria"))
                auditoria(req, eq.getClub().getGrupo()); 
            else if (accion.equals("/estadistica"))
                estadistica(req, eq);  
            else if (accion.equals("/administracion"))
                administracion(req, eq); 


        } catch (DAOException ex) {
            logger.error(ex.getMessage());
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("errorDes", UtilGenericas.pilaError(ex));
            dir = "/Utiles/error.jsp";
            
             //Aqui gestionar pagina de error
        } catch (Exception ex) {
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("errorDes", UtilGenericas.pilaError(ex));
            dir = "/Utiles/error.jsp";
             //Aqui gestionar pagina de error
        }
        
             
        RequestDispatcher view = req.getRequestDispatcher(dir);
        view.forward(req, resp);
       
    }

   
    private HttpServletRequest inicio(HttpServletRequest req, EquipoFutbol8 eq)
            throws DAOException{

        req.setAttribute("op", "inicio");
        req.setAttribute("titulo", "FUTBOL8");

        req.setAttribute("equipo", eq);
        
        ArrayList<Noticia> noticias = 
                JDBCDAONoticia.obtenerNoticias(eq.getClub().getGrupo(), Deporte.Futbol8, 10);
        
        long idJornada = 0;
                
        PartidoFutbol8 ultimoPartido = JDBCDAOFutbol8.obtenerUltimoPartido(eq);
        if (ultimoPartido != null){
            idJornada = JDBCDAOFutbol8.idJornada(ultimoPartido);
        }        
           
        String proximoTipoComp = "";
        
        ArrayList<Object> datos = UtilesFutbol8.datosProximoPartido(eq, req.getServletContext());
        PartidoFutbol8 proximoPartido = (PartidoFutbol8) datos.get(0);
        CompeticionFutbol8 comp = (CompeticionFutbol8) datos.get(1);
        if (comp != null) proximoTipoComp = comp.getClase();
        
        req.setAttribute("ultimoPartido", ultimoPartido);
        req.setAttribute("proximoPartido", proximoPartido);
        req.setAttribute("proximoTipoComp", proximoTipoComp);
        req.setAttribute("ultimaJornada", idJornada);
        req.setAttribute("noticias", noticias);
        req.setAttribute("existeComp", comp != null);
        
        logger.info("existeComp = " + comp);
        
        UtilesHttpServlet.tratarComentarios(req, eq.getClub(), false);   
               
        return req;

    }
    
    
    
    private HttpServletRequest partido(HttpServletRequest req)
            throws DAOException{

        req.setAttribute("op", "partido");
        req.setAttribute("titulo", "INFORME PARTIDO");
        
        try {
            long id = Long.parseLong(req.getParameter("id"));
            PartidoFutbol8 partido = JDBCDAOFutbol8.obtenerPartido(id);
            EstadisticaPartidoFutbol8 est = JDBCDAOFutbol8.obtenerEstadisticaPartido(partido);
            
            ArrayList<JugadorFutbol8> aliEqLocal = 
                    AlineacionFutbol8.parseAlineacion(est.getAlineacionLocal());
            ArrayList<JugadorFutbol8> aliEqVisitante = 
                    AlineacionFutbol8.parseAlineacion(est.getAlineacionVisitante());
            
            int max = Math.max(aliEqLocal.size(), aliEqVisitante.size());
            
            ArrayList<EstadisticaAlineacionFutbol8> estAli = new ArrayList<EstadisticaAlineacionFutbol8>();
            for (int i = 0; i < max; i++){
                EstadisticaAlineacionFutbol8 e = new EstadisticaAlineacionFutbol8();
                if (aliEqLocal.size() > i){
                    e.setJugadorLocal(aliEqLocal.get(i).getNombre());
                    e.setPosicionLocal(aliEqLocal.get(i).getPosicion().name());
                    e.setValoracionLocal(String.valueOf(aliEqLocal.get(i).getValoracionReal()));
                    e.setValoracionPartidoLocal(String.valueOf(aliEqLocal.get(i).getValoracionPartido()));
                }else{
                    e.setJugadorLocal("");
                    e.setPosicionLocal("");
                    e.setValoracionLocal("0");
                    e.setValoracionPartidoLocal("0");
                }
                
                if (aliEqVisitante.size() > i){
                    e.setJugadorVisitante(aliEqVisitante.get(i).getNombre());
                    e.setPosicionVisitante(aliEqVisitante.get(i).getPosicion().name());
                    e.setValoracionVisitante(String.valueOf(aliEqVisitante.get(i).getValoracionReal()));
                    e.setValoracionPartidoVisitante(String.valueOf(aliEqVisitante.get(i).getValoracionPartido()));
                }else{
                    e.setJugadorVisitante("");
                    e.setPosicionVisitante("");
                    e.setValoracionVisitante("0");
                    e.setValoracionPartidoVisitante("0");
                }
                estAli.add(e);
            }
            
            req.setAttribute("partido", partido);
            req.setAttribute("jugsEqLocal", aliEqLocal);
            req.setAttribute("jugsEqVisitante", aliEqVisitante);
            req.setAttribute("alineacion", estAli);
            req.setAttribute("estadistica", est);
            req.setAttribute("entrenadorEqLocal", 
                    AlineacionFutbol8.parseEntrenador(est.getAlineacionLocal()));
            req.setAttribute("entrenadorEqVisitante", 
                    AlineacionFutbol8.parseEntrenador(est.getAlineacionVisitante()));
        }
        catch(Exception ex){
            req.setAttribute("error", "Lo sentimos no es posible mostrar este partido");
        }
         
         return req;
    }
    

    private void obtenerListaEquipos(HttpServletRequest req,
            Grupo grupo) throws DAOException{

        ArrayList equiposGrupo = JDBCDAOFutbol8.listaEquiposFutbol8(grupo);

        req.setAttribute("equiposGrupo", equiposGrupo);

    }

  
  
    private void fichaEquipo(HttpServletRequest req) throws DAOException {

        req.setAttribute("op", "fichaEquipo");
        req.setAttribute("titulo", "FICHA EQUIPO");

        long idEquipo = Long.valueOf(req.getParameter("id"));

        EquipoFutbol8 eq = JDBCDAOFutbol8.obtenerSimpleEquipoFutbol8(idEquipo);
        req.setAttribute("equipoFicha", eq);
        req.setAttribute("jugadoresFicha", eq.getJugadores());
        req.setAttribute("entrenadorFicha", eq.getEntrenador());
        obtenerJuvenil(req, eq);


    }

   
    private void gestion(HttpServletRequest req, EquipoFutbol8 eq) throws DAOException {
        
        req.setAttribute("op", "gestion");
        req.setAttribute("titulo", "GESTION DEL EQUIPO");
        req.setAttribute("equipo", eq);
        ArrayList<EquipoFutbol8> eqsGrupo = 
                JDBCDAOFutbol8.listaEquiposFutbol8(eq.getClub().getGrupo());
        ArrayList<EquipoFutbol8> eqsPrima = new ArrayList<EquipoFutbol8>();
        for (EquipoFutbol8 eqf8 : eqsGrupo) 
            if (!eqf8.equals(eq)) eqsPrima.add(eqf8);
        
        req.setAttribute("equiposPrima", eqsPrima);
        req.setAttribute("tiposCompeticion", CompeticionFutbol8.clasesCompeticion);
        req.setAttribute("preciosEntrada", Seccion.PRECIOS_ENTRADAS);
        
        req.setAttribute("posiciones", UtilesFutbol8.posicionesJuvenil());
        String posJuvenil = eq.getPosicionJuvenil().name();
        req.setAttribute("posicionJuvenil", posJuvenil);
        req.setAttribute("tacticas", eq.getEntrenador().getTacticas());

        String op = (String) req.getParameter("operacion");
        Movimiento mov;
        int precio;
        
        // Modo vacaciones
        VacacionFutbol8 vac = JDBCDAOFutbol8.obtenerDatosVacacionesFutbol8(eq);        
        if (vac == null){
            vac = new VacacionFutbol8(eq);
            JDBCDAOFutbol8.grabarVacacionFutbol8(vac);
        }
        vac.setEquipo(eq);
                
        try{
            if (op == null) {
            }// no hacemos nada
            else if (op.equals("Ampliar Asientos")) {
                precio = eq.ampliarCampo();
                mov = new Movimiento(eq, ClaseMovimiento.AmpliacionEstadio, precio);
                JDBCDAOMovimiento.grabarRegistro(mov);
            }
            else if (op.equals("Firmar Contrato")) {
                precio = eq.ampliarPublicidad();
                mov = new Movimiento(eq, ClaseMovimiento.Gestion, precio);
                JDBCDAOMovimiento.grabarRegistro(mov);
            }
            else if (op.equals("Nuevo Tecnico")) {
                precio = eq.ampliarEqTecnico();
                mov = new Movimiento(eq, ClaseMovimiento.Gestion, precio);
                JDBCDAOMovimiento.grabarRegistro(mov);
            }
            else if (op.equals("Nuevo Ojeador")) {
                precio = eq.ampliarOjeadores();
                mov = new Movimiento(eq, ClaseMovimiento.Gestion, precio);
                JDBCDAOMovimiento.grabarRegistro(mov);
            }
            else if (op.equals("Invertir")) {
                int cantidad = Integer.parseInt(req.getParameter("invBolsa"));
                if (cantidad > 0){
                    eq.invertirBolsa(cantidad);
                    mov = new Movimiento(eq, ClaseMovimiento.InversionBolsa, cantidad);
                    JDBCDAOMovimiento.grabarRegistro(mov);
                }
            }
            else if (op.equals("Rescatar")) {
                int cantidad = eq.rescatarBolsa();
                if (cantidad > 0){
                    mov = new Movimiento(eq, ClaseMovimiento.RescateBolsa, cantidad);
                    JDBCDAOMovimiento.grabarRegistro(mov);
                }
            }
            else if (op.equals("Pedir")) {
                int cantidad = Integer.parseInt(req.getParameter("valorCredito"));
                if (cantidad > 0){
                    eq.pedirCredito(cantidad);
                    mov = new Movimiento(eq, ClaseMovimiento.ConcesionCredito, cantidad);
                    JDBCDAOMovimiento.grabarRegistro(mov);
                }                    
            }
            else if (op.equals("Devolver")) {
                int cantidad = Integer.parseInt(req.getParameter("valorCredito"));
                if (cantidad > 0){
                    cantidad = eq.devolverCredito(cantidad);
                    mov = new Movimiento(eq, ClaseMovimiento.DevolucionCredito, cantidad);
                    JDBCDAOMovimiento.grabarRegistro(mov);
                }
            }
            else if (op.equals("Cambiar Precio")) {
                int cantidad = Integer.parseInt(req.getParameter("precioEntrada"));
                eq.setPrecioEntradas(cantidad);                
            }
            else if (op.equals("Cambiar Nombre")) {
                String nom = req.getParameter("nombreEstadio");
                eq.setNombreCampo(nom);               
            }
            else if (op.equals("Cambiar Posicion")) {
                PosicionElegidaFutbol8 pos = PosicionElegidaFutbol8.valueOf(req.getParameter("posicion"));
                eq.setPosicionJuvenil(pos);  
                req.setAttribute("posicionJuvenil", pos);
            }
            else if (op.equals("Cambiar")) {
                int equipacion = Integer.parseInt(req.getParameter("equipacion"));
                eq.setEquipacion(equipacion);                
            }
            else if (op.equals("Primar")) {
                try {
                    int cantidad = Integer.parseInt(req.getParameter("prima"));
                    if (cantidad > 0){
                        if (eq.getPresupuesto() < cantidad) 
                            throw new UnsupportedOperationException("No tienes tanto presupuesto");
                        long idEqAPrimar = Long.parseLong(req.getParameter("equipoPrima"));
                        String claseComp = req.getParameter("tipoCompeticion");
                        EquipoFutbol8 eqAPrimar = JDBCDAOFutbol8.obtenerSimpleEquipoFutbol8(idEqAPrimar); 
                        CompeticionFutbol8 comp = JDBCDAOFutbol8.competicionActiva(eq.getClub().getGrupo(), claseComp);                        
                        PartidoFutbol8 partido = JDBCDAOFutbol8.obtenerProximoPartido(comp, eqAPrimar);
                        if (partido == null) throw new UnsupportedOperationException("No hay ningun partido previsto");                        
                        AlineacionFutbol8 alin = JDBCDAOFutbol8.obtenerAlineacionPartido(partido, eqAPrimar);
                        alin.setPrima(cantidad, true);
                        JDBCDAOFutbol8.grabarAlineacionFutbol8(alin);
                        eq.setPresupuesto(eq.getPresupuesto() - cantidad);                        
                        mov = new Movimiento(eq, ClaseMovimiento.Primas, cantidad);
                        JDBCDAOMovimiento.grabarRegistro(mov);
                    }
                } catch(Exception ex){
                    req.setAttribute("errorPrimas", ex.getMessage());
                }
            }
            else if (op.equals("Activar")) {
                op = null;  
                vac.setActivo(true);
                JDBCDAOFutbol8.grabarVacacionFutbol8(vac);
            }
            else if (op.equals("Desactivar")) {
                op = null;     
                vac.setActivo(false);
                JDBCDAOFutbol8.grabarVacacionFutbol8(vac);
            }            
            else if (op.equals("Grabar Ajustes")) {
                op = null;
                boolean activarRenovacion,fijarTactica,entrenoAuto;
                if (req.getParameter("activarRenovacion") != null && 
                        req.getParameter("activarRenovacion").equals("on")) activarRenovacion = true;
                else activarRenovacion = false;
                vac.setRenovacion(activarRenovacion);
                if (req.getParameter("fijarTactica") != null && 
                        req.getParameter("fijarTactica").equals("on")) fijarTactica = true;
                else fijarTactica = false;
                vac.setActivarTactica(fijarTactica);
                if (req.getParameter("entrenoAuto") != null && 
                        req.getParameter("entrenoAuto").equals("on")) entrenoAuto = true;
                else entrenoAuto = false;
                vac.setActivarEntreno(entrenoAuto); 
                vac.setTactica(TacticaFutbol8.tacticaFutbol8(Integer.parseInt(req.getParameter("tactica"))));
                vac.setPosicionEntreno(PosicionElegidaFutbol8.valueOf(req.getParameter("posicionEntreno")));
                JDBCDAOFutbol8.grabarVacacionFutbol8(vac);
            }

            if (op != null){
                JDBCDAOFutbol8.grabarEquipoFutbol8((EquipoFutbol8) eq);
            }
          
            req.setAttribute("vacaciones", vac);
            
        }
        catch (Exception ex) {
            req.setAttribute("error", ex.getMessage());
        }
            

    }

    private void prepararPartido(HttpServletRequest req) throws DAOException {

        req.setAttribute("op", "prepararPartido");
        req.setAttribute("titulo", "PREPARAR PROXIMO PARTIDO");
        
        String panel = req.getParameter("panel");
        if (panel == null) panel = "miEquipo";
        req.setAttribute("panel", panel);
        
        TacticaFutbol8 tacSeleccionada;
        TacticaFutbol8 tacSeleccionadaSimul;
        HttpSession sesion = req.getSession(); 
        boolean unPartido = false;
        boolean resultados = false;
        boolean tiros = false;
        TacticaFutbol8 tacSimul;
        boolean simulRandom = false;
        EquipoFutbol8 eqSimul = null;
        
        long idEquipo = (Long) req.getSession().getAttribute("idEquipo");
        
        EquipoFutbol8 eq = JDBCDAOFutbol8.obtenerSimpleEquipoFutbol8(idEquipo);
       
        ArrayList<Object> datos = UtilesFutbol8.datosProximoPartido(eq, req.getServletContext());
        CompeticionFutbol8 comp = (CompeticionFutbol8) datos.get(1);        
        PartidoFutbol8 partido = null;
        
        if (comp != null)
            partido = JDBCDAOFutbol8.obtenerDatosPrepararPartido(comp, eq, eqSimul);
        
        if (partido == null) return;
        
        // Si no existe equipo simulado lo creamos
        if (sesion.getAttribute("equipoSimul") == null){
             if (partido.getEqLocal().getId() != idEquipo)
                eqSimul = (EquipoFutbol8) partido.getEqLocal();
            else
                eqSimul = (EquipoFutbol8) partido.getEqVisitante();
            
            eqSimul.limpiarJugadoresQueJuegan();
            
            AlineacionFutbol8 ali = new AlineacionFutbol8(eqSimul, partido);
            eqSimul.setAlineacion(ali);                
            
            tacSimul = eqSimul.getEntrenador().getTacticaAleatoria();                
            eqSimul.getAlineacion().setTactica(tacSimul); 
            eqSimul.hacerAlineacionAutomatica(tacSimul, true);
            
            sesion.setAttribute("equipoSimul", eqSimul);
                  
        }
        else 
            eqSimul = (EquipoFutbol8) sesion.getAttribute("equipoSimul");
            
        
        String op = (String) req.getParameter("operacion");
        
        // Operaciones con mi Equipo
        if (panel.equals("miEquipo")){
            
            String numTac = (String) req.getParameter("tacsel");
            if (numTac == null){
                if (eq.getAlineacion().getTactica() != null)
                    tacSeleccionada = eq.getAlineacion().getTactica();
                else
                    tacSeleccionada = eq.getEntrenador().getTacticas().get(0);
            }else{
                tacSeleccionada = eq.getEntrenador().getTactica(Integer.parseInt(numTac));
                limpiarAlineacion(eq);
            }            
            eq.getAlineacion().setTactica(tacSeleccionada);
            JDBCDAOFutbol8.grabarAlineacionFutbol8(eq.getAlineacion());
            
            if (op == null) {
            }// no hacemos nada
            else if (op.equals("Grabar")) {
                
                String alineacion = (String) req.getParameter("alineacion");
                grabarAlineacion(alineacion, eq, true);
                String esfuerzo = (String) req.getParameter("esfuerzo");
                eq.getAlineacion().setEsfuerzo(EsfuerzoFutbol8.valueOf(esfuerzo));
                String estrategia = (String) req.getParameter("estrategia");
                eq.getAlineacion().setEstrategia(EstrategiaFutbol8.valueOf(estrategia));
                String primas = (String) req.getParameter("primas");
                if (StringUtil.isNumero(primas)){
                    int num = Integer.parseInt(primas);
                    if (num <= 0){}
                    else if (num > 0 && eq.getPresupuesto() < num)
                        req.setAttribute("error", "No tienes suficiente presupuesto para primar");
                    else{
                        try {
                            eq.getAlineacion().setPrima(num, false);
                            eq.setPresupuesto(eq.getPresupuesto() - num);
                            JDBCDAOFutbol8.grabarEquipoFutbol8(eq);
                            Movimiento mov = new Movimiento(eq, ClaseMovimiento.Primas, num);
                            JDBCDAOMovimiento.grabarRegistro(mov);                      
                        }catch(Exception e){
                            req.setAttribute("error", e.getMessage());
                        }
                    }
                }                    
                JDBCDAOFutbol8.grabarAlineacionFutbol8(eq.getAlineacion());
                
            }
            else if (op.equals("Alineacion Automatica")) {
                eq.hacerAlineacionAutomatica(tacSeleccionada, true);
                for (JugadorFutbol8 jug : eq.getJugadores()) {
                    JDBCDAOFutbol8.grabarJugadorFutbol8(jug);
                }                
                JDBCDAOFutbol8.grabarAlineacionFutbol8(eq.getAlineacion());
            }               
            
            req.setAttribute("esfuerzo", eq.getAlineacion().getEsfuerzo().name());
            req.setAttribute("estrategia", eq.getAlineacion().getEstrategia().name());
            req.setAttribute("primas", eq.getAlineacion().getPrimas());
            req.setAttribute("equipo", eq);
            req.setAttribute("porteros", eq.getPorterosAliniables());
            req.setAttribute("jugadores", eq.getJugadoresAliniables());
            req.setAttribute("jugadoresLesionados", eq.getJugadoresLesionados());
            req.setAttribute("jugadoresSancionados", eq.getJugadoresSancionados());
            req.setAttribute("tacSeleccionada", tacSeleccionada);
            String alineacionTxt = eq.getAlineacion().getAlineacionCodificada();
            String noJuegan = AlineacionFutbol8.codificarJugadores(eq.getJugadoresBanquillo());
            if (!alineacionTxt.isEmpty() && !noJuegan.isEmpty())                
                alineacionTxt = alineacionTxt + ";";
            alineacionTxt = alineacionTxt + noJuegan;
            req.setAttribute("alineacionTxt", alineacionTxt);
                        
        }
        else if (panel.equals("eqRival")){
            
            if (sesion.getAttribute("equipoSimul") != null){
                eqSimul = (EquipoFutbol8) sesion.getAttribute("equipoSimul");            
                String numTacSimul = (String) req.getParameter("tacsel");
                if (numTacSimul != null)
                    if (Integer.parseInt(numTacSimul) != (eqSimul.getAlineacion().getTactica().getNumero())){
                        eqSimul.getAlineacion().setTactica(TacticaFutbol8.tacticaFutbol8(Integer.parseInt(numTacSimul)));
                        simulRandom = true;
                    }
            }
            
            if (op == null) {
            }// no hacemos nada
            else if (op.equals("Grabar Simulacion")) {
                
                String alineacion = (String) req.getParameter("alineacion");
                grabarAlineacion(alineacion, eqSimul, false);
                String esfuerzo = (String) req.getParameter("esfuerzoSimul");
                eqSimul.getAlineacion().setEsfuerzo(EsfuerzoFutbol8.valueOf(esfuerzo));
                String estrategia = (String) req.getParameter("estrategiaSimul");
                eqSimul.getAlineacion().setEstrategia(EstrategiaFutbol8.valueOf(estrategia));
                String primas = (String) req.getParameter("primasSimul");
                if (StringUtil.isNumero(primas)){
                    int num = Integer.parseInt(primas);                     
                    eqSimul.getAlineacion().setPrima(num, false);                
                }      
                sesion.setAttribute("equipoSimul", eqSimul);
            }    
            
            //eqSimul.limpiarJugadoresQueJuegan();
            String numTacSimul = (String) req.getParameter("tacsel");
            partido.getAlineaciones().add(eqSimul.getAlineacion());
            
            if (numTacSimul != null) 
                tacSeleccionadaSimul = eqSimul.getEntrenador().getTactica(Integer.parseInt(numTacSimul));
            else 
                tacSeleccionadaSimul = eqSimul.getAlineacion().getTactica();
            
            eqSimul.getAlineacion().setTactica(tacSeleccionadaSimul);
            if (simulRandom) 
                eqSimul.hacerAlineacionAutomatica(tacSeleccionadaSimul, true);
            else
                eqSimul.marcarJugadoresAliniados();
            
            req.setAttribute("esfuerzoSimul", eqSimul.getAlineacion().getEsfuerzo().name());
            req.setAttribute("estrategiaSimul", eqSimul.getAlineacion().getEstrategia().name());
            req.setAttribute("primasSimul", eqSimul.getAlineacion().getPrimas());
            req.setAttribute("equipo", eqSimul);
            req.setAttribute("porteros", eqSimul.getPorterosAliniables());
            req.setAttribute("jugadores", eqSimul.getJugadoresAliniables());
            req.setAttribute("jugadoresLesionados", eqSimul.getJugadoresLesionados());
            req.setAttribute("jugadoresSancionados", eqSimul.getJugadoresSancionados());
            req.setAttribute("tacSeleccionada", tacSeleccionadaSimul);  
            String alineacionTxt = eqSimul.getAlineacion().getAlineacionCodificada();
            String noJuegan = AlineacionFutbol8.codificarJugadores(eqSimul.getJugadoresBanquillo());
            if (!alineacionTxt.isEmpty() && !noJuegan.isEmpty())                
                alineacionTxt = alineacionTxt + ";";
            alineacionTxt = alineacionTxt + noJuegan;
            req.setAttribute("alineacionTxt", alineacionTxt);
            
            
        }
        else if (panel.equals("simular")){
            
            if (op == null) {
            }// no hacemos nada
            
            else if (op.equals("Jugar un Partido")) { 
                partido = JDBCDAOFutbol8.obtenerDatosPrepararPartido(comp, eq, eqSimul);
                EstadisticaPartidoFutbol8 est = new EstadisticaPartidoFutbol8(partido);
                partido.setEstadistica(est);
                partido.setTipo(comp.getClase());
                partido.setIsSimulacion(true);
                partido.jugarPartido();  
                // Pruebas de penalties
                // if (partido.getGanador() == null)
                //    partido.hacerTandaPenalties();
                sesion.setAttribute("estadistica", est);
                unPartido = true;
                
                ArrayList<JugadorFutbol8> jugsLocal = 
                        partido.getAlineacionEqLocal().jugadores();
                ArrayList<JugadorFutbol8> jugsVisitante = 
                        partido.getAlineacionEqVisitante().jugadores();
                
                req.setAttribute("jugsEqLocal", jugsLocal);
                req.setAttribute("jugsEqVisitante", jugsVisitante);
                req.getSession().setAttribute("cronica", partido.getCronica());
                
            }
            else if (op.equals("Calcular Resultados")) {
                
                partido = JDBCDAOFutbol8.obtenerDatosPrepararPartido(comp, eq, eqSimul);
                int victorias = 0; int empates = 0; int derrotas = 0;
                partido.setTipo(comp.getClase());
                partido.setIsSimulacion(true);
                for (int i = 1; i <= PartidoFutbol8.NUMERO_SIMULACIONES; i++){                
                    partido.jugarPartido();
                    if (partido.getGanador() == null) empates++;
                    else if (partido.getGanador().equals(eq)) victorias++;
                    else derrotas++;                    
                }   
                req.setAttribute("vistoriasSilum", victorias);
                req.setAttribute("empatesSilum", empates);
                req.setAttribute("derrotasSilum", derrotas);
                resultados = true;
            }
            else if (op.equals("Simular Tiros")) {
                
                partido = JDBCDAOFutbol8.obtenerDatosPrepararPartido(comp, eq, eqSimul);
                partido.setTipo(comp.getClase());
                partido.setIsSimulacion(true);
                partido.simulacionTiros();
                ArrayList<JugadorFutbol8> tirJugs = new ArrayList<JugadorFutbol8>();
                tirJugs.addAll(partido.getAlineacionEqLocal().jugadoresCampo());
                tirJugs.addAll(partido.getAlineacionEqVisitante().jugadoresCampo());
                Collections.sort(tirJugs, new PosicionComparator());
                
                ArrayList<JugadorFutbol8> parJugs = new ArrayList<JugadorFutbol8>();
                parJugs.add(partido.getAlineacionEqLocal().getPortero());
                parJugs.add(partido.getAlineacionEqVisitante().getPortero());
                
                req.setAttribute("tirJugs", tirJugs);
                req.setAttribute("parJugs", parJugs);
                tiros = true;
            }
            
            req.setAttribute("unPartido", unPartido);
            req.setAttribute("resultados", resultados);
            req.setAttribute("tiros", tiros);

            
        }
        
        req.setAttribute("partido", partido);

    }
   

    private void calendario(HttpServletRequest req, EquipoFutbol8 eq) 
            throws DAOException {

        req.setAttribute("op", "calendario"); 
        req.setAttribute("titulo", "CALENDARIO");
        
        String idComp = req.getParameter("idComp");
        String idJornada = req.getParameter("id");
        
        Grupo grp = eq.getClub().getGrupo();
        
        if (idJornada != null){
            long id = Long.parseLong(idJornada);
            JornadaFutbol8 jornada = 
                    JDBCDAOFutbol8.obtenerJornada(id, grp);
            req.setAttribute("jornada", jornada);
            req.setAttribute("compJornada", jornada.getCompeticion());
        }
        else
        {
            CompeticionFutbol8 comp;
            if (idComp != null){
                long id = Long.parseLong(idComp);
                comp = JDBCDAOFutbol8.obtenerCompeticion(id);
            }
            else{
                String tipo = req.getParameter("tipo");            
                comp = JDBCDAOFutbol8.competicionActiva(grp, tipo);
            }
          
            
            if (comp != null){
                
                comp = JDBCDAOFutbol8.obtenerDatosHistoCompeticion(comp);
                ArrayList<JornadaFutbol8> jornadas = comp.getJornadas();
                
                ArrayList<JornadaFutbol8> jornadasIda = new ArrayList<JornadaFutbol8>();
                ArrayList<JornadaFutbol8> jornadasVuelta = new ArrayList<JornadaFutbol8>();
                JornadaFutbol8 jornadaFinal = null;
                if (jornadas.size() == 1)
                    jornadasIda = jornadas;
                else{                           
                    if (comp.getClase().equals("Liga")){
                        jornadasIda.addAll(jornadas.subList(0, (jornadas.size() / 2)));
                        jornadasVuelta.addAll(jornadas.subList(jornadas.size() / 2, jornadas.size()));                
                    }
                    else if (comp.getClase().equals("Copa")){
                        for (int i = 0; i < jornadas.size() - 1; i = i + 2) {
                            jornadasIda.add(jornadas.get(i));
                            jornadasVuelta.add(jornadas.get(i + 1));
                        }
                        jornadaFinal = jornadas.get(jornadas.size()-1);
                    }                        
                }
                
                
                req.setAttribute("competicion", comp);
                req.setAttribute("jornadasida", jornadasIda);
                req.setAttribute("jornadasvuelta", jornadasVuelta);
                req.setAttribute("jornadaFinal", jornadaFinal);  
                req.setAttribute("nombreEquipo", eq.getNombre());
            }
        }

    }
    
     private void estadistica(HttpServletRequest req, EquipoFutbol8 eq) throws DAOException {
         
        req.setAttribute("op", "estadistica");    
        req.setAttribute("titulo", "ESTADISTICA"); 
        
        String idComp = req.getParameter("comp");         
        
        CompeticionFutbol8 comp;
         
        if (idComp != null && !idComp.equals("")){
            long id = Long.parseLong(idComp);
            comp = JDBCDAOFutbol8.obtenerCompeticion(id);
        }
        else{
            comp = JDBCDAOFutbol8.competicionActiva(eq.getClub().getGrupo(), "Liga");
        }
        
        if (comp != null)
            req.setAttribute("comp", comp.getId());
        
        String idEq = req.getParameter("equipoSeleccionado");
        if (idEq != null && !idEq.equals("")){
            eq = JDBCDAOFutbol8.obtenerSimpleEquipoFutbol8(Integer.parseInt(idEq));
        }   
        
        if (eq != null)
            req.setAttribute("equipoSeleccionado", eq.getId());
        
        ArrayList<EquipoFutbol8> eqs = JDBCDAOFutbol8.listaEquiposFutbol8(eq.getClub().getGrupo(), true);
        ArrayList<EstadisticaResultadosFutbol8> partidos = new ArrayList<EstadisticaResultadosFutbol8>();
        HashSet<EquipoFutbol8> equiposSeleccion = new HashSet<EquipoFutbol8>();
        if (comp != null && eq != null){            
            comp = JDBCDAOFutbol8.obtenerDatosHistoCompeticion(comp);
            ArrayList<JornadaFutbol8> jornadas = comp.getJornadas();
            for (JornadaFutbol8 jor : jornadas) {
                if (jor.isDisputada()){
                    for (PartidoFutbol8 partido : jor.getPartidos()) {                        
                            EstadisticaPartidoFutbol8 est = partido.getEstadistica();
                            if (eq.getNombre().equals(est.getEqLocal()) || eq.getNombre().equals(est.getEqVisitante())){
                                EstadisticaResultadosFutbol8 resul = new EstadisticaResultadosFutbol8(est, eq);
                                partidos.add(resul);
                            }
                            for (EquipoFutbol8 equipo : eqs) {
                                if (equipo.getNombre().equals(est.getEqLocal()) || equipo.getNombre().equals(est.getEqVisitante()))
                                    equiposSeleccion.add(equipo);
                            }
                    }
                }
            }
        }
        
        req.setAttribute("seleccionEquipos", equiposSeleccion);
        req.setAttribute("partidos", partidos);
        req.setAttribute("mediaGanados", EstadisticaResultadosFutbol8.getMediaGanados(partidos));
        req.setAttribute("mediaEmpatados", EstadisticaResultadosFutbol8.getMediaEmpatados(partidos));
        req.setAttribute("mediaPerdidos", EstadisticaResultadosFutbol8.getMediaPerdidos(partidos));
        req.setAttribute("mediaOpcionesGanar", EstadisticaResultadosFutbol8.getMediaOpcionesGanar(partidos));
        req.setAttribute("mediaOpcionesEmpatar", EstadisticaResultadosFutbol8.getMediaOpcionesEmpatar(partidos));
        req.setAttribute("mediaOpcionesPerder", EstadisticaResultadosFutbol8.getMediaOpcionesPerder(partidos));
        
        
        
    }


     private void goleadores(HttpServletRequest req, EquipoFutbol8 eq) 
            throws DAOException {

        req.setAttribute("op", "goleadores");    
        req.setAttribute("titulo", "GOLEADORES");
        
        
        String idComp = (String) req.getParameter("idComp");
        Grupo grp = eq.getClub().getGrupo();         
        
        if (idComp != null && !idComp.isEmpty()){
            List<GoleadorFutbol8> goleadores;
            long id = Long.parseLong(idComp);
            CompeticionFutbol8 comp = JDBCDAOFutbol8.obtenerCompeticion(id);
            goleadores = JDBCDAOFutbol8.obtenerGoleadores(comp); 
            req.setAttribute("goleadores", goleadores);
        }
        else{
            List<JugadorFutbol8> goleadores;
            goleadores = UtilesFutbol8.obtenerGoleadores(grp); 
            req.setAttribute("goleadores", goleadores);
        }
        
               
        
        req.setAttribute("grupo", grp.getNombre()); 
        
     }
     
     private void porteros(HttpServletRequest req, EquipoFutbol8 eq) throws DAOException {
        
         req.setAttribute("op", "porteros");    
         req.setAttribute("titulo", "PORTEROS");
                
         String idComp = (String) req.getParameter("idComp");
         Grupo grp = eq.getClub().getGrupo();         
        
         if (idComp != null && !idComp.isEmpty()){
             List<PorteroFutbol8> porteros;
             long id = Long.parseLong(idComp);
             CompeticionFutbol8 comp = JDBCDAOFutbol8.obtenerCompeticion(id);
             porteros = JDBCDAOFutbol8.obtenerPorteros(comp); 
             req.setAttribute("porteros", porteros);
         }
         else{
             List<JugadorFutbol8> porteros;
             porteros = UtilesFutbol8.obtenerPorteros(grp); 
             req.setAttribute("porteros", porteros);
         }
                  
         
         req.setAttribute("grupo", grp.getNombre()); 
    }
   
    private void clasificacion(HttpServletRequest req, EquipoFutbol8 eq)
            throws DAOException {

        req.setAttribute("op", "clasificacion");
        req.setAttribute("titulo", "CLASIFICACION");

        Grupo grp = eq.getClub().getGrupo();
        
        String idComp = (String) req.getParameter("comp");

        
        ArrayList<EquipoFutbol8> eqs;
        if (idComp == null)
            eqs = JDBCDAOFutbol8.obtenerClasificacion(grp);
        else{
            CompeticionFutbol8 comp = JDBCDAOFutbol8.competicion(Long.parseLong(idComp));
            eqs = JDBCDAOFutbol8.obtenerClasificacion(comp);
        }
        req.setAttribute("equipos", eqs);

        
    }

    private void movimientos(HttpServletRequest req, EquipoFutbol8 eq)
            throws DAOException {

        req.setAttribute("op", "movimientos");
        req.setAttribute("titulo", "MOVIMIENTOS");
        
        ArrayList<Movimiento> movs = new ArrayList<Movimiento>();
        String op = (String) req.getParameter("operacion");
        boolean todo = false;
        
        if (op == null || op.equals("Ver ultimos 30 dias")) {
            // por defecto mostramos los ultimos 30 dias
            GregorianCalendar c = new GregorianCalendar();        
            Date diaActual = new Date();
            c.setGregorianChange(diaActual);
            c.add(Calendar.DAY_OF_YEAR, 1);
            diaActual = c.getTime();
            c.add(Calendar.DAY_OF_YEAR, -30);
            Date diaInicial = c.getTime();            
            movs = JDBCDAOMovimiento.obtenerMovimientos(eq, diaInicial, diaActual);
            todo = false;
        }   
        else if (op.equals("Ver Todos")) {
            movs = JDBCDAOMovimiento.obtenerMovimientos(eq);
            todo = true;
        }   
        
        
        movs = Movimiento.obtenerExtracto(eq, movs);

        req.setAttribute("idEquipo", eq.getId());
        req.setAttribute("movimientos", movs);
        req.setAttribute("todo", todo);
    }
    
    private void competiciones(HttpServletRequest req, EquipoFutbol8 eq, 
            boolean isGrupoActivo)
            throws DAOException {

        req.setAttribute("op", "competiciones");
        req.setAttribute("titulo", "COMPETICIONES");
        
        Grupo grp = eq.getClub().getGrupo();
        ArrayList<CompeticionFutbol8> competiciones = new ArrayList<CompeticionFutbol8>();

        if (isGrupoActivo){ 
            competiciones = JDBCDAOFutbol8.listaCompetionesFinalizadas(grp);
        }else{
            ArrayList<Grupo> grupos = JDBCDAOGrupo.obtenerGruposActivos();
            for (Grupo grupo : grupos) {
                ArrayList<CompeticionFutbol8> comps;
                if (!grupo.equals(grp)){
                     comps = JDBCDAOFutbol8.listaCompetionesGrupo(grupo);
                     if (!comps.isEmpty())
                         competiciones.addAll(comps);
                }
            }
            
        }
        Competicion.ordenarFechaDescendente(competiciones);
        req.setAttribute("competiciones", competiciones);
        
    }
    
     private void entrenamiento(HttpServletRequest req, EquipoFutbol8 eq) 
             throws DAOException {
        
         req.setAttribute("op", "entrenamiento");
         req.setAttribute("titulo", "ENTRENAMIENTO");
         
         //ArrayList<JugadorFutbol8> jugadores = JDBCDAOFutbol8.obtenerJugadores(eq);
         
         String op = (String) req.getParameter("operacion");
         String pos = (String) req.getParameter("posicion");
         
         if (pos == null) pos = "Todas";
         
         ArrayList posiciones = new ArrayList();
         posiciones.add("Todas");
         posiciones.addAll(UtilesFutbol8.posicionesJugador());
         
         obtenerJuvenil(req, eq);
         
         ArrayList<EntrenoJugadorFutbol8> jugsEntr = UtilesFutbol8.jugadoresEntrenables(eq, pos);
         
         if (op == null){
             
         }else if (op.equals("Entrenar")){
             jugsEntr = UtilesFutbol8.entrenar(eq, pos);      
         }
         
         req.setAttribute("jugadores", jugsEntr);
         req.setAttribute("posicion", pos);
         req.setAttribute("posiciones", posiciones);
         req.setAttribute("puedeEntrenar", eq.isPuedeEntrenar());
         req.setAttribute("eqTecnico", eq.getEqTecnico());         
        
    }
    
    private void mercado(HttpServletRequest req, EquipoFutbol8 eq) 
            throws DAOException {
        
         req.setAttribute("op", "mercado");
         req.setAttribute("titulo", "MERCADO");
         
         if (eq.getPresupuesto() <= 0){
             req.setAttribute("error", "No tienes presupuesto para acceder al mercado");
             return;
         }
                     
         ArrayList<JugadorFutbol8> jugadores = 
                 JDBCDAOFutbol8.obtenerJugadoresGrupo(eq.getClub().getGrupo());
         
         ArrayList<JugadorFutbol8> jugadoresCompra = 
                 filtrarJugadoresCompra(jugadores, eq);
         ArrayList<JugadorFutbol8> jugadoresSubasta = 
                 filtrarJugadoresSubasta(jugadores, eq);      
         
         
         String op = (String) req.getParameter("operacion");
         Movimiento mov;
         int cantidad;
         
         try {
         
         if (op == null){
             
         }else if (op.equals("Pujar")){
             for (JugadorFutbol8 jug : jugadoresSubasta) {
                 String id = String.valueOf(jug.getId());
                 if (req.getParameter(id) != null && req.getParameter(id).equals("on")){
                     int puja = Integer.parseInt(req.getParameter("puja"+id));
                     jug.hacerPuja(eq, puja);
                     JDBCDAOFutbol8.grabarJugadorFutbol8(jug);
                     String txt = "Has realizado una puja por el jugador " + jug.getNombre() + " por la cantidad de " + puja;
                     Correo.getCorreo().enviarMail("ClubDeportivo Puja Jugador Futbol8", 
                             txt, true, eq.getClub().getMail());
                 }
             }
             
         }else if (op.equals("Comprar")){
             if (!horaCompraValida()) throw new 
                     UnsupportedOperationException("No es posible la compra, esta fuera de horario");
             for (JugadorFutbol8 jug : jugadoresCompra) {
                 String id = String.valueOf(jug.getId());
                 if (req.getParameter(id) != null && req.getParameter(id).equals("on")){  
                     EquipoFutbol8 eqVenta = jug.getEquipo();
                     int clausula = jug.getClausula();
                     cantidad = jug.hacerCompra(eq);
                     ConfigEconomiaFutbol8 conf = JDBCDAOFutbol8.obtenerConfig(jug.getGrupo());
                     int iva = cantidad * conf.getIva() / 100;
                     eq.setPresupuesto(eq.getPresupuesto() - iva);
                     mov = new Movimiento(eq, ClaseMovimiento.Fichajes, cantidad + iva, jug.getNombre());
                     JDBCDAOMovimiento.grabarRegistro(mov);
                     mov = new Movimiento(eqVenta, ClaseMovimiento.VentaJugadores, clausula, jug.getNombre());
                     JDBCDAOMovimiento.grabarRegistro(mov);
                     JDBCDAOFutbol8.grabarJugadorFutbol8(jug);
                     JDBCDAOFutbol8.grabarEquipoFutbol8(eq);
                     JDBCDAOFutbol8.grabarEquipoFutbol8(eqVenta);   
                     String txt = "El jugador " + jug.getNombre() + " ha sido"
                             + " fichado por el " + eq.getNombre() + " procedente del " + eqVenta.getNombre() + 
                             " por " + cantidad + " mas el iva de " + iva;
                     UtilesFutbol8.darNoticia(eq.getClub().getGrupo(), txt);
                     jugadoresCompra.remove(jug);   

                     Correo.getCorreo().enviarMail("ClubDeportivo Compra Jugador Futbol8", 
                             txt, true, eqVenta.getClub().getMail());
                 }
             }
         }
         
         } catch (Exception ex){
             req.setAttribute("error", ex.getMessage());
         }
         
         Collections.sort(jugadoresCompra, new PosicionComparator());
         Collections.sort(jugadoresSubasta, new PosicionComparator());
         
         if (!jugadoresCompra.isEmpty())
             req.setAttribute("jugadoresCompra", jugadoresCompra);
         if (!jugadoresSubasta.isEmpty())
             req.setAttribute("jugadoresSubasta", jugadoresSubasta);
         
    }
    
     private void plantilla(HttpServletRequest req, EquipoFutbol8 eq) 
             throws DAOException {
         
        req.setAttribute("op", "plantilla");
        req.setAttribute("titulo", "PLANTILLA");
        
        ArrayList<JugadorFutbol8> jugadores = JDBCDAOFutbol8.obtenerJugadores(eq);
        Collections.sort(jugadores, new PosicionComparator());
        eq.setJugadores(jugadores);
        ArrayList<JugadorFutbol8> jugadoresTmp = new ArrayList<JugadorFutbol8>();
        jugadoresTmp.addAll(jugadores);
        obtenerJuvenil(req, eq);
        
        String op = (String) req.getParameter("operacion");
        
        try{
        
        if (op == null){
            
        }else if (op.equals("Despedir")){
            for (JugadorFutbol8 jug : jugadoresTmp) {
                 String id = String.valueOf(jug.getId());
                 if (req.getParameter(id) != null && req.getParameter(id).equals("on")){  
                     int coste = jug.despedir();    
                     jugadores.remove(jug);
                     JDBCDAOFutbol8.eliminarJugadorFutbol8(jug);
                     JDBCDAOFutbol8.grabarEquipoFutbol8(eq);                     
                     Movimiento mov = new Movimiento(eq, 
                             ClaseMovimiento.Indemnizacion, 
                             coste, 
                             jug.getNombre());
                     JDBCDAOMovimiento.grabarRegistro(mov);
                     String txt = "El jugador " + jug.getNombre() + " ha sido"
                             + " despedido por el " + eq.getNombre();
                     UtilesFutbol8.darNoticia(eq.getClub().getGrupo(), txt);
                 }
            }
        }else if (op.equals("Blindar")){
            for (JugadorFutbol8 jug : jugadoresTmp) {
                 String id = String.valueOf(jug.getId());
                 if (req.getParameter(id) != null && req.getParameter(id).equals("on")){  
                     int coste = jug.blindar();                     
                     JDBCDAOFutbol8.grabarJugadorFutbol8(jug);
                     JDBCDAOFutbol8.grabarEquipoFutbol8(eq);
                     Movimiento mov = new Movimiento(eq, 
                             ClaseMovimiento.Blindajes, 
                             coste, 
                             jug.getNombre());
                     JDBCDAOMovimiento.grabarRegistro(mov);
                     String txt = "El jugador " + jug.getNombre() + " ha sido"
                             + " blindado por el " + eq.getNombre();
                     UtilesFutbol8.darNoticia(eq.getClub().getGrupo(), txt);
                 }
            }
        }else if (op.equals("Mejorar Contrato")){
            for (JugadorFutbol8 jug : jugadoresTmp) {
                 String id = String.valueOf(jug.getId());
                 if (req.getParameter(id) != null && req.getParameter(id).equals("on")){  
                     jug.mejorarContrato();                     
                     JDBCDAOFutbol8.grabarJugadorFutbol8(jug);
                     JDBCDAOFutbol8.grabarEquipoFutbol8(eq);
                     String txt = "Al jugador " + jug.getNombre() + " se le ha "
                             + " mejorado el contrato por el " + eq.getNombre();
                     UtilesFutbol8.darNoticia(eq.getClub().getGrupo(), txt);
                 }
            }
        }else if (op.equals("Renovar")){
            for (JugadorFutbol8 jug : jugadoresTmp) {
                 String id = String.valueOf(jug.getId());
                 if (req.getParameter(id) != null && req.getParameter(id).equals("on")){  
                    UtilesFutbol8.renovarJugador(jug, eq);
                 }
            }
        }else if (op.equals("Poner Transferible")){
            for (JugadorFutbol8 jug : jugadoresTmp) {
                 String id = String.valueOf(jug.getId());
                 if (req.getParameter(id) != null && req.getParameter(id).equals("on")){  
                     jug.ponerTransferible();                     
                     JDBCDAOFutbol8.grabarJugadorFutbol8(jug);                    
                     String txt = "El jugador " + jug.getNombre() + " se ha "
                             + " declarado transferible por el " + eq.getNombre();
                     UtilesFutbol8.darNoticia(eq.getClub().getGrupo(), txt);
                 }
            }
        }else if (op.equals("Subastar")){
            for (JugadorFutbol8 jug : jugadoresTmp) {
                 String id = String.valueOf(jug.getId());
                 if (req.getParameter(id) != null && req.getParameter(id).equals("on")){  
                     jug.subastar();                     
                     JDBCDAOFutbol8.grabarJugadorFutbol8(jug);                    
                     String txt = "El jugador " + jug.getNombre() + " se ha "
                             + " puesto en subasta por el " + eq.getNombre();
                     UtilesFutbol8.darNoticia(eq.getClub().getGrupo(), txt);
                 }
            }  
        }else if (op.equals("Incorporar Ojeado")){
            if (eq.getJugadoresOjeados() > 0 && eq.getJugadores().size() < 16){
                JugadorFutbol8 newJug = UtilesFutbol8.crearJugadorOjeadofutbol8(eq);
                eq.setJugadoresOjeados(eq.getJugadoresOjeados() - 1);
                JDBCDAOFutbol8.grabarEquipoFutbol8(eq);
                String txt = "El jugador " + newJug.getNombre() + " ojeado "
                        + " por el " + eq.getNombre() + " se ha incorparado al equipo";
                UtilesFutbol8.darNoticia(eq.getClub().getGrupo(), txt);
            }   
        }else if (op.equals("Incorporar Juvenil")){
            JuvenilFutbol8 juv = eq.getJuvenil();
            if (juv != null && juv.isIncorporacion() && eq.getJugadores().size() < 16){
                JugadorFutbol8 newJug = UtilesFutbol8.crearJugadorfutbol8(eq, juv.getPosicion(), juv.getValoracionReal());
                JDBCDAOFutbol8.eliminarJuvenilFutbol8(juv);
                eq.setJuvenil(null);
                req.setAttribute("juvenil", null);    
                String txt = "El juvenil " + newJug.getNombre() + " se ha incorporado a "
                        + " la plantilla del " + eq.getNombre();                
                UtilesFutbol8.darNoticia(eq.getClub().getGrupo(), txt);
            }    
          
        }
        }catch (Exception ex){
            req.setAttribute("error", ex.getMessage());
        }
        
        req.setAttribute("equipo", eq);
        req.setAttribute("jugadores", jugadores);
        
    }
     
     private void jugador(HttpServletRequest req, EquipoFutbol8 eq) {
        
         req.setAttribute("op", "jugador");
         req.setAttribute("titulo", "JUGADOR");
         
         String idJug = (String) req.getParameter("id");
         
         long id = 0;
         if (idJug != null && !idJug.isEmpty())
             id = Long.parseLong(idJug);
         JugadorFutbol8 jug = null;
         
         try{     
             jug = JDBCDAOFutbol8.obtenerJugador(id, eq);
             if (jug == null) throw new UnsupportedOperationException("Jugador no Encontrado");
             jug.setGrupo(eq.getClub().getGrupo());
             jug.setEquipo(eq);
             String nombre = (String) req.getParameter("nombre");
             nombre = new String(nombre.getBytes(), "UTF-8"); 
             if (nombre != null && !nombre.isEmpty()){
                 nombre = StringUtil.removeCharsEspeciales(nombre);
                 nombre = StringUtil.truncate(nombre, 40);
                 if (nombre.isEmpty())
                     req.setAttribute("error", "El nombre propuesto no es valido, no es posible realizar el cambio");
                 else{
                     boolean existe = JDBCDAOFutbol8.existeJugador(nombre, eq.getClub().getGrupo());
                     if (existe)
                         req.setAttribute("error", "Este nombre ya existe, no es posible realizar el cambio");
                     else{  
                         jug.setNombre(nombre);
                         JDBCDAOFutbol8.grabarJugadorFutbol8(jug);
                     }                     
                 }
             }
             
         }catch (Exception ex){
            req.setAttribute("error", ex.getMessage());
        }
         
         req.setAttribute("jugador", jug);        
        
    }
     
      private JuvenilFutbol8 obtenerJuvenil(HttpServletRequest req, EquipoFutbol8 eq) throws DAOException {
          
          JuvenilFutbol8 juvenil = JDBCDAOFutbol8.obtenerJuvenil(eq);
          eq.setJuvenil(juvenil);
          
          req.setAttribute("juvenil", juvenil);
          return juvenil;
          
      }
     
     
      private void entrenador(HttpServletRequest req, EquipoFutbol8 eq) throws DAOException {

        req.setAttribute("op", "entrenador");
        req.setAttribute("titulo", "ENTRENADOR");
        
        ArrayList<EntrenadorFutbol8> listaEntrLibres = 
                JDBCDAOFutbol8.obtenerEntrenadoresLibresGrupo(eq.getClub().getGrupo());        
        
            
        String op = (String) req.getParameter("operacion");
        Movimiento mov;
        
        try{ 
        
            if (op == null){            
            }else if (op.equals("Contratar") && req.getParameter("newEntrenador") != null){
                EntrenadorFutbol8 actual = eq.getEntrenador();
                Long idNuevo = Long.parseLong(req.getParameter("newEntrenador"));
                EntrenadorFutbol8 nuevo = JDBCDAOFutbol8.obtenerEntrenador(idNuevo);
                 nuevo.contratar(actual); 
                 eq.setEntrenador(nuevo);
                 nuevo.setEquipo(eq);
                 eq.setMoral(EquipoFutbol8.MORAL_INICIAL);
                 JDBCDAOFutbol8.grabarEquipoFutbol8(eq);
                 JDBCDAOFutbol8.grabarEntrenadorFutbol8(nuevo); 
                 String txt = "El entrenador " + nuevo.getNombre() + " ha sido "
                         + " fichado por el " + eq.getNombre() + " y ha despedido"
                         + " ha " + actual.getNombre() + " con una indemnizacion de "
                         + actual.getIndemnizacion() + " Euros";
                 UtilesFutbol8.darNoticia(eq.getClub().getGrupo(), txt);
                 mov = new Movimiento(eq, ClaseMovimiento.Indemnizacion, 
                         actual.getIndemnizacion(), actual.getNombre());
                 JDBCDAOMovimiento.grabarRegistro(mov);
                 
                for (EntrenadorFutbol8 entr : listaEntrLibres) 
                    if (entr.getId() == idNuevo){
                        listaEntrLibres.remove(entr);
                        break;
                    }

                JDBCDAOFutbol8.eliminarEntrenadorFutbol8(actual);
            
            }
        }catch(Exception ex){
            req.setAttribute("error", ex.getMessage());
        }
        
        req.setAttribute("tacticas", eq.getEntrenador().getTacticas());
        req.setAttribute("entrenador", eq.getEntrenador());
        req.setAttribute("listaEntrenadores", listaEntrLibres);
        
    }
      
      
    private void cursoEntrenador(HttpServletRequest req, EquipoFutbol8 eq) throws DAOException {
        
        req.setAttribute("op", "cursoEntrenador");   
       
        
        String op = (String) req.getParameter("operacion");
        req.setAttribute("titulo", "CURSO DE ENTRENADORES");
        
        if (op == null){}
        else if (op.equals("Realizar Curso")){
            String numTac = (String) req.getParameter("tactSel");
            TacticaFutbol8 tact = TacticaFutbol8.tacticaFutbol8(Integer.parseInt(numTac));
            eq.getEntrenador().anyadirTacticaCurso(tact);
            JDBCDAOFutbol8.grabarEntrenadorFutbol8(eq.getEntrenador());
            JDBCDAOFutbol8.grabarEquipoFutbol8(eq);
            Movimiento mov = new Movimiento(eq, ClaseMovimiento.Cursos, EntrenadorFutbol8.PRECIO_CURSO);
            JDBCDAOMovimiento.grabarRegistro(mov);
            String txt = "El entrenador " + eq.getEntrenador().getNombre() + " del " + eq.getNombre() +
                         " ha realizado un curso de tacticas";
            UtilesFutbol8.darNoticia(eq.getClub().getGrupo(), txt);
                 
        }
        
         ArrayList<TacticaFutbol8> listaTacts = TacticaFutbol8.tacticasFutbol8();
        ArrayList<TacticaFutbol8> listaFinal = new ArrayList<TacticaFutbol8>();
        
        for (TacticaFutbol8 tactlista : listaTacts) {
            boolean anyadir = true;
            for (TacticaFutbol8 tact : eq.getEntrenador().getTacticas()) {
                if (tactlista.equals(tact)){
                    anyadir = false;
                    break;
                }
            }
            if (anyadir) listaFinal.add(tactlista);
        }
        
        req.setAttribute("tacticas", listaFinal);
        
    }
    
    private void noticias(HttpServletRequest req, Grupo grp) throws DAOException {
        
        req.setAttribute("op", "noticias");  
        req.setAttribute("titulo", "NOTICIAS");
        
        int numero = 100;
        String num = (String) req.getParameter("numero");

        if (num != null)
            numero = Integer.parseInt(num);
        
        ArrayList<Noticia> noticias = 
                JDBCDAONoticia.obtenerNoticias(grp, Deporte.Futbol8, numero);        
       
        req.setAttribute("noticias", noticias);
        req.setAttribute("numero", numero);
               
         
        
    }
    
    private void comentarios(HttpServletRequest req, Club club) throws DAOException {
        
        req.setAttribute("op", "comentarios");   
        req.setAttribute("titulo", "COMENTARIOS");
        req.setAttribute("inicio", false);
        
        int numero = 100;
        String num = (String) req.getParameter("numero");

        if (num != null)
            numero = Integer.parseInt(num);
        
        UtilesHttpServlet.tratarComentarios(req, club, false, numero);  
        
        req.setAttribute("numero", numero);
        
    }
    
     private void auditoria(HttpServletRequest req, Grupo grp) throws DAOException {
        
        req.setAttribute("op", "auditoria");   
        req.setAttribute("titulo", "AUDITORIA");

        ArrayList<EquipoFutbol8> equipos = JDBCDAOFutbol8.listaEquiposFutbol8(grp, true);
        ArrayList<Movimiento> movs;
        
        for (EquipoFutbol8 eq : equipos) {
            movs = JDBCDAOMovimiento.obtenerMovimientos(eq);
            for (Movimiento mov : movs) {
                if (mov.isPositivo())
                    eq.setTotalIngresos(eq.getTotalIngresos() + mov.getValor());
                else
                    eq.setTotalGastos(eq.getTotalGastos() + mov.getValor());
            }
        }
        
        req.setAttribute("equipos", equipos);
        
    }
     
      private void administracion(HttpServletRequest req, EquipoFutbol8 eq) throws DAOException {
        
        req.setAttribute("op", "administracion");   
        req.setAttribute("titulo", "ADMINISTRACIÓN");
        
        ConfigEconomiaFutbol8 config = JDBCDAOFutbol8.obtenerConfig(eq.getClub().getGrupo());
        
        boolean modificable = false;
        
        String op = (String) req.getParameter("operacion");
         
         if (op == null){
             
         }else if (op.equals("Modificar") && config.isModificable(eq)){
             try {
                 String interesesCredito = (String) req.getParameter("interesesCredito");
                 String retencionHaciendaBase = (String) req.getParameter("retencionHaciendaBase");
                 String retencionLineal = (String) req.getParameter("retencionLineal");
                 String iva = (String) req.getParameter("iva");
                 String ibi = (String) req.getParameter("ibi");
                 String subidaMaxBolsa = (String) req.getParameter("subidaMaxBolsa");
                 String crackBolsa = (String) req.getParameter("crackBolsa");
                 String porcentajePremioLiga = (String) req.getParameter("porcentajePremioLiga");
                 String porcentajeCampeonCopa = (String) req.getParameter("porcentajeCampeonCopa");
                 int interesesCreditoValor = Integer.parseInt(interesesCredito);
                 int retencionHaciendaBaseValor = Integer.parseInt(retencionHaciendaBase);
                 boolean retencionLinealValor = false;
                 if (retencionLineal != null && retencionLineal.equals("on")) 
                     retencionLinealValor = true;                 
                 int ivaValor = Integer.parseInt(iva);
                 int ibiValor = Integer.parseInt(ibi);
                 int subidaMaxBolsaValor = Integer.parseInt(subidaMaxBolsa);
                 boolean crackBolsaValor = false;
                 if (crackBolsa != null && crackBolsa.equals("on")) 
                     crackBolsaValor = true;                 
                 int porcentajePremioLigaValor = Integer.parseInt(porcentajePremioLiga);
                 int porcentajeCampeonCopaValor = Integer.parseInt(porcentajeCampeonCopa);
                 config.setInteresCredito(interesesCreditoValor);
                 config.setRetencionHaciendaBase(retencionHaciendaBaseValor);
                 config.setRetencionLineal(retencionLinealValor);
                 config.setIva(ivaValor);
                 config.setIBI(ibiValor);
                 config.setSubidaMaxBolsa(subidaMaxBolsaValor);
                 config.setCrackBolsa(crackBolsaValor);
                 config.setPorcentajePremioLiga(porcentajePremioLigaValor);
                 config.setPorcentajeCampeonCopa(porcentajeCampeonCopaValor);
                 
                 config.setModificado(true);
                 
                 JDBCDAOFutbol8.grabarConfigEconomiaFutbol8(config);
                 
                 String txt = "El " + eq.getNombre() + " ha cambiado la configuración económica del grupo";                 
                 UtilesFutbol8.darNoticia(eq.getClub().getGrupo(), txt);
                 
                 txt = "El " + eq.getNombre() + " ha cambiado la configuración económica del grupo: <br/><br/>" +
                         "Intereses Credito: " + config.getInteresCredito() + "<br/>" +
                         "Retencion Hacienda: " + config.getRetencionHaciendaBase();
                 if (config.isRetencionLineal())
                     txt = txt + " (LINEAL)<br/>";
                 else
                     txt = txt + " (ESCALONADA)<br/>";
                        
                 txt = txt + "Iva: " + config.getIva() + "<br/>" +
                         "IBI: " + config.getIBI() + "<br/>" +
                         "Subida Máxima Bolsa: " + config.getSubidaMaxBolsa();
                 if (config.isCrackBolsa())
                     txt = txt + " (CRACK ACTIVADO)<br/>";
                 else
                     txt = txt + " (CRACK DESACTIVADO)<br/>";
                 txt = txt + "Premio Ganador Liga: " + config.getPorcentajePremioLiga() + "%<br/>" +
                         "Premio Ganador Copa: " + config.getPorcentajeCampeonCopa() + "%<br/>"; 
                 ArrayList<String> correos = JDBCDAOClub.mailsClubs(eq.getClub().getGrupo(), Deporte.Futbol8);
                 Correo.getCorreo().enviarMailMasivo("ClubDeportivo Cambio Configuración Futbol8", txt, true, correos); 
                 
                 
             }catch (Exception ex){
                 req.setAttribute("error", ex.getMessage());
             }
         }
        
        
        
        
        if (config != null)
            modificable = config.isModificable(eq);
       
        req.setAttribute("modificable", modificable);
        req.setAttribute("config", config);
        
    }
           

    private void grabarAlineacion(String txtAlineacion, EquipoFutbol8 eq, 
            boolean grabarEnDIsco) throws DAOException {

        ArrayList<JugadorFutbol8> jugadores = eq.getJugadores();
        for (JugadorFutbol8 jug : jugadores) {
            jug.setJuegaJornada(false);
        }
        AlineacionFutbol8 alineacion = eq.getAlineacion();
        alineacion.limpiarAlineacion();

        if (!txtAlineacion.isEmpty()){
            for (String datos : txtAlineacion.split(";")) {
                String[] dato = datos.split("_");
                String posicion = dato[0];
                long idJugador = Long.parseLong(dato[1]);
                JugadorFutbol8 jug = eq.getJugador(idJugador);
                jug.setJuegaJornada(true);
                alineacion.setDato(posicion, jug);
            }
        }

        if (grabarEnDIsco){
            for (JugadorFutbol8 jug : jugadores) 
                JDBCDAOFutbol8.grabarJugadorFutbol8(jug);       

            JDBCDAOFutbol8.grabarAlineacionFutbol8(alineacion);
        }

    }

    private void limpiarAlineacion(EquipoFutbol8 eq) throws DAOException {

        ArrayList<JugadorFutbol8> jugadores = eq.getJugadores();
        for (JugadorFutbol8 jug : jugadores) {
            jug.setJuegaJornada(false);
        }
        AlineacionFutbol8 alineacion = eq.getAlineacion();
        alineacion.limpiarAlineacion();

        for (JugadorFutbol8 jug : jugadores) {
            JDBCDAOFutbol8.grabarJugadorFutbol8(jug);
        }

        JDBCDAOFutbol8.grabarAlineacionFutbol8(alineacion);


    }

   
    private ArrayList<JugadorFutbol8> filtrarJugadoresCompra(
            ArrayList<JugadorFutbol8> jugadores, EquipoFutbol8 eq) {
        
       ArrayList<JugadorFutbol8> lista = new ArrayList<JugadorFutbol8>();
       
        for (JugadorFutbol8 jug : jugadores) {
            if (!jug.isEnSubasta() && !jug.isBlindado()
                    && !jug.getEquipo().equals(eq)) lista.add(jug);
        }
        
        return lista;
    }

   private ArrayList<JugadorFutbol8> filtrarJugadoresSubasta(
            ArrayList<JugadorFutbol8> jugadores, EquipoFutbol8 eq) {
       
       ArrayList<JugadorFutbol8> lista = new ArrayList<JugadorFutbol8>();
       
        for (JugadorFutbol8 jug : jugadores) {
            if (jug.isEnSubasta() && (jug.getEquipo() == null ||
                    !jug.getEquipo().equals(eq))) lista.add(jug);
        }
        
        return lista;
   }

   
   
    
  
    private boolean horaCompraValida() {
        
        int horaMin = 14;
        int horaMax = 24;
        
        Calendar cal = Calendar.getInstance(); // devuelve una instancia de Calendar con la fecha actual
        int hora = cal.get(Calendar.HOUR_OF_DAY);

        return hora >= horaMin && hora < horaMax;
        
    }

  

   
  

   

    


}



     

