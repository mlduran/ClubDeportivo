
package mld.clubdeportivo.controladores;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import static mld.clubdeportivo.utilidades.IODatos.obtenerAyuda;
import static mld.clubdeportivo.utilidades.IODatos.obtenerIndiceAyuda;


/**
 *
 * @author Miguel
 */
public class AyudaHttpServlet extends HttpServlet {

   
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

        var grp = req.getParameter("grupo");
        var cod = req.getParameter("codigo");

         try {
            if (null == grp) {}
            else switch (grp) {
                case "index":
                    obtenerIndice(req, cod);
                    break;            
                default:
                    obtenerCodigoGeneral(req, cod, grp);
                    break;
            }            
        }catch(Exception ex){
            req.setAttribute("error", ex.getMessage());
        }
  

        var view = req.getRequestDispatcher("/Utiles/ayuda.jsp");
        view.forward(req, resp);
       
    }

   
    private void obtenerCodigoGeneral(HttpServletRequest req, String cod, String grp) {
        

        var ruta =  this.getInitParameter("rutaficherosayuda");
        
        HashMap datos = obtenerAyuda(ruta + "/" + grp + ".help", cod);
        
        req.setAttribute("texto", datos.get("texto"));
        req.setAttribute("titulo", datos.get("titulo"));
    }

    private void obtenerIndice(HttpServletRequest req, String cod) {
        
        var appManager = this.getServletContext();
        var ruta =  this.getInitParameter("rutaficherosayuda");
        var rutaBase = appManager.getContextPath();
        var txt = new StringBuilder();
        
        txt.append(obtenerIndiceAyuda(ruta + "/general.help", rutaBase, "General"));
        txt.append("<br/>");
        txt.append(obtenerIndiceAyuda(ruta + "/" + cod + ".help", rutaBase, cod));
        
        req.setAttribute("texto", txt);
        req.setAttribute("titulo", "INDICE");
    }
    
    
 

}

     

