
package mld.clubdeportivo.controladores;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import static java.lang.String.valueOf;
import static java.time.LocalDateTime.now;
import java.util.Date;
import java.util.List;
import mld.clubdeportivo.base.Club;
import mld.clubdeportivo.bd.DAOException;
import java.util.logging.*;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static mld.clubdeportivo.bd.JDBCDAOClub.grabarClub;
import static mld.clubdeportivo.bd.JDBCDAOClub.listaClubsRanking;
import static mld.clubdeportivo.bd.JDBCDAOClub.obtenerClubPorUsuario;
import static mld.clubdeportivo.bd.JDBCDAOClub.validarLogin;
import static mld.clubdeportivo.controladores.UtilesHttpServlet.registrarEntrada;
import static mld.clubdeportivo.controladores.UtilesHttpServlet.registrarLogin;
import static mld.clubdeportivo.utilidades.Calculos.valorAleatorio;
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
public class LoginHttpServlet {

    private static Logger logger = getLogger(LoginHttpServlet.class.getName());
    
    @Value("${custom.usuarioAdmin}")
    private String usuarioAdmin;
    @Value("${custom.passwordAdmin}")
    private String passwordAdmin;
   
    @GetMapping("/")
    public String doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            UtilesHttpServlet.registrarEntrada(req);
        } catch (DAOException ex) {
            logger.log(SEVERE, ex.getMessage());
        }
        return "login";
    }

    @PostMapping("/login")
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        var sesion = req.getSession();
        sesion.setAttribute("idClub", null);
        sesion.setAttribute("fechaServer", fechaServidor());

        try {

            var accion = req.getParameter("accion");
            var envioClave = req.getParameter("bSubmit");
            
            if (accion != null && accion.equals("envioPassword")){
                var view =
                        req.getRequestDispatcher("/Login/envioPassword.jsp");
                view.forward(req, resp);
                return;
            }
            else if(envioClave != null && envioClave.equals("Enviar Nuevo Password")){
                var usuarioEnvio = req.getParameter("usuarioEnvio");
                var mailEnvio = req.getParameter("mailEnvio");
                if (usuarioEnvio != null && mailEnvio != null){
                    var club = obtenerClubPorUsuario(usuarioEnvio);
                    if (club != null && club.getMail().equals(mailEnvio)){
                        var contr = valorAleatorio(100000, 999999);
                        club.setPassword(valueOf(contr));        
                        var txt = "El nuevo password para acceder a tu cuenta es " + valueOf(contr) + "<br/>";
                        getCorreo().enviarMail("Club Deportivo Nuevo Password", txt, true, club.getMail());
                        grabarClub(club);
                        req.setAttribute("resultado", "Se ha enviado la nueva contraseña a la dirección de correo indicada");
                    }else{
                        req.setAttribute("resultado", "No se ha encontrado ningun usuario y mail coincidente, no se realiza ninguna acción");
                    }
                }
                var view =
                        req.getRequestDispatcher("/Login/envioPassword.jsp");
                view.forward(req, resp);
                return;
                
            }
            List<Club> lista = listaClubsRanking(10, false);
            req.setAttribute("listaranking", lista);

            var usuario = req.getParameter("usuario");
            var password = req.getParameter("password");

            if (esAdmin(usuario, password)){
                sesion.setAttribute("idClub", valueOf(0));
                resp.sendRedirect(req.getContextPath() + "/admin");
            }
            else {

                var club = validarLogin(usuario, password);

                if (club != null) {
                    sesion.setAttribute("idClub", club.getId());
                    club.setUltimoAcceso(new Date());
                    grabarClub(club);
                    registrarLogin(req, club.getNombre());
                    resp.sendRedirect(req.getContextPath() + "/panelControl/presentacion");                    
                }
                else {
                    var appManager = req.getServletContext();
                    var txtBannerEntrada = appManager.getInitParameter("bannerEntrada");
                    req.setAttribute("txtBannerEntrada", txtBannerEntrada);
                    var txtJuegos = appManager.getInitParameter("juegos");
                    if (txtJuegos != null && txtJuegos.length() > 0){
                        var juegos = txtJuegos.split(",");
                        var juego = juegos[valorAleatorio(juegos.length)];
                        req.setAttribute("juego", juego);
                    }
                    if (usuario != null || password != null)
                        req.setAttribute("noValidado", true);
                    var view =
                            req.getRequestDispatcher("/Login/login.jsp");
                    view.forward(req, resp);
                }
            }
            
        } catch (DAOException ex) {
            logger.log(SEVERE, "Error acceso a BD: ".concat(ex.getMessage()));
            req.setAttribute("error", ex.getMessage());
        }
               
    }
    
    

    private boolean esAdmin(String usuario, String password) {

        var result = false;

        if (usuario != null && password != null){            

            result = usuario.equals(usuarioAdmin) && password.equals(passwordAdmin);
        }

        return result;
    }

    private String fechaServidor() {
        
        var locaDate = now();
        var txt = new StringBuilder();
        
        txt.append(locaDate.getDayOfMonth()).append('-');
        txt.append(locaDate.getMonthValue()).append('-');
        txt.append(locaDate.getYear()).append('-');
        
        txt.append(locaDate.getHour()).append('-');
        txt.append(locaDate.getMinute()).append('-');
        txt.append(locaDate.getSecond());        
        
        return txt.toString();
        
    }
    
    
    
}

     

