package mld.clubdeportivo.bd;

import static java.lang.Class.forName;
import java.sql.Connection;
import java.sql.DriverManager;
import static java.sql.DriverManager.getConnection;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.logging.*;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static mld.clubdeportivo.bd.ConexionConfigDAO.getConexionConfigDAO;


/**
 *
 * @author java1
 */
public class ConexionDAO {
    
    private static Logger logApp
            = getLogger(ConexionDAO.class.getName());
    
    static public Connection getConexion(String schema) throws DAOException {
        
        var config = getConexionConfigDAO();
        
        if (config == null)
            throw new NoSuchElementException("No se ha inicializado la configuracion");
        
        Connection miConexion = null;
        
        try {
            
            // Si el usuario no existe cogemos el de el
            // schema
            var usuario = config.getUsuario();
            if (usuario == null || usuario.isEmpty() || usuario.equals(""))
                usuario = schema;
            
            //logApp.log(Level.INFO, "Usuario ".concat(usuario) );
            
            forName("com.mysql.cj.jdbc.Driver");
            
            var extrasCon = "?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            // esto es para codificar en utf8
            //"?useUnicode=true&characterEncoding=UTF-8",
            extrasCon = extrasCon + "&useUnicode=true&characterEncoding=UTF-8";
            miConexion = getConnection(
                    config.getUrl() + schema + extrasCon,
                    usuario,
                    config.getPassword());
            miConexion.setAutoCommit(false);
            
        } catch (SQLException ex) {
            logApp.log(SEVERE, "Error al obtner conexion: ".concat(ex.getMessage()));
            throw new DAOException(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            getLogger(ConexionDAO.class.getName()).log(SEVERE, null, ex);
        }
        
        return miConexion;
        
    }
    
}