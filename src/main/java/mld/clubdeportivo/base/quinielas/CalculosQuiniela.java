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

    public static ArrayList<ResultadosApuestas> calcularResultadosQuiniela(
            ArrayList<EquipoQuiniela> eqs,
            String[] resultados,
            int ptsJornada) {

        /*
     * ============================================================
     * 1. Calcular los aciertos totales de cada partido
     * ============================================================
     *
     * IMPORTANTE:
     * Solo se tienen en cuenta los jugadores del grupo recibido.
         */
        var aciertosTotales = new Integer[15];
        Arrays.fill(aciertosTotales, 0);

        for (var eq : eqs) {

            var ap1 = eq.getApuestas().get(0);
            var ap2 = eq.getApuestas().get(1);

            for (int i = 0; i < 15; i++) {

                if (ap1.getResultado()[i] != null
                        && ap1.getResultado()[i].equals(resultados[i])) {

                    aciertosTotales[i]++;
                }

                if (ap2.getResultado()[i] != null
                        && ap2.getResultado()[i].equals(resultados[i])) {

                    aciertosTotales[i]++;
                }
            }
        }

        /*
     * ============================================================
     * 2. Puntos disponibles por partido
     * ============================================================
     *
     * Cada jugador tiene 2 columnas.
         */
        int ptsPartido = eqs.size() * 2 * 10;

        /*
     * ============================================================
     * 3. Calcular resultados de cada jugador
     * ============================================================
         */
        ArrayList<ResultadosApuestas> resulApuestas = new ArrayList<>();

        for (var eq : eqs) {

            var ap1 = eq.getApuestas().get(0);
            var ap2 = eq.getApuestas().get(1);

            int aciertosCol1 = 0;
            int aciertosCol2 = 0;

            int ptsCol1 = 0;
            int ptsCol2 = 0;

            int[] puntosCol1 = new int[15];
            int[] puntosCol2 = new int[15];

            for (int i = 0; i < 15; i++) {

                /*
             * ----------------------------------------------------
             * Columna 1
             * ----------------------------------------------------
                 */
                if (ap1.getResultado()[i] != null
                        && ap1.getResultado()[i].equals(resultados[i])) {

                    aciertosCol1++;

                    if (aciertosTotales[i] > 0) {

                        int puntos = ptsPartido / aciertosTotales[i];

                        ptsCol1 += puntos;
                        puntosCol1[i] = puntos;
                    }
                }

                /*
             * ----------------------------------------------------
             * Columna 2
             * ----------------------------------------------------
                 */
                if (ap2.getResultado()[i] != null
                        && ap2.getResultado()[i].equals(resultados[i])) {

                    aciertosCol2++;

                    if (aciertosTotales[i] > 0) {

                        int puntos = ptsPartido / aciertosTotales[i];

                        ptsCol2 += puntos;
                        puntosCol2[i] = puntos;
                    }
                }
            }

            /*
         * Guardamos los puntos de cada partido en las apuestas.
         *
         * Esto NO guarda en BD.
         * Simplemente deja los objetos preparados para que,
         * si posteriormente se llama a guardarResultadosQuiniela(),
         * se puedan persistir.
             */
            ap1.setPuntos(puntosCol1);
            ap2.setPuntos(puntosCol2);

            /*
         * Crear resultado del jugador.
             */
            var res = new ResultadosApuestas();

            res.setEquipo(eq);

            res.setAciertos1(aciertosCol1);
            res.setAciertos2(aciertosCol2);

            res.setAciertosCol1(String.valueOf(aciertosCol1));
            res.setAciertosCol2(String.valueOf(aciertosCol2));

            res.setPtsCol1(ptsCol1);
            res.setPtsCol2(ptsCol2);

            resulApuestas.add(res);
        }

        /*
     * ============================================================
     * 4. ORDENACIÓN
     * ============================================================
     *
     * La clasificación se hace POR ACIERTOS, no por puntos.
     *
     * Primero:
     *   mayor número de aciertos de la mejor columna.
     *
     * Segundo:
     *   mayor número de aciertos de la segunda columna.
         */
        resulApuestas.sort((a, b) -> {

            int maxAciertosA = Math.max(
                    a.getAciertos1(),
                    a.getAciertos2()
            );

            int maxAciertosB = Math.max(
                    b.getAciertos1(),
                    b.getAciertos2()
            );

            /*
         * Primero comparamos la mejor columna.
             */
            if (maxAciertosA != maxAciertosB) {

                return Integer.compare(
                        maxAciertosB,
                        maxAciertosA
                );
            }

            /*
         * Si empatan, comparamos la segunda columna.
             */
            int minAciertosA = Math.min(
                    a.getAciertos1(),
                    a.getAciertos2()
            );

            int minAciertosB = Math.min(
                    b.getAciertos1(),
                    b.getAciertos2()
            );

            return Integer.compare(
                    minAciertosB,
                    minAciertosA
            );
        });

        /*
     * ============================================================
     * 5. Puntos de jornada según posición
     * ============================================================
         */
        int[] puntosPorPosicion = {
            ptsJornada,
            ptsJornada / 2,
            ptsJornada / 4,
            ptsJornada / 8
        };

        /*
     * ============================================================
     * 6. Asignar posiciones
     * ============================================================
     *
     * Dos jugadores con los mismos aciertos en las dos columnas
     * tienen la misma posición.
     *
     * Ejemplo:
     *
     * 14 - 10 -> posición 1
     * 14 - 10 -> posición 1
     * 14 -  9 -> posición 2
     * 13 - 12 -> posición 3
         */
        int indice = 0;
        int posicionActual = 0;

        while (indice < resulApuestas.size()) {

            var base = resulApuestas.get(indice);

            int maxAciertosBase = Math.max(
                    base.getAciertos1(),
                    base.getAciertos2()
            );

            int minAciertosBase = Math.min(
                    base.getAciertos1(),
                    base.getAciertos2()
            );

            /*
         * Buscar todos los jugadores que tienen exactamente
         * los mismos aciertos en las dos columnas.
             */
            int siguiente = indice;

            while (siguiente < resulApuestas.size()) {

                var actual = resulApuestas.get(siguiente);

                int maxAciertosActual = Math.max(
                        actual.getAciertos1(),
                        actual.getAciertos2()
                );

                int minAciertosActual = Math.min(
                        actual.getAciertos1(),
                        actual.getAciertos2()
                );

                if (maxAciertosBase != maxAciertosActual
                        || minAciertosBase != minAciertosActual) {

                    break;
                }

                siguiente++;
            }

            /*
         * Todos los jugadores del grupo tienen la misma posición.
             */
            for (int i = indice; i < siguiente; i++) {

                var resultado = resulApuestas.get(i);

                resultado.setPosicionReal(
                        posicionActual + 1
                );

                if (posicionActual < puntosPorPosicion.length) {

                    resultado.setPtsJornada(
                            puntosPorPosicion[posicionActual]
                    );

                } else {

                    resultado.setPtsJornada(0);
                }
            }

            /*
         * Pasamos al siguiente grupo de resultados diferentes.
             */
            indice = siguiente;
            posicionActual++;
        }

        return resulApuestas;
    }

}
