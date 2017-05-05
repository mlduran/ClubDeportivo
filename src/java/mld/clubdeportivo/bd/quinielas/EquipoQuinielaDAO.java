package mld.clubdeportivo.bd.quinielas;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import mld.clubdeportivo.base.Club;
import mld.clubdeportivo.base.Grupo;
import mld.clubdeportivo.base.quinielas.EquipoQuiniela;
import mld.clubdeportivo.bd.*;


/**
 *
 * @author java1
 */
public class EquipoQuinielaDAO extends ObjetoDAO {

    protected String schema(){return "quiniela" + EquipoQuinielaDAO.getEntorno();}
    protected String nombreTabla(){return TablasDAO.equiposquiniela.name();}

    protected String[] camposTabla(){
        String[] campos = {
            "club",
            "activo"};
        return campos;
        }


    protected EquipoQuiniela getEquipoQuinielaById(long id) throws DAOException {

        return (EquipoQuiniela) getObjetoById(id);
    }
    
    protected long idEquipoQuiniela(Club club) throws DAOException{

        String id = String.valueOf(club.getId());
        String txtsql = "SELECT id FROM " + nombreTabla() + " WHERE club = ".concat(id);

        long idobj = (long) queryNumerica(txtsql, TipoRetornoDAO.BIGINT);

        return idobj;

    }
      
    protected EquipoQuiniela getEquipoQuinielaByClub(Club club) throws DAOException {

        String txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE club = ?";

        ArrayList params = new ArrayList();
        params.add(club.getId());
        
        EquipoQuiniela obj = (EquipoQuiniela) getDataObject(txtsql, params);

        return obj;
    }
 
    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        EquipoQuiniela objeq = (EquipoQuiniela) obj;

        sql.setLong(1, objeq.obtenerIdClub());
        sql.setBoolean(2, objeq.isActivo());

        // Si es un update asignamos el parametro del id
        if (tipo == TipoSaveDAO.update) sql.setLong(3, objeq.getId());

        return sql;
    }

 
    @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        EquipoQuiniela obj = new EquipoQuiniela();
                
        obj.setId(retorno.getLong("id"));
        obj.setActivo(retorno.getBoolean("activo"));

        return obj;
    }

   
    protected ArrayList<EquipoQuiniela> getEquiposQuinielaActivos()
            throws DAOException {

        String txtsql = "SELECT * " + "FROM " + nombreTabla() +
                " WHERE activo = true";

        ArrayList<EquipoQuiniela> listaObjs =
                (ArrayList) getDataObjects(txtsql);

        return listaObjs;

    }
    
  
  
}
