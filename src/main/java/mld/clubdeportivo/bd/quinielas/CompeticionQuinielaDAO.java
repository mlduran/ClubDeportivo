package mld.clubdeportivo.bd.quinielas;

import java.sql.*;
import static java.sql.Types.VARCHAR;
import java.util.ArrayList;
import mld.clubdeportivo.base.Club;
import mld.clubdeportivo.base.Grupo;
import mld.clubdeportivo.base.quinielas.CompeticionQuiniela;
import mld.clubdeportivo.bd.*;
import static mld.clubdeportivo.bd.TablasDAO.competicionesquiniela;
import static mld.clubdeportivo.bd.TipoRetornoDAO.INTEGER;
import static mld.clubdeportivo.bd.TipoSaveDAO.update;


/**
 *
 * @author java1
 */
public class CompeticionQuinielaDAO extends ObjetoDAO {

    protected String schema(){return "quiniela" + getEntorno();}
    protected String nombreTabla(){return competicionesquiniela.name();}
    protected String[] camposTabla() {
        String[] campos = {
            "grupo",
            "nombre",
            "activa",
            "proximajornada",
            "ultimajornada",
            "fecha",
            "campeon",
            "subcampeon"
        };
        return campos;
        }

    // hay que crear competicion
   
   
    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        var objcomp = (CompeticionQuiniela) obj;
        var grp = new Grupo();
        sql.setLong(1, grp.grupoQuiniela().getId());
        sql.setString(2, objcomp.getNombre());
        sql.setBoolean(3, objcomp.isActiva());
        sql.setInt(4, objcomp.getProximaJornada());
        sql.setInt(5, objcomp.getUltimaJornada());

        if (objcomp.getFecha() == null)
            sql.setDate(6, null);
        else
            sql.setTimestamp(6, new Timestamp(objcomp.getFecha().getTime()));

        if (objcomp.getCampeon() == null)
            sql.setNull(7, VARCHAR);
        else
            sql.setString(7, objcomp.getCampeon());

        if (objcomp.getSubcampeon() == null)
            sql.setNull(8, VARCHAR);
        else
            sql.setString(8, objcomp.getSubcampeon());

        // Si es un update asignamos el parametro del id
        if (tipo == update) sql.setLong(9, objcomp.getId());

        return sql;
    }


    @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        var obj = new CompeticionQuiniela();
                
        obj.setId(retorno.getLong("id"));
        obj.setActiva(retorno.getBoolean("activa"));
        obj.setNombre(retorno.getString("nombre"));
        obj.setProximaJornada(retorno.getInt("proximajornada"));
        obj.setUltimaJornada(retorno.getInt("ultimajornada"));
        obj.setFecha(retorno.getDate("fecha"));
        obj.setCampeon(retorno.getString("campeon")); 
        obj.setSubcampeon(retorno.getString("subcampeon")); 

        return obj;
    }

    protected CompeticionQuiniela getCompeticionById(long id)
             throws DAOException {


        return (CompeticionQuiniela) getObjetoById(id);

    }

    protected CompeticionQuiniela getCompeticionByNombre(String nombre, Grupo grp)
             throws DAOException {

         var txtsql = "SELECT * FROM " + nombreTabla() + 
                 " WHERE nombre = ? AND grupo = ?";
         var params = new ArrayList();
         params.add(nombre);
         params.add(grp.getId());

         var obj =
                (CompeticionQuiniela) getDataObject(txtsql, params);

        return obj;

    }

    protected ArrayList<CompeticionQuiniela> getCompeticionesNoActivas()
             throws DAOException {

         
         var txtsql = "SELECT * FROM " + nombreTabla() +
                 " WHERE activa = false";

         ArrayList<CompeticionQuiniela> objs =
                (ArrayList) getDataObjects(txtsql);

        return objs;

    }


     protected CompeticionQuiniela getCompeticionActiva()
             throws DAOException {

         // se supone que solo puede haber una 

         var txtsql = "SELECT * FROM " + nombreTabla() + 
                 " WHERE activa = true";
         var obj =
                (CompeticionQuiniela) getDataObject(txtsql);

        return obj;

    }
     
      protected int getCompeticionesGanadas(Club club)
             throws DAOException {
         
         // se supone que solo puede haber una 
         
         var params = new ArrayList();
         params.add(club.getNombre());
         
         var txtsql = "SELECT COUNT(*) FROM " + nombreTabla() + 
                 " WHERE campeon = ?";
         var num = (int) this.queryNumerica(txtsql, INTEGER, params);
         
         return num;
         
     }


  
}
