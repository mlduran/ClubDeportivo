package mld.clubdeportivo.bd.quinielas;

import java.sql.*;
import java.util.ArrayList;
import mld.clubdeportivo.base.Club;
import mld.clubdeportivo.base.Grupo;
import mld.clubdeportivo.base.quinielas.CompeticionQuiniela;
import mld.clubdeportivo.bd.*;


/**
 *
 * @author java1
 */
public class CompeticionQuinielaDAO extends ObjetoDAO {

    protected String schema(){return "quiniela" + this.getEntorno();}
    protected String nombreTabla(){return TablasDAO.competicionesquiniela.name();}
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

        CompeticionQuiniela objcomp = (CompeticionQuiniela) obj;

        Grupo grp = new Grupo();
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
            sql.setNull(7, java.sql.Types.VARCHAR);
        else
            sql.setString(7, objcomp.getCampeon());

        if (objcomp.getSubcampeon() == null)
            sql.setNull(8, java.sql.Types.VARCHAR);
        else
            sql.setString(8, objcomp.getSubcampeon());

        // Si es un update asignamos el parametro del id
        if (tipo == TipoSaveDAO.update) sql.setLong(9, objcomp.getId());

        return sql;
    }


    @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        CompeticionQuiniela obj = new CompeticionQuiniela();
                
        obj.setId(retorno.getLong("id"));
        obj.setActiva(retorno.getBoolean("activa"));
        obj.setNombre(retorno.getString("nombre"));
        obj.setProximaJornada(retorno.getInt("proximajornada"));
        obj.setUltimaJornada(retorno.getInt("ultimajornada"));
        obj.setFecha(retorno.getDate("fecha"));


        return obj;
    }

    protected CompeticionQuiniela getCompeticionById(long id)
             throws DAOException {


        return (CompeticionQuiniela) getObjetoById(id);

    }

    protected CompeticionQuiniela getCompeticionByNombre(String nombre, Grupo grp)
             throws DAOException {

         String txtsql = "SELECT * FROM " + nombreTabla() + 
                 " WHERE nombre = ? AND grupo = ?";

         ArrayList params = new ArrayList();
         params.add(nombre);
         params.add(grp.getId());

         CompeticionQuiniela obj =
                (CompeticionQuiniela) getDataObject(txtsql, params);

        return obj;

    }

    protected ArrayList<CompeticionQuiniela> getCompeticionesNoActivas()
             throws DAOException {

         
         String txtsql = "SELECT * FROM " + nombreTabla() +
                 " WHERE activa = false";

         ArrayList<CompeticionQuiniela> objs =
                (ArrayList) getDataObjects(txtsql);

        return objs;

    }


     protected CompeticionQuiniela getCompeticionActiva()
             throws DAOException {

         // se supone que solo puede haber una 

         String txtsql = "SELECT * FROM " + nombreTabla() + 
                 " WHERE activa = true";
        
         CompeticionQuiniela obj =
                (CompeticionQuiniela) getDataObject(txtsql);

        return obj;

    }
     
      protected int getCompeticionesGanadas(Club club)
             throws DAOException {
         
         // se supone que solo puede haber una 
         
         ArrayList params = new ArrayList();
         params.add(club.getNombre());
         
         String txtsql = "SELECT COUNT(*) FROM " + nombreTabla() + 
                 " WHERE campeon = ?";
         
         int num = (int) this.queryNumerica(txtsql, TipoRetornoDAO.INTEGER, params);
         
         return num;
         
     }


  
}
