
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
import java.util.List;
import mld.clubdeportivo.base.quinielas.ApuestaQuiniela;
import mld.clubdeportivo.base.quinielas.EquipoQuiniela;
import mld.clubdeportivo.base.quinielas.JornadaQuiniela;
import mld.clubdeportivo.bd.DAOException;
import mld.clubdeportivo.bd.ObjetoDAO;
import mld.clubdeportivo.bd.TablasDAO;
import static mld.clubdeportivo.bd.TablasDAO.apuestasquiniela;
import mld.clubdeportivo.bd.TipoSaveDAO;
import static mld.clubdeportivo.bd.TipoSaveDAO.update;

public class ApuestaQuinielaDAO extends ObjetoDAO{

    protected String schema(){return "quiniela" + getEntorno();}
    protected String nombreTabla() {
        return apuestasquiniela.name();
    }
    protected String[] camposTabla() {
        String[] campos = {
            "jornada",
            "equipo",
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
            "puntos1",
            "puntos2",
            "puntos3",
            "puntos4",
            "puntos5",
            "puntos6",
            "puntos7",
            "puntos8",
            "puntos9",
            "puntos10",
            "puntos11",
            "puntos12",
            "puntos13",
            "puntos14",
            "puntos15",           
            "actualizada",
        };
        return campos;
        }

    protected List <ApuestaQuiniela> getApuestasByJornada(EquipoQuiniela eq,
            JornadaQuiniela jornada) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() +
                " WHERE jornada = ? AND equipo = ? ";
        var params = new ArrayList();
        params.add(jornada.getId());
        params.add(eq.getId());

        ArrayList <ApuestaQuiniela> lista =
                (ArrayList) getDataObjects(txtsql, params);

        return lista;
    }

     protected List <ApuestaQuiniela> getApuestasByJornada(JornadaQuiniela jornada)
             throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() +
                " WHERE jornada = ?";
        var params = new ArrayList();
        params.add(jornada.getId());

        ArrayList <ApuestaQuiniela> lista =
                (ArrayList) getDataObjects(txtsql, params);

        return lista;
    }

    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        var objap = (ApuestaQuiniela) obj;

        sql.setLong(1, objap.getJornada().getId());
        sql.setLong(2, objap.getEquipo().getId());

        for (var i = 3; i < 18; i++){
            if (objap.getResultado() != null)
                sql.setString(i, objap.getResultado()[i-3]);
            else
                sql.setString(i, null);
        }
        for (var i = 18; i < 33; i++){
            if (objap.getPuntos()!= null)
                sql.setInt(i, objap.getPuntos()[i-18]);
            else
                sql.setInt(i, 0);
        }
        
        if (objap.getActualizada() == null)
            sql.setTimestamp(33, null);
        else
            sql.setTimestamp(33,
                    new Timestamp(objap.getActualizada().getTime()));
        
        // Si es un update asignamos el parametro del id
        if (tipo == update) sql.setLong(34, objap.getId());

        return sql;
    }

      @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        var obj = new ApuestaQuiniela();

        obj.setId(retorno.getLong("id"));
        
        var resultado = new String[15];
        for (var i = 0; i < 15; i++){
            resultado[i] = retorno.getString("resultado" + (i + 1));
        }
        obj.setResultado(resultado);
        
        var puntos = new int[15];
        for (var i = 0; i < 15; i++){
            puntos[i] = retorno.getInt("puntos" + (i + 1));
        }
        obj.setPuntos(puntos);        
     
        obj.setActualizada(retorno.getTimestamp("actualizada"));

        return obj;
    }

    protected List<ApuestaQuiniela> getApuestasByEquipo(EquipoQuiniela eq)
            throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() +
                " WHERE equipo = ? ";
        var params = new ArrayList();
        params.add(eq.getId());

        ArrayList <ApuestaQuiniela> lista =
                (ArrayList) getDataObjects(txtsql, params);

        return lista;
    }
}
