package mld.clubdeportivo.bd.quinielas;

import static java.lang.String.valueOf;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import mld.clubdeportivo.base.Club;
import mld.clubdeportivo.base.quinielas.EquipoQuiniela;
import mld.clubdeportivo.bd.*;
import static mld.clubdeportivo.bd.TablasDAO.equiposquiniela;
import static mld.clubdeportivo.bd.TipoRetornoDAO.BIGINT;
import static mld.clubdeportivo.bd.TipoSaveDAO.update;


/**
 *
 * @author java1
 */
public class EquipoQuinielaDAO extends ObjetoDAO {

    protected String schema(){return "quiniela" + getEntorno();}
    protected String nombreTabla(){return equiposquiniela.name();}

    protected String[] camposTabla(){
        String[] campos = {
            "club",
            "activo",
            "admin"
        };
        return campos;
        }


    protected EquipoQuiniela getEquipoQuinielaById(long id) throws DAOException {

        return (EquipoQuiniela) getObjetoById(id);
    }
    
    protected long idEquipoQuiniela(Club club) throws DAOException{

        var id = valueOf(club.getId());
        var txtsql = "SELECT id FROM " + nombreTabla() + " WHERE club = ".concat(id);
        var idobj = (long) queryNumerica(txtsql, BIGINT);

        return idobj;

    }
      
    protected EquipoQuiniela getEquipoQuinielaByClub(Club club) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE club = ?";
        var params = new ArrayList();
        params.add(club.getId());
        
        var obj = (EquipoQuiniela) getDataObject(txtsql, params);

        return obj;
    }
 
    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        var objeq = (EquipoQuiniela) obj;

        sql.setLong(1, objeq.obtenerIdClub());
        sql.setBoolean(2, objeq.isActivo());
        sql.setBoolean(3, objeq.isAdmin());

        // Si es un update asignamos el parametro del id
        if (tipo == update) sql.setLong(4, objeq.getId());

        return sql;
    }

 
    @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        var obj = new EquipoQuiniela();
                
        obj.setId(retorno.getLong("id"));
        obj.setActivo(retorno.getBoolean("activo"));
        obj.setAdmin(retorno.getBoolean("admin"));

        return obj;
    }

   
    protected ArrayList<EquipoQuiniela> getEquiposQuinielaActivos()
            throws DAOException {

        var txtsql = "SELECT * " + "FROM " + nombreTabla() +
                " WHERE activo = true";

        ArrayList<EquipoQuiniela> listaObjs =
                (ArrayList) getDataObjects(txtsql);

        return listaObjs;

    }
    
  
  
}
