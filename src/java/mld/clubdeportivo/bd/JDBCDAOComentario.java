
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

        ComentarioDAO dao = new ComentarioDAO();

        ArrayList<Comentario> lista  =
                (ArrayList<Comentario>) dao.getComentariosGenerales(num);

        return lista;

    }

    public static ArrayList<Comentario> obtenerComentarios(Grupo grp, int num)
            throws DAOException{
        // Devuelve lista de movimientos

        ComentarioDAO dao = new ComentarioDAO();

        ArrayList<Comentario> lista  =
                (ArrayList<Comentario>) dao.getComentarios(grp, num);

        return lista;

    }
    
    public static void eliminarComentarios(Grupo grp) throws DAOException{
        
        ComentarioDAO dao = new ComentarioDAO();
        ArrayList<Comentario> coments = (ArrayList<Comentario>) dao.getComentarios(grp);
        
        for (Comentario comentario : coments) {
            dao.delete(comentario);
        }
        
    }
            
   
    
    public static void grabarComentario(Comentario reg) throws DAOException {
        ComentarioDAO dao = new ComentarioDAO();
        dao.save(reg);
    }

    public static void eliminarComentarios(Club club) throws DAOException {
        ComentarioDAO dao = new ComentarioDAO();
        dao.eliminarComentarios(club);
    }
 

}
