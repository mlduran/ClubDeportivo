
package mld.clubdeportivo.bd;

import java.util.*;
import mld.clubdeportivo.base.*;


/**
 *
 * @author mlopezd
 */
public class JDBCDAOComentario {

    private JDBCDAOComentario(){}

   
    public static ArrayList<Comentario> obtenerComentariosGenerales(int num)
            throws DAOException{
        // Devuelve lista de movimientos

        var dao = new ComentarioDAO();
        var lista  =
                (ArrayList<Comentario>) dao.getComentariosGenerales(num);

        return lista;

    }

    public static ArrayList<Comentario> obtenerComentarios(Grupo grp, int num)
            throws DAOException{
        // Devuelve lista de movimientos

        var dao = new ComentarioDAO();
        var lista  =
                (ArrayList<Comentario>) dao.getComentarios(grp, num);

        return lista;

    }
    
    public static void eliminarComentarios(Grupo grp) throws DAOException{
        
        var dao = new ComentarioDAO();
        var coments = (ArrayList<Comentario>) dao.getComentarios(grp);
        for (var comentario : coments) {
            dao.delete(comentario);
        }
        
    }
            
   
    
    public static void grabarComentario(Comentario reg) throws DAOException {
        var dao = new ComentarioDAO();
        dao.save(reg);
    }

    public static void eliminarComentarios(Club club) throws DAOException {
        var dao = new ComentarioDAO();
        dao.eliminarComentarios(club);
    }
 

}
