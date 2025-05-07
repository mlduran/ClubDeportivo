/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mld.clubdeportivo.bd.futbol8;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static java.sql.Types.NULL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mld.clubdeportivo.base.Grupo;
import mld.clubdeportivo.base.futbol8.EquipoFutbol8;
import mld.clubdeportivo.base.futbol8.JugadorFutbol8;
import mld.clubdeportivo.base.futbol8.PosicionJugFutbol8;
import static mld.clubdeportivo.base.futbol8.PosicionJugFutbol8.values;
import mld.clubdeportivo.bd.*;
import static mld.clubdeportivo.bd.TablasDAO.jugadoresfutbol8;
import static mld.clubdeportivo.bd.TipoRetornoDAO.INTEGER;
import static mld.clubdeportivo.bd.TipoSaveDAO.update;

/**
 *
 * @author Miguel
 */
public class JugadorFutbol8DAO extends ObjetoDAO{

    protected String schema(){return "futbol8" + getEntorno();}
    protected String nombreTabla(){return jugadoresfutbol8.name();}
    protected String[] camposTabla() {
        String[] campos = {
            "nombre",
            "ficha", 
            "clausula",
            "blindado",
            "jornadaslesion",
            "transferible",
            "contrato",
            "posicion",
            "valoracion",
            "estadofisico",
            "tarjetaamarilla",
            "tarjetaroja",
            "juegajornada",
            "golesliga",
            "golescopa",
            "equipo",
            "grupo",
            "ensubasta",
            "puja",
            "equipopuja",
            "partidosjugados"
        };
        return campos;
        }

     
    @Override
    protected Object crearObjeto(ResultSet retorno) throws SQLException, DAOException {

        if (retorno == null ) return null;

        var obj = new JugadorFutbol8();
        
        obj.setId(retorno.getLong("id"));
        obj.setNombre(retorno.getString("nombre"));
        obj.setFicha(retorno.getInt("ficha"));
        obj.setClausula(retorno.getInt("clausula"));
        obj.setBlindado(retorno.getBoolean("blindado"));
        obj.setJornadasLesion(retorno.getInt("jornadaslesion"));
        obj.setTransferible(retorno.getBoolean("transferible"));
        obj.setContrato(retorno.getInt("contrato"));
        obj.setPosicion(values()[(retorno.getInt("posicion"))]);
        obj.setValoracion(retorno.getInt("valoracion"));
        obj.setEstadoFisico(retorno.getInt("estadofisico"));
        obj.setTarjetaAmarilla(retorno.getBoolean("tarjetaamarilla"));
        obj.setTarjetaRoja(retorno.getBoolean("tarjetaroja"));
        obj.setJuegaJornada(retorno.getBoolean("juegajornada"));
        obj.setGolesLiga(retorno.getInt("golesliga"));
        obj.setGolesCopa(retorno.getInt("golescopa"));
        obj.setEnSubasta(retorno.getBoolean("ensubasta"));
        obj.setPuja(retorno.getInt("puja"));
        obj.setEquipoPuja(retorno.getLong("equipopuja"));
        obj.setPartidosJugados(retorno.getInt("partidosjugados"));
        

        return obj;
    }

    @Override
    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        var objeto = (JugadorFutbol8) obj;
        sql.setString(1, objeto.getNombre());
        sql.setInt(2, objeto.getFicha());
        sql.setInt(3, objeto.getClausula());
        sql.setBoolean(4, objeto.isBlindado());
        sql.setInt(5, objeto.getJornadasLesion());
        sql.setBoolean(6, objeto.isTransferible());
        sql.setInt(7, objeto.getContrato());
        sql.setInt(8, objeto.getPosicion().ordinal());
        sql.setInt(9, objeto.getValoracion());
        sql.setInt(10, objeto.getEstadoFisico());
        sql.setBoolean(11, objeto.isTarjetaAmarilla());
        sql.setBoolean(12, objeto.isTarjetaRoja());
        sql.setBoolean(13, objeto.isJuegaJornada());
        sql.setInt(14, objeto.getGolesLiga());
        sql.setInt(15, objeto.getGolesCopa());
        if (objeto.getEquipo() != null)
            sql.setLong(16, objeto.getEquipo().getId());
        else
            sql.setNull(16, NULL);
        sql.setLong(17, objeto.getGrupo().getId());
        sql.setBoolean(18, objeto.isEnSubasta());
        sql.setInt(19, objeto.getPuja());
        sql.setLong(20, objeto.getEquipoPuja());
        sql.setInt(21, objeto.getPartidosJugados());

        // Si es un update asignamos el parametro del id
        if (tipo == update) sql.setLong(22, objeto.getId());

        return sql;

    }
    
     protected JugadorFutbol8 getJugadorByEquipo(long id, EquipoFutbol8 eq) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE id = ?"
                + " AND equipo = ?";
        var params = new ArrayList();
        params.add(id);
        params.add(eq.getId());

        var obj = (JugadorFutbol8) getDataObject(txtsql, params);

        return obj;
    }
    
    protected JugadorFutbol8 getJugadorByNombre(String nombre, Grupo grp) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE nombre = ? AND grupo = ?";
        var params = new ArrayList();
        params.add(nombre);
        params.add(grp.getId());

        var obj = (JugadorFutbol8) getDataObject(txtsql, params);

        return obj;
    }

  
   
    protected List<JugadorFutbol8> getJugadoresFutbol8(Grupo grp) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE grupo = ?";
        var params = new ArrayList();
        params.add(grp.getId());

        ArrayList<JugadorFutbol8> listaObjs =
                (ArrayList) getDataObjects(txtsql, params);

        return listaObjs;
    }

    protected List<JugadorFutbol8> getJugadoresFutbol8(
            PosicionJugFutbol8 pos) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE posicion = ?";
        var params = new ArrayList();
        params.add(pos.ordinal());

        ArrayList<JugadorFutbol8> listaObjs = 
                (ArrayList) getDataObjects(txtsql, params);

        return listaObjs;
    }

   
    protected List<JugadorFutbol8> getJugadoresFutbol8(
            EquipoFutbol8 equipo) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE equipo = ?";
        var params = new ArrayList();
        params.add(equipo.getId());

        ArrayList<JugadorFutbol8> listaObjs =
                (ArrayList) getDataObjects(txtsql, params);

        return listaObjs;
    }

    
    protected List<JugadorFutbol8> getJugadoresFutbol8() throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla();

        ArrayList<JugadorFutbol8> listaObjs = (ArrayList) getDataObjects(txtsql);

        return listaObjs;
    }
    
    protected List<JugadorFutbol8> getJugadoresFutbol8Libres(
            Grupo grp) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE "
                + "equipo IS NULL AND grupo = ?";
        var params = new ArrayList();
        params.add(grp.getId());

        ArrayList<JugadorFutbol8> listaObjs =
                (ArrayList) getDataObjects(txtsql, params);
        
        for (var jug : listaObjs)
            jug.setGrupo(grp);

        return listaObjs;
    }
    
    protected List<JugadorFutbol8> getJugadoresFutbol8Mercado(
            EquipoFutbol8 equipo) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE equipo = ?"
                + " AND blindado = false";
        var params = new ArrayList();
        params.add(equipo.getId());

        ArrayList<JugadorFutbol8> listaObjs =
                (ArrayList) getDataObjects(txtsql, params);

        return listaObjs;
    }
    
    protected int getValoracionMediaJugadores(Grupo grp) throws DAOException {

        var txtsql = "SELECT AVG(valoracion) FROM " + nombreTabla() + "  WHERE grupo = ?";
        var params = new ArrayList();
        params.add(grp.getId());

        return (int) queryNumerica(txtsql, INTEGER, params);
    }
    
    protected int getClausulaMediaJugadores(Grupo grp) throws DAOException {

        var txtsql = "SELECT AVG(clausula) FROM " + nombreTabla() + "  WHERE grupo = ? " +
                "AND equipo IS NOT NULL";
        var params = new ArrayList();
        params.add(grp.getId());

        return (int) queryNumerica(txtsql, INTEGER, params);
    }

    protected ArrayList<HashMap> getJugadorEquipoSubasta(Grupo grp) throws DAOException{
        
        var txtsql = "SELECT * FROM " + nombreTabla() +
                " WHERE ensubasta = true AND equipopuja IS NOT NULL " +
                " AND grupo = " + grp.getId();
        
        String[] campos = {"id", "equipopuja"};
        var datos = queryRegistros(txtsql, campos);
        
        return datos;
        
    }
    
    protected void eliminarJugadores(EquipoFutbol8 eq) throws DAOException{
        
        var txtsql = "DELETE FROM " +
                    nombreTabla() + " WHERE equipo = " + eq.getId();
        deleteObjects(txtsql);        
        
    }
    
   
}
