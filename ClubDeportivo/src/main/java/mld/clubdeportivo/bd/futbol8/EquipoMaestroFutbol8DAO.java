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
import mld.clubdeportivo.base.EquipoMaestro;
import mld.clubdeportivo.bd.DAOException;
import mld.clubdeportivo.bd.ObjetoDAO;
import mld.clubdeportivo.bd.TablasDAO;
import static mld.clubdeportivo.bd.TablasDAO.equiposmaestrofutbol8;
import mld.clubdeportivo.bd.TipoSaveDAO;
import static mld.clubdeportivo.bd.TipoSaveDAO.update;

/**
 *
 * @author Miguel
 */
public class EquipoMaestroFutbol8DAO extends ObjetoDAO{

    protected String schema(){return "futbol8" + getEntorno();}
    protected String nombreTabla(){return equiposmaestrofutbol8.name();}
    protected String[] camposTabla() {
        String[] campos = {
            "nombre"};
        return campos;
        }
  

    @Override
    protected Object crearObjeto(ResultSet retorno) throws SQLException {

        if (retorno == null ) return null;

        var obj = new EquipoMaestro();
        obj.setNombre(retorno.getString("nombre"));
 
        obj.setId(retorno.getLong("id"));
       
        return obj;
    }

    @Override
    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        var objeto = (EquipoMaestro) obj;
        sql.setString(1, objeto.getNombre());

        // Si es un update asignamos el parametro del id
        if (tipo == update) sql.setLong(2, objeto.getId());

        return sql;

    }
   

    protected List<EquipoMaestro> getEquiposMaestros() throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla();

        ArrayList<EquipoMaestro> listaObjs = (ArrayList) getDataObjects(txtsql);

        return listaObjs;
    }

}
