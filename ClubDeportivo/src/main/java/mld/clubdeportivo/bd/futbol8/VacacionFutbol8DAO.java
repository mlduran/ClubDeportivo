
package mld.clubdeportivo.bd.futbol8;

/**
 *
 * @author mlopezd
 */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static java.sql.Types.NULL;
import java.util.ArrayList;
import java.util.HashMap;
import mld.clubdeportivo.base.futbol8.*;
import static mld.clubdeportivo.base.futbol8.PosicionElegidaFutbol8.values;
import static mld.clubdeportivo.base.futbol8.TacticaFutbol8.tacticaFutbol8;
import mld.clubdeportivo.bd.*;
import static mld.clubdeportivo.bd.TablasDAO.vacacionesfutbol8;
import static mld.clubdeportivo.bd.TipoSaveDAO.update;


public class VacacionFutbol8DAO extends ObjetoDAO{
    
    protected String schema(){return "futbol8" + getEntorno();}

    protected String nombreTabla() {
        return vacacionesfutbol8.name();
    }
    protected String[] camposTabla() {
        String[] campos = {
            "activo",
            "equipo",
            "renovacion",
            "tactica",
            "numerotactica",
            "entreno",
            "posicionentreno"           
        };
        return campos;
        }

    
   
    protected VacacionFutbol8 getByEquipo(EquipoFutbol8 eq) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() +
                " WHERE equipo = ? ";
        var params = new ArrayList();
        params.add(eq.getId());

        var obj = (VacacionFutbol8) getDataObject(txtsql, params);

        return obj;
    }

       
    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        var objVac = (VacacionFutbol8) obj;

        sql.setBoolean(1, objVac.isActivo());
        sql.setLong(2, objVac.getEquipo().getId());
        sql.setBoolean(3, objVac.isRenovacion());
        sql.setBoolean(4, objVac.isActivarTactica());                
        if (objVac.getTactica() != null)
            sql.setLong(5, objVac.getTactica().getNumero());
        else
            sql.setNull(5, NULL);
        sql.setBoolean(6, objVac.isActivarEntreno());
        sql.setInt(7, objVac.getPosicionEntreno().ordinal());
       
        // Si es un update asignamos el parametro del id
        if (tipo == update) sql.setLong(8, objVac.getId());

        return sql;
    }

    @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        var obj = new VacacionFutbol8();

        obj.setId(retorno.getLong("id"));
        obj.setActivo(retorno.getBoolean("activo"));
        obj.setIdEq(retorno.getLong("equipo"));
        obj.setRenovacion(retorno.getBoolean("renovacion"));
        obj.setActivarTactica(retorno.getBoolean("tactica"));
        obj.setTactica(tacticaFutbol8(retorno.getInt("numerotactica")));
        obj.setActivarEntreno(retorno.getBoolean("entreno"));
        obj.setPosicionEntreno(values()[(retorno.getInt("posicionentreno"))]);

        return obj;
    }

 
   
}
