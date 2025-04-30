package mld.clubdeportivo.controladores;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.String.valueOf;
import java.util.*;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.getInstance;
import mld.clubdeportivo.base.Grupo;
import mld.clubdeportivo.base.quinielas.*;
import mld.clubdeportivo.bd.DAOException;
import mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela;
import java.util.logging.*;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static mld.clubdeportivo.base.quinielas.EstadisticaQuiniela.clasificar;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.clasificacionQuiniela;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.competicionActiva;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.competicionesNoActivas;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.grabarApuesta;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.listaEquiposQuiniela;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.numeroJornadasDisputadas;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerCompeticionPorId;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerEstadistica;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerJornadasValidadas;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerProximaJornada;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerSimpleEquipoQuiniela;
import static mld.clubdeportivo.controladores.UtilesHttpServlet.comprobarEstado;
import static mld.clubdeportivo.controladores.UtilesHttpServlet.tratarComentarios;
import static mld.clubdeportivo.controladores.UtilesQuiniela.actualizarJornadaQuiniela;
import static mld.clubdeportivo.controladores.UtilesQuiniela.crearJornadaQuiniela;
import static mld.clubdeportivo.controladores.UtilesQuiniela.obtenerResultados;
import static mld.clubdeportivo.controladores.UtilesQuiniela.validarJornada;
import static mld.clubdeportivo.utilidades.Correo.getCorreo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Miguel
 */
@Controller
public class PanelControlQuinielaHttpServlet {

    private static Logger logger
            = getLogger(PanelControlQuinielaHttpServlet.class.getName());

    @Value("${custom.diascumplimentacion}")
    private String diascumplimentacion;

    @GetMapping(
            {
                "/panelControl/Quiniela/inicio",
                "/panelControl/Quiniela/cumplimentar",
                "/panelControl/Quiniela/jornada",
                "/panelControl/Quiniela/clasificacion",
                "/panelControl/Quiniela/jornadasDisputadas",
                "/panelControl/Quiniela/competiciones",
                "/panelControl/Quiniela/historico",
                "/panelControl/Quiniela/jornadaAdmin"
            })
    public String doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        return processRequest(req, resp);
    }

    @PostMapping(
            {
                "/panelControl/Quiniela/inicio",
                "/panelControl/Quiniela/competiciones",
                "/panelControl/Quiniela/cumplimentar",
                "/panelControl/Quiniela/jornadaAdmin",
                "/panelControl/Quiniela/historico",
                "/panelControl/Quiniela/jornada",
                "/panelControl/Quiniela/jornadasDisputadas"
            })
    public String doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        return processRequest(req, resp);
    }

    private String processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String estado = comprobarEstado(req, resp);
        if (!"".equals(estado)) {
            return "redirect:/";
        }

        req.setAttribute("path", "/panelControl/Quiniela/inicio");

        try {

            long id = (Long) req.getSession().getAttribute("idEquipo");
            var eq = obtenerSimpleEquipoQuiniela(id);
            
            req.setAttribute("nombreGrupo", eq.getClub().getGrupo().getNombre());

            req.setAttribute("esAdmin", eq.isAdmin());

            int numEquipos = obtenerListaEquipos(req, eq.getClub().getGrupo()).size();

            var path = req.getRequestURI();
            String accion = path.substring(path.lastIndexOf("/") + 1);

            if (accion.equals("inicio")) {
                inicio(req, eq);
            } else if (accion.equals("cumplimentar")) {
                cumplimentar(req, eq);
            } else if (accion.equals("jornada")) {
                jornada(req, eq);
            } else if (accion.equals("clasificacion")) {
                clasificacion(req, eq);
            } else if (accion.equals("jornadasDisputadas")) {
                jornadasDisputadas(req, eq);
            } else if (accion.equals("competiciones")) {
                competiciones(req);
            } else if (accion.equals("competicion")) {
                verCompeticion(req);
            } else if (accion.equals("historico")) {
                verHistorico(req);
            } else if (accion.equals("jornadaAdmin") && eq.isAdmin()) {
                cumplimentarAdmin(req, eq, numEquipos);
            }

        } catch (Exception ex) {
            logger.log(SEVERE, ex.getMessage());
            req.setAttribute("error", ex.getMessage());
        }

        return "panelControlQuiniela";

    }

    private HttpServletRequest inicio(HttpServletRequest req, EquipoQuiniela eq)
            throws DAOException {

        req.setAttribute("op", "inicio");
        req.setAttribute("titulo", "CLASIFICACION GENERAL");

        req.setAttribute("equipo", eq);

        var comp = competicionActiva();
        var pts
                = clasificacionQuiniela(comp, true);
        var jornadas = jornadasDisputadas(comp);

        JornadaQuiniela ultimaJornada = null;
        if (jornadas.size() > 0) {
            ultimaJornada = jornadas.get(jornadas.size() - 1);
        }

        req.setAttribute("comp", comp);
        req.setAttribute("ultimaJornada", ultimaJornada);
        req.setAttribute("clasificacion", pts);

        tratarComentarios(req, eq.getClub(), false);

        return req;

    }

    private List obtenerListaEquipos(HttpServletRequest req, Grupo grp)
            throws DAOException {

        var equipos = listaEquiposQuiniela();
        var equiposGrupo = new ArrayList<>();
        for (var eq : equipos) {
            if (eq.getClub().getGrupo().equals(grp)) {
                equiposGrupo.add(eq);
            }
        }

        req.setAttribute("equiposGrupo", equiposGrupo);

        return equiposGrupo;

    }

    private void obtenerDatosApuestas(HttpServletRequest req, JornadaQuiniela jornada, EquipoQuiniela eq) throws DAOException {

        ArrayList<ApuestaQ> datosApuestas = new ArrayList<>();
        ArrayList<ResultadosApuestas> resultadosApuestas = new ArrayList<>();

        var equipos = listaEquiposQuiniela();
        var equiposGrupo = new ArrayList<EquipoQuiniela>();
        for (var e : equipos) {
            if (eq.getGrupo().equals(e.getGrupo())) {
                equiposGrupo.add(e);
            }
        }

        var aciertosTotales = new Integer[15];
        for (var i = 0; i < 15; i++) {
            aciertosTotales[i] = 0;
        }
        
        // primero obtenemos losaciertos totales para cada partido
        for (var eqg : equiposGrupo) {
            var obtenerApuestas = JDBCDAOQuiniela.obtenerApuestas(eqg, jornada);
            var ap1 = obtenerApuestas.get(0);
            var ap2 = obtenerApuestas.get(1);          
            for (var i = 0; i < 15; i++) {
                if (ap1.getResultado()[i] != null && ap1.getResultado()[i].equals(jornada.getResultado()[i])) {
                    aciertosTotales[i]++;
                }
                if (ap2.getResultado()[i] != null && ap2.getResultado()[i].equals(jornada.getResultado()[i])) {
                    aciertosTotales[i]++;
                }
            }
        }

        Date actualizada = null;

        for (var eqg : equiposGrupo) {
            var obtenerApuestas = JDBCDAOQuiniela.obtenerApuestas(eqg, jornada);
            var ap1 = obtenerApuestas.get(0);
            var ap2 = obtenerApuestas.get(1);

            int aciertosCol1 = 0;
            int aciertosCol2 = 0;
            int ptsCol1 = 0;
            int ptsCol2 = 0;

            var ptsPartido = equiposGrupo.size() * 2 * 10;
            for (var i = 0; i < 15; i++) {
                var ap = new ApuestaQ();

                ap.setNumero(valueOf(i + 1));
                ap.setEquipo(eq);
                ap.setPartido(jornada.getPartido()[i]);
                ap.setColumna1(ap1.getResultado()[i]);
                ap.setColumna2(ap2.getResultado()[i]);
                ap.setResultado(jornada.getResultado()[i]);

                if (ap1.getResultado()[i] != null && ap1.getResultado()[i].equals(ap.getResultado())) {
                    aciertosCol1++;
                }
                if (ap2.getResultado()[i] != null && ap2.getResultado()[i].equals(ap.getResultado())) {
                    aciertosCol2++;
                }

                if (eq.equals(eqg)) {
                    datosApuestas.add(ap);
                    if (actualizada == null) {
                        actualizada = ap1.getActualizada();
                    }
                }

                if (ap.getColumna1() != null && ap.getColumna1().equals(ap.getResultado())) {
                    ptsCol1 = ptsCol1 + (ptsPartido / aciertosTotales[i]);
                    ap.setPtsCol1(ptsPartido / aciertosTotales[i]);
                }
                if (ap.getColumna2() != null && ap.getColumna2().equals(ap.getResultado())) {
                    ap.setPtsCol2(ptsPartido / aciertosTotales[i]);
                    ptsCol2 = ptsCol2 + (ptsPartido / aciertosTotales[i]);
                }
            }        

            // Crear y agregar el objeto ResultadosApuestas            
            ResultadosApuestas res = new ResultadosApuestas();
            res.setEquipo(eqg);
            res.setAciertosCol1(String.valueOf(aciertosCol1));
            res.setAciertosCol2(String.valueOf(aciertosCol2));
            res.setPtsCol1(ptsCol1);
            res.setPtsCol2(ptsCol2);

            resultadosApuestas.add(res);

        }

        req.setAttribute("apuestas", datosApuestas);
        req.setAttribute("resultadosApuestas", resultadosApuestas);
        req.setAttribute("actualizada", actualizada);
    }

    private void obtenerApuestas(HttpServletRequest req, JornadaQuiniela jornada,
            ApuestaQuiniela apuesta1, ApuestaQuiniela apuesta2, boolean isTemporal) throws DAOException {

        var datosQuiniela = new ArrayList();
        ArrayList<String> apuestas = null;

        if (isTemporal) {
            apuestas = obtenerApuestasTemporalGrupo_obsoleto(req, jornada, apuesta1.getEquipo());
        }

        var col1 = 0;
        var col2 = 0;

        for (var i = 0; i < 15; i++) {

            var ap = new ApuestaQ();

            ap.setNumero(valueOf(i + 1));
            ap.setPartido(jornada.getPartido()[i]);
            ap.setColumna1(apuesta1.getResultado()[i]);
            ap.setColumna2(apuesta2.getResultado()[i]);
            if (!isTemporal) {
                ap.setResultado(jornada.getResultado()[i]);
            } else {
                if (apuestas != null) {
                    ap.setResultado(apuestas.get(i));
                    if (apuesta1.getResultado()[i] != null && apuesta1.getResultado()[i].equals(apuestas.get(i))) {
                        col1++;
                    }
                    if (apuesta2.getResultado()[i] != null && apuesta2.getResultado()[i].equals(apuestas.get(i))) {
                        col2++;
                    }
                }
            }

            datosQuiniela.add(ap);
        }

        req.setAttribute("apuestas", datosQuiniela);
        req.setAttribute("actualizada", apuesta1.getActualizada());

    }

    private ArrayList<String> obtenerApuestasTemporalGrupo_obsoleto(HttpServletRequest req,
            JornadaQuiniela jornada, EquipoQuiniela eq) throws DAOException {

        ArrayList<String> apuestas = null;

        try {
            // Intentamos obtner los resultados hasta el momento
            var jor = valueOf(jornada.getNumero());
            apuestas = obtenerResultados(jor, false);
        } catch (Exception ex) {
            return apuestas;
        }

        var listaGrp = new ArrayList<EquipoQuiniela>();
        var lista
                = listaEquiposQuiniela();
        for (var equipoQuiniela : lista) {
            var mismoGrupo = eq.getClub().getGrupo().equals(equipoQuiniela.getClub().getGrupo());
            if (mismoGrupo) {
                listaGrp.add(equipoQuiniela);
            }
        }
        ArrayList<ApuestaQuiniela> apuestasEq;

        HashMap<EquipoQuiniela, HashMap<Integer, Integer>> col1 = new HashMap();
        HashMap<EquipoQuiniela, HashMap<Integer, Integer>> col2 = new HashMap();

        for (var equipoQuiniela : listaGrp) {

            var aciertos_col1 = new HashMap<Integer, Integer>();
            var aciertos_col2 = new HashMap<Integer, Integer>();
            apuestasEq = (ArrayList<ApuestaQuiniela>) JDBCDAOQuiniela.obtenerApuestas(equipoQuiniela, jornada);

            for (var i = 0; i < 15; i++) {

                var ap = new ApuestaQ();
                var apuesta1 = (ApuestaQuiniela) apuestasEq.get(0);
                var apuesta2 = (ApuestaQuiniela) apuestasEq.get(1);

                ap.setNumero(valueOf(i + 1));
                ap.setPartido(jornada.getPartido()[i]);
                ap.setColumna1(apuesta1.getResultado()[i]);
                ap.setColumna2(apuesta2.getResultado()[i]);
                ap.setResultado(apuestas.get(i));
                if (apuesta1.getResultado()[i] == null) {
                    apuesta1.getResultado()[i] = " ";
                }
                if (apuesta1.getResultado()[i].equals(apuestas.get(i))) {
                    aciertos_col1.put(i, 1);
                } else {
                    aciertos_col1.put(i, 0);
                }
                col1.put(equipoQuiniela, aciertos_col1);

                if (apuesta2.getResultado()[i] == null) {
                    apuesta2.getResultado()[i] = " ";
                }
                if (apuesta2.getResultado()[i].equals(apuestas.get(i))) {
                    aciertos_col2.put(i, 1);
                } else {
                    aciertos_col2.put(i, 0);
                }
                col2.put(equipoQuiniela, aciertos_col2);

            }
        }

        if (listaGrp.size() > 1) {
            for (var i = 0; i < 15; i++) {
                var totalAcertados = 0;
                for (var equi : listaGrp) {
                    if (col1.get(equi).get(i) == 1 || col2.get(equi).get(i) == 1) {
                        totalAcertados++;
                    }
                }
                if (totalAcertados == 1) {
                    for (var equi : listaGrp) {
                        if (col1.get(equi).get(i) == 1) {
                            col1.get(equi).put(i, 2);
                        }
                        if (col2.get(equi).get(i) == 1) {
                            col2.get(equi).put(i, 2);
                        }
                    }
                }
            }
        }

        for (var equipoQuiniela : listaGrp) {
            Integer total_col1 = 0;
            Integer total_col2 = 0;
            for (var i = 0; i < 15; i++) {
                total_col1 = total_col1 + col1.get(equipoQuiniela).get(i);
                total_col2 = total_col2 + col2.get(equipoQuiniela).get(i);
            }

            equipoQuiniela.setResultadoProvisional("(" + total_col1 + " , " + total_col2 + ") aciertos");
        }

        req.setAttribute("resultadosTemp", listaGrp);

        return apuestas;

    }

    private void obtenerApuestaMultiple(HttpServletRequest req,
            JornadaQuiniela jornada, String tipo) throws DAOException {

        // Creo tabla de 15 * 3 para meter los acumulados de cada apuesta
        var ta = new int[15][3];
        var apuestas
                = (ArrayList<ApuestaQuiniela>) JDBCDAOQuiniela.obtenerApuestas(jornada);
        for (var ap : apuestas) {
            for (var i = 0; i < 15; i++) {
                var resul = ap.getResultado()[i];
                if (null == resul) {
                    continue;
                } else {
                    switch (resul) {
                        case "1":
                            ta[i][0]++;
                            break;
                        case "X":
                            ta[i][1]++;
                            break;
                        case "2":
                            ta[i][2]++;
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        // calculamos el primer reultado
        var apuesta = new String[15];
        for (var i = 0; i < 15; i++) {
            if (ta[i][0] >= ta[i][1] && ta[i][0] >= ta[i][2]) {
                apuesta[i] = "1";
                ta[i][0] = 0;
            } else if (ta[i][1] >= ta[i][0] && ta[i][1] >= ta[i][2]) {
                apuesta[i] = "X";
                ta[i][1] = 0;
            } else if (ta[i][2] >= ta[i][0] && ta[i][2] >= ta[i][1]) {
                apuesta[i] = "2";
                ta[i][2] = 0;
            }
        }

        // calculamos posibles dobles y su peso
        var doble = new String[15];
        var doblePeso = new int[15];
        for (var i = 0; i < 15; i++) {
            if (ta[i][0] >= ta[i][1] && ta[i][0] >= ta[i][2]) {
                doble[i] = "1";
                doblePeso[i] = ta[i][0];
                ta[i][0] = 0;
            } else if (ta[i][1] >= ta[i][0] && ta[i][1] >= ta[i][2]) {
                doble[i] = "X";
                doblePeso[i] = ta[i][1];
                ta[i][1] = 0;
            } else if (ta[i][2] >= ta[i][0] && ta[i][2] >= ta[i][1]) {
                doble[i] = "2";
                doblePeso[i] = ta[i][2];
                ta[i][2] = 0;
            }
        }

        // calculamos posibles triples y su peso
        var triple = new String[15];
        var triplePeso = new int[15];
        for (var i = 0; i < 15; i++) {
            if (ta[i][0] >= ta[i][1] && ta[i][0] >= ta[i][2]) {
                triple[i] = "1";
                triplePeso[i] = ta[i][0];
            } else if (ta[i][1] >= ta[i][0] && ta[i][1] >= ta[i][2]) {
                triple[i] = "X";
                triplePeso[i] = ta[i][1];
            } else if (ta[i][2] >= ta[i][0] && ta[i][2] >= ta[i][1]) {
                triple[i] = "2";
                triplePeso[i] = ta[i][2];
            }
        }

        var doblesFinales = new String[15];
        var triplesFinales = new String[15];

        if (tipo.equalsIgnoreCase("3 Dobles")) {
            for (var i = 0; i < 3; i++) {
                var p = 0;
                var n = 0;
                var v = "";
                for (var ii = 0; ii < 14; ii++) { // el pleno al 15 no cuenta
                    if (doblePeso[ii] > n) {
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
        var datos = new ArrayList<>();
        for (var i = 0; i < 15; i++) {

            var ap = new ApuestaMix();
            ap.setNumero(valueOf(i + 1));
            ap.setPartido(jornada.getPartido()[i]);

            switch (apuesta[i]) {
                case "1":
                    ap.setCol1(true);
                    break;
                case "X":
                    ap.setColX(true);
                    break;
                case "2":
                    ap.setCol2(true);
                    break;
                default:
                    break;
            }

            if (doblesFinales[i] != null) {
                switch (doblesFinales[i]) {
                    case "1":
                        ap.setCol1(true);
                        break;
                    case "X":
                        ap.setColX(true);
                        break;
                    case "2":
                        ap.setCol2(true);
                        break;
                    default:
                        break;
                }
            }

            if (triplesFinales[i] != null) {
                switch (triplesFinales[i]) {
                    case "1":
                        ap.setCol1(true);
                        break;
                    case "X":
                        ap.setColX(true);
                        break;
                    case "2":
                        ap.setCol2(true);
                        break;
                    default:
                        break;
                }
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

        var comp = competicionActiva();
        req.setAttribute("comp", comp);
        if (comp == null) {
            return;
        }

        if (!sePuedeCumplimentar()) {
            throw new UnsupportedOperationException("No se puede cumplimentar este dia");
        }

        var op = (String) req.getParameter("operacion");
        ApuestaQuiniela apuesta1 = null;
        ApuestaQuiniela apuesta2 = null;

        var jornada = obtenerProximaJornada(comp);
        req.setAttribute("jornada", jornada);

        if (jornada == null || jornada.isValidada() || jornada.isBloqueada()) {
            op = null;
        } else {
            var apuestas
                    = (ArrayList) JDBCDAOQuiniela.obtenerApuestas(eq, jornada);
            apuesta1 = (ApuestaQuiniela) apuestas.get(0);
            apuesta2 = (ApuestaQuiniela) apuestas.get(1);
        }

        if (op == null) {
        }// no hacemos nada
        else if (op.equals("Grabar")) {
            var resultCol1 = new String[15];
            var resultCol2 = new String[15];
            var isError = false;
            var txt = new StringBuilder();
            txt.append("Has cumplimentado tus columnas correctamente <br/><br/>");
            String nombre, valor;
            for (var i = 0; i < 15; i++) {
                nombre = "col1_apuesta" + (i + 1);
                valor = req.getParameter(nombre);
                if ((valor == null)
                        || (!valor.equals("1")
                        && !valor.equals("X")
                        && !valor.equals("2"))) {
                    req.setAttribute("error", "Falta cumplimentar la apuesta " + (i + 1) + " colummna 1");
                    isError = true;
                }
                resultCol1[i] = valor;
                txt.append(valor).append('-');

                nombre = "col2_apuesta" + (i + 1);
                valor = req.getParameter(nombre);
                if ((valor == null)
                        || (!valor.equals("1")
                        && !valor.equals("X")
                        && !valor.equals("2"))) {
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
            grabarApuesta(apuesta1);
            grabarApuesta(apuesta2);
            if (!isError) {
                getCorreo().enviarMail("ClubDeportivo Cumplimentación Quiniela Correcta",
                        txt.toString(), true, eq.getClub().getMail());
            }

        }

        obtenerApuestas(req, jornada, apuesta1, apuesta2, false);

    }

    private void cumplimentarAdmin(HttpServletRequest req, EquipoQuiniela eq, int numEquipos)
            throws DAOException, IllegalArgumentException {

        req.setAttribute("op", "cumplimentarAdmin");
        req.setAttribute("titulo", "ADMINISTRAR JORNADA");

        var comp = competicionActiva();
        req.setAttribute("comp", comp);
        if (comp == null) {
            return;
        }

        // Aqui verificar si se puede administrar
        //if (!sePuedeCumplimentar())
        //    throw new UnsupportedOperationException("No se puede cumplimentar este dia");
        var op = (String) req.getParameter("operacion");
        var jornada = obtenerProximaJornada(comp);
        var datos = new ArrayList<>();
        var numJornada = "";
        var partidos = new String[15];
        var resultados = new String[15];
        if (null == op) {

        } else {
            switch (op) {
                case "Crear Jornada":
                    var nj = (String) req.getParameter("numeroJornada");
                    var pj = (String) req.getParameter("puntosJornada");
                    try {
                        if (nj == null || nj == "") {
                            throw new UnsupportedOperationException("Se ha de informar numero de jornada");
                        }
                        if (pj == null || nj == "") {
                            throw new UnsupportedOperationException("Se han de informar los puntos de la jornada");
                        }
                        for (var i = 1; i < 16; i++) {
                            var np = valueOf(i);
                            if (np.length() == 1) {
                                np = "0" + np;
                            }
                            partidos[i - 1] = req.getParameter("partido" + np);
                        }
                        jornada = crearJornadaQuiniela(nj, pj, partidos);
                    } catch (Exception ex) {
                        req.setAttribute("error", "Error al crear jornada quiniela ".concat(ex.getMessage()));
                    }
                    break;
                case "Grabar":
                    for (var i = 1; i < 16; i++) {
                        var np = valueOf(i);
                        if (np.length() == 1) {
                            np = "0" + np;
                        }
                        partidos[i - 1] = req.getParameter("partido" + np);
                        resultados[i - 1] = req.getParameter("resultado" + valueOf(i));
                    }
                    actualizarJornadaQuiniela(jornada, partidos, resultados);
                    break;
                case "Validar":
                    validarJornada(comp);
                    jornada = null;
                    break;
                default:
                    break;
            }
        }

        if (jornada == null || jornada.isValidada()) {
            for (var i = 1; i < 16; i++) {
                var ap = new ApuestaMix();
                ap.setNumero(i);
                ap.setPartido("");
                ap.setCol1(false);
                ap.setColX(false);
                ap.setCol2(false);
                datos.add(ap);
            }
        } else {
            numJornada = valueOf(jornada.getNumero());
            String r;
            for (var i = 1; i < 16; i++) {
                var ap = new ApuestaMix();
                ap.setNumero(i);
                ap.setPartido(jornada.getPartido()[i - 1]);
                r = jornada.getResultado()[i - 1];
                if (null != r) {
                    switch (r) {
                        case "1":
                            ap.setCol1(true);
                            break;
                        case "X":
                            ap.setColX(true);
                            break;
                        case "2":
                            ap.setCol2(true);
                            break;
                        default:
                            break;
                    }
                }
                datos.add(ap);
            }
        }

        boolean resultadosCompletos;
        if (jornada != null) {
            resultadosCompletos = jornada.resultadosCompletos();
        } else {
            resultadosCompletos = false;
        }

        req.setAttribute("datos", datos);
        req.setAttribute("numJornada", numJornada);
        req.setAttribute("jornadaActiva", jornada != null);
        req.setAttribute("resultadosCompletos", resultadosCompletos);
        if (jornada != null) {
            req.setAttribute("puntosJornada", jornada.getPuntos());
        } else {
            req.setAttribute("puntosJornada", numEquipos * 15 * 10);
        }

    }

    private void jornada(HttpServletRequest req, EquipoQuiniela eqActual)
            throws DAOException {

        req.setAttribute("op", "jornada");
        req.setAttribute("titulo", "JORNADA ACTUAL");

        var eq = selectorEquipo(req, eqActual);

        if (eq == null) {
            return;
        }

        var tipoApuesta = tipoApuestas(req);
        var comp = competicionActiva();
        req.setAttribute("comp", comp);
        if (comp == null) {
            return;
        }

        var jornada = obtenerProximaJornada(comp);
        if (jornada == null) {
            return;
        }

        if (jornada.isValidada()) {
            throw new UnsupportedOperationException("No hay ninguna jornada pendiente");
        }

        if (sePuedeCumplimentar() && !jornada.isBloqueada()) {
            throw new UnsupportedOperationException("Todavia no se puede consultar la jornada");
        }

        req.setAttribute("jornada", jornada);

        if (jornada == null || jornada.isValidada()) {
            return;
        }

        ArrayList<ApuestaQuiniela> apuestas
                = (ArrayList) JDBCDAOQuiniela.obtenerApuestas(eq, jornada);

        obtenerDatosApuestas(req, jornada, eq);

    }

    private String tipoApuestas(HttpServletRequest req) {

        String tipo;
        var lista = new ArrayList<>();

        if (req.getParameter("tipoapuesta") != null) {
            req.setAttribute("apuestaelegida", req.getParameter("tipoapuesta"));
            tipo = req.getParameter("tipoapuesta");
        } else {
            req.setAttribute("apuestaelegida", "Sencilla");
            tipo = "Sencilla";
        }

        lista.add("Sencilla");

        req.setAttribute("tiposapuesta", lista);

        return tipo;

    }

    private boolean sePuedeCumplimentar() {

        var sePuede = false;
        var confDias = diascumplimentacion;
        var dias = confDias.split(",");
        var calendario = getInstance();
        calendario.setTime(new Date());

        var diaAct = valueOf(calendario.get(DAY_OF_WEEK));
        for (var dia : dias) {
            if (dia.equals(diaAct)) {
                sePuede = true;
            }
        }
        return sePuede;

    }

    private String diasParaCumplimentar() {

        var txt = new StringBuilder();
        var confDias = diascumplimentacion;
        var dias = confDias.split(",");
        for (var dia : dias) {
            if (dia.equals("2")) {
                txt.append("Lunes,");
            }
            if (dia.equals("3")) {
                txt.append("Martes,");
            }
            if (dia.equals("4")) {
                txt.append("Miercoles,");
            }
            if (dia.equals("5")) {
                txt.append("Jueves,");
            }
            if (dia.equals("6")) {
                txt.append("Viernes,");
            }
            if (dia.equals("7")) {
                txt.append("Sabado,");
            }
            if (dia.equals("1")) {
                txt.append("Domingo,");
            }
        }

        String txtDias;
        if (txt.toString().isEmpty()) {
            txtDias = "";
        } else {
            txtDias = txt.substring(0, txt.length() - 1);
        }

        return txtDias;

    }

    private void clasificacion(HttpServletRequest req, EquipoQuiniela eq)
            throws DAOException {

        req.setAttribute("op", "clasificacion");
        req.setAttribute("titulo", "CLASIFICACION DEL GRUPO");

        var grp = eq.getClub().getGrupo();
        var idComp = req.getParameter("competicion");
        CompeticionQuiniela comp;
        if (idComp == null) {
            comp = competicionActiva();
        } else {
            var id = parseLong(idComp);
            comp = obtenerCompeticionPorId(id);
        }

        var numJornadas = numeroJornadasDisputadas(comp);
        var pts
                = clasificacionQuiniela(comp, false);
        var ptsGrp = new ArrayList<>();
        for (var punt : pts) {
            if (punt.getEquipo().getClub().getGrupo().equals(grp)) {
                ptsGrp.add(punt);
            }
        }

        req.setAttribute("comp", comp);
        req.setAttribute("clasificacion", ptsGrp);
        req.setAttribute("numJornadas", numJornadas);

    }

    private CompeticionQuiniela selectorCompeticion(HttpServletRequest req)
            throws DAOException {

        var comps
                = new HashSet<>();

        CompeticionQuiniela comp = null;
        var compAct = competicionActiva();

        if (compAct != null) {
            comps.add(compAct);
        }

        var compsNoAct
                = competicionesNoActivas();

        comps.addAll(compsNoAct);

        if (req.getParameter("competicion") == null) {

            if (compAct != null) {
                comp = compAct;
            } else if (!compsNoAct.isEmpty()) {
                comp = compsNoAct.get(compsNoAct.size() - 1);
            }
        } else {
            var id = parseLong(req.getParameter("competicion"));
            comp = obtenerCompeticionPorId(id);
        }

        req.setAttribute("competicion", comp.getId());
        req.setAttribute("competiciones", comps);

        return comp;

    }

    private JornadaQuiniela selectorJornada(HttpServletRequest req,
            CompeticionQuiniela comp) throws DAOException {

        var jors
                = (ArrayList<JornadaQuiniela>) obtenerJornadasValidadas(comp);

        JornadaQuiniela jor = null;

        if (req.getParameter("jornada") == null) {
            jor = jors.get(jors.size() - 1);
        } else {
            var id = parseInt(req.getParameter("jornada"));
            for (var jq : jors) {
                if (jq.getId() == id) {
                    jor = jq;
                }
            }
            if (jor == null) {
                jor = jors.get(jors.size() - 1);
            }
        }

        req.setAttribute("jornada", jor.getId());
        req.setAttribute("jornadas", jors);

        return jor;
    }

    private EquipoQuiniela selectorEquipo(HttpServletRequest req, EquipoQuiniela eqActual)
            throws DAOException {

        EquipoQuiniela eq;

        if (req.getParameter("equipo") == null) {
            eq = eqActual;
        } else {
            var id = parseLong(req.getParameter("equipo"));
            eq = obtenerSimpleEquipoQuiniela(id);
        }

        var listaeq = new ArrayList<>();
        listaeq.add(eq);

        var lista
                = listaEquiposQuiniela();
        for (var equipoQuiniela : lista) {
            var mismoGrupo = eq.getClub().getGrupo().equals(equipoQuiniela.getClub().getGrupo());
            if (!eq.equals(equipoQuiniela) && mismoGrupo) {
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

        var comp = selectorCompeticion(req);
        if (comp == null) {
            return;
        }

        var jor = selectorJornada(req, comp);
        if (jor == null) {
            return;
        }

        var eq = selectorEquipo(req, eqActual);
        if (eq == null) {
            return;
        }

        var apuestas
                = (ArrayList) JDBCDAOQuiniela.obtenerApuestas(eq, jor);
        if (apuestas.size() > 1) {
            var apuesta1 = (ApuestaQuiniela) apuestas.get(0);
            var apuesta2 = (ApuestaQuiniela) apuestas.get(1);
            obtenerApuestas(req, jor, apuesta1, apuesta2, false);

            var est = obtenerEstadistica(eq, comp, jor);

            req.setAttribute("estadistica", est);
        }

    }

    private ArrayList<JornadaQuiniela> jornadasDisputadas(CompeticionQuiniela comp) throws DAOException {

        var jornadas
                = (ArrayList<JornadaQuiniela>) obtenerJornadasValidadas(comp);
        var grp = comp.getGrupo();
        var eqs = listaEquiposQuiniela(grp);
        for (var jornada : jornadas) {
            jornada.setEstadisticas(new ArrayList());
            for (var eq : eqs) {
                var est
                        = obtenerEstadistica(eq, comp, jornada);
                if (est != null) {
                    est.setEquipo(eq.getNombre());
                    eq.setEstadisitica(est);
                    jornada.getEstadisticas().add(est);
                }
            }
            var estq = jornada.getEstadisticas();
            clasificar(estq, true);
            jornada.setEstadisticas(estq);
        }

        return jornadas;
    }

    private void competiciones(HttpServletRequest req) throws DAOException {

        req.setAttribute("op", "competiciones");
        req.setAttribute("titulo", "COMPETICIONES");

        var comp = selectorCompeticion(req);
        if (comp == null) {
            return;
        }

        var jornadas = jornadasDisputadas(comp);
        var jornadasCol1 = new ArrayList<>();
        var jornadasCol2 = new ArrayList<>();
        var jornadasCol3 = new ArrayList<>();
        var i = 1;
        for (var jornada : jornadas) {
            if (i == 1) {
                jornadasCol1.add(jornada);
                i = 2;
            } else if (i == 2) {
                jornadasCol2.add(jornada);
                i = 3;
            } else if (i == 3) {
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

        var comp = selectorCompeticion(req);
        if (comp == null) {
            return;
        }

        var jornadas = jornadasDisputadas(comp);
        var jornadasCol1 = new ArrayList<>();
        var jornadasCol2 = new ArrayList<>();
        var jornadasCol3 = new ArrayList<>();
        var i = 1;
        for (var jornada : jornadas) {
            if (i == 1) {
                jornadasCol1.add(jornada);
                i = 2;
            } else if (i == 2) {
                jornadasCol2.add(jornada);
                i = 3;
            } else if (i == 3) {
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

        var competiciones
                = competicionesNoActivas();

        req.setAttribute("competiciones", competiciones);
    }
}
