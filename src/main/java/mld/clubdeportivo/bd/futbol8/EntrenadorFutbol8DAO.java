/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mld.clubdeportivo.bd.futbol8;

import static java.lang.Integer.valueOf;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static java.sql.Types.BIGINT;
import static java.sql.Types.INTEGER;
import java.util.ArrayList;
import java.util.List;
import mld.clubdeportivo.base.Grupo;
import mld.clubdeportivo.base.futbol8.EntrenadorFutbol8;
import mld.clubdeportivo.base.futbol8.EquipoFutbol8;
import mld.clubdeportivo.base.futbol8.TacticaFutbol8;
import static mld.clubdeportivo.base.futbol8.TacticaFutbol8.tacticaFutbol8;
import mld.clubdeportivo.bd.*;
import static mld.clubdeportivo.bd.TablasDAO.entrenadoresfutbol8;
import static mld.clubdeportivo.bd.TipoRetornoDAO.INTEGER;
import static mld.clubdeportivo.bd.TipoSaveDAO.update;

/**
 *
 * @author Miguel
 */
public class EntrenadorFutbol8DAO extends ObjetoDAO{

    @Override
    protected String schema(){return "futbol8" + getEntorno();}
    @Override
    protected String nombreTabla(){return entrenadoresfutbol8.name();}
    @Override
    protected String[] camposTabla() {
        String[] campos = {
            "nombre",
            "equipo",
            "tacticas",
            "ficha",
            "contrato",
            "grupo",
            "ultimatacticautilizada",
            "plustactica"
        };
        return campos;
        }

     
    @Override
    protected Object crearObjeto(ResultSet retorno) throws SQLException, DAOException {

        if (retorno == null ) return null;

        var obj = new EntrenadorFutbol8();
        
        obj.setId(retorno.getLong("id"));
        obj.setNombre(retorno.getString("nombre"));
        obj.setFicha(retorno.getInt("ficha"));
        obj.setContrato(retorno.getInt("contrato"));

        var tacts = new ArrayList<TacticaFutbol8>();
        var stringTact = "";
        if (retorno.getString("tacticas") != null)
            stringTact = retorno.getString("tacticas");

        for (var  numero : stringTact.split(",")) {
            var tact = tacticaFutbol8(Integer.parseInt(numero));
            tacts.add(tact);
        }
        obj.setTacticas(tacts);
        
        obj.setUltimaTacticaUtilizada(tacticaFutbol8(retorno.getInt("ultimatacticautilizada")));
        
        obj.setPlusTactica(retorno.getInt("plustactica"));
                        
        return obj;
    }

    @Override
    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        var objeto = (EntrenadorFutbol8) obj;
        sql.setString(1, objeto.getNombre());
        if (objeto.getEquipo() != null)
            sql.setLong(2, objeto.getEquipo().getId());
        else
            sql.setNull(2, BIGINT);
        sql.setString(3, objeto.getTacticasString());
        sql.setInt(4, objeto.getFicha());
        sql.setInt(5, objeto.getContrato());
        sql.setLong(6, objeto.getGrupo().getId());
        
        if (objeto.getUltimaTacticaUtilizada() != null)
            sql.setInt(7, objeto.getUltimaTacticaUtilizada().getNumero());
        else
            sql.setNull(7, 0);
        
        sql.setInt(8, objeto.getPlusTactica());
        
        // Si es un update asignamos el parametro del id
        if (tipo == update) sql.setLong(9, objeto.getId());

        return sql;

    }

   
    protected List<EntrenadorFutbol8> getEntrenadoresFutbol8(Grupo grp) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE grupo = ?";
        var params = new ArrayList();
        params.add(grp.getId());

        ArrayList<EntrenadorFutbol8> listaObjs =
                (ArrayList) getDataObjects(txtsql, params);

        return listaObjs;
    }
    
    protected List<EntrenadorFutbol8> getEntrenadoresLibresFutbol8(Grupo grp) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE grupo = ? "
                + "AND equipo is NULL";
        var params = new ArrayList();
        params.add(grp.getId());

        ArrayList<EntrenadorFutbol8> listaObjs =
                (ArrayList) getDataObjects(txtsql, params);

        return listaObjs;
    }
    
    protected int getNumEntrenadoresFutbol8(Grupo grp) throws DAOException {

        var txtsql = "SELECT COUNT(*) FROM " + nombreTabla() +
                "  WHERE grupo = " + grp.getId();

        return (int) queryNumerica(txtsql, TipoRetornoDAO.INTEGER);
    }
       
    protected EntrenadorFutbol8 getEntrenadorFutbol8(
            EquipoFutbol8 equipo) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE equipo = ?";
        var params = new ArrayList();
        params.add(equipo.getId());

        var obj =  (EntrenadorFutbol8) getDataObject(txtsql, params);
        
        obj.setEquipo(equipo);
        obj.setGrupo(equipo.getClub().getGrupo());
        equipo.setEntrenador(obj);

        return obj;
    }

    
    protected void eliminarEntrenador(EquipoFutbol8 eq) throws DAOException{
        
        var txtsql = "DELETE FROM " +
                    nombreTabla() + " WHERE equipo = " + eq.getId();
        deleteObjects(txtsql);        
        
    }
    
   
}
