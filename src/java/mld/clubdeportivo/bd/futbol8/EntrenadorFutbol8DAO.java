/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mld.clubdeportivo.bd.futbol8;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mld.clubdeportivo.base.Grupo;
import mld.clubdeportivo.base.futbol8.EntrenadorFutbol8;
import mld.clubdeportivo.base.futbol8.EquipoFutbol8;
import mld.clubdeportivo.base.futbol8.TacticaFutbol8;
import mld.clubdeportivo.bd.*;

/**
 *
 * @author Miguel
 */
public class EntrenadorFutbol8DAO extends ObjetoDAO{

    protected String schema(){return "futbol8" + EntrenadorFutbol8DAO.getEntorno();}
    protected String nombreTabla(){return TablasDAO.entrenadoresfutbol8.name();}
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

        EntrenadorFutbol8 obj = new EntrenadorFutbol8();
        
        obj.setId(retorno.getLong("id"));
        obj.setNombre(retorno.getString("nombre"));
        obj.setFicha(retorno.getInt("ficha"));
        obj.setContrato(retorno.getInt("contrato"));

        ArrayList<TacticaFutbol8> tacts = new ArrayList<TacticaFutbol8>();

        String stringTact = "";
        if (retorno.getString("tacticas") != null)
            stringTact = retorno.getString("tacticas");

        for (String  numero : stringTact.split(",")) {
            TacticaFutbol8 tact = TacticaFutbol8.tacticaFutbol8(Integer.valueOf(numero));
            tacts.add(tact);
        }
        obj.setTacticas(tacts);
        
        obj.setUltimaTacticaUtilizada(
                TacticaFutbol8.tacticaFutbol8(retorno.getInt("ultimatacticautilizada")));
        
        obj.setPlusTactica(retorno.getInt("plustactica"));
                        
        return obj;
    }

    @Override
    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        EntrenadorFutbol8 objeto = (EntrenadorFutbol8) obj;
        sql.setString(1, objeto.getNombre());
        if (objeto.getEquipo() != null)
            sql.setLong(2, objeto.getEquipo().getId());
        else
            sql.setNull(2, java.sql.Types.BIGINT);
        sql.setString(3, objeto.getTacticasString());
        sql.setInt(4, objeto.getFicha());
        sql.setInt(5, objeto.getContrato());
        sql.setLong(6, objeto.getGrupo().getId());
        
        if (objeto.getUltimaTacticaUtilizada() != null)
            sql.setInt(7, objeto.getUltimaTacticaUtilizada().getNumero());
        else
            sql.setNull(7, java.sql.Types.INTEGER);
        
        sql.setInt(8, objeto.getPlusTactica());
        
        // Si es un update asignamos el parametro del id
        if (tipo == TipoSaveDAO.update) sql.setLong(9, objeto.getId());

        return sql;

    }

   
    protected List<EntrenadorFutbol8> getEntrenadoresFutbol8(Grupo grp) throws DAOException {

        String txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE grupo = ?";

        ArrayList params = new ArrayList();
        params.add(grp.getId());

        ArrayList<EntrenadorFutbol8> listaObjs =
                (ArrayList) getDataObjects(txtsql, params);

        return listaObjs;
    }
    
    protected List<EntrenadorFutbol8> getEntrenadoresLibresFutbol8(Grupo grp) throws DAOException {

        String txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE grupo = ? "
                + "AND equipo is NULL";

        ArrayList params = new ArrayList();
        params.add(grp.getId());

        ArrayList<EntrenadorFutbol8> listaObjs =
                (ArrayList) getDataObjects(txtsql, params);

        return listaObjs;
    }
    
    protected int getNumEntrenadoresFutbol8(Grupo grp) throws DAOException {

        String txtsql = "SELECT COUNT(*) FROM " + nombreTabla() +
                "  WHERE grupo = " + grp.getId();

        return (int) queryNumerica(txtsql, TipoRetornoDAO.INTEGER);
    }
       
    protected EntrenadorFutbol8 getEntrenadorFutbol8(
            EquipoFutbol8 equipo) throws DAOException {

        String txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE equipo = ?";

        ArrayList params = new ArrayList();
        params.add(equipo.getId());

        EntrenadorFutbol8 obj =  (EntrenadorFutbol8) getDataObject(txtsql, params);
        
        obj.setEquipo(equipo);
        obj.setGrupo(equipo.getClub().getGrupo());
        equipo.setEntrenador(obj);

        return obj;
    }

    
    protected void eliminarEntrenador(EquipoFutbol8 eq) throws DAOException{
        
        String txtsql = "DELETE FROM " +
                    nombreTabla() + " WHERE equipo = " + eq.getId();
        deleteObjects(txtsql);        
        
    }
    
   
}
