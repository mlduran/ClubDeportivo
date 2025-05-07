/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mld.clubdeportivo.bd.futbol8;

import java.sql.*;
import java.util.*;
import mld.clubdeportivo.base.futbol8.*;
import static mld.clubdeportivo.base.futbol8.PosicionJugFutbol8.values;
import mld.clubdeportivo.bd.DAOException;
import mld.clubdeportivo.bd.ObjetoDAO;
import mld.clubdeportivo.bd.TablasDAO;
import static mld.clubdeportivo.bd.TablasDAO.jugadoresmaestrofutbol8;
import mld.clubdeportivo.bd.TipoSaveDAO;
import static mld.clubdeportivo.bd.TipoSaveDAO.update;

/**
 *
 * @author Miguel
 */
public class JugadorMaestroFutbol8DAO extends ObjetoDAO{

    protected String schema(){return "futbol8" + getEntorno();}
    protected String nombreTabla(){return jugadoresmaestrofutbol8.name();}
    protected String[] camposTabla() {
        String[] campos = {
            "nombre",
            "posicion"};
        return campos;
        }
  

    @Override
    protected Object crearObjeto(ResultSet retorno) throws SQLException {

        if (retorno == null ) return null;

        var obj = new JugadorMaestroFutbol8(
                retorno.getString("nombre"),
                values()[(retorno.getInt("posicion"))]);
 
        obj.setId(retorno.getLong("id"));
       
        return obj;
    }

    @Override
    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        var objeto = (JugadorMaestroFutbol8) obj;
        sql.setString(1, objeto.getNombre());
        sql.setInt(2, objeto.getPosicion().ordinal());

        // Si es un update asignamos el parametro del id
        if (tipo == update) sql.setLong(3, objeto.getId());

        return sql;

    }

    
    protected List<JugadorMaestroFutbol8> getJugadoresMaestros(
            PosicionJugFutbol8 pos) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE posicion = ?";
        var params = new ArrayList();
        params.add(pos.ordinal());

        ArrayList<JugadorMaestroFutbol8> listaObjs = 
                (ArrayList) getDataObjects(txtsql, params);

        return listaObjs;
    }

    protected List<JugadorMaestroFutbol8> getJugadoresMaestros() throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla();

        ArrayList<JugadorMaestroFutbol8> listaObjs = (ArrayList) getDataObjects(txtsql);

        return listaObjs;
    }

}
