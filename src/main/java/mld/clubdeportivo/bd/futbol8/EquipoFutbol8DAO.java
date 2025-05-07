/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mld.clubdeportivo.bd.futbol8;

import static java.lang.String.valueOf;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import mld.clubdeportivo.base.Club;
import mld.clubdeportivo.base.futbol8.EquipoFutbol8;
import mld.clubdeportivo.base.futbol8.EspectativaFutbol8;
import static mld.clubdeportivo.base.futbol8.EspectativaFutbol8.values;
import mld.clubdeportivo.base.futbol8.JugadorFutbol8;
import mld.clubdeportivo.base.futbol8.PosicionElegidaFutbol8;
import static mld.clubdeportivo.base.futbol8.PosicionElegidaFutbol8.values;
import mld.clubdeportivo.base.futbol8.PosicionJugFutbol8;
import mld.clubdeportivo.bd.*;
import static mld.clubdeportivo.bd.TablasDAO.equiposfutbol8;
import static mld.clubdeportivo.bd.TipoRetornoDAO.BIGINT;
import static mld.clubdeportivo.bd.TipoSaveDAO.update;

/**
 *
 * @author Miguel
 */
public class EquipoFutbol8DAO extends ObjetoDAO{

    @Override
    protected String schema(){return "futbol8" + getEntorno();}
    @Override
    protected String nombreTabla(){return equiposfutbol8.name();}
    @Override
    protected String[] camposTabla() {
        String[] campos = {
            "activo",
            "automatico",
            "moral",
            "campo", 
            "publicidad", 
            "eqtecnico",
            "ojeadores", 
            "jugadoresojeados",
            "presupuesto", 
            "credito", 
            "bolsa", 
            "fechaBolsa",
            "entrenamiento", 
            "simulaciones",
            "modoauto",             
            "ampliarcampo", 
            "ampliarpublicidad", 
            "ampliareqtecnico", 
            "ampliarojeadores",
            "club",
            "precioentradas",
            "equipacion",
            "espectativa",
            "posicionJuvenil",
            "nombrecampo"
        };
        return campos;
        }

 
    protected EquipoFutbol8 getEquipoFubtol8ById(long id) throws DAOException {

        return (EquipoFutbol8) getObjetoById(id);
    }
    
    protected long idEquipoFutbol8(Club club) throws DAOException{

        var id = valueOf(club.getId());
        var txtsql = "SELECT id FROM " + nombreTabla() + " WHERE club = ".concat(id);
        var idobj = (long) queryNumerica(txtsql, BIGINT);

        return idobj;

    }

    protected EquipoFutbol8 getEquipoFubtol8ByClub(Club club) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE club = ?";
        var params = new ArrayList();
        params.add(club.getId());

        var obj = (EquipoFutbol8) getDataObject(txtsql, params);

        return obj;
    }

    protected ArrayList<EquipoFutbol8> getEquiposFubtol8()
            throws DAOException {
        

        var txtsql = "SELECT * " +
                        "FROM " + nombreTabla();

        ArrayList<EquipoFutbol8> listaObjs =
                (ArrayList) getDataObjects(txtsql);

        return listaObjs;

    }


        

    @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null) {
            return null;
        }

        var obj = new EquipoFutbol8();

        obj.setId(retorno.getLong("id"));
        obj.setActivo(retorno.getBoolean("activo"));
        obj.setAutomatico(retorno.getBoolean("automatico"));
        obj.setMoral(retorno.getInt("moral"));
        obj.setCampo(retorno.getInt("campo"));
        obj.setPublicidad(retorno.getInt("publicidad"));
        obj.setEqTecnico(retorno.getInt("eqtecnico"));
        obj.setOjeadores(retorno.getInt("ojeadores"));
        obj.setAmpliarCampo(retorno.getBoolean("ampliarcampo"));
        obj.setAmpliarPublicidad(retorno.getBoolean("ampliarpublicidad"));
        obj.setAmpliarEqTecnico(retorno.getBoolean("ampliareqtecnico"));
        obj.setAmpliarOjeadores(retorno.getBoolean("ampliarojeadores"));
        obj.setJugadoresOjeados(retorno.getInt("jugadoresojeados"));        
        obj.setPresupuesto(retorno.getInt("presupuesto"));
        obj.setCredito(retorno.getInt("credito"));
        obj.setBolsa(retorno.getInt("bolsa"));
        obj.setFechaBolsa(retorno.getDate("fechabolsa"));
        obj.setEntrenamiento(retorno.getBoolean("entrenamiento"));
        obj.setSimulaciones(retorno.getInt("simulaciones"));
        obj.setModoAuto(retorno.getBoolean("modoauto"));
        obj.setPrecioEntradas(retorno.getInt("precioentradas"));
        obj.setEquipacion(retorno.getInt("equipacion"));
        obj.setEspectativa(EspectativaFutbol8.values()[(retorno.getInt("espectativa"))]);
        obj.setPosicionJuvenil(PosicionElegidaFutbol8.values()[(retorno.getInt("posicionJuvenil"))]);
        obj.setNombreCampo(retorno.getString("nombrecampo"));
        obj.setIdClub(retorno.getLong("club"));
        return obj;

    }

    @Override
    protected PreparedStatement asignarCampos(PreparedStatement sql,
            Object obj, TipoSaveDAO tipo) throws SQLException {

        var objeto = (EquipoFutbol8) obj;

        sql.setBoolean(1, objeto.isActivo());
        sql.setBoolean(2, objeto.isAutomatico());
        sql.setInt(3, objeto.getMoral());
        sql.setInt(4, objeto.getCampo());
        sql.setInt(5, objeto.getPublicidad());
        sql.setInt(6, objeto.getEqTecnico());
        sql.setInt(7, objeto.getOjeadores());
        sql.setInt(8, objeto.getJugadoresOjeados());        
        sql.setInt(9, objeto.getPresupuesto());
        sql.setInt(10, objeto.getCredito());
        sql.setInt(11, objeto.getBolsa());
        if (objeto.getFechaBolsa() != null)
            sql.setTimestamp(12,
                new Timestamp(objeto.getFechaBolsa().getTime()));
        else 
            sql.setTimestamp(12, null);
        sql.setBoolean(13, objeto.isEntrenamiento());
        sql.setInt(14, objeto.getSimulaciones());
        sql.setBoolean(15, objeto.isModoAuto());
        sql.setBoolean(16, objeto.isAmpliarCampo());
        sql.setBoolean(17, objeto.isAmpliarPublicidad());
        sql.setBoolean(18, objeto.isAmpliarEqTecnico());
        sql.setBoolean(19, objeto.isAmpliarOjeadores());
        
        sql.setLong(20, objeto.getClub().getId());
        sql.setInt(21, objeto.getPrecioEntradas());
        sql.setInt(22, objeto.getEquipacion());
        sql.setInt(23, objeto.getEspectativa().ordinal());
        sql.setInt(24, objeto.getPosicionJuvenil().ordinal());
        sql.setString(25, objeto.getNombreCampo());

        // Si es un update asignamos el parametro del id
        if (tipo == update) sql.setLong(26, objeto.getId());

        return sql;
    }

  
    protected List<JugadorFutbol8> getJugadoresFutbol8(EquipoFutbol8 equipo)
            throws DAOException {

        var dao = new JugadorFutbol8DAO();
        var listaObjs = (ArrayList) dao.getJugadoresFutbol8(equipo);

        return listaObjs;
    }
}
