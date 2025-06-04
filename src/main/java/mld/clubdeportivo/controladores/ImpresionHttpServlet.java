package mld.clubdeportivo.controladores;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import static java.lang.Long.parseLong;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import mld.clubdeportivo.base.quinielas.*;
import mld.clubdeportivo.bd.*;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.listaEquiposQuiniela;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerApuestas;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerJornada;
import static mld.clubdeportivo.bd.quinielas.JDBCDAOQuiniela.obtenerSimpleEquipoQuiniela;
import static mld.clubdeportivo.controladores.UtilesHttpServlet.comprobarEstado;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
                    case "expotarQuiniela":
                        exportarQuiniela(req);
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
            texto = generarPlantillaImpresion(listaGrp);
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

    private Set<char[][]> tablaDatosQuiniela(ArrayList<EquipoQuiniela> lista) {

        Set<char[][]> datos = new LinkedHashSet<>();

        // 8 * 3 todas las casillas de la quiniela 1 X 2
        char[][] tabla = new char[14][8 * 3];

        int ap = 0;
        for (EquipoQuiniela eq : lista) {

            String[] ap1 = eq.getApuestas().get(0).getResultado();
            String[] ap2 = eq.getApuestas().get(1).getResultado();
            for (int i = 0; i < 14; i++) {

                //Apuesta 1
                if ("1".equals(ap1[i])) {
                    tabla[i][ap] = 'X';
                }
                if ("X".equals(ap1[i])) {
                    tabla[i][ap + 1] = 'X';
                }
                if ("2".equals(ap1[i])) {
                    tabla[i][ap + 2] = 'X';
                }

                //Apuesta 2
                if ("1".equals(ap2[i])) {
                    tabla[i][ap + 3] = 'X';
                }
                if ("X".equals(ap2[i])) {
                    tabla[i][ap + 4] = 'X';
                }
                if ("2".equals(ap2[i])) {
                    tabla[i][ap + 5] = 'X';
                }

            }

            ap = ap + 6;

            if (ap >= 24) {
                datos.add(tabla);
                tabla = new char[14][8 * 3];
                ap = 0;
            }
        }

        return datos;
    }

    private String generarPlantillaImpresion(ArrayList<EquipoQuiniela> lista) {
        // Coordenadas horizontales en porcentaje de cada casilla de una apuesta (1, X, 2)
        String[] posicionesHorizontales = {
            "4.10%", "6.54%", "8.98%", // Apuesta 1
            "12.60%", "15.04%", "17.48%", // Apuesta 2
            "21.09%", "23.54%", "25.98%", // Apuesta 3
            "29.59%", "32.03%", "34.47%", // Apuesta 4
            "38.09%", "40.53%", "42.97%", // Apuesta 5
            "46.58%", "49.02%", "51.46%", // Apuesta 6
            "55.07%", "57.51%", "59.95%", // Apuesta 7
            "63.56%", "66.00%", "68.44%" // Apuesta 8
        };

        // Posiciones verticales por fila (14 partidos)
        String[] posicionesVerticales = new String[14];
        for (int i = 0; i < 14; i++) {
            double top = 4.46 + (i * 5.73); // Espaciado vertical entre filas
            posicionesVerticales[i] = String.format(Locale.US, "%.2f%%", top);
        }

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'><style>");
        html.append("body { margin-left: 50mm; margin-top: 30mm; padding: 0; }");
        html.append(".boleto { position: relative; width: 16cm; height: 10.5cm; background-color: white; }");
        html.append(".x { position: absolute; font-size: 9pt; font-family: monospace; font-weight: bold; }");
        html.append("@media print { body {margin-left: 50mm; margin-top: 30mm; } }");
        html.append("</style></head><body>");

        Set<char[][]> tablas = tablaDatosQuiniela(lista);

        int bloqueActual = 0;
        for (char[][] tabla : tablas) {
            html.append("<div class='boleto'>");

            for (int fila = 0; fila < 14; fila++) {
                for (int columna = 0; columna < 8 * 3; columna++) {
                    if (tabla[fila][columna] == 'X') {
                        html.append(divX(posicionesHorizontales[columna], posicionesVerticales[fila]));
                    }
                }
            }

            html.append("</div>");

            if (++bloqueActual < tablas.size()) {
                html.append("<div style='page-break-after: always;'></div>");
            }
        }

        html.append("</body></html>");
        return html.toString();
    }

    private String divX(String left, String top) {
        //System.out.println("<div class='x' style='left:" + left + "; top:" + top + ";'>X</div>");
        return "<div class='x' style='left:" + left + "; top:" + top + ";'>X</div>";
    }

    @GetMapping("/exportar")
    private ResponseEntity<ByteArrayResource> exportarQuiniela(HttpServletRequest req
    ) throws DAOException, IOException {

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

         List<String> datos = new ArrayList();

        for (var eq : listaGrp) {
            apuestas = (ArrayList) obtenerApuestas(eq, jornada);
            for (var ap : apuestas) {
                String txt = "";
                int ok = 0;
                int num = 1;
                for (String r : ap.getResultado()){
                    if (r != null){
                        txt = txt.concat(r);
                        ok = ok + 1;
                    }
                    else
                        txt = txt.concat(" ");
                    num = num + 1;
                    if (num > 14)
                        break;
                }
                if (ok > 0)
                    datos.add(txt);
            }
        }
        
        String nomFich = "Jornada_" + String.valueOf(jornada.getNumero()) + ".txt";

        // Crear archivo temporal
        Path tempFile = Files.createTempFile("export", ".txt");
        Files.write(tempFile, datos, StandardOpenOption.WRITE);

        // Preparar archivo para descarga
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(tempFile));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nomFich)
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(resource.contentLength())
                .body(resource);

    }

    private void imprimirPartidoFutbol8(HttpServletRequest req, String texto) {

        var txt = new StringBuilder();
        txt.append("Partido Prueba");
        req.setAttribute("texto", txt.toString());

    }

}
