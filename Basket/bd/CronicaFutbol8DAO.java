
package mld.clubdeportivo.bd.futbol8;

/**
 *
 * @author mlopezd
 */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mld.clubdeportivo.base.futbol8.CronicaFutbol8;
import mld.clubdeportivo.base.futbol8.EstadisticaPartidoFutbol8;
import mld.clubdeportivo.base.futbol8.PartidoFutbol8;
import mld.clubdeportivo.bd.DAOException;
import mld.clubdeportivo.bd.ObjetoDAO;
import mld.clubdeportivo.bd.TablasDAO;
import mld.clubdeportivo.bd.TipoSaveDAO;
import mld.clubdeportivo.utilidades.StringUtil;

public class CronicaFutbol8DAO extends ObjetoDAO{

    protected String schema(){return "futbol8" + CronicaFutbol8DAO.getEntorno();}
    protected String nombreTabla() {
        return TablasDAO.cronicasfutbol8.name();
    }
    protected String[] camposTabla() {
        String[] campos = {            
            "partido",
            "accion",
            "cuadrante",
            "minuto"
        };
        return campos;
        }

    protected ArrayList<CronicaFutbol8> getCronicaByPartido(
            PartidoFutbol8 partido) throws DAOException {

        String txtsql = "SELECT * FROM " + nombreTabla() +
                " WHERE partido = ?";

        ArrayList params = new ArrayList();
        params.add(partido.getId());       

        ArrayList<CronicaFutbol8> objs =
                (ArrayList) getDataObjects(txtsql, params);

        return objs;
    }
    
    protected ArrayList<CronicaFutbol8> getCronicaByPartido(
            long idPartido) throws DAOException {

        String txtsql = "SELECT * FROM " + nombreTabla() +
                " WHERE partido = " + idPartido;

        ArrayList<CronicaFutbol8> objs =
                (ArrayList) getDataObjects(txtsql);

        return objs;
    }

    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        CronicaFutbol8 objest = (CronicaFutbol8) obj;

        sql.setLong(1, objest.getPartido().getId());
        sql.setString(2, StringUtil.truncate(objest.getAccion(), 250));
        sql.setString(3, StringUtil.truncate(objest.getCuadrante(), 3));              
        sql.setInt(4, objest.getMinuto());
     
        // Si es un update asignamos el parametro del id
        if (tipo == TipoSaveDAO.update) sql.setLong(5, objest.getId());

        return sql;
    }

    @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        CronicaFutbol8 obj = new CronicaFutbol8();

        obj.setId(retorno.getLong("id"));
        obj.setIdPartido(retorno.getLong("partido"));
        obj.setAccion(retorno.getString("accion"));
        obj.setCuadrante(retorno.getString("cuadrante"));               
        obj.setMinuto(retorno.getInt("minuto"));        

        return obj;
    }
 
}
