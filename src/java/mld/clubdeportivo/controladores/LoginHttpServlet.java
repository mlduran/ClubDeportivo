
package mld.clubdeportivo.controladores;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mld.clubdeportivo.base.Club;
import mld.clubdeportivo.bd.DAOException;
import mld.clubdeportivo.bd.JDBCDAOClub;
import mld.clubdeportivo.utilidades.Calculos;
import mld.clubdeportivo.utilidades.Correo;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Miguel
 */
public class LoginHttpServlet extends HttpServlet {

    private static Logger logger = LogManager.getLogger(LoginHttpServlet.class);
   
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            UtilesHttpServlet.registrarEntrada(req);
        } catch (DAOException ex) {
            logger.error(ex.getMessage());
        }
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession sesion = req.getSession();
        sesion.setAttribute("idClub", null);
        sesion.setAttribute("fechaServer", fechaServidor());

        try {

            String accion = req.getParameter("accion");
            String envioClave = req.getParameter("bSubmit");
            
            if (accion != null && accion.equals("envioPassword")){
                RequestDispatcher view =
                        req.getRequestDispatcher("/Login/envioPassword.jsp");
                view.forward(req, resp);
                return;
            }
            else if(envioClave != null && envioClave.equals("Enviar Nuevo Password")){
                String usuarioEnvio = req.getParameter("usuarioEnvio");
                String mailEnvio = req.getParameter("mailEnvio");
                if (usuarioEnvio != null && mailEnvio != null){
                    Club club = JDBCDAOClub.obtenerClubPorUsuario(usuarioEnvio);
                    if (club != null && club.getMail().equals(mailEnvio)){
                        int contr = Calculos.valorAleatorio(100000, 999999);
                        club.setPassword(String.valueOf(contr));        
                        String txt = "El nuevo password para acceder a tu cuenta es " + String.valueOf(contr) + "<br/>";
                        Correo.getCorreo().enviarMail("Club Deportivo Nuevo Password", txt, true, club.getMail());
                        JDBCDAOClub.grabarClub(club);
                        req.setAttribute("resultado", "Se ha enviado la nueva contraseña a la dirección de correo indicada");
                    }else{
                        req.setAttribute("resultado", "No se ha encontrado ningun usuario y mail coincidente, no se realiza ninguna acción");
                    }
                }
                RequestDispatcher view =
                        req.getRequestDispatcher("/Login/envioPassword.jsp");
                view.forward(req, resp);
                return;
                
            }
            List<Club> lista = JDBCDAOClub.listaClubsRanking(10, false);
            req.setAttribute("listaranking", lista);

            String usuario = req.getParameter("usuario");
            String password = req.getParameter("password");

            if (esAdmin(usuario, password)){
                sesion.setAttribute("idClub", Long.valueOf(0));
                resp.sendRedirect(req.getContextPath() + "/admin");
            }
            else {

                Club club = JDBCDAOClub.validarLogin(usuario, password);

                if (club != null) {
                    sesion.setAttribute("idClub", club.getId());
                    club.setUltimoAcceso(new Date());
                    JDBCDAOClub.grabarClub(club);
                    UtilesHttpServlet.registrarLogin(req, club.getNombre());
                    resp.sendRedirect(req.getContextPath() + "/panelControl/presentacion");                    
                }
                else {
                    ServletContext appManager = req.getServletContext();
                    String txtBannerEntrada = appManager.getInitParameter("bannerEntrada");
                    req.setAttribute("txtBannerEntrada", txtBannerEntrada);
                    String txtJuegos = appManager.getInitParameter("juegos");
                    if (txtJuegos != null && txtJuegos.length() > 0){
                        String juegos[] = txtJuegos.split(",");
                        String juego = juegos[Calculos.valorAleatorio(juegos.length)];
                        req.setAttribute("juego", juego);
                    }
                    if (usuario != null || password != null)
                        req.setAttribute("noValidado", true);
                    RequestDispatcher view =
                            req.getRequestDispatcher("/Login/login.jsp");
                    view.forward(req, resp);
                }
            }
            
        } catch (DAOException ex) {
            logger.error("Error acceso a BD: ".concat(ex.getMessage()));
            req.setAttribute("error", ex.getMessage());
        }
               
    }
    
    

    private boolean esAdmin(String usuario, String password) {

        boolean result = false;

        if (usuario != null && password != null){
            String usuarioAdmin = this.getInitParameter("usuarioAdmin");
            String passwordAdmin = this.getInitParameter("passwordAdmin");

            result = usuario.equals(usuarioAdmin) && password.equals(passwordAdmin);
        }

        return result;
    }

    private String fechaServidor() {
        
        Calendar calendario = new GregorianCalendar();
        calendario.setTime(new Date());
        
        StringBuilder txt = new StringBuilder();
        
        txt.append(calendario.get(Calendar.DAY_OF_MONTH)).append('-');
        txt.append(calendario.get(Calendar.MONTH)).append('-');
        txt.append(calendario.get(Calendar.YEAR)).append('-');
        
        txt.append(calendario.get(Calendar.HOUR_OF_DAY)).append('-');
        txt.append(calendario.get(Calendar.MINUTE)).append('-');
        txt.append(calendario.get(Calendar.SECOND));        
        
        return txt.toString();
        
    }
    
    
    
}

     

