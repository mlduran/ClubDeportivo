package mld.clubdeportivo.listeners;


import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import java.io.IOException;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static mld.clubdeportivo.bd.ConexionConfigDAO.getConexionConfigDAO;
import static mld.clubdeportivo.utilidades.Correo.getCorreo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author java1
 */

@Component
public class WebAppListener implements ServletContextListener, 
        HttpSessionListener{

    @Value("${custom.configmysql}")
    private String configmysql;
    
    @Value("${custom.configcorreo}")
    private String configcorreo;
    
    private static final Logger logApp
            = LoggerFactory.getLogger(WebAppListener.class.getName());

    private void initConfigBD(String config) {

        try {
            var configBD = getConexionConfigDAO();
            configBD.initConfigConexion(config);
        } catch (IOException | SQLException ex) {
            logApp.error("Error al crear configuracion conexion: ".concat(ex.getMessage()));
        }

    }
    
    private void initConfigCorreo(String config) {

        
        try {
            var configCorreo = getCorreo();
            configCorreo.initCorreo(config);
        } catch (IOException ex) {
            logApp.error("Error al crear configuracion conexion: ".concat(ex.getMessage()));       
        }

    }

 
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        var appManager = sce.getServletContext();
        //var fichConfLlog = appManager.getInitParameter("log4jcfg");
        //DOMConfigurator.configure(appManager.getRealPath(fichConfLlog));
        
        String fichConf, config;

        logApp.info("Ejecutando contextInitialized en WebAppListener");
        config = appManager.getRealPath(configmysql);
        initConfigBD(config);
        
        fichConf = appManager.getInitParameter("configcorreo");
        config = appManager.getRealPath(configcorreo);
        initConfigCorreo(config);
        

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
       

    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {


    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

        
    }



}
