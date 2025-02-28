
package mld.clubdeportivo.bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import mld.clubdeportivo.base.Bolsa;
import static mld.clubdeportivo.bd.TablasDAO.bolsa;
import static mld.clubdeportivo.bd.TipoSaveDAO.update;

/**
 *
 * @author Miguel
 */
public class BolsaDAO extends ObjetoDAO {

    protected String schema(){return "clubdeportivo" + getEntorno();}
    protected String nombreTabla(){return bolsa.name();}
    protected String[] camposTabla(){
        String[] campos = {
            "fecha",
            "valor"};
        return campos;
        }
 
    protected List<Bolsa> getRegistros() throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + " ORDER BY id";
        
        ArrayList<Bolsa> listaObjs = (ArrayList) getDataObjects(txtsql);

        return listaObjs;
    }
    
    protected List<Bolsa> getRegistros(Date fecha) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + " WHERE fecha > STR_TO_DATE(?,'%Y-%m-%d') ORDER BY id";
        var params = new ArrayList();
        params.add(fecha);
        
        ArrayList<Bolsa> listaObjs = (ArrayList) getDataObjects(txtsql, params);

        return listaObjs;
    }

   
    @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        var obj = new Bolsa();

        obj.setId(retorno.getLong("id"));        
        obj.setFecha(retorno.getTimestamp("fecha"));
        obj.setValor(retorno.getInt("valor"));

        return obj;
    }

    @Override
    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        var newObj = (Bolsa) obj;
       
        sql.setTimestamp(1,
                new Timestamp(newObj.getFecha().getTime()));
        sql.setInt(2, newObj.getValor());

        // Si es un update asignamos el parametro del id
        if (tipo == update) sql.setLong(3, newObj.getId());

        return sql;
    }
    
}
