
package mld.clubdeportivo.bd;

import java.util.*;
import mld.clubdeportivo.base.*;


/**
 *
 * @author mlopezd
 */
public class JDBCDAONoticia {

    private JDBCDAONoticia(){}

   
    public static ArrayList<Noticia> obtenerNoticias(Grupo grp, Deporte deporte, int num)
            throws DAOException{
        // Devuelve lista de movimientos

        var dao = new NoticiaDAO();
        var lista  =
                (ArrayList<Noticia>) dao.getNoticias(grp, deporte, num);

        return lista;

    }

   
    
    public static void grabarNoticia(Noticia reg) throws DAOException {
        var dao = new NoticiaDAO();
        dao.save(reg);
    }
    
    public static void eliminarNoticias(Grupo grp) throws DAOException{
        
        var dao = new NoticiaDAO();
        var noticias = (ArrayList<Noticia>) dao.getNoticias(grp);
        for (var noticia : noticias) {
            dao.delete(noticia);
        }
        
    }

 

}
