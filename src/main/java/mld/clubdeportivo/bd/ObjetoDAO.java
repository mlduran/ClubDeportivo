
package mld.clubdeportivo.bd;

import static java.lang.String.valueOf;
import static java.lang.String.valueOf;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;
import mld.clubdeportivo.base.Club;
import mld.clubdeportivo.base.Objeto;
import mld.clubdeportivo.base.Seccion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.util.logging.Level.INFO;
import static java.util.logging.Logger.getLogger;
import static mld.clubdeportivo.base.Objeto.UNSAVED_VALUE;
import static mld.clubdeportivo.bd.ConexionConfigDAO.getConexionConfigDAO;
import static mld.clubdeportivo.bd.ConexionDAO.getConexion;
import static mld.clubdeportivo.bd.TablasDAO.entrenadoresmaestrofutbol8;
import static mld.clubdeportivo.bd.TablasDAO.equiposmaestrofutbol8;
import static mld.clubdeportivo.bd.TablasDAO.jugadoresmaestrofutbol8;
import static mld.clubdeportivo.bd.TablasDAO.values;
import static mld.clubdeportivo.bd.TipoRetornoDAO.BIGINT;
import static mld.clubdeportivo.bd.TipoRetornoDAO.DOUBLE;
import static mld.clubdeportivo.bd.TipoRetornoDAO.FLOAT;
import static mld.clubdeportivo.bd.TipoRetornoDAO.INTEGER;
import static mld.clubdeportivo.bd.TipoSaveDAO.insert;
import static mld.clubdeportivo.bd.TipoSaveDAO.update;


/**
 *
 * @author Miguel
 */
public abstract class ObjetoDAO {
    
    
    private static Logger logger = LoggerFactory.getLogger(ObjetoDAO.class.getName());
    
    protected ObjetoDAO(){}
    
    // Metodo que devuelve el nombre del Schema de BD a utilizar
    protected abstract String schema();
    
    // Metodo que devuelve el nombre de la tabla de BD a utilizar
    protected abstract String nombreTabla();
    
    // Metodo que devuelve el nombre los campos de la tabla de BD a utilizar
    protected abstract String[] camposTabla();
    
    // Crea el objeto correspondiente con el resultado de la consulta
    protected abstract Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException;
    
    // Asigna los campos de cada objeto cuando se graba en BD
    protected abstract PreparedStatement asignarCampos(
            PreparedStatement sql, Object obj, TipoSaveDAO tipo) throws SQLException;
    
    
    
    // METODOS BASICOS
    
    private PreparedStatement pasarParametros(PreparedStatement sql, ArrayList params)
            throws SQLException {
        
        var i = 1;
        for (var parametro : params) {
            
            Class clase = parametro.getClass();
            
            if (clase == String.class) {
                sql.setString(i, (String) parametro);
            }
            else if (clase == Boolean.class) {
                sql.setBoolean(i, (Boolean) parametro);
            }
            else if (clase == Integer.class) {
                sql.setInt(i, (Integer) parametro);
            }
            else if (clase == Long.class) {
                sql.setLong(i, (Long) parametro);
            }
            else if (clase == Date.class) {
                // Para mysql el formato es yyyy-MM-dd
                var formato = new SimpleDateFormat("yyyy-MM-dd");
                var fecha = (Date) parametro;
                sql.setString(i, formato.format(fecha));
            }
            else
                throw new IllegalArgumentException(
                        "Tipo de parametro no soportado");
            
            i++;
        }
        
        return sql;
    }
    
    protected List<Object> getDataObjects(String txtsql, ArrayList params)
            throws DAOException {
        
        // Recursos a usar
        Connection miConexion = null;
        PreparedStatement sql = null;
        ResultSet retorno = null;
        
        ArrayList lista = null;
        
        logger.info(txtsql);
        if (params != null)
            for (var param :params){
                logger.info(param.toString());
            }
        
        // inicializacion recursos
        
        try {
            
            miConexion = getConexion(schema());
            // Pasar a objeto
            
            // Creamos sentencia
            sql = miConexion.prepareStatement(txtsql);
            
            if (params != null){
                // pasar parametros
                pasarParametros(sql, params);
            }
            
            //ejecutamos la sentencia
            retorno = sql.executeQuery();
            lista = new ArrayList();
            
            // leemos el retorno
            while (retorno.next()) {
                lista.add(this.crearObjeto(retorno));
            }
            
            
            // si hemos desactivado el autocommit hayque guardar
            miConexion.commit();
            
        }
        // captura de errores
        catch (SQLException ex){
            
            if (miConexion != null){
                try {
                    miConexion.rollback();
                }catch (SQLException ex2){
                    throw new DAOException(ex2);
                }
            }
            
            throw new DAOException(ex);
        }
        // cerramos los recursos
        finally {
            try{
                if (retorno != null) retorno.close();
                if (sql != null) sql.close();
                if (miConexion != null) miConexion.close();
            }catch (SQLException ex2){
                throw new DAOException(ex2);
            }
        }
        
        return lista;
        
    }
    
    protected List<Object> getDataObjects(String txtsql) throws DAOException {
        
        return getDataObjects(txtsql, null);
        
    }
    
    protected Object getDataObjectById(String txtsql, long id) throws DAOException {
        
        var params = new ArrayList();
        params.add(id);
        
        Object objeto = null;
        
        var lista = (ArrayList) getDataObjects(txtsql, params);
        
        if (lista.size() > 0)
            objeto = (Object) lista.get(0);
        
        return objeto;
        
    }
    
    protected Object getDataObject(String txtsql) throws DAOException {
        
        var params = new ArrayList();
        
        Object objeto = null;
        
        var lista = (ArrayList) getDataObjects(txtsql, params);
        
        if (lista.size() > 0)
            objeto = (Object) lista.get(0);
        
        return objeto;
        
    }
    
    protected Object getDataObject(String txtsql, String param) throws DAOException {
        
        var params = new ArrayList();
        params.add(param);
        
        Object objeto = null;
        
        var lista = (ArrayList) getDataObjects(txtsql, params);
        
        if (lista.size() > 0)
            objeto = (Object) lista.get(0);
        
        return objeto;
        
    }
    
    protected Object getDataObject(String txtsql, ArrayList params) throws DAOException {
        
        Object objeto = null;
        
        var lista = (ArrayList) getDataObjects(txtsql, params);
        
        if (lista.size() > 0)
            objeto = (Object) lista.get(0);
        
        return objeto;
        
    }
    
    protected long saveObject(Object obj, String txtsql, TipoSaveDAO tipo)
            throws DAOException {
        // Asumimos que el id que retornamos será siempre un long
        // y siempre se llamará id
        
        if (obj == null) {
            throw new NullPointerException("No se admite la referencia nula");
        }
        
        // el id lo retornamos siempre, pero solo tiene sentido
        // en las inserciones, para actualizaciones se retornará 0
        long id = 0;
        Connection conexion = null;
        PreparedStatement sql = null;
        ResultSet retorno = null;
        
        logger.info(txtsql);
        logger.info(tipo.toString());
         
        try
        {
            //Abrimos la conexión al SGBD mediante el gestor de drivers (DriverManager)
            conexion = getConexion(schema());
            
            //Activamos el modo transaccional
            conexion.setAutoCommit(false);
            
            //Precompilamos y ejecutamos la sentencia SQL contra el SGBD
            
            if (tipo == insert){
                // Es una insercion
                sql = conexion.prepareStatement(txtsql);
                asignarCampos(sql, obj, tipo);
                logger.info(sql.toString());
                sql.executeUpdate();
                sql = conexion.prepareStatement("SELECT @@IDENTITY");
                retorno = sql.executeQuery();
                if (retorno.next()) {
                    id = retorno.getLong(1);
                }
            } else {
                // Es una actualizacion
                sql = conexion.prepareStatement(txtsql);
                asignarCampos(sql, obj, tipo);
                sql.executeUpdate();
            }
            
            //COMMIT (Aceptamos posibles cambios)
            conexion.commit();
        }
        
        catch (SQLException ex) {
            //ROLLBACK (Deshacer posibles cambios)
            if (conexion != null) {
                try {
                    conexion.rollback();
                }
                catch (SQLException ex2) {
                    throw new DAOException(ex2);
                }
            }
            throw new DAOException(ex);
        }
        finally {
            //Liberamos los recursos consumidos durante la conexión
            try {
                if (retorno != null) retorno.close();
                if (sql != null) sql.close();
                if (conexion != null) conexion.close();
            }
            catch (SQLException ex) {
                throw new DAOException(ex);
            }
        }
        
        return id;
    }
    
    
    protected void deleteObject(long id, String nombreTabla) throws DAOException {
        
        //Recursos a utilizar en la comunicación con el sgbd
        Connection conexion = null;
        PreparedStatement sql = null;
        
        try {
            
            //Abrimos conexión contra la base de datos
            conexion = getConexion(schema());
            
            //Precompilamos y ejecutamos la sentencia SQL contra el SGBD
            sql = conexion.prepareStatement("DELETE FROM " +
                    nombreTabla + " WHERE id = ?");
            sql.setLong(1, id);
            
            logger.info(sql.toString());
            
            sql.executeUpdate();
            
            //COMMIT (Aceptamos posibles cambios)
            conexion.commit();
            
        } catch (SQLException ex) {
            //ROLLBACK (Deshacer posibles cambios)
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException ex2) {
                    throw new DAOException(ex2);
                }
            }
            throw new DAOException(ex);
        } finally {
            //Liberamos los recursos consumidos durante la conexión
            try {
                if (sql != null) {
                    sql.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException ex) {
                throw new DAOException(ex);
            }
        }
    }
    
    protected void deleteObjects(String query) throws DAOException {
        
        //Recursos a utilizar en la comunicación con el sgbd
        Connection conexion = null;
        PreparedStatement sql = null;
        
        try {
            
            //Abrimos conexión contra la base de datos
            conexion = getConexion(schema());
            
            //Precompilamos y ejecutamos la sentencia SQL contra el SGBD
            sql = conexion.prepareStatement(query);
            
            logger.info(sql.toString());
            
            sql.executeUpdate();
            
            //COMMIT (Aceptamos posibles cambios)
            conexion.commit();
            
        } catch (SQLException ex) {
            //ROLLBACK (Deshacer posibles cambios)
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException ex2) {
                    throw new DAOException(ex2);
                }
            }
            throw new DAOException(ex);
        } finally {
            //Liberamos los recursos consumidos durante la conexión
            try {
                if (sql != null) {
                    sql.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException ex) {
                throw new DAOException(ex);
            }
        }
    }
    
    protected void ejecutarModificacion(String txtSql) throws DAOException {
        
        //Recursos a utilizar en la comunicación con el sgbd
        Connection conexion = null;
        PreparedStatement sql = null;
        
        try {
            
            //Abrimos conexión contra la base de datos
            conexion = getConexion(schema());
            
            //Precompilamos y ejecutamos la sentencia SQL contra el SGBD
            sql = conexion.prepareStatement(txtSql);
            
            logger.info(txtSql);
            
            sql.executeUpdate();
            
            //COMMIT (Aceptamos posibles cambios)
            conexion.commit();
            
        } catch (SQLException ex) {
            //ROLLBACK (Deshacer posibles cambios)
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException ex2) {
                    throw new DAOException(ex2);
                }
            }
            throw new DAOException(ex);
        } finally {
            //Liberamos los recursos consumidos durante la conexión
            try {
                if (sql != null) {
                    sql.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException ex) {
                throw new DAOException(ex);
            }
        }
    }
    
    public static void deleteTable(String schema, String nombreTabla) throws DAOException {
        
        //Recursos a utilizar en la comunicación con el sgbd
        Connection conexion = null;
        PreparedStatement sql = null;
        
        try {
            
            //Abrimos conexión contra la base de datos
            conexion = getConexion(schema);
            
            //Precompilamos y ejecutamos la sentencia SQL contra el SGBD
            sql = conexion.prepareStatement("DELETE FROM " +
                    nombreTabla + " WHERE id <> -1");
            
            logger.info(sql.toString());
            
            sql.executeUpdate();
            
            //COMMIT (Aceptamos posibles cambios)
            conexion.commit();
            
        } catch (SQLException ex) {
            //ROLLBACK (Deshacer posibles cambios)
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException ex2) {
                    throw new DAOException(ex2);
                }
            }
            throw new DAOException(ex);
        } finally {
            //Liberamos los recursos consumidos durante la conexión
            try {
                if (sql != null) {
                    sql.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException ex) {
                throw new DAOException(ex);
            }
        }
    }
    
    protected double queryNumerica(String txtsql, TipoRetornoDAO tipoRetorno,
            ArrayList params) throws DAOException {
        // Tipo retorno puede ser long, int, float
        
        if (tipoRetorno != INTEGER &&
                tipoRetorno != DOUBLE &&
                tipoRetorno != FLOAT &&
                tipoRetorno != BIGINT)
            throw new IllegalArgumentException(
                    "Tipo de parametro no soportado");
        
        Integer vint = null;
        Long vlong = null;
        Float vfloat = null;
        Double vdouble = null;
        
        //Recursos a utilizar en la comunicación con el sgbd
        Connection conexion = null;
        PreparedStatement sql = null;
        ResultSet retorno = null;
        
        logger.info(txtsql);
        logger.info(tipoRetorno.toString());
        
        try {
            //Abrimos conexión contra la base de datos
            conexion = getConexion(schema());
            
            //Precompilamos y ejecutamos la sentencia SQL contra el SGBD
            sql = conexion.prepareStatement(txtsql);
            
            if (params != null){
                // pasar parametros
                pasarParametros(sql, params);
            }
            
            
            retorno = sql.executeQuery();
            
            //Procesar los resultados mediante el lector de resultados
            if (retorno.next()) {
                if (tipoRetorno == INTEGER)
                    vint = retorno.getInt(1);
                else if (tipoRetorno == BIGINT)
                    vlong = retorno.getLong(1);
                else if(tipoRetorno == FLOAT)
                    vfloat = retorno.getFloat(1);
                else if(tipoRetorno == DOUBLE)
                    vdouble = retorno.getDouble(1);
            }
            
            //COMMIT (Aceptamos posibles cambios)
        }
        
        catch (SQLException ex) {
            //ROLLBACK (Deshacer posibles cambios)
            if (conexion != null) {
                try {
                    conexion.rollback();
                }
                catch (SQLException ex2) {
                    throw new DAOException(ex2);
                }
            }
            throw new DAOException(ex);
        }
        finally {
            //Liberamos los recursos consumidos durante la conexión
            try {
                if (retorno != null) retorno.close();
                if (sql != null) sql.close();
                if (conexion != null) conexion.close();
            }
            catch (SQLException ex) {
                throw new DAOException(ex);
            }
        }
        
        if (tipoRetorno == INTEGER)return vint;
        else if (tipoRetorno == BIGINT)return vlong;
        else if(tipoRetorno == FLOAT)return vfloat;
        else return vdouble;
    }
    
    protected double queryNumerica(String txtsql, TipoRetornoDAO tipoRetorno
    ) throws DAOException {
        
        return queryNumerica(txtsql, tipoRetorno, null);
        
    }
    
    protected HashMap queryRegistro(String txtsql, String[] campos)
            throws DAOException {
        // Hace una query de registro y mete el resultado
        // en una hash
        
        //Recursos a utilizar en la comunicación con el sgbd
        Connection conexion = null;
        PreparedStatement sql = null;
        ResultSet retorno = null;
        var mapeo = new HashMap();
        
        logger.info(txtsql);
        
        try {
            //Abrimos conexión contra la base de datos
            conexion = getConexion(schema());
            
            //Precompilamos y ejecutamos la sentencia SQL contra el SGBD
            sql = conexion.prepareStatement(txtsql);
            retorno = sql.executeQuery();
            
            //Procesar los resultados mediante el lector de resultados
            if (retorno.next()) {
                for (var campo : campos) {
                    mapeo.put(campo, retorno.getObject(campo));
                }
            }
            
            //COMMIT (Aceptamos posibles cambios)
        }
        
        catch (SQLException ex) {
            //ROLLBACK (Deshacer posibles cambios)
            if (conexion != null) {
                try {
                    conexion.rollback();
                }
                catch (SQLException ex2) {
                    throw new DAOException(ex2);
                }
            }
            throw new DAOException(ex);
        }
        finally {
            //Liberamos los recursos consumidos durante la conexión
            try {
                if (retorno != null) retorno.close();
                if (sql != null) sql.close();
                if (conexion != null) conexion.close();
            }
            catch (SQLException ex) {
                throw new DAOException(ex);
            }
        }
        
        return mapeo;
    }
    
    protected ArrayList<HashMap> queryRegistros(String txtsql, String[] campos)
            throws DAOException {
        // Hace una query de registro y mete el resultado
        // en una hash
        
        //Recursos a utilizar en la comunicación con el sgbd
        Connection conexion = null;
        PreparedStatement sql = null;
        ResultSet retorno = null;
        var listaMapeos = new ArrayList<HashMap>();
        
        logger.info(txtsql);
        
        try {
            //Abrimos conexión contra la base de datos
            conexion = getConexion(schema());
            
            //Precompilamos y ejecutamos la sentencia SQL contra el SGBD
            sql = conexion.prepareStatement(txtsql);
            retorno = sql.executeQuery();
            
            //Procesar los resultados mediante el lector de resultados
            while (retorno.next()) {
                HashMap<String, Object> mapeo = new HashMap();
                for (var campo : campos) {
                    mapeo.put(campo, retorno.getObject(campo));
                }
                listaMapeos.add(mapeo);
            }
            
            //COMMIT (Aceptamos posibles cambios)
        }
        
        catch (SQLException ex) {
            //ROLLBACK (Deshacer posibles cambios)
            if (conexion != null) {
                try {
                    conexion.rollback();
                }
                catch (SQLException ex2) {
                    throw new DAOException(ex2);
                }
            }
            throw new DAOException(ex);
        }
        finally {
            //Liberamos los recursos consumidos durante la conexión
            try {
                if (retorno != null) retorno.close();
                if (sql != null) sql.close();
                if (conexion != null) conexion.close();
            }
            catch (SQLException ex) {
                throw new DAOException(ex);
            }
        }
        
        return listaMapeos;
    }
    
    protected String cadenaInsert(){
        
        var cad = new StringBuilder();
        boolean primero;
        
        cad.append("INSERT INTO ");
        cad.append(nombreTabla()).append("(");
        primero = true;
        for (var campo : camposTabla()) {
            if (!primero) cad.append(","); else primero = false;
            cad.append(campo);
        }
        
        cad.append(") VALUES(");
        primero = true;
        for (String camposTabla : camposTabla()) {
            if (!primero) cad.append(","); else primero = false;
            cad.append("?");
        }
        
        cad.append(")");
        
        return cad.toString();
        
    }
    
    protected String cadenaUpdate(){
        
        var cad = new StringBuilder();
        boolean primero;
        
        cad.append("UPDATE ");
        cad.append(nombreTabla()).append(" SET ");
        primero = true;
        for (var campo : camposTabla()) {
            if (!primero) cad.append(","); else primero = false;
            cad.append(campo).append("=?");
        }
        
        cad.append(" WHERE id = ?");
        
        return cad.toString();
        
    }
    
    public void save(Objeto obj) throws DAOException {
        
        long id;
        String txtsql;
        
        if (obj.getId() == UNSAVED_VALUE) {
            txtsql = cadenaInsert();
            id = saveObject(obj, txtsql, insert);
            obj.setId(id);
        } else{
            txtsql = cadenaUpdate();
            saveObject(obj, txtsql, update);
        }
    }
    
    public void delete(Objeto obj) throws DAOException {
        
        if (obj == null)
            throw new NullPointerException("No se admite la referencia nula");
        
        var id = obj.getId();
        
        deleteObject(id, nombreTabla());
    }
    
    
    // METODOS CONSULTA STANDARD SQL
    
    public Object getObjetoById(long id) throws DAOException {
        
        var txtsql = "SELECT * FROM " + this.nombreTabla() + " WHERE id = ?";
        var obj =  getDataObjectById(txtsql, id);
        
        return obj;
    }
    
    public long idClub(Seccion eq) throws DAOException {
        
        var id = valueOf(eq.getId());
        var txtsql = "SELECT club FROM " +
                nombreTabla() + " WHERE id = ".concat(id);
        var idobj = (long) queryNumerica(txtsql, BIGINT);
        
        return idobj;
        
    }
    
    public long idGrupo(Club club) throws DAOException{
        
        var id = valueOf(club.getId());
        var txtsql = "SELECT grupo FROM " + nombreTabla() + "  WHERE id = ".concat(id);
        var idobj = (long) queryNumerica(txtsql, BIGINT);
        
        return idobj;
        
    }
    
    public static ArrayList<TablasDAO> tablasMaestro(){
        
        var lista = new ArrayList<TablasDAO>();
        
        lista.add(entrenadoresmaestrofutbol8);
        lista.add(equiposmaestrofutbol8);
        lista.add(jugadoresmaestrofutbol8);
        
        return lista;
    }
    
    public static void limpiarTablas() throws DAOException{
        
        var tablas = values();
        var excepciones = tablasMaestro();
        var entorno = getEntorno();
        for (var tabla : tablas) {
            if (!excepciones.contains(tabla))
                if (tabla.name().contains("futbol8"))
                    deleteTable("futbol8" + entorno, tabla.name());
                else if (tabla.name().contains("quiniela"))
                    deleteTable("quiniela" + entorno, tabla.name());
                else
                    deleteTable("clubdeportivo" + entorno, tabla.name());
            
        }
        
    }
    
    public static String getEntorno() {
        
        var entorno = getConexionConfigDAO().getEntorno();
        if (entorno.equals("produccion"))
            return "";
        else
            return entorno;
    }
    
    
    
}
