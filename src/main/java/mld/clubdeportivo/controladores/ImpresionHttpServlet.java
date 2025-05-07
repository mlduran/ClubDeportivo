package mld.clubdeportivo.controladores;

import jakarta.servlet.ServletException;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Miguel
 */
@Controller
public class ImpresionHttpServlet {

    @GetMapping("/imprimir")
    public String doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        return processRequest(req, resp);
    }

    private String processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String estado = comprobarEstado(req, resp);
        if (!"".equals(estado)) {
            return estado;
        }

        var op = req.getParameter("operacion");
        var texto = "";

        try {
            if (null == op) {
            } else {
                switch (op) {
                    case "quiniela":
                        imprimirQuiniela(req, false);
                        break;
                    case "quinielaBoleto":
                        imprimirQuiniela(req, true);
                        break;
                    case "partidoFutbol8":
                        imprimirPartidoFutbol8(req, texto);
                        break;
                    default:
                        break;
                }
            }
        } catch (DAOException ex) {

        }

        return "Utiles/imprimir";

    }

    private void imprimirQuiniela(HttpServletRequest req,
            boolean impresionBoleto) throws DAOException {

        var lista
                = listaEquiposQuiniela();
        var listaGrp = new ArrayList<EquipoQuiniela>();

        long id = (Long) req.getSession().getAttribute("idEquipo");
        var eqAct = obtenerSimpleEquipoQuiniela(id);

        listaGrp.add(eqAct);
        for (var equipoQuiniela : lista) {
            var mismoGrupo = eqAct.getClub().getGrupo().equals(equipoQuiniela.getClub().getGrupo());
            if (!eqAct.equals(equipoQuiniela) && mismoGrupo) {
                listaGrp.add(equipoQuiniela);
            }
        }

        ArrayList<ApuestaQuiniela> apuestas;

        var op = req.getParameter("jornada");
        var jornada
                = obtenerJornada(parseLong(op));
        for (var eq : listaGrp) {
            apuestas = (ArrayList) obtenerApuestas(eq, jornada);
            eq.setApuestas(apuestas);
            for (var ap : apuestas) {
                ap.setEquipo(eq);
            }
        }

        String texto;
        if (impresionBoleto) {
            texto = generarPlantillaImpresion(listaGrp, jornada);
        } else {
            texto = txtApuestasJornada(listaGrp, jornada);
        }

        req.setAttribute("texto", texto);

    }

    private String txtApuestasJornada(ArrayList<EquipoQuiniela> lista,
            JornadaQuiniela jornada) {

        var txt = new StringBuilder();
        var tabla = new String[15][lista.size() * 2];
        var numeq = 0;
        for (var eq : lista) {
            var ap1 = eq.getApuestas().get(0).getResultado();
            var ap2 = eq.getApuestas().get(1).getResultado();
            for (var i = 0; i < 15; i++) {
                if (ap1[i] != null) {
                    tabla[i][numeq] = ap1[i];
                } else {
                    tabla[i][numeq] = "-";
                }
                if (ap2[i] != null) {
                    tabla[i][numeq + 1] = ap2[i];
                } else {
                    tabla[i][numeq + 1] = "-";
                }
            }
            numeq = numeq + 2;
        }

        txt.append("<table class='gidView' border='1' style=\"background-color: white;\">");
        txt.append("<tr>");
        txt.append("<th>PARTIDO</th>");
        for (var eq : lista) {
            txt.append("<th>").append(eq.getNombreCorto()).append("</th>");
        }
        txt.append("</tr>");
        txt.append("<tr>");
        txt.append("<th></th>");
        for (EquipoQuiniela lista1 : lista) {
            txt.append("<th colspan=1 align='center'>C1-C2</th>");
        }
        txt.append("</tr>");

        String[] colores = {"FFF111", "F5DA81", "EDFAEA", "F7A6A6",
            "D2659F", "ECF6CE", "C15151", "F2F5A9"};

        for (var i = 0; i < 15; i++) {
            txt.append("<tr>");
            txt.append("<td>");
            txt.append(jornada.getPartido()[i]);
            txt.append("</td>");
            for (var ii = 0; ii < lista.size() * 2; ii = ii + 2) {
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

    private String generarPlantillaImpresion(ArrayList<EquipoQuiniela> lista, JornadaQuiniela jornada) {
        var html = new StringBuilder();
        html.append("<html><head><style>");
        html.append("body { margin: 0; padding: 0; }");
        html.append("table { border-collapse: collapse; font-family: monospace; font-size: 12pt; background-color: white; }");
        html.append("td { width: 30px; height: 25px; text-align: center; }");
        html.append(".marcada { font-weight: bold; }");
        html.append(".bloque { margin-bottom: 30px; }");
        html.append("@media print { .salto-pagina { page-break-after: always; } }");
        html.append("</style></head><body>");

        var totalApuestas = lista.size();
        var tabla = new String[15][totalApuestas * 2];

        // Rellenar matriz tabla[i][columna] con resultados de cada apuesta
        int numeq = 0;
        for (var eq : lista) {
            var ap1 = eq.getApuestas().get(0).getResultado();
            var ap2 = eq.getApuestas().get(1).getResultado();
            for (int i = 0; i < 15; i++) {
                tabla[i][numeq] = ap1[i] != null ? ap1[i] : "-";
                tabla[i][numeq + 1] = ap2[i] != null ? ap2[i] : "-";
            }
            numeq += 2;
        }

        // Procesar bloques de hasta 8 apuestas (16 columnas)
        for (int bloque = 0; bloque < totalApuestas; bloque += 8) {
            html.append("<div class='bloque'><table>");
            for (int i = 0; i < 15; i++) {
                html.append("<tr>");
                for (int j = bloque * 2; j < Math.min((bloque + 8) * 2, totalApuestas * 2); j++) {
                    String res = tabla[i][j];
                    html.append("<td class='").append("1".equals(res) ? "marcada" : "").append("'>")
                            .append("1".equals(res) ? "X" : "&nbsp;").append("</td>");
                    html.append("<td class='").append("X".equals(res) ? "marcada" : "").append("'>")
                            .append("X".equals(res) ? "X" : "&nbsp;").append("</td>");
                    html.append("<td class='").append("2".equals(res) ? "marcada" : "").append("'>")
                            .append("2".equals(res) ? "X" : "&nbsp;").append("</td>");
                    html.append("<td style='width: 10px'></td>"); // Separador entre apuestas
                }
                html.append("</tr>");
            }
            html.append("</table></div>");

            // Si quedan más bloques por imprimir, insertar salto de página
            if (bloque + 8 < totalApuestas) {
                html.append("<div class='salto-pagina'></div>");
            }
        }

        html.append("</body></html>");
        return html.toString();
    }

    private void imprimirPartidoFutbol8(HttpServletRequest req, String texto) {

        var txt = new StringBuilder();
        txt.append("Partido Prueba");
        req.setAttribute("texto", txt.toString());

    }

}
