/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mld.clubdeportivo.bd.futbol8;

import java.sql.*;
import java.util.*;
import mld.clubdeportivo.base.futbol8.*;
import mld.clubdeportivo.bd.DAOException;
import mld.clubdeportivo.bd.ObjetoDAO;
import mld.clubdeportivo.bd.TablasDAO;
import mld.clubdeportivo.bd.TipoSaveDAO;

/**
 *
 * @author Miguel
 */
public class JugadorMaestroFutbol8DAO extends ObjetoDAO{

    protected String schema(){return "futbol8" + JugadorMaestroFutbol8DAO.getEntorno();}
    protected String nombreTabla(){return TablasDAO.jugadoresmaestrofutbol8.name();}
    protected String[] camposTabla() {
        String[] campos = {
            "nombre",
            "posicion"};
        return campos;
        }
  

    @Override
    protected Object crearObjeto(ResultSet retorno) throws SQLException {

        if (retorno == null ) return null;

        JugadorMaestroFutbol8 obj = new JugadorMaestroFutbol8(
                retorno.getString("nombre"),
                PosicionJugFutbol8.values()[(retorno.getInt("posicion"))]);
 
        obj.setId(retorno.getLong("id"));
       
        return obj;
    }

    @Override
    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        JugadorMaestroFutbol8 objeto = (JugadorMaestroFutbol8) obj;
        sql.setString(1, objeto.getNombre());
        sql.setInt(2, objeto.getPosicion().ordinal());

        // Si es un update asignamos el parametro del id
        if (tipo == TipoSaveDAO.update) sql.setLong(3, objeto.getId());

        return sql;

    }

    
    protected List<JugadorMaestroFutbol8> getJugadoresMaestros(
            PosicionJugFutbol8 pos) throws DAOException {

        String txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE posicion = ?";

        ArrayList params = new ArrayList();
        params.add(pos.ordinal());

        ArrayList<JugadorMaestroFutbol8> listaObjs = 
                (ArrayList) getDataObjects(txtsql, params);

        return listaObjs;
    }

    protected List<JugadorMaestroFutbol8> getJugadoresMaestros() throws DAOException {

        String txtsql = "SELECT * FROM " + nombreTabla();

        ArrayList<JugadorMaestroFutbol8> listaObjs = (ArrayList) getDataObjects(txtsql);

        return listaObjs;
    }

}
