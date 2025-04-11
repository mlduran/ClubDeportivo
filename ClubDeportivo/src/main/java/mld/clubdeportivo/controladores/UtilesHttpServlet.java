
package mld.clubdeportivo.controladores;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.IOException;
import static java.lang.Runtime.getRuntime;
import static java.util.Arrays.asList;
import java.util.Date;
import java.util.Properties;
import mld.clubdeportivo.base.*;
import mld.clubdeportivo.bd.DAOException;
import java.util.logging.*;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static mld.clubdeportivo.base.TipoRegistro.Login;
import static mld.clubdeportivo.bd.JDBCDAOComentario.grabarComentario;
import static mld.clubdeportivo.bd.JDBCDAOComentario.obtenerComentarios;
import static mld.clubdeportivo.bd.JDBCDAOComentario.obtenerComentariosGenerales;
import static mld.clubdeportivo.bd.JDBCDAORegistro.grabarRegistro;
import static mld.clubdeportivo.utilidades.Correo.getCorreo;

/**
 *
 * @author Miguel
 */
public class UtilesHttpServlet extends HttpServlet {

    private static Logger logger = getLogger(LoginHttpServlet.class.getName());
     
   
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
       
       
       
    }

    protected static boolean comprobarEstado(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException{
        
         
        var ok = true;
        var appManager = req.getServletContext();
        var sesion = req.getSession();
        
        if (appManager.getAttribute("simulando") != null && 
                appManager.getAttribute("simulando").equals("true")){
            var view =
                            req.getRequestDispatcher("/Utiles/simulando.jsp");
            view.forward(req, resp);
            ok = false;
        }
        else if (appManager.getAttribute("mantenimiento") != null && 
                appManager.getAttribute("mantenimiento").equals("true")){
            var view =
                            req.getRequestDispatcher("/Utiles/mantenimiento.jsp");
            view.forward(req, resp);
            ok = false;
        }
        else if (sesion.getAttribute("idClub") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            ok = false;
        }

        return ok;

    }
    
    protected static boolean comprobarEstadoAdmin(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException{
        
         
        var ok = true;
        var sesion = req.getSession();
        
        if (sesion.getAttribute("idClub") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            ok = false;
        }

        return ok;

    }
    
     protected static ArrayList<String> deportesActivos(String deportesactivos){

        var lista = new ArrayList<String>();
        var confDeportes = deportesactivos;
        lista.addAll(asList(confDeportes.split(",")));
        
        return lista;

    }
     
     protected static boolean isDeporteActivo(String deportesactivos, Deporte deporte){

         var isActivo = false;
         var lista = deportesActivos(deportesactivos);
         for (var dep : lista) {
             if (deporte.name().equals(dep)){
                 isActivo = true;
                 break;
             }
         }
        
         return isActivo;

    }
       

    protected static void registrarEntrada(HttpServletRequest req)
            throws ServletException, IOException, DAOException{

        var ip = req.getRemoteAddr();
        var reg = new Registro(ip);
        grabarRegistro(reg);


    }

    protected static void registrarLogin(HttpServletRequest req, String usuario)
            throws ServletException, IOException, DAOException{

        var ip = req.getRemoteAddr();
        var reg = new Registro();
        reg.setAplicacion("");
        reg.setFecha(new Date());
        reg.setIp(ip);
        reg.setTipo(Login);
        reg.setUsuario(usuario);
        reg.setObservaciones("");
        grabarRegistro(reg);


    }
    
    protected static void tratarComentarios(HttpServletRequest req, Club club, 
            boolean general, int numRegs) throws DAOException{
    
        var coment = req.getParameter("comentario");
        

        if (coment != null && !coment.isEmpty()){
            //try {
                //coment = new String(coment.getBytes(), "UTF-8");
                var newComent = new Comentario(club.getGrupo(), club.getNombre(), coment, general);
                grabarComentario(newComent);
            //} catch (UnsupportedEncodingException ex) {
             //   java.util.logging.Logger.getLogger(UtilesHttpServlet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            //}
        }
        
        ArrayList<Comentario> coments;
        if (general)
            coments = obtenerComentariosGenerales(numRegs);
        else 
            coments = obtenerComentarios(club.getGrupo(), numRegs);
        
        req.setAttribute("comentarios", coments);
        
    }
    
    protected static void tratarComentarios(HttpServletRequest req, Club club, 
            boolean general) throws DAOException{
        
        tratarComentarios(req, club, general, 10);
        
    }
    
    public static void ejecutaBackup(ServletContext appManager, String tipo) {
        
        // tipo es Backup o Restore
        
        var ap = getRuntime();
        String sistemaOperativo;
        String rutaBackup;
        String usuario;
        String password;
        var fichConfParam = appManager.getInitParameter("configmysql");
        var fichConfig = appManager.getRealPath(fichConfParam);
        var config = new Properties();

        try {
            config.load(new FileReader(fichConfig));
            sistemaOperativo = config.getProperty("mysql.so");
            if (sistemaOperativo == null) sistemaOperativo = "windows";            
            rutaBackup = config.getProperty("mysql.backup");
            usuario = config.getProperty("mysql.usuario");
            password = config.getProperty("mysql.password");    
            var unidad = "";
            var ruta = "";
            
            if (sistemaOperativo.equals("windows")){
                if (rutaBackup != null){
                    var comp = rutaBackup.split(":");
                    if (comp.length == 2){
                        unidad = comp[0] + ":";
                        ruta = comp[1];
                    }                    
                }                
            
                if (tipo != null && rutaBackup != null && usuario != null && password != null){
                    var proc = ap.exec("cmd.exe /c start " + rutaBackup + "/" + tipo + 
                            "ClubDeportivo.bat " + usuario + " " + password +
                            " " + unidad + " " + ruta);                       
                    try {
                        proc.waitFor();
                    } catch (InterruptedException ex) {
                        logger.log(SEVERE, "Error en Interrupcion Backup: ".concat(ex.getMessage()));
                    }
                    if (proc.exitValue() != 0) 
                        logger.log(SEVERE, "Error en Backup, el procedimiento ha devuelto error: " + proc.exitValue()); 
                }
            }
            if (sistemaOperativo.equals("linux")){
                
            }
            
        } catch (IOException ex) {
            logger.log(SEVERE, "Error en Backup: ".concat(ex.getMessage()));
        }


    }
    
     public static void pruebaCorreo(ServletContext appManager, String correo){
    
        getCorreo().enviarMail("Test correo ClubDeportivo", 
                             "Prueba Correo", true, correo);
  
     }

}

     

