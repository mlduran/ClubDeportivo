/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package mld.clubdeportivo.controladores;

import jakarta.mail.MessagingException;
import java.io.*;
import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;
import static java.lang.String.valueOf;
import static java.lang.System.out;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import static java.util.Calendar.DAY_OF_WEEK;
import mld.clubdeportivo.base.Club;
import mld.clubdeportivo.base.Grupo;
import mld.clubdeportivo.base.quinielas.*;
import mld.clubdeportivo.bd.*;
import mld.clubdeportivo.bd.quinielas.*;
import java.util.logging.*;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static mld.clubdeportivo.base.Deporte.Quiniela;
import static mld.clubdeportivo.base.quinielas.CalculosQuiniela.calculoResultadosQuiniela;
import static mld.clubdeportivo.bd.JDBCDAOClub.grabarClub;
import static mld.clubdeportivo.bd.JDBCDAOClub.mailsClubs;
import static mld.clubdeportivo.bd.JDBCDAOClub.obtenerSimpleClub;
import static mld.clubdeportivo.bd.JDBCDAOGrupo.obtenerGruposActivos;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.clasificacionQuiniela;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.competicionActiva;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.equiposQuinielaActivos;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.grabarCompeticion;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.numeroJornadasDisputadas;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerApuestas;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerApuestasPorJornada;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerEquiposActivos;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerEstadistica;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerJornadaPorNumero;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerJornadasNoValidadas;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerNumeroProximaJornada;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerPuntosPorEquipo;
import static mld.clubdeportivo.utilidades.Correo.getCorreo;
import static mld.clubdeportivo.utilidades.IODatos.eliminarFicheroJornadaValidada;
import static mld.clubdeportivo.utilidades.IODatos.lineasFicheroUTF8;
import static mld.clubdeportivo.utilidades.IODatos.obtenerDatosFicherosQuini;


/**
 *
 * @author Miguel
 */
public class UtilesQuiniela {
    
    private static final Logger logApp
            = getLogger(UtilesQuiniela.class.getName());
    
    private static final String URL_WEB = "http://www.resultados-futbol.com/scripts/api/api.php";
    private static final String URL_API = "http://apiclient.resultados-futbol.com/scripts/api/api.php";
    private static final String KEY = "0f37440445075cc85cadebe0b25690d8";
    
    static void enviaCorreoNuevaCompeticion() throws DAOException, IOException, MessagingException {
        // Lo enviamos a todos aunque no sean del grupo
        var correos = mailsClubs();
        var txt = "Se ha creado la competición de quiniela para esta temporada, si no estas dado de alta puedes hacerlo en este momento";
        
        getCorreo().enviarMailMasivo("ClubDeportivo Nueva Competicion Quiniela",
                txt, true, correos);
        
        
    }
    
    static void enviaCorreoNuevaJornada(JornadaQuiniela jornada) throws DAOException, IOException, MessagingException {
        
        var correos = mailsClubs(Quiniela);
        var txt = "Se ha creado la jornada " + jornada.getNumero() + " para la quiniela, ya puedes cumplimentar tus columnas";
        txt = txt + "\n\n Los puntos para el ganador de esta jornada seran ".concat(String.valueOf(jornada.getPuntos()));
        
        getCorreo().enviarMailMasivo("ClubDeportivo Nueva Jornada Quiniela " + jornada.getNumero(),
                txt, true, correos);
        
        
    }
    
    
    static void enviaCorreoFinCompeticion() throws DAOException, IOException, MessagingException {
        
        var correos = mailsClubs(Quiniela);
        var txt = "Se ha finalizado la competicion de Quiniela de esta temporada";
        
        getCorreo().enviarMailMasivo("ClubDeportivo Fin Competicion Quiniela",
                txt, true, correos);
        
        
    }
    
    static void enviaCorreoJornadaValidada(int numero) throws DAOException {
        
        var txt = new StringBuilder();
        
        txt.append("Se ha validado la jornada numero ").append(numero).append("<br/><br/>");
        
        var comp = competicionActiva();
        var pts =
                clasificacionQuiniela(comp, false);
        
        ArrayList<String> correos;
        var grupos = obtenerGruposActivos();
        for (var grp : grupos) {
            correos = mailsClubs(grp, Quiniela);
            
            txt.append("Clasificación");
            txt.append("<table>");
            for (var punt : pts)
                if (punt.getEquipo().getClub().getGrupo().equals(grp)){
                    txt.append("<tr>");
                    txt.append("<td>");
                    txt.append(punt.getNombreEquipo());
                    txt.append("</td>");
                    txt.append("<td>");
                    txt.append(punt.getPuntos());
                    txt.append("</td>");
                    txt.append("</tr>");
                }
            txt.append("</table>");
            getCorreo().enviarMailMasivo("ClubDeportivo Validacion Quiniela Jornada " + numero,
                    txt.toString(), true, correos);
        }
        
    }
    
    public static void crearCompeticionQuiniela(CompeticionQuiniela comp)
            throws DAOException, IOException, MessagingException{
        
        var dao = new CompeticionQuinielaDAO();
        
        dao.save(comp);
        
        var daopun = new PuntuacionQuinielaDAO();
        var eqs = equiposQuinielaActivos();
        for (var eq : eqs) {
            var pun = new PuntuacionQuiniela();
            pun.setEquipo(eq);
            pun.setClub(eq.getNombre());
            pun.setCompeticion(comp);
            pun.setPuntos(0);
            pun.setVictorias(0);
            daopun.save(pun);
        }
        
        enviaCorreoNuevaCompeticion();
        
    }
    
    static void finalizarCompeticion(CompeticionQuiniela comp)
            throws DAOException, IOException, MessagingException {
        
        var numJornadas = numeroJornadasDisputadas(comp);
        var pts =
                clasificacionQuiniela(comp, true);
        
        PuntuacionQuiniela campeon;
        PuntuacionQuiniela subCampeon;
        if (pts.size() > 0){
            campeon = pts.get(0);
            comp.setCampeon(campeon.getEquipo().getNombre());
        }
        if (pts.size() > 1) {
            subCampeon = pts.get(1);
            comp.setSubcampeon(subCampeon.getEquipo().getNombre());
        }
        
        // Actualizacion ranking
        // primero quitamos la mitad de los puntos que todo el mundo tiene
        // y despues añadimos
        int ptsAct, ptsNew;
        var pos = 1;
        for (var punQuini : pts) {
            var club = punQuini.getEquipo().getClub();
            ptsAct = club.getRanking();
            ptsNew = numJornadas * 50 / pos;
            club.setRanking(ptsAct / 2 + ptsNew);
            pos++;
            grabarClub(club);
        }
        
        comp.setActiva(false);
        grabarCompeticion(comp);
        
        enviaCorreoFinCompeticion();
        
    }
    
    public static ArrayList<EquipoQuiniela> obtenerDatosLanzamiento(
            CompeticionQuiniela comp, JornadaQuiniela jornada) throws DAOException{
        
        
        var daoeq = new EquipoQuinielaDAO();
        var eqs = equiposQuinielaActivos();
        var daoclub = new ClubDAO();
        var daogrp = new GrupoDAO();
        for (var eq : eqs) {
            
            var idClub = daoeq.idClub(eq);
            var club = (Club) daoclub.getObjetoById(idClub);
            eq.setClub(club);
            var idgrp = daoclub.idGrupo(club);
            var grp = (Grupo) daogrp.getObjetoById(idgrp);
            club.setGrupo(grp);
            
            var apuestas = obtenerApuestasPorJornada(eq, jornada);
            eq.setApuestas(apuestas);
            for (var apuesta : apuestas) {
                apuesta.setEquipo(eq);
            }
            
            ArrayList<PuntuacionQuiniela> puntos = new ArrayList();
            var pt = obtenerPuntosPorEquipo(comp, eq);
            pt.setEquipo(eq);
            pt.setCompeticion(comp);
            puntos.add(pt);
            eq.setPuntuaciones(puntos);
            
            ArrayList<EstadisticaQuiniela> estadis = new ArrayList();
            var est = obtenerEstadistica(eq, comp, jornada);
            est.setEquipo(eq.getNombre());
            est.setCompeticion(comp.getNombre());
            est.setJornada(jornada.getDescripcion());
            estadis.add(est);
            eq.setEstadisiticas(estadis);
            
        }
        
        return eqs;
        
    }
    
    public static void crearRegistrosNuevoEquipo(EquipoQuiniela eq)
            throws DAOException{
        
        var daoap = new ApuestaQuinielaDAO();
        var daoest = new EstadisticaQuinielaDAO();
        var daopun = new PuntuacionQuinielaDAO();
        var comp = competicionActiva();
        
        if (comp == null) return;
        
        var jornadas =
                (ArrayList<JornadaQuiniela>) obtenerJornadasNoValidadas(comp);
        for (var jornada : jornadas) {
            for (var i = 1; i < 3; i++){
                var apuesta = new ApuestaQuiniela();
                apuesta.setEquipo(eq);
                apuesta.setJornada(jornada);
                daoap.save(apuesta);
            }
            var est = new EstadisticaQuiniela();
            est.setEquipo(eq.getNombre());
            est.setCompeticion(comp.getNombre());
            est.setJornada(jornada.getDescripcion());
            est.setPuntos(0);
            est.setAciertos("");
            daoest.save(est);
        }
        var pun = new PuntuacionQuiniela();
        pun.setEquipo(eq);
        pun.setClub(eq.getNombre());
        pun.setCompeticion(comp);
        pun.setPuntos(0);
        pun.setVictorias(0);
        daopun.save(pun);
        
        
    }
    
    public static JornadaQuiniela crearJornadaQuiniela(String numJornada, String ptsJornada, String[] partidos) throws DAOException{
        
        // Crea una nueva jornada
        
        var dao = new CompeticionQuinielaDAO();
        var daoj = new JornadaQuinielaDAO();
        var daoap = new ApuestaQuinielaDAO();
        var daoeq = new EquipoQuinielaDAO();
        var daoest = new EstadisticaQuinielaDAO();
        var comp = competicionActiva();
        var eqs = obtenerEquiposActivos();
        
        if (comp == null){
            throw new UnsupportedOperationException("Error no existe competicion activa");
        }
        
        var numJor = parseInt(numJornada.trim());
        var ptsJor = parseInt(ptsJornada.trim());
        var jorQuini = obtenerJornadaPorNumero(comp, numJor);
        
        if (jorQuini != null)
            throw new UnsupportedOperationException("Este numero de jornada ya existe");
        
        jorQuini = new JornadaQuiniela();
        jorQuini.setCompeticion(comp);
        jorQuini.setDescripcion("Jornada " + numJornada);
        jorQuini.setNumero(numJor);
        jorQuini.setPuntos(ptsJor);
        /* Obsoleto
        int pts = 16;
        int val_aleatorio = Calculos.valorAleatorio(1, 4);
        if (val_aleatorio == 1) {
        pts = 32;
        }else if(val_aleatorio == 2) {
        pts = 24;
        }
        jorQuini.setPuntos(pts);
        */
        jorQuini.setPartido(partidos);
        jorQuini.setResultado(new String[15]);
        jorQuini.setBloqueada(false);
        
        jorQuini.setValidada(false);
        daoj.save(jorQuini);
        
        for (var eq : eqs){
            
            var idclub = daoeq.idClub(eq);
            var club = obtenerSimpleClub(idclub);
            eq.setClub(club);
            // Creamos 2 columnas
            for (var i = 1; i < 3; i++){
                var apuesta = new ApuestaQuiniela();
                apuesta.setEquipo(eq);
                apuesta.setJornada(jorQuini);
                daoap.save(apuesta);
            }
            var est = new EstadisticaQuiniela();
            est.setEquipo(eq.getNombre());
            est.setCompeticion(comp.getNombre());
            est.setJornada(jorQuini.getDescripcion());
            est.setPuntos(0);
            est.setAciertos("");
            daoest.save(est);
        }
        
        comp.setProximaJornada(numJor);
        dao.save(comp);
        
        try {
            enviaCorreoNuevaJornada(jorQuini);
        } catch (IOException | MessagingException ex) {
            logApp.log(SEVERE, "Error envio mail: " + ex.getMessage());
        }
        
        return jorQuini;
        
    }
    
    public static void actualizarJornadaQuiniela(JornadaQuiniela jorQuini,
            String[] partidos, String[] resultados) throws DAOException{
        
        // Actualiza partidos y resultados jornada
        
        var daoj = new JornadaQuinielaDAO();
        
        if (jorQuini == null)
            throw new UnsupportedOperationException("La jornada no existe");
        
        jorQuini.setPartido(partidos);
        jorQuini.setResultado(resultados);
        daoj.save(jorQuini);
        
        
    }
    
    public static void cargarJornadasQuiniela_obsoleto(String ruta) throws DAOException{
        
        // si se ha creado una nueva jornada devuelve true si no
        // false
        
        var isCreaJornada = false;
        
        ArrayList<HashMap<String, Object>> datosFich;
        try {
            datosFich = obtenerDatosFicherosQuini(ruta);
        } catch (FileNotFoundException ex) {
            logApp.log(SEVERE, "Error en carga de datos: " + ex.getMessage());
            return;
        } catch (IOException ex) {
            logApp.log(SEVERE, "Error en carga de datos: " + ex.getMessage());
            return;
        }
        
        var dao = new CompeticionQuinielaDAO();
        var daoj = new JornadaQuinielaDAO();
        var daoap = new ApuestaQuinielaDAO();
        var daoeq = new EquipoQuinielaDAO();
        var daoest = new EstadisticaQuinielaDAO();
        var comp = competicionActiva();
        var eqs = obtenerEquiposActivos();
        JornadaQuiniela jorQuini = null;
        
        if (comp == null){
            logApp.log(SEVERE, "Error en carga de datos, no existe competicion activa");
            return;
        }
        
        var ultimaJornada = 0;
        for (var datos : datosFich) {
            
            var nomComp = (String) datos.get("competicion");
            if (!comp.getNombre().equals(nomComp)) {
                logApp.log(SEVERE, "Error en carga de datos, no existe competicion " + nomComp);
                continue;
            }
            int jornada = valueOf((String) datos.get("jornada"));
            
            var partidos = (String[]) datos.get("partidos");
            var resultados = (String[]) datos.get("resultados");
            
            jorQuini = obtenerJornadaPorNumero(comp, jornada);
            
            if (jorQuini != null && jorQuini.isValidada()) continue;
            
            if (jorQuini != null) jorQuini.setCompeticion(comp);
            
            var crearRel = false;
            if (jorQuini == null){
                jorQuini = new JornadaQuiniela();
                jorQuini.setCompeticion(comp);
                jorQuini.setDescripcion("Jornada " + jornada);
                jorQuini.setNumero(jornada);
                jorQuini.setPartido(partidos);
                jorQuini.setBloqueada(false);
                crearRel = true;
                isCreaJornada = true;
            }else{
                jorQuini.setPartido(partidos);
                if (resultados[0] != null){
                    jorQuini.setResultado(resultados);
                    jorQuini.setFecha(new Date());
                    ultimaJornada = jornada;
                }
            }
            
            jorQuini.setValidada(false);
            daoj.save(jorQuini);
            if (crearRel == true)
                for (var eq : eqs){
                    
                    var idclub = daoeq.idClub(eq);
                    var club = obtenerSimpleClub(idclub);
                    eq.setClub(club);
                    // Creamos 2 columnas
                    for (var i = 1; i < 3; i++){
                        var apuesta = new ApuestaQuiniela();
                        apuesta.setEquipo(eq);
                        apuesta.setJornada(jorQuini);
                        daoap.save(apuesta);
                    }
                    var est = new EstadisticaQuiniela();
                    est.setEquipo(eq.getNombre());
                    est.setCompeticion(comp.getNombre());
                    est.setJornada(jorQuini.getDescripcion());
                    est.setPuntos(0);
                    est.setAciertos("");
                    daoest.save(est);
                }
        }
        var proxJor = obtenerNumeroProximaJornada();
        comp.setProximaJornada(proxJor);
        if (ultimaJornada != 0) comp.setUltimaJornada(ultimaJornada);
        dao.save(comp);
        
        if (isCreaJornada) try {
            enviaCorreoNuevaJornada(jorQuini);
        } catch (IOException | MessagingException ex) {
            logApp.log(SEVERE, "Error envio mail: " + ex.getMessage());
        }
        
    }
    
    
    
    public static void validarJornada(CompeticionQuiniela comp) throws DAOException{
        
        var jornadas =
                (ArrayList<JornadaQuiniela>) obtenerJornadasNoValidadas(comp);
        
        if (jornadas.size() > 0){
            var jor = jornadas.get(0);
            try {
                validarJornada(jor.getNumero());
            }catch (Exception ex){
                logApp.log(SEVERE, "Error al validar jornada: " + ex.getMessage());
            }
        }
    }
    
    public static void validarJornada(int numero) throws DAOException{
        
        
        var daopunt = new PuntuacionQuinielaDAO();
        var daoest= new EstadisticaQuinielaDAO();
        var daocomp = new CompeticionQuinielaDAO();
        var comp = competicionActiva();
        if (comp == null)
            throw new UnsupportedOperationException("No hay competicion activa");
        
        var daojor = new JornadaQuinielaDAO();
        var jornada = obtenerJornadaPorNumero(comp, numero);
        jornada.setCompeticion(comp);
        
        if (!jornada.resultadosCompletos())
            throw new UnsupportedOperationException("Faltan resultados en jornada " + numero);
        
        var resultados = jornada.getResultado();
        var eqs = obtenerDatosLanzamiento(comp, jornada);
        var listaEqs = new HashMap<Object,ArrayList<EquipoQuiniela>>();
        for (var eq : eqs) {
            Long grp = eq.getClub().getGrupo().getId();
            if (listaEqs.get(grp) == null )
                listaEqs.put(grp, new ArrayList<>());
            listaEqs.get(grp).add(eq);
        }
        for (Map.Entry e : listaEqs.entrySet()) {
            var eqsGrp = (ArrayList<EquipoQuiniela>) e.getValue();
            calculoResultadosQuiniela(eqsGrp, resultados, false, jornada.getPuntos());
        }
        
        calculoResultadosQuiniela(eqs, resultados, true, jornada.getPuntos());
        
        for (var eq : eqs) {
            daopunt.save(eq.getPuntuaciones().get(0));
            daoest.save(eq.getEstadisiticas().get(0));
            grabarClub(eq.getClub());
        }
        
        jornada.setValidada(true);
        daojor.save(jornada);
        
        
        comp.setUltimaJornada(numero);
        comp.setProximaJornada(0);
        daocomp.save(comp);
        
        enviaCorreoJornadaValidada(numero);
        
    }
    
    public static void validarJornada_borrar(int numero, String ruta) throws DAOException{
        
        
        var daopunt = new PuntuacionQuinielaDAO();
        var daoest= new EstadisticaQuinielaDAO();
        var comp = competicionActiva();
        if (comp == null)
            throw new UnsupportedOperationException("No hay competicion activa");
        
        var daojor = new JornadaQuinielaDAO();
        var jornada = obtenerJornadaPorNumero(comp, numero);
        jornada.setCompeticion(comp);
        
        var resultados = jornada.getResultado();
        var results = 0;
        for (var i = 0; i < 15; i++)
            if(resultados[i] != null) results++;
        if (results == 0) return;
        else if (results != 15)
            throw new UnsupportedOperationException("Faltan resultados en jornada " + numero);
        
        var eqs = obtenerDatosLanzamiento(comp, jornada);
        var listaEqs =
                new HashMap<Object,ArrayList<EquipoQuiniela>>();
        for (var eq : eqs) {
            Long grp = eq.getClub().getGrupo().getId();
            if (listaEqs.get(grp) == null )
                listaEqs.put(grp, new ArrayList<>());
            listaEqs.get(grp).add(eq);
        }
        for (Map.Entry e : listaEqs.entrySet()) {
            var eqsGrp = (ArrayList<EquipoQuiniela>) e.getValue();
            calculoResultadosQuiniela(eqsGrp, resultados, false, jornada.getPuntos());
        }
        
        calculoResultadosQuiniela(eqs, resultados, true, jornada.getPuntos());
        
        for (var eq : eqs) {
            daopunt.save(eq.getPuntuaciones().get(0));
            daoest.save(eq.getEstadisiticas().get(0));
            grabarClub(eq.getClub());
        }
        
        jornada.setValidada(true);
        daojor.save(jornada);
        
        enviaCorreoJornadaValidada(numero);
        
        eliminarFicheroJornadaValidada(ruta, comp.getNombre(), numero);
        
    }
    
    
    public static ArrayList<String> obtenerDatosBD(String numJornada) throws DAOException
    {
        
        var results = new ArrayList<String>();
        var comp = competicionActiva();
        if (comp == null)
            throw new UnsupportedOperationException("No hay competicion activa");
        
        var jornada = obtenerJornadaPorNumero(comp, parseInt(numJornada));
        results.addAll(Arrays.asList(jornada.getResultado()));
        
        return results;
        
    }
    
    public static ArrayList<String> obtenerResultados(String jornada, boolean url)
            throws  IOException, ParseException, DAOException {
        
        return obtenerDatosBD(jornada);
        
    }
    
    public static boolean isFechaFinDeSemana(String fecha) throws ParseException{
        
        //String fecha = "2014-11-08 16:00:00";
        
        var finSemana = false;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        var date = formatter.parse(fecha);
        var calendar = new GregorianCalendar();
        calendar.setTime(date);
        var diaSemana = calendar.get(DAY_OF_WEEK);
        if (diaSemana == 7 || diaSemana == 1){ // sabado o domingo
            finSemana = true;
        }
        return finSemana;
        
    }
    
    public static boolean isFechaPosteriorActual(String fecha) throws ParseException{
        
        
        
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        var date = formatter.parse(fecha);
        var dateActual = new Date();
        
        return dateActual.before(date);
        
        
    }
    
    private static void escribirResultados(String nomfich, ArrayList<String> results) throws IOException {
        
        var lineas = lineasFicheroUTF8(nomfich);
        var newLineas = new ArrayList<>();
        
        out.println(results.size());
        for (var i = 0; i < results.size(); i++){
            // Si ya tenemos el resultado lo eliminamos
            var linea = lineas[i].split("\t")[0];
            
            out.println((i + 1) + " " + linea + "\t" + results.get(i));
            newLineas.add(linea + "\t" + results.get(i));
        }
        
        try {
            try (Writer output = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(nomfich),"UTF8"))) {
                for (var linea : newLineas) {
                    //System.out.print(linea);
                    output.write( linea + "\n" );
                }
            }
        }catch(Exception ex){}
        
    }
    
    private static void escribirPartidos(String nomfich, ArrayList<String> partidos) throws IOException {
        
        try {
            try (Writer output = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(nomfich),"UTF8"))) {
                for (var linea : partidos) {
                    //System.out.print(linea);
                    output.write( linea + "\n" );
                }
            }
        }catch(Exception ex){}
        
        
    }
    
    public static String obtenerApuestaGeneral(CompeticionQuiniela comp,
            int numjornada) throws DAOException{
        
        var jornada = obtenerJornadaPorNumero(comp, numjornada);
        // Creo tabla de 15 * 3 para meter los acumulados de cada apuesta
        var ta = new int[15][3];
        var apuestas =
                (ArrayList<ApuestaQuiniela>) obtenerApuestas(jornada);
        for (var ap: apuestas) {
            for (var i = 0; i < 15; i++){
                var resul = ap.getResultado()[i];
                if (null == resul) continue;
                else switch (resul) {
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
        // calculamos el primer reultado
        var apuesta = new String[15];
        for (var i = 0; i < 15; i++){
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
        
        var result = "";
        for (var i= 0; i < 15; i++){
            result = result.concat(apuesta[i]).concat(" ");
        }
        
        return result;
        
    }
}
