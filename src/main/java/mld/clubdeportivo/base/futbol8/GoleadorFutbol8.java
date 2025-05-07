/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mld.clubdeportivo.base.futbol8;

import mld.clubdeportivo.base.Objeto;

/**
 *
 * @author mlopezd
 */
public final class GoleadorFutbol8 extends Objeto{
    
    private CompeticionFutbol8 competicion;
    private PosicionJugFutbol8 posicion;
    private String equipo;
    private String nombre;
    private int golesLiga;
    private int golesCopa;
    
    public GoleadorFutbol8(){}

    // Para alta de jugador
    public GoleadorFutbol8(CompeticionFutbol8 comp, JugadorFutbol8 jug){

        this.setCompeticion(comp);
        this.setNombre(jug.getNombre());
        this.setEquipo(jug.getNombreEquipo());
        this.setPosicion(jug.getPosicion());
        this.setGolesLiga(jug.getGolesLiga());
        this.setGolesCopa(jug.getGolesCopa());       

    }

    public CompeticionFutbol8 getCompeticion() {
        return competicion;
    }

    public void setCompeticion(CompeticionFutbol8 competicion) {
        this.competicion = competicion;
    }

    public PosicionJugFutbol8 getPosicion() {
        return posicion;
    }

    public void setPosicion(PosicionJugFutbol8 posicion) {
        this.posicion = posicion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getGolesLiga() {
        return golesLiga;
    }

    public void setGolesLiga(int golesLiga) {
        this.golesLiga = golesLiga;
    }

    public int getGolesCopa() {
        return golesCopa;
    }

    public void setGolesCopa(int golesCopa) {
        this.golesCopa = golesCopa;
    }

    public String getEquipo() {
        return equipo;
    }
    
    public String getNombreEquipo() {
        return this.getEquipo();
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    public String getColorValoracion() {
        return "white";
    }
    
    public String getValoracionReal() {
        return "";
    }
    
    public int getGolesTemporada(){
        return this.getGolesCopa() + this.getGolesLiga();
    }
    
}
