/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mld.clubdeportivo.base;

import static mld.clubdeportivo.base.Equipo.CANTIDAD_BASE;

/**
 *
 * @author mlopezd
 */
public class ConfigEconomia extends Objeto{
    
    private Grupo grupo;
    private Equipo equipoGestor;
    private Long idEquipoGestor;
    private int diasGestion; // para rotar cada 20 jornadas
    private boolean modificado;
    private int interesCredito;
    private int retencionHaciendaBase;
    private boolean retencionLineal; //si es lineal o escalonada segun ingresos
    private int iva; // se aplica a todas las inversiones y traspasos
    private int IBI; // se aplica al estadio
    private int subidaMaxBolsa;
    private boolean crackBolsa;

       
    public static int MAX_DIAS_GESTION = 20;
    protected static int INTERESES_CREDITO_DEFECTO = 5;
    protected static int RETENCION_HACIENDA_DEFECTO = 5;
    protected static int SUBIDA_MAX_BOLSA_DEFECTO = 20;
    protected static int IVA_DEFECTO = 10;
    protected static int IBI_DEFECTO = 10;
    
     

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Equipo getEquipoGestor() {
        return equipoGestor;
    }

    public void setEquipoGestor(Equipo equipoGestor) {
        this.equipoGestor = equipoGestor;
    }

    public int getDiasGestion() {
        return diasGestion;
    }

    public void setDiasGestion(int diasGestion) {
        this.diasGestion = diasGestion;
    }

    public boolean isModificado() {
        return modificado;
    }

    public void setModificado(boolean modificado) {
        this.modificado = modificado;
    }

    public int getInteresCredito() {
        return interesCredito;
    }

    public void setInteresCredito(int interesCredito) {
        if (interesCredito < 4 || interesCredito > 10)
            throw new IllegalArgumentException("Valor de interes credito incorrecto");
        this.interesCredito = interesCredito;
    }

    public int getRetencionHaciendaBase() {
        return retencionHaciendaBase;
    }
    
    public int getPorcentajeRetencionHacienda(int cantidad) {
        
        var retencion = this.getRetencionHaciendaBase();
        if (!this.isRetencionLineal())
            retencion = retencion + (cantidad / CANTIDAD_BASE);
        
        return retencion;
        
    }
    
    public int getRetencionHacienda(int cantidad) {

        var hacienda = cantidad * this.getPorcentajeRetencionHacienda(cantidad) / 100;            
        
        return hacienda;
    }

    public void setRetencionHaciendaBase(int retencionHaciendaBase) {
        if (retencionHaciendaBase < 4 || retencionHaciendaBase > 15)
            throw new IllegalArgumentException("Valor de retencion hacienda incorrecto");
        this.retencionHaciendaBase = retencionHaciendaBase;
    }

    public boolean isRetencionLineal() {
        return retencionLineal;
    }

    public void setRetencionLineal(boolean retencionLineal) {
        this.retencionLineal = retencionLineal;
    }

    public int getIva() {
        return iva;
    }

    public void setIva(int iva) {
         if (iva < 5 || iva > 20)
            throw new IllegalArgumentException("Valor de IVA incorrecto");
        this.iva = iva;
    }

    public int getSubidaMaxBolsa() {
        return subidaMaxBolsa;
    }
    
    public int getBajadaMaxBolsa() {
        return this.getSubidaMaxBolsa() - 5;
    }

    public void setSubidaMaxBolsa(int subidaMaxBolsa) {
         if (subidaMaxBolsa < 10 || subidaMaxBolsa > 25)
            throw new IllegalArgumentException("Valor de maxima subida bolsa incorrecto");
        this.subidaMaxBolsa = subidaMaxBolsa;
    }

    public boolean isCrackBolsa() {
        return crackBolsa;
    }

    public void setCrackBolsa(boolean crackBolsa) {
        this.crackBolsa = crackBolsa;
    }

    public Long getIdEquipoGestor() {
        return idEquipoGestor;
    }

    public void setIdEquipoGestor(Long idEquipoGestor) {
        this.idEquipoGestor = idEquipoGestor;
    }
    
    public boolean isModificable(Equipo eq){
        
        var mod = true;
        
        if (this.getEquipoGestor() == null ||
                !eq.equals(this.getEquipoGestor()) ||
                this.isModificado())
            return false;
        
        return mod;
        
    }

    public int getIBI() {
        return IBI;
    }

    public void setIBI(int IBI) {
        if (IBI < 5 || IBI > 20)
            throw new IllegalArgumentException("Valor de IBI incorrecto");
        this.IBI = IBI;
    }
    
}
