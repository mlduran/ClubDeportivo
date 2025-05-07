
package mld.clubdeportivo.bd;

import java.util.*;
import mld.clubdeportivo.base.*;


/**
 *
 * @author mlopezd
 */
public class JDBCDAORegistro {

    private JDBCDAORegistro(){}

   
    public static ArrayList<Registro> obtenerRegistros(int num) throws DAOException{
        // Devuelve lista de clubs sin relaciones

        var dao = new RegistroDAO();
        var lista  = (ArrayList<Registro>) dao.getRegistros(num);

        return lista;

    }

    
    public static void grabarRegistro(Registro reg) throws DAOException {
        var dao = new RegistroDAO();
        dao.save(reg);
    }

 

}
