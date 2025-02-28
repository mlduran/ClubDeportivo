package mld.clubdeportivo.bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mld.clubdeportivo.base.Club;
import mld.clubdeportivo.base.Grupo;
import static mld.clubdeportivo.bd.TablasDAO.grupos;
import static mld.clubdeportivo.bd.TipoRetornoDAO.INTEGER;
import static mld.clubdeportivo.bd.TipoSaveDAO.update;
import mld.clubdeportivo.utilidades.UtilGenericas;
import static mld.clubdeportivo.utilidades.UtilGenericas.isGrupo;


/**
 *
 * @author java1
 */
public class GrupoDAO extends ObjetoDAO {

    protected String schema(){return "clubdeportivo" + getEntorno();}
    protected String nombreTabla() {
        return grupos.name();
    }
    protected String[] camposTabla() {
        String[] campos = {
            "activo",
            "completo",
            "generico",
            "maxEquipos",
            "nombre",
            "privado",
            "codigo",
            "ip"
        };
        return campos;
        }
    
    public String getNombreTabla(){
        return schema() + "." + nombreTabla();
    }

    protected Grupo getGrupoById(long id) throws DAOException {

       return (Grupo) getObjetoById(id);
    }

    protected Grupo getGrupoByNombre(String nombre) throws DAOException {

        if (!isGrupo(nombre))
            return null;

        var txtsql = "SELECT * FROM " + nombreTabla() + " WHERE nombre = ?";
        var obj = (Grupo) getDataObject(txtsql, nombre);

        return obj;

    }
    
    protected Grupo getGrupoByIp(String ip) throws DAOException {
        
        var txtsql = "SELECT * FROM " + nombreTabla() + " WHERE ip = ?";
        var obj = (Grupo) getDataObject(txtsql, ip);

        return obj;

    }

    protected List<Grupo> getGrupos(boolean activo) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE activo = ?";
        var params = new ArrayList();
        params.add(activo);

        ArrayList<Grupo> listaObjs = (ArrayList) getDataObjects(txtsql, params);

        return listaObjs;
    }

     protected List<Grupo> getGruposDisponibles() throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE activo = true AND"
                + " completo = false";

        ArrayList<Grupo> listaObjs = (ArrayList) getDataObjects(txtsql);

        return listaObjs;
    }

    protected List<Grupo> getGrupos(boolean activo, int offset, int count)
            throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE activo = ? "
                + "WHERE active = ? LIMIT ?,?";
        var params = new ArrayList();
        params.add(activo);
        params.add(offset);
        params.add(count);

        ArrayList<Grupo> listaObjs = (ArrayList) getDataObjects(txtsql, params);

        return listaObjs;
    }

    protected List<Club> getClubs(Grupo grp)
            throws DAOException {

        var txtsql = "SELECT * FROM clubs WHERE "
                + "WHERE grupo = " + grp.getId();

        ArrayList<Club> listaObjs = (ArrayList) getDataObjects(txtsql);

        return listaObjs;
    }
  
    protected int getNumGrupos() throws DAOException {

         var txtsql = "SELECT COUNT(*) FROM " + nombreTabla();
         return (int) queryNumerica(txtsql, INTEGER);
    }

    protected int getNumGrupos(boolean active) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

      

    @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        var obj = new Grupo(
                retorno.getString("nombre"),
                retorno.getInt("maxEquipos"),
                retorno.getBoolean("privado"),
                retorno.getBoolean("generico"),
                new ArrayList());

        obj.setId(retorno.getLong("id"));
        obj.setActivo(retorno.getBoolean("activo"));
        obj.setCompleto(retorno.getBoolean("completo"));
        obj.setCodigo(retorno.getInt("codigo"));
        obj.setIp(retorno.getString("ip"));

        return obj;
    }

    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        var objGrupo = (Grupo) obj;

        sql.setBoolean(1, objGrupo.isActivo());
        sql.setBoolean(2, objGrupo.isCompleto());
        sql.setBoolean(3, objGrupo.isGenerico());
        sql.setInt(4, objGrupo.getMaxEquipos());
        sql.setString(5, objGrupo.getNombre());
        sql.setBoolean(6, objGrupo.isPrivado());
        sql.setInt(7, objGrupo.getCodigo());
        sql.setString(8, objGrupo.getIp());

        // Si es un update asignamos el parametro del id
        if (tipo == update) sql.setLong(9, objGrupo.getId());

        return sql;
    }

   
  
}
