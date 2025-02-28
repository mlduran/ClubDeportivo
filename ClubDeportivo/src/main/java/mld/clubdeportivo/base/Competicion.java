package mld.clubdeportivo.base;

import java.util.*;
import static java.util.Collections.sort;
import mld.clubdeportivo.base.futbol8.*;
import mld.clubdeportivo.utilidades.Calculos;
import mld.clubdeportivo.utilidades.JornadaTmp;

/**
 *
 * @author Miguel
 */
public abstract class Competicion extends Objeto{

    private Grupo grupo;
    private Deporte tipo;
    private String nombre;
    private boolean activa;
    private int proximaJornada;
    private int ultimaJornada;
    private Date fecha;
    private String campeon;
    private String subcampeon;
    
    public Competicion(){}

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Deporte getTipo() {
        return tipo;
    }

    public void setTipo(Deporte tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public int getProximaJornada() {
        return proximaJornada;
    }

    public void setProximaJornada(int proximaJornada) {
        this.proximaJornada = proximaJornada;
    }

    public int getUltimaJornada() {
        return ultimaJornada;
    }

    public void setUltimaJornada(int ultimaJornada) {
        this.ultimaJornada = ultimaJornada;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCampeon() {
        return campeon;
    }

    public void setCampeon(String campeon) {
        this.campeon = campeon;
    }

    public String getSubcampeon() {
        return subcampeon;
    }

    public void setSubcampeon(String subcampeon) {
        this.subcampeon = subcampeon;
    }
    
    public static ArrayList ordenarFechaDescendente(ArrayList lista){
        
        var listaOrdenada = new ArrayList<>();
        for (var comp : lista) 
            listaOrdenada.add((Competicion) comp);
        
        sort(lista, new FechaComparator());
        
        return listaOrdenada;        
        
    }

    private static class FechaComparator implements Comparator<Competicion> {

        public int compare(Competicion o1, Competicion o2) {

            if (o1 == null || o2 == null)
                throw new NullPointerException("Referencia nula");

            return o2.getFecha().compareTo(o1.getFecha());
        }
    }

    public static Comparator<Competicion> FechaComparator(){

        return new FechaComparator();

    }
      

}
