
package mld.clubdeportivo.bd.quinielas;

/**
 *
 * @author mlopezd
 */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import mld.clubdeportivo.base.Competicion;
import mld.clubdeportivo.base.quinielas.EquipoQuiniela;
import mld.clubdeportivo.base.quinielas.PuntuacionQuiniela;
import mld.clubdeportivo.bd.*;

public class PuntuacionQuinielaDAO extends ObjetoDAO{

    protected String schema(){return "quiniela" + PuntuacionQuinielaDAO.getEntorno();}
    protected String nombreTabla() {
        return TablasDAO.puntuacionesquiniela.name();
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

        String id = String.valueOf(pt.getId());
        String txtsql = "SELECT equipo FROM " + nombreTabla() + "  WHERE id = ".concat(id);

        long idobj = (long) queryNumerica(txtsql, TipoRetornoDAO.BIGINT);

        return idobj;

    }

    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        PuntuacionQuiniela objpun = (PuntuacionQuiniela) obj;

        sql.setLong(1, objpun.getEquipo().getId());
        sql.setLong(2, objpun.getCompeticion().getId());
        sql.setInt(3, objpun.getPuntos());
        sql.setInt(4, objpun.getVictorias());
        sql.setInt(5, objpun.getPuntosGeneral());
        sql.setInt(6, objpun.getVictoriasGeneral());
       
        // Si es un update asignamos el parametro del id
        if (tipo == TipoSaveDAO.update) sql.setLong(7, objpun.getId());

        return sql;
    }

    @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        PuntuacionQuiniela obj = new PuntuacionQuiniela();

        obj.setId(retorno.getLong("id"));
        obj.setPuntos(retorno.getInt("puntos"));
        obj.setVictorias(retorno.getInt("victorias"));
        obj.setPuntosGeneral(retorno.getInt("puntosgeneral"));
        obj.setVictoriasGeneral(retorno.getInt("victoriasgeneral"));

        return obj;
    }
    
     protected PuntuacionQuiniela getPuntosByEquipo(Competicion comp, EquipoQuiniela eq)
             throws DAOException {

         String txtsql = "SELECT * FROM " + nombreTabla() + 
                 " WHERE competicion = ? AND equipo = ?";
         
         ArrayList params = new ArrayList();
         params.add(comp.getId());
         params.add(eq.getId());

         PuntuacionQuiniela obj =
                (PuntuacionQuiniela) getDataObject(txtsql, params);

        return obj;

    }
     
      protected ArrayList<PuntuacionQuiniela> getPuntuacionesByEquipo(EquipoQuiniela eq)
             throws DAOException {

         String txtsql = "SELECT * FROM " + nombreTabla() + 
                 " WHERE equipo = ?";
         
         ArrayList params = new ArrayList();
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

         ArrayList params = new ArrayList();
         params.add(comp.getId());

         ArrayList<PuntuacionQuiniela> obj =
                (ArrayList) getDataObjects(txtsql, params);

        return obj;

    }
     
     public long idEquipo(long idEquipo) throws DAOException{

        String id = String.valueOf(idEquipo);
        
        String txtsql = "SELECT equipo FROM " + nombreTabla() + "  WHERE id = ".concat(id);

        long idobj = (long) queryNumerica(txtsql, TipoRetornoDAO.BIGINT);

        return idobj;

    }

}
