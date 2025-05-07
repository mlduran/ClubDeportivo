/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mld.clubdeportivo.base.quinielas;

import mld.clubdeportivo.base.Puntuacion;

/**
 *
 * @author Miguel
 */
public class PuntuacionQuiniela extends Puntuacion{

    private EquipoQuiniela equipo;
    private int victorias;
    private int puntosGeneral;
    private int victoriasGeneral;

    public EquipoQuiniela getEquipo() {
        return equipo;
    }

    public void setEquipo(EquipoQuiniela equipo) {
        this.equipo = equipo;
    }

    public int getVictorias() {
        return victorias;
    }

    public void setVictorias(int victorias) {
        this.victorias = victorias;
    }

    public String getNombreEquipo(){
        return this.getEquipo().getNombre();
    }

    /**
     * @return the puntosGeneral
     */
    public int getPuntosGeneral() {
        return puntosGeneral;
    }

    /**
     * @param puntosGeneral the puntosGeneral to set
     */
    public void setPuntosGeneral(int puntosGeneral) {
        this.puntosGeneral = puntosGeneral;
    }

    /**
     * @return the victoriasGeneral
     */
    public int getVictoriasGeneral() {
        return victoriasGeneral;
    }

    /**
     * @param victoriasGeneral the victoriasGeneral to set
     */
    public void setVictoriasGeneral(int victoriasGeneral) {
        this.victoriasGeneral = victoriasGeneral;
    }
    

}
