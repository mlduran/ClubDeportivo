package mld.clubdeportivo.bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import mld.clubdeportivo.base.ClaseMovimiento;
import static mld.clubdeportivo.base.ClaseMovimiento.values;
import mld.clubdeportivo.base.Movimiento;
import mld.clubdeportivo.base.Seccion;
import static mld.clubdeportivo.bd.TablasDAO.movimientos;
import static mld.clubdeportivo.bd.TipoSaveDAO.update;


public class MovimientoDAO extends ObjetoDAO {

    protected String schema(){return "clubdeportivo" + getEntorno();}
    protected String nombreTabla() {
        return movimientos.name();
    }
    protected String[] camposTabla() {
        String[] campos = {
            "fecha",
            "equipo",
            "clase",
            "valor",
            "descripcion"};
        return campos;
        }

    protected Movimiento getMovimientoById(long id) throws DAOException {

       return (Movimiento) getObjetoById(id);
    }


    protected List<Movimiento> getMovimientos(Seccion seccion) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE equipo = ? "
                + "ORDER BY fecha DESC";
        var params = new ArrayList();
        params.add(seccion.getId());

        ArrayList<Movimiento> listaObjs = (ArrayList) getDataObjects(txtsql, params);

        return listaObjs;
    }
    
     protected List<Movimiento> getMovimientosByClase(Seccion seccion) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE equipo = ? "
                + "ORDER BY clase, fecha DESC";
        var params = new ArrayList();
        params.add(seccion.getId());

        ArrayList<Movimiento> listaObjs = (ArrayList) getDataObjects(txtsql, params);

        return listaObjs;
    }

    protected List<Movimiento> getMovimientos(Seccion seccion,
            Date fechaIni, Date fechaFin) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE equipo = ?" +
                " AND fecha >= ? AND fecha <= ? ORDER BY fecha DESC";
        var params = new ArrayList();
        params.add(seccion.getId());
        params.add(fechaIni);
        params.add(fechaFin);

        ArrayList<Movimiento> listaObjs = (ArrayList) getDataObjects(txtsql, params);

        return listaObjs;
    }


    @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        var obj = new Movimiento();

        obj.setId(retorno.getLong("id"));
        obj.setFecha(retorno.getTimestamp("fecha"));
        obj.setClase(values()[(retorno.getInt("clase"))]);
        obj.setValor(retorno.getInt("valor"));
        obj.setDescripcion(retorno.getString("descripcion"));

        return obj;
    }

    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        var objMov = (Movimiento) obj;

        sql.setTimestamp(1,
                new Timestamp(objMov.getFecha().getTime()));
        sql.setLong(2, objMov.getEquipo().getId());
        sql.setInt(3, objMov.getClase().ordinal());
        sql.setInt(4, objMov.getValor());
        sql.setString(5, objMov.getDescripcion());
       
        // Si es un update asignamos el parametro del id
        if (tipo == update) sql.setLong(6, objMov.getId());

        return sql;
    }



}