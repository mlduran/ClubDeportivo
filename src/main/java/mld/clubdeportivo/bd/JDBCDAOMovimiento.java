
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

        var dao = new MovimientoDAO();
        var lista  =
                (ArrayList<Movimiento>) dao.getMovimientos(seccion);

        return lista;

    }
    
    public static ArrayList<Movimiento> obtenerMovimientosPorClase(Seccion seccion)
            throws DAOException{
        // Devuelve lista de movimientos

        var dao = new MovimientoDAO();
        var lista  =
                (ArrayList<Movimiento>) dao.getMovimientosByClase(seccion);

        return lista;

    }

    public static ArrayList<Movimiento> obtenerMovimientos(Seccion seccion,
            Date fechaIni, Date fechaFin)
            throws DAOException{
        // Devuelve lista de movimientos

        var dao = new MovimientoDAO();
        var lista  =
                (ArrayList<Movimiento>) dao.getMovimientos(seccion, fechaIni, fechaFin);

        return lista;

    }
    
    public static void eliminarMovimientos(Seccion seccion) throws DAOException{
        
        var dao = new MovimientoDAO();
        var movs = (ArrayList<Movimiento>) dao.getMovimientos(seccion);
        for (var movimiento : movs) {
            dao.delete(movimiento);
        }
        
    }

    
    public static void grabarRegistro(Movimiento reg) throws DAOException {

        var dao = new MovimientoDAO();
        dao.save(reg);
    }

 

}
