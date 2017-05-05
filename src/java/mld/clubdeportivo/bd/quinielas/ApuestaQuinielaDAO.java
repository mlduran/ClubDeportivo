
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
import mld.clubdeportivo.bd.TipoSaveDAO;

public class ApuestaQuinielaDAO extends ObjetoDAO{

    protected String schema(){return "quiniela" + this.getEntorno();}
    protected String nombreTabla() {
        return TablasDAO.apuestasquiniela.name();
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
            "actualizada"
        };
        return campos;
        }

    protected List <ApuestaQuiniela> getApuestasByJornada(EquipoQuiniela eq,
            JornadaQuiniela jornada) throws DAOException {

        String txtsql = "SELECT * FROM " + nombreTabla() +
                " WHERE jornada = ? AND equipo = ? ";

        ArrayList params = new ArrayList();
        params.add(jornada.getId());
        params.add(eq.getId());

        ArrayList <ApuestaQuiniela> lista =
                (ArrayList) getDataObjects(txtsql, params);

        return lista;
    }

     protected List <ApuestaQuiniela> getApuestasByJornada(JornadaQuiniela jornada)
             throws DAOException {

        String txtsql = "SELECT * FROM " + nombreTabla() +
                " WHERE jornada = ?";

        ArrayList params = new ArrayList();
        params.add(jornada.getId());

        ArrayList <ApuestaQuiniela> lista =
                (ArrayList) getDataObjects(txtsql, params);

        return lista;
    }

    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        ApuestaQuiniela objap = (ApuestaQuiniela) obj;

        sql.setLong(1, objap.getJornada().getId());
        sql.setLong(2, objap.getEquipo().getId());

        for (int i = 3; i < 18; i++){
            if (objap.getResultado() != null)
                sql.setString(i, objap.getResultado()[i-3]);
            else
                sql.setString(i, null);
        }

        if (objap.getActualizada() == null)
            sql.setTimestamp(18, null);
        else
            sql.setTimestamp(18,
                    new Timestamp(objap.getActualizada().getTime()));

        // Si es un update asignamos el parametro del id
        if (tipo == TipoSaveDAO.update) sql.setLong(19, objap.getId());

        return sql;
    }

      @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        ApuestaQuiniela obj = new ApuestaQuiniela();

        obj.setId(retorno.getLong("id"));
        
        String[] resultado = new String[15];

        for (int i = 0; i < 15; i++){
            resultado[i] = retorno.getString("resultado" + (i + 1));
        }

        obj.setResultado(resultado);

        obj.setActualizada(retorno.getTimestamp("actualizada"));

        return obj;
    }

    protected List<ApuestaQuiniela> getApuestasByEquipo(EquipoQuiniela eq)
            throws DAOException {

        String txtsql = "SELECT * FROM " + nombreTabla() +
                " WHERE equipo = ? ";

        ArrayList params = new ArrayList();
        params.add(eq.getId());

        ArrayList <ApuestaQuiniela> lista =
                (ArrayList) getDataObjects(txtsql, params);

        return lista;
    }
}
