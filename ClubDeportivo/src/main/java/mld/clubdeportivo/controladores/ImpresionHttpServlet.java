
package mld.clubdeportivo.controladores;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import static java.lang.Long.parseLong;
import java.util.ArrayList;
import mld.clubdeportivo.base.quinielas.*;
import mld.clubdeportivo.bd.*;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.listaEquiposQuiniela;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerApuestas;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerJornada;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerSimpleEquipoQuiniela;
import static mld.clubdeportivo.controladores.UtilesHttpServlet.comprobarEstado;

/**
 *
 * @author Miguel
 */
public class ImpresionHttpServlet extends HttpServlet {

   
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
        
        if (!comprobarEstado(req, resp)) return;

        var op = req.getParameter("operacion");
        var texto = "";

        try {
            if (null == op) {
            } else switch (op) {
                case "quiniela":
                    imprimirQuiniela(req, texto);
                    break;
                case "partidoFutbol8":
                    imprimirPartidoFutbol8(req, texto);
                    break;
                default:
                    break;
            }
        }catch(DAOException ex){

        }


        var view = req.getRequestDispatcher("/Utiles/imprimir.jsp");
        view.forward(req, resp);
       
       
    }

    private void imprimirQuiniela(HttpServletRequest req,
            String texto) throws DAOException {

        var lista =
                listaEquiposQuiniela();
        var listaGrp = new ArrayList<EquipoQuiniela>();
        
        long id = (Long) req.getSession().getAttribute("idEquipo");
        var eqAct = obtenerSimpleEquipoQuiniela(id);
        
        listaGrp.add(eqAct);
        for (var equipoQuiniela : lista) {
            var mismoGrupo = eqAct.getClub().getGrupo().equals(equipoQuiniela.getClub().getGrupo());
            if (!eqAct.equals(equipoQuiniela) && mismoGrupo){
                listaGrp.add(equipoQuiniela);
            }
        }

        ArrayList<ApuestaQuiniela> apuestas;

        var op = req.getParameter("jornada");
        var jornada =
                obtenerJornada(parseLong(op));
        for (var eq : listaGrp) {
            apuestas = (ArrayList) obtenerApuestas(eq, jornada);
            eq.setApuestas(apuestas);
            for (var ap : apuestas) {
                ap.setEquipo(eq);
            }
        }

        texto = txtApuestasJornada(listaGrp, jornada);

        req.setAttribute("texto", texto);


    }

    private String txtApuestasJornada(ArrayList<EquipoQuiniela> lista,
            JornadaQuiniela jornada) {

        var txt = new StringBuilder();
        var tabla = new String[15][lista.size()*2];
        var numeq = 0;
        for (var eq : lista) {
            var ap1 = eq.getApuestas().get(0).getResultado();
            var ap2 = eq.getApuestas().get(1).getResultado();
            for (var i = 0; i < 15; i++){
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
        for (var eq : lista){
            txt.append("<th>").append(eq.getNombreCorto()).append("</th>");
        }
        txt.append("</tr>");
        txt.append("<tr>");
         txt.append("<th></th>");
        for (EquipoQuiniela lista1 : lista) {           
            txt.append("<th colspan=1 align='center'>C1-C2</th>");
        }
        txt.append("</tr>");
        
        String[] colores = {"FFF111","F5DA81","EDFAEA","F7A6A6",
            "D2659F","ECF6CE","C15151","F2F5A9"};
           
        for (var i = 0; i < 15; i++) {
            txt.append("<tr>");
            txt.append("<td>");
            txt.append(jornada.getPartido()[i]);
            txt.append("</td>");
            for (var ii = 0; ii < lista.size()*2; ii = ii + 2){   
                var color = colores[(ii / 2) % colores.length];
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
        
        var txt = new StringBuilder();
        txt.append("Partido Prueba");
        req.setAttribute("texto", txt.toString());

    }

 

}

     

