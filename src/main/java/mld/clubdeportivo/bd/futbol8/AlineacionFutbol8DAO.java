
package mld.clubdeportivo.bd.futbol8;

/**
 *
 * @author mlopezd
 */

import static java.lang.String.valueOf;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static java.sql.Types.NULL;
import java.util.ArrayList;
import java.util.HashMap;
import mld.clubdeportivo.base.futbol8.*;
import static mld.clubdeportivo.base.futbol8.EsfuerzoFutbol8.values;
import static mld.clubdeportivo.base.futbol8.EstrategiaFutbol8.values;
import static mld.clubdeportivo.base.futbol8.TacticaFutbol8.tacticaFutbol8;
import mld.clubdeportivo.bd.*;
import static mld.clubdeportivo.bd.TablasDAO.alineacionesfutbol8;
import static mld.clubdeportivo.bd.TipoRetornoDAO.INTEGER;
import static mld.clubdeportivo.bd.TipoSaveDAO.update;


public class AlineacionFutbol8DAO extends ObjetoDAO{
    
    protected String schema(){return "futbol8" + getEntorno();}

    protected String nombreTabla() {
        return alineacionesfutbol8.name();
    }
    protected String[] camposTabla() {
        String[] campos = {
            "equipo",
            "partido",
            "portero",
            "cuadrantep",
            "jugador1",
            "cuadrante1",
            "jugador2",
            "cuadrante2",
            "jugador3",
            "cuadrante3",
            "jugador4",
            "cuadrante4",
            "jugador5",
            "cuadrante5",
            "jugador6",
            "cuadrante6",
            "jugador7",
            "cuadrante7",
            "tactica",
            "primas",
            "esfuerzo",
            "estrategia"
        };
        return campos;
        }

    
   
    protected AlineacionFutbol8 getByEquipo(EquipoFutbol8 eq,
            PartidoFutbol8 partido) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() +
                " WHERE partido = ? AND equipo = ? ";
        var params = new ArrayList();
        params.add(partido.getId());
        params.add(eq.getId());

        var obj = (AlineacionFutbol8) getDataObject(txtsql, params);

        return obj;
    }

    protected ArrayList<AlineacionFutbol8> getAlineaciones(PartidoFutbol8 partido
             ) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE partido = ? ";
        var params = new ArrayList();
        params.add(partido.getId());

        ArrayList<AlineacionFutbol8> objs = (ArrayList) getDataObjects(txtsql, params);

        return objs;
    }
    
    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        var objal = (AlineacionFutbol8) obj;

        sql.setLong(1, objal.getEquipo().getId());
        sql.setLong(2, objal.getPartido().getId());

        if (objal.getPortero() != null)
            sql.setLong(3, objal.getPortero().getId());
        else
            sql.setNull(3, NULL);
        if (objal.getCuadrantep() != null)
            sql.setString(4, objal.getCuadrantep());
        else
            sql.setNull(4, NULL);

        if (objal.getJugador1() != null)
            sql.setLong(5, objal.getJugador1().getId());
        else
            sql.setNull(5, NULL);
        if (objal.getCuadrante1() != null)
            sql.setString(6, objal.getCuadrante1());
        else
            sql.setNull(6, NULL);

        if (objal.getJugador2() != null)
            sql.setLong(7, objal.getJugador2().getId());
        else
            sql.setNull(7, NULL);
        if (objal.getCuadrante2() != null)
            sql.setString(8, objal.getCuadrante2());
        else
            sql.setNull(8, NULL);

         if (objal.getJugador3() != null)
            sql.setLong(9, objal.getJugador3().getId());
        else
            sql.setNull(9, NULL);
        if (objal.getCuadrante3() != null)
            sql.setString(10, objal.getCuadrante3());
        else
            sql.setNull(10, NULL);

         if (objal.getJugador4() != null)
            sql.setLong(11, objal.getJugador4().getId());
        else
            sql.setNull(11, NULL);
        if (objal.getCuadrante4() != null)
            sql.setString(12, objal.getCuadrante4());
        else
            sql.setNull(12, NULL);

         if (objal.getJugador5() != null)
            sql.setLong(13, objal.getJugador5().getId());
        else
            sql.setNull(13, NULL);
        if (objal.getCuadrante5() != null)
            sql.setString(14, objal.getCuadrante5());
        else
            sql.setNull(14, NULL);

         if (objal.getJugador6() != null)
            sql.setLong(15, objal.getJugador6().getId());
        else
            sql.setNull(15, NULL);
        if (objal.getCuadrante6() != null)
            sql.setString(16, objal.getCuadrante6());
        else
            sql.setNull(16, NULL);

         if (objal.getJugador7() != null)
            sql.setLong(17, objal.getJugador7().getId());
        else
            sql.setNull(17, NULL);
        if (objal.getCuadrante7() != null)
            sql.setString(18, objal.getCuadrante7());
        else
            sql.setNull(18, NULL);
        if (objal.getTactica() != null)
            sql.setLong(19, objal.getTactica().getNumero());
        else
            sql.setNull(19, NULL);

        sql.setInt(20, objal.getPrimas());
        sql.setInt(21, objal.getEsfuerzo().ordinal());
        sql.setInt(22, objal.getEstrategia().ordinal());

        // Si es un update asignamos el parametro del id
        if (tipo == update) sql.setLong(23, objal.getId());

        return sql;
    }

    @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        var obj = new AlineacionFutbol8();

        obj.setId(retorno.getLong("id"));
        obj.setTactica(tacticaFutbol8(retorno.getInt("tactica")));
        obj.setCuadrantep(retorno.getString("cuadrantep"));
        obj.setCuadrante1(retorno.getString("cuadrante1"));
        obj.setCuadrante2(retorno.getString("cuadrante2"));
        obj.setCuadrante3(retorno.getString("cuadrante3"));
        obj.setCuadrante4(retorno.getString("cuadrante4"));
        obj.setCuadrante5(retorno.getString("cuadrante5"));
        obj.setCuadrante6(retorno.getString("cuadrante6"));
        obj.setCuadrante7(retorno.getString("cuadrante7"));
        obj.setPrimas(retorno.getInt("primas"));
        obj.setEsfuerzo(EsfuerzoFutbol8.values()[retorno.getInt("esfuerzo")]);
        obj.setEstrategia(EstrategiaFutbol8.values()[retorno.getInt("estrategia")]);

        return obj;
    }

    protected int numTactica(AlineacionFutbol8 ali) throws DAOException{

        var id = valueOf(ali.getId());
        var txtsql = "SELECT tactica FROM " + nombreTabla() +
                " WHERE id = ".concat(id);
        var idobj = (int) queryNumerica(txtsql, INTEGER);

        return idobj;

    }
    protected HashMap<String,Long> idsJugadores(EquipoFutbol8 eq,
            PartidoFutbol8 partido) throws DAOException{

       
        var txtsql = "SELECT * FROM " + nombreTabla() +
                " WHERE partido = " + partido.getId() +
                " AND equipo = " + eq.getId();
        
        String[] campos = {"portero", "jugador1", "jugador2",
        "jugador3", "jugador4", "jugador5", "jugador6", "jugador7"};
        HashMap<String,Long> datos = queryRegistro(txtsql, campos);

        return datos;
    }

    protected AlineacionFutbol8 asignarJugadores(AlineacionFutbol8 ali)
            throws DAOException{

        var eq = (EquipoFutbol8) ali.getEquipo();
        var partido = (PartidoFutbol8) ali.getPartido();
        var ids = idsJugadores(eq, partido);

        ali.setPortero(eq.getJugador(ids.get("portero")));
        ali.setJugador1(eq.getJugador(ids.get("jugador1")));
        ali.setJugador2(eq.getJugador(ids.get("jugador2")));
        ali.setJugador3(eq.getJugador(ids.get("jugador3")));
        ali.setJugador4(eq.getJugador(ids.get("jugador4")));
        ali.setJugador5(eq.getJugador(ids.get("jugador5")));
        ali.setJugador6(eq.getJugador(ids.get("jugador6")));
        ali.setJugador7(eq.getJugador(ids.get("jugador7")));

        return ali;
    }
   
    protected void eliminarAlineaciones(EquipoFutbol8 eq) throws DAOException{
        
        var txtsql = "DELETE FROM " +
                    nombreTabla() + " WHERE equipo = " + eq.getId();
        deleteObjects(txtsql);        
        
    }

   
}
