
package mld.clubdeportivo.controladores;

import java.io.IOException;
import java.util.HashMap;
import javax.servlet.*;
import javax.servlet.http.*;
import mld.clubdeportivo.utilidades.IODatos;
import org.apache.log4j.*;

/**
 *
 * @author Miguel
 */
public class AyudaHttpServlet extends HttpServlet {

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

        String grp = req.getParameter("grupo");
        String cod = req.getParameter("codigo");

         try {
            if (grp == null) {}
            else if (grp.equals("index"))
                obtenerIndice(req, cod); 
            else 
                obtenerCodigoGeneral(req, cod, grp);            
        }catch(Exception ex){
            req.setAttribute("error", ex.getMessage());
        }
  

        RequestDispatcher view = req.getRequestDispatcher("/Utiles/ayuda.jsp");
        view.forward(req, resp);
       
    }

   
    private void obtenerCodigoGeneral(HttpServletRequest req, String cod, String grp) {
        

        String ruta =  this.getInitParameter("rutaficherosayuda");
        
        HashMap datos = IODatos.obtenerAyuda(ruta + "/" + grp + ".help", cod);
        
        req.setAttribute("texto", datos.get("texto"));
        req.setAttribute("titulo", datos.get("titulo"));
    }

    private void obtenerIndice(HttpServletRequest req, String cod) {
        
        ServletContext appManager = this.getServletContext();

        String ruta =  this.getInitParameter("rutaficherosayuda");
        String rutaBase = appManager.getContextPath();
        
        StringBuilder txt = new StringBuilder();
        
        txt.append(IODatos.obtenerIndiceAyuda(ruta + "/general.help", rutaBase, "General"));
        txt.append("<br/>");
        txt.append(IODatos.obtenerIndiceAyuda(ruta + "/" + cod + ".help", rutaBase, cod));
        
        req.setAttribute("texto", txt);
        req.setAttribute("titulo", "INDICE");
    }
    
    
 

}

     

