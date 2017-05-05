
package mld.clubdeportivo.controladores;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mld.clubdeportivo.base.Club;
import mld.clubdeportivo.base.Deporte;
import mld.clubdeportivo.base.Faq;
import mld.clubdeportivo.base.Grupo;
import mld.clubdeportivo.base.futbol8.CompeticionFutbol8;
import mld.clubdeportivo.base.futbol8.JugadorFutbol8;
import mld.clubdeportivo.base.quinielas.CompeticionQuiniela;
import mld.clubdeportivo.base.quinielas.JornadaQuiniela;
import mld.clubdeportivo.bd.JDBCDAOClub;
import mld.clubdeportivo.bd.JDBCDAOFaq;
import mld.clubdeportivo.bd.JDBCDAOGrupo;
import mld.clubdeportivo.bd.ObjetoDAO;
import mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8;
import mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela;
import mld.clubdeportivo.utilidades.Correo;
import mld.clubdeportivo.utilidades.IODatos;
import mld.clubdeportivo.utilidades.StringUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


/**
 *
 * @author Miguel
 */
public class AdminHttpServlet extends HttpServlet {

    private static Logger logger = LogManager.getLogger(AdminHttpServlet.class);
   
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

        
        if (!UtilesHttpServlet.comprobarEstadoAdmin(req, resp)) return;
        
        long id = (Long) req.getSession().getAttribute("idClub");
        
        if (id != 0) return;
        
        String op = (String) req.getParameter("operacion");
        ServletContext appManager = this.getServletContext();

        String ruta = appManager.getInitParameter("rutaficheroscarga");

        try{

            CompeticionQuiniela comp =
                    JDBCDAOQuiniela.competicionActiva();
            ArrayList <JornadaQuiniela> jornadas = new ArrayList();

            if (comp != null){
                jornadas = (ArrayList<JornadaQuiniela>) JDBCDAOQuiniela.obtenerJornadasNoValidadas(comp);
            }

            req.setAttribute("jornadasq", jornadas);  
            req.setAttribute("compQuini", comp);  
            if (appManager.getAttribute("mantenimiento") != null && 
                    appManager.getAttribute("mantenimiento").equals("true"))
                req.setAttribute("mantenimient", "true");
            else 
                req.setAttribute("mantenimient", "false");

            if (op == null) {
            }// no hacemos nada
            else if (op.equals("Iniciar Mantenimiento")) { 
               appManager.setAttribute("mantenimiento", "true");
               req.setAttribute("mantenimient", "true");

            }
            else if (op.equals("Acabar Mantenimiento")) { 
               appManager.setAttribute("mantenimiento", "false");
               req.setAttribute("mantenimient", "false");

            }
            else if (op.equals("Hacer Backup BD")) { 
               UtilesHttpServlet.ejecutaBackup(appManager, "Backup");

            }
            else if (op.equals("Carga ficheros Quiniela")) { 
                if (JDBCDAOQuiniela.competicionActiva() != null){
                    UtilesQuiniela.cargarJornadasQuiniela(ruta);                                        
                }

            } else if (op.equals("Eliminar Ficheros Quiniela")) {
                    IODatos.eliminarFicheros(ruta, "quini");

            } else if (op.equals("Crear Competicion Quiniela")) {
                String nombreComp = req.getParameter("nombrecompeticion");
                CompeticionQuiniela compQ = 
                        new CompeticionQuiniela(nombreComp);
                UtilesQuiniela.crearCompeticionQuiniela(compQ);
                
            } else if (op.equals("Finalizar Competicion Quiniela")) {
                UtilesQuiniela.finalizarCompeticion(comp);
                
            } else if (op.equals("Crear Fichero Partidos Quiniela")){
                String jornada = req.getParameter("jornadapartidos");
                UtilesQuiniela.crearFicheroJornada(jornada, ruta);

            } else if (op.equals("Lanzar Jornada Quiniela")){
                int numero = Integer.parseInt(req.getParameter("jornadaquiniela"));
                UtilesQuiniela.validarJornada(numero, ruta);
                
            } else if (op.equals("Lanzar Jornada Quiniela Obteniendo Resultados")){
                CompeticionQuiniela compQ = JDBCDAOQuiniela.competicionActiva();
                UtilesQuiniela.validarJornada(compQ, ruta);
                
            } else if (op.equals("Resultados Generales")){
                CompeticionQuiniela compQ = JDBCDAOQuiniela.competicionActiva();
                int numero = Integer.parseInt(req.getParameter("jornadaresult"));
                String r = UtilesQuiniela.obtenerApuestaGeneral(compQ, numero);
                req.setAttribute("mostrarjornadaresult", r);
            } else if (op.equals("Carga Maestro Entrenadores Futbol8")){
                String fich = ruta + "/entrenadores.txt";
                IODatos.cargarMaestroEntrenadores(fich);
                
            } else if (op.equals("Carga Maestro Equipos Futbol8")){
                String fich = ruta + "/equipos.txt";
                IODatos.cargarMaestroEquipos(fich);
            } else if (op.equals("Crear Competiciones Futbol8")){
                LanzarJornadaFutbol8.crearCompeticiones();              
                
            } else if (op.equals("Limpiar Tablas")){
                ObjetoDAO.limpiarTablas();
                
            } else if (op.equals("Eliminar Grupo")){
                int idGrp = Integer.parseInt(req.getParameter("grupo"));
                Grupo grp = JDBCDAOGrupo.obtenerSimpleGrupo(idGrp);
                JDBCDAOGrupo.eliminarGrupo(grp);
            
            } else if (op.equals("Eliminar Club")){
                int idClub = Integer.parseInt(req.getParameter("club"));
                Club club = JDBCDAOClub.obtenerSimpleClub(idClub);
                JDBCDAOClub.eliminarClub(club);
                
            } else if (op.equals("Eliminar Competicion Futbol8")){
                int idCompF8 = Integer.parseInt(req.getParameter("competicionFutbol8"));
                CompeticionFutbol8 compf8 = JDBCDAOFutbol8.obtenerCompeticion((long)idCompF8);
                JDBCDAOFutbol8.eliminarCompeticionFutbol8(compf8);            
            
            } else if (op.equals("Enviar Comunicado")){
                String tipo = req.getParameter("tipoComunicado");
                String textComunicado = req.getParameter("txtComunicado");
                if (textComunicado != null){
                    textComunicado = new String(textComunicado.getBytes(), "UTF-8");
                    textComunicado = StringUtil.tratarSaltosLineaHTML(textComunicado);
                }
                String tituloComunicado = req.getParameter("tituloComunicado");
                if (tituloComunicado != null)
                    tituloComunicado = new String(tituloComunicado.getBytes(), "UTF-8");
                if (tituloComunicado == null) tituloComunicado = "";
                ArrayList<String> correos = new ArrayList<String>();
                if (tipo != null && textComunicado != null && !textComunicado.isEmpty()){
                    if (tipo.equals("General"))
                        correos.addAll(JDBCDAOClub.mailsClubs());
                    else if (tipo.equals("Futbol8"))
                        correos.addAll(JDBCDAOClub.mailsClubs(Deporte.Futbol8));
                    else if (tipo.equals("Quiniela"))
                        correos.addAll(JDBCDAOClub.mailsClubs(Deporte.Quiniela));
                    else if (tipo.equals("Basket"))
                        correos.addAll(JDBCDAOClub.mailsClubs(Deporte.Basket));
                    
                    if (!correos.isEmpty())
                        Correo.getCorreo().enviarMailMasivo("ClubDeportivo Aviso " + tipo + " " + tituloComunicado, 
                                textComunicado, true, correos);
 
                }
            }
            
            ArrayList<Grupo> grps = JDBCDAOGrupo.obtenerGruposActivos();
            ArrayList<Club> clubs = JDBCDAOClub.listaClubs();
            ArrayList<CompeticionFutbol8> comps = JDBCDAOFutbol8.listaCompetiones();
            ArrayList<Faq> faqs = JDBCDAOFaq.obtenerFaqsNoContestadas();
            
            if (!faqs.isEmpty())
                req.setAttribute("faq", faqs.get(0));
            
            req.setAttribute("grupos", grps);
            req.setAttribute("clubs", clubs);
            req.setAttribute("competiciones", comps);
                

        } catch (Exception ex) {
            req.setAttribute("error", ex.getMessage());
        }
        
        ArrayList<JugadorFutbol8> jugsF8 = new ArrayList<JugadorFutbol8>();
        for (int i = 100; i < 1000; i = i + 100){
            JugadorFutbol8 jug = new JugadorFutbol8();
            jug.setValoracion(i);
            jugsF8.add(jug);
        }
        
        req.setAttribute("jugsF8", jugsF8);

        RequestDispatcher view =
                req.getRequestDispatcher("/PanelControl/panelControlAdmin.jsp");
        view.forward(req, resp);
       
       
    }

  


}

     

