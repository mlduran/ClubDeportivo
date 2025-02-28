
package mld.clubdeportivo.bd;

import java.util.ArrayList;
import java.util.Date;
import mld.clubdeportivo.base.Bolsa;


/**
 *
 * @author mlopezd
 */
public class JDBCDAOBolsa {

    
    private JDBCDAOBolsa(){}

   
    public static ArrayList<Bolsa> obtenerDatosBolsa() throws DAOException{
        // Devuelve todos los registros

        var dao = new BolsaDAO();
        var lista = (ArrayList<Bolsa>) dao.getRegistros();

        return lista;

    }
    
    public static ArrayList<Bolsa> obtenerDatosBolsa(Date fecha) throws DAOException {
        
        var dao = new BolsaDAO();
        var lista = (ArrayList<Bolsa>) dao.getRegistros(fecha);

        return lista;
        
    }

    
    public static void grabarBolsa(Bolsa reg) throws DAOException {
        var dao = new BolsaDAO();
        dao.save(reg);
    }

 

}
