
package mld.clubdeportivo.controladores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mld.clubdeportivo.base.Club;
import mld.clubdeportivo.base.Grupo;
import mld.clubdeportivo.bd.DAOException;
import mld.clubdeportivo.bd.JDBCDAOClub;
import mld.clubdeportivo.bd.JDBCDAOGrupo;
import mld.clubdeportivo.utilidades.Calculos;
import mld.clubdeportivo.utilidades.Correo;
import mld.clubdeportivo.utilidades.UtilGenericas;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Miguel
 */
public class AltasHttpServlet extends HttpServlet {

    private static Logger logger = LogManager.getLogger(AltasHttpServlet.class);


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

        String action = req.getPathInfo();

        if (action.equalsIgnoreCase("/grupo")){

            altaGrupo(req, resp);
        }
        else if (action.equalsIgnoreCase("/club")){

            altaClub(req, resp);
        }
        
        else {
            resp.sendRedirect(req.getContextPath() + "/login");

        }

    }

    
    private void altaGrupo(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        try {
                String nombre = req.getParameter("nombre");
                String privado = req.getParameter("privado");

                Map<String, String> errors = new HashMap<String, String>();

                boolean iscumplimentado = true;
                if (nombre == null || privado == null)
                    iscumplimentado = false;
                else if(!UtilGenericas.isGrupo(nombre))
                    errors.put("nombre", "El código indicado no tiene formato válido");
                else if (JDBCDAOGrupo.existeGrupo(nombre))
                    errors.put("nombre", "Ya existe un grupo con ese Nombre");
                else if (JDBCDAOGrupo.isIPRegistrada(req.getRemoteAddr()))
                    errors.put("nombre", "En estos momentos no es posible dar de alta un grupo");

                if (iscumplimentado && errors.isEmpty()) {
                    Grupo grp = new Grupo(nombre, Boolean.parseBoolean(privado),
                            new ArrayList());
                    int cod = 0;
                    if (grp.isPrivado()){
                        cod = Calculos.valorAleatorio(10000, 99999);
                        grp.setCodigo(cod);
                    }
                    // En este momento no grabamos el grupo
                    // lo haremos cuando el usuario se de 
                    // de alta con un club
                    //JDBCDAOGrupo.grabarGrupo(grp);
                    grp.setIp(req.getRemoteAddr());

                    req.getSession().setAttribute("newgrupo", grp);
                }
                else {
                    req.setAttribute("errors", errors);                    
                }

            }
            catch (DAOException ex) {
                logger.error("Error al dar de alta grupo: ".concat(ex.getMessage()));
                req.setAttribute("error", ex.getMessage());
        }
        RequestDispatcher view = req.getRequestDispatcher("/Altas/altaGrupo.jsp");
        view.forward(req, resp);
        
    }

     private void altaClub(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

 
         try {

             ArrayList<Grupo> grps = (ArrayList) JDBCDAOGrupo.obtenerGruposDisponibles();
             ArrayList<Grupo> gruposPrivados = new ArrayList<Grupo>();
             ArrayList<Grupo> gruposPublicos = new ArrayList<Grupo>();
             Grupo newgrp = null;
             int codigo = 0;
             if (req.getSession().getAttribute("newgrupo") != null){
                 newgrp = (Grupo) req.getSession().getAttribute("newgrupo");
                 req.setAttribute("codigo", newgrp.getCodigo());
                 codigo = newgrp.getCodigo();
                 if (newgrp.isPrivado()) gruposPrivados.add(newgrp);
                 else gruposPublicos.add(newgrp);
             }

             for (Grupo grupo : grps) {
                 if (grupo.isPrivado()) gruposPrivados.add(grupo);
                 else gruposPublicos.add(grupo);
             }
             req.setAttribute("gruposprivados", gruposPrivados);
             req.setAttribute("grupospublicos", gruposPublicos);

             String nombre = req.getParameter("nombre");
             String usuario = req.getParameter("usuario");
             String password = req.getParameter("password");
             String password2 = req.getParameter("password2");
             String mail = req.getParameter("mail");
             String grpprivado = req.getParameter("grupoprivado");
             String grppublico = req.getParameter("grupopublico");
             String cod = req.getParameter("codigo");

             if (codigo == 0 && cod != null && cod.matches("^\\d+$"))
                 codigo = Integer.valueOf(cod);
             
             Grupo grp = null;
             if (newgrp != null)
                 grp = newgrp;
             else if (grpprivado != null && !grpprivado.equals(""))
                 grp = JDBCDAOGrupo.obtenerSimpleGrupo(Long.parseLong(grpprivado));
             else if (grppublico != null && !grppublico.equals(""))
                 grp = JDBCDAOGrupo.obtenerSimpleGrupo(Long.parseLong(grppublico));

             Map<String, String> errors = new HashMap<String, String>();

             boolean iscumplentado = true;
                if (nombre == null || usuario == null || password == null ||
                        password2 == null || mail == null){
                    iscumplentado = false;
                }
                else if (grpprivado != null && !grpprivado.equals("") && grppublico != null && !grppublico.equals("")){
                    errors.put("grupo", "Escoger solo un tipo de grupo");
                }
                else if (!UtilGenericas.isClub(nombre))
                    errors.put("nombre", "El Nombre del Club no tiene formato válido");
                else if (JDBCDAOClub.existeClub(nombre))
                    errors.put("nombre", "Ya existe un Club con ese Nombre");
                else if(!UtilGenericas.isUsername(usuario))
                    errors.put("usuario", "El Nombre de usuario no tiene formato válido");
                else if (JDBCDAOClub.existeUsuario(usuario))
                    errors.put("usuario", "Ya existe el codigo de usuario indicado");
                else if(!UtilGenericas.isPassword(password))
                    errors.put("password", "La contraseña no tiene formato válido");
                else if (!password.equals(password2))
                    errors.put("password", "Las 2 contraseñas introducidas no son iguales");
                else if (!UtilGenericas.isMail(mail))
                    errors.put("mail", "El mail no tiene formato válido");
                else if (grp == null)
                    errors.put("grupo", "Elegir un grupo");
                else if (grp.isPrivado() && grp.getCodigo() != codigo)
                    errors.put("codigo", "Has elegido un grupo privado y el codigo introducido es erroneo");

                if (iscumplentado && errors.isEmpty()) {
                    Club club = new Club(grp, nombre, usuario, password, mail);
                    if (newgrp != null) JDBCDAOGrupo.grabarGrupo(newgrp);
                    JDBCDAOClub.grabarClub(club);
                    
                    req.setAttribute("clubsave", club);
                    String contrasenyaGrupo = "";
                    if (grp.isPrivado())
                        contrasenyaGrupo = "Contraseña Grupo: " + club.getGrupo().getCodigo() + "<br/>";
                    
                    String txt = "Felicidades ya tienes tu club creado<br/><br/>" 
                            + "Grupo: " + club.getGrupo().getNombre() + "<br/>" 
                            + contrasenyaGrupo  
                            + "Nombre Club: " + club.getNombre() + "<br/>" 
                            + "Usuario: " + club.getUsuario() + "<br/>"  
                            + "Password: " + password + "<br/><br/>";
                    txt = txt + "En estos momentos tienes 2 secciones disponibles para darte de alta: Futbol8 y Quiniela, entra con tu usuario y password y date de alta en las que desees para poder participar en las próximas competiciones<br/><br/>";
                    txt = txt + "GRACIAS POR PARTICIPAR!!!";
                            
                    Correo.getCorreo().enviarMail("ClubDeportivo Alta de Club", 
                            txt, true, club.getMail());

                    RequestDispatcher view = req.getRequestDispatcher("/Altas/altaClub.jsp");
                    view.forward(req, resp);
                }
                else {
                    req.setAttribute("errors", errors);
                    RequestDispatcher view = req.getRequestDispatcher("/Altas/altaClub.jsp");
                    view.forward(req, resp);
                }
            }
            catch (DAOException ex) {
                logger.error("Error al dar de alta Club: ".concat(ex.getMessage()));
                req.setAttribute("error", ex.getMessage());
                RequestDispatcher view = req.getRequestDispatcher("/Altas/altaClub.jsp");
                view.forward(req, resp);
         }
    }
    

}
