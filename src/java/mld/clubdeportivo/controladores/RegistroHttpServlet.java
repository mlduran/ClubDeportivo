
package mld.clubdeportivo.controladores;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.http.*;
import mld.clubdeportivo.base.Registro;
import mld.clubdeportivo.bd.*;
import org.apache.log4j.*;

/**
 *
 * @author Miguel
 */
public class RegistroHttpServlet extends HttpServlet {

    private static Logger logger = LogManager.getLogger(LoginHttpServlet.class);
   
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            
            int num = 100;
            
            String numTxt = req.getParameter("numero");
            if (numTxt != null) num = Integer.parseInt(numTxt);
            if (num < 0) num = 0;
            ArrayList<Registro> regs = JDBCDAORegistro.obtenerRegistros(num);
            req.setAttribute("numero", num);
            req.setAttribute("regs", regs);
        } catch (DAOException ex) {
             req.setAttribute("error", ex.getMessage());
        }

        RequestDispatcher view = req.getRequestDispatcher("/Utiles/registro.jsp");
        view.forward(req, resp);
       
       
    }


 

}

     

