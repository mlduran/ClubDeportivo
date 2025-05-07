package mld.clubdeportivo.bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import static java.sql.Types.NULL;
import java.util.ArrayList;
import java.util.List;
import mld.clubdeportivo.base.Deporte;
import static mld.clubdeportivo.base.Deporte.values;
import mld.clubdeportivo.base.Grupo;
import mld.clubdeportivo.base.Noticia;
import static mld.clubdeportivo.bd.TablasDAO.noticias;
import static mld.clubdeportivo.bd.TipoSaveDAO.update;


public class NoticiaDAO extends ObjetoDAO {

    protected String schema(){return "clubdeportivo" + getEntorno();}
    protected String nombreTabla() {
        return noticias.name();       
    }
    protected String[] camposTabla() {
        String[] campos = {
            "grupo",
            "deporte",
            "fecha",
            "noticia",
            "general"
            };
        return campos;
        }

    protected Noticia getNoticiaById(long id) throws DAOException {

       return (Noticia) getObjetoById(id);
    }


    protected List<Noticia> getNoticiasGenerales(Grupo grp) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE grupo = ? "
                + "AND deporte is NULL ORDER BY id DESC";
        var params = new ArrayList();
        params.add(grp.getId());

        ArrayList<Noticia> listaObjs = (ArrayList) getDataObjects(txtsql, params);

        return listaObjs;
    }

    protected List<Noticia> getNoticias(Grupo grp, Deporte deporte, int num) 
            throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE grupo = ? "
                + "AND deporte = ? ORDER BY id DESC LIMIT ?";
        var params = new ArrayList();
        params.add(grp.getId());
        params.add(deporte.ordinal());
        params.add(num);

        ArrayList<Noticia> listaObjs = (ArrayList) getDataObjects(txtsql, params);

        return listaObjs;
    }
    
    protected List<Noticia> getNoticias(Grupo grp) 
            throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE grupo = ?";
        var params = new ArrayList();
        params.add(grp.getId());

        ArrayList<Noticia> listaObjs = (ArrayList) getDataObjects(txtsql, params);

        return listaObjs;
    }
    

    @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        var obj = new Noticia();

        obj.setId(retorno.getLong("id"));
        obj.setDeporte(values()[(retorno.getInt("deporte"))]);
        obj.setFecha(retorno.getTimestamp("fecha"));
        obj.setNoticia(retorno.getString("noticia"));
        obj.setGeneral(retorno.getBoolean("general"));


        return obj;
    }

    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        var objNot = (Noticia) obj;

        sql.setLong(1, objNot.getGrupo().getId());
        if (objNot.getDeporte() != null)
            sql.setInt(2, objNot.getDeporte().ordinal());
        else 
            sql.setNull(2, NULL);
        sql.setTimestamp(3,
                new Timestamp(objNot.getFecha().getTime()));      
        sql.setString(4, objNot.getNoticia());
        
        sql.setBoolean(5, objNot.isGeneral());
       
        // Si es un update asignamos el parametro del id
        if (tipo == update) sql.setLong(6, objNot.getId());

        return sql;
    }



}