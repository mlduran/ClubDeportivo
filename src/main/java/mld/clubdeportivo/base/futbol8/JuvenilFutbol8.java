/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mld.clubdeportivo.base.futbol8;

import mld.clubdeportivo.base.Jugador;
import mld.clubdeportivo.utilidades.Calculos;
import static mld.clubdeportivo.utilidades.Calculos.obtener;

/**
 *
 * @author mlopezd
 */
public final class JuvenilFutbol8 extends Jugador{
    
    private EquipoFutbol8 equipo;
    private PosicionJugFutbol8 posicion;
    private int valoracion;
    private int jornadas;
    
    public JuvenilFutbol8(){}

    // Para alta de jugador
    public JuvenilFutbol8(EquipoFutbol8 equipo,
            PosicionJugFutbol8 posicion){

        this.setEquipo(equipo);
        this.setNombre("Juvenil");
        this.setPosicion(posicion);
        this.setValoracion(100);
        this.setJornadas(0);       

    }


    public EquipoFutbol8 getEquipo() {
        return equipo;
    }

    public void setEquipo(EquipoFutbol8 equipo) {
        this.equipo = equipo;
    }


    public PosicionJugFutbol8 getPosicion() {
        return posicion;
    }


    public void setPosicion(PosicionJugFutbol8 posicion) {
        this.posicion = posicion;
    }


    public int getValoracion() {
        return valoracion;
    }


    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }
    
    public int getValoracionReal() {
        return this.getValoracion() / 10;
    }
    
    public String getColorValoracion(){

        return colorFondoCelda(this.getValoracionReal());
    }


    public int getJornadas() {
        return jornadas;
    }


    public void setJornadas(int jornadas) {
        this.jornadas = jornadas;
    }
    
    public boolean isIncorporacion() {
        return this.getJornadas() > 20;
    }

    public void bajarValoracion() {
        
        if (obtener(2))
            this.setValoracion(this.getValoracion() - 10);
        if (this.getValoracion() < 100) this.setValoracion(100);
        
    }
    
}
