
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

        NoticiaDAO dao = new NoticiaDAO();

        ArrayList<Noticia> lista  =
                (ArrayList<Noticia>) dao.getNoticias(grp, deporte, num);

        return lista;

    }

   
    
    public static void grabarNoticia(Noticia reg) throws DAOException {
        NoticiaDAO dao = new NoticiaDAO();
        dao.save(reg);
    }
    
    public static void eliminarNoticias(Grupo grp) throws DAOException{
        
        NoticiaDAO dao = new NoticiaDAO();
        ArrayList<Noticia> noticias = (ArrayList<Noticia>) dao.getNoticias(grp);
        
        for (Noticia noticia : noticias) {
            dao.delete(noticia);
        }
        
    }

 

}
