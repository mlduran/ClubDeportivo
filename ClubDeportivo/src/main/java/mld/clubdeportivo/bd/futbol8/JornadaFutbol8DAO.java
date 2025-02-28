
package mld.clubdeportivo.bd.futbol8;

/**
 *
 * @author mlopezd
 */

import static java.lang.String.valueOf;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import mld.clubdeportivo.base.Competicion;
import mld.clubdeportivo.base.futbol8.JornadaFutbol8;
import mld.clubdeportivo.bd.*;
import static mld.clubdeportivo.bd.TablasDAO.jornadasfutbol8;
import static mld.clubdeportivo.bd.TipoRetornoDAO.BIGINT;
import static mld.clubdeportivo.bd.TipoSaveDAO.update;

public class JornadaFutbol8DAO extends ObjetoDAO{

    protected String schema(){return "futbol8" + getEntorno();}
    protected String nombreTabla() {
        return jornadasfutbol8.name();
    }
    protected String[] camposTabla() {
        String[] campos = {
            "numero",
            "descripcion",
            "competicion",
            "fecha"
        };
        return campos;
        }


    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        var objjor = (JornadaFutbol8) obj;

        sql.setInt(1, objjor.getNumero());
        sql.setString(2, objjor.getDescripcion());
        sql.setLong(3, objjor.getCompeticion().getId());

        if (objjor.getFecha() == null)
            sql.setTimestamp(4, null);
        else
            sql.setTimestamp(4,
                    new Timestamp(objjor.getFecha().getTime()));
        
        // Si es un update asignamos el parametro del id
        if (tipo == update) sql.setLong(5, objjor.getId());

        return sql;
    }

    @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        var obj = new JornadaFutbol8();

        obj.setId(retorno.getLong("id"));
        obj.setNumero(retorno.getInt("numero"));
        obj.setDescripcion(retorno.getString("descripcion"));
        obj.setFecha(retorno.getTimestamp("fecha"));       

        return obj;
    }
    
     protected JornadaFutbol8 getJornadaByNumero(Competicion comp, int numero)
             throws DAOException {

         var txtsql = "SELECT * FROM " + nombreTabla() + 
                 " WHERE competicion = ? AND numero = ?";
         var params = new ArrayList();
         params.add(comp.getId());
         params.add(numero);

         var obj =
                (JornadaFutbol8) getDataObject(txtsql, params);

        return obj;

    }

      protected ArrayList<JornadaFutbol8> getJornadas(Competicion comp)
             throws DAOException {

         var txtsql = "SELECT * FROM " + nombreTabla() +
                 " WHERE competicion = ?";
         var params = new ArrayList();
         params.add(comp.getId());

         ArrayList<JornadaFutbol8> objs =
                (ArrayList) getDataObjects(txtsql, params);

        return objs;

    }

      protected JornadaFutbol8 getProximaJornada(Competicion comp)
              throws DAOException {


          var txtsql = "SELECT * FROM " + nombreTabla() +
                " WHERE competicion = ? AND fecha is NULL" +
                " ORDER BY numero";
          var params = new ArrayList();
          params.add(comp.getId());

          var obj =
                  (JornadaFutbol8) getDataObject(txtsql, params);

          return obj;

    }
      
    protected long getIdCompeticion(JornadaFutbol8 jornada) throws DAOException{
         
         var id = valueOf(jornada.getId());
         var txtsql = "SELECT competicion FROM " +
                 nombreTabla() + " WHERE id = ".concat(id);
         var idobj = (long) queryNumerica(txtsql, BIGINT);

         return idobj;         
         
     }
    
    protected JornadaFutbol8 getUltimaJornadaDisputada() throws DAOException{
    
        var txtsql = "SELECT * FROM " + nombreTabla() + 
                " ORDER BY fecha DESC LIMIT 1";
        var obj =
                  (JornadaFutbol8) getDataObject(txtsql);
        
        return obj;
    }
    

}
