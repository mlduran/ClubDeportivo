/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mld.clubdeportivo.base.basket;

import mld.clubdeportivo.base.Jugador;
import mld.clubdeportivo.utilidades.Calculos;

/**
 *
 * @author mlopezd
 */
public final class JuvenilBasket extends Jugador{
    
    private EquipoBasket equipo;
    private PosicionJugBasket posicion;
    private int valoracion;
    private int jornadas;
    
    public JuvenilBasket(){}

    // Para alta de jugador
    public JuvenilBasket(EquipoBasket equipo,
            PosicionJugBasket posicion){

        this.setEquipo(equipo);
        this.setNombre("Juvenil");
        this.setPosicion(posicion);
        this.setValoracion(100);
        this.setJornadas(0);       

    }


    public EquipoBasket getEquipo() {
        return equipo;
    }

    public void setEquipo(EquipoBasket equipo) {
        this.equipo = equipo;
    }


    public PosicionJugBasket getPosicion() {
        return posicion;
    }


    public void setPosicion(PosicionJugBasket posicion) {
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
        
        if (Calculos.obtener(2))
            this.setValoracion(this.getValoracion() - 10);
        if (this.getValoracion() < 100) this.setValoracion(100);
        
    }
    
}
