
package mld.clubdeportivo.controladores;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Long.parseLong;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import static java.util.Calendar.DAY_OF_YEAR;
import mld.clubdeportivo.base.Bolsa;
import mld.clubdeportivo.base.Movimiento;
import mld.clubdeportivo.base.futbol8.*;
import static mld.clubdeportivo.base.futbol8.CronicaFutbol8.datosJsonCronicaFutbol8;
import mld.clubdeportivo.base.quinielas.CompeticionQuiniela;
import mld.clubdeportivo.base.quinielas.JornadaQuiniela;
import mld.clubdeportivo.bd.DAOException;
import static mld.clubdeportivo.bd.JDBCDAOBolsa.obtenerDatosBolsa;
import static mld.clubdeportivo.bd.JDBCDAOMovimiento.obtenerMovimientos;
import static mld.clubdeportivo.bd.JDBCDAOMovimiento.obtenerMovimientosPorClase;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerAlineacionPartido;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerCronica;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerPartido;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerSimpleEquipoFutbol8;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.competicionActiva;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerCompeticionPorId;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerEstadistica;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerJornadasValidadas;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerSimpleEquipoQuiniela;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 *
 * @author Miguel
 */


@Controller
public class JsonServlet  {

   
    @GetMapping("/json/posicionesQuiniela")
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

         processRequest(req, resp);
    }

    
    private String formatJsonpresupuesto(String tipo, int valor){
        // Ejemplo {"Ingresos Campo": 100}
        return "{" + formatJson("tipo", tipo) + "," + formatJson("valor", valor) + "}";        
    }
    
    private String formatJson(String campo, int valor){
        return "\"" + campo + "\":" + valor;        
    }
    
    private String formatJson(String campo, long valor){
        return "\"" + campo + "\":" + valor;        
    }

    private String formatJson(String campo, String valor){
        return "\"" + campo + "\":\"" + valor + "\"";        
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
       
        var txt = "";
        var path = req.getRequestURI();
        String accion = path.substring(path.lastIndexOf("/") + 1);        
        
        try {
            if (null == accion) {}
            else switch (accion) {
                case "bolsa":
                    txt = datosBolsa(req);
                    break;
                case "presupuesto":
                    txt = datosPresupuestoFutbol8(req);
                    break;
                case "movimientos":
                    txt = datosMovimientos(req);
                    break;
                case "auditoria":
                    txt = datosAuditoria(req);
                    break;
                case "posicionesQuiniela":
                    txt = datosPosicionesQuiniela(req);
                    break;
                case "jugadoresFutbol8":
                    txt = datosJugadoresFutbol8(req);
                    break;
                case "cronicaFutbol8":
                    txt = datosCronicaFutbol8(req);
                    break;
                default:
                    break;
            }
        } catch (DAOException ex) {
            
        }
         
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        //txt = new String(txt.getBytes(), "UTF-8");
        resp.getWriter().print(txt);
       
    }

    private String datosBolsa(HttpServletRequest req) throws DAOException {
 
        var fecha = req.getParameter("fecha");
        var txt = new StringBuilder();
        txt.append('[');
        
        var lista = new ArrayList<Bolsa>();
        if (fecha == null || fecha.isEmpty())
            lista = obtenerDatosBolsa();
        else {
             var formato = new SimpleDateFormat("dd/MM/yyyy");
             try {
                 var fechaDate = formato.parse(fecha);
                 lista = obtenerDatosBolsa(fechaDate);
             } catch (ParseException ex) { }
        }
            
        var primero = true;
        for (var elem : lista) {
            if (!primero)
                txt.append(',');
            else 
                primero = false;
            txt.append(elem.toJson()); 
        }   
        txt.append(']');

        return txt.toString();
    }

    private String datosPresupuestoFutbol8(HttpServletRequest req) throws DAOException {
        
        var txt = new StringBuilder();
        txt.append('[');
        
        var idTxt = req.getParameter("equipo");
        var tipo = req.getParameter("tipo");
        long id;
        if (idTxt != null && tipo != null){ 
            id = parseLong(idTxt); 
            var eq = obtenerSimpleEquipoFutbol8(id);
            if (eq != null){
                switch (tipo) {
                    case "gastos":
                        txt.append(formatJsonpresupuesto("Entrenador", eq.getCosteEntrenador()));
                        txt.append(',');
                        txt.append(formatJsonpresupuesto("Eq. Tec.", eq.getCosteEquipoTecnico()));
                        txt.append(',');
                        txt.append(formatJsonpresupuesto("Jugadores", eq.getCosteJugadores()));
                        txt.append(',');
                        txt.append(formatJsonpresupuesto("Ojeadores", eq.getCosteOjeadores()));
                        txt.append(',');
                        txt.append(formatJsonpresupuesto("Coste Aprox. Gestion", eq.getCosteMedioGestion()));
                        txt.append(',');
                        txt.append(formatJsonpresupuesto("Coste Aprox. Mant. Estadio", eq.getCosteMedioMantenimiento()));
                        break;
                    case "ingresos":
                        txt.append(formatJsonpresupuesto("Ingresos Aprox. Campo", eq.getIngresosMediosCampo()));
                        txt.append(',');
                        txt.append(formatJsonpresupuesto("Ingresos Aprox. Publi.", eq.getIngresosMediosPublicidad()));
                        break;
                    case "balance":
                        txt.append(formatJsonpresupuesto("Ingresos Aproximados", eq.getIngresosMediosCampo() + eq.getIngresosMediosPublicidad()));
                        txt.append(',');
                        txt.append(formatJsonpresupuesto("Gastos Aproximados", eq.getCosteTotal()));
                        break;
                    default:
                        break;
                }
            }
        }
        txt.append(']');

        return txt.toString();
        
        
    }
    
   

    private String datosMovimientos(HttpServletRequest req) throws DAOException {
        
        var txt = new StringBuilder();
        txt.append('[');
        
        var idTxt = req.getParameter("equipo");
        var tipo = req.getParameter("tipo");
        var todo = req.getParameter("total");
        long id;
        if (idTxt != null && todo != null){ 
            id = parseLong(idTxt); 
            var isTodo = parseBoolean(todo);
            var eq = obtenerSimpleEquipoFutbol8(id);
            if (eq != null){
                ArrayList<Movimiento> movs;
                if (isTodo)
                    movs = obtenerMovimientos(eq);
                else{
                    var c = new GregorianCalendar();
                    var diaActual = new Date();
                    c.setGregorianChange(diaActual);
                    c.add(DAY_OF_YEAR, 1);
                    diaActual = c.getTime();
                    c.add(DAY_OF_YEAR, -30);
                    var diaInicial = c.getTime();
                    movs = obtenerMovimientos(eq, diaInicial, diaActual);
                }
                var ingresos = new HashMap<String,Integer>();
                var gastos = new HashMap<String,Integer>();
                for (var mov : movs) {
                    if (mov.isPositivo()){
                        if (ingresos.get(mov.getClaseTxtSimple()) == null)
                            ingresos.put(mov.getClaseTxtSimple(), 0);
                        ingresos.put(mov.getClaseTxtSimple(), ingresos.get(mov.getClaseTxtSimple()) + mov.getValor());
                    }
                    if (!mov.isPositivo()){
                        if (gastos.get(mov.getClaseTxtSimple()) == null)
                            gastos.put(mov.getClaseTxtSimple(), 0);
                        gastos.put(mov.getClaseTxtSimple(), gastos.get(mov.getClaseTxtSimple()) + mov.getValor());
                    }                        
                }      
                
                Iterator it;
                var totalGastos = 0;
                var totalIngresos = 0;                
                it = gastos.entrySet().iterator();
                var primero = true;
                while (it.hasNext()) {
                    var e = (Map.Entry)it.next();
                    if (tipo.equals("gastos")){
                        if (primero) primero = false;
                        else txt.append(',');
                        txt.append(formatJsonpresupuesto((String) e.getKey(),(Integer) e.getValue()));
                    }
                    totalGastos = totalGastos + (Integer) e.getValue();
                } 
                it = ingresos.entrySet().iterator();
                primero = true;
                while (it.hasNext()) {
                    var e = (Map.Entry)it.next();
                    if (tipo.equals("ingresos")){
                        if (primero) primero = false;
                        else txt.append(',');
                        txt.append(formatJsonpresupuesto((String) e.getKey(),(Integer) e.getValue())); 
                    }
                    totalIngresos = totalIngresos + (Integer) e.getValue();
                }
                if (tipo.equals("balance")){
                    txt.append(formatJsonpresupuesto("Ingresos", totalIngresos)); 
                    txt.append(',');
                    txt.append(formatJsonpresupuesto("Gastos", totalGastos));                     
                }
            }
        }
        txt.append(']');

        return txt.toString();
    }

    private String datosPosicionesQuiniela(HttpServletRequest req) throws DAOException {
        
        var txt = new StringBuilder();
        txt.append('[');
        
        var idTxt = req.getParameter("equipo");
        
        if (idTxt != null){ 
            var id = parseLong(idTxt);
            var eq = obtenerSimpleEquipoQuiniela(id);
            var idComp = req.getParameter("competicion");
            CompeticionQuiniela comp;
            if (idComp == null){
                comp = competicionActiva();
            }
            else{
                var idLong = parseLong(idComp);
                comp = obtenerCompeticionPorId(idLong);
            }
            
            if (eq != null && comp != null){
        
                var jornadas =
                        (ArrayList<JornadaQuiniela>) obtenerJornadasValidadas(comp);
                var posiciones = new HashMap<Integer, Integer>();
                for (var jornada : jornadas) { 
                    var est = 
                            obtenerEstadistica(eq, comp, jornada);
                    var pos = est.getPosicion();
                    if (!posiciones.containsKey(pos))
                        posiciones.put(pos, 1);
                    else
                        posiciones.put(pos, posiciones.get(pos) + 1);
                }
                var primero = true;
                for (Map.Entry e : posiciones.entrySet()) {
                    if (primero) primero = false;
                    else txt.append(',');
                    var txtPos = "";
                    if ((Integer) e.getKey() == 1) txtPos = "Primero";
                    else if ((Integer) e.getKey() == 2) txtPos = "Segundo";
                    else if ((Integer) e.getKey() == 3) txtPos = "Tercero";
                    else if ((Integer) e.getKey() == 4) txtPos = "Cuarto";
                    else if ((Integer) e.getKey() == 5) txtPos = "Quinto";
                    else if ((Integer) e.getKey() == 6) txtPos = "Sexto";
                    else if ((Integer) e.getKey() == 7) txtPos = "Septimo";
                    else if ((Integer) e.getKey() == 8) txtPos = "Octavo";
                    else if ((Integer) e.getKey() == 9) txtPos = "Noveno";
                    else if ((Integer) e.getKey() == 10) txtPos = "Decimo";
                    else if ((Integer) e.getKey() > 4) txtPos = "Otros";
                    txt.append(formatJsonpresupuesto(
                            txtPos, (Integer) e.getValue()));
                }
            }
        }
        
        txt.append(']');

        return txt.toString();
    }

    private String datosJugadoresFutbol8(HttpServletRequest req) throws DAOException {
        
        var txt = new StringBuilder();
        txt.append('[');
        
        var idEqTxt = req.getParameter("equipo");
        var idParTxt = req.getParameter("partido");
        
        EquipoFutbol8 eq = null;
        PartidoFutbol8 partido;
        AlineacionFutbol8 ali = null;
        
        if (idEqTxt != null && idParTxt != null){ 
            var idEq = parseLong(idEqTxt);
            eq = obtenerSimpleEquipoFutbol8(idEq); 
            var idPar = parseLong(idParTxt);
            partido = obtenerPartido(idPar);
            if (eq != null && partido != null)
                ali = obtenerAlineacionPartido(partido, eq);
        }
        
        if (ali != null){        
            var jugadores = eq.getJugadores();
            var primero = true;
            for (var jug : jugadores) {
                if (primero) primero = false;
                else txt.append(',');
                txt.append('{');
                txt.append(formatJson("id", jug.getId())).append(',');
                txt.append(formatJson("nombre", jug.getNombre())).append(',');
                txt.append(formatJson("posicion", jug.getPosicion().name())).append(',');
                txt.append(formatJson("valoracionMedia", jug.getValoracionMedia()));
                txt.append('}');
            }            
        }
        
        txt.append(']');

        return txt.toString();
    }

    
     private String datosCronicaFutbol8(HttpServletRequest req) throws DAOException {
        
        
        var idParTxt = req.getParameter("partido");  
        ArrayList<CronicaFutbol8> cronica = null;
        
        if (idParTxt != null && !idParTxt.equals("undefined")){ 
            var idPar = parseLong(idParTxt);
            cronica = obtenerCronica(idPar);
        }
        
        if (cronica == null && req.getSession().getAttribute("cronica") != null)
            cronica = (ArrayList<CronicaFutbol8>) req.getSession().getAttribute("cronica");
        
        var txt = datosJsonCronicaFutbol8(cronica);

        return txt;
    }

    private String datosAuditoria(HttpServletRequest req) throws DAOException {
        
        var txt = new StringBuilder();
        EquipoFutbol8 eq = null;
                
        txt.append('[');
        
        var idTxt = req.getParameter("equipo");
        long id;
        if (idTxt != null){ 
            id = parseLong(idTxt); 
            eq = obtenerSimpleEquipoFutbol8(id);
            if (eq != null && !eq.isSaldoCuentasOK()){
                var patronFecha = "dd/MM/yyyy";
                var formato = new SimpleDateFormat(patronFecha);
                var movs = obtenerMovimientosPorClase(eq);
                var primero = true;
                for (var mov : movs) {                    
                    if (primero) primero = false; else txt.append(',');
                    txt.append('{');
                    txt.append(formatJson("clase", mov.getClaseTxt()));
                    txt.append(',');
                    txt.append(formatJson("fecha", formato.format(mov.getFecha())));
                    txt.append(',');
                    int valor;
                    if (mov.isPositivo()) valor = mov.getValor();
                    else valor = -(mov.getValor());
                    txt.append(formatJson("cantidad", valor));
                    txt.append('}');
                    eq.setTotalIngresos(eq.getTotalIngresos() + valor);
                }
            }                 
        }
        txt.append(']');
        
        if (eq != null &&eq.isSaldoCuentasOK()){
            txt.delete(0, txt.length());
            txt.append("[]");
        }
            

        return txt.toString();
    }
 

}

     

