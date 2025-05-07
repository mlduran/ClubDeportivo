 
package mld.clubdeportivo.bd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import mld.clubdeportivo.base.Club;
import mld.clubdeportivo.base.Deporte;
import static mld.clubdeportivo.base.Deporte.Basket;
import static mld.clubdeportivo.base.Deporte.Futbol8;
import static mld.clubdeportivo.base.Deporte.Quiniela;
import static mld.clubdeportivo.base.Deporte.values;
import mld.clubdeportivo.base.Grupo;
import static mld.clubdeportivo.bd.ConexionDAO.getConexion;
import static mld.clubdeportivo.bd.TablasDAO.clubs;
import static mld.clubdeportivo.bd.TipoSaveDAO.update;
import mld.clubdeportivo.utilidades.Seguridad;
import static mld.clubdeportivo.utilidades.Seguridad.SHA1Digest;

/**
 *
 * @author Miguel
 */
public class ClubDAO extends ObjetoDAO {  
    
    protected String schema(){return "clubdeportivo" + getEntorno();}
    protected String nombreTabla(){return clubs.name();}
    protected String[] camposTabla(){
        String[] campos = {
            "activo",
            "nombre",
            "usuario",
            "password",
            "mail",
            "fundacion",
            "ultimoAcceso",
            "ranking",
            "grupo",
            "auto"
        };
        return campos;
        }
   
    public String getNombreTabla(){
        return schema() + "." + nombreTabla();
    }
    
    protected Club getClubByUsuario(String usuario, String password) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE usuario = ? "
                + "AND password = ?";
        var params = new ArrayList();
        params.add(usuario);
        params.add(SHA1Digest(password));

        var obj = (Club) getDataObject(txtsql, params);

        return obj;
    }

    protected Club getClubById(long id) throws DAOException {

        return (Club) getObjetoById(id);
    }

    protected Club getClubByNombre(String nombre) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE nombre = ?";
        var obj = (Club) getDataObject(txtsql, nombre);

        return obj;
    }

    protected Club getClubByUsuario(String usuario) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE usuario = ?";
        var obj = (Club) getDataObject(txtsql, usuario);

        return obj;
    }

    protected List<Club> getClubs() throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla();
        
        ArrayList<Club> listaObjs = (ArrayList) getDataObjects(txtsql);

        return listaObjs;
    }
    
     protected List<Club> getClubsNoAuto() throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + " WHERE auto = false";
        
        ArrayList<Club> listaObjs = (ArrayList) getDataObjects(txtsql);

        return listaObjs;
    }

    protected List<Club> getClubsByRanking(int max) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() +
                " ORDER BY ranking DESC LIMIT ?";
        var params = new ArrayList();
        params.add(max);

        ArrayList<Club> listaObjs = (ArrayList) getDataObjects(txtsql, params);

        return listaObjs;
    }

    protected List<Club> getClubsByGrupo(Grupo grupo) throws DAOException {

        var txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE grupo = ?";
        var params = new ArrayList();
        params.add(grupo.getId());

        ArrayList<Club> listaObjs = (ArrayList) getDataObjects(txtsql, params);

        return listaObjs;
    }

   
    @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        var obj = new Club();

        obj.setId(retorno.getLong("id"));
        obj.setNombre(retorno.getString("nombre"));
        obj.setUsuario(retorno.getString("usuario"));
        obj.setPassword(retorno.getString("password"));
        obj.setMail(retorno.getString("mail"));
        obj.setFundacion(retorno.getTimestamp("fundacion"));
        obj.setUltimoAcceso(retorno.getTimestamp("ultimoacceso"));
        obj.setRanking(retorno.getInt("ranking"));
        obj.setActivo(retorno.getBoolean("activo"));
        obj.setAuto(retorno.getBoolean("auto"));
        
        asignarDeportes(obj);

        return obj;
    }

    @Override
    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        var objClub = (Club) obj;
       
        sql.setBoolean(1, objClub.isActivo());
        sql.setString(2, objClub.getNombre());
        sql.setString(3, objClub.getUsuario());
        sql.setString(4, objClub.getPassword());
        sql.setString(5, objClub.getMail());
        sql.setTimestamp(6,
                new Timestamp(objClub.getFundacion().getTime()));
        sql.setTimestamp(7,
                new Timestamp(objClub.getUltimoAcceso().getTime()));
        sql.setInt(8, objClub.getRanking());
        sql.setLong(9, objClub.getGrupo().getId());
        sql.setBoolean(10, objClub.isAuto());
        // Si es un update asignamos el parametro del id
        if (tipo == update) sql.setLong(11, objClub.getId());

        return sql;
    }
    

    protected void asignarDeportes(Club club) throws DAOException{

        ArrayList<Deporte> deportes = new ArrayList();
        
        for (var dep : values()) {
        
            Connection miConexion = null;
            PreparedStatement sql = null;
            ResultSet retorno = null;

            
            String schema;
            var extras = "";
            if (dep.equals(Futbol8)){
                schema = "futbol8";
                extras = " AND automatico = false";
            }
            else if (dep.equals(Quiniela))
                schema = "quiniela";
            else if (dep.equals(Basket))
                schema = "";
            else
                schema = "clubdeportivo";
            
            if (!schema.isEmpty()){
                try {
                            
                    miConexion = getConexion(schema + getEntorno());
                    String txtsql;
                    
                    var tabla = "equipos" + dep.name().toLowerCase();
                    txtsql = "SELECT * FROM " + tabla + " WHERE club = ?" + extras;
                    sql = miConexion.prepareStatement(txtsql);
                    sql.setLong(1, club.getId());
                    retorno = sql.executeQuery();
                    if (retorno.next()) deportes.add(dep);  
                    
                }
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
                finally {
                    try{
                        if (retorno != null) retorno.close();
                        if (sql != null) sql.close();
                        if (miConexion != null) miConexion.close();
                    }catch (SQLException ex2){
                        throw new DAOException(ex2);
                    }
                }
            }
        }
        club.setDeportes(deportes);
    }


        
        
    
}
