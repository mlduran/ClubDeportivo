
package mld.clubdeportivo.bd.quinielas;

/**
 *
 * @author mlopezd
 */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import mld.clubdeportivo.base.Competicion;
import mld.clubdeportivo.base.quinielas.CompeticionQuiniela;
import mld.clubdeportivo.base.quinielas.JornadaQuiniela;
import mld.clubdeportivo.bd.DAOException;
import mld.clubdeportivo.bd.ObjetoDAO;
import mld.clubdeportivo.bd.TablasDAO;
import static mld.clubdeportivo.bd.TablasDAO.jornadasquiniela;
import mld.clubdeportivo.bd.TipoRetornoDAO;
import static mld.clubdeportivo.bd.TipoRetornoDAO.INTEGER;
import mld.clubdeportivo.bd.TipoSaveDAO;
import static mld.clubdeportivo.bd.TipoSaveDAO.update;

public class JornadaQuinielaDAO extends ObjetoDAO{

    protected String schema(){return "quiniela" + getEntorno();}
    protected String nombreTabla() {
        return jornadasquiniela.name();
    }
    protected String[] camposTabla() {
        String[] campos = {
            "numero",
            "descripcion",
            "competicion",
            "puntos",
            "fecha",
            "validada",
            "partido1",
            "partido2",
            "partido3",
            "partido4",
            "partido5",
            "partido6",
            "partido7",
            "partido8",
            "partido9",
            "partido10",
            "partido11",
            "partido12",
            "partido13",
            "partido14",
            "partido15",
            "resultado1",
            "resultado2",
            "resultado3",
            "resultado4",
            "resultado5",
            "resultado6",
            "resultado7",
            "resultado8",
            "resultado9",
            "resultado10",
            "resultado11",
            "resultado12",
            "resultado13",
            "resultado14",
            "resultado15",
            "bloqueada"
        };
        return campos;
        }


    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        var objjor = (JornadaQuiniela) obj;

        sql.setInt(1, objjor.getNumero());
        sql.setString(2, objjor.getDescripcion());
        sql.setLong(3, objjor.getCompeticion().getId());
        sql.setInt(4, objjor.getPuntos());

        if (objjor.getFecha() == null)
            sql.setTimestamp(5, null);
        else
            sql.setTimestamp(5,
                    new Timestamp(objjor.getFecha().getTime()));
        sql.setBoolean(6, objjor.isValidada());

        for (var i = 7; i < 22; i++){
            sql.setString(i, objjor.getPartido()[i-7]);
            if (objjor.getResultado() != null)
                sql.setString(i + 15, objjor.getResultado()[i-7]);
            else
                sql.setString(i + 15, null);
        }

        sql.setBoolean(37, objjor.isBloqueada());
        // Si es un update asignamos el parametro del id
        if (tipo == update) sql.setLong(38, objjor.getId());

        return sql;
    }

    @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        var obj = new JornadaQuiniela();

        obj.setId(retorno.getLong("id"));
        obj.setNumero(retorno.getInt("numero"));
        obj.setDescripcion(retorno.getString("descripcion"));
        obj.setPuntos(retorno.getInt("puntos"));
        obj.setFecha(retorno.getTimestamp("fecha"));
        obj.setValidada(retorno.getBoolean("validada"));
        obj.setBloqueada(retorno.getBoolean("bloqueada"));

        var partido = new String[15];
        var resultado = new String[15];

        for (var i = 0; i < 15; i++){
            partido[i] = retorno.getString("partido" + (i + 1));
            resultado[i] = retorno.getString("resultado" + (i + 1));
        }

        obj.setPartido(partido);
        obj.setResultado(resultado);

        return obj;
    }
    
     protected JornadaQuiniela getJornadaByNumero(Competicion comp, int numero)
             throws DAOException {

         var txtsql = "SELECT * FROM " + nombreTabla() + 
                 " WHERE competicion = ? AND numero = ?";
         var params = new ArrayList();
         params.add(comp.getId());
         params.add(numero);

         var obj =
                (JornadaQuiniela) getDataObject(txtsql, params);

        return obj;

    }

     protected ArrayList<JornadaQuiniela> getJornadasNoValidadas(
             CompeticionQuiniela comp) throws DAOException {

         var txtsql = "SELECT * FROM " + nombreTabla() +
                 " WHERE validada = false AND competicion = ?";
         var params = new ArrayList();
         params.add(comp.getId());

         ArrayList<JornadaQuiniela> objs =
                (ArrayList) getDataObjects(txtsql, params);

        return objs;

    }

     protected ArrayList<JornadaQuiniela> getJornadasValidadas(
             CompeticionQuiniela comp) throws DAOException {

         var txtsql = "SELECT * FROM " + nombreTabla() +
                 " WHERE validada = true AND competicion = ?";
         var params = new ArrayList();
         params.add(comp.getId());

         ArrayList<JornadaQuiniela> objs =
                (ArrayList) getDataObjects(txtsql, params);

        return objs;

    }
     
      protected int getNumeroJornadasValidadas(
             CompeticionQuiniela comp) throws DAOException {

         var txtsql = "SELECT COUNT(*) FROM " + nombreTabla() +
                 " WHERE validada = true AND competicion = ?";
         var params = new ArrayList();
         params.add(comp.getId());

        var num = (int) queryNumerica(txtsql, INTEGER, params);

        return num;

    }

    protected int getProximaJornada() throws DAOException {

        var n = 0;
        var txtsql = "SELECT * FROM " + nombreTabla() +
                " WHERE validada = false" +
                " ORDER BY numero";
        var obj =
                (JornadaQuiniela) getDataObject(txtsql);

        if (obj != null) n = obj.getNumero();

        return n;

    }
    
    protected int getUltimaJornadaValidada() throws DAOException {

        var n = 0;
        var txtsql = "SELECT * FROM " + nombreTabla() +
                " WHERE validada = true" +
                " ORDER BY numero";
        var obj =
                (JornadaQuiniela) getDataObject(txtsql);

        if (obj != null) n = obj.getNumero();

        return n;

    }

}
