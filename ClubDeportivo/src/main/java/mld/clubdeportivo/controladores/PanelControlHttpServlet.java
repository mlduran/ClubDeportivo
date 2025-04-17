package mld.clubdeportivo.controladores;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import static java.lang.Long.valueOf;
import java.util.ArrayList;
import mld.clubdeportivo.base.Club;
import mld.clubdeportivo.base.Deporte;
import mld.clubdeportivo.base.Faq;
import mld.clubdeportivo.base.Grupo;
import mld.clubdeportivo.base.quinielas.EquipoQuiniela;
import mld.clubdeportivo.bd.DAOException;
import java.util.logging.*;
import static java.util.logging.Logger.getLogger;
import static mld.clubdeportivo.base.Deporte.Futbol8;
import static mld.clubdeportivo.base.Deporte.Quiniela;
import static mld.clubdeportivo.base.Deporte.values;
import static mld.clubdeportivo.bd.JDBCDAOClub.grabarClub;
import static mld.clubdeportivo.bd.JDBCDAOClub.listaClubsGrupo;
import static mld.clubdeportivo.bd.JDBCDAOClub.listaClubsRanking;
import static mld.clubdeportivo.bd.JDBCDAOClub.obtenerSimpleClub;
import static mld.clubdeportivo.bd.JDBCDAOFaq.grabarFaq;
import static mld.clubdeportivo.bd.JDBCDAOFaq.obtenerFaqsContestadas;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.competicionActiva;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.eliminarEquipoFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.grabarEquipoFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.idEquipoFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.numeroCompeticionesGanadas;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.obtenerSimpleEquipoFutbol8;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.competicionActiva;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.eliminarEquipoQuiniela;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.grabarEquipo;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.idEquipoQuiniela;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.numeroCompeticionesGanadas;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerEquipo;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerJornadasValidadas;
import static mld.clubdeportivo.controladores.UtilesHttpServlet.comprobarEstado;
import static mld.clubdeportivo.controladores.UtilesHttpServlet.deportesActivos;
import static mld.clubdeportivo.controladores.UtilesHttpServlet.tratarComentarios;
import static mld.clubdeportivo.controladores.UtilesQuiniela.crearRegistrosNuevoEquipo;
import static mld.clubdeportivo.utilidades.Correo.getCorreo;
import static mld.clubdeportivo.utilidades.Seguridad.SHA1Digest;
import static mld.clubdeportivo.utilidades.UtilGenericas.isMail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Miguel
 */
@Controller
public class PanelControlHttpServlet {

    private static Logger logger
            = getLogger(PanelControlHttpServlet.class.getName());

    @Value("${custom.deportesactivos}")
    private String deportesactivos;

    @GetMapping({"/panelControl/presentacion", "/panelControl/datosUsuario", 
        "/panelControl/ranking", "/panelControl/fichaClub", "/panelControl/inicio"})
    public String doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        return processRequest(req, resp);
    }

    @PostMapping({"/panelControl/presentacion", "/panelControl/datosUsuario"})
    public String doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        return processRequest(req, resp);
    }

    private String processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        var path = req.getRequestURI();
        String accion = path.substring(path.lastIndexOf("/") + 1);

        req.setAttribute("path", "/panelControl/presentacion");

        try {
            if (accion.equals("fichaClub")) {
                fichaClub(req);
            } else {
                String estado = comprobarEstado(req, resp);
                if (!"".equals(estado)) {
                    return estado;
                }

                long id = (Long) req.getSession().getAttribute("idClub");

                var club = obtenerSimpleClub(id);

                req.setAttribute("nombreGrupo", club.getGrupo().getNombre());
                req.getSession().setAttribute("Futbol8", false);
                req.getSession().setAttribute("Basket", false);
                req.getSession().setAttribute("Quiniela", false);
                for (var deporte : deportesActivos(deportesactivos)) {
                    req.setAttribute(deporte, true);
                }
                var root = req.getContextPath();
                switch (accion) {
                    case "presentacion":
                        presentacion(req, club);
                        return "panelControl";
                    case "inicio":
                        var dep = deporte(req);
                        if (dep == Futbol8) {
                            req.getSession().setAttribute("idEquipo", idEquipoFutbol8(club));
                            return "redirect:/panelControl/Futbol8/inicio";
                        } else if (dep == Quiniela) {
                            req.getSession().setAttribute("idEquipo", idEquipoQuiniela(club));
                            return "redirect:/panelControl/Quiniela/inicio";
                        }
                        break;
                    case "datosUsuario":
                        datosUsuario(req, club);
                        break;
                    case "faqs":
                        faqs(req, club);
                        break;
                    case "altas":
                        altaDeporte(req, club);
                        break;
                    case "ranking":
                        ranking(req);
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception ex) {
            req.setAttribute("error", ex.getMessage());
        }

        return "panelControl";

    }

    private void presentacion(HttpServletRequest req, Club club)
            throws DAOException {

        req.setAttribute("op", "presentacion");

        req.getSession().setAttribute("deporte", null);
        req.setAttribute("club", club);
        var posibles = club.getDeportes().size() != values().length;
        req.setAttribute("posibles", posibles);
        obtenerListaClubs(req, club.getGrupo());

        tratarComentarios(req, club, true);

    }

    private Deporte deporte(HttpServletRequest req)
            throws DAOException {

        Deporte dp = null;
        var deporte = req.getParameter("deporte");

        if (deporte != null) {
            if (deporte.equals(Futbol8.name())) {
                req.getSession().setAttribute("deporte", Futbol8.name());
                dp = Futbol8;
            } else if (deporte.equals(Quiniela.name())) {
                req.getSession().setAttribute("deporte", Quiniela.name());
                dp = Quiniela;
            }
        }

        return dp;

    }

    private void obtenerListaClubs(HttpServletRequest req,
            Grupo grupo) throws DAOException {

        var clubsGrupo
                = (ArrayList<Club>) listaClubsGrupo(grupo);
        req.setAttribute("clubsGrupo", clubsGrupo);

    }

    private void fichaClub(HttpServletRequest req) throws DAOException {

        req.setAttribute("op", "fichaClub");

        long idClub = valueOf(req.getParameter("id"));

        var club = obtenerSimpleClub(idClub);
        var ligasFutbol8 = numeroCompeticionesGanadas(club, "Liga");
        var copasFutbol8 = numeroCompeticionesGanadas(club, "Copa");
        var copasQuiniela = numeroCompeticionesGanadas(club);

        req.setAttribute("clubFicha", club);
        req.setAttribute("ligasFutbol8", ligasFutbol8);
        req.setAttribute("copasFutbol8", copasFutbol8);
        req.setAttribute("copasQuiniela", copasQuiniela);

        obtenerListaClubs(req, club.getGrupo());

    }

    private void datosUsuario(HttpServletRequest req, Club club) throws DAOException {

        req.setAttribute("op", "datosUsuario");
        req.setAttribute("club", club);

        var op = (String) req.getParameter("operacion");
        if (null == op) {
        }// no hacemos nada
        else {
            switch (op) {
                case "Cambiar password":
                    var pact = req.getParameter("passwordact");
                    if (pact == null) {
                        pact = "";
                    }
                    var pactCod = SHA1Digest(pact);
                    var pw = req.getParameter("password");
                    var pw2 = req.getParameter("password2");
                    if (!pactCod.equals(club.getPassword())) {
                        throw new IllegalArgumentException("Contraseña actual incorrecta");
                    }
                    if (!pw.equals(pw2)) {
                        throw new IllegalArgumentException("Las 2 nuevas contraseñas no coinciden");
                    }
                    club.setPassword(pw);
                    grabarClub(club);
                    req.setAttribute("ok", true);
                    break;
                case "Cambiar correo":
                    var mail = req.getParameter("mail");
                    if (!isMail(mail)) {
                        throw new IllegalArgumentException("El correo no tiene el formato correcto");
                    }
                    club.setMail(mail);
                    grabarClub(club);
                    req.setAttribute("ok", true);
                    break;
                case "baja":
                    var deporte = req.getParameter("seccion");
                    if (deporte.equals("Futbol8")) {
                        var eq = obtenerSimpleEquipoFutbol8(club);
                        if (competicionActiva(club.getGrupo(), "Liga") != null) {
                            eq.setActivo(false);
                            eq.setAutomatico(true);
                            grabarEquipoFutbol8(eq);
                        } else {
                            eliminarEquipoFutbol8(eq);
                        }
                    } else if (deporte.equals("Quiniela")) {
                        var eq = obtenerEquipo(club);
                        eliminarEquipoQuiniela(eq);
                    }
                    req.setAttribute("ok", true);
                    break;
                default:
                    break;
            }
        }

    }

    private void faqs(HttpServletRequest req, Club club) throws DAOException, UnsupportedEncodingException {

        req.setAttribute("op", "faqs");

        var op = (String) req.getParameter("operacion");
        req.setAttribute("enviado", false);

        if (op == null) {
        }// no hacemos nada
        else if (op.equals("Enviar")) {

            var preg = req.getParameter("pregunta");
            if (preg != null && !preg.equals("")) {
                //preg = new String(preg.getBytes(), "UTF-8");
                var fq = new Faq(club, preg);
                grabarFaq(fq);
                var appManager = req.getServletContext();
                var dirCorreo = appManager.getInitParameter("mailcontacto");
                getCorreo().enviarMail("ClubDeportivo Pregunta " + club.getNombre(),
                        preg, true, dirCorreo);
                req.setAttribute("enviado", true);
            }
        }

        var faqs = obtenerFaqsContestadas();
        req.setAttribute("faqs", faqs);

    }

    private void altaDeporte(HttpServletRequest req, Club club) throws DAOException {

        presentacion(req, club);

        try {
            var dep = req.getParameter("deporte");
            Deporte deporte = null;

            if (dep.equals(Futbol8.name())) {
                deporte = Futbol8;
            } else if (dep.equals(Futbol8.name())) {
                deporte = Futbol8;
            } else if (dep.equals(Quiniela.name())) {
                deporte = Quiniela;
            }

            altaEquipo(club, deporte, false);
            club.getDeportes().add(deporte);

        } catch (Exception ex) {
            req.setAttribute("error", "Error en alta de Seccion: ".concat(ex.getMessage()));
        }

    }

    public static void altaEquipo(Club club, Deporte deporte,
            boolean auto) throws DAOException {

        if (deporte == Futbol8) {
            altaEquipoFutbol8(club, auto);
        } else if (deporte == Quiniela) {
            altaEquipoQuiniela(club);
        }

    }

    private static void altaEquipoQuiniela(Club club) throws DAOException {

        var comp = competicionActiva();

        if (comp != null && !obtenerJornadasValidadas(comp).isEmpty()) {

            var disputando = false;
            var clubs = listaClubsGrupo(club.getGrupo());
            for (var unClub : clubs) {
                if (!unClub.equals(club) && unClub.isQuiniela()) {
                    disputando = true;
                }
            }

            if (disputando) {
                throw new UnsupportedOperationException("Existe una competicion activa, no es posible dar de alta este Deporte si hay clubs de este grupo disputandola, Deberas esperar a que finalize de la actual temporada de la liga Española de Futbol");
            }
        }

        var eq = new EquipoQuiniela();
        eq.setActivo(true);
        eq.setClub(club);

        grabarEquipo(eq);
        eq.setClub(club);
        crearRegistrosNuevoEquipo(eq);

    }

    private static void altaEquipoFutbol8(Club club, boolean auto)
            throws DAOException {

        UtilesFutbol8.altaEquipoFutbol8(club, auto);

    }

    private void ranking(HttpServletRequest req) throws DAOException {

        req.setAttribute("op", "ranking");

        var clubs = listaClubsRanking(1000, true);

        req.setAttribute("clubs", clubs);

    }

}
