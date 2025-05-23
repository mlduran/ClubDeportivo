
package mld.clubdeportivo.bd.futbol8;

/**
 *
 * @author mlopezd
 */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static java.sql.Types.BIGINT;
import java.util.ArrayList;
import mld.clubdeportivo.base.Grupo;
import mld.clubdeportivo.base.futbol8.ConfigEconomiaFutbol8;
import mld.clubdeportivo.bd.DAOException;
import mld.clubdeportivo.bd.ObjetoDAO;
import mld.clubdeportivo.bd.TablasDAO;
import static mld.clubdeportivo.bd.TablasDAO.configeconomiasfutbol8;
import mld.clubdeportivo.bd.TipoSaveDAO;
import static mld.clubdeportivo.bd.TipoSaveDAO.update;

public class ConfigEconomiaFutbol8DAO extends ObjetoDAO{

    protected String schema(){return "futbol8" + getEntorno();}
    protected String nombreTabla() {
        return configeconomiasfutbol8.name();
    }
    protected String[] camposTabla() {
        String[] campos = {            
            "grupo",
            "equipogestor",
            "diasgestion",
            "modificado",
            "interescredito",
            "retencionhaciendabase",
            "retencionlineal",
            "iva",
            "subidamaxbolsa",
            "crackbolsa",
            "porcentajepremioliga",
            "porcentajecampeoncopa",
            "ibi"
        };
        return campos;
        }

    protected ConfigEconomiaFutbol8 getConfigByGrupo(
            Grupo grp) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() +
                " WHERE grupo = ?";
        var params = new ArrayList();
        params.add(grp.getId());       

        var objs =
                 (ConfigEconomiaFutbol8) getDataObject(txtsql, params);

        return objs;
    }
    
 
    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        var objconf = (ConfigEconomiaFutbol8) obj;

        sql.setLong(1, objconf.getGrupo().getId());
        if (objconf.getEquipoGestor() != null)
            sql.setLong(2, objconf.getEquipoGestor().getId());
        else 
            sql.setNull(2, BIGINT);
        sql.setInt(3, objconf.getDiasGestion());              
        sql.setBoolean(4, objconf.isModificado());
        sql.setInt(5, objconf.getInteresCredito()); 
        sql.setInt(6, objconf.getRetencionHaciendaBase()); 
        sql.setBoolean(7, objconf.isRetencionLineal());
        sql.setInt(8, objconf.getIva()); 
        sql.setInt(9, objconf.getSubidaMaxBolsa());
        sql.setBoolean(10, objconf.isCrackBolsa());
        sql.setInt(11, objconf.getPorcentajePremioLiga()); 
        sql.setInt(12, objconf.getPorcentajeCampeonCopa()); 
        sql.setInt(13, objconf.getIBI());
        // Si es un update asignamos el parametro del id
        if (tipo == update) sql.setLong(14, objconf.getId());

        return sql;
    }

    @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        var obj = new ConfigEconomiaFutbol8();

        obj.setId(retorno.getLong("id"));
        obj.setIdEquipoGestor(retorno.getLong("equipogestor"));
        obj.setDiasGestion(retorno.getInt("diasgestion"));
        obj.setModificado(retorno.getBoolean("modificado"));               
        obj.setInteresCredito(retorno.getInt("interescredito"));
        obj.setRetencionHaciendaBase(retorno.getInt("retencionhaciendabase"));
        obj.setRetencionLineal(retorno.getBoolean("retencionlineal"));
        obj.setIva(retorno.getInt("iva"));
        obj.setSubidaMaxBolsa(retorno.getInt("subidamaxbolsa"));
        obj.setCrackBolsa(retorno.getBoolean("crackbolsa"));
        obj.setPorcentajePremioLiga(retorno.getInt("porcentajepremioliga"));
        obj.setPorcentajeCampeonCopa(retorno.getInt("porcentajecampeoncopa"));
        obj.setIBI(retorno.getInt("ibi"));

        return obj;
    }
 
}
