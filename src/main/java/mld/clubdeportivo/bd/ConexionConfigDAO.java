package mld.clubdeportivo.bd;

import java.sql.*;
import java.io.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Miguel
 */
public class ConexionConfigDAO {

    private String url;
    private String usuario;
    private String password;
    private String entorno;
    
  
    private static final ConexionConfigDAO INSTANCE = new ConexionConfigDAO();

    private static final Logger logApp
            = LoggerFactory.getLogger(ConexionConfigDAO.class.getName());

    static public ConexionConfigDAO getConexionConfigDAO() {

        return INSTANCE;
    }

    public void initConfigConexion(String configMysql)
            throws IOException, SQLException {

        logApp.info("Inicializando Parametros Conexion en initConexion");

        // Si por ejemplo tenemos la url es que ya hemos inicializado
        if (url != null) {
            logApp.warn("Los parametros de conexion ya se han inicializado");
            return;
        }

        // Obtenermos parametros
        var config = new Properties();

        logApp.info("Leyendo fichero configuracion de: ".concat(configMysql));
        config.load(new FileReader(configMysql));

        this.setUrl(config.getProperty("mysql.url"));
        this.setUsuario(config.getProperty("mysql.usuario"));
        this.setPassword(config.getProperty("mysql.password"));
        this.setEntorno(config.getProperty("mysql.entorno"));

        logApp.info("Parametros Conexion inicializados");
        logApp.info(config.toString());

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEntorno() {
        return entorno;
    }

    public void setEntorno(String entorno) {
        this.entorno = entorno;
    }


}
