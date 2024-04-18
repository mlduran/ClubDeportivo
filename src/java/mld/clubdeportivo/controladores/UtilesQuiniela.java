/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package mld.clubdeportivo.controladores;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.mail.MessagingException;
import mld.clubdeportivo.base.Club;
import mld.clubdeportivo.base.Deporte;
import mld.clubdeportivo.base.Grupo;
import mld.clubdeportivo.base.quinielas.*;
import mld.clubdeportivo.bd.*;
import mld.clubdeportivo.bd.quinielas.*;
import mld.clubdeportivo.utilidades.Correo;
import mld.clubdeportivo.utilidades.IODatos;
import java.util.logging.*;
import mld.clubdeportivo.utilidades.Calculos;


/**
 *
 * @author Miguel
 */
public class UtilesQuiniela {
    
    private static Logger logApp
            = Logger.getLogger(UtilesQuiniela.class.getName());
    
    private static String URL_WEB = "http://www.resultados-futbol.com/scripts/api/api.php";
    private static String URL_API = "http://apiclient.resultados-futbol.com/scripts/api/api.php";
    private static String KEY = "0f37440445075cc85cadebe0b25690d8";
    
    static void enviaCorreoNuevaCompeticion() throws DAOException, IOException, MessagingException {
        // Lo enviamos a todos aunque no sean del grupo
        ArrayList<String> correos = JDBCDAOClub.mailsClubs();
        
        String txt = "Se ha creado la competición de quiniela para esta temporada, si no estas dado de alta puedes hacerlo en este momento";
        
        Correo.getCorreo().enviarMailMasivo("ClubDeportivo Nueva Competicion Quiniela",
                txt, true, correos);
        
        
    }
    
    static void enviaCorreoNuevaJornada(JornadaQuiniela jornada) throws DAOException, IOException, MessagingException {
        
        ArrayList<String> correos = JDBCDAOClub.mailsClubs(Deporte.Quiniela);
        
        String txt = "Se ha creado la jornada " + jornada.getNumero() + " para la quiniela, ya puedes cumplimentar tus columnas";
        txt = txt + "\n\n Los puntos para el ganador de esta jornada seran ".concat(String.valueOf(jornada.getPuntos()));
        
        Correo.getCorreo().enviarMailMasivo("ClubDeportivo Nueva Jornada Quiniela " + jornada.getNumero(),
                txt, true, correos);
        
        
    }
    
    
    static void enviaCorreoFinCompeticion() throws DAOException, IOException, MessagingException {
        
        ArrayList<String> correos = JDBCDAOClub.mailsClubs(Deporte.Quiniela);
        
        String txt = "Se ha finalizado la competicion de Quiniela de esta temporada";
        
        Correo.getCorreo().enviarMailMasivo("ClubDeportivo Fin Competicion Quiniela",
                txt, true, correos);
        
        
    }
    
    static void enviaCorreoJornadaValidada(int numero) throws DAOException {
        
        StringBuilder txt = new StringBuilder();
        
        txt.append("Se ha validado la jornada numero ").append(numero).append("<br/><br/>");
        
        CompeticionQuiniela comp = JDBCDAOQuiniela.competicionActiva();
        
        ArrayList<PuntuacionQuiniela> pts =
                JDBCDAOQuiniela.clasificacionQuiniela(comp, false);
        
        ArrayList<String> correos;
        ArrayList<Grupo> grupos = JDBCDAOGrupo.obtenerGruposActivos();
        
        for (Grupo grp : grupos) {
            correos = JDBCDAOClub.mailsClubs(grp, Deporte.Quiniela);
            
            txt.append("Clasificación");
            txt.append("<table>");
            for (PuntuacionQuiniela punt : pts)
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
            Correo.getCorreo().enviarMailMasivo("ClubDeportivo Validacion Quiniela Jornada " + numero,
                    txt.toString(), true, correos);
        }
        
    }
    
    public static void crearCompeticionQuiniela(CompeticionQuiniela comp)
            throws DAOException, IOException, MessagingException{
        
        CompeticionQuinielaDAO dao = new CompeticionQuinielaDAO();
        
        dao.save(comp);
        
        PuntuacionQuinielaDAO daopun = new PuntuacionQuinielaDAO();
        ArrayList<EquipoQuiniela> eqs = JDBCDAOQuiniela.equiposQuinielaActivos();
        
        for (EquipoQuiniela eq : eqs) {
            PuntuacionQuiniela pun = new PuntuacionQuiniela();
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
        
        int numJornadas = JDBCDAOQuiniela.numeroJornadasDisputadas(comp);
        
        ArrayList<PuntuacionQuiniela> pts =
                JDBCDAOQuiniela.clasificacionQuiniela(comp, true);
        
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
        int pos = 1;
        for (PuntuacionQuiniela punQuini : pts) {
            Club club = punQuini.getEquipo().getClub();
            ptsAct = club.getRanking();
            ptsNew = numJornadas * 50 / pos;
            club.setRanking(ptsAct / 2 + ptsNew);
            pos++;
            JDBCDAOClub.grabarClub(club);
        }
        
        comp.setActiva(false);
        JDBCDAOQuiniela.grabarCompeticion(comp);
        
        enviaCorreoFinCompeticion();
        
    }
    
    public static ArrayList<EquipoQuiniela> obtenerDatosLanzamiento(
            CompeticionQuiniela comp, JornadaQuiniela jornada) throws DAOException{
        
        
        EquipoQuinielaDAO daoeq = new EquipoQuinielaDAO();
        ArrayList<EquipoQuiniela> eqs = JDBCDAOQuiniela.equiposQuinielaActivos();
        
        ClubDAO daoclub = new ClubDAO();
        GrupoDAO daogrp = new GrupoDAO();
        
        for (EquipoQuiniela eq : eqs) {
            
            long idClub = daoeq.idClub(eq);
            Club club = (Club) daoclub.getObjetoById(idClub);
            eq.setClub(club);
            long idgrp = daoclub.idGrupo(club);
            Grupo grp = (Grupo) daogrp.getObjetoById(idgrp);
            club.setGrupo(grp);
            
            ArrayList<ApuestaQuiniela> apuestas = JDBCDAOQuiniela.obtenerApuestasPorJornada(eq, jornada);
            eq.setApuestas(apuestas);
            for (ApuestaQuiniela apuesta : apuestas) {
                apuesta.setEquipo(eq);
            }
            
            ArrayList<PuntuacionQuiniela> puntos = new ArrayList();
            PuntuacionQuiniela pt = JDBCDAOQuiniela.obtenerPuntosPorEquipo(comp, eq);
            pt.setEquipo(eq);
            pt.setCompeticion(comp);
            puntos.add(pt);
            eq.setPuntuaciones(puntos);
            
            ArrayList<EstadisticaQuiniela> estadis = new ArrayList();
            EstadisticaQuiniela est = JDBCDAOQuiniela.obtenerEstadistica(eq, comp, jornada);
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
        
        ApuestaQuinielaDAO daoap = new ApuestaQuinielaDAO();
        EstadisticaQuinielaDAO daoest = new EstadisticaQuinielaDAO();
        PuntuacionQuinielaDAO daopun = new PuntuacionQuinielaDAO();
        
        CompeticionQuiniela comp = JDBCDAOQuiniela.competicionActiva();
        
        if (comp == null) return;
        
        ArrayList<JornadaQuiniela> jornadas =
                (ArrayList<JornadaQuiniela>) JDBCDAOQuiniela.obtenerJornadasNoValidadas(comp);
        
        for (JornadaQuiniela jornada : jornadas) {
            for (int i = 1; i < 3; i++){
                ApuestaQuiniela apuesta = new ApuestaQuiniela();
                apuesta.setEquipo(eq);
                apuesta.setJornada(jornada);
                daoap.save(apuesta);
            }
            EstadisticaQuiniela est = new EstadisticaQuiniela();
            est.setEquipo(eq.getNombre());
            est.setCompeticion(comp.getNombre());
            est.setJornada(jornada.getDescripcion());
            est.setPuntos(0);
            est.setAciertos("");
            daoest.save(est);
        }
        
        PuntuacionQuiniela pun = new PuntuacionQuiniela();
        pun.setEquipo(eq);
        pun.setClub(eq.getNombre());
        pun.setCompeticion(comp);
        pun.setPuntos(0);
        pun.setVictorias(0);
        daopun.save(pun);
        
        
    }
    
    public static JornadaQuiniela crearJornadaQuiniela(String numJornada, String ptsJornada, String[] partidos) throws DAOException{
        
        // Crea una nueva jornada
        
        CompeticionQuinielaDAO dao = new CompeticionQuinielaDAO();
        JornadaQuinielaDAO daoj = new JornadaQuinielaDAO();
        ApuestaQuinielaDAO daoap = new ApuestaQuinielaDAO();
        EquipoQuinielaDAO daoeq = new EquipoQuinielaDAO();
        EstadisticaQuinielaDAO daoest = new EstadisticaQuinielaDAO();
        CompeticionQuiniela comp = JDBCDAOQuiniela.competicionActiva();
        ArrayList<EquipoQuiniela> eqs = JDBCDAOQuiniela.obtenerEquiposActivos();
        
        if (comp == null){
            throw new UnsupportedOperationException("Error no existe competicion activa");
        }
        
        int numJor = Integer.parseInt(numJornada.trim());
        int ptsJor = Integer.parseInt(ptsJornada.trim());
        
        JornadaQuiniela jorQuini = JDBCDAOQuiniela.obtenerJornadaPorNumero(comp, numJor);
        
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
        
        for (EquipoQuiniela eq : eqs){
            
            long idclub = daoeq.idClub(eq);
            Club club = JDBCDAOClub.obtenerSimpleClub(idclub);
            eq.setClub(club);
            // Creamos 2 columnas
            for (int i = 1; i < 3; i++){
                ApuestaQuiniela apuesta = new ApuestaQuiniela();
                apuesta.setEquipo(eq);
                apuesta.setJornada(jorQuini);
                daoap.save(apuesta);
            }
            EstadisticaQuiniela est = new EstadisticaQuiniela();
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
        } catch (IOException ex) {
            logApp.log(Level.SEVERE, "Error envio mail: " + ex.getMessage());
        } catch (MessagingException ex) {
            logApp.log(Level.SEVERE, "Error envio mail: " + ex.getMessage());
        }
        
        return jorQuini;
        
    }
    
    public static void actualizarJornadaQuiniela(JornadaQuiniela jorQuini,
            String[] partidos, String[] resultados) throws DAOException{
        
        // Actualiza partidos y resultados jornada
        
        JornadaQuinielaDAO daoj = new JornadaQuinielaDAO();
        
        if (jorQuini == null)
            throw new UnsupportedOperationException("La jornada no existe");
        
        jorQuini.setPartido(partidos);
        jorQuini.setResultado(resultados);
        daoj.save(jorQuini);
        
        
    }
    
    public static void cargarJornadasQuiniela_obsoleto(String ruta) throws DAOException{
        
        // si se ha creado una nueva jornada devuelve true si no
        // false
        
        boolean isCreaJornada = false;
        
        ArrayList<HashMap<String, Object>> datosFich;
        try {
            datosFich = IODatos.obtenerDatosFicherosQuini(ruta);
        } catch (FileNotFoundException ex) {
            logApp.log(Level.SEVERE, "Error en carga de datos: " + ex.getMessage());
            return;
        } catch (IOException ex) {
            logApp.log(Level.SEVERE, "Error en carga de datos: " + ex.getMessage());
            return;
        }
        
        CompeticionQuinielaDAO dao = new CompeticionQuinielaDAO();
        JornadaQuinielaDAO daoj = new JornadaQuinielaDAO();
        ApuestaQuinielaDAO daoap = new ApuestaQuinielaDAO();
        EquipoQuinielaDAO daoeq = new EquipoQuinielaDAO();
        EstadisticaQuinielaDAO daoest = new EstadisticaQuinielaDAO();
        CompeticionQuiniela comp = JDBCDAOQuiniela.competicionActiva();
        ArrayList<EquipoQuiniela> eqs = JDBCDAOQuiniela.obtenerEquiposActivos();
        JornadaQuiniela jorQuini = null;
        
        if (comp == null){
            logApp.log(Level.SEVERE, "Error en carga de datos, no existe competicion activa");
            return;
        }
        
        int ultimaJornada = 0;
        for (HashMap<String, Object> datos : datosFich) {
            
            String nomComp = (String) datos.get("competicion");
            if (!comp.getNombre().equals(nomComp)) {
                logApp.log(Level.SEVERE, "Error en carga de datos, no existe competicion " + nomComp);
                continue;
            }
            int jornada = Integer.valueOf((String) datos.get("jornada"));
            
            String[] partidos = (String[]) datos.get("partidos");
            String[] resultados = (String[]) datos.get("resultados");
            
            jorQuini = JDBCDAOQuiniela.obtenerJornadaPorNumero(comp, jornada);
            
            if (jorQuini != null && jorQuini.isValidada()) continue;
            
            if (jorQuini != null) jorQuini.setCompeticion(comp);
            
            boolean crearRel = false;
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
                for (EquipoQuiniela eq : eqs){
                    
                    long idclub = daoeq.idClub(eq);
                    Club club = JDBCDAOClub.obtenerSimpleClub(idclub);
                    eq.setClub(club);
                    // Creamos 2 columnas
                    for (int i = 1; i < 3; i++){
                        ApuestaQuiniela apuesta = new ApuestaQuiniela();
                        apuesta.setEquipo(eq);
                        apuesta.setJornada(jorQuini);
                        daoap.save(apuesta);
                    }
                    EstadisticaQuiniela est = new EstadisticaQuiniela();
                    est.setEquipo(eq.getNombre());
                    est.setCompeticion(comp.getNombre());
                    est.setJornada(jorQuini.getDescripcion());
                    est.setPuntos(0);
                    est.setAciertos("");
                    daoest.save(est);
                }
        }
        int proxJor = JDBCDAOQuiniela.obtenerNumeroProximaJornada();
        comp.setProximaJornada(proxJor);
        if (ultimaJornada != 0) comp.setUltimaJornada(ultimaJornada);
        dao.save(comp);
        
        if (isCreaJornada) try {
            enviaCorreoNuevaJornada(jorQuini);
        } catch (IOException ex) {
            logApp.log(Level.SEVERE, "Error envio mail: " + ex.getMessage());
        } catch (MessagingException ex) {
            logApp.log(Level.SEVERE, "Error envio mail: " + ex.getMessage());
        }
        
    }
    
    
    
    public static void validarJornada(CompeticionQuiniela comp) throws DAOException{
        
        ArrayList<JornadaQuiniela> jornadas =
                (ArrayList<JornadaQuiniela>) JDBCDAOQuiniela.obtenerJornadasNoValidadas(comp);
        
        if (jornadas.size() > 0){
            JornadaQuiniela jor = jornadas.get(0);
            try {
                validarJornada(jor.getNumero());
            }catch (Exception ex){
                logApp.log(Level.SEVERE, "Error al validar jornada: " + ex.getMessage());
            }
        }
    }
    
    public static void validarJornada(int numero) throws DAOException{
        
        
        PuntuacionQuinielaDAO daopunt = new PuntuacionQuinielaDAO();
        EstadisticaQuinielaDAO daoest= new EstadisticaQuinielaDAO();
        CompeticionQuinielaDAO daocomp = new CompeticionQuinielaDAO();
        
        CompeticionQuiniela comp = JDBCDAOQuiniela.competicionActiva();
        if (comp == null)
            throw new UnsupportedOperationException("No hay competicion activa");
        
        JornadaQuinielaDAO daojor = new JornadaQuinielaDAO();
        
        JornadaQuiniela jornada = JDBCDAOQuiniela.obtenerJornadaPorNumero(comp, numero);
        jornada.setCompeticion(comp);
        
        if (!jornada.resultadosCompletos())
            throw new UnsupportedOperationException("Faltan resultados en jornada " + numero);
        
        String[] resultados = jornada.getResultado();
        
        ArrayList<EquipoQuiniela> eqs = obtenerDatosLanzamiento(comp, jornada);
        
        HashMap<Long,ArrayList<EquipoQuiniela>> listaEqs =
                new HashMap<Long,ArrayList<EquipoQuiniela>>();
        
        for (EquipoQuiniela eq : eqs) {
            Long grp = eq.getClub().getGrupo().getId();
            if (listaEqs.get(grp) == null )
                listaEqs.put(grp, new ArrayList<EquipoQuiniela>());
            listaEqs.get(grp).add(eq);
        }
        
        Iterator itr = listaEqs.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry e = (Map.Entry)itr.next();
            ArrayList<EquipoQuiniela> eqsGrp = (ArrayList<EquipoQuiniela>) e.getValue();
            CalculosQuiniela.calculoResultadosQuiniela(eqsGrp, resultados, false, jornada.getPuntos());
        }
        
        CalculosQuiniela.calculoResultadosQuiniela(eqs, resultados, true, jornada.getPuntos());
        
        for (EquipoQuiniela eq : eqs) {
            daopunt.save(eq.getPuntuaciones().get(0));
            daoest.save(eq.getEstadisiticas().get(0));
            JDBCDAOClub.grabarClub(eq.getClub());
        }
        
        jornada.setValidada(true);
        daojor.save(jornada);
        
        
        comp.setUltimaJornada(numero);
        comp.setProximaJornada(0);
        daocomp.save(comp);
        
        enviaCorreoJornadaValidada(numero);
        
    }
    
    public static void validarJornada(int numero, String ruta) throws DAOException{
        
        
        PuntuacionQuinielaDAO daopunt = new PuntuacionQuinielaDAO();
        EstadisticaQuinielaDAO daoest= new EstadisticaQuinielaDAO();
        
        CompeticionQuiniela comp = JDBCDAOQuiniela.competicionActiva();
        if (comp == null)
            throw new UnsupportedOperationException("No hay competicion activa");
        
        JornadaQuinielaDAO daojor = new JornadaQuinielaDAO();
        
        JornadaQuiniela jornada = JDBCDAOQuiniela.obtenerJornadaPorNumero(comp, numero);
        jornada.setCompeticion(comp);
        
        String[] resultados = jornada.getResultado();
        
        int results = 0;
        for (int i = 0; i < 15; i++)
            if(resultados[i] != null) results++;
        if (results == 0) return;
        else if (results != 15)
            throw new UnsupportedOperationException("Faltan resultados en jornada " + numero);
        
        ArrayList<EquipoQuiniela> eqs = obtenerDatosLanzamiento(comp, jornada);
        
        HashMap<Long,ArrayList<EquipoQuiniela>> listaEqs =
                new HashMap<Long,ArrayList<EquipoQuiniela>>();
        
        for (EquipoQuiniela eq : eqs) {
            Long grp = eq.getClub().getGrupo().getId();
            if (listaEqs.get(grp) == null )
                listaEqs.put(grp, new ArrayList<EquipoQuiniela>());
            listaEqs.get(grp).add(eq);
        }
        
        Iterator itr = listaEqs.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry e = (Map.Entry)itr.next();
            ArrayList<EquipoQuiniela> eqsGrp = (ArrayList<EquipoQuiniela>) e.getValue();
            CalculosQuiniela.calculoResultadosQuiniela(eqsGrp, resultados, false, jornada.getPuntos());
        }
        
        CalculosQuiniela.calculoResultadosQuiniela(eqs, resultados, true, jornada.getPuntos());
        
        for (EquipoQuiniela eq : eqs) {
            daopunt.save(eq.getPuntuaciones().get(0));
            daoest.save(eq.getEstadisiticas().get(0));
            JDBCDAOClub.grabarClub(eq.getClub());
        }
        
        jornada.setValidada(true);
        daojor.save(jornada);
        
        enviaCorreoJornadaValidada(numero);
        
        IODatos.eliminarFicheroJornadaValidada(ruta, comp.getNombre(), numero);
        
    }
    
    
    public static ArrayList<String> obtenerDatosBD(String numJornada) throws DAOException
    {
        
        ArrayList<String> results = new ArrayList<String>();
        
        CompeticionQuiniela comp = JDBCDAOQuiniela.competicionActiva();
        if (comp == null)
            throw new UnsupportedOperationException("No hay competicion activa");
        
        JornadaQuiniela jornada = JDBCDAOQuiniela.obtenerJornadaPorNumero(comp, Integer.parseInt(numJornada));
        
        for (String r : jornada.getResultado()){
            results.add(r);
        }
        
        return results;
        
    }
    
    public static ArrayList<String> obtenerResultados(String jornada, boolean url)
            throws  IOException, ParseException, DAOException {
        
        return obtenerDatosBD(jornada);
        
    }
    
    public static boolean isFechaFinDeSemana(String fecha) throws ParseException{
        
        //String fecha = "2014-11-08 16:00:00";
        
        boolean finSemana = false;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = formatter.parse(fecha);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
        if (diaSemana == 7 || diaSemana == 1){ // sabado o domingo
            finSemana = true;
        }
        return finSemana;
        
    }
    
    public static boolean isFechaPosteriorActual(String fecha) throws ParseException{
        
        
        
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = formatter.parse(fecha);
        Date dateActual = new Date();
        
        return dateActual.before(date);
        
        
    }
    
    private static void escribirResultados(String nomfich, ArrayList<String> results) throws IOException {
        
        String[] lineas = IODatos.lineasFicheroUTF8(nomfich);
        ArrayList<String> newLineas = new ArrayList<String>();
        
        System.out.println(results.size());
        for (int i = 0; i < results.size(); i++){
            // Si ya tenemos el resultado lo eliminamos
            String linea = lineas[i].split("\t")[0];
            
            System.out.println((i + 1) + " " + linea + "\t" + results.get(i));
            newLineas.add(linea + "\t" + results.get(i));
        }
        
        try {
            Writer output = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(nomfich),"UTF8"));
            for (String linea : newLineas) {
                //System.out.print(linea);
                output.write( linea + "\n" );
            }
            output.close();
        }catch(Exception ex){}
        
    }
    
    private static void escribirPartidos(String nomfich, ArrayList<String> partidos) throws IOException {
        
        try {
            Writer output = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(nomfich),"UTF8"));
            for (String linea : partidos) {
                //System.out.print(linea);
                output.write( linea + "\n" );
            }
            output.close();
        }catch(Exception ex){}
        
        
    }
    
    public static String obtenerApuestaGeneral(CompeticionQuiniela comp,
            int numjornada) throws DAOException{
        
        JornadaQuiniela jornada = JDBCDAOQuiniela.obtenerJornadaPorNumero(comp, numjornada);
        
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
        
        String result = "";
        for (int i= 0; i < 15; i++){
            result = result.concat(apuesta[i]).concat(" ");
        }
        
        return result;
        
    }
}
