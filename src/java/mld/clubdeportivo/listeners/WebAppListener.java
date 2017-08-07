package mld.clubdeportivo.listeners;


import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.http.*;
import mld.clubdeportivo.bd.ConexionConfigDAO;
import mld.clubdeportivo.utilidades.Correo;
import org.apache.log4j.*;
import org.apache.log4j.xml.DOMConfigurator;

/**
 *
 * @author java1
 */

public class WebAppListener implements ServletContextListener, 
        HttpSessionListener{


    private static Logger logApp
            = LogManager.getLogger(WebAppListener.class);

    private void initConfigBD(String config) {

        try {
            ConexionConfigDAO configBD = ConexionConfigDAO.getConexionConfigDAO();
            configBD.initConfigConexion(config);
        } catch (IOException ex) {
            logApp.error("Error al crear configuracion conexion: ".concat(ex.getMessage()));
        } catch (SQLException ex) {
            logApp.error("Error al crear configuracion conexion: ".concat(ex.getMessage()));
        }

    }
    
    private void initConfigCorreo(String config) {

        
        try {
            Correo configCorreo = Correo.getCorreo();
            configCorreo.initCorreo(config);
        } catch (IOException ex) {
            logApp.error("Error al crear configuracion conexion: ".concat(ex.getMessage()));       
        }

    }

 
    public void contextInitialized(ServletContextEvent sce) {

        ServletContext appManager = sce.getServletContext();

        String fichConfLlog = appManager.getInitParameter("log4jcfg");
        DOMConfigurator.configure(appManager.getRealPath(fichConfLlog));
        
        String fichConf, config;

        logApp.info("Ejecutando contextInitialized en WebAppListener");
        fichConf = appManager.getInitParameter("configmysql");
        config = appManager.getRealPath(fichConf);
        initConfigBD(config);
        
        fichConf = appManager.getInitParameter("configcorreo");
        config = appManager.getRealPath(fichConf);
        initConfigCorreo(config);
        

    }

    public void contextDestroyed(ServletContextEvent sce) {
       

    }

    public void sessionCreated(HttpSessionEvent se) {


    }

    public void sessionDestroyed(HttpSessionEvent se) {

        
    }



}
