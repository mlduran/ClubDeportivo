
package mld.clubdeportivo.controladores;

import mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.http.*;
import mld.clubdeportivo.base.quinielas.*;
import mld.clubdeportivo.bd.*;
import org.apache.log4j.*;

/**
 *
 * @author Miguel
 */
public class ImpresionHttpServlet extends HttpServlet {

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

        String op = req.getParameter("operacion");

        String texto = "";

        try {
            if (op == null) {
            } else if (op.equals("quiniela")) {
                imprimirQuiniela(req, texto);
            } else if (op.equals("partidoFutbol8")) {
                imprimirPartidoFutbol8(req, texto);
            }
        }catch(DAOException ex){

        }


        RequestDispatcher view = req.getRequestDispatcher("/Utiles/imprimir.jsp");
        view.forward(req, resp);
       
       
    }

    private void imprimirQuiniela(HttpServletRequest req,
            String texto) throws DAOException {

        ArrayList<EquipoQuiniela> lista =
                JDBCDAOQuiniela.listaEquiposQuiniela();
        
        ArrayList<EquipoQuiniela> listaGrp = new ArrayList<EquipoQuiniela>();
        
        long id = (Long) req.getSession().getAttribute("idEquipo");
        EquipoQuiniela eqAct = JDBCDAOQuiniela.obtenerSimpleEquipoQuiniela(id);
        
        listaGrp.add(eqAct);
        for (EquipoQuiniela equipoQuiniela : lista) {
            boolean mismoGrupo = eqAct.getClub().getGrupo().equals(equipoQuiniela.getClub().getGrupo());
            if (!eqAct.equals(equipoQuiniela) && mismoGrupo){
                listaGrp.add(equipoQuiniela);
            }
        }

        ArrayList<ApuestaQuiniela> apuestas;

        String op = req.getParameter("jornada");

        JornadaQuiniela jornada =
                JDBCDAOQuiniela.obtenerJornada(Long.parseLong(op));

        for (EquipoQuiniela eq : listaGrp) {
            apuestas = (ArrayList) JDBCDAOQuiniela.obtenerApuestas(eq, jornada);
            eq.setApuestas(apuestas);
            for (ApuestaQuiniela ap : apuestas) {
                ap.setEquipo(eq);
            }
        }

        texto = txtApuestasJornada(listaGrp, jornada);

        req.setAttribute("texto", texto);


    }

    private String txtApuestasJornada(ArrayList<EquipoQuiniela> lista,
            JornadaQuiniela jornada) {

        StringBuilder txt = new StringBuilder();

        String tabla[][] = new String[15][lista.size()*2];

        int numeq = 0;
        for (EquipoQuiniela eq : lista) {
            String[] ap1 = eq.getApuestas().get(0).getResultado();
            String[] ap2 = eq.getApuestas().get(1).getResultado();
            for (int i = 0; i < 15; i++){
                if (ap1[i] != null) tabla[i][numeq] = ap1[i];
                else tabla[i][numeq] = "-";
                if (ap2[i] != null) tabla[i][numeq + 1] = ap2[i];
                else tabla[i][numeq + 1] = "-";
            }
            numeq = numeq + 2;
        }

        txt.append("<table class='gidView' border='1'>");
        txt.append("<tr>");
        txt.append("<th>PARTIDO</th>");
        for (EquipoQuiniela eq : lista){
            txt.append("<th>").append(eq.getNombreCorto()).append("</th>");
        }
        txt.append("</tr>");
        txt.append("<tr>");
         txt.append("<th></th>");
        for (int i = 0; i < lista.size(); i++){           
            txt.append("<th colspan=1 align='center'>C1-C2</th>");
        }
        txt.append("</tr>");
        
        String[] colores = {"FFF111","F5DA81","EDFAEA","F7A6A6",
            "D2659F","ECF6CE","C15151","F2F5A9"};
           
        for (int i = 0; i < 15; i++) {
            txt.append("<tr>");
            txt.append("<td>");
            txt.append(jornada.getPartido()[i]);
            txt.append("</td>");
            for (int ii = 0; ii < lista.size()*2; ii = ii + 2){   
                String color = colores[(ii / 2) % colores.length];
                txt.append("<td colspan=1 align='center' bgcolor='").append(color).append("'>");
                txt.append(tabla[i][ii]);
                txt.append("&nbsp;&nbsp;");
                txt.append(tabla[i][ii + 1]);
                txt.append("</td>");
            }
            txt.append("</tr>");
        }

        txt.append("</table>");

        return txt.toString();

    }

    private void imprimirPartidoFutbol8(HttpServletRequest req, String texto) {
        
        StringBuilder txt = new StringBuilder();
        txt.append("Partido Prueba");
        req.setAttribute("texto", txt.toString());

    }

 

}

     

