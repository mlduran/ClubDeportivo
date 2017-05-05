
package mld.clubdeportivo.bd.quinielas;

/**
 *
 * @author mlopezd
 */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import mld.clubdeportivo.base.quinielas.EstadisticaQuiniela;
import mld.clubdeportivo.bd.DAOException;
import mld.clubdeportivo.bd.ObjetoDAO;
import mld.clubdeportivo.bd.TablasDAO;
import mld.clubdeportivo.bd.TipoSaveDAO;

public class EstadisticaQuinielaDAO extends ObjetoDAO{

    protected String schema(){return "quiniela" + EstadisticaQuinielaDAO.getEntorno();}
    protected String nombreTabla() {
        return TablasDAO.estadisticasquiniela.name();
    }
    protected String[] camposTabla() {
        String[] campos = {            
            "equipo",
            "competicion",
            "jornada",
            "puntos",
            "aciertos",
            "puntosgeneral",
            "posicion"
        };
        return campos;
        }

    protected EstadisticaQuiniela getEstadisticaByJornada(String eq,
            String comp, String jornada) throws DAOException {

        String txtsql = "SELECT * FROM " + nombreTabla() +
                " WHERE equipo = ? AND competicion = ? AND jornada = ? ORDER BY puntos DESC";

        ArrayList params = new ArrayList();
        params.add(eq);
        params.add(comp);
        params.add(jornada);        

        EstadisticaQuiniela est =
                (EstadisticaQuiniela) getDataObject(txtsql, params);

        return est;
    }

    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        EstadisticaQuiniela objest = (EstadisticaQuiniela) obj;

        sql.setString(1, objest.getEquipo());
        sql.setString(2, objest.getCompeticion());
        sql.setString(3, objest.getJornada());
        sql.setInt(4, objest.getPuntos());
        sql.setString(5, objest.getAciertos());
        sql.setInt(6, objest.getPuntosGeneral());
        sql.setInt(7, objest.getPosicion());
     
        // Si es un update asignamos el parametro del id
        if (tipo == TipoSaveDAO.update) sql.setLong(8, objest.getId());

        return sql;
    }

    @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        EstadisticaQuiniela obj = new EstadisticaQuiniela();

        obj.setId(retorno.getLong("id"));
        obj.setEquipo(retorno.getString("equipo"));
        obj.setJornada(retorno.getString("jornada"));
        obj.setAciertos(retorno.getString("aciertos"));
        obj.setPuntos(retorno.getInt("puntos"));
        obj.setAciertos(retorno.getString("aciertos"));
        obj.setPuntosGeneral(retorno.getInt("puntosgeneral"));
        obj.setPosicion(retorno.getInt("posicion"));

        return obj;
    }
 
}
