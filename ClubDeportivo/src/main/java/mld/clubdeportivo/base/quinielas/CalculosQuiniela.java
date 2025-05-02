/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mld.clubdeportivo.base.quinielas;

import static java.lang.String.valueOf;
import static java.lang.String.valueOf;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Miguel
 */
public class CalculosQuiniela {

    public static void calculoResultadosQuiniela(
            ArrayList<EquipoQuiniela> eqs, String[] resultados, boolean isGeneral, int ptsJornada) {

        var aciertosTotales = new Integer[15];
        for (var i = 0; i < 15; i++) {
            aciertosTotales[i] = 0;
        }

        // primero obtenemos los aciertos totales para cada partido
        for (var eq : eqs) {
            var ap1 = eq.getApuestas().get(0);
            var ap2 = eq.getApuestas().get(1);
            for (var i = 0; i < 15; i++) {
                if (ap1.getResultado()[i] != null && ap1.getResultado()[i].equals(resultados[i])) {
                    aciertosTotales[i]++;
                }
                if (ap2.getResultado()[i] != null && ap2.getResultado()[i].equals(resultados[i])) {
                    aciertosTotales[i]++;
                }
            }
        }

        //Generamos la lista con datos
        var ptsPartido = eqs.size() * 2 * 10;

        ArrayList<ResultadosApuestas> resulApuestas = new ArrayList<>();
        for (var eq : eqs) {

            var ap1 = eq.getApuestas().get(0);
            var ap2 = eq.getApuestas().get(1);
            int aciertosCol1 = 0;
            int aciertosCol2 = 0;
            int ptsCol1 = 0;
            int ptsCol2 = 0;
            var ap = new ResultadosApuestas();
            ap.setEquipo(eq);
            var puntoscol1 = new int[15];
            var puntoscol2 = new int[15];
            for (var i = 0; i < 15; i++) {

                if (ap1.getResultado()[i] != null && ap1.getResultado()[i].equals(resultados[i])) {
                    aciertosCol1++;
                    ptsCol1 = ptsCol1 + (ptsPartido / aciertosTotales[i]);
                    puntoscol1[i] = ptsPartido / aciertosTotales[i];
                }
                if (ap2.getResultado()[i] != null && ap2.getResultado()[i].equals(resultados[i])) {
                    aciertosCol2++;                     
                    ptsCol2 = ptsCol2 + (ptsPartido / aciertosTotales[i]);
                    puntoscol2[i] = ptsPartido / aciertosTotales[i];
                }
            }
            eq.getApuestas().get(0).setPuntos(puntoscol1);
            eq.getApuestas().get(1).setPuntos(puntoscol2);
            ap.setAciertos1(aciertosCol1);
            ap.setAciertos2(aciertosCol2);
            ap.setAciertosCol1(String.valueOf(aciertosCol1));
            ap.setAciertosCol2(String.valueOf(aciertosCol2));
            ap.setPtsCol1(ptsCol1);
            ap.setPtsCol2(ptsCol2);
            resulApuestas.add(ap);
        }

        // La ordenamos por aciertos y despues por puntos
        ArrayList<ResultadosApuestas> resulOrdenado = new ArrayList<>(resulApuestas);
        resulOrdenado.sort((a, b) -> {
            int maxA = Math.max(a.getAciertos1(), a.getAciertos2());
            int maxB = Math.max(b.getAciertos1(), b.getAciertos2());

            if (maxB != maxA) {
                return Integer.compare(maxB, maxA);
            }

            int minA = Math.min(a.getAciertos1(), a.getAciertos2());
            int minB = Math.min(b.getAciertos1(), b.getAciertos2());

            if (minB != minA) {
                return Integer.compare(minB, minA);
            }

            int puntosA = a.getPtsCol1() + a.getPtsCol2();
            int puntosB = b.getPtsCol1() + b.getPtsCol2();

            return Integer.compare(puntosB, puntosA);
        });

        // Definir los puntos jornada por posición
        int[] puntosPorPosicion = {ptsJornada, ptsJornada / 2, ptsJornada / 4, ptsJornada / 8};

        int posicionActual = 0;
        int indice = 0;

        while (indice < resulOrdenado.size() && posicionActual < puntosPorPosicion.length) {
            ResultadosApuestas base = resulOrdenado.get(indice);
            int ac1 = base.getAciertos1();
            int ac2 = base.getAciertos2();
            Set<Integer> baseSet = Set.of(ac1, ac2);

            // Encontrar todos los que tienen la misma combinación de aciertos (sin importar el orden)
            List<ResultadosApuestas> mismos = resulOrdenado.stream()
                    .filter(r -> Set.of(r.getAciertos1(), r.getAciertos2()).equals(baseSet))
                    .collect(Collectors.toList());

            for (ResultadosApuestas r : mismos) {
                r.setPtsJornada(puntosPorPosicion[posicionActual]);
                r.setPosicionReal(posicionActual + 1);
            }

            // Saltar al siguiente grupo diferente
            indice += mismos.size();
            posicionActual++;
        }

        // Informamos datos
        for (var ResultadosApuestas : resulOrdenado) {
            EquipoQuiniela eq = ResultadosApuestas.getEquipo();
            var puntosActuales
                    = eq.getPuntuaciones().get(0).getPuntos();
            int puntosNuevos;
            if (ResultadosApuestas.getAciertos1() > ResultadosApuestas.getAciertos2()) {
                puntosNuevos = ResultadosApuestas.getPtsCol1();
            } else if (ResultadosApuestas.getAciertos2() > ResultadosApuestas.getAciertos1()) {
                puntosNuevos = ResultadosApuestas.getPtsCol2();
            } else {
                puntosNuevos = Math.max(ResultadosApuestas.getPtsCol1(), ResultadosApuestas.getPtsCol2());
            }
            puntosNuevos = puntosNuevos + ResultadosApuestas.getPtsJornada();

            eq.getPuntuaciones().get(0).setPuntos(puntosActuales + puntosNuevos);            

            var victoriasActuales
                    = eq.getPuntuaciones().get(0).getVictorias();
            if (ResultadosApuestas.getPosicionReal() == 1) {
                eq.getPuntuaciones().get(0).setVictorias(victoriasActuales + 1);
            }
            eq.getEstadisiticas().get(0).setPuntos(puntosNuevos);
            eq.getEstadisiticas().get(0).setAciertos(ResultadosApuestas.getAciertosCol1()
                    + " - " + ResultadosApuestas.getAciertosCol2());
            eq.getEstadisiticas().get(0).setPosicion(ResultadosApuestas.getPosicionReal());
            
        }

    }

    public static void calculoResultadosQuiniela_obsoleto(
            ArrayList<EquipoQuiniela> eqs, String[] resultados, boolean isGeneral, int ptsJornada) {

        HashMap<EquipoQuiniela, Integer> aciertos1 = new HashMap();
        HashMap<EquipoQuiniela, Integer> aciertos2 = new HashMap();
        HashMap<EquipoQuiniela, Integer> puntos1 = new HashMap();
        HashMap<EquipoQuiniela, Integer> puntos2 = new HashMap();

        HashMap<EquipoQuiniela, Boolean> todoOk = new HashMap();

        var ptsPartido = eqs.size() * 2 * 10;

        var aciertosTotales = new Integer[15];
        for (var i = 0; i < 15; i++) {
            aciertosTotales[i] = 0;
        }

        // primero obtenemos los aciertos totales para cada partido
        for (var eq : eqs) {
            var ap1 = eq.getApuestas().get(0);
            var ap2 = eq.getApuestas().get(1);
            for (var i = 0; i < 15; i++) {
                if (ap1.getResultado()[i] != null && ap1.getResultado()[i].equals(resultados[i])) {
                    aciertosTotales[i]++;
                }
                if (ap2.getResultado()[i] != null && ap2.getResultado()[i].equals(resultados[i])) {
                    aciertosTotales[i]++;
                }
            }
        }

        // calculamos los aciertos y puntos para cada columna
        for (var eq : eqs) {
            aciertos1.put(eq, 0);
            aciertos2.put(eq, 0);
            puntos1.put(eq, 0);
            puntos2.put(eq, 0);
            var apuestas1 = eq.getApuestas().get(0).getResultado();
            var apuestas2 = eq.getApuestas().get(1).getResultado();
            todoOk.put(eq, true);

            for (var i = 0; i < 15; i++) {
                if (apuestas1[i] != null && resultados[i].equals(apuestas1[i])) {
                    aciertos1.put(eq, (Integer) aciertos1.get(eq) + 1);
                }
                if (apuestas2[i] != null && resultados[i].equals(apuestas2[i])) {
                    aciertos2.put(eq, (Integer) aciertos2.get(eq) + 1);
                }
                if (apuestas1[i] != null && resultados[i].equals(apuestas1[i])) {
                    puntos1.put(eq, (Integer) puntos1.get(eq) + ptsPartido / aciertosTotales[i]);
                }
                if (apuestas2[i] != null && resultados[i].equals(apuestas2[i])) {
                    puntos2.put(eq, (Integer) puntos2.get(eq) + ptsPartido / aciertosTotales[i]);
                }
                if (apuestas1[i] == null || apuestas2[i] == null) {
                    todoOk.put(eq, false);
                }
            }
        }

        // Ordenamos los resultados por columna segun equipo
        HashMap<EquipoQuiniela, int[]> aciertos = new HashMap();
        HashMap<EquipoQuiniela, int[]> puntos = new HashMap();
        for (var eq : eqs) {
            var r = new int[2];
            var p = new int[2];
            if (aciertos1.get(eq) > aciertos2.get(eq)) {
                r[0] = aciertos1.get(eq);
                r[1] = aciertos2.get(eq);
                p[0] = puntos1.get(eq);
                p[1] = puntos2.get(eq);
            } else {
                r[0] = aciertos2.get(eq);
                r[1] = aciertos1.get(eq);
                p[0] = puntos2.get(eq);
                p[1] = puntos1.get(eq);
            }
            aciertos.put(eq, r);
            puntos.put(eq, r);
        }

        // Ordenamos por equipo y mejor resultado
        ArrayList<Object[]> clasif = new ArrayList();

        while (aciertos.size() > 0) {
            var mayorCol1 = 0;
            var mayorCol2 = 0;
            EquipoQuiniela mayor = null;
            for (var eq : aciertos.keySet()) {
                if (mayor == null) {
                    mayor = eq;
                }
                if ((aciertos.get(eq)[0] > mayorCol1)
                        || (aciertos.get(eq)[0] == mayorCol1
                        && aciertos.get(eq)[1] > mayorCol2)) {
                    mayor = eq;
                    mayorCol1 = aciertos.get(eq)[0];
                    mayorCol2 = aciertos.get(eq)[1];
                }
            }
            var datos = new Object[4];
            datos[0] = mayor;
            datos[1] = mayorCol1;
            datos[2] = mayorCol2;
            clasif.add(datos);
            aciertos.remove(mayor);
        }

        // una vez ordenados miramos si hay repetidos y asignamos
        // en la parte 4 del vector la posicion final
        var pos = 1;
        clasif.get(0)[3] = pos;
        for (var i = 1; i < clasif.size(); i++) {
            if (clasif.get(i)[1] == clasif.get(i - 1)[1]
                    && clasif.get(i)[2] == clasif.get(i - 1)[2]) {
                clasif.get(i)[3] = pos;
            } else {
                pos++;
                clasif.get(i)[3] = pos;
            }
        }

        // damos los puntos: 
        // el primero puntos ptsPorPartido y el resto se va dividiendo por 2
        for (var i = 0; i < clasif.size(); i++) {
            var eq = (EquipoQuiniela) clasif.get(i)[0];
            int posReal = (Integer) clasif.get(i)[3];
            int puntCol = (Integer) clasif.get(i)[1];
            int puntCol2 = (Integer) clasif.get(i)[2];

            var puntosNuevos = 0;
            switch (posReal) {
                case 1 ->
                    puntosNuevos = ptsJornada;
                case 2 ->
                    puntosNuevos = (int) ptsJornada / 2;
                case 3 ->
                    puntosNuevos = (int) ptsJornada / 4;
                case 4 ->
                    puntosNuevos = (int) ptsJornada / 8;
                default -> {
                }
            }

            puntosNuevos = puntosNuevos + puntCol;

            if (!isGeneral) {

                var puntosActuales
                        = eq.getPuntuaciones().get(0).getPuntos();

                eq.getPuntuaciones().get(0).setPuntos(puntosActuales + puntosNuevos);

                var victoriasActuales
                        = eq.getPuntuaciones().get(0).getVictorias();
                if (posReal == 1) {
                    eq.getPuntuaciones().get(0).setVictorias(victoriasActuales + 1);
                }

                eq.getEstadisiticas().get(0).setPuntos(puntosNuevos);
                eq.getEstadisiticas().get(0).setAciertos(aciertos1.get(eq)
                        + " - " + valueOf(aciertos2.get(eq)));
                eq.getEstadisiticas().get(0).setPosicion(posReal);
            } else {

                //OBSOLETOOOOO
                var puntosActuales
                        = eq.getPuntuaciones().get(0).getPuntosGeneral();

                eq.getPuntuaciones().get(0).setPuntosGeneral(puntosActuales + puntosNuevos);

                var victoriasActuales
                        = eq.getPuntuaciones().get(0).getVictoriasGeneral();
                if (posReal == 1) {
                    eq.getPuntuaciones().get(0).setVictoriasGeneral(victoriasActuales + 1);
                }

                eq.getEstadisiticas().get(0).setPuntosGeneral(puntosNuevos);
                eq.getClub().setRanking(eq.getClub().getRanking() + puntCol);

            }

        }

    }

}
