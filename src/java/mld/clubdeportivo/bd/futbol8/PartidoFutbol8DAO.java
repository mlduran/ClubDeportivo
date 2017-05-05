
package mld.clubdeportivo.bd.futbol8;

/**
 *
 * @author mlopezd
 */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import mld.clubdeportivo.base.Equipo;
import mld.clubdeportivo.base.futbol8.EquipoFutbol8;
import mld.clubdeportivo.base.futbol8.JornadaFutbol8;
import mld.clubdeportivo.base.futbol8.PartidoFutbol8;
import mld.clubdeportivo.bd.*;

public class PartidoFutbol8DAO extends ObjetoDAO{

    protected String schema(){return "futbol8" + PartidoFutbol8DAO.getEntorno();}
    protected String nombreTabla() {
        return TablasDAO.partidosfutbol8.name();
    }
    protected String[] camposTabla() {
        String[] campos = {
            "jornada",
            "eqlocal",
            "eqvisitante",
            "goleslocal",
            "golesvisitante",
            "espectadores",
            "precioentradas",
            "descripcion"
        };
        return campos;
        }


    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        PartidoFutbol8 objpar = (PartidoFutbol8) obj;

        sql.setLong(1, objpar.getJornada().getId());
        if (objpar.getEqLocal() != null)
            sql.setLong(2, objpar.getEqLocal().getId());
        else
            sql.setNull(2, java.sql.Types.NULL);
        if (objpar.getEqVisitante() != null)
            sql.setLong(3, objpar.getEqVisitante().getId());
        else
            sql.setNull(3, java.sql.Types.NULL);
        sql.setInt(4, objpar.getGolesLocal());
        sql.setInt(5, objpar.getGolesVisitante());
        
        sql.setInt(6, objpar.getEspectadores());
        sql.setInt(7, objpar.getPrecioEntradas());
        sql.setString(8, objpar.getDescripcion());
        
        // Si es un update asignamos el parametro del id
        if (tipo == TipoSaveDAO.update) sql.setLong(9, objpar.getId());

        return sql;
    }

    @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        PartidoFutbol8 obj = new PartidoFutbol8();

        obj.setId(retorno.getLong("id"));
        obj.setGolesLocal(retorno.getInt("goleslocal"));
        obj.setGolesVisitante(retorno.getInt("golesvisitante"));
        obj.setEspectadores(retorno.getInt("espectadores"));
        obj.setPrecioEntradas(retorno.getInt("precioentradas"));
        obj.setDescripcion(retorno.getString("descripcion"));

        return obj;
    }

    protected JornadaFutbol8 obtenerPartidos(
            JornadaFutbol8 jor, HashMap mapeoEqs) throws DAOException{

        String txtsql = "SELECT * FROM " + nombreTabla() +
                "  WHERE jornada = " + jor.getId();
        String[] campos = {"id", "eqlocal", "eqvisitante",
            "goleslocal", "golesvisitante","espectadores","precioentradas","descripcion"};
        ArrayList<HashMap> mapeoPartidos = queryRegistros(txtsql, campos);

        jor.setPartidos(new ArrayList<PartidoFutbol8>());

        for (HashMap mapeo : mapeoPartidos) {
            PartidoFutbol8 partido = new PartidoFutbol8();
            partido.setId((Long) mapeo.get("id"));
            partido.setEqLocal((Equipo) mapeoEqs.get(mapeo.get("eqlocal")));
            partido.setEqVisitante((Equipo) mapeoEqs.get(mapeo.get("eqvisitante")));
            partido.setGolesLocal((Integer) mapeo.get("goleslocal"));
            partido.setGolesVisitante((Integer) mapeo.get("golesvisitante"));
            partido.setEspectadores((Integer) mapeo.get("espectadores"));
            partido.setPrecioEntradas((Integer) mapeo.get("precioentradas"));
            partido.setDescripcion((String) mapeo.get("descripcion"));

            partido.setJornada(jor);
            jor.getPartidos().add(partido);
        }

        return jor;

    }

    protected long idEquipoLocalPartido(PartidoFutbol8 partido) throws DAOException{

        String id = String.valueOf(partido.getId());
        String txtsql = "SELECT eqlocal FROM " + nombreTabla() +
                " WHERE id = ".concat(id);

        long idobj = (long) queryNumerica(txtsql, TipoRetornoDAO.BIGINT);

        return idobj;

    }

     protected long idEquipoVisitantelPartido(PartidoFutbol8 partido) throws DAOException{

        String id = String.valueOf(partido.getId());
        String txtsql = "SELECT eqvisitante FROM " + nombreTabla() +
                " WHERE id = ".concat(id);

        long idobj = (long) queryNumerica(txtsql, TipoRetornoDAO.BIGINT);

        return idobj;

    }

     protected PartidoFutbol8 getPartido(JornadaFutbol8 jor,
             EquipoFutbol8 eq) throws DAOException {

        String txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE jornada = ? "
                + "AND (eqlocal = ? OR eqvisitante = ?)";

        ArrayList params = new ArrayList();
        params.add(jor.getId());
        params.add(eq.getId());
        params.add(eq.getId());

        PartidoFutbol8 obj = (PartidoFutbol8) getDataObject(txtsql, params);

        return obj;
    }
     
     protected ArrayList<PartidoFutbol8> getPartidos(JornadaFutbol8 jor
             ) throws DAOException {

        String txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE jornada = ? ";

        ArrayList params = new ArrayList();
        params.add(jor.getId());

        ArrayList<PartidoFutbol8> objs = (ArrayList) getDataObjects(txtsql, params);

        return objs;
    }
     
     protected PartidoFutbol8 getUltimoPartido(EquipoFutbol8 eq) throws DAOException{
      
       
         String txtsql = "SELECT partidosfutbol8.* FROM partidosfutbol8, jornadasfutbol8" +
                 " WHERE partidosfutbol8.jornada = jornadasfutbol8.id AND " +
                 "jornadasfutbol8.fecha IS NOT NULL AND " +
                 "(partidosfutbol8.eqlocal = ?" + 
                 " OR partidosfutbol8.eqvisitante = ?)" + 
                 " ORDER BY jornadasfutbol8.fecha DESC LIMIT 1";
         
         ArrayList params = new ArrayList();
         params.add(eq.getId());
         params.add(eq.getId());
         
         PartidoFutbol8 obj = (PartidoFutbol8) getDataObject(txtsql, params);       
         
         return obj;
     }
     
     protected long getIdEqLocal(PartidoFutbol8 partido) throws DAOException{
         
         String id = String.valueOf(partido.getId());
         String txtsql = "SELECT eqlocal FROM " +
                 nombreTabla() + " WHERE id = ".concat(id);

         long idobj = (long) queryNumerica(txtsql, TipoRetornoDAO.BIGINT);

         return idobj;         
         
     }
     
     protected long getIdEqVisitante(PartidoFutbol8 partido) throws DAOException{
         
         String id = String.valueOf(partido.getId());
         String txtsql = "SELECT eqvisitante FROM " +
                 nombreTabla() + " WHERE id = ".concat(id);

         long idobj = (long) queryNumerica(txtsql, TipoRetornoDAO.BIGINT);

         return idobj;         
         
     }
     
     protected long getIdJornada(PartidoFutbol8 partido) throws DAOException{
         
         String id = String.valueOf(partido.getId());
         String txtsql = "SELECT jornada FROM " +
                 nombreTabla() + " WHERE id = ".concat(id);

         long idobj = (long) queryNumerica(txtsql, TipoRetornoDAO.BIGINT);

         return idobj;         
         
     }

}
