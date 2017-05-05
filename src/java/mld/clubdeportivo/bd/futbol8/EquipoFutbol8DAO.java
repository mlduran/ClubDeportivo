/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mld.clubdeportivo.bd.futbol8;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import mld.clubdeportivo.base.Club;
import mld.clubdeportivo.base.futbol8.EquipoFutbol8;
import mld.clubdeportivo.base.futbol8.EspectativaFutbol8;
import mld.clubdeportivo.base.futbol8.JugadorFutbol8;
import mld.clubdeportivo.base.futbol8.PosicionElegidaFutbol8;
import mld.clubdeportivo.bd.*;

/**
 *
 * @author Miguel
 */
public class EquipoFutbol8DAO extends ObjetoDAO{

    protected String schema(){return "futbol8" + EquipoFutbol8DAO.getEntorno();}
    protected String nombreTabla(){return TablasDAO.equiposfutbol8.name();}
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

        String id = String.valueOf(club.getId());
        String txtsql = "SELECT id FROM " + nombreTabla() + " WHERE club = ".concat(id);

        long idobj = (long) queryNumerica(txtsql, TipoRetornoDAO.BIGINT);

        return idobj;

    }

    protected EquipoFutbol8 getEquipoFubtol8ByClub(Club club) throws DAOException {

        String txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE club = ?";

        ArrayList params = new ArrayList();
        params.add(club.getId());

        EquipoFutbol8 obj = (EquipoFutbol8) getDataObject(txtsql, params);

        return obj;
    }

    protected ArrayList<EquipoFutbol8> getEquiposFubtol8()
            throws DAOException {
        

        String txtsql = "SELECT * " +
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

        EquipoFutbol8 obj = new EquipoFutbol8();

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

        EquipoFutbol8 objeto = (EquipoFutbol8) obj;

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
        if (tipo == TipoSaveDAO.update) sql.setLong(26, objeto.getId());

        return sql;
    }

  
    protected List<JugadorFutbol8> getJugadoresFutbol8(EquipoFutbol8 equipo)
            throws DAOException {

        JugadorFutbol8DAO dao = new JugadorFutbol8DAO();

        ArrayList listaObjs = (ArrayList) dao.getJugadoresFutbol8(equipo);

        return listaObjs;
    }
}
