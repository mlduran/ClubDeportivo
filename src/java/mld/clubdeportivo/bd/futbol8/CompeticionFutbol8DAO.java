package mld.clubdeportivo.bd.futbol8;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import mld.clubdeportivo.base.Club;
import mld.clubdeportivo.base.Grupo;
import mld.clubdeportivo.base.futbol8.CompeticionFutbol8;
import mld.clubdeportivo.bd.DAOException;
import mld.clubdeportivo.bd.ObjetoDAO;
import mld.clubdeportivo.bd.TablasDAO;
import mld.clubdeportivo.bd.TipoRetornoDAO;
import mld.clubdeportivo.bd.TipoSaveDAO;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


/**
 *
 * @author java1
 */
public class CompeticionFutbol8DAO extends ObjetoDAO {
    
    private static Logger logger = 
            LogManager.getLogger(CompeticionFutbol8DAO.class);

    protected String schema(){return "futbol8" + CompeticionFutbol8DAO.getEntorno();}
    protected String nombreTabla(){return TablasDAO.competicionesfutbol8.name();}
    protected String[] camposTabla() {
        String[] campos = {
            "grupo",
            "nombre",
            "activa",
            "proximajornada",
            "ultimajornada",
            "fecha",
            "campeon",
            "subcampeon",
            "clase",
            "maximosGoleadores",
            "recaudacion"
        };
        return campos;
        }

    // hay que crear competicion
   
   
    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        CompeticionFutbol8 objcomp = (CompeticionFutbol8) obj;

        sql.setLong(1, objcomp.getGrupo().getId());
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

        sql.setString(9, objcomp.getClase());
        
        sql.setString(10, objcomp.getMaximosGoleadores());
        sql.setInt(11, objcomp.getRecaudacion());

        // Si es un update asignamos el parametro del id
        if (tipo == TipoSaveDAO.update) sql.setLong(12, objcomp.getId());

        return sql;
    }


    @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        CompeticionFutbol8 obj = new CompeticionFutbol8();
                
        obj.setId(retorno.getLong("id"));
        obj.setClase(retorno.getString("clase"));
        obj.setActiva(retorno.getBoolean("activa"));
        obj.setNombre(retorno.getString("nombre"));
        obj.setProximaJornada(retorno.getInt("proximajornada"));
        obj.setUltimaJornada(retorno.getInt("ultimajornada"));
        obj.setFecha(retorno.getDate("fecha"));
        obj.setCampeon(retorno.getString("campeon"));
        obj.setSubcampeon(retorno.getString("subcampeon"));
        obj.setMaximosGoleadores(retorno.getString("maximosgoleadores"));
        obj.setRecaudacion(retorno.getInt("recaudacion"));

        return obj;
    }

    protected CompeticionFutbol8 getCompeticionById(long id)
             throws DAOException {


        return (CompeticionFutbol8) getObjetoById(id);

    }

    protected CompeticionFutbol8 getCompeticionByNombre(String nombre, Grupo grp)
             throws DAOException {

         String txtsql = "SELECT * FROM " + nombreTabla() + 
                 " WHERE nombre = ? AND grupo = ?";

         ArrayList params = new ArrayList();
         params.add(nombre);
         params.add(grp.getId());

         CompeticionFutbol8 obj =
                (CompeticionFutbol8) getDataObject(txtsql, params);

        return obj;

    }

    protected ArrayList<CompeticionFutbol8> getCompeticionesNoActivas(Grupo grp)
             throws DAOException {

        ArrayList params = new ArrayList();
        params.add(grp.getId());
         
        String txtsql = "SELECT * FROM " + nombreTabla() +
                 " WHERE activa = false AND grupo = ?";

        ArrayList<CompeticionFutbol8> objs =
                (ArrayList) getDataObjects(txtsql, params);

        return objs;

    }

    protected ArrayList<CompeticionFutbol8> getCompeticiones(Grupo grp)
             throws DAOException {

        ArrayList params = new ArrayList();
        params.add(grp.getId());
         
        String txtsql = "SELECT * FROM " + nombreTabla() +
                 " WHERE grupo = ?";

        ArrayList<CompeticionFutbol8> objs =
                (ArrayList) getDataObjects(txtsql, params);

        return objs;

    }
    
    protected ArrayList<CompeticionFutbol8> getCompeticiones()
             throws DAOException {

         
        String txtsql = "SELECT * FROM " + nombreTabla();

        ArrayList<CompeticionFutbol8> objs =
                (ArrayList) getDataObjects(txtsql);

        return objs;

    }

     protected CompeticionFutbol8 getCompeticionActiva(Grupo grp, String clase)
             throws DAOException {

         // se supone que solo puede haber una 

         ArrayList params = new ArrayList();
         params.add(grp.getId());
         params.add(clase);
        
         String txtsql = "SELECT * FROM " + nombreTabla() + 
                 " WHERE activa = true AND grupo = ? AND clase = ?";
         
         CompeticionFutbol8 obj =
                (CompeticionFutbol8) getDataObject(txtsql, params);
         
         if (obj == null){
             logger.error("No se encuentra competicion activa con " + txtsql);
             logger.error(params);
         }
         
         logger.info(obj);

        return obj;

    }

     protected int getCompeticionesGanadas(Club club, String clase)
             throws DAOException {
         
         // se supone que solo puede haber una 
         
         ArrayList params = new ArrayList();
         params.add(club.getNombre());
         params.add(clase);
         
         String txtsql = "SELECT COUNT(*) FROM " + nombreTabla() + 
                 " WHERE campeon = ? AND clase = ?";
         
         int num = (int) this.queryNumerica(txtsql, TipoRetornoDAO.INTEGER, params);
         
         return num;
         
     }

  
}
