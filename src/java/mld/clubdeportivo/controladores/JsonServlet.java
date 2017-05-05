
package mld.clubdeportivo.controladores;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mld.clubdeportivo.base.Bolsa;
import mld.clubdeportivo.base.Movimiento;
import mld.clubdeportivo.base.futbol8.*;
import mld.clubdeportivo.base.quinielas.CompeticionQuiniela;
import mld.clubdeportivo.base.quinielas.EquipoQuiniela;
import mld.clubdeportivo.base.quinielas.EstadisticaQuiniela;
import mld.clubdeportivo.base.quinielas.JornadaQuiniela;
import mld.clubdeportivo.bd.DAOException;
import mld.clubdeportivo.bd.JDBCDAOBolsa;
import mld.clubdeportivo.bd.JDBCDAOMovimiento;
import mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8;
import mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Miguel
 */
public class JsonServlet extends HttpServlet {

    private static Logger logger = LogManager.getLogger(JsonServlet.class);
   
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
       
        String txt = "";
       
        String accion = req.getPathInfo();           
        
        try {
            if (accion == null) {}
            else if (accion.equals("/bolsa"))
                txt = datosBolsa(req);
            else if (accion.equals("/presupuesto"))
                txt = datosPresupuestoFutbol8(req);
            else if (accion.equals("/movimientos"))
                txt = datosMovimientos(req);
            else if (accion.equals("/auditoria"))
                txt = datosAuditoria(req);
            else if (accion.equals("/posicionesQuiniela"))
                txt = datosPosicionesQuiniela(req);
            else if (accion.equals("/jugadoresFutbol8"))
                txt = datosJugadoresFutbol8(req);
            else if (accion.equals("/cronicaFutbol8"))
                txt = datosCronicaFutbol8(req);
        } catch (DAOException ex) {
            
        }
         
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        //txt = new String(txt.getBytes(), "UTF-8");
        resp.getWriter().print(txt);
       
    }

    private String datosBolsa(HttpServletRequest req) throws DAOException {
 
        String fecha = req.getParameter("fecha");
        StringBuilder txt = new StringBuilder();
        txt.append('[');
        
        ArrayList<Bolsa> lista = new ArrayList<Bolsa>();
        if (fecha == null || fecha.isEmpty())
            lista = JDBCDAOBolsa.obtenerDatosBolsa();
        else {
             SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
             try {
                 Date fechaDate = formato.parse(fecha);
                 lista = JDBCDAOBolsa.obtenerDatosBolsa(fechaDate);
             } catch (ParseException ex) { }
        }
            
        boolean primero = true;
        for (Bolsa elem : lista) {
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
        
        StringBuilder txt = new StringBuilder();
        txt.append('[');
        
        String idTxt = req.getParameter("equipo");
        String tipo = req.getParameter("tipo");
        long id;
        if (idTxt != null && tipo != null){ 
            id = Long.parseLong(idTxt); 
            EquipoFutbol8 eq = JDBCDAOFutbol8.obtenerSimpleEquipoFutbol8(id);
            if (eq != null){
                if (tipo.equals("gastos")){
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
                }
                else if (tipo.equals("ingresos")){
                    txt.append(formatJsonpresupuesto("Ingresos Aprox. Campo", eq.getIngresosMediosCampo())); 
                    txt.append(',');
                    txt.append(formatJsonpresupuesto("Ingresos Aprox. Publi.", eq.getIngresosMediosPublicidad()));                     
                }
                else if (tipo.equals("balance")){
                    txt.append(formatJsonpresupuesto("Ingresos Aproximados", eq.getIngresosMediosCampo() + eq.getIngresosMediosPublicidad())); 
                    txt.append(',');
                    txt.append(formatJsonpresupuesto("Gastos Aproximados", eq.getCosteTotal()));                     
                }
            }
        }
        txt.append(']');

        return txt.toString();
        
        
    }
    
   

    private String datosMovimientos(HttpServletRequest req) throws DAOException {
        
        StringBuilder txt = new StringBuilder();
        txt.append('[');
        
        String idTxt = req.getParameter("equipo");
        String tipo = req.getParameter("tipo");
        String todo = req.getParameter("total");
        long id;
        if (idTxt != null && todo != null){ 
            id = Long.parseLong(idTxt); 
            boolean isTodo = Boolean.parseBoolean(todo);
            EquipoFutbol8 eq = JDBCDAOFutbol8.obtenerSimpleEquipoFutbol8(id);
            if (eq != null){
                ArrayList<Movimiento> movs;
                if (isTodo)
                    movs = JDBCDAOMovimiento.obtenerMovimientos(eq);
                else{
                    GregorianCalendar c = new GregorianCalendar();        
                    Date diaActual = new Date();
                    c.setGregorianChange(diaActual);
                    c.add(Calendar.DAY_OF_YEAR, 1);
                    diaActual = c.getTime();
                    c.add(Calendar.DAY_OF_YEAR, -30);
                    Date diaInicial = c.getTime();            
                    movs = JDBCDAOMovimiento.obtenerMovimientos(eq, diaInicial, diaActual);
                }
                HashMap<String, Integer> ingresos = new HashMap<String, Integer>();
                HashMap<String, Integer> gastos = new HashMap<String, Integer>();
                for (Movimiento mov : movs) {
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
                int totalGastos = 0;
                int totalIngresos = 0;                
                it = gastos.entrySet().iterator();
                boolean primero = true;
                while (it.hasNext()) {
                    Map.Entry e = (Map.Entry)it.next();
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
                    Map.Entry e = (Map.Entry)it.next();
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
        
        StringBuilder txt = new StringBuilder();
        txt.append('[');
        
        String idTxt = req.getParameter("equipo");
        
        if (idTxt != null){ 
            long id = Long.parseLong(idTxt); 
            EquipoQuiniela eq = JDBCDAOQuiniela.obtenerSimpleEquipoQuiniela(id);
        
            String idComp = req.getParameter("competicion");
            CompeticionQuiniela comp;
            if (idComp == null){
                comp = JDBCDAOQuiniela.competicionActiva();
            }
            else{
                long idLong = Long.parseLong(idComp);
                comp = JDBCDAOQuiniela.obtenerCompeticionPorId(idLong);
            }
            
            if (eq != null && comp != null){
        
                ArrayList<JornadaQuiniela> jornadas =
                        (ArrayList<JornadaQuiniela>) JDBCDAOQuiniela.obtenerJornadasValidadas(comp);
                HashMap<Integer, Integer> posiciones = new HashMap<Integer, Integer>();
                for (JornadaQuiniela jornada : jornadas) { 
                    EstadisticaQuiniela est = 
                            JDBCDAOQuiniela.obtenerEstadistica(eq, comp, jornada); 
                    int pos = est.getPosicion();
                    if (!posiciones.containsKey(pos))
                        posiciones.put(pos, 1);
                    else
                        posiciones.put(pos, posiciones.get(pos) + 1);
                }                
                boolean primero = true;
                Iterator it = posiciones.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry e = (Map.Entry)it.next();
                    if (primero) primero = false;
                        else txt.append(',');
                    String txtPos = "";
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
        
        StringBuilder txt = new StringBuilder();
        txt.append('[');
        
        String idEqTxt = req.getParameter("equipo");
        String idParTxt = req.getParameter("partido");
        
        EquipoFutbol8 eq = null;
        PartidoFutbol8 partido;
        AlineacionFutbol8 ali = null;
        
        if (idEqTxt != null && idParTxt != null){ 
            long idEq = Long.parseLong(idEqTxt); 
            eq = JDBCDAOFutbol8.obtenerSimpleEquipoFutbol8(idEq); 
            long idPar = Long.parseLong(idParTxt); 
            partido = JDBCDAOFutbol8.obtenerPartido(idPar);
            if (eq != null && partido != null)
                ali = JDBCDAOFutbol8.obtenerAlineacionPartido(partido, eq);
        }
        
        if (ali != null){        
            ArrayList<JugadorFutbol8> jugadores = eq.getJugadores();
            boolean primero = true;
            for (JugadorFutbol8 jug : jugadores) {
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
        
        
        String idParTxt = req.getParameter("partido");  
        ArrayList<CronicaFutbol8> cronica = null;
        
        if (idParTxt != null && !idParTxt.equals("undefined")){ 
            long idPar = Long.parseLong(idParTxt); 
            cronica = JDBCDAOFutbol8.obtenerCronica(idPar);
        }
        
        if (cronica == null && req.getSession().getAttribute("cronica") != null)
            cronica = (ArrayList<CronicaFutbol8>) req.getSession().getAttribute("cronica");
        
        String txt = CronicaFutbol8.datosJsonCronicaFutbol8(cronica);

        return txt;
    }

    private String datosAuditoria(HttpServletRequest req) throws DAOException {
        
        StringBuilder txt = new StringBuilder();
        EquipoFutbol8 eq = null;
                
        txt.append('[');
        
        String idTxt = req.getParameter("equipo");
        long id;
        if (idTxt != null){ 
            id = Long.parseLong(idTxt); 
            eq = JDBCDAOFutbol8.obtenerSimpleEquipoFutbol8(id);
            if (eq != null && !eq.isSaldoCuentasOK()){
                String patronFecha = "dd/MM/yyyy";
                SimpleDateFormat formato = new SimpleDateFormat(patronFecha);
                ArrayList<Movimiento> movs = JDBCDAOMovimiento.obtenerMovimientosPorClase(eq);
                boolean primero = true;
                for (Movimiento mov : movs) {                    
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

     

