package mld.clubdeportivo.bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import mld.clubdeportivo.base.Faq;
import static mld.clubdeportivo.bd.TablasDAO.faqs;
import static mld.clubdeportivo.bd.TipoSaveDAO.update;


public class FaqDAO extends ObjetoDAO {

    protected String schema(){return "clubdeportivo" + getEntorno();}
    protected String nombreTabla() {
        return faqs.name();       
    }
    protected String[] camposTabla() {
        String[] campos = {
            "club",
            "fecha",
            "pregunta",
            "respuesta"
            };
        return campos;
        }

    protected Faq getFaqById(long id) throws DAOException {

       return (Faq) getObjetoById(id);
    }

       
    protected List<Faq> getFaqsContestadas() 
            throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + " WHERE respuesta <> '' "
                + "ORDER BY fecha DESC";
      
        ArrayList<Faq> listaObjs = (ArrayList) getDataObjects(txtsql);

        return listaObjs;
    }
    
    protected List<Faq> getFaqsNoContestadas() 
            throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + " WHERE respuesta = '' "
                + "ORDER BY fecha DESC";
      
        ArrayList<Faq> listaObjs = (ArrayList) getDataObjects(txtsql);

        return listaObjs;
    }
    

    @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        var obj = new Faq();

        obj.setId(retorno.getLong("id"));
        obj.setIdClub(retorno.getLong("club"));
        obj.setFecha(retorno.getTimestamp("fecha"));
        obj.setPregunta(retorno.getString("pregunta"));
        obj.setRespuesta(retorno.getString("respuesta"));

        return obj;
    }

    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        var objFaq = (Faq) obj;

        sql.setLong(1, objFaq.getClub().getId());        
        sql.setTimestamp(2,
                new Timestamp(objFaq.getFecha().getTime()));      
        sql.setString(3, objFaq.getPregunta());
        sql.setString(4, objFaq.getRespuesta()); 
       
        // Si es un update asignamos el parametro del id
        if (tipo == update) sql.setLong(5, objFaq.getId());

        return sql;
    }



}