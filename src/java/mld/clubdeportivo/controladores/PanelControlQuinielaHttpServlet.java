
package mld.clubdeportivo.controladores;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mld.clubdeportivo.base.Grupo;
import mld.clubdeportivo.base.quinielas.*;
import mld.clubdeportivo.bd.DAOException;
import mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela;
import mld.clubdeportivo.utilidades.Correo;
import java.util.logging.*;

/**
 *
 * @author Miguel
 */
public class PanelControlQuinielaHttpServlet extends HttpServlet {

    private static Logger logger = 
            Logger.getLogger(PanelControlQuinielaHttpServlet.class.getName());
   
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
        
        req.setAttribute("path", "/panelControl/Quiniela/inicio");
        
        try { 

            long id = (Long) req.getSession().getAttribute("idEquipo");
            EquipoQuiniela eq = JDBCDAOQuiniela.obtenerSimpleEquipoQuiniela(id);

            req.setAttribute("esAdmin", eq.isAdmin());
            
            obtenerListaEquipos(req, eq.getClub().getGrupo());

            String accion = req.getPathInfo();           

            if (accion.equals("/inicio"))
                inicio(req, eq);
            else if (accion.equals("/cumplimentar"))
                cumplimentar(req, eq);
            else if (accion.equals("/jornada"))
                jornada(req, eq);
            else if (accion.equals("/clasificacion"))
                clasificacion(req, eq);
            else if (accion.equals("/jornadasDisputadas"))
                jornadasDisputadas(req, eq);
            else if (accion.equals("/competiciones"))
                competiciones(req);
            else if (accion.equals("/competicion"))
                verCompeticion(req);
            else if (accion.equals("/historico"))
                verHistorico(req);
            else if (accion.equals("/jornadaAdmin") && eq.isAdmin()) 
                cumplimentarAdmin(req, eq);
                      

        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage());
            req.setAttribute("error", ex.getMessage());
        }
            
     
        RequestDispatcher view =
                req.getRequestDispatcher("/PanelControl/panelControlQuiniela.jsp");
        view.forward(req, resp);
       
    }

   
    private HttpServletRequest inicio(HttpServletRequest req, EquipoQuiniela eq)
            throws DAOException{

        req.setAttribute("op", "inicio");
        req.setAttribute("titulo", "CLASIFICACION GENERAL");

        req.setAttribute("equipo", eq);
        
        CompeticionQuiniela comp = JDBCDAOQuiniela.competicionActiva();
                
        ArrayList<PuntuacionQuiniela> pts =
                JDBCDAOQuiniela.clasificacionQuiniela(comp, true);
        
        ArrayList<JornadaQuiniela> jornadas = jornadasDisputadas(comp); 
        
        JornadaQuiniela ultimaJornada = null;
        if (jornadas.size() > 0)
            ultimaJornada = jornadas.get(jornadas.size()-1);

        req.setAttribute("comp", comp);
        req.setAttribute("ultimaJornada", ultimaJornada);
        req.setAttribute("clasificacion", pts);
        
        UtilesHttpServlet.tratarComentarios(req, eq.getClub(), false);   
               
        return req;

    }

    private void obtenerListaEquipos(HttpServletRequest req, Grupo grp)
            throws DAOException{

        ArrayList<EquipoQuiniela> equipos = JDBCDAOQuiniela.listaEquiposQuiniela();
        
        ArrayList<EquipoQuiniela> equiposGrupo = new ArrayList<EquipoQuiniela>();
        
        for (EquipoQuiniela eq : equipos) 
            if (eq.getClub().getGrupo().equals(grp))
                equiposGrupo.add(eq);

        req.setAttribute("equiposGrupo", equiposGrupo);

    }
    
    private void obtenerApuestas(HttpServletRequest req, JornadaQuiniela jornada,
           ApuestaQuiniela apuesta1, ApuestaQuiniela apuesta2, boolean isTemporal) throws DAOException{

        ArrayList datosQuiniela = new ArrayList();
        ArrayList<String> apuestas = null;

        if (isTemporal){
           apuestas = obtenerApuestasTemporalGrupo(req, jornada, apuesta1.getEquipo());
        }
                
        int col1 = 0; 
        int col2 = 0;

        for (int i = 0; i < 15; i++){

            ApuestaQ ap = new ApuestaQ();

            ap.setNumero(String.valueOf(i + 1));
            ap.setPartido(jornada.getPartido()[i]);
            ap.setColumna1(apuesta1.getResultado()[i]);
            ap.setColumna2(apuesta2.getResultado()[i]);
            if (!isTemporal)
                ap.setResultado(jornada.getResultado()[i]);
            else{
                if (apuestas != null){                
                    ap.setResultado(apuestas.get(i));
                    if (apuesta1.getResultado()[i] != null && apuesta1.getResultado()[i].equals(apuestas.get(i)))
                        col1++;
                    if (apuesta2.getResultado()[i] != null && apuesta2.getResultado()[i].equals(apuestas.get(i)))
                        col2++;
                }
            }
                        
            datosQuiniela.add(ap);
        }

        req.setAttribute("apuestas", datosQuiniela);
        req.setAttribute("actualizada", apuesta1.getActualizada());


    }


    private ArrayList<String> obtenerApuestasTemporalGrupo(HttpServletRequest req, 
            JornadaQuiniela jornada, EquipoQuiniela eq) throws DAOException{

        ArrayList<String> apuestas = null;

        try{
            // Intentamos obtner los resultados hasta el momento
            String jor = String.valueOf(jornada.getNumero());
            apuestas = UtilesQuiniela.obtenerResultados(jor, false); 
        }catch(Exception ex){
            return apuestas;
        }
    
        ArrayList<EquipoQuiniela> listaGrp = new ArrayList<EquipoQuiniela>();
        ArrayList<EquipoQuiniela> lista =
                JDBCDAOQuiniela.listaEquiposQuiniela();
        for (EquipoQuiniela equipoQuiniela : lista) {
            boolean mismoGrupo = eq.getClub().getGrupo().equals(equipoQuiniela.getClub().getGrupo());
            if (mismoGrupo){
                listaGrp.add(equipoQuiniela);
            }
        }
        ArrayList<ApuestaQuiniela> apuestasEq; 
            
        HashMap<EquipoQuiniela,HashMap<Integer,Integer>> col1 = new HashMap();
        HashMap<EquipoQuiniela,HashMap<Integer,Integer>> col2 = new HashMap();
        
        for (EquipoQuiniela equipoQuiniela : listaGrp) { 
        
            HashMap<Integer,Integer> aciertos_col1 = new HashMap<Integer,Integer>();
            HashMap<Integer,Integer> aciertos_col2 = new HashMap<Integer,Integer>();
            apuestasEq =  (ArrayList<ApuestaQuiniela>) JDBCDAOQuiniela.obtenerApuestas(equipoQuiniela, jornada);
  
            for (int i = 0; i < 15; i++){

                ApuestaQ ap = new ApuestaQ();
                ApuestaQuiniela apuesta1 = (ApuestaQuiniela) apuestasEq.get(0);
                ApuestaQuiniela apuesta2 = (ApuestaQuiniela) apuestasEq.get(1);

                ap.setNumero(String.valueOf(i + 1));
                ap.setPartido(jornada.getPartido()[i]);
                ap.setColumna1(apuesta1.getResultado()[i]);  
                ap.setColumna2(apuesta2.getResultado()[i]);
                ap.setResultado(apuestas.get(i));
                if (apuesta1.getResultado()[i] == null) 
                        apuesta1.getResultado()[i] = " ";
                if (apuesta1.getResultado()[i].equals(apuestas.get(i)))
                    aciertos_col1.put(i, 1);
                else
                    aciertos_col1.put(i, 0);
                col1.put(equipoQuiniela, aciertos_col1);
                
                if (apuesta2.getResultado()[i] == null) 
                        apuesta2.getResultado()[i] = " ";
                if (apuesta2.getResultado()[i].equals(apuestas.get(i)))
                    aciertos_col2.put(i, 1);
                else
                    aciertos_col2.put(i, 0);
                col2.put(equipoQuiniela, aciertos_col2);
                           
            }
        }
        
        if (listaGrp.size() > 1){
            for (int i = 0; i < 15; i++){
                int totalAcertados = 0;
                for (EquipoQuiniela equi : listaGrp) {                   
                    if (col1.get(equi).get(i) == 1 || col2.get(equi).get(i) == 1)
                         totalAcertados++;     
                }
                if (totalAcertados == 1){
                     for (EquipoQuiniela equi : listaGrp) {                      
                        if (col1.get(equi).get(i) == 1)
                            col1.get(equi).put(i, 2);
                        if (col2.get(equi).get(i) == 1)
                            col2.get(equi).put(i, 2);
                    }                    
                }                
            }            
        }
        
        for (EquipoQuiniela equipoQuiniela : listaGrp) {  
            Integer total_col1 = 0;
            Integer total_col2 = 0;
            for (int i = 0; i < 15; i++){               
                    total_col1 = total_col1 + col1.get(equipoQuiniela).get(i);                
                    total_col2 = total_col2 + col2.get(equipoQuiniela).get(i);
            }
            
            equipoQuiniela.setResultadoProvisional("(" + total_col1 + " , " + total_col2 + ") aciertos");
        }

        req.setAttribute("resultadosTemp", listaGrp);
        
        return apuestas;


    }
    
        

    private void obtenerApuestaMultiple(HttpServletRequest req,
            JornadaQuiniela jornada, String tipo) throws DAOException{

        // Creo tabla de 15 * 3 para meter los acumulados de cada apuesta
        int[][] ta = new int[15][3];
       
        ArrayList<ApuestaQuiniela> apuestas =
                (ArrayList<ApuestaQuiniela>) JDBCDAOQuiniela.obtenerApuestas(jornada);

        for (ApuestaQuiniela ap: apuestas) {
            for (int i = 0; i < 15; i++){
                String resul = ap.getResultado()[i];
                if (resul == null) continue;
                else if (resul.equals("1")) ta[i][0]++;
                else if (resul.equals("X")) ta[i][1]++;
                else if (resul.equals("2")) ta[i][2]++;
            }
        }

        // calculamos el primer reultado
        String[] apuesta = new String[15];
        for (int i = 0; i < 15; i++){
            if (ta[i][0] >= ta[i][1] && ta[i][0] >= ta[i][2]){
                apuesta[i] = "1";
                ta[i][0] = 0;
            }
            else if (ta[i][1] >= ta[i][0] && ta[i][1] >= ta[i][2]){
                apuesta[i] = "X";
                ta[i][1] = 0;
            }
            else if (ta[i][2] >= ta[i][0] && ta[i][2] >= ta[i][1]){
                apuesta[i] = "2";
                ta[i][2] = 0;
            }
        }        

        // calculamos posibles dobles y su peso
        String[] doble = new String[15];
        int[] doblePeso = new int[15];
        for (int i = 0; i < 15; i++){
            if (ta[i][0] >= ta[i][1] && ta[i][0] >= ta[i][2]){
                doble[i] = "1";
                doblePeso[i] = ta[i][0];
                ta[i][0] = 0;
            }
            else if (ta[i][1] >= ta[i][0] && ta[i][1] >= ta[i][2]){
                doble[i] = "X";
                doblePeso[i] = ta[i][1];
                ta[i][1] = 0;
            }
            else if (ta[i][2] >= ta[i][0] && ta[i][2] >= ta[i][1]){
                doble[i] = "2";
                doblePeso[i] = ta[i][2];
                ta[i][2] = 0;
            }
        }

        // calculamos posibles triples y su peso
        String[] triple = new String[15];
        int[] triplePeso = new int[15];
        for (int i = 0; i < 15; i++){
            if (ta[i][0] >= ta[i][1] && ta[i][0] >= ta[i][2]){
                triple[i] = "1";
                triplePeso[i] = ta[i][0];
            }
            else if (ta[i][1] >= ta[i][0] && ta[i][1] >= ta[i][2]){
                triple[i] = "X";
                triplePeso[i] = ta[i][1];
            }
            else if (ta[i][2] >= ta[i][0] && ta[i][2] >= ta[i][1]){
                triple[i] = "2";
                triplePeso[i] = ta[i][2];
            }
        }

        String[] doblesFinales = new String[15];
        String[] triplesFinales = new String[15];

        if (tipo.equalsIgnoreCase("3 Dobles")){
            for (int i = 0; i < 3; i++){
                int p = 0;
                int n = 0;
                String v = "";
                for (int ii = 0; ii < 14; ii++){ // el pleno al 15 no cuenta
                    if (doblePeso[ii] > n){
                        n = doblePeso[ii];
                        v = doble[ii];
                        p = ii;
                        doblePeso[ii] = 0;
                    }
                }
                doblesFinales[p] = v;
            }
        }

        /* Formateamos para pasar a la vista, a la que devolvemos
        un array de vectores de 3 posiciones con ture o false */
        ArrayList<ApuestaMix> datos = new ArrayList<ApuestaMix>();
        for (int i = 0; i < 15; i++){

            ApuestaMix ap = new ApuestaMix();
            ap.setNumero(String.valueOf(i + 1));
            ap.setPartido(jornada.getPartido()[i]);

            if (apuesta[i].equals("1")) ap.setCol1(true);
            else if (apuesta[i].equals("X")) ap.setColX(true);
            else if (apuesta[i].equals("2")) ap.setCol2(true);

            if (doblesFinales[i] != null){
                if (doblesFinales[i].equals("1")) ap.setCol1(true);
                else if (doblesFinales[i].equals("X")) ap.setColX(true);
                else if (doblesFinales[i].equals("2")) ap.setCol2(true);
            }

            if (triplesFinales[i] != null){
                if (triplesFinales[i].equals("1")) ap.setCol1(true);
                else if (triplesFinales[i].equals("X")) ap.setColX(true);
                else if (triplesFinales[i].equals("2")) ap.setCol2(true);
            }

            datos.add(ap);

        }

        req.setAttribute("apuestasmix", datos);        
        
    }

    private void cumplimentar(HttpServletRequest req, EquipoQuiniela eq) 
            throws DAOException, IllegalArgumentException {

        req.setAttribute("op", "cumplimentar");
        req.setAttribute("titulo", "CUMPLIMENTAR");
        req.setAttribute("diasParaCumplimentar", diasParaCumplimentar());
        
        CompeticionQuiniela comp = JDBCDAOQuiniela.competicionActiva();
        req.setAttribute("comp", comp);
        if (comp == null) return;

        if (!sePuedeCumplimentar())
            throw new UnsupportedOperationException("No se puede cumplimentar este dia");

        String op = (String) req.getParameter("operacion");
        ApuestaQuiniela apuesta1 = null;
        ApuestaQuiniela apuesta2 = null; 
        
        JornadaQuiniela jornada = JDBCDAOQuiniela.obtenerProximaJornada(comp);
        req.setAttribute("jornada", jornada);

        if (jornada == null || jornada.isValidada() || jornada.isBloqueada()) 
            op = null;
        else{
            ArrayList apuestas =
                    (ArrayList) JDBCDAOQuiniela.obtenerApuestas(eq, jornada);
            apuesta1 = (ApuestaQuiniela) apuestas.get(0);
            apuesta2 = (ApuestaQuiniela) apuestas.get(1);
        }

         if (op == null) {
            }// no hacemos nada
         else if (op.equals("Grabar")) {
             String[] resultCol1 = new String[15];
             String[] resultCol2 = new String[15];
             boolean isError = false;
             
             StringBuilder txt = new StringBuilder();
             txt.append("Has cumplimentado tus columnas correctamente <br/><br/>");
             String nombre, valor;
             for (int i = 0; i < 15; i++) {
                 nombre = "col1_apuesta" + (i + 1);
                 valor = req.getParameter(nombre);
                 if ((valor == null) ||
                     (!valor.equals("1") &&
                     !valor.equals("X") &&
                     !valor.equals("2") )){
                     req.setAttribute("error", "Falta cumplimentar la apuesta " + (i + 1) + " colummna 1");
                     isError = true;
                 }                
                 resultCol1[i] = valor;
                 txt.append(valor).append('-');

                 nombre = "col2_apuesta" + (i + 1);
                 valor = req.getParameter(nombre);
                 if ((valor == null) ||
                     (!valor.equals("1") &&
                     !valor.equals("X") &&
                     !valor.equals("2") )){
                     req.setAttribute("error", "Falta cumplimentar la apuesta " + (i + 1) + " colummna 2");
                     isError = true;
                 }
                 resultCol2[i] = valor;
                 txt.append(valor).append("<br/>");
             }

             apuesta1.setResultado(resultCol1);
             apuesta1.setActualizada(new Date());
             apuesta2.setResultado(resultCol2);
             apuesta2.setActualizada(new Date());
             JDBCDAOQuiniela.grabarApuesta(apuesta1);
             JDBCDAOQuiniela.grabarApuesta(apuesta2);
             if (!isError){                 
                     Correo.getCorreo().enviarMail("ClubDeportivo Cumplimentación Quiniela Correcta", 
                             txt.toString(), true, eq.getClub().getMail());
             }
             
         }
         
         obtenerApuestas(req, jornada, apuesta1, apuesta2, false);


    }
    
    private void cumplimentarAdmin(HttpServletRequest req, EquipoQuiniela eq) 
            throws DAOException, IllegalArgumentException {
        
        req.setAttribute("op", "cumplimentarAdmin");
        req.setAttribute("titulo", "ADMINISTRAR JORNADA");
        
        CompeticionQuiniela comp = JDBCDAOQuiniela.competicionActiva();
        req.setAttribute("comp", comp);
        if (comp == null) return;

        // Aqui verificar si se puede administrar
        //if (!sePuedeCumplimentar())
        //    throw new UnsupportedOperationException("No se puede cumplimentar este dia");

        String op = (String) req.getParameter("operacion");
              
        JornadaQuiniela jornada = JDBCDAOQuiniela.obtenerProximaJornada(comp);
        
        ArrayList<ApuestaMix> datos = new ArrayList<ApuestaMix>();  
        String numJornada = "";
              
        String[] partidos = new String[15];
        String[] resultados = new String[15];
        
        if (op == null){
            
        }
        else if (op.equals("Crear Jornada")) {
             String nj = (String) req.getParameter("numeroJornada");  
             String pj = (String) req.getParameter("puntosJornada");  
             try {
                 if (nj == null || nj == "") throw 
                         new UnsupportedOperationException("Se ha de informar numero de jornada");
                 if (pj == null || nj == "") throw 
                         new UnsupportedOperationException("Se han de informar los puntos de la jornada");
                 for(int i = 1; i < 16; i++){
                     String np = String.valueOf(i);
                     if (np.length() == 1) np = "0" + np;
                     partidos[i - 1] = req.getParameter("partido" + np);
                 }                 
                jornada = UtilesQuiniela.crearJornadaQuiniela(nj, pj, partidos);
            } catch (Exception ex){
                req.setAttribute("error", "Error al crear jornada quiniela ".concat(ex.getMessage()));
            }
        }
        else if (op.equals("Grabar")) { 
            
            for(int i = 1; i < 16; i++){
                String np = String.valueOf(i);
                if (np.length() == 1) np = "0" + np;
                partidos[i - 1] = req.getParameter("partido" + np);
                resultados[i - 1] = req.getParameter("resultado" + String.valueOf(i));
            }       
            
            UtilesQuiniela.actualizarJornadaQuiniela(jornada, partidos, resultados);             
          
        }
        else if (op.equals("Validar")){
            UtilesQuiniela.validarJornada(comp);
            jornada = null;
        }
        
        if (jornada == null || jornada.isValidada()) {
            for (int i = 1; i < 16; i++){
                 ApuestaMix ap = new ApuestaMix();
                 ap.setNumero(i);
                 ap.setPartido("");
                 ap.setCol1(false);
                 ap.setColX(false);
                 ap.setCol2(false);
                 datos.add(ap);
             }           
        }
        else{
            numJornada = String.valueOf(jornada.getNumero());
            String r;
            for (int i = 1; i < 16; i++){
                ApuestaMix ap = new ApuestaMix();
                ap.setNumero(i);
                ap.setPartido(jornada.getPartido()[i-1]);
                r = jornada.getResultado()[i-1];
                if ("1".equals(r)) ap.setCol1(true);
                else if ("X".equals(r)) ap.setColX(true);
                else if ("2".equals(r)) ap.setCol2(true);
                datos.add(ap);
                }
         }
        
        boolean resultadosCompletos;
        if (jornada != null)
            resultadosCompletos = jornada.resultadosCompletos();
        else
            resultadosCompletos = false;
         
        req.setAttribute("datos", datos);
        req.setAttribute("numJornada", numJornada);
        req.setAttribute("jornadaActiva", jornada != null);  
        req.setAttribute("resultadosCompletos", resultadosCompletos); 
         
    }

    private void jornada(HttpServletRequest req, EquipoQuiniela eqActual)
            throws DAOException {

        req.setAttribute("op", "jornada");
        req.setAttribute("titulo", "JORNADA ACTUAL");
        
        EquipoQuiniela eq = selectorEquipo(req, eqActual);
        
        if (eq == null) return;

        String tipoApuesta = tipoApuestas(req);
        
        CompeticionQuiniela comp = JDBCDAOQuiniela.competicionActiva();
        req.setAttribute("comp", comp);
        if (comp == null) return;
        
        JornadaQuiniela jornada = JDBCDAOQuiniela.obtenerProximaJornada(comp);
        
        if (jornada.isValidada())
            throw new UnsupportedOperationException("No hay ninguna jornada pendiente");
        
        if (sePuedeCumplimentar() && !jornada.isBloqueada())
            throw new UnsupportedOperationException("Todavia no se puede consultar la jornada");

        req.setAttribute("jornada", jornada);

        if (jornada == null || jornada.isValidada()) return;
        
        ArrayList<ApuestaQuiniela> apuestas =
                        (ArrayList) JDBCDAOQuiniela.obtenerApuestas(eq, jornada);
        

        if (tipoApuesta.equals("Sencilla")) {
            ApuestaQuiniela apuesta1 = (ApuestaQuiniela) apuestas.get(0);
            ApuestaQuiniela apuesta2 = (ApuestaQuiniela) apuestas.get(1);
            obtenerApuestas(req, jornada, apuesta1, apuesta2, true);
        } else
            obtenerApuestaMultiple(req, jornada, tipoApuesta);
        
    }

    private String tipoApuestas(HttpServletRequest req){

        String tipo;
        ArrayList<String> lista = new ArrayList<String>();

        if (req.getParameter("tipoapuesta") != null){
            req.setAttribute("apuestaelegida", req.getParameter("tipoapuesta"));
            tipo = req.getParameter("tipoapuesta");
        }
        else{
            req.setAttribute("apuestaelegida", "Sencilla");
            tipo = "Sencilla";
        }

        String tipos = this.getInitParameter("tiposapuesta");
        lista.addAll(Arrays.asList(tipos.split(",")));
        lista.add("Sencilla");

        req.setAttribute("tiposapuesta", lista);

        return tipo;

    }
  

    private boolean sePuedeCumplimentar(){

        boolean sePuede = false;

        String confDias = this.getInitParameter("diascumplimentacion");

        String[] dias = confDias.split(",");

        Calendar calendario = Calendar.getInstance();
        calendario.setTime(new Date());

        String diaAct = String.valueOf(calendario.get(Calendar.DAY_OF_WEEK));

        for (String dia : dias) {
            if (dia.equals(diaAct)) sePuede = true;
        }
        return sePuede;

    }
    
    private String diasParaCumplimentar(){

        StringBuilder txt =  new StringBuilder();

        String confDias = this.getInitParameter("diascumplimentacion");

        String[] dias = confDias.split(",");
       
        for (String dia : dias) {
            if (dia.equals("2")) txt.append("Lunes,");
            if (dia.equals("3")) txt.append("Martes,");
            if (dia.equals("4")) txt.append("Miercoles,");
            if (dia.equals("5")) txt.append("Jueves,");
            if (dia.equals("6")) txt.append("Viernes,");
            if (dia.equals("7")) txt.append("Sabado,");
            if (dia.equals("1")) txt.append("Domingo,");
        }
        
        String txtDias;
        if (txt.toString().isEmpty()) txtDias = "";
        else txtDias = txt.substring(0, txt.length()-1);
        
        return txtDias;

    }

    private void clasificacion(HttpServletRequest req, EquipoQuiniela eq) 
            throws DAOException {

        req.setAttribute("op", "clasificacion");
        req.setAttribute("titulo", "CLASIFICACION DEL GRUPO");
        
        Grupo grp = eq.getClub().getGrupo();
        
        String idComp = req.getParameter("competicion");
        CompeticionQuiniela comp;
        if (idComp == null){
            comp = JDBCDAOQuiniela.competicionActiva();
        }
        else{
            long id = Long.parseLong(idComp);
            comp = JDBCDAOQuiniela.obtenerCompeticionPorId(id);
        }
            
        int numJornadas = JDBCDAOQuiniela.numeroJornadasDisputadas(comp);

        ArrayList<PuntuacionQuiniela> pts =
                JDBCDAOQuiniela.clasificacionQuiniela(comp, false);
        
        ArrayList<PuntuacionQuiniela> ptsGrp = new ArrayList<PuntuacionQuiniela>();
        
        for (PuntuacionQuiniela punt : pts) 
            if (punt.getEquipo().getClub().getGrupo().equals(grp))
                ptsGrp.add(punt);              

        req.setAttribute("comp", comp);
        req.setAttribute("clasificacion", ptsGrp);
        req.setAttribute("numJornadas", numJornadas);
        
    }

    private CompeticionQuiniela selectorCompeticion(HttpServletRequest req)
            throws DAOException{

        HashSet<CompeticionQuiniela> comps =
                new HashSet<CompeticionQuiniela>();

        CompeticionQuiniela comp = null;
        CompeticionQuiniela compAct = JDBCDAOQuiniela.competicionActiva();   
        
        if (compAct != null) comps.add(compAct);        
        
        ArrayList<CompeticionQuiniela> compsNoAct = 
                JDBCDAOQuiniela.competicionesNoActivas();
        
        comps.addAll(compsNoAct);
        
        
        if (req.getParameter("competicion") == null){
              
              if (compAct != null) comp = compAct;
              else if (!compsNoAct.isEmpty()) comp = compsNoAct.get(compsNoAct.size()-1);
        }
        else {
            long id = Long.parseLong(req.getParameter("competicion"));
            comp = JDBCDAOQuiniela.obtenerCompeticionPorId(id);
        }
  

        req.setAttribute("competicion", comp.getId());
        req.setAttribute("competiciones", comps);

        return comp;

    }

    private JornadaQuiniela selectorJornada(HttpServletRequest req,
            CompeticionQuiniela comp) throws DAOException{
        
        ArrayList<JornadaQuiniela> jors =
                (ArrayList<JornadaQuiniela>) JDBCDAOQuiniela.obtenerJornadasValidadas(comp);

        JornadaQuiniela jor = null;        

        if (req.getParameter("jornada") == null)
                jor = jors.get(jors.size()-1);
        else {
            int id = Integer.parseInt(req.getParameter("jornada"));
            for (JornadaQuiniela jq : jors) {
                if (jq.getId() == id)
                        jor = jq;
            }
            if (jor == null) jor = jors.get(jors.size()-1);
        }
        
        req.setAttribute("jornada", jor.getId());
        req.setAttribute("jornadas", jors);
        
        return jor;
    }


    private EquipoQuiniela selectorEquipo(HttpServletRequest req, EquipoQuiniela eqActual)
            throws DAOException{

        EquipoQuiniela eq;

        if (req.getParameter("equipo") == null)
                eq = eqActual;
        else {
            long id = Long.parseLong(req.getParameter("equipo"));
            eq = JDBCDAOQuiniela.obtenerSimpleEquipoQuiniela(id);
        }

        ArrayList<EquipoQuiniela> listaeq = new ArrayList<EquipoQuiniela>();
        listaeq.add(eq);

        ArrayList<EquipoQuiniela> lista =
                JDBCDAOQuiniela.listaEquiposQuiniela();
        for (EquipoQuiniela equipoQuiniela : lista) {
            boolean mismoGrupo = eq.getClub().getGrupo().equals(equipoQuiniela.getClub().getGrupo());
            if (!eq.equals(equipoQuiniela) && mismoGrupo){
                listaeq.add(equipoQuiniela);
            }
        }

        req.setAttribute("equipo", eq.getId());
        req.setAttribute("equipos", listaeq);

        return eq;

    }



    private void jornadasDisputadas(HttpServletRequest req, EquipoQuiniela eqActual)
            throws DAOException {

        req.setAttribute("op", "jornadasDisputadas");
        req.setAttribute("titulo", "JORNADAS DISPUTADAS");
    

        CompeticionQuiniela comp = selectorCompeticion(req);
        if (comp == null) {
            return;
        }

        JornadaQuiniela jor = selectorJornada(req, comp);
        if (jor == null) {
            return;
        }

        EquipoQuiniela eq = selectorEquipo(req, eqActual);
        if (eq == null) {
            return;
        }
        
        ArrayList apuestas =
                (ArrayList) JDBCDAOQuiniela.obtenerApuestas(eq, jor);
        if (apuestas.size() > 1){
            ApuestaQuiniela apuesta1 = (ApuestaQuiniela) apuestas.get(0);
            ApuestaQuiniela apuesta2 = (ApuestaQuiniela) apuestas.get(1);
            obtenerApuestas(req, jor, apuesta1, apuesta2, false);

            EstadisticaQuiniela est = JDBCDAOQuiniela.obtenerEstadistica(eq, comp, jor);

            req.setAttribute("estadistica", est);
        }

    }

    private ArrayList<JornadaQuiniela> jornadasDisputadas(CompeticionQuiniela comp) throws DAOException {

        ArrayList<JornadaQuiniela> jornadas =
                (ArrayList<JornadaQuiniela>) JDBCDAOQuiniela.obtenerJornadasValidadas(comp);

        Grupo grp = comp.getGrupo();
        ArrayList<EquipoQuiniela> eqs = JDBCDAOQuiniela.listaEquiposQuiniela(grp);


        for (JornadaQuiniela jornada : jornadas) {
            jornada.setEstadisticas(new ArrayList());
            for (EquipoQuiniela eq : eqs) {
                EstadisticaQuiniela est =
                        JDBCDAOQuiniela.obtenerEstadistica(eq, comp, jornada);
                if (est != null){
                    est.setEquipo(eq.getNombre());
                    eq.setEstadisitica(est);
                    jornada.getEstadisticas().add(est);
                }
            }
            ArrayList<EstadisticaQuiniela> estq = jornada.getEstadisticas();
            EstadisticaQuiniela.clasificar(estq, true);
            jornada.setEstadisticas(estq);
        }
        
        return jornadas;
    }
    
    private void competiciones(HttpServletRequest req) throws DAOException {

        req.setAttribute("op", "competiciones");
        req.setAttribute("titulo", "COMPETICIONES");

        CompeticionQuiniela comp = selectorCompeticion(req);
        if (comp == null) {
            return;
        }

        ArrayList<JornadaQuiniela> jornadas = jornadasDisputadas(comp);                
        
        ArrayList<JornadaQuiniela> jornadasCol1 = new ArrayList<JornadaQuiniela>();
        ArrayList<JornadaQuiniela> jornadasCol2 = new ArrayList<JornadaQuiniela>();
        ArrayList<JornadaQuiniela> jornadasCol3 = new ArrayList<JornadaQuiniela>();
        
        int i = 1;
        for (JornadaQuiniela jornada : jornadas) {
            if (i == 1) {
                jornadasCol1.add(jornada);
                i = 2;
            }
            else if (i == 2) {
                jornadasCol2.add(jornada);
                i = 3;
            }
            else if (i == 3) {
                jornadasCol3.add(jornada);
                i = 1;
            }
        }        

        req.setAttribute("jornadasCol1", jornadasCol1);
        req.setAttribute("jornadasCol2", jornadasCol2);
        req.setAttribute("jornadasCol3", jornadasCol3);
    }

    


    private void verCompeticion(HttpServletRequest req) throws DAOException {

        req.setAttribute("op", "competiciones");
        req.setAttribute("titulo", "COMPETICIONES");

        CompeticionQuiniela comp = selectorCompeticion(req);
        if (comp == null) {
            return;
        }

        ArrayList<JornadaQuiniela> jornadas = jornadasDisputadas(comp);                
        
        ArrayList<JornadaQuiniela> jornadasCol1 = new ArrayList<JornadaQuiniela>();
        ArrayList<JornadaQuiniela> jornadasCol2 = new ArrayList<JornadaQuiniela>();
        ArrayList<JornadaQuiniela> jornadasCol3 = new ArrayList<JornadaQuiniela>();
        
        int i = 1;
        for (JornadaQuiniela jornada : jornadas) {
            if (i == 1) {
                jornadasCol1.add(jornada);
                i = 2;
            }
            else if (i == 2) {
                jornadasCol2.add(jornada);
                i = 3;
            }
            else if (i == 3) {
                jornadasCol3.add(jornada);
                i = 1;
            }
        }        

        req.setAttribute("jornadasCol1", jornadasCol1);
        req.setAttribute("jornadasCol2", jornadasCol2);
        req.setAttribute("jornadasCol3", jornadasCol3);
    }

private void verHistorico(HttpServletRequest req) throws DAOException {

        req.setAttribute("op", "historico");
        req.setAttribute("titulo", "HISTORICO");      

        ArrayList<CompeticionQuiniela> competiciones = 
                JDBCDAOQuiniela.competicionesNoActivas();      
      
        req.setAttribute("competiciones", competiciones);
         }
}
    

