
package mld.clubdeportivo.controladores;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import mld.clubdeportivo.bd.*;
import static mld.clubdeportivo.bd.JDBCDAORegistro.obtenerRegistros;


/**
 *
 * @author Miguel
 */
public class RegistroHttpServlet extends HttpServlet {

   
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
            
            var num = 100;
            var numTxt = req.getParameter("numero");
            if (numTxt != null) num = parseInt(numTxt);
            if (num < 0) num = 0;
            var regs = obtenerRegistros(num);
            req.setAttribute("numero", num);
            req.setAttribute("regs", regs);
        } catch (DAOException ex) {
             req.setAttribute("error", ex.getMessage());
        }

        var view = req.getRequestDispatcher("/Utiles/registro.jsp");
        view.forward(req, resp);
       
       
    }


 

}

     

