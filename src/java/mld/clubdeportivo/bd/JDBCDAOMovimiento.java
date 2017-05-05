
package mld.clubdeportivo.bd;

import java.util.*;
import mld.clubdeportivo.base.*;


/**
 *
 * @author mlopezd
 */
public class JDBCDAOMovimiento {

    private JDBCDAOMovimiento(){}

   
    public static ArrayList<Movimiento> obtenerMovimientos(Seccion seccion)
            throws DAOException{
        // Devuelve lista de movimientos

        MovimientoDAO dao = new MovimientoDAO();

        ArrayList<Movimiento> lista  =
                (ArrayList<Movimiento>) dao.getMovimientos(seccion);

        return lista;

    }
    
    public static ArrayList<Movimiento> obtenerMovimientosPorClase(Seccion seccion)
            throws DAOException{
        // Devuelve lista de movimientos

        MovimientoDAO dao = new MovimientoDAO();

        ArrayList<Movimiento> lista  =
                (ArrayList<Movimiento>) dao.getMovimientosByClase(seccion);

        return lista;

    }

    public static ArrayList<Movimiento> obtenerMovimientos(Seccion seccion,
            Date fechaIni, Date fechaFin)
            throws DAOException{
        // Devuelve lista de movimientos

        MovimientoDAO dao = new MovimientoDAO();

        ArrayList<Movimiento> lista  =
                (ArrayList<Movimiento>) dao.getMovimientos(seccion, fechaIni, fechaFin);

        return lista;

    }
    
    public static void eliminarMovimientos(Seccion seccion) throws DAOException{
        
        MovimientoDAO dao = new MovimientoDAO();
        ArrayList<Movimiento> movs = (ArrayList<Movimiento>) dao.getMovimientos(seccion);
        
        for (Movimiento movimiento : movs) {
            dao.delete(movimiento);
        }
        
    }

    
    public static void grabarRegistro(Movimiento reg) throws DAOException {

        MovimientoDAO dao = new MovimientoDAO();
        dao.save(reg);
    }

 

}
