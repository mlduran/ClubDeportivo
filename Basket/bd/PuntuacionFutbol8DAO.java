
package mld.clubdeportivo.bd.futbol8;

/**
 *
 * @author mlopezd
 */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import mld.clubdeportivo.base.Competicion;
import mld.clubdeportivo.base.futbol8.EquipoFutbol8;
import mld.clubdeportivo.base.futbol8.PuntuacionFutbol8;
import mld.clubdeportivo.bd.*;

public class PuntuacionFutbol8DAO extends ObjetoDAO{

    protected String schema(){return "futbol8" + PuntuacionFutbol8DAO.getEntorno();}
    protected String nombreTabla() {
        return TablasDAO.puntuacionesfutbol8.name();
    }
    protected String[] camposTabla() {
        String[] campos = {
            "equipo",
            "competicion",
            "puntos",
            "victorias",
            "empates",
            "derrotas",
            "golesfavor",
            "golescontra",
            "club"
        };
        return campos;
        }

    protected long idEquipo(PuntuacionFutbol8 pt) throws DAOException {

        String id = String.valueOf(pt.getId());
        String txtsql = "SELECT equipo FROM " + nombreTabla() + "  WHERE id = ".concat(id);

        long idobj = (long) queryNumerica(txtsql, TipoRetornoDAO.BIGINT);

        return idobj;

    }

    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        PuntuacionFutbol8 objpun = (PuntuacionFutbol8) obj;

        sql.setLong(1, objpun.getEquipo().getId());
        sql.setLong(2, objpun.getCompeticion().getId());
        sql.setInt(3, objpun.getPuntos());
        sql.setInt(4, objpun.getVictorias());
        sql.setInt(5, objpun.getEmpates());
        sql.setInt(6, objpun.getDerrotas());
        sql.setInt(7, objpun.getGolesFavor());
        sql.setInt(8, objpun.getGolesContra());
        sql.setString(9, objpun.getClub());
       
        // Si es un update asignamos el parametro del id
        if (tipo == TipoSaveDAO.update) sql.setLong(10, objpun.getId());

        return sql;
    }

    @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        PuntuacionFutbol8 obj = new PuntuacionFutbol8();

        obj.setId(retorno.getLong("id"));
        obj.setPuntos(retorno.getInt("puntos"));
        obj.setVictorias(retorno.getInt("victorias"));
        obj.setEmpates(retorno.getInt("empates"));
        obj.setDerrotas(retorno.getInt("derrotas"));
        obj.setGolesFavor(retorno.getInt("golesfavor"));
        obj.setGolesContra(retorno.getInt("golescontra"));
        obj.setClub(retorno.getString("club"));

        return obj;
    }
    
     protected PuntuacionFutbol8 getPuntosByEquipo(Competicion comp, EquipoFutbol8 eq)
             throws DAOException {

         String txtsql = "SELECT * FROM " + nombreTabla() + 
                 " WHERE competicion = ? AND equipo = ?";
         
         ArrayList params = new ArrayList();
         params.add(comp.getId());
         params.add(eq.getId());

         PuntuacionFutbol8 obj =
                (PuntuacionFutbol8) getDataObject(txtsql, params);

        return obj;

    }

     protected ArrayList<PuntuacionFutbol8> getPuntuacionesCompeticion(Competicion comp)
             throws DAOException {

         String txtsql = "SELECT * FROM " + nombreTabla() +
                 " WHERE competicion = ? ORDER BY puntos DESC, "
                 + "golesfavor DESC, victorias DESC";

         ArrayList params = new ArrayList();
         params.add(comp.getId());

         ArrayList<PuntuacionFutbol8> obj =
                (ArrayList) getDataObjects(txtsql, params);

        return obj;

    }

}
