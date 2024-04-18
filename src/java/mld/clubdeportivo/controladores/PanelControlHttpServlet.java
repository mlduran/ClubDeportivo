
package mld.clubdeportivo.controladores;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
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
import mld.clubdeportivo.base.futbol8.EquipoFutbol8;
import mld.clubdeportivo.base.quinielas.CompeticionQuiniela;
import mld.clubdeportivo.base.quinielas.EquipoQuiniela;
import mld.clubdeportivo.bd.DAOException;
import mld.clubdeportivo.bd.JDBCDAOClub;
import mld.clubdeportivo.bd.JDBCDAOFaq;
import mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8;
import mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela;
import mld.clubdeportivo.utilidades.Correo;
import mld.clubdeportivo.utilidades.Seguridad;
import mld.clubdeportivo.utilidades.UtilGenericas;
import java.util.logging.*;

/**
 *
 * @author Miguel
 */
public class PanelControlHttpServlet extends HttpServlet {

    private static Logger logger = 
            Logger.getLogger(PanelControlHttpServlet.class.getName());
   
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

        String accion = req.getPathInfo();
        ServletContext appManager = this.getServletContext();
        
        req.setAttribute("path", "/panelControl/presentacion");
        
        try {
            if (accion.equals("/fichaClub")){
                fichaClub(req);
            }else
            {
            
                if (!UtilesHttpServlet.comprobarEstado(req, resp)) return;        
                
                long id = (Long) req.getSession().getAttribute("idClub");
                
                Club club = JDBCDAOClub.obtenerSimpleClub(id);
                
                req.setAttribute("nombreGrupo", club.getGrupo().getNombre());
                for (String deporte : UtilesHttpServlet.deportesActivos(appManager)) {
                    req.setAttribute(deporte, true);
                }
                
                String root = req.getContextPath();
                
                if (accion.equals("/presentacion")){
                    presentacion(req, club);               
                    
                }
                else if (accion.equals("/inicio")){
                    
                    Deporte dep = deporte(req);
                    
                    if (dep == Deporte.Futbol8) {
                        
                        req.getSession().setAttribute("idEquipo",
                                JDBCDAOFutbol8.idEquipoFutbol8(club));
                        resp.sendRedirect(root + "/panelControl/Futbol8/inicio");
                        return;
                    }
                    else if (dep == Deporte.Quiniela) {
                        req.getSession().setAttribute("idEquipo",
                                JDBCDAOQuiniela.idEquipoQuiniela(club));
                        resp.sendRedirect(root + "/panelControl/Quiniela/inicio");
                        return;
                    }
                }
                
                else if (accion.equals("/datosUsuario")) {
                    datosUsuario(req, club);               
                }
                else if (accion.equals("/faqs")) {
                    faqs(req, club);               
                }
                else if (accion.equals("/altas")) {
                    altaDeporte(req, club);               
                }
                else if (accion.equals("/ranking")) {
                    ranking(req);               
                }
            }                
        } catch (Exception ex) {
            req.setAttribute("error", ex.getMessage());            
        }
        
        RequestDispatcher view =
                req.getRequestDispatcher("/PanelControl/panelControl.jsp");
        view.forward(req, resp);        
        

    }

    private HttpServletRequest presentacion(HttpServletRequest req, Club club)
            throws DAOException{

        req.setAttribute("op", "presentacion");
        
        req.getSession().setAttribute("deporte", null);  
        req.setAttribute("club", club);
        boolean posibles = club.getDeportes().size() != Deporte.values().length;
        req.setAttribute("posibles", posibles);
        obtenerListaClubs(req, club.getGrupo());
        
        UtilesHttpServlet.tratarComentarios(req, club, true);        

        return req;

    }

    private Deporte deporte(HttpServletRequest req)
            throws DAOException{

        Deporte dp  = null;
        String deporte = req.getParameter("deporte");

        if (deporte != null){
            if (deporte.equals(Deporte.Futbol8.name())){
                req.getSession().setAttribute("deporte", Deporte.Futbol8.name());
                dp = Deporte.Futbol8;
            }
            else if(deporte.equals(Deporte.Quiniela.name())){
                req.getSession().setAttribute("deporte", Deporte.Quiniela.name());
                dp = Deporte.Quiniela;
            }
        }
        
        return dp;

    }

  
    private void obtenerListaClubs(HttpServletRequest req,
            Grupo grupo) throws DAOException{

        ArrayList<Club> clubsGrupo = 
                (ArrayList<Club>) JDBCDAOClub.listaClubsGrupo(grupo);
        req.setAttribute("clubsGrupo", clubsGrupo);

    }

 
   
    private void fichaClub(HttpServletRequest req) throws DAOException {

        req.setAttribute("op", "fichaClub");

        long idClub = Long.valueOf(req.getParameter("id"));

        Club club = JDBCDAOClub.obtenerSimpleClub(idClub);
        int ligasFutbol8 = JDBCDAOFutbol8.numeroCompeticionesGanadas(club, "Liga");
        int copasFutbol8 = JDBCDAOFutbol8.numeroCompeticionesGanadas(club, "Copa");
        int copasQuiniela = JDBCDAOQuiniela.numeroCompeticionesGanadas(club);
    
        
        req.setAttribute("clubFicha", club);
        req.setAttribute("ligasFutbol8", ligasFutbol8);
        req.setAttribute("copasFutbol8", copasFutbol8);
        req.setAttribute("copasQuiniela", copasQuiniela);

        obtenerListaClubs(req, club.getGrupo());

    }

   

    private void datosUsuario(HttpServletRequest req, Club club) throws DAOException {

        req.setAttribute("op", "datosUsuario");
        req.setAttribute("club", club);
        
        String op = (String) req.getParameter("operacion");
        
        if (op == null) {
        }// no hacemos nada
        else if (op.equals("Cambiar password")) {
            
            String pact = req.getParameter("passwordact");
            if (pact == null) pact = "";
            String pactCod = Seguridad.SHA1Digest(pact);
            String pw = req.getParameter("password");
            String pw2 = req.getParameter("password2");

            if (!pactCod.equals(club.getPassword()))
                throw new IllegalArgumentException("Contraseña actual incorrecta");

            if (!pw.equals(pw2))
                throw new IllegalArgumentException("Las 2 nuevas contraseñas no coinciden");

            club.setPassword(pw);

            JDBCDAOClub.grabarClub(club);

            req.setAttribute("ok", true);
            
        }
        else if (op.equals("Cambiar correo")) {
            String mail = req.getParameter("mail");
            
            if (!UtilGenericas.isMail(mail))
                throw new IllegalArgumentException("El correo no tiene el formato correcto");
            
            club.setMail(mail);
            
            JDBCDAOClub.grabarClub(club);

            req.setAttribute("ok", true);
            
        }
        
        else if (op.equals("baja")) {
            String deporte = req.getParameter("seccion");
            
            if (deporte.equals("Futbol8")){
                EquipoFutbol8 eq = JDBCDAOFutbol8.obtenerSimpleEquipoFutbol8(club);
                if (JDBCDAOFutbol8.competicionActiva(club.getGrupo(), "Liga") != null){                    
                    eq.setActivo(false);
                    eq.setAutomatico(true);
                    JDBCDAOFutbol8.grabarEquipoFutbol8(eq);
                }
                else{
                    JDBCDAOFutbol8.eliminarEquipoFutbol8(eq);
                }
            }
            else if (deporte.equals("Quiniela")){
                EquipoQuiniela eq = JDBCDAOQuiniela.obtenerEquipo(club);
                JDBCDAOQuiniela.eliminarEquipoQuiniela(eq);
            }
           
            req.setAttribute("ok", true);
            
        }



    }
    
     private void faqs(HttpServletRequest req, Club club) throws DAOException, UnsupportedEncodingException {
         
         req.setAttribute("op", "faqs");
         
         String op = (String) req.getParameter("operacion");
         req.setAttribute("enviado", false);
         
         if (op == null) {
         }// no hacemos nada
         else if (op.equals("Enviar")) {
             
             String preg = req.getParameter("pregunta");
             if (preg != null && !preg.equals("")) {
                 //preg = new String(preg.getBytes(), "UTF-8");
                 Faq fq = new Faq(club, preg);
                 JDBCDAOFaq.grabarFaq(fq);
                 ServletContext appManager = req.getServletContext();
                 String dirCorreo = appManager.getInitParameter("mailcontacto");
                 Correo.getCorreo().enviarMail("ClubDeportivo Pregunta " + club.getNombre(),
                         preg, true, dirCorreo);                 
                 req.setAttribute("enviado", true);
             }
         }
         
         ArrayList<Faq> faqs = JDBCDAOFaq.obtenerFaqsContestadas();
         req.setAttribute("faqs", faqs);
        
    }

    private void altaDeporte(HttpServletRequest req, Club club) throws DAOException {
        
        presentacion(req, club);
        
        try {
            String dep = req.getParameter("deporte");
            Deporte deporte = null;
            
            if (dep.equals(Deporte.Futbol8.name()))
                deporte = Deporte.Futbol8;
            else if (dep.equals(Deporte.Futbol8.name()))
                deporte = Deporte.Futbol8;
            else if (dep.equals(Deporte.Quiniela.name()))
                deporte = Deporte.Quiniela;
            
            altaEquipo(club, deporte, false);
            club.getDeportes().add(deporte);
                
        } catch (Exception ex) {
            req.setAttribute("error","Error en alta de Seccion: ".concat(ex.getMessage()));
        }
                
    }
    
      public static void altaEquipo(Club club, Deporte deporte,
            boolean auto) throws DAOException{

        if (deporte == Deporte.Futbol8) 
            altaEquipoFutbol8(club, auto);
        else if (deporte == Deporte.Quiniela)
            altaEquipoQuiniela(club);

    }

     private static void altaEquipoQuiniela(Club club) throws DAOException {

         CompeticionQuiniela comp = JDBCDAOQuiniela.competicionActiva();
         
         if (comp != null && !JDBCDAOQuiniela.obtenerJornadasValidadas(comp).isEmpty()){
         
             boolean disputando = false;
             ArrayList<Club> clubs = JDBCDAOClub.listaClubsGrupo(club.getGrupo());
             for (Club unClub : clubs) 
                 if (!unClub.equals(club) && unClub.isQuiniela()) disputando = true;                  
         
             if (disputando)
                 throw new UnsupportedOperationException("Existe una competicion activa, no es posible dar de alta este Deporte si hay clubs de este grupo disputandola, Deberas esperar a que finalize de la actual temporada de la liga Española de Futbol");
         }
         
         EquipoQuiniela eq = new EquipoQuiniela();
         eq.setActivo(true);
         eq.setClub(club);

         JDBCDAOQuiniela.grabarEquipo(eq);
         eq.setClub(club);
         UtilesQuiniela.crearRegistrosNuevoEquipo(eq);

    }

    private static void altaEquipoFutbol8(Club club, boolean auto)
             throws DAOException{

        UtilesFutbol8.altaEquipoFutbol8(club, auto);

    }

    private void ranking(HttpServletRequest req) throws DAOException {
        
        req.setAttribute("op", "ranking");
        
        ArrayList<Club> clubs = JDBCDAOClub.listaClubsRanking(1000, true);
        
        req.setAttribute("clubs", clubs);
        
        
    }

   
  

}



     

