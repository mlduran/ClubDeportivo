
package mld.clubdeportivo.bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import mld.clubdeportivo.base.Bolsa;

/**
 *
 * @author Miguel
 */
public class BolsaDAO extends ObjetoDAO {

    protected String schema(){return "clubdeportivo" + BolsaDAO.getEntorno();}
    protected String nombreTabla(){return TablasDAO.bolsa.name();}
    protected String[] camposTabla(){
        String[] campos = {
            "fecha",
            "valor"};
        return campos;
        }
 
    protected List<Bolsa> getRegistros() throws DAOException {

        String txtsql = "SELECT * FROM " + nombreTabla() + " ORDER BY id";
        
        ArrayList<Bolsa> listaObjs = (ArrayList) getDataObjects(txtsql);

        return listaObjs;
    }
    
    protected List<Bolsa> getRegistros(Date fecha) throws DAOException {

        String txtsql = "SELECT * FROM " + nombreTabla() + " WHERE fecha > STR_TO_DATE(?,'%Y-%m-%d') ORDER BY id";
        
        ArrayList params = new ArrayList();
        params.add(fecha);
        
        ArrayList<Bolsa> listaObjs = (ArrayList) getDataObjects(txtsql, params);

        return listaObjs;
    }

   
    @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        Bolsa obj = new Bolsa();

        obj.setId(retorno.getLong("id"));        
        obj.setFecha(retorno.getTimestamp("fecha"));
        obj.setValor(retorno.getInt("valor"));

        return obj;
    }

    @Override
    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        Bolsa newObj = (Bolsa) obj;
       
        sql.setTimestamp(1,
                new Timestamp(newObj.getFecha().getTime()));
        sql.setInt(2, newObj.getValor());

        // Si es un update asignamos el parametro del id
        if (tipo == TipoSaveDAO.update) sql.setLong(3, newObj.getId());

        return sql;
    }
    
}
