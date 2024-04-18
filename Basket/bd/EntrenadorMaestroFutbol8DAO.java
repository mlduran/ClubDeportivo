/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mld.clubdeportivo.bd.futbol8;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mld.clubdeportivo.base.futbol8.EntrenadorMaestroFutbol8;
import mld.clubdeportivo.bd.DAOException;
import mld.clubdeportivo.bd.ObjetoDAO;
import mld.clubdeportivo.bd.TablasDAO;
import mld.clubdeportivo.bd.TipoSaveDAO;

/**
 *
 * @author Miguel
 */
public class EntrenadorMaestroFutbol8DAO extends ObjetoDAO{

    protected String schema(){return "futbol8" + EntrenadorMaestroFutbol8DAO.getEntorno();}
    protected String nombreTabla(){return TablasDAO.entrenadoresmaestrofutbol8.name();}
    protected String[] camposTabla() {
        String[] campos = {
            "nombre"};
        return campos;
        }
  

    @Override
    protected Object crearObjeto(ResultSet retorno) throws SQLException {

        if (retorno == null ) return null;

        EntrenadorMaestroFutbol8 obj = new EntrenadorMaestroFutbol8(
                retorno.getString("nombre"));
 
        obj.setId(retorno.getLong("id"));
       
        return obj;
    }

    @Override
    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        EntrenadorMaestroFutbol8 objeto = (EntrenadorMaestroFutbol8) obj;
        sql.setString(1, objeto.getNombre());

        // Si es un update asignamos el parametro del id
        if (tipo == TipoSaveDAO.update) sql.setLong(2, objeto.getId());

        return sql;

    }
   

    protected List<EntrenadorMaestroFutbol8> getEntrenadoresMaestros() throws DAOException {

        String txtsql = "SELECT * FROM " + nombreTabla();

        ArrayList<EntrenadorMaestroFutbol8> listaObjs = (ArrayList) getDataObjects(txtsql);

        return listaObjs;
    }

}
