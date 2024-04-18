package mld.clubdeportivo.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.logging.*;


/**
 *
 * @author java1
 */
public class ConexionDAO {
    
    private static Logger logApp
            = Logger.getLogger(ConexionDAO.class.getName());
    
    static public Connection getConexion(String schema) throws DAOException {
        
        ConexionConfigDAO config = ConexionConfigDAO.getConexionConfigDAO();
        
        if (config == null)
            throw new NoSuchElementException("No se ha inicializado la configuracion");
        
        Connection miConexion = null;
        
        try {
            
            // Si el usuario no existe cogemos el de el
            // schema
            String usuario = config.getUsuario();
            if (usuario == null || usuario.isEmpty() || usuario.equals(""))
                usuario = schema;
            
            //logApp.log(Level.INFO, "Usuario ".concat(usuario) );
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            String extrasCon = "?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            // esto es para codificar en utf8
            //"?useUnicode=true&characterEncoding=UTF-8",
            extrasCon = extrasCon + "&useUnicode=true&characterEncoding=UTF-8";
            miConexion = DriverManager.getConnection(
                    config.getUrl() + schema + extrasCon,
                    usuario,
                    config.getPassword());
            miConexion.setAutoCommit(false);
            
        } catch (SQLException ex) {
            logApp.log(Level.SEVERE, "Error al obtner conexion: ".concat(ex.getMessage()));
            throw new DAOException(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return miConexion;
        
    }
    
}