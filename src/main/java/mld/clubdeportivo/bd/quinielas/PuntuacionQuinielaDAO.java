
package mld.clubdeportivo.bd.quinielas;

/**
 *
 * @author mlopezd
 */

import static java.lang.String.valueOf;
import static java.lang.String.valueOf;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import mld.clubdeportivo.base.Competicion;
import mld.clubdeportivo.base.quinielas.EquipoQuiniela;
import mld.clubdeportivo.base.quinielas.PuntuacionQuiniela;
import mld.clubdeportivo.bd.*;
import static mld.clubdeportivo.bd.TablasDAO.puntuacionesquiniela;
import static mld.clubdeportivo.bd.TipoRetornoDAO.BIGINT;
import static mld.clubdeportivo.bd.TipoSaveDAO.update;

public class PuntuacionQuinielaDAO extends ObjetoDAO{

    protected String schema(){return "quiniela" + getEntorno();}
    protected String nombreTabla() {
        return puntuacionesquiniela.name();
    }
    protected String[] camposTabla() {
        String[] campos = {
            "equipo",
            "competicion",
            "puntos",
            "victorias",
            "puntosgeneral",
            "victoriasgeneral"
        };
        return campos;
        }

    protected long idEquipo(PuntuacionQuiniela pt) throws DAOException {

        var id = valueOf(pt.getId());
        var txtsql = "SELECT equipo FROM " + nombreTabla() + "  WHERE id = ".concat(id);
        var idobj = (long) queryNumerica(txtsql, BIGINT);

        return idobj;

    }

    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        var objpun = (PuntuacionQuiniela) obj;

        sql.setLong(1, objpun.getEquipo().getId());
        sql.setLong(2, objpun.getCompeticion().getId());
        sql.setInt(3, objpun.getPuntos());
        sql.setInt(4, objpun.getVictorias());
        sql.setInt(5, objpun.getPuntosGeneral());
        sql.setInt(6, objpun.getVictoriasGeneral());
       
        // Si es un update asignamos el parametro del id
        if (tipo == update) sql.setLong(7, objpun.getId());

        return sql;
    }

    @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        var obj = new PuntuacionQuiniela();

        obj.setId(retorno.getLong("id"));
        obj.setPuntos(retorno.getInt("puntos"));
        obj.setVictorias(retorno.getInt("victorias"));
        obj.setPuntosGeneral(retorno.getInt("puntosgeneral"));
        obj.setVictoriasGeneral(retorno.getInt("victoriasgeneral"));

        return obj;
    }
    
     protected PuntuacionQuiniela getPuntosByEquipo(Competicion comp, EquipoQuiniela eq)
             throws DAOException {

         var txtsql = "SELECT * FROM " + nombreTabla() + 
                 " WHERE competicion = ? AND equipo = ?";
         var params = new ArrayList();
         params.add(comp.getId());
         params.add(eq.getId());

         var obj =
                (PuntuacionQuiniela) getDataObject(txtsql, params);

        return obj;

    }
     
      protected ArrayList<PuntuacionQuiniela> getPuntuacionesByEquipo(EquipoQuiniela eq)
             throws DAOException {

         var txtsql = "SELECT * FROM " + nombreTabla() + 
                 " WHERE equipo = ?";
         var params = new ArrayList();
         params.add(eq.getId());

         ArrayList<PuntuacionQuiniela> obj =
                 (ArrayList) getDataObjects(txtsql, params);

        return obj;

    }
     

     protected ArrayList<PuntuacionQuiniela> getPuntuacionesCompeticion(
             Competicion comp, boolean isGeneral) throws DAOException {

         String txtsql;
         
         if (isGeneral)
             txtsql = "SELECT * FROM " + nombreTabla() +
                 " WHERE competicion = ? ORDER BY puntosgeneral DESC, victoriasgeneral DESC";
         else
             txtsql = "SELECT * FROM " + nombreTabla() +
                 " WHERE competicion = ? ORDER BY puntos DESC, victorias DESC";

         var params = new ArrayList();
         params.add(comp.getId());

         ArrayList<PuntuacionQuiniela> obj =
                (ArrayList) getDataObjects(txtsql, params);

        return obj;

    }
     
     public long idEquipo(long idEquipo) throws DAOException{

        var id = valueOf(idEquipo);
        var txtsql = "SELECT equipo FROM " + nombreTabla() + "  WHERE id = ".concat(id);
        var idobj = (long) queryNumerica(txtsql, BIGINT);

        return idobj;

    }

}
