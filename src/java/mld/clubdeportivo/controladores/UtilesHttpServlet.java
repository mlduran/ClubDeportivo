
package mld.clubdeportivo.controladores;

import java.io.FileReader;
import java.util.ArrayList;
import javax.servlet.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import mld.clubdeportivo.base.*;
import mld.clubdeportivo.bd.DAOException;
import mld.clubdeportivo.bd.JDBCDAOComentario;
import mld.clubdeportivo.bd.JDBCDAORegistro;
import java.util.logging.*;
import mld.clubdeportivo.utilidades.Correo;

/**
 *
 * @author Miguel
 */
public class UtilesHttpServlet extends HttpServlet {

    private static Logger logger = Logger.getLogger(LoginHttpServlet.class.getName());
    
   
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
        
         
        boolean ok = true;
        
        ServletContext appManager = req.getServletContext();
        HttpSession sesion = req.getSession();
        
        if (appManager.getAttribute("simulando") != null && 
                appManager.getAttribute("simulando").equals("true")){
            RequestDispatcher view =
                            req.getRequestDispatcher("/Utiles/simulando.jsp");
            view.forward(req, resp);
            ok = false;
        }
        else if (appManager.getAttribute("mantenimiento") != null && 
                appManager.getAttribute("mantenimiento").equals("true")){
            RequestDispatcher view =
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
        
         
        boolean ok = true;
        
        HttpSession sesion = req.getSession();
        
        if (sesion.getAttribute("idClub") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            ok = false;
        }

        return ok;

    }
    
     protected static ArrayList<String> deportesActivos(ServletContext appManager){

        ArrayList<String> lista = new ArrayList<String>();        
        String confDeportes = appManager.getInitParameter("deportesactivos");
        lista.addAll(Arrays.asList(confDeportes.split(",")));
        
        return lista;

    }
     
     protected static boolean isDeporteActivo(ServletContext appManager, Deporte deporte){

         boolean isActivo = false;
         ArrayList<String> lista = deportesActivos(appManager);        
         for (String dep : lista) {
             if (deporte.name().equals(dep)){
                 isActivo = true;
                 break;
             }
         }
        
         return isActivo;

    }
       

    protected static void registrarEntrada(HttpServletRequest req)
            throws ServletException, IOException, DAOException{

        String ip = req.getRemoteAddr();
        Registro reg = new Registro(ip);
        JDBCDAORegistro.grabarRegistro(reg);


    }

    protected static void registrarLogin(HttpServletRequest req, String usuario)
            throws ServletException, IOException, DAOException{

        String ip = req.getRemoteAddr();
        Registro reg = new Registro();
        reg.setAplicacion("");
        reg.setFecha(new Date());
        reg.setIp(ip);
        reg.setTipo(TipoRegistro.Login);
        reg.setUsuario(usuario);
        reg.setObservaciones("");
        JDBCDAORegistro.grabarRegistro(reg);


    }
    
    protected static void tratarComentarios(HttpServletRequest req, Club club, 
            boolean general, int numRegs) throws DAOException{
    
        String coment = req.getParameter("comentario");
        

        if (coment != null && !coment.isEmpty()){
            //try {
                //coment = new String(coment.getBytes(), "UTF-8");
                Comentario newComent = new Comentario(club.getGrupo(), club.getNombre(), coment, general);
                JDBCDAOComentario.grabarComentario(newComent);
            //} catch (UnsupportedEncodingException ex) {
             //   java.util.logging.Logger.getLogger(UtilesHttpServlet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            //}
        }
        
        ArrayList<Comentario> coments;
        if (general)
            coments = JDBCDAOComentario.obtenerComentariosGenerales(numRegs);
        else 
            coments = JDBCDAOComentario.obtenerComentarios(club.getGrupo(), numRegs);
        
        req.setAttribute("comentarios", coments);
        
    }
    
    protected static void tratarComentarios(HttpServletRequest req, Club club, 
            boolean general) throws DAOException{
        
        tratarComentarios(req, club, general, 10);
        
    }
    
    public static void ejecutaBackup(ServletContext appManager, String tipo) {
        
        // tipo es Backup o Restore
        
        Runtime ap = Runtime.getRuntime();
        String sistemaOperativo;
        String rutaBackup;
        String usuario;
        String password;
        String fichConfParam = appManager.getInitParameter("configmysql");
        String fichConfig = appManager.getRealPath(fichConfParam);
        Properties config = new Properties();

        try {
            config.load(new FileReader(fichConfig));
            sistemaOperativo = config.getProperty("mysql.so");
            if (sistemaOperativo == null) sistemaOperativo = "windows";            
            rutaBackup = config.getProperty("mysql.backup");
            usuario = config.getProperty("mysql.usuario");
            password = config.getProperty("mysql.password");    
            String unidad = "";
            String ruta = "";
            
            if (sistemaOperativo.equals("windows")){
                if (rutaBackup != null){
                    String[] comp = rutaBackup.split(":");
                    if (comp.length == 2){
                        unidad = comp[0] + ":";
                        ruta = comp[1];
                    }                    
                }                
            
                if (tipo != null && rutaBackup != null && usuario != null && password != null){
                    Process proc = ap.exec("cmd.exe /c start " + rutaBackup + "/" + tipo + 
                            "ClubDeportivo.bat " + usuario + " " + password +
                            " " + unidad + " " + ruta);                       
                    try {
                        proc.waitFor();
                    } catch (InterruptedException ex) {
                        logger.log(Level.SEVERE, "Error en Interrupcion Backup: ".concat(ex.getMessage()));
                    }
                    if (proc.exitValue() != 0) 
                        logger.log(Level.SEVERE, "Error en Backup, el procedimiento ha devuelto error: " + proc.exitValue()); 
                }
            }
            if (sistemaOperativo.equals("linux")){
                
            }
            
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error en Backup: ".concat(ex.getMessage()));
        }


    }
    
     public static void pruebaCorreo(ServletContext appManager, String correo){
    
      Correo.getCorreo().enviarMail("Test correo ClubDeportivo", 
                             "Prueba Correo", true, correo);
  
     }

}

     

