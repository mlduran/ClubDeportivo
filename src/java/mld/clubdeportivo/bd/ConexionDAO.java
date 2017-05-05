package mld.clubdeportivo.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


/**
 *
 * @author java1
 */
public class ConexionDAO {
    
    private static Logger logApp
            = LogManager.getLogger(ConexionDAO.class);    

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
            
            miConexion = DriverManager.getConnection(
                    config.getUrl() + schema, 
                    // esto es para codificar en utf8
                    //"?useUnicode=true&characterEncoding=UTF-8",
                    usuario,
                    config.getPassword());
             miConexion.setAutoCommit(false);

        } catch (SQLException ex) {
            logApp.error("Error al obtner conexion: ".concat(ex.getMessage()));
            throw new DAOException(ex.getMessage());
        }

        return miConexion;

    }

}