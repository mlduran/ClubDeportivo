
package mld.clubdeportivo.bd.futbol8;

/**
 *
 * @author mlopezd
 */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import mld.clubdeportivo.base.futbol8.EstadisticaPartidoFutbol8;
import mld.clubdeportivo.base.futbol8.PartidoFutbol8;
import mld.clubdeportivo.bd.DAOException;
import mld.clubdeportivo.bd.ObjetoDAO;
import mld.clubdeportivo.bd.TablasDAO;
import static mld.clubdeportivo.bd.TablasDAO.estadisticaspartidofutbol8;
import mld.clubdeportivo.bd.TipoSaveDAO;
import static mld.clubdeportivo.bd.TipoSaveDAO.update;

public class EstadisticaPartidoFutbol8DAO extends ObjetoDAO{

    protected String schema(){return "futbol8" + getEntorno();}
    protected String nombreTabla() {
        return estadisticaspartidofutbol8.name();
    }
    protected String[] camposTabla() {
        String[] campos = {            
            "partido",
            "eqlocal",
            "eqvisitante",
            "alineacionlocal",
            "alineacionvisitante",
            "goleslocal",
            "golesvisitante",
            "tacticalocal",
            "tacticavisitante",
            "esfuerzolocal",
            "esfuerzovisitante",
            "estrategialocal",
            "estrategiavisitante",
            "primaslocal",
            "primasvisitante",
            "posesionlocal",
            "posesionvisitante",
            "jugadaslocal",
            "jugadasvisitante",
            "ocasioneslocal",
            "ocasionesvisitante",
            "tirospuertalocal",
            "tirospuertavisitante",
            "faltasdirectaslocal",
            "faltasdirectasvisitante",
            "cornerslocal",
            "cornersvisitante",
            "penaltieslocal",
            "penaltiesvisitante",
            "goleadoreslocal",
            "goleadoresvisitante",
            "tarjetaslocal",
            "tarjetasvisitante",
            "lesionadoslocal",
            "lesionadosvisitante",
            "extraseqlocal",
            "extraseqvisitante",
            "morallocal",
            "moralvisitante",
            "tiroslejanoslocal",
            "tiroslejanosvisitante",
            "victoriaslocal",
            "victoriasvisitante"
        };
        return campos;
        }

    protected EstadisticaPartidoFutbol8 getEstadisticaByPartido(
            PartidoFutbol8 partido) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() +
                " WHERE partido = ?";
        var params = new ArrayList();
        params.add(partido.getId());       

        var est =
                (EstadisticaPartidoFutbol8) getDataObject(txtsql, params);

        return est;
    }

    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        var objest = (EstadisticaPartidoFutbol8) obj;

        sql.setLong(1, objest.getPartido().getId());
        sql.setString(2, objest.getEqLocal());
        sql.setString(3, objest.getEqVisitante());
        sql.setString(4, objest.getAlineacionLocal());
        sql.setString(5, objest.getAlineacionVisitante());        
        sql.setInt(6, objest.getGolesLocal());
        sql.setInt(7, objest.getGolesVisitante());
        sql.setInt(8, objest.getTacticaLocal());
        sql.setInt(9, objest.getTacticaVisitante());
        sql.setString(10, objest.getEsfuerzoLocal());
        sql.setString(11, objest.getEsfuerzoVisitante());
        sql.setString(12, objest.getEstrategiaLocal());
        sql.setString(13, objest.getEstrategiaVisitante());
        sql.setInt(14, objest.getPrimasLocal());
        sql.setInt(15, objest.getPrimasVisitante());
        
        sql.setInt(16, objest.getPosesionLocal());
        sql.setInt(17, objest.getPosesionVisitante());
        sql.setInt(18, objest.getJugadasLocal());
        sql.setInt(19, objest.getJugadasVisitante());
        sql.setInt(20, objest.getOcasionesLocal());
        sql.setInt(21, objest.getOcasionesVisitante());
        sql.setInt(22, objest.getTirosPuertaLocal());
        sql.setInt(23, objest.getTirosPuertaVisitante());
        sql.setInt(24, objest.getFaltasDirectasLocal());
        sql.setInt(25, objest.getFaltasDirectasVisitante());
        sql.setInt(26, objest.getCornersLocal());
        sql.setInt(27, objest.getCornersVisitante());
        sql.setInt(28, objest.getPenaltiesLocal());
        sql.setInt(29, objest.getPenaltiesVisitante());
        sql.setString(30, objest.getGoleadoresLocal());
        sql.setString(31, objest.getGoleadoresVisitante());
        sql.setString(32, objest.getTarjetasLocal());
        sql.setString(33, objest.getTarjetasVisitante());
        sql.setString(34, objest.getLesionadosLocal());
        sql.setString(35, objest.getLesionadosVisitante());
        sql.setString(36, objest.getExtrasEqLocal());
        sql.setString(37, objest.getExtrasEqVisitante());
        sql.setInt(38, objest.getMoralLocal());
        sql.setInt(39, objest.getMoralVisitante());
        sql.setInt(40, objest.getTirosLejanosLocal());
        sql.setInt(41, objest.getTirosLejanosVisitante());
        sql.setInt(42, objest.getVictoriasLocal());
        sql.setInt(43, objest.getVictoriasVisitante());
                
     
        // Si es un update asignamos el parametro del id
        if (tipo == update) sql.setLong(44, objest.getId());

        return sql;
    }

    @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        var obj = new EstadisticaPartidoFutbol8();

        obj.setId(retorno.getLong("id"));
        obj.setEqLocal(retorno.getString("eqlocal"));
        obj.setEqVisitante(retorno.getString("eqvisitante"));
        obj.setAlineacionLocal(retorno.getString("alineacionlocal"));
        obj.setAlineacionVisitante(retorno.getString("alineacionvisitante"));
        obj.setGolesLocal(retorno.getInt("goleslocal"));
        obj.setGolesVisitante(retorno.getInt("golesvisitante"));
        obj.setTacticaLocal(retorno.getInt("tacticalocal"));
        obj.setTacticaVisitante(retorno.getInt("tacticavisitante"));
        obj.setEsfuerzoLocal(retorno.getString("esfuerzolocal"));
        obj.setEsfuerzoVisitante(retorno.getString("esfuerzovisitante"));
        obj.setEstrategiaLocal(retorno.getString("estrategialocal"));
        obj.setEstrategiaVisitante(retorno.getString("estrategiavisitante"));
        obj.setPrimasLocal(retorno.getInt("primaslocal"));
        obj.setPrimasVisitante(retorno.getInt("primasvisitante"));
        
        obj.setPosesionLocal(retorno.getInt("posesionlocal"));
        obj.setPosesionVisitante(retorno.getInt("posesionvisitante"));
        obj.setJugadasLocal(retorno.getInt("jugadaslocal"));
        obj.setJugadasVisitante(retorno.getInt("jugadasvisitante"));
        obj.setOcasionesLocal(retorno.getInt("ocasioneslocal"));
        obj.setOcasionesVisitante(retorno.getInt("ocasionesvisitante"));        
        obj.setTirosPuertaLocal(retorno.getInt("tirospuertalocal"));
        obj.setTirosPuertaVisitante(retorno.getInt("tirospuertavisitante"));
        obj.setTirosLejanosLocal(retorno.getInt("tiroslejanoslocal"));
        obj.setTirosLejanosVisitante(retorno.getInt("tiroslejanosvisitante"));
        obj.setFaltasDirectasLocal(retorno.getInt("faltasdirectaslocal"));
        obj.setFaltasDirectasVisitante(retorno.getInt("faltasdirectasvisitante"));
        obj.setCornersLocal(retorno.getInt("cornerslocal"));
        obj.setCornersVisitante(retorno.getInt("cornersvisitante"));
        obj.setPenaltiesLocal(retorno.getInt("penaltieslocal"));
        obj.setPenaltiesVisitante(retorno.getInt("penaltiesvisitante"));
        obj.setGoleadoresLocal(retorno.getString("goleadoreslocal"));
        obj.setGoleadoresVisitante(retorno.getString("goleadoresvisitante"));
        obj.setTarjetasLocal(retorno.getString("tarjetaslocal"));
        obj.setTarjetasVisitante(retorno.getString("tarjetasvisitante"));
        obj.setLesionadosLocal(retorno.getString("lesionadoslocal"));
        obj.setLesionadosVisitante(retorno.getString("lesionadosvisitante"));
        obj.setExtrasEqLocal(retorno.getString("extraseqlocal"));
        obj.setExtrasEqVisitante(retorno.getString("extraseqvisitante"));
        obj.setMoralLocal(retorno.getInt("morallocal"));
        obj.setMoralVisitante(retorno.getInt("moralvisitante")); 
        obj.setVictoriasLocal(retorno.getInt("victoriaslocal"));
        obj.setVictoriasVisitante(retorno.getInt("victoriasvisitante")); 

        return obj;
    }
 
}
