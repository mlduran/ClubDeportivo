/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mld.clubdeportivo.base.quinielas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import mld.clubdeportivo.base.Estadistica;

/**
 *
 * @author Miguel
 */
public class EstadisticaQuiniela extends Estadistica {

    private String equipo;
    private String competicion;
    private String jornada;
    private int puntos;
    private String aciertos;
    private int puntosGeneral;
    private int posicion;

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    public String getCompeticion() {
        return competicion;
    }

    public void setCompeticion(String competicion) {
        this.competicion = competicion;
    }

    public String getJornada() {
        return jornada;
    }

    public void setJornada(String jornada) {
        this.jornada = jornada;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public String getAciertos() {
        return aciertos;
    }

    public void setAciertos(String aciertos) {
        this.aciertos = aciertos;
    }

    public int getPuntosGeneral() {
        return puntosGeneral;
    }

    public void setPuntosGeneral(int puntosGeneral) {
        this.puntosGeneral = puntosGeneral;
    }
    
    public static ArrayList<EstadisticaQuiniela> clasificar(ArrayList<EstadisticaQuiniela> lista, boolean isGrupo){
        
        if (isGrupo)
            Collections.sort(lista, new ClasificacionComparatorGrupo());
        else 
            Collections.sort(lista, new ClasificacionComparatorGeneral());
        return lista;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }
            
    
    
   // Collections.sort(eqs, new ClasificacionComparator());
    
     private static class ClasificacionComparatorGrupo
            implements Comparator<EstadisticaQuiniela> {

        public int compare(EstadisticaQuiniela o1, EstadisticaQuiniela o2) {

            if (o1 == null || o2 == null)
                throw new NullPointerException("Referencia nula");
            
            int result;
            
            if  (o1.getPuntos() > o2.getPuntos()) result = -1;
            else result = 1;
            
            return result;
        }
    }

    public static Comparator<EstadisticaQuiniela> ClasificacionComparatorGrupo(){

        return new ClasificacionComparatorGrupo();

    }
    
     private static class ClasificacionComparatorGeneral
            implements Comparator<EstadisticaQuiniela> {

        public int compare(EstadisticaQuiniela o1, EstadisticaQuiniela o2) {

            if (o1 == null || o2 == null)
                throw new NullPointerException("Referencia nula");
            
            int result;
            
            if  (o1.getPuntos() > o2.getPuntos()) result = -1;
            else result = 1;
            
            return result;
        }
    }

    public static Comparator<EstadisticaQuiniela> ClasificacionComparatorGeneral(){

        return new ClasificacionComparatorGrupo();

    }
    
}
