package mld.clubdeportivo.controladores;

/**
 *
 * @author Miguel
 */

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.Part;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class FileHttpServlet extends HttpServlet{
    
     private static Logger logger = LogManager.getLogger(AdminHttpServlet.class);
   
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {
        try {
            ServletContext appManager = this.getServletContext();
            String ruta = appManager.getInitParameter("rutaficheroscarga");
            String rutaReal = appManager.getRealPath("/");
            File dir = new File(ruta);
            String[] listaFiles = dir.list();
            req.setAttribute("ficheros", listaFiles);
            req.setAttribute("ruta", ruta);
            req.setAttribute("rutaReal", rutaReal);
            RequestDispatcher view = 
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

        ServletContext appManager = this.getServletContext();
        String ruta = appManager.getInitParameter("rutaficheroscarga");
        //ruta = "D:/Proyectos_Web/ClubDeportivo/web/WEB-INF/FicherosCarga";

        resp.setContentType("text/html;charset=UTF-8");

        PrintWriter out = null;

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
                logger.error(ex1.getMessage());
            }
        }finally{
            if (out != null) out.close();
        }

       

    }



}
