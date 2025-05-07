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
public final class PorteroFutbol8 extends Objeto{
    
    private CompeticionFutbol8 competicion;
    private String equipo;
    private String nombre;
    private int golesLiga;
    private int golesCopa;
    private int partidos;
    
    public PorteroFutbol8(){}

    // Para alta de jugador
    public PorteroFutbol8(CompeticionFutbol8 comp, JugadorFutbol8 jug){

        this.setCompeticion(comp);
        this.setNombre(jug.getNombre());
        this.setEquipo(jug.getNombreEquipo());
        this.setPartidos(jug.getPartidosJugados());
        this.setGolesLiga(jug.getGolesLiga());
        this.setGolesCopa(jug.getGolesCopa());       

    }

    public CompeticionFutbol8 getCompeticion() {
        return competicion;
    }

    public void setCompeticion(CompeticionFutbol8 competicion) {
        this.competicion = competicion;
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

    public int getPartidos() {
        return partidos;
    }
    
    public int getPartidosJugados() {
        return this.getPartidos();
    }

    public void setPartidos(int partidos) {
        this.partidos = partidos;
    }
    
    
    public double getRankingPortero(){
        
        var minPartidos = 15;
        
        float r = 0;
        if (this.getPartidos() > 0)
            r = (float) ((float) (this.getGolesLiga() + this.getGolesCopa()) / (float) this.getPartidos());
        
        // Si no se han jugado los partidos minimos le sumamos la penalizacion
        if (this.getPartidos() < minPartidos)
            r = r + ((minPartidos - this.getPartidos()) / 2);
        
        return r;
    }
    
}
