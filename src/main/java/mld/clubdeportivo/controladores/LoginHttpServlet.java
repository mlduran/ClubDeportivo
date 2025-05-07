package mld.clubdeportivo.controladores;

import jakarta.servlet.ServletException;
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
            req.setAttribute("idClub", null);
            req.setAttribute("fechaServer", fechaServidor());
            UtilesHttpServlet.registrarEntrada(req);
            obtenerDatos(req);
        } catch (DAOException ex) {
            logger.log(SEVERE, ex.getMessage());
        }
        return "login";
    }
    
    @GetMapping("/login")
    public String recuperarContrasenya(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        return processRequest(req, resp);
    }

    @PostMapping("/login")
    public String doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        return processRequest(req, resp);
    }

    private void obtenerDatos(HttpServletRequest req) throws DAOException {

        List<Club> lista = listaClubsRanking(10, false);
        req.setAttribute("listaranking", lista);

    }

    private String processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        var sesion = req.getSession();

        try {

            var accion = req.getParameter("accion");
            var envioClave = req.getParameter("bSubmit");

            if (accion != null && accion.equals("envioPassword")) {
                return "envioPassword";
            } else if (envioClave != null && envioClave.equals("Enviar Nuevo Password")) {
                var usuarioEnvio = req.getParameter("usuarioEnvio");
                var mailEnvio = req.getParameter("mailEnvio");
                if (usuarioEnvio != null && mailEnvio != null) {
                    var club = obtenerClubPorUsuario(usuarioEnvio);
                    if (club != null && club.getMail().equals(mailEnvio)) {
                        var contr = valorAleatorio(100000, 999999);
                        club.setPassword(valueOf(contr));
                        var txt = "El nuevo password para acceder a tu cuenta es " + valueOf(contr) + "<br/>";
                        getCorreo().enviarMail("Club Deportivo Nuevo Password", txt, true, club.getMail());
                        grabarClub(club);
                        req.setAttribute("resultado", "Se ha enviado la nueva contraseña a la dirección de correo indicada");
                    } else {
                        req.setAttribute("resultado", "No se ha encontrado ningun usuario y mail coincidente, no se realiza ninguna acción");
                    }
                }
                return "envioPassword";
            }

            var usuario = req.getParameter("usuario");
            var password = req.getParameter("password");

            if (esAdmin(usuario, password)) {
                sesion.setAttribute("idClub", valueOf(0));
                resp.sendRedirect(req.getContextPath() + "/admin");
            } else {

                var club = validarLogin(usuario, password);

                if (club != null) {
                    sesion.setAttribute("idClub", club.getId());
                    club.setUltimoAcceso(new Date());
                    grabarClub(club);
                    registrarLogin(req, club.getNombre());
                    resp.sendRedirect(req.getContextPath() + "/panelControl/presentacion");
                } else {
                    if (usuario != null || password != null) {
                        req.setAttribute("noValidado", true);
                    }
                    req.setAttribute("fechaServer", fechaServidor());
                    obtenerDatos(req);
                    return "login";
                }
            }

        } catch (DAOException ex) {
            logger.log(SEVERE, "Error acceso a BD: ".concat(ex.getMessage()));
            req.setAttribute("error", ex.getMessage());
        }

        return "login";

    }

    private boolean esAdmin(String usuario, String password) {

        var result = false;

        if (usuario != null && password != null) {

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
