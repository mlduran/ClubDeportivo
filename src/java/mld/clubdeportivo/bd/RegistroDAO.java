
package mld.clubdeportivo.bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import mld.clubdeportivo.base.Registro;
import mld.clubdeportivo.base.TipoRegistro;

/**
 *
 * @author Miguel
 */
public class RegistroDAO extends ObjetoDAO {

    protected String schema(){return "clubdeportivo" + RegistroDAO.getEntorno();}
    protected String nombreTabla(){return TablasDAO.registro.name();}
    protected String[] camposTabla(){
        String[] campos = {
            "aplicacion",
            "tipo",
            "ip",
            "usuario",
            "fecha",
            "observaciones"};
        return campos;
        }
 
    protected List<Registro> getRegistros(int num) throws DAOException {

        String txtsql = "SELECT * FROM " + nombreTabla() + " ORDER BY id DESC LIMIT " + num;
        
        ArrayList<Registro> listaObjs = (ArrayList) getDataObjects(txtsql);

        return listaObjs;
    }

   
    @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        Registro obj = new Registro();

        obj.setId(retorno.getLong("id"));
        obj.setAplicacion(retorno.getString("aplicacion"));
        obj.setTipo(TipoRegistro.values()[(retorno.getInt("tipo"))]);
        obj.setIp(retorno.getString("ip"));
        obj.setUsuario(retorno.getString("usuario"));
        obj.setFecha(retorno.getTimestamp("fecha"));
        obj.setObservaciones(retorno.getString("observaciones"));

        return obj;
    }

    @Override
    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        Registro newObj = (Registro) obj;
       
        sql.setString(1, newObj.getAplicacion());
        sql.setInt(2, newObj.getTipo().ordinal());
        sql.setString(3, newObj.getIp());
        sql.setString(4, newObj.getUsuario());
        sql.setTimestamp(5,
                new Timestamp(newObj.getFecha().getTime()));
        sql.setString(6, newObj.getObservaciones());

        // Si es un update asignamos el parametro del id
        if (tipo == TipoSaveDAO.update) sql.setLong(7, newObj.getId());

        return sql;
    }
    
}
