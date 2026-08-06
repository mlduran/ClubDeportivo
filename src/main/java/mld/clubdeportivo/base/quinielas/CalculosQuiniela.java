/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mld.clubdeportivo.base.quinielas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Miguel
 */
public class CalculosQuiniela {

    public static void calculoResultadosQuiniela(
            ArrayList<EquipoQuiniela> eqs, String[] resultados, int ptsJornada) {

        var aciertosTotales = new Integer[15];
        Arrays.fill(aciertosTotales, 0);

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
        // La ordenamos por puntos de las columnas
        ArrayList<ResultadosApuestas> resulOrdenado = new ArrayList<>(resulApuestas);
        resulOrdenado.sort((a, b) -> {

            int maxPtsA = Math.max(a.getPtsCol1(), a.getPtsCol2());
            int maxPtsB = Math.max(b.getPtsCol1(), b.getPtsCol2());

            if (maxPtsB != maxPtsA) {
                return Integer.compare(maxPtsB, maxPtsA);
            }

            int minPtsA = Math.min(a.getPtsCol1(), a.getPtsCol2());
            int minPtsB = Math.min(b.getPtsCol1(), b.getPtsCol2());

            return Integer.compare(minPtsB, minPtsA);
        });

        // Definir los puntos jornada por posición
        int[] puntosPorPosicion = {ptsJornada, ptsJornada / 2, ptsJornada / 4, ptsJornada / 8};

        int posicionActual = 0;
        int indice = 0;

        while (indice < resulOrdenado.size()) {
            ResultadosApuestas base = resulOrdenado.get(indice);
            int pts1 = base.getPtsCol1();
            int pts2 = base.getPtsCol2();

            List<Integer> baseList = Stream.of(pts1, pts2)
                    .sorted()
                    .collect(Collectors.toList());

            List<ResultadosApuestas> mismos = resulOrdenado.stream()
                    .filter(r -> {
                        List<Integer> otros = Stream.of(r.getPtsCol1(), r.getPtsCol2())
                                .sorted()
                                .collect(Collectors.toList());
                        return otros.equals(baseList);
                    })
                    .collect(Collectors.toList());

            for (ResultadosApuestas r : mismos) {
                if (posicionActual < puntosPorPosicion.length) {
                    r.setPtsJornada(puntosPorPosicion[posicionActual]);
                } else {
                    r.setPtsJornada(0);
                }
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

            // Bonus por aciertos
            int maxAciertos = Math.max(ResultadosApuestas.getAciertos1(), ResultadosApuestas.getAciertos2());

            switch (maxAciertos) {
                case 15 -> puntosNuevos += 3000;
                case 14 -> puntosNuevos += 2000;
                case 13 -> puntosNuevos += 1000;
                case 12 -> puntosNuevos += 500;
                default -> {
                }
            }

            puntosNuevos += ResultadosApuestas.getPtsJornada();
            puntosNuevos = puntosNuevos + ResultadosApuestas.getPtsJornada();

            eq.getPuntuaciones().get(0).setPuntos(puntosActuales + puntosNuevos);

            var victoriasActuales
                    = eq.getPuntuaciones().get(0).getVictorias();
            if (ResultadosApuestas.getPosicionReal() == 1) {
                eq.getPuntuaciones().get(0).setVictorias(victoriasActuales + 1);
            }
            eq.getEstadisiticas().get(0).setPuntos(puntosNuevos);
            // ordenamos aciertos
            String aciertosCol1 = ResultadosApuestas.getAciertosCol1();
            String aciertosCol2 = ResultadosApuestas.getAciertosCol2();
            if (Integer.parseInt(aciertosCol1) < Integer.parseInt(aciertosCol2)) {
                aciertosCol1 = ResultadosApuestas.getAciertosCol2();
                aciertosCol2 = ResultadosApuestas.getAciertosCol1();
            }

            eq.getEstadisiticas().get(0).setAciertos(aciertosCol1 + " - " + aciertosCol2);
            eq.getEstadisiticas().get(0).setPosicion(ResultadosApuestas.getPosicionReal());

        }

    }

}
