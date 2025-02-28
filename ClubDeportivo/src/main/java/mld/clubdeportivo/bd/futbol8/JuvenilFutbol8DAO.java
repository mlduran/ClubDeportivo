/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mld.clubdeportivo.bd.futbol8;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import mld.clubdeportivo.base.futbol8.EquipoFutbol8;
import mld.clubdeportivo.base.futbol8.JuvenilFutbol8;
import mld.clubdeportivo.base.futbol8.PosicionJugFutbol8;
import static mld.clubdeportivo.base.futbol8.PosicionJugFutbol8.values;
import mld.clubdeportivo.bd.DAOException;
import mld.clubdeportivo.bd.ObjetoDAO;
import mld.clubdeportivo.bd.TablasDAO;
import static mld.clubdeportivo.bd.TablasDAO.juvenilesfutbol8;
import mld.clubdeportivo.bd.TipoSaveDAO;
import static mld.clubdeportivo.bd.TipoSaveDAO.update;

/**
 *
 * @author Miguel
 */
public class JuvenilFutbol8DAO extends ObjetoDAO{

    protected String schema(){return "futbol8" + getEntorno();}
    protected String nombreTabla(){return juvenilesfutbol8.name();}
    protected String[] camposTabla() {
        String[] campos = {
            "equipo",
            "nombre",
            "posicion",
            "valoracion",
            "jornadas"        
        };
        return campos;
        }
  

    @Override
    protected Object crearObjeto(ResultSet retorno) throws SQLException {

        if (retorno == null ) return null;

        var obj = new JuvenilFutbol8();
        
        obj.setId(retorno.getLong("id"));
        obj.setNombre(retorno.getString("nombre"));
        obj.setPosicion(values()[(retorno.getInt("posicion"))]);
        obj.setValoracion(retorno.getInt("valoracion"));
        obj.setJornadas(retorno.getInt("jornadas"));

        return obj;
    }

    @Override
    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        var objeto = (JuvenilFutbol8) obj;
        sql.setLong(1, objeto.getEquipo().getId());
        sql.setString(2, objeto.getNombre());
        sql.setInt(3, objeto.getPosicion().ordinal());
        sql.setInt(4, objeto.getValoracion());
        sql.setInt(5, objeto.getJornadas());
        
        // Si es un update asignamos el parametro del id
        if (tipo == update) sql.setLong(6, objeto.getId());

        return sql;

    }
    
    protected JuvenilFutbol8 getJuvenilByEquipo(EquipoFutbol8 eq) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE equipo = ?";
        var params = new ArrayList();
        params.add(eq.getId());

        var obj = (JuvenilFutbol8) getDataObject(txtsql, params);

        return obj;
    }
    
       

    
}
