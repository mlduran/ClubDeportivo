
package mld.clubdeportivo.controladores;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import mld.clubdeportivo.bd.*;
import static mld.clubdeportivo.bd.JDBCDAORegistro.obtenerRegistros;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


/**
 *
 * @author Miguel
 */
@Controller
public class RegistroHttpServlet {

   
    @GetMapping("/registro")
    public String doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        return processRequest(req, resp);
    }

    @PostMapping("/registro")
    public String doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        return processRequest(req, resp);
    }

    private String processRequest(HttpServletRequest req, HttpServletResponse resp)
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

        return "registro";
       
       
    }


 

}

     

