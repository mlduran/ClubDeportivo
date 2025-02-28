
package mld.clubdeportivo.controladores;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.Long.valueOf;
import static java.lang.Math.max;
import static java.lang.String.valueOf;
import java.util.*;
import static java.util.Calendar.DAY_OF_YEAR;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.getInstance;
import static java.util.Collections.sort;
import mld.clubdeportivo.base.*;
import mld.clubdeportivo.base.futbol8.JugadorFutbol8.PosicionComparator;
import mld.clubdeportivo.base.futbol8.*;
import mld.clubdeportivo.bd.*;
import mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8;
import java.util.logging.*;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static mld.clubdeportivo.base.ClaseMovimiento.AmpliacionEstadio;
import static mld.clubdeportivo.base.ClaseMovimiento.Blindajes;
import static mld.clubdeportivo.base.ClaseMovimiento.ConcesionCredito;
import static mld.clubdeportivo.base.ClaseMovimiento.Cursos;
import static mld.clubdeportivo.base.ClaseMovimiento.DevolucionCredito;
import static mld.clubdeportivo.base.ClaseMovimiento.Fichajes;
import static mld.clubdeportivo.base.ClaseMovimiento.Gestion;
import static mld.clubdeportivo.base.ClaseMovimiento.Indemnizacion;
import static mld.clubdeportivo.base.ClaseMovimiento.InversionBolsa;
import static mld.clubdeportivo.base.ClaseMovimiento.Primas;
import static mld.clubdeportivo.base.ClaseMovimiento.RescateBolsa;
import static mld.clubdeportivo.base.ClaseMovimiento.VentaJugadores;
import static mld.clubdeportivo.base.Competicion.ordenarFechaDescendente;
import static mld.clubdeportivo.base.Deporte.Futbol8;
import static mld.clubdeportivo.base.Movimiento.obtenerExtracto;
import static mld.clubdeportivo.base.Seccion.PRECIOS_ENTRADAS;
import static mld.clubdeportivo.base.futbol8.AlineacionFutbol8.codificarJugadores;
import static mld.clubdeportivo.base.futbol8.AlineacionFutbol8.parseAlineacion;
import static mld.clubdeportivo.base.futbol8.AlineacionFutbol8.parseEntrenador;
import static mld.clubdeportivo.base.futbol8.CompeticionFutbol8.clasesCompeticion;
import static mld.clubdeportivo.base.futbol8.EntrenadorFutbol8.PRECIO_CURSO;
import static mld.clubdeportivo.base.futbol8.EquipoFutbol8.MORAL_INICIAL;
import static mld.clubdeportivo.base.futbol8.EsfuerzoFutbol8.valueOf;
import static mld.clubdeportivo.base.futbol8.EstadisticaResultadosFutbol8.getMediaEmpatados;
import static mld.clubdeportivo.base.futbol8.EstadisticaResultadosFutbol8.getMediaGanados;
import static mld.clubdeportivo.base.futbol8.EstadisticaResultadosFutbol8.getMediaOpcionesEmpatar;
import static mld.clubdeportivo.base.futbol8.EstadisticaResultadosFutbol8.getMediaOpcionesGanar;
import static mld.clubdeportivo.base.futbol8.EstadisticaResultadosFutbol8.getMediaOpcionesPerder;
import static mld.clubdeportivo.base.futbol8.EstadisticaResultadosFutbol8.getMediaPerdidos;
import static mld.clubdeportivo.base.futbol8.PartidoFutbol8.NUMERO_SIMULACIONES;
import static mld.clubdeportivo.base.futbol8.TacticaFutbol8.tacticaFutbol8;
import static mld.clubdeportivo.base.futbol8.TacticaFutbol8.tacticasFutbol8;
import static mld.clubdeportivo.bd.JDBCDAOClub.mailsClubs;
import static mld.clubdeportivo.bd.JDBCDAOGrupo.obtenerGruposActivos;
import static mld.clubdeportivo.bd.JDBCDAOMovimiento.grabarRegistro;
import static mld.clubdeportivo.bd.JDBCDAOMovimiento.obtenerMovimientos;
import static mld.clubdeportivo.bd.JDBCDAONoticia.obtenerNoticias;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.competicion;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.competicionActiva;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.eliminarEntrenadorFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.eliminarJugadorFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.eliminarJuvenilFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.existeJugador;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.grabarAlineacionFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.grabarConfigEconomiaFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.grabarEntrenadorFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.grabarEquipoFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.grabarJugadorFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.grabarVacacionFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.idJornada;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.listaCompetionesFinalizadas;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.listaCompetionesGrupo;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.listaEquiposFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerAlineacionPartido;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerClasificacion;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerCompeticion;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerConfig;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerDatosHistoCompeticion;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerDatosPrepararPartido;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerDatosVacacionesFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerEntrenador;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerEntrenadoresLibresGrupo;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerEstadisticaPartido;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerGoleadores;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerJornada;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerJugador;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerJugadores;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerJugadoresGrupo;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerPartido;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerPorteros;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerProximoPartido;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerSimpleEquipoFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerUltimoPartido;
import static mld.clubdeportivo.controladores.UtilesFutbol8.crearJugadorOjeadofutbol8;
import static mld.clubdeportivo.controladores.UtilesFutbol8.crearJugadorfutbol8;
import static mld.clubdeportivo.controladores.UtilesFutbol8.darNoticia;
import static mld.clubdeportivo.controladores.UtilesFutbol8.datosProximoPartido;
import static mld.clubdeportivo.controladores.UtilesFutbol8.entrenar;
import static mld.clubdeportivo.controladores.UtilesFutbol8.jugadoresEntrenables;
import static mld.clubdeportivo.controladores.UtilesFutbol8.obtenerGoleadores;
import static mld.clubdeportivo.controladores.UtilesFutbol8.obtenerPorteros;
import static mld.clubdeportivo.controladores.UtilesFutbol8.posicionesJugador;
import static mld.clubdeportivo.controladores.UtilesFutbol8.posicionesJuvenil;
import static mld.clubdeportivo.controladores.UtilesFutbol8.renovarJugador;
import static mld.clubdeportivo.controladores.UtilesHttpServlet.comprobarEstado;
import static mld.clubdeportivo.controladores.UtilesHttpServlet.tratarComentarios;
import static mld.clubdeportivo.utilidades.Correo.getCorreo;
import static mld.clubdeportivo.utilidades.StringUtil.isNumero;
import static mld.clubdeportivo.utilidades.StringUtil.removeCharsEspeciales;
import static mld.clubdeportivo.utilidades.StringUtil.truncate;
import static mld.clubdeportivo.utilidades.UtilGenericas.pilaError;

/**
 *
 * @author Miguel
 */
public class PanelControlFutbol8HttpServlet extends HttpServlet {

    private static Logger logger = 
            getLogger(PanelControlFutbol8HttpServlet.class.getName());
   
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

        if (!comprobarEstado(req, resp)) return;
        
        req.setAttribute("path", "/panelControl/Futbol8/inicio");
        
        var dir = "/PanelControl/panelControlFutbol8.jsp";

        try {

            long id = (Long) req.getSession().getAttribute("idEquipo");
            var eq = obtenerSimpleEquipoFutbol8(id);
            obtenerListaEquipos(req, eq.getClub().getGrupo());

            var accion = req.getPathInfo();           
            switch (accion) {
                case "/inicio":
                    inicio(req, eq);
                    break;
                case "/fichaEquipo":
                    fichaEquipo(req);
                    break;
                case "/gestion":
                    gestion(req, eq);
                    break;
                case "/prepararPartido":
                    prepararPartido(req);
                    break;
                case "/entrenamiento":
                    entrenamiento(req, eq);
                    break;
                case "/entrenador":
                    entrenador(req, eq);
                    break;
                case "/cursoEntrenador":
                    cursoEntrenador(req, eq);
                    break;
                case "/calendario":
                    calendario(req, eq);
                    break;
                case "/clasificacion":
                    clasificacion(req, eq);
                    break;
                case "/movimientos":
                    movimientos(req, eq);
                    break;
                case "/mercado":
                    mercado(req, eq);
                    break;
                case "/plantilla":
                    plantilla(req, eq);
                    break;
                case "/jugador":
                    jugador(req, eq);
                    break;
                case "/partido":
                    partido(req);
                    break;
                case "/goleadores":
                    goleadores(req, eq);
                    break;
                case "/porteros":
                    porteros(req, eq);
                    break;
                case "/competiciones":
                    competiciones(req, eq, true);
                    break;
                case "/competicionesGrupos":
                    competiciones(req, eq, false);
                    break;
                case "/noticias":
                    noticias(req, eq.getClub().getGrupo());
                    break;
                case "/comentarios":
                    comentarios(req, eq.getClub());
                    break;
                case "/auditoria":
                    auditoria(req, eq.getClub().getGrupo());
                    break;
                case "/estadistica":
                    estadistica(req, eq);
                    break;
                case "/administracion":
                    administracion(req, eq);
                    break;
                default:
                    break;
            }


        } catch (DAOException ex) {
            logger.log(SEVERE, ex.getMessage());
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("errorDes", pilaError(ex));
            dir = "/Utiles/error.jsp";
            
             //Aqui gestionar pagina de error
        } catch (Exception ex) {
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("errorDes", pilaError(ex));
            dir = "/Utiles/error.jsp";
             //Aqui gestionar pagina de error
        }
        
             
        var view = req.getRequestDispatcher(dir);
        view.forward(req, resp);
       
    }

   
    private HttpServletRequest inicio(HttpServletRequest req, EquipoFutbol8 eq)
            throws DAOException{

        req.setAttribute("op", "inicio");
        req.setAttribute("titulo", "FUTBOL8");

        req.setAttribute("equipo", eq);
        
        var noticias = 
                obtenerNoticias(eq.getClub().getGrupo(), Futbol8, 10);
        
        long idJornada = 0;
                 
        var ultimoPartido = obtenerUltimoPartido(eq);
        if (ultimoPartido != null){
            idJornada = idJornada(ultimoPartido);
        }        
           
        var proximoTipoComp = "";
        CompeticionFutbol8 comp = null;
        PartidoFutbol8 proximoPartido = null;
        
        try {
            var datos = datosProximoPartido(eq, req.getServletContext());
             proximoPartido = (PartidoFutbol8) datos.get(0);
            comp = (CompeticionFutbol8) datos.get(1);
        }catch(Exception ex)
        {               
                };
        if (comp != null) {
            proximoTipoComp = comp.getClase();
            logger.info("existeComp = " + comp.toString());
        }
        
        req.setAttribute("ultimoPartido", ultimoPartido);
        req.setAttribute("proximoPartido", proximoPartido);
        req.setAttribute("proximoTipoComp", proximoTipoComp);
        req.setAttribute("ultimaJornada", idJornada);
        req.setAttribute("noticias", noticias);
        req.setAttribute("existeComp", comp != null);
        
        
        tratarComentarios(req, eq.getClub(), false);   
               
        return req;

    }
    
    
    
    private HttpServletRequest partido(HttpServletRequest req)
            throws DAOException{

        req.setAttribute("op", "partido");
        req.setAttribute("titulo", "INFORME PARTIDO");
        
        try {
            var id = parseLong(req.getParameter("id"));
            var partido = obtenerPartido(id);
            var est = obtenerEstadisticaPartido(partido);
            var aliEqLocal = 
                    parseAlineacion(est.getAlineacionLocal());
            var aliEqVisitante = 
                    parseAlineacion(est.getAlineacionVisitante());
            var max = max(aliEqLocal.size(), aliEqVisitante.size());
            var estAli = new ArrayList<>();
            for (var i = 0; i < max; i++){
                var e = new EstadisticaAlineacionFutbol8();
                if (aliEqLocal.size() > i){
                    e.setJugadorLocal(aliEqLocal.get(i).getNombre());
                    e.setPosicionLocal(aliEqLocal.get(i).getPosicion().name());
                    e.setValoracionLocal(valueOf(aliEqLocal.get(i).getValoracionReal()));
                    e.setValoracionPartidoLocal(valueOf(aliEqLocal.get(i).getValoracionPartido()));
                }else{
                    e.setJugadorLocal("");
                    e.setPosicionLocal("");
                    e.setValoracionLocal("0");
                    e.setValoracionPartidoLocal("0");
                }
                
                if (aliEqVisitante.size() > i){
                    e.setJugadorVisitante(aliEqVisitante.get(i).getNombre());
                    e.setPosicionVisitante(aliEqVisitante.get(i).getPosicion().name());
                    e.setValoracionVisitante(valueOf(aliEqVisitante.get(i).getValoracionReal()));
                    e.setValoracionPartidoVisitante(valueOf(aliEqVisitante.get(i).getValoracionPartido()));
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
                    parseEntrenador(est.getAlineacionLocal()));
            req.setAttribute("entrenadorEqVisitante", 
                    parseEntrenador(est.getAlineacionVisitante()));
        }
        catch(Exception ex){
            req.setAttribute("error", "Lo sentimos no es posible mostrar este partido");
        }
         
         return req;
    }
    

    private void obtenerListaEquipos(HttpServletRequest req,
            Grupo grupo) throws DAOException{

        ArrayList equiposGrupo = listaEquiposFutbol8(grupo);

        req.setAttribute("equiposGrupo", equiposGrupo);

    }

  
  
    private void fichaEquipo(HttpServletRequest req) throws DAOException {

        req.setAttribute("op", "fichaEquipo");
        req.setAttribute("titulo", "FICHA EQUIPO");

        long idEquipo = Long.getLong(req.getParameter("id"));

        var eq = obtenerSimpleEquipoFutbol8(idEquipo);
        req.setAttribute("equipoFicha", eq);
        req.setAttribute("jugadoresFicha", eq.getJugadores());
        req.setAttribute("entrenadorFicha", eq.getEntrenador());
        obtenerJuvenil(req, eq);


    }

   
    private void gestion(HttpServletRequest req, EquipoFutbol8 eq) throws DAOException {
        
        req.setAttribute("op", "gestion");
        req.setAttribute("titulo", "GESTION DEL EQUIPO");
        req.setAttribute("equipo", eq);
        var eqsGrupo = 
                listaEquiposFutbol8(eq.getClub().getGrupo());
        var eqsPrima = new ArrayList<>();
        for (var eqf8 : eqsGrupo) 
            if (!eqf8.equals(eq)) eqsPrima.add(eqf8);
        
        req.setAttribute("equiposPrima", eqsPrima);
        req.setAttribute("tiposCompeticion", clasesCompeticion);
        req.setAttribute("preciosEntrada", PRECIOS_ENTRADAS);
        
        req.setAttribute("posiciones", posicionesJuvenil());
        var posJuvenil = eq.getPosicionJuvenil().name();
        req.setAttribute("posicionJuvenil", posJuvenil);
        req.setAttribute("tacticas", eq.getEntrenador().getTacticas());

        var op = (String) req.getParameter("operacion");
        Movimiento mov;
        int precio;
        
        // Modo vacaciones
        var vac = obtenerDatosVacacionesFutbol8(eq);        
        if (vac == null){
            vac = new VacacionFutbol8(eq);
            grabarVacacionFutbol8(vac);
        }
        vac.setEquipo(eq);
                
        try{
            if (null == op) {
            }// no hacemos nada
            else switch (op) {
                case "Ampliar Asientos" -> {
                    precio = eq.ampliarCampo();
                    mov = new Movimiento(eq, AmpliacionEstadio, precio);
                    grabarRegistro(mov);
                }
                case "Firmar Contrato" -> {
                    precio = eq.ampliarPublicidad();
                    mov = new Movimiento(eq, Gestion, precio);
                    grabarRegistro(mov);
                }
                case "Nuevo Tecnico" -> {
                    precio = eq.ampliarEqTecnico();
                    mov = new Movimiento(eq, Gestion, precio);
                    grabarRegistro(mov);
                }
                case "Nuevo Ojeador" -> {
                    precio = eq.ampliarOjeadores();
                    mov = new Movimiento(eq, Gestion, precio);
                    grabarRegistro(mov);
                }
                case "Invertir" ->                     {
                        var cantidad = parseInt(req.getParameter("invBolsa"));
                        if (cantidad > 0) {
                            eq.invertirBolsa(cantidad);
                            mov = new Movimiento(eq, InversionBolsa, cantidad);
                            grabarRegistro(mov);
                        }
                    }
                case "Rescatar" ->                     {
                        var cantidad = eq.rescatarBolsa();
                        if (cantidad > 0) {
                            mov = new Movimiento(eq, RescateBolsa, cantidad);
                            grabarRegistro(mov);
                        }
                    }
                case "Pedir" ->                     {
                        var cantidad = parseInt(req.getParameter("valorCredito"));
                        if (cantidad > 0) {
                            eq.pedirCredito(cantidad);
                            mov = new Movimiento(eq, ConcesionCredito, cantidad);
                            grabarRegistro(mov);
                        }
                    }
                case "Devolver" ->                     {
                        var cantidad = parseInt(req.getParameter("valorCredito"));
                        if (cantidad > 0) {
                            cantidad = eq.devolverCredito(cantidad);
                            mov = new Movimiento(eq, DevolucionCredito, cantidad);
                            grabarRegistro(mov);
                        }
                    }
                case "Cambiar Precio" ->                     {
                        var cantidad = parseInt(req.getParameter("precioEntrada"));
                        eq.setPrecioEntradas(cantidad);
                    }
                case "Cambiar Nombre" -> {
                    var nom = req.getParameter("nombreEstadio");
                    eq.setNombreCampo(nom);
                }
                case "Cambiar Posicion" -> {
                    PosicionElegidaFutbol8 pos = PosicionElegidaFutbol8.valueOf(req.getParameter("posicion"));
                    eq.setPosicionJuvenil(pos);
                    req.setAttribute("posicionJuvenil", pos);
                }
                case "Cambiar" -> {
                    var equipacion = parseInt(req.getParameter("equipacion"));
                    eq.setEquipacion(equipacion);
                }
                case "Primar" -> {
                    try {
                        var cantidad = parseInt(req.getParameter("prima"));
                        if (cantidad > 0) {
                            if (eq.getPresupuesto() < cantidad)
                                throw new UnsupportedOperationException("No tienes tanto presupuesto");
                            var idEqAPrimar = parseLong(req.getParameter("equipoPrima"));
                            var claseComp = req.getParameter("tipoCompeticion");
                            var eqAPrimar = obtenerSimpleEquipoFutbol8(idEqAPrimar);
                            var comp = competicionActiva(eq.getClub().getGrupo(), claseComp);
                            var partido = obtenerProximoPartido(comp, eqAPrimar);
                            if (partido == null) throw new UnsupportedOperationException("No hay ningun partido previsto");
                            var alin = obtenerAlineacionPartido(partido, eqAPrimar);
                            alin.setPrima(cantidad, true);
                            grabarAlineacionFutbol8(alin);
                            eq.setPresupuesto(eq.getPresupuesto() - cantidad);
                            mov = new Movimiento(eq, Primas, cantidad);
                            grabarRegistro(mov);
                        }
                    }catch(Exception ex){
                        req.setAttribute("errorPrimas", ex.getMessage());
                    }
                }
                case "Activar" -> {
                    op = null;
                    vac.setActivo(true);
                    grabarVacacionFutbol8(vac);
                }
                case "Desactivar" -> {
                    op = null;
                    vac.setActivo(false);
                    grabarVacacionFutbol8(vac);
                }
                case "Grabar Ajustes" -> {
                    if (op != null) {
                        grabarEquipoFutbol8((EquipoFutbol8) eq);
                    }
                }
                default -> {
                }
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
        
        var panel = req.getParameter("panel");
        if (panel == null) panel = "miEquipo";
        req.setAttribute("panel", panel);
        
        TacticaFutbol8 tacSeleccionada;
        TacticaFutbol8 tacSeleccionadaSimul;
        var sesion = req.getSession();
        var unPartido = false;
        var resultados = false;
        var tiros = false;
        TacticaFutbol8 tacSimul;
        var simulRandom = false;
        EquipoFutbol8 eqSimul = null;
        
        long idEquipo = (Long) req.getSession().getAttribute("idEquipo");
        
        var eq = obtenerSimpleEquipoFutbol8(idEquipo);
        var datos = datosProximoPartido(eq, req.getServletContext());
        var comp = (CompeticionFutbol8) datos.get(1);        
        PartidoFutbol8 partido = null;
        
        if (comp != null)
            partido = obtenerDatosPrepararPartido(comp, eq, eqSimul);
        
        if (partido == null) return;
        
        // Si no existe equipo simulado lo creamos
        if (sesion.getAttribute("equipoSimul") == null){
             if (partido.getEqLocal().getId() != idEquipo)
                eqSimul = (EquipoFutbol8) partido.getEqLocal();
            else
                eqSimul = (EquipoFutbol8) partido.getEqVisitante();
            
            eqSimul.limpiarJugadoresQueJuegan();
            
            var ali = new AlineacionFutbol8(eqSimul, partido);
            eqSimul.setAlineacion(ali);                
            
            tacSimul = eqSimul.getEntrenador().getTacticaAleatoria();                
            eqSimul.getAlineacion().setTactica(tacSimul); 
            eqSimul.hacerAlineacionAutomatica(tacSimul, true);
            
            sesion.setAttribute("equipoSimul", eqSimul);
                  
        }
        else 
            eqSimul = (EquipoFutbol8) sesion.getAttribute("equipoSimul");
            
        
        var op = (String) req.getParameter("operacion");
        // Operaciones con mi Equipo
        switch (panel) {
            case "miEquipo":
                {
                    var numTac = (String) req.getParameter("tacsel");
                    if (numTac == null) {
                        if (eq.getAlineacion().getTactica() != null)
                            tacSeleccionada = eq.getAlineacion().getTactica();
                        else
                            tacSeleccionada = eq.getEntrenador().getTacticas().get(0);
                    } else {
                        tacSeleccionada = eq.getEntrenador().getTactica(parseInt(numTac));
                        limpiarAlineacion(eq);
                    }
                    eq.getAlineacion().setTactica(tacSeleccionada);
                    grabarAlineacionFutbol8(eq.getAlineacion());
                    if (null == op) {
                    } // no hacemos nada
                    // no hacemos nada
                    else {
                        switch (op) {
                            case "Grabar":
                                var alineacion = (String) req.getParameter("alineacion");
                                grabarAlineacion(alineacion, eq, true);
                                var esfuerzo = (String) req.getParameter("esfuerzo");
                                eq.getAlineacion().setEsfuerzo(EsfuerzoFutbol8.valueOf(esfuerzo));
                                var estrategia = (String) req.getParameter("estrategia");
                                eq.getAlineacion().setEstrategia( EstrategiaFutbol8.valueOf(estrategia));
                                var primas = (String) req.getParameter("primas");
                                if (isNumero(primas)) {
                                    var num = parseInt(primas);
                                    if (num <= 0) {
                                    } else if (num > 0 && eq.getPresupuesto() < num) {
                                        req.setAttribute("error", "No tienes suficiente presupuesto para primar");
                                    } else {
                                        try {
                                            eq.getAlineacion().setPrima(num, false);
                                            eq.setPresupuesto(eq.getPresupuesto() - num);
                                            grabarEquipoFutbol8(eq);
                                            var mov = new Movimiento(eq, Primas, num);
                                            grabarRegistro(mov);
                                        }catch(Exception e){
                                            req.setAttribute("error", e.getMessage());
                                        }           }
                                }
                                grabarAlineacionFutbol8(eq.getAlineacion());
                                break;
                            case "Alineacion Automatica":
                                eq.hacerAlineacionAutomatica(tacSeleccionada, true);
                                for (var jug : eq.getJugadores()) {
                                    grabarJugadorFutbol8(jug);
                                }
                                grabarAlineacionFutbol8(eq.getAlineacion());
                                break;
                            default:
                                break;
                        }
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
                    var alineacionTxt = eq.getAlineacion().getAlineacionCodificada();
                    var noJuegan = codificarJugadores(eq.getJugadoresBanquillo());
                    if (!alineacionTxt.isEmpty() && !noJuegan.isEmpty())
                        alineacionTxt = alineacionTxt + ";";
                    alineacionTxt = alineacionTxt + noJuegan;
                    req.setAttribute("alineacionTxt", alineacionTxt);
                    break;
                }
            case "eqRival":
                {
                    if (sesion.getAttribute("equipoSimul") != null) {
                        eqSimul = (EquipoFutbol8) sesion.getAttribute("equipoSimul");
                        var numTacSimul = (String) req.getParameter("tacsel");
                        if (numTacSimul != null) {
                            if (parseInt(numTacSimul) != (eqSimul.getAlineacion().getTactica().getNumero())) {
                                eqSimul.getAlineacion().setTactica(tacticaFutbol8(parseInt(numTacSimul)));
                                simulRandom = true;
                            }
                        }
                    }
                    if (op == null) {
                    } // no hacemos nada
                    else if (op.equals("Grabar Simulacion")) {
                        var alineacion = (String) req.getParameter("alineacion");
                        grabarAlineacion(alineacion, eqSimul, false);
                        var esfuerzo = (String) req.getParameter("esfuerzoSimul");
                        eqSimul.getAlineacion().setEsfuerzo( EsfuerzoFutbol8.valueOf(esfuerzo));
                        var estrategia = (String) req.getParameter("estrategiaSimul");
                        eqSimul.getAlineacion().setEstrategia( EstrategiaFutbol8.valueOf(estrategia));
                        var primas = (String) req.getParameter("primasSimul");
                        if (isNumero(primas)) {
                            var num = parseInt(primas);
                            eqSimul.getAlineacion().setPrima(num, false);
                        }
                        sesion.setAttribute("equipoSimul", eqSimul);
                    }
                    //eqSimul.limpiarJugadoresQueJuegan();
                    var numTacSimul = (String) req.getParameter("tacsel");
                    partido.getAlineaciones().add(eqSimul.getAlineacion());
                    if (numTacSimul != null) {
                        tacSeleccionadaSimul = eqSimul.getEntrenador().getTactica(parseInt(numTacSimul));
                    } else {
                        tacSeleccionadaSimul = eqSimul.getAlineacion().getTactica();
                    }
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
                    var alineacionTxt = eqSimul.getAlineacion().getAlineacionCodificada();
                    var noJuegan = codificarJugadores(eqSimul.getJugadoresBanquillo());
                    if (!alineacionTxt.isEmpty() && !noJuegan.isEmpty())
                        alineacionTxt = alineacionTxt + ";";
                    alineacionTxt = alineacionTxt + noJuegan;
                    req.setAttribute("alineacionTxt", alineacionTxt);
                    break;
                }
            case "simular":
                if (null == op) {
                } // no hacemos nada
                // no hacemos nada
                else {
                    switch (op) {
                        case "Jugar un Partido":
                            partido = obtenerDatosPrepararPartido(comp, eq, eqSimul);
                            var est = new EstadisticaPartidoFutbol8(partido);
                            partido.setEstadistica(est);
                            partido.setTipo(comp.getClase());
                            partido.setIsSimulacion(true);
                            partido.jugarPartido();
                            // Pruebas de penalties
                            // if (partido.getGanador() == null)
                            //    partido.hacerTandaPenalties();
                            sesion.setAttribute("estadistica", est);
                            unPartido = true;
                            var jugsLocal = partido.getAlineacionEqLocal().jugadores();
                            var jugsVisitante = partido.getAlineacionEqVisitante().jugadores();
                            req.setAttribute("jugsEqLocal", jugsLocal);
                            req.setAttribute("jugsEqVisitante", jugsVisitante);
                            req.getSession().setAttribute("cronica", partido.getCronica());
                            break;
                        case "Calcular Resultados":
                            partido = obtenerDatosPrepararPartido(comp, eq, eqSimul);
                            var victorias = 0;
                            var empates = 0;
                            var derrotas = 0;
                            partido.setTipo(comp.getClase());
                            partido.setIsSimulacion(true);
                            for (var i = 1; i <= NUMERO_SIMULACIONES; i++) {
                                partido.jugarPartido();
                                if (partido.getGanador() == null) empates++;
                                else if (partido.getGanador().equals(eq)) victorias++;
                                else derrotas++;
                            }
                            req.setAttribute("vistoriasSilum", victorias);
                            req.setAttribute("empatesSilum", empates);
                            req.setAttribute("derrotasSilum", derrotas);
                            resultados = true;
                            break;
                        case "Simular Tiros":
                            partido = obtenerDatosPrepararPartido(comp, eq, eqSimul);
                            partido.setTipo(comp.getClase());
                            partido.setIsSimulacion(true);
                            partido.simulacionTiros();
                            var tirJugs = new ArrayList<JugadorFutbol8>();
                            tirJugs.addAll(partido.getAlineacionEqLocal().jugadoresCampo());
                            tirJugs.addAll(partido.getAlineacionEqVisitante().jugadoresCampo());
                            sort(tirJugs, new PosicionComparator());
                            var parJugs = new ArrayList<JugadorFutbol8>();
                            parJugs.add(partido.getAlineacionEqLocal().getPortero());
                            parJugs.add(partido.getAlineacionEqVisitante().getPortero());
                            req.setAttribute("tirJugs", tirJugs);
                            req.setAttribute("parJugs", parJugs);
                            tiros = true;
                            break;
                        default:
                            break;
                    }
                }
                req.setAttribute("unPartido", unPartido);
                req.setAttribute("resultados", resultados);
                req.setAttribute("tiros", tiros);
                break;
            default:
                break;
        }
        
        req.setAttribute("partido", partido);

    }
   

    private void calendario(HttpServletRequest req, EquipoFutbol8 eq) 
            throws DAOException {

        req.setAttribute("op", "calendario"); 
        req.setAttribute("titulo", "CALENDARIO");
        
        var idComp = req.getParameter("idComp");
        var idJornada = req.getParameter("id");
        var grp = eq.getClub().getGrupo();
        
        if (idJornada != null){
            var id = parseLong(idJornada);
            var jornada = 
                    obtenerJornada(id, grp);
            req.setAttribute("jornada", jornada);
            req.setAttribute("compJornada", jornada.getCompeticion());
        }
        else
        {
            CompeticionFutbol8 comp;
            if (idComp != null){
                var id = parseLong(idComp);
                comp = obtenerCompeticion(id);
            }
            else{
                var tipo = req.getParameter("tipo");
                comp = competicionActiva(grp, tipo);
            }
          
            
            if (comp != null){
                
                comp = obtenerDatosHistoCompeticion(comp);
                var jornadas = comp.getJornadas();
                var jornadasIda = new ArrayList<JornadaFutbol8>();
                var jornadasVuelta = new ArrayList<JornadaFutbol8>();
                JornadaFutbol8 jornadaFinal = null;
                if (jornadas.size() == 1)
                    jornadasIda = jornadas;
                else{                           
                    if (comp.getClase().equals("Liga")){
                        jornadasIda.addAll(jornadas.subList(0, (jornadas.size() / 2)));
                        jornadasVuelta.addAll(jornadas.subList(jornadas.size() / 2, jornadas.size()));                
                    }
                    else if (comp.getClase().equals("Copa")){
                        for (var i = 0; i < jornadas.size() - 1; i = i + 2) {
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
        
        var idComp = req.getParameter("comp");         
        
        CompeticionFutbol8 comp;
         
        if (idComp != null && !idComp.equals("")){
            var id = parseLong(idComp);
            comp = obtenerCompeticion(id);
        }
        else{
            comp = competicionActiva(eq.getClub().getGrupo(), "Liga");
        }
        
        if (comp != null)
            req.setAttribute("comp", comp.getId());
        
        var idEq = req.getParameter("equipoSeleccionado");
        if (idEq != null && !idEq.equals("")){
            eq = obtenerSimpleEquipoFutbol8(parseInt(idEq));
        }   
        
        if (eq != null)
            req.setAttribute("equipoSeleccionado", eq.getId());
        
        var eqs = listaEquiposFutbol8(eq.getClub().getGrupo(), true);
        var partidos = new ArrayList<EstadisticaResultadosFutbol8>();
        var equiposSeleccion = new HashSet<EquipoFutbol8>();
        if (comp != null && eq != null){            
            comp = obtenerDatosHistoCompeticion(comp);
            var jornadas = comp.getJornadas();
            for (var jor : jornadas) {
                if (jor.isDisputada()){
                    for (var partido : jor.getPartidos()) {                        
                            var est = partido.getEstadistica();
                            if (eq.getNombre().equals(est.getEqLocal()) || eq.getNombre().equals(est.getEqVisitante())){
                                var resul = new EstadisticaResultadosFutbol8(est, eq);
                                partidos.add(resul);
                            }
                            for (var equipo : eqs) {
                                if (equipo.getNombre().equals(est.getEqLocal()) || equipo.getNombre().equals(est.getEqVisitante()))
                                    equiposSeleccion.add(equipo);
                            }
                    }
                }
            }
        }
        
        req.setAttribute("seleccionEquipos", equiposSeleccion);
        req.setAttribute("partidos", partidos);
        req.setAttribute("mediaGanados", getMediaGanados(partidos));
        req.setAttribute("mediaEmpatados", getMediaEmpatados(partidos));
        req.setAttribute("mediaPerdidos", getMediaPerdidos(partidos));
        req.setAttribute("mediaOpcionesGanar", getMediaOpcionesGanar(partidos));
        req.setAttribute("mediaOpcionesEmpatar", getMediaOpcionesEmpatar(partidos));
        req.setAttribute("mediaOpcionesPerder", getMediaOpcionesPerder(partidos));
        
        
        
    }


     private void goleadores(HttpServletRequest req, EquipoFutbol8 eq) 
            throws DAOException {

        req.setAttribute("op", "goleadores");    
        req.setAttribute("titulo", "GOLEADORES");
        
        
        var idComp = (String) req.getParameter("idComp");
        var grp = eq.getClub().getGrupo();         
        
        if (idComp != null && !idComp.isEmpty()){
            List<GoleadorFutbol8> goleadores;
            var id = parseLong(idComp);
            var comp = obtenerCompeticion(id);
            goleadores = obtenerGoleadores(comp); 
            req.setAttribute("goleadores", goleadores);
        }
        else{
            List<JugadorFutbol8> goleadores;
            goleadores = obtenerGoleadores(grp); 
            req.setAttribute("goleadores", goleadores);
        }
        
               
        
        req.setAttribute("grupo", grp.getNombre()); 
        
     }
     
     private void porteros(HttpServletRequest req, EquipoFutbol8 eq) throws DAOException {
        
         req.setAttribute("op", "porteros");    
         req.setAttribute("titulo", "PORTEROS");
                
         var idComp = (String) req.getParameter("idComp");
         var grp = eq.getClub().getGrupo();         
        
         if (idComp != null && !idComp.isEmpty()){
             List<PorteroFutbol8> porteros;
             var id = parseLong(idComp);
             var comp = obtenerCompeticion(id);
             porteros = obtenerPorteros(comp); 
             req.setAttribute("porteros", porteros);
         }
         else{
             List<JugadorFutbol8> porteros;
             porteros = obtenerPorteros(grp); 
             req.setAttribute("porteros", porteros);
         }
                  
         
         req.setAttribute("grupo", grp.getNombre()); 
    }
   
    private void clasificacion(HttpServletRequest req, EquipoFutbol8 eq)
            throws DAOException {

        req.setAttribute("op", "clasificacion");
        req.setAttribute("titulo", "CLASIFICACION");

        var grp = eq.getClub().getGrupo();
        var idComp = (String) req.getParameter("comp");

        
        ArrayList<EquipoFutbol8> eqs;
        if (idComp == null)
            eqs = obtenerClasificacion(grp);
        else{
            var comp = competicion(parseLong(idComp));
            eqs = obtenerClasificacion(comp);
        }
        req.setAttribute("equipos", eqs);

        
    }

    private void movimientos(HttpServletRequest req, EquipoFutbol8 eq)
            throws DAOException {

        req.setAttribute("op", "movimientos");
        req.setAttribute("titulo", "MOVIMIENTOS");
        
        var movs = new ArrayList<Movimiento>();
        var op = (String) req.getParameter("operacion");
        var todo = false;
        
        if (op == null || op.equals("Ver ultimos 30 dias")) {
            // por defecto mostramos los ultimos 30 dias
            var c = new GregorianCalendar();
            var diaActual = new Date();
            c.setGregorianChange(diaActual);
            c.add(DAY_OF_YEAR, 1);
            diaActual = c.getTime();
            c.add(DAY_OF_YEAR, -30);
            var diaInicial = c.getTime();
            movs = obtenerMovimientos(eq, diaInicial, diaActual);
            todo = false;
        }   
        else if (op.equals("Ver Todos")) {
            movs = obtenerMovimientos(eq);
            todo = true;
        }   
        
        
        movs = obtenerExtracto(eq, movs);

        req.setAttribute("idEquipo", eq.getId());
        req.setAttribute("movimientos", movs);
        req.setAttribute("todo", todo);
    }
    
    private void competiciones(HttpServletRequest req, EquipoFutbol8 eq, 
            boolean isGrupoActivo)
            throws DAOException {

        req.setAttribute("op", "competiciones");
        req.setAttribute("titulo", "COMPETICIONES");
        
        var grp = eq.getClub().getGrupo();
        var competiciones = new ArrayList<CompeticionFutbol8>();

        if (isGrupoActivo){ 
            competiciones = listaCompetionesFinalizadas(grp);
        }else{
            var grupos = obtenerGruposActivos();
            for (var grupo : grupos) {
                ArrayList<CompeticionFutbol8> comps;
                if (!grupo.equals(grp)){
                     comps = listaCompetionesGrupo(grupo);
                     if (!comps.isEmpty())
                         competiciones.addAll(comps);
                }
            }
            
        }
        ordenarFechaDescendente(competiciones);
        req.setAttribute("competiciones", competiciones);
        
    }
    
     private void entrenamiento(HttpServletRequest req, EquipoFutbol8 eq) 
             throws DAOException {
        
         req.setAttribute("op", "entrenamiento");
         req.setAttribute("titulo", "ENTRENAMIENTO");
         
         //ArrayList<JugadorFutbol8> jugadores = JDBCDAOFutbol8.obtenerJugadores(eq);
         
         var op = (String) req.getParameter("operacion");
         var pos = (String) req.getParameter("posicion");
         
         if (pos == null) pos = "Todas";
         
         var posiciones = new ArrayList();
         posiciones.add("Todas");
         posiciones.addAll(posicionesJugador());
         
         obtenerJuvenil(req, eq);
         
         var jugsEntr = jugadoresEntrenables(eq, pos);
         
         if (op == null){
             
         }else if (op.equals("Entrenar")){
             jugsEntr = entrenar(eq, pos);      
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
                     
         var jugadores = 
                 obtenerJugadoresGrupo(eq.getClub().getGrupo());
         var jugadoresCompra = 
                 filtrarJugadoresCompra(jugadores, eq);
         var jugadoresSubasta = 
                 filtrarJugadoresSubasta(jugadores, eq);
         var op = (String) req.getParameter("operacion");
         Movimiento mov;
         int cantidad;
         
         try {
         
         if (null == op){
             
         }else switch (op) {
                case "Pujar" -> {
                    for (var jug : jugadoresSubasta) {
                        var id = String.valueOf(jug.getId());
                        if (req.getParameter(id) != null && req.getParameter(id).equals("on")) {
                            var puja = parseInt(req.getParameter("puja"+id));
                            jug.hacerPuja(eq, puja);
                            grabarJugadorFutbol8(jug);
                            var txt = "Has realizado una puja por el jugador " + jug.getNombre() + " por la cantidad de " + puja;
                            getCorreo().enviarMail("ClubDeportivo Puja Jugador Futbol8", txt, true, eq.getClub().getMail());
                        }
                    }
             }
                case "Comprar" -> {
                    if (!horaCompraValida()) throw new
                                     UnsupportedOperationException("No es posible la compra, esta fuera de horario");
                    for (var jug : jugadoresCompra) {
                        var id = String.valueOf(jug.getId());
                        if (req.getParameter(id) != null && req.getParameter(id).equals("on")) {
                            var eqVenta = jug.getEquipo();
                            var clausula = jug.getClausula();
                            cantidad = jug.hacerCompra(eq);
                            var conf = obtenerConfig(jug.getGrupo());
                            var iva = cantidad * conf.getIva() / 100;
                            eq.setPresupuesto(eq.getPresupuesto() - iva);
                            mov = new Movimiento(eq, Fichajes, cantidad + iva, jug.getNombre());
                            grabarRegistro(mov);
                            mov = new Movimiento(eqVenta, VentaJugadores, clausula, jug.getNombre());
                            grabarRegistro(mov);
                            grabarJugadorFutbol8(jug);
                            grabarEquipoFutbol8(eq);
                            grabarEquipoFutbol8(eqVenta);
                            var txt = "El jugador " + jug.getNombre() + " ha sido"
                                    + " fichado por el " + eq.getNombre() + " procedente del " + eqVenta.getNombre() +
                                    " por " + cantidad + " mas el iva de " + iva;
                            darNoticia(eq.getClub().getGrupo(), txt);
                            jugadoresCompra.remove(jug);
                            getCorreo().enviarMail("ClubDeportivo Compra Jugador Futbol8", txt, true, eqVenta.getClub().getMail());
                        }
                    }
             }
                default -> {
             }
            }
         
         } catch (Exception ex){
             req.setAttribute("error", ex.getMessage());
         }
         
         sort(jugadoresCompra, new PosicionComparator());
         sort(jugadoresSubasta, new PosicionComparator());
         
         if (!jugadoresCompra.isEmpty())
             req.setAttribute("jugadoresCompra", jugadoresCompra);
         if (!jugadoresSubasta.isEmpty())
             req.setAttribute("jugadoresSubasta", jugadoresSubasta);
         
    }
    
     private void plantilla(HttpServletRequest req, EquipoFutbol8 eq) 
             throws DAOException {
         
        req.setAttribute("op", "plantilla");
        req.setAttribute("titulo", "PLANTILLA");
        
        var jugadores = obtenerJugadores(eq);
        sort(jugadores, new PosicionComparator());
        eq.setJugadores(jugadores);
        var jugadoresTmp = new ArrayList<JugadorFutbol8>();
        jugadoresTmp.addAll(jugadores);
        obtenerJuvenil(req, eq);
        
        var op = (String) req.getParameter("operacion");
        
        try{
        
        if (null == op){
            
        }else switch (op) {
                case "Despedir" -> {
                    for (var jug : jugadoresTmp) {
                        var id = String.valueOf(jug.getId());
                        if (req.getParameter(id) != null && req.getParameter(id).equals("on")) {
                            var coste = jug.despedir();
                            jugadores.remove(jug);
                            eliminarJugadorFutbol8(jug);
                            grabarEquipoFutbol8(eq);
                            var mov = new Movimiento(eq, Indemnizacion, coste, jug.getNombre());
                            grabarRegistro(mov);
                            var txt = "El jugador " + jug.getNombre() + " ha sido"
                                    + " despedido por el " + eq.getNombre();
                            darNoticia(eq.getClub().getGrupo(), txt);
                        }
                    }
            }
                case "Blindar" -> {
                    for (var jug : jugadoresTmp) {
                        var id = String.valueOf(jug.getId());
                        if (req.getParameter(id) != null && req.getParameter(id).equals("on")) {
                            var coste = jug.blindar();
                            grabarJugadorFutbol8(jug);
                            grabarEquipoFutbol8(eq);
                            var mov = new Movimiento(eq, Blindajes, coste, jug.getNombre());
                            grabarRegistro(mov);
                            var txt = "El jugador " + jug.getNombre() + " ha sido"
                                    + " blindado por el " + eq.getNombre();
                            darNoticia(eq.getClub().getGrupo(), txt);
                        }
                    }
            }
                case "Mejorar Contrato" -> {
                    for (var jug : jugadoresTmp) {
                        var id = String.valueOf(jug.getId());
                        if (req.getParameter(id) != null && req.getParameter(id).equals("on")) {
                            jug.mejorarContrato();
                            grabarJugadorFutbol8(jug);
                            grabarEquipoFutbol8(eq);
                            var txt = "Al jugador " + jug.getNombre() + " se le ha "
                                    + " mejorado el contrato por el " + eq.getNombre();
                            darNoticia(eq.getClub().getGrupo(), txt);
                        }
                    }
            }
                case "Renovar" -> {
                    for (var jug : jugadoresTmp) {
                        var id = String.valueOf(jug.getId());
                        if (req.getParameter(id) != null && req.getParameter(id).equals("on")) {
                            renovarJugador(jug, eq);
                        }
                    }
            }
                case "Poner Transferible" -> {
                    for (var jug : jugadoresTmp) {
                        var id = String.valueOf(jug.getId());
                        if (req.getParameter(id) != null && req.getParameter(id).equals("on")) {
                            jug.ponerTransferible();
                            grabarJugadorFutbol8(jug);
                            var txt = "El jugador " + jug.getNombre() + " se ha "
                                    + " declarado transferible por el " + eq.getNombre();
                            darNoticia(eq.getClub().getGrupo(), txt);
                        }
                    }
            }
                case "Subastar" -> {
                    for (var jug : jugadoresTmp) {
                        var id = String.valueOf(jug.getId());
                        if (req.getParameter(id) != null && req.getParameter(id).equals("on")) {
                            jug.subastar();
                            grabarJugadorFutbol8(jug);
                            var txt = "El jugador " + jug.getNombre() + " se ha "
                                    + " puesto en subasta por el " + eq.getNombre();
                            darNoticia(eq.getClub().getGrupo(), txt);
                        }
                    }
            }
                case "Incorporar Ojeado" -> {
                    if (eq.getJugadoresOjeados() > 0 && eq.getJugadores().size() < 16) {
                        var newJug = crearJugadorOjeadofutbol8(eq);
                        eq.setJugadoresOjeados(eq.getJugadoresOjeados() - 1);
                        grabarEquipoFutbol8(eq);
                        var txt = "El jugador " + newJug.getNombre() + " ojeado "
                                + " por el " + eq.getNombre() + " se ha incorparado al equipo";
                        darNoticia(eq.getClub().getGrupo(), txt);
                    }
            }
                case "Incorporar Juvenil" -> {
                    var juv = eq.getJuvenil();
                    if (juv != null && juv.isIncorporacion() && eq.getJugadores().size() < 16) {
                        var newJug = crearJugadorfutbol8(eq, juv.getPosicion(), juv.getValoracionReal());
                        eliminarJuvenilFutbol8(juv);
                        eq.setJuvenil(null);
                        req.setAttribute("juvenil", null);
                        var txt = "El juvenil " + newJug.getNombre() + " se ha incorporado a "
                                + " la plantilla del " + eq.getNombre();
                        darNoticia(eq.getClub().getGrupo(), txt);
                    }
            }
                default -> {
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
         
         var idJug = (String) req.getParameter("id");
         
         long id = 0;
         if (idJug != null && !idJug.isEmpty())
             id = parseLong(idJug);
         JugadorFutbol8 jug = null;
         
         try{     
             jug = obtenerJugador(id, eq);
             if (jug == null) throw new UnsupportedOperationException("Jugador no Encontrado");
             jug.setGrupo(eq.getClub().getGrupo());
             jug.setEquipo(eq);
             var nombre = (String) req.getParameter("nombre");
             //nombre = new String(nombre.getBytes(), "UTF-8"); 
             if (nombre != null && !nombre.isEmpty()){
                 nombre = removeCharsEspeciales(nombre);
                 nombre = truncate(nombre, 40);
                 if (nombre.isEmpty())
                     req.setAttribute("error", "El nombre propuesto no es valido, no es posible realizar el cambio");
                 else{
                     var existe = existeJugador(nombre, eq.getClub().getGrupo());
                     if (existe)
                         req.setAttribute("error", "Este nombre ya existe, no es posible realizar el cambio");
                     else{  
                         jug.setNombre(nombre);
                         grabarJugadorFutbol8(jug);
                     }                     
                 }
             }
             
         }catch (Exception ex){
            req.setAttribute("error", ex.getMessage());
        }
         
         req.setAttribute("jugador", jug);        
        
    }
     
      private JuvenilFutbol8 obtenerJuvenil(HttpServletRequest req, EquipoFutbol8 eq) throws DAOException {
          
          var juvenil = JDBCDAOFutbol8.obtenerJuvenil(eq);
          eq.setJuvenil(juvenil);
          
          req.setAttribute("juvenil", juvenil);
          return juvenil;
          
      }
     
     
      private void entrenador(HttpServletRequest req, EquipoFutbol8 eq) throws DAOException {

        req.setAttribute("op", "entrenador");
        req.setAttribute("titulo", "ENTRENADOR");
        
        var listaEntrLibres = 
                obtenerEntrenadoresLibresGrupo(eq.getClub().getGrupo());
        var op = (String) req.getParameter("operacion");
        Movimiento mov;
        
        try{ 
        
            if (op == null){            
            }else if (op.equals("Contratar") && req.getParameter("newEntrenador") != null){
                var actual = eq.getEntrenador();
                Long idNuevo = parseLong(req.getParameter("newEntrenador"));
                var nuevo = obtenerEntrenador(idNuevo);
                 nuevo.contratar(actual); 
                 eq.setEntrenador(nuevo);
                 nuevo.setEquipo(eq);
                 eq.setMoral(MORAL_INICIAL);
                 grabarEquipoFutbol8(eq);
                 grabarEntrenadorFutbol8(nuevo); 
                 var txt = "El entrenador " + nuevo.getNombre() + " ha sido "
                         + " fichado por el " + eq.getNombre() + " y ha despedido"
                         + " ha " + actual.getNombre() + " con una indemnizacion de "
                         + actual.getIndemnizacion() + " Euros";
                 darNoticia(eq.getClub().getGrupo(), txt);
                 mov = new Movimiento(eq, Indemnizacion, 
                         actual.getIndemnizacion(), actual.getNombre());
                 grabarRegistro(mov);
                 
                for (var entr : listaEntrLibres) 
                    if (entr.getId() == idNuevo){
                        listaEntrLibres.remove(entr);
                        break;
                    }

                eliminarEntrenadorFutbol8(actual);
            
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
       
        
        var op = (String) req.getParameter("operacion");
        req.setAttribute("titulo", "CURSO DE ENTRENADORES");
        
        if (op == null){}
        else if (op.equals("Realizar Curso")){
            var numTac = (String) req.getParameter("tactSel");
            var tact = tacticaFutbol8(parseInt(numTac));
            eq.getEntrenador().anyadirTacticaCurso(tact);
            grabarEntrenadorFutbol8(eq.getEntrenador());
            grabarEquipoFutbol8(eq);
            var mov = new Movimiento(eq, Cursos, PRECIO_CURSO);
            grabarRegistro(mov);
            var txt = "El entrenador " + eq.getEntrenador().getNombre() + " del " + eq.getNombre() +
                         " ha realizado un curso de tacticas";
            darNoticia(eq.getClub().getGrupo(), txt);
                 
        }
        
         var listaTacts = tacticasFutbol8();
        var listaFinal = new ArrayList<>();
        for (var tactlista : listaTacts) {
            var anyadir = true;
            for (var tact : eq.getEntrenador().getTacticas()) {
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
        
        var numero = 100;
        var num = (String) req.getParameter("numero");

        if (num != null)
            numero = parseInt(num);
        
        var noticias = 
                obtenerNoticias(grp, Futbol8, numero);        
       
        req.setAttribute("noticias", noticias);
        req.setAttribute("numero", numero);
               
         
        
    }
    
    private void comentarios(HttpServletRequest req, Club club) throws DAOException {
        
        req.setAttribute("op", "comentarios");   
        req.setAttribute("titulo", "COMENTARIOS");
        req.setAttribute("inicio", false);
        
        var numero = 100;
        var num = (String) req.getParameter("numero");

        if (num != null)
            numero = parseInt(num);
        
        tratarComentarios(req, club, false, numero);  
        
        req.setAttribute("numero", numero);
        
    }
    
     private void auditoria(HttpServletRequest req, Grupo grp) throws DAOException {
        
        req.setAttribute("op", "auditoria");   
        req.setAttribute("titulo", "AUDITORIA");

        var equipos = listaEquiposFutbol8(grp, true);
        ArrayList<Movimiento> movs;
        
        for (var eq : equipos) {
            movs = obtenerMovimientos(eq);
            for (var mov : movs) {
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
        
        var config = obtenerConfig(eq.getClub().getGrupo());
        var modificable = false;
        var op = (String) req.getParameter("operacion");
         
         if (op == null){
             
         }else if (op.equals("Modificar") && config.isModificable(eq)){
             try {
                 var interesesCredito = (String) req.getParameter("interesesCredito");
                 var retencionHaciendaBase = (String) req.getParameter("retencionHaciendaBase");
                 var retencionLineal = (String) req.getParameter("retencionLineal");
                 var iva = (String) req.getParameter("iva");
                 var ibi = (String) req.getParameter("ibi");
                 var subidaMaxBolsa = (String) req.getParameter("subidaMaxBolsa");
                 var crackBolsa = (String) req.getParameter("crackBolsa");
                 var porcentajePremioLiga = (String) req.getParameter("porcentajePremioLiga");
                 var porcentajeCampeonCopa = (String) req.getParameter("porcentajeCampeonCopa");
                 var interesesCreditoValor = parseInt(interesesCredito);
                 var retencionHaciendaBaseValor = parseInt(retencionHaciendaBase);
                 var retencionLinealValor = false;
                 if (retencionLineal != null && retencionLineal.equals("on")) 
                     retencionLinealValor = true;                 
                 var ivaValor = parseInt(iva);
                 var ibiValor = parseInt(ibi);
                 var subidaMaxBolsaValor = parseInt(subidaMaxBolsa);
                 var crackBolsaValor = false;
                 if (crackBolsa != null && crackBolsa.equals("on")) 
                     crackBolsaValor = true;                 
                 var porcentajePremioLigaValor = parseInt(porcentajePremioLiga);
                 var porcentajeCampeonCopaValor = parseInt(porcentajeCampeonCopa);
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
                 
                 grabarConfigEconomiaFutbol8(config);
                 
                 var txt = "El " + eq.getNombre() + " ha cambiado la configuración económica del grupo";
                 darNoticia(eq.getClub().getGrupo(), txt);
                 
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
                 var correos = mailsClubs(eq.getClub().getGrupo(), Futbol8);
                 getCorreo().enviarMailMasivo("ClubDeportivo Cambio Configuración Futbol8", txt, true, correos); 
                 
                 
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

        var jugadores = eq.getJugadores();
        for (var jug : jugadores) {
            jug.setJuegaJornada(false);
        }
        var alineacion = eq.getAlineacion();
        alineacion.limpiarAlineacion();

        if (!txtAlineacion.isEmpty()){
            for (var datos : txtAlineacion.split(";")) {
                var dato = datos.split("_");
                var posicion = dato[0];
                var idJugador = parseLong(dato[1]);
                var jug = eq.getJugador(idJugador);
                jug.setJuegaJornada(true);
                alineacion.setDato(posicion, jug);
            }
        }

        if (grabarEnDIsco){
            for (var jug : jugadores) 
                grabarJugadorFutbol8(jug);       

            grabarAlineacionFutbol8(alineacion);
        }

    }

    private void limpiarAlineacion(EquipoFutbol8 eq) throws DAOException {

        var jugadores = eq.getJugadores();
        for (var jug : jugadores) {
            jug.setJuegaJornada(false);
        }
        var alineacion = eq.getAlineacion();
        alineacion.limpiarAlineacion();

        for (var jug : jugadores) {
            grabarJugadorFutbol8(jug);
        }

        grabarAlineacionFutbol8(alineacion);


    }

   
    private ArrayList<JugadorFutbol8> filtrarJugadoresCompra(
            ArrayList<JugadorFutbol8> jugadores, EquipoFutbol8 eq) {
        
        var lista = new ArrayList<JugadorFutbol8>();
        for (var jug : jugadores) {
            if (!jug.isEnSubasta() && !jug.isBlindado()
                    && !jug.getEquipo().equals(eq)) lista.add(jug);
        }
        
        return lista;
    }

   private ArrayList<JugadorFutbol8> filtrarJugadoresSubasta(
            ArrayList<JugadorFutbol8> jugadores, EquipoFutbol8 eq) {
       
        var lista = new ArrayList<JugadorFutbol8>();
        for (var jug : jugadores) {
            if (jug.isEnSubasta() && (jug.getEquipo() == null ||
                    !jug.getEquipo().equals(eq))) lista.add(jug);
        }
        
        return lista;
   }

   
   
    
  
    private boolean horaCompraValida() {
        
        var horaMin = 14;
        var horaMax = 24;
        var cal = getInstance(); // devuelve una instancia de Calendar con la fecha actual
        var hora = cal.get(HOUR_OF_DAY);

        return hora >= horaMin && hora < horaMax;
        
    }

  

   
  

   

    


}



     

