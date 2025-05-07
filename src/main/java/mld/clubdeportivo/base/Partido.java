/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mld.clubdeportivo.base;

/**
 *
 * @author Miguel
 */
public class Partido extends Objeto {

    private Jornada jornada;
    private Equipo eqLocal;
    private Equipo eqVisitante;
    private String descripcion;

    private int espectadores;
    private int PrecioEntradas;

    public Jornada getJornada() {
        return jornada;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }

    public Equipo getEqLocal() {
        return eqLocal;
    }

    public String getNombreEqLocal() {
        return this.getEqLocal().getNombre();
    }

    public void setEqLocal(Equipo eqLocal) {
        this.eqLocal = eqLocal;
    }

    public Equipo getEqVisitante() {
        return eqVisitante;
    }

    public String getNombreEqVisitante() {
        return this.getEqVisitante().getNombre();
    }

    public void setEqVisitante(Equipo eqVisitante) {
        this.eqVisitante = eqVisitante;
    }

    public int getEspectadores() {
        return espectadores;
    }

    public void setEspectadores(int espectadores) {
        this.espectadores = espectadores;
    }

    public int getPrecioEntradas() {
        return PrecioEntradas;
    }

    public void setPrecioEntradas(int PrecioEntradas) {
        this.PrecioEntradas = PrecioEntradas;
    }
    
    public String getTextoPartido(){
        var txt = "";
        if (this.getEqLocal() != null && this.getEqVisitante() != null)
            txt = this.getNombreEqLocal() + " - " + this.getNombreEqVisitante();
        return txt;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


}
