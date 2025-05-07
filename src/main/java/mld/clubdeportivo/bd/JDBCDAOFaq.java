
package mld.clubdeportivo.bd;

import java.util.ArrayList;
import mld.clubdeportivo.base.Faq;


/**
 *
 * @author mlopezd
 */
public class JDBCDAOFaq {

    private JDBCDAOFaq(){}

    
    public static Faq obtenerFaqPorId(long id) throws DAOException{
        
        var dao = new FaqDAO();
        
        return dao.getFaqById(id);
        
    }
   
    public static ArrayList<Faq> obtenerFaqsContestadas()
            throws DAOException{
        // Devuelve lista de faqs

        var dao = new FaqDAO();
        var lista  =
                (ArrayList<Faq>) dao.getFaqsContestadas();

        return lista;

    }
    
    public static ArrayList<Faq> obtenerFaqsNoContestadas()
            throws DAOException{
        // Devuelve lista de faqs

        var dao = new FaqDAO();
        var lista  =
                (ArrayList<Faq>) dao.getFaqsNoContestadas();

        return lista;

    }

 
    
    public static void grabarFaq(Faq reg) throws DAOException {
        var dao = new FaqDAO();
        dao.save(reg);
    }

    public static void eliminarFaq(Faq reg) throws DAOException {
        var dao = new FaqDAO();
        dao.delete(reg);
    }
 

}
