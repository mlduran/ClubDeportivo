/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mld.clubdeportivo.base.quinielas;

import static java.lang.String.valueOf;
import static java.lang.String.valueOf;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Miguel
 */
public class CalculosQuiniela {

    public static void calculoResultadosQuiniela(
            ArrayList<EquipoQuiniela> eqs, String[] resultados, boolean isGeneral, int ptsPorPartido){


        HashMap<EquipoQuiniela, Integer> aciertos1 = new HashMap();
        HashMap<EquipoQuiniela, Integer> aciertos2 = new HashMap();
        HashMap<EquipoQuiniela, Boolean> todoOk = new HashMap();
        
        // calculamos los aciertos para cada columna
        for (var eq : eqs) {
            aciertos1.put(eq, 0);
            aciertos2.put(eq, 0);
            var apuestas1 = eq.getApuestas().get(0).getResultado();
            var apuestas2 = eq.getApuestas().get(1).getResultado();
            todoOk.put(eq, true);

            for (var i = 0; i < 15; i++){
                if (apuestas1[i] != null && resultados[i].equals(apuestas1[i]))
                    aciertos1.put(eq, (Integer) aciertos1.get(eq) + 1);
                if (apuestas2[i] != null && resultados[i].equals(apuestas2[i]))
                    aciertos2.put(eq, (Integer) aciertos2.get(eq) + 1);
                if (apuestas1[i] == null || apuestas2[i] == null)
                    todoOk.put(eq, false);
            }
        }
        
        // si algun equipo tiene el unico resultado acertado le damos 
        // un punto añadido para ese resultado
        if (eqs.size() > 1){
            for (var i = 0; i < 15; i++){
                var totalAcertados = 0;
                for (var eq : eqs) {
                    var apuestas1 = eq.getApuestas().get(0).getResultado();
                    var apuestas2 = eq.getApuestas().get(1).getResultado();
                     if (apuestas1[i] != null && resultados[i].equals(apuestas1[i]) ||
                         apuestas2[i] != null && resultados[i].equals(apuestas2[i])){
                         totalAcertados++;
                     }
                }
                if (totalAcertados > 0){
                    var ptsAsig = ptsPorPartido / totalAcertados;
                    for (var eq : eqs) {
                        var apuestas1 = eq.getApuestas().get(0).getResultado();
                        var apuestas2 = eq.getApuestas().get(1).getResultado();
                        if (apuestas1[i] != null && resultados[i].equals(apuestas1[i]))
                            aciertos1.put(eq, (Integer) aciertos1.get(eq) + ptsAsig);
                        if (apuestas2[i] != null && resultados[i].equals(apuestas2[i]))
                            aciertos2.put(eq, (Integer) aciertos2.get(eq) + ptsAsig);
                    }
                    
                }                
            }            
        }

        // Ordenamos los resultados por columna segun equipo
        HashMap<EquipoQuiniela, int[]> aciertos = new HashMap();
        for (var eq : eqs) {
            var r = new int[2];
            if (aciertos1.get(eq) > aciertos2.get(eq)){
                r[0] = aciertos1.get(eq);
                r[1] = aciertos2.get(eq);
            }else{
                r[0] = aciertos2.get(eq);
                r[1] = aciertos1.get(eq);
            }
            aciertos.put(eq, r);
        }

        // Ordenamos por equipo y mejor resultado
        ArrayList<Object[]> clasif = new ArrayList();

        while (aciertos.size() > 0){
            var mayorCol1 = 0;
            var mayorCol2 = 0;
            EquipoQuiniela mayor = null;
            for (var eq : aciertos.keySet()) {
                if (mayor == null) mayor = eq;
                if ((aciertos.get(eq)[0] > mayorCol1) ||
                    (aciertos.get(eq)[0] == mayorCol1 &&
                     aciertos.get(eq)[1] > mayorCol2))
                {
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
        for (var i = 1; i < clasif.size(); i++){
            if (clasif.get(i)[1] == clasif.get(i - 1)[1] &&
                clasif.get(i)[2] == clasif.get(i - 1)[2])
                clasif.get(i)[3] = pos;
            else{
                pos++;
                clasif.get(i)[3] = pos;
            }
        }

        // damos los puntos: 
        // el primero puntos * 4
        // el segundo puntos * 2

        for (var i = 0; i < clasif.size(); i++){
            var eq = (EquipoQuiniela) clasif.get(i)[0];
            int posReal = (Integer) clasif.get(i)[3];
            int puntCol = (Integer) clasif.get(i)[1];
            int puntCol2 = (Integer) clasif.get(i)[2];            

            var puntosNuevos = 0;
            switch (posReal) {
                case 1 -> puntosNuevos = ptsPorPartido * 4;
                case 2 -> puntosNuevos = ptsPorPartido * 2;
                case 3 -> puntosNuevos = ptsPorPartido * 1;
                case 4 -> puntosNuevos = (int) (ptsPorPartido * 0.5);
                default -> {
                }
            }
                                   
            puntosNuevos = puntosNuevos + puntCol;
            
            if (!isGeneral){

                var puntosActuales =
                    eq.getPuntuaciones().get(0).getPuntos();
                
                eq.getPuntuaciones().get(0).setPuntos(puntosActuales + puntosNuevos);
                
                var victoriasActuales =
                        eq.getPuntuaciones().get(0).getVictorias();
                if (posReal == 1)
                    eq.getPuntuaciones().get(0).setVictorias(victoriasActuales + 1);
                
                eq.getEstadisiticas().get(0).setPuntos(puntosNuevos);
                eq.getEstadisiticas().get(0).setAciertos(valueOf(puntCol) +
                        " - " + valueOf(puntCol2));
                eq.getEstadisiticas().get(0).setPosicion(posReal);
            }
            else{
                
                var puntosActuales =
                    eq.getPuntuaciones().get(0).getPuntosGeneral();
                
                eq.getPuntuaciones().get(0).setPuntosGeneral(puntosActuales + puntosNuevos);
                
                var victoriasActuales =
                        eq.getPuntuaciones().get(0).getVictoriasGeneral();
                if (posReal == 1)
                    eq.getPuntuaciones().get(0).setVictoriasGeneral(victoriasActuales + 1);
                
                eq.getEstadisiticas().get(0).setPuntosGeneral(puntosNuevos);
                eq.getClub().setRanking(eq.getClub().getRanking() + puntCol);
                
            }
        
        }

    }

}
