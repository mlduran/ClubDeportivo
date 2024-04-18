/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mld.clubdeportivo.base.quinielas;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Miguel
 */
public class CalculosQuiniela {

    public static void calculoResultadosQuiniela(
            ArrayList<EquipoQuiniela> eqs, String[] resultados, boolean isGeneral, int puntos){


        HashMap<EquipoQuiniela, Integer> aciertos1 = new HashMap();
        HashMap<EquipoQuiniela, Integer> aciertos2 = new HashMap();
        HashMap<EquipoQuiniela, Boolean> todoOk = new HashMap();

        // calculamos los aciertos para cada columna
        for (EquipoQuiniela eq : eqs) {
            aciertos1.put(eq, 0);
            aciertos2.put(eq, 0);
            String[] apuestas1 = eq.getApuestas().get(0).getResultado();
            String[] apuestas2 = eq.getApuestas().get(1).getResultado();
            todoOk.put(eq, true);

            for (int i = 0; i < 15; i++){
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
            for (int i = 0; i < 15; i++){
                int totalAcertados = 0;
                for (EquipoQuiniela eq : eqs) {
                    String[] apuestas1 = eq.getApuestas().get(0).getResultado();
                    String[] apuestas2 = eq.getApuestas().get(1).getResultado();
                     if (apuestas1[i] != null && resultados[i].equals(apuestas1[i]) ||
                         apuestas2[i] != null && resultados[i].equals(apuestas2[i])){
                         totalAcertados++;
                     }
                }
                if (totalAcertados == 1){
                    for (EquipoQuiniela eq : eqs) {
                        String[] apuestas1 = eq.getApuestas().get(0).getResultado();
                        String[] apuestas2 = eq.getApuestas().get(1).getResultado();
                        if (apuestas1[i] != null && resultados[i].equals(apuestas1[i]))
                            aciertos1.put(eq, (Integer) aciertos1.get(eq) + 1);
                        if (apuestas2[i] != null && resultados[i].equals(apuestas2[i]))
                            aciertos2.put(eq, (Integer) aciertos2.get(eq) + 1);
                    }
                    
                }                
            }            
        }

        // Ordenamos los resultados por columna segun equipo
        HashMap<EquipoQuiniela, int[]> aciertos = new HashMap();
        for (EquipoQuiniela eq : eqs) {
            int[] r = new int[2];
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
            int mayorCol1 = 0;
            int mayorCol2 = 0;
            EquipoQuiniela mayor = null;
            for (EquipoQuiniela eq : aciertos.keySet()) {
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
            Object[] datos = new Object[4];
            datos[0] = mayor;
            datos[1] = mayorCol1;
            datos[2] = mayorCol2;
            clasif.add(datos);
            aciertos.remove(mayor);
        }

        // una vez ordenados miramos si hay repetidos y asignamos
        // en la parte 4 del vector la posicion final
        int pos = 1;
        clasif.get(0)[3] = pos;
        for (int i = 1; i < clasif.size(); i++){
            if (clasif.get(i)[1] == clasif.get(i - 1)[1] &&
                clasif.get(i)[2] == clasif.get(i - 1)[2])
                clasif.get(i)[3] = pos;
            else{
                pos++;
                clasif.get(i)[3] = pos;
            }
        }

        // damos los puntos: el primero 16 y el resto van dividiendo
        // por 2 y ademas se suman los aciertos de la columna 1
        // A partir de 10 aciertos se suman 5, 10, 20, 40, 80 respectivamente 

        for (int i = 0; i < clasif.size(); i++){
            EquipoQuiniela eq = (EquipoQuiniela) clasif.get(i)[0];
            int posReal = (Integer) clasif.get(i)[3];
            int puntCol = (Integer) clasif.get(i)[1];
            int puntCol2 = (Integer) clasif.get(i)[2];            

            int puntosNuevos = 0;

            if (posReal == 1) puntosNuevos = puntos;
            else if (posReal == 2) puntosNuevos = puntos / 2;
            else if (posReal == 3) puntosNuevos = puntos / 4;
            else if (posReal == 4) puntosNuevos = puntos / 8;
            
            int suplemento = 0;

            if (puntCol == 10) suplemento = 5;
            else if (puntCol == 11) suplemento = 10;
            else if (puntCol == 12) suplemento = 25;
            else if (puntCol == 13) suplemento = 50;
            else if (puntCol == 14) suplemento = 200;
            else if (puntCol >= 15) suplemento = 500;            
            else if (todoOk.get(eq) && puntCol == 0 && puntCol2 == 0)
                suplemento = 500;
            else if (todoOk.get(eq) && (puntCol == 0 || puntCol2 == 0))
                suplemento = 100;
            else if (todoOk.get(eq) && (puntCol == 1 || puntCol2 == 1))
                suplemento = 50;
                       
            puntosNuevos = puntosNuevos + puntCol + suplemento;
            
            if (!isGeneral){

                int puntosActuales =
                    eq.getPuntuaciones().get(0).getPuntos();
                
                eq.getPuntuaciones().get(0).setPuntos(puntosActuales + puntosNuevos);
                
                int victoriasActuales =
                        eq.getPuntuaciones().get(0).getVictorias();
                if (posReal == 1)
                    eq.getPuntuaciones().get(0).setVictorias(victoriasActuales + 1);
                
                eq.getEstadisiticas().get(0).setPuntos(puntosNuevos);
                eq.getEstadisiticas().get(0).setAciertos(String.valueOf(puntCol) +
                        " - " + String.valueOf(puntCol2));
                eq.getEstadisiticas().get(0).setPosicion(posReal);
            }
            else{
                
                int puntosActuales =
                    eq.getPuntuaciones().get(0).getPuntosGeneral();
                
                eq.getPuntuaciones().get(0).setPuntosGeneral(puntosActuales + puntosNuevos);
                
                int victoriasActuales =
                        eq.getPuntuaciones().get(0).getVictoriasGeneral();
                if (posReal == 1)
                    eq.getPuntuaciones().get(0).setVictoriasGeneral(victoriasActuales + 1);
                
                eq.getEstadisiticas().get(0).setPuntosGeneral(puntosNuevos);
                eq.getClub().setRanking(eq.getClub().getRanking() + puntCol);
                
            }
            
            
            
            
                
                
        }


    }

}
