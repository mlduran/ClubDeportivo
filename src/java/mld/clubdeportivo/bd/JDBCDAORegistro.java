
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

        RegistroDAO dao = new RegistroDAO();

        ArrayList<Registro> lista  = (ArrayList<Registro>) dao.getRegistros(num);

        return lista;

    }

    
    public static void grabarRegistro(Registro reg) throws DAOException {
        RegistroDAO dao = new RegistroDAO();
        dao.save(reg);
    }

 

}
