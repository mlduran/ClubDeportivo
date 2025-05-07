package mld.clubdeportivo.bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import mld.clubdeportivo.base.Club;
import mld.clubdeportivo.base.Comentario;
import mld.clubdeportivo.base.Grupo;
import static mld.clubdeportivo.bd.TablasDAO.comentarios;
import static mld.clubdeportivo.bd.TipoSaveDAO.update;


public class ComentarioDAO extends ObjetoDAO {

    protected String schema(){return "clubdeportivo" + getEntorno();}
    protected String nombreTabla() {
        return comentarios.name();       
    }
    protected String[] camposTabla() {
        String[] campos = {
            "grupo",
            "club",
            "fecha",
            "comentario",
            "general"
            };
        return campos;
        }

    protected Comentario getComentarioById(long id) throws DAOException {

       return (Comentario) getObjetoById(id);
    }


    protected List<Comentario> getComentariosGenerales(int num) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE general IS true "
                + " ORDER BY fecha DESC";

        ArrayList<Comentario> listaObjs;
        
        if (num > 0){
            txtsql = txtsql.concat(" LIMIT ?");
            var params = new ArrayList();
            params.add(num);
            listaObjs = (ArrayList) getDataObjects(txtsql, params);
        }
        else
            listaObjs = (ArrayList) getDataObjects(txtsql); 

        return listaObjs;
    }
    
    protected List<Comentario> getComentariosGenerales() throws DAOException {

        return getComentariosGenerales(0);
        
    }

    protected List<Comentario> getComentarios(Grupo grp, int num) 
            throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE grupo = ? "
                + "ORDER BY fecha DESC";
        
        if (num > 0)
            txtsql = txtsql.concat(" LIMIT ?");

        var params = new ArrayList();
        params.add(grp.getId());
        
        if (num > 0)
            params.add(num);

        ArrayList<Comentario> listaObjs = (ArrayList) getDataObjects(txtsql, params);

        return listaObjs;
    }
    
    protected List<Comentario> getComentarios(Grupo grp) 
            throws DAOException {
        
        return getComentarios(grp, 0);
    }
    

    @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        var obj = new Comentario();

        obj.setId(retorno.getLong("id"));
        obj.setClub(retorno.getString("club"));
        obj.setFecha(retorno.getTimestamp("fecha"));
        obj.setComentario(retorno.getString("comentario"));
        obj.setGeneral(retorno.getBoolean("general"));


        return obj;
    }

    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        var objNot = (Comentario) obj;

        sql.setLong(1, objNot.getGrupo().getId());
        sql.setString(2, objNot.getClub());

        sql.setTimestamp(3,
                new Timestamp(objNot.getFecha().getTime()));      
        sql.setString(4, objNot.getComentario());
        
        sql.setBoolean(5, objNot.isGeneral());
       
        // Si es un update asignamos el parametro del id
        if (tipo == update) sql.setLong(6, objNot.getId());

        return sql;
    }

    
    protected void eliminarComentarios(Club club) throws DAOException{
        
        var txtsql = "DELETE FROM " +
                    nombreTabla() + " WHERE club = " + club.getId();
        deleteObjects(txtsql);        
        
    }


}