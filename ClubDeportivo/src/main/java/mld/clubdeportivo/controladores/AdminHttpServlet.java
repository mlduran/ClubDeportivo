
package mld.clubdeportivo.controladores;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import static mld.clubdeportivo.base.Deporte.Futbol8;
import static mld.clubdeportivo.base.Deporte.Quiniela;
import mld.clubdeportivo.base.futbol8.JugadorFutbol8;
import mld.clubdeportivo.base.quinielas.CompeticionQuiniela;
import mld.clubdeportivo.base.quinielas.JornadaQuiniela;
import static mld.clubdeportivo.bd.JDBCDAOClub.eliminarClub;
import static mld.clubdeportivo.bd.JDBCDAOClub.listaClubs;
import static mld.clubdeportivo.bd.JDBCDAOClub.mailsClubs;
import static mld.clubdeportivo.bd.JDBCDAOClub.obtenerSimpleClub;
import static mld.clubdeportivo.bd.JDBCDAOFaq.obtenerFaqsNoContestadas;
import static mld.clubdeportivo.bd.JDBCDAOGrupo.eliminarGrupo;
import static mld.clubdeportivo.bd.JDBCDAOGrupo.obtenerGruposActivos;
import static mld.clubdeportivo.bd.JDBCDAOGrupo.obtenerSimpleGrupo;
import static mld.clubdeportivo.bd.ObjetoDAO.limpiarTablas;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.eliminarCompeticionFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.listaCompetiones;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerCompeticion;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.competicionActiva;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerJornadasNoValidadas;
import static mld.clubdeportivo.controladores.LanzarJornadaFutbol8.crearCompeticiones;
import static mld.clubdeportivo.controladores.UtilesHttpServlet.comprobarEstadoAdmin;
import static mld.clubdeportivo.controladores.UtilesHttpServlet.ejecutaBackup;
import static mld.clubdeportivo.controladores.UtilesHttpServlet.pruebaCorreo;
import static mld.clubdeportivo.controladores.UtilesQuiniela.cargarJornadasQuiniela_obsoleto;
import static mld.clubdeportivo.controladores.UtilesQuiniela.crearCompeticionQuiniela;
import static mld.clubdeportivo.controladores.UtilesQuiniela.finalizarCompeticion;
import static mld.clubdeportivo.controladores.UtilesQuiniela.obtenerApuestaGeneral;
import static mld.clubdeportivo.utilidades.Correo.getCorreo;
import static mld.clubdeportivo.utilidades.IODatos.cargarMaestroEntrenadores;
import static mld.clubdeportivo.utilidades.IODatos.cargarMaestroEquipos;
import static mld.clubdeportivo.utilidades.IODatos.eliminarFicheros;
import static mld.clubdeportivo.utilidades.StringUtil.tratarSaltosLineaHTML;



/**
 *
 * @author Miguel
 */
public class AdminHttpServlet extends HttpServlet {

   
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

        
        if (!comprobarEstadoAdmin(req, resp)) return;
        
        long id = (Long) req.getSession().getAttribute("idClub");
        
        if (id != 0) return;
        
        var op = (String) req.getParameter("operacion");
        var appManager = this.getServletContext();
        var ruta = appManager.getInitParameter("rutaficheroscarga");

        try{

            var comp =
                    competicionActiva();
            ArrayList <JornadaQuiniela> jornadas = new ArrayList();

            if (comp != null){
                jornadas = (ArrayList<JornadaQuiniela>) obtenerJornadasNoValidadas(comp);
            }

            req.setAttribute("jornadasq", jornadas);  
            req.setAttribute("compQuini", comp);  
            if (appManager.getAttribute("mantenimiento") != null && 
                    appManager.getAttribute("mantenimiento").equals("true"))
                req.setAttribute("mantenimient", "true");
            else 
                req.setAttribute("mantenimient", "false");

            if (null == op) {
            }// no hacemos nada
            else switch (op) {
                case "Iniciar Mantenimiento":
                    appManager.setAttribute("mantenimiento", "true");
                    req.setAttribute("mantenimient", "true");
                    break;
                case "Acabar Mantenimiento":
                    appManager.setAttribute("mantenimiento", "false");
                    req.setAttribute("mantenimient", "false");
                    break;
                case "Hacer Backup BD":
                    ejecutaBackup(appManager, "Backup");
                    break;
                case "Prueba Correo":
                    var correo = (String) req.getParameter("correoPrueba");
                    pruebaCorreo(appManager, correo);
                    break;
                case "Carga ficheros Quiniela":
                    if (competicionActiva() != null) {
                        cargarJornadasQuiniela_obsoleto(ruta);
                    }
                    break;
                case "Eliminar Ficheros Quiniela":
                    eliminarFicheros(ruta, "quini");
                    break;
                case "Crear Competicion Quiniela":
                    {
                        var nombreComp = req.getParameter("nombrecompeticion");
                        var compQ = new CompeticionQuiniela(nombreComp);
                        crearCompeticionQuiniela(compQ);
                        break;
                    }
                case "Finalizar Competicion Quiniela":
                    finalizarCompeticion(comp);
                    break;
                case "Resultados Generales":
                    {
                        var compQ = competicionActiva();
                        var numero = parseInt(req.getParameter("jornadaresult"));
                        var r = obtenerApuestaGeneral(compQ, numero);
                        req.setAttribute("mostrarjornadaresult", r);
                        break;
                    }
                case "Carga Maestro Entrenadores Futbol8":
                    {
                        var fich = ruta + "/entrenadores.txt";
                        cargarMaestroEntrenadores(fich);
                        break;
                    }
                case "Carga Maestro Equipos Futbol8":
                    {
                        var fich = ruta + "/equipos.txt";
                        cargarMaestroEquipos(fich);
                        break;
                    }
                case "Crear Competiciones Futbol8":
                    crearCompeticiones();
                    break;
                case "Limpiar Tablas":
                    limpiarTablas();
                    break;
                case "Eliminar Grupo":
                    var idGrp = parseInt(req.getParameter("grupo"));
                    var grp = obtenerSimpleGrupo(idGrp);
                    eliminarGrupo(grp);
                    break;
                case "Eliminar Club":
                    var idClub = parseInt(req.getParameter("club"));
                    var club = obtenerSimpleClub(idClub);
                    eliminarClub(club);
                    break;
                case "Eliminar Competicion Futbol8":
                    var idCompF8 = parseInt(req.getParameter("competicionFutbol8"));
                    var compf8 = obtenerCompeticion((long)idCompF8);
                    eliminarCompeticionFutbol8(compf8);
                    break;
                case "Enviar Comunicado":
                    var tipo = req.getParameter("tipoComunicado");
                    var textComunicado = req.getParameter("txtComunicado");
                    if (textComunicado != null) {
                        //textComunicado = new String(textComunicado.getBytes(), "UTF-8");
                        textComunicado = tratarSaltosLineaHTML(textComunicado);
                    }
                    var tituloComunicado = req.getParameter("tituloComunicado");
                    //if (tituloComunicado != null)
                    //tituloComunicado = new String(tituloComunicado.getBytes(), "UTF-8");
                    if (tituloComunicado == null) tituloComunicado = "";
                    var correos = new ArrayList<String>();
                    if (tipo != null && textComunicado != null && !textComunicado.isEmpty()) {
                        if (tipo.equals("General")) {
                            correos.addAll(mailsClubs());
                        } else if (tipo.equals("Futbol8")) {
                            correos.addAll(mailsClubs(Futbol8));
                        } else if (tipo.equals("Quiniela")) {
                            correos.addAll(mailsClubs(Quiniela));
                        } else if (tipo.equals("Futbol8")) {
                            correos.addAll(mailsClubs(Futbol8));
                        }
                        if (!correos.isEmpty()) {
                            getCorreo().enviarMailMasivo("ClubDeportivo Aviso " + tipo + " " + tituloComunicado, textComunicado, true, correos);
                        }
                    }
                    break;
                default:
                    break;
            }
            var grps = obtenerGruposActivos();
            var clubs = listaClubs();
            var comps = listaCompetiones();
            var faqs = obtenerFaqsNoContestadas();
            
            if (!faqs.isEmpty())
                req.setAttribute("faq", faqs.get(0));
            
            req.setAttribute("grupos", grps);
            req.setAttribute("clubs", clubs);
            req.setAttribute("competiciones", comps);
                

        } catch (Exception ex) {
            req.setAttribute("error", ex.getMessage());
        }
        
        var jugsF8 = new ArrayList<JugadorFutbol8>();
        for (var i = 100; i < 1000; i = i + 100){
            var jug = new JugadorFutbol8();
            jug.setValoracion(i);
            jugsF8.add(jug);
        }
        
        req.setAttribute("jugsF8", jugsF8);

        var view =
                req.getRequestDispatcher("/PanelControl/panelControlAdmin.jsp");
        view.forward(req, resp);
       
       
    }

  


}

     

