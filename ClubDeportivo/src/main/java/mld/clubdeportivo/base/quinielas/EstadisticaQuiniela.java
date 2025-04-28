/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mld.clubdeportivo.base.quinielas;

import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import static java.util.Collections.sort;
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
            sort(lista, new ClasificacionComparatorGrupo());
        else 
            sort(lista, new ClasificacionComparatorGeneral());
        return lista;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }
            
    
    
   // Collections.sort(eqs, new ClasificacionComparator());
    
    private static void cambiar(int a, int b)
{
        var temp = a;
     a = b;
     b = temp;
}
    
    private static int casoEmpatePuntos(EstadisticaQuiniela o1, EstadisticaQuiniela o2)
    {
        var result = 1;
        
        String[] sAciertosO1;
        String[] sAciertosO2;
        sAciertosO1 = o1.getAciertos().split(" - ");
        sAciertosO2 = o2.getAciertos().split(" - ");
        
        var aciertosO1 = new int[2];
        var aciertosO2 = new int[2];
        
        aciertosO1[0] = parseInt(sAciertosO1[0]);
        aciertosO1[1] = parseInt(sAciertosO1[1]);        
        if (aciertosO1[1] > aciertosO1[0]) cambiar(aciertosO1[0],aciertosO1[1]);        
                        
        aciertosO2[0] = parseInt(sAciertosO2[0]);
        aciertosO2[1] = parseInt(sAciertosO2[1]);
        if (aciertosO2[1] > aciertosO2[0]) cambiar(aciertosO2[0],aciertosO2[1]);
        
        if (aciertosO1[0] > aciertosO2[0]) result = -1;
        else if (aciertosO1[1] > aciertosO2[1]) result = -1;
                
        return result;
        
    }
    
     private static class ClasificacionComparatorGrupo
            implements Comparator<EstadisticaQuiniela> {

        public int compare(EstadisticaQuiniela o1, EstadisticaQuiniela o2) {

            if (o1 == null || o2 == null)
                throw new NullPointerException("Referencia nula");
            
            int result;
            
            if  (o1.getPuntos() == o2.getPuntos())
            {
                result = casoEmpatePuntos(o1, o2);
            }
            else if (o1.getPuntos() > o2.getPuntos()) result = -1;
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
