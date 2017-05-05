 
package mld.clubdeportivo.bd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import mld.clubdeportivo.base.Club;
import mld.clubdeportivo.base.Deporte;
import mld.clubdeportivo.base.Grupo;
import mld.clubdeportivo.utilidades.Seguridad;

/**
 *
 * @author Miguel
 */
public class ClubDAO extends ObjetoDAO {  
    
    protected String schema(){return "clubdeportivo" + ClubDAO.getEntorno();}
    protected String nombreTabla(){return TablasDAO.clubs.name();}
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
            "grupo"};
        return campos;
        }
   
    public String getNombreTabla(){
        return schema() + "." + nombreTabla();
    }
    
    protected Club getClubByUsuario(String usuario, String password) throws DAOException {

        String txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE usuario = ? "
                + "AND password = ?";

        ArrayList params = new ArrayList();
        params.add(usuario);
        params.add(Seguridad.SHA1Digest(password));

        Club obj = (Club) getDataObject(txtsql, params);

        return obj;
    }

    protected Club getClubById(long id) throws DAOException {

        return (Club) getObjetoById(id);
    }

    protected Club getClubByNombre(String nombre) throws DAOException {

        String txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE nombre = ?";

        Club obj = (Club) getDataObject(txtsql, nombre);

        return obj;
    }

    protected Club getClubByUsuario(String usuario) throws DAOException {

        String txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE usuario = ?";

        Club obj = (Club) getDataObject(txtsql, usuario);

        return obj;
    }

    protected List<Club> getClubs() throws DAOException {

        String txtsql = "SELECT * FROM " + nombreTabla();
        
        ArrayList<Club> listaObjs = (ArrayList) getDataObjects(txtsql);

        return listaObjs;
    }

    protected List<Club> getClubsByRanking(int max) throws DAOException {

        String txtsql = "SELECT * FROM " + nombreTabla() +
                " ORDER BY ranking DESC LIMIT ?";

        ArrayList params = new ArrayList();
        params.add(max);

        ArrayList<Club> listaObjs = (ArrayList) getDataObjects(txtsql, params);

        return listaObjs;
    }

    protected List<Club> getClubsByGrupo(Grupo grupo) throws DAOException {

        String txtsql = "SELECT * FROM " + nombreTabla() + "  WHERE grupo = ?";

        ArrayList params = new ArrayList();
        params.add(grupo.getId());

        ArrayList<Club> listaObjs = (ArrayList) getDataObjects(txtsql, params);

        return listaObjs;
    }

   
    @Override
    protected Object crearObjeto(ResultSet retorno)
            throws SQLException, DAOException {

        if (retorno == null ) return null;

        Club obj = new Club();

        obj.setId(retorno.getLong("id"));
        obj.setNombre(retorno.getString("nombre"));
        obj.setUsuario(retorno.getString("usuario"));
        obj.setPassword(retorno.getString("password"));
        obj.setMail(retorno.getString("mail"));
        obj.setFundacion(retorno.getTimestamp("fundacion"));
        obj.setUltimoAcceso(retorno.getTimestamp("ultimoacceso"));
        obj.setRanking(retorno.getInt("ranking"));
        obj.setActivo(retorno.getBoolean("activo"));
            
        asignarDeportes(obj);

        return obj;
    }

    @Override
    protected PreparedStatement asignarCampos(PreparedStatement sql, Object obj,
            TipoSaveDAO tipo) throws SQLException {

        Club objClub = (Club) obj;
       
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
        // Si es un update asignamos el parametro del id
        if (tipo == TipoSaveDAO.update) sql.setLong(10, objClub.getId());

        return sql;
    }
    

    protected void asignarDeportes(Club club) throws DAOException{

        ArrayList<Deporte> deportes = new ArrayList();
        
        for (Deporte dep : Deporte.values()) {
        
            Connection miConexion = null;
            PreparedStatement sql = null;
            ResultSet retorno = null;

            
            String schema;
            String extras = "";
            if (dep.equals(Deporte.Futbol8)){
                schema = "futbol8";
                extras = " AND automatico = false";
            }
            else if (dep.equals(Deporte.Quiniela))
                schema = "quiniela";
            else if (dep.equals(Deporte.Basket))
                schema = "";
            else
                schema = "clubdeportivo";
            
            if (!schema.isEmpty()){
                try {
                            
                    miConexion = ConexionDAO.getConexion(schema + ClubDAO.getEntorno());
                    String txtsql;
                    
                    String tabla = "equipos" + dep.name().toLowerCase();
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
