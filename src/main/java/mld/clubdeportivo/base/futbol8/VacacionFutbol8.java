/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mld.clubdeportivo.base.futbol8;

import mld.clubdeportivo.base.Vacaciones;
import static mld.clubdeportivo.base.futbol8.PosicionElegidaFutbol8.Cualquiera;

/**
 *
 * @author Miguel
 */
public final class VacacionFutbol8 extends Vacaciones {
    
    
    private EquipoFutbol8 equipo;
    private long idEq; 
    private boolean renovacion;
    private boolean activarTactica;
    private TacticaFutbol8 tactica;
    private boolean activarEntreno;
    private PosicionElegidaFutbol8 posicionEntreno;

    public VacacionFutbol8(){}


    public VacacionFutbol8(EquipoFutbol8 eq){

        this.setActivo(false);
        this.setEquipo(eq);
        this.setRenovacion(false);
        this.setActivarTactica(false);
        this.setTactica(eq.getEntrenador().getTacticaAleatoria());
        this.setActivarEntreno(false);
        this.setPosicionEntreno(Cualquiera);

    };

    public EquipoFutbol8 getEquipo() {
        return equipo;
    }

    public void setEquipo(EquipoFutbol8 equipo) {
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

    public TacticaFutbol8 getTactica() {
        return tactica;
    }

    public void setTactica(TacticaFutbol8 tactica) {
        this.tactica = tactica;
    }

    public boolean isActivarEntreno() {
        return activarEntreno;
    }

    public void setActivarEntreno(boolean activarEntreno) {
        this.activarEntreno = activarEntreno;
    }

    public PosicionElegidaFutbol8 getPosicionEntreno() {
        return posicionEntreno;
    }

    public void setPosicionEntreno(PosicionElegidaFutbol8 posicionEntreno) {
        this.posicionEntreno = posicionEntreno;
    }

   
   


}
