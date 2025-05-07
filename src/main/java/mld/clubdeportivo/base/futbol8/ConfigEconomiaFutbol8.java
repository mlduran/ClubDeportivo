/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mld.clubdeportivo.base.futbol8;

import mld.clubdeportivo.base.ConfigEconomia;
import mld.clubdeportivo.base.Grupo;

/**
 *
 * @author mlopezd
 */
public final class ConfigEconomiaFutbol8 extends ConfigEconomia{
    
    private int porcentajePremioLiga;
    private int porcentajeCampeonCopa; // el subcampeon se lleva el resto
    
    private int PREMIO_LIGA_DEFECTO = 10;
    private int PREMIO_COPA_DEFECTO = 70;
    
    public ConfigEconomiaFutbol8(){}
    
    public ConfigEconomiaFutbol8(Grupo grp){
        
        this.setGrupo(grp);
        this.setEquipoGestor(null);
        this.setDiasGestion(0); // cambio a los MAX_DIAS_GESTION
        this.setModificado(false);
        this.setInteresCredito(INTERESES_CREDITO_DEFECTO);
        this.setRetencionHaciendaBase(RETENCION_HACIENDA_DEFECTO);
        this.setRetencionLineal(true);
        this.setSubidaMaxBolsa(SUBIDA_MAX_BOLSA_DEFECTO);
        this.setIva(IVA_DEFECTO);
        this.setPorcentajePremioLiga(PREMIO_LIGA_DEFECTO);
        this.setPorcentajeCampeonCopa(PREMIO_COPA_DEFECTO);
        this.setIBI(IBI_DEFECTO);
        
    }
    

    public int getPorcentajePremioLiga() {
        return porcentajePremioLiga;
    }

    public void setPorcentajePremioLiga(int porcentajePremioLiga) {
        if (porcentajePremioLiga < 5 || porcentajePremioLiga > 15)
            throw new IllegalArgumentException("Valor porcentaje premio liga incorrecto");
        this.porcentajePremioLiga = porcentajePremioLiga;
    }

    public int getPorcentajeCampeonCopa() {
        return porcentajeCampeonCopa;
    }
    
    public int getPorcentajeSubCampeonCopa() {
        return 100 - this.getPorcentajeCampeonCopa();
    }

    public void setPorcentajeCampeonCopa(int porcentajeCampeonCopa) {
        if (porcentajeCampeonCopa < 60 || porcentajeCampeonCopa > 80)
            throw new IllegalArgumentException("Valor porcentaje premio copa incorrecto");
        this.porcentajeCampeonCopa = porcentajeCampeonCopa;
    }
    
}
