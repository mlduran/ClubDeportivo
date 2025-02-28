
package mld.clubdeportivo.bd.futbol8;

/**
 *
 * @author mlopezd
 */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import mld.clubdeportivo.base.futbol8.CompeticionFutbol8;
import mld.clubdeportivo.base.futbol8.GoleadorFutbol8;
import mld.clubdeportivo.base.futbol8.PorteroFutbol8;
import mld.clubdeportivo.base.futbol8.PosicionJugFutbol8;
import mld.clubdeportivo.bd.DAOException;
import mld.clubdeportivo.bd.ObjetoDAO;
import mld.clubdeportivo.bd.TablasDAO;
import static mld.clubdeportivo.bd.TablasDAO.porterosfutbol8;
import mld.clubdeportivo.bd.TipoSaveDAO;
import static mld.clubdeportivo.bd.TipoSaveDAO.update;


public class PorteroFutbol8DAO extends ObjetoDAO{
    
    protected String schema(){return "futbol8" + getEntorno();}

    protected String nombreTabla() {
        return porterosfutbol8.name();
    }
    protected String[] camposTabla() {
        String[] campos = {
            "competicion",
            "nombre",
            "golesliga",
            "golescopa",
            "partidos",
            "equipo"
        };
        return campos;
        }

    
   
    protected ArrayList<PorteroFutbol8> getByCompeticion(CompeticionFutbol8 comp) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() +
                " WHERE competicion = ? ";
        var params = new ArrayList();
        params.add(comp.getId());

        ArrayList<PorteroFutbol8> objs = (ArrayList) getDataObjects(txtsql, params);

        return objs;
    }

       
    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        var objGol = (PorteroFutbol8) obj;

        sql.setLong(1, objGol.getCompeticion().getId());
        sql.setString(2, objGol.getNombre());   
        sql.setInt(3, objGol.getGolesLiga());
        sql.setInt(4, objGol.getGolesCopa());
        sql.setInt(5, objGol.getPartidos());
        sql.setString(6, objGol.getEquipo());
       
        // Si es un update asignamos el parametro del id
        if (tipo == update) sql.setLong(7, objGol.getId());

        return sql;
    }

    @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        var obj = new PorteroFutbol8();

        obj.setId(retorno.getLong("id"));
        obj.setNombre(retorno.getString("nombre"));
        obj.setEquipo(retorno.getString("equipo"));
        obj.setPartidos(retorno.getInt("partidos"));
        obj.setGolesLiga(retorno.getInt("golesliga"));
        obj.setGolesCopa(retorno.getInt("golescopa"));

        return obj;
    }

 
   
}
