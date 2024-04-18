/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mld.clubdeportivo.base.basket;

import mld.clubdeportivo.base.Vacaciones;

/**
 *
 * @author Miguel
 */
public final class VacacionBasket extends Vacaciones {
    
    
    private EquipoBasket equipo;
    private long idEq; 
    private boolean renovacion;
    private boolean activarTactica;
    private TacticaBasket tactica;
    private boolean activarEntreno;
    private PosicionElegidaBasket posicionEntreno;

    public VacacionBasket(){}


    public VacacionBasket(EquipoBasket eq){

        this.setActivo(false);
        this.setEquipo(eq);
        this.setRenovacion(false);
        this.setActivarTactica(false);
        this.setTactica(eq.getEntrenador().getTacticaAleatoria());
        this.setActivarEntreno(false);
        this.setPosicionEntreno(PosicionElegidaBasket.Cualquiera);

    };

    public EquipoBasket getEquipo() {
        return equipo;
    }

    public void setEquipo(EquipoBasket equipo) {
        this.equipo = equipo;
    }

    public long getIdEq() {
        return idEq;
    }

    public void setIdEq(long idEq) {
        this.idEq = idEq;
    }

    public boolean isRenovacion() {
        return renovacion;
    }

    public void setRenovacion(boolean renovacion) {
        this.renovacion = renovacion;
    }

    public boolean isActivarTactica() {
        return activarTactica;
    }

    public void setActivarTactica(boolean activarTactica) {
        this.activarTactica = activarTactica;
    }

    public TacticaBasket getTactica() {
        return tactica;
    }

    public void setTactica(TacticaBasket tactica) {
        this.tactica = tactica;
    }

    public boolean isActivarEntreno() {
        return activarEntreno;
    }

    public void setActivarEntreno(boolean activarEntreno) {
        this.activarEntreno = activarEntreno;
    }

    public PosicionElegidaBasket getPosicionEntreno() {
        return posicionEntreno;
    }

    public void setPosicionEntreno(PosicionElegidaBasket posicionEntreno) {
        this.posicionEntreno = posicionEntreno;
    }

   
   


}
