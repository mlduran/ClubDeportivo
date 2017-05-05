
package mld.clubdeportivo.bd.futbol8;

/**
 *
 * @author mlopezd
 */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import mld.clubdeportivo.base.futbol8.*;
import mld.clubdeportivo.bd.*;


public class VacacionFutbol8DAO extends ObjetoDAO{
    
    protected String schema(){return "futbol8" + VacacionFutbol8DAO.getEntorno();}

    protected String nombreTabla() {
        return TablasDAO.vacacionesfutbol8.name();
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

        String txtsql = "SELECT * FROM " + nombreTabla() +
                " WHERE equipo = ? ";

        ArrayList params = new ArrayList();
        params.add(eq.getId());

        VacacionFutbol8 obj = (VacacionFutbol8) getDataObject(txtsql, params);

        return obj;
    }

       
    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        VacacionFutbol8 objVac = (VacacionFutbol8) obj;

        sql.setBoolean(1, objVac.isActivo());
        sql.setLong(2, objVac.getEquipo().getId());
        sql.setBoolean(3, objVac.isRenovacion());
        sql.setBoolean(4, objVac.isActivarTactica());                
        if (objVac.getTactica() != null)
            sql.setLong(5, objVac.getTactica().getNumero());
        else
            sql.setNull(5, java.sql.Types.NULL);
        sql.setBoolean(6, objVac.isActivarEntreno());
        sql.setInt(7, objVac.getPosicionEntreno().ordinal());
       
        // Si es un update asignamos el parametro del id
        if (tipo == TipoSaveDAO.update) sql.setLong(8, objVac.getId());

        return sql;
    }

    @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        VacacionFutbol8 obj = new VacacionFutbol8();

        obj.setId(retorno.getLong("id"));
        obj.setActivo(retorno.getBoolean("activo"));
        obj.setIdEq(retorno.getLong("equipo"));
        obj.setRenovacion(retorno.getBoolean("renovacion"));
        obj.setActivarTactica(retorno.getBoolean("tactica"));
        obj.setTactica(TacticaFutbol8.tacticaFutbol8(retorno.getInt("numerotactica")));
        obj.setActivarEntreno(retorno.getBoolean("entreno"));
        obj.setPosicionEntreno(PosicionElegidaFutbol8.values()[(retorno.getInt("posicionentreno"))]);

        return obj;
    }

 
   
}
