package mld.clubdeportivo.controladores;

/**
 *
 * @author Miguel
 */


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

public class FileHttpServlet extends HttpServlet{
    
     private static Logger logger = LoggerFactory.getLogger(AdminHttpServlet.class.getName());
   
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {
        try {
            var appManager = this.getServletContext();
            var ruta = appManager.getInitParameter("rutaficheroscarga");
            var rutaReal = appManager.getRealPath("/");
            var dir = new File(ruta);
            var listaFiles = dir.list();
            req.setAttribute("ficheros", listaFiles);
            req.setAttribute("ruta", ruta);
            req.setAttribute("rutaReal", rutaReal);
            var view = 
                    req.getRequestDispatcher("/Utiles/cargaFichero.jsp");
            view.forward(req, resp);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            req.setAttribute("error", ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        var appManager = this.getServletContext();
        var ruta = appManager.getInitParameter("rutaficheroscarga");
        //ruta = "D:/Proyectos_Web/ClubDeportivo/web/WEB-INF/FicherosCarga";

        resp.setContentType("text/html;charset=UTF-8");

        PrintWriter out = null;

        /*
        try {
            out = resp.getWriter();
            File dir = new File(ruta);

            MultipartParser mp = new MultipartParser(req, 1024 * 1024);
            Part part;
            while ((part = mp.readNextPart()) != null)
                if (part.isFile()){
                    FilePart filePart = (FilePart) part;
                    filePart.writeTo(dir);
                }


        } catch (IOException ex) {
            req.setAttribute("error", ex.getMessage());
            RequestDispatcher view =
                    req.getRequestDispatcher("/Utiles/cargaFichero.jsp");
            try {
                view.forward(req, resp);
            } catch (IOException ex1) {
                logger.log(Level.SEVERE, ex1.getMessage());
            }
        }finally{
            if (out != null) out.close();
        }

       */

    }



}
