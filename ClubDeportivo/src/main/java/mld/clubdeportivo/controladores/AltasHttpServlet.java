package mld.clubdeportivo.controladores;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.valueOf;
import static java.lang.Long.parseLong;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import mld.clubdeportivo.base.Club;
import mld.clubdeportivo.base.Grupo;
import mld.clubdeportivo.bd.DAOException;
import java.util.logging.*;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static mld.clubdeportivo.bd.JDBCDAOClub.existeClub;
import static mld.clubdeportivo.bd.JDBCDAOClub.existeUsuario;
import static mld.clubdeportivo.bd.JDBCDAOClub.grabarClub;
import static mld.clubdeportivo.bd.JDBCDAOGrupo.existeGrupo;
import static mld.clubdeportivo.bd.JDBCDAOGrupo.grabarGrupo;
import static mld.clubdeportivo.bd.JDBCDAOGrupo.isIPRegistrada;
import static mld.clubdeportivo.bd.JDBCDAOGrupo.obtenerGruposDisponibles;
import static mld.clubdeportivo.bd.JDBCDAOGrupo.obtenerSimpleGrupo;
import static mld.clubdeportivo.utilidades.Calculos.valorAleatorio;
import static mld.clubdeportivo.utilidades.Correo.getCorreo;
import static mld.clubdeportivo.utilidades.UtilGenericas.isClub;
import static mld.clubdeportivo.utilidades.UtilGenericas.isGrupo;
import static mld.clubdeportivo.utilidades.UtilGenericas.isMail;
import static mld.clubdeportivo.utilidades.UtilGenericas.isPassword;
import static mld.clubdeportivo.utilidades.UtilGenericas.isUsername;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Miguel
 */
@Controller
public class AltasHttpServlet {

    private static Logger logger = getLogger(AltasHttpServlet.class.getName());

    @GetMapping("/alta/club")
    public String doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
                grupos(req);
            } catch (DAOException ex) {
                Logger.getLogger(AltasHttpServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        return "altaClub";
    }

    @PostMapping("/alta/club")
    public String doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        return processRequest(req, resp);
    }

    private String processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getRequestURI();
        var action = path.substring(path.lastIndexOf("/") + 1);

        if (action.contains("grupo")) {
            return altaGrupo(req, resp);
        } else if (action.contains("club")) {            
            return altaClub(req, resp);
        } else {
            return "redirect:/";

        }

    }

    private String altaGrupo(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        try {
            var nombre = req.getParameter("nombre");
            var privado = req.getParameter("privado");

            Map<String, String> errors = new HashMap<>();

            var iscumplimentado = true;
            if (nombre == null || privado == null) {
                iscumplimentado = false;
            } else if (!isGrupo(nombre)) {
                errors.put("nombre", "El código indicado no tiene formato válido");
            } else if (existeGrupo(nombre)) {
                errors.put("nombre", "Ya existe un grupo con ese Nombre");
            } else if (isIPRegistrada(req.getRemoteAddr())) {
                errors.put("nombre", "En estos momentos no es posible dar de alta un grupo");
            }

            if (iscumplimentado && errors.isEmpty()) {
                var grp = new Grupo(nombre, parseBoolean(privado),
                        new ArrayList());
                var cod = 0;
                if (grp.isPrivado()) {
                    cod = valorAleatorio(10000, 99999);
                    grp.setCodigo(cod);
                }
                // En este momento no grabamos el grupo
                // lo haremos cuando el usuario se de 
                // de alta con un club
                //JDBCDAOGrupo.grabarGrupo(grp);
                grp.setIp(req.getRemoteAddr());

                req.getSession().setAttribute("newgrupo", grp);
            } else {
                req.setAttribute("errors", errors);
            }

        } catch (DAOException ex) {
            logger.log(SEVERE, "Error al dar de alta grupo: ".concat(ex.getMessage()));
            req.setAttribute("error", ex.getMessage());
        }
        return "altaGrupo";

    }

    private void grupos(HttpServletRequest req) throws DAOException {

        ArrayList<Grupo> grps = (ArrayList) obtenerGruposDisponibles();
        var gruposPrivados = new ArrayList<>();
        var gruposPublicos = new ArrayList<>();
        Grupo newgrp = null;
        var codigo = 0;
        if (req.getSession().getAttribute("newgrupo") != null) {
            newgrp = (Grupo) req.getSession().getAttribute("newgrupo");
            req.setAttribute("codigo", newgrp.getCodigo());
            codigo = newgrp.getCodigo();
            if (newgrp.isPrivado()) {
                gruposPrivados.add(newgrp);
            } else {
                gruposPublicos.add(newgrp);
            }
        }

        for (var grupo : grps) {
            if (grupo.isPrivado()) {
                gruposPrivados.add(grupo);
            } else {
                gruposPublicos.add(grupo);
            }
        }
        req.setAttribute("gruposprivados", gruposPrivados);
        req.setAttribute("grupospublicos", gruposPublicos);

    }

    private String altaClub(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        try {

            ArrayList<Grupo> grps = (ArrayList) obtenerGruposDisponibles();
            var gruposPrivados = new ArrayList<>();
            var gruposPublicos = new ArrayList<>();
            Grupo newgrp = null;
            var codigo = 0;
            if (req.getSession().getAttribute("newgrupo") != null) {
                newgrp = (Grupo) req.getSession().getAttribute("newgrupo");
                req.setAttribute("codigo", newgrp.getCodigo());
                codigo = newgrp.getCodigo();
                if (newgrp.isPrivado()) {
                    gruposPrivados.add(newgrp);
                } else {
                    gruposPublicos.add(newgrp);
                }
            }

            for (var grupo : grps) {
                if (grupo.isPrivado()) {
                    gruposPrivados.add(grupo);
                } else {
                    gruposPublicos.add(grupo);
                }
            }
            req.setAttribute("gruposprivados", gruposPrivados);
            req.setAttribute("grupospublicos", gruposPublicos);

            var nombre = req.getParameter("nombre");
            var usuario = req.getParameter("usuario");
            var password = req.getParameter("password");
            var password2 = req.getParameter("password2");
            var mail = req.getParameter("mail");
            var grpprivado = req.getParameter("grupoprivado");
            var grppublico = req.getParameter("grupopublico");
            var cod = req.getParameter("codigo");

            if (codigo == 0 && cod != null && cod.matches("^\\d+$")) {
                codigo = valueOf(cod);
            }

            Grupo grp = null;
            if (newgrp != null) {
                grp = newgrp;
            } else if (grpprivado != null && !grpprivado.equals("")) {
                grp = obtenerSimpleGrupo(parseLong(grpprivado));
            } else if (grppublico != null && !grppublico.equals("")) {
                grp = obtenerSimpleGrupo(parseLong(grppublico));
            }

            Map<String, String> errors = new HashMap<>();

            var iscumplentado = true;
            if (nombre == null || usuario == null || password == null
                    || password2 == null || mail == null) {
                iscumplentado = false;
            } else if (grpprivado != null && !grpprivado.equals("") && grppublico != null && !grppublico.equals("")) {
                errors.put("grupo", "Escoger solo un tipo de grupo");
            } else if (!isClub(nombre)) {
                errors.put("nombre", "El Nombre del Club no tiene formato válido");
            } else if (existeClub(nombre)) {
                errors.put("nombre", "Ya existe un Club con ese Nombre");
            } else if (!isUsername(usuario)) {
                errors.put("usuario", "El Nombre de usuario no tiene formato válido");
            } else if (existeUsuario(usuario)) {
                errors.put("usuario", "Ya existe el codigo de usuario indicado");
            } else if (!isPassword(password)) {
                errors.put("password", "La contraseña no tiene formato válido");
            } else if (!password.equals(password2)) {
                errors.put("password", "Las 2 contraseñas introducidas no son iguales");
            } else if (!isMail(mail)) {
                errors.put("mail", "El mail no tiene formato válido");
            } else if (grp == null) {
                errors.put("grupo", "Elegir un grupo");
            } else if (grp.isPrivado() && grp.getCodigo() != codigo) {
                errors.put("codigo", "Has elegido un grupo privado y el codigo introducido es erroneo");
            }

            if (iscumplentado && errors.isEmpty()) {
                var club = new Club(grp, nombre, usuario, password, mail);
                if (newgrp != null) {
                    grabarGrupo(newgrp);
                }
                grabarClub(club);

                req.setAttribute("clubsave", club);
                var contrasenyaGrupo = "";
                if (grp.isPrivado()) {
                    contrasenyaGrupo = "Contraseña Grupo: " + club.getGrupo().getCodigo() + "<br/>";
                }

                var txt = "Felicidades ya tienes tu club creado<br/><br/>"
                        + "Grupo: " + club.getGrupo().getNombre() + "<br/>"
                        + contrasenyaGrupo
                        + "Nombre Club: " + club.getNombre() + "<br/>"
                        + "Usuario: " + club.getUsuario() + "<br/>"
                        + "Password: " + password + "<br/><br/>";
                txt = txt + "En estos momentos tienes 2 secciones disponibles para darte de alta: Futbol8 y Quiniela, entra con tu usuario y password y date de alta en las que desees para poder participar en las próximas competiciones<br/><br/>";
                txt = txt + "GRACIAS POR PARTICIPAR!!!";

                getCorreo().enviarMail("ClubDeportivo Alta de Club",
                        txt, true, club.getMail());

                return "altaClub";
            } else {
                req.setAttribute("errors", errors);
                return "altaClub";
            }
        } catch (DAOException ex) {
            logger.log(SEVERE, "Error al dar de alta Club: ".concat(ex.getMessage()));
            req.setAttribute("error", ex.getMessage());
            return "altaClub";
        }
    }

}
