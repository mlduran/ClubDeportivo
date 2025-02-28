/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mld.clubdeportivo.base.futbol8;

import mld.clubdeportivo.base.Estadistica;

/**
 *
 * @author Miguel
 */
public final class EstadisticaPartidoFutbol8 extends Estadistica{
    
    private PartidoFutbol8 partido;
    private String eqLocal;
    private String eqVisitante;
    private int moralLocal;
    private int moralVisitante;
    private String alineacionLocal;
    private String alineacionVisitante;
    private int golesLocal;
    private int golesVisitante;
    private int tacticaLocal;
    private int tacticaVisitante;
    private String esfuerzoLocal;
    private String esfuerzoVisitante;
    private String estrategiaLocal;
    private String estrategiaVisitante;
    private int primasLocal;
    private int primasVisitante;
    
    private int posesionLocal;
    private int posesionVisitante;
    private int jugadasLocal;
    private int jugadasVisitante;
    private int ocasionesLocal;
    private int ocasionesVisitante;
    private int tirosPuertaLocal;
    private int tirosPuertaVisitante;
    private int tirosLejanosLocal;
    private int tirosLejanosVisitante;
    private int faltasDirectasLocal;
    private int faltasDirectasVisitante;
    private int cornersLocal;
    private int cornersVisitante;
    private int penaltiesLocal;
    private int penaltiesVisitante;
    private String goleadoresLocal;
    private String goleadoresVisitante;
    private String tarjetasLocal;
    private String tarjetasVisitante;
    private String lesionadosLocal;
    private String lesionadosVisitante;
    private String extrasEqLocal;
    private String extrasEqVisitante;
    private int victoriasLocal;
    private int victoriasVisitante;
    
    public EstadisticaPartidoFutbol8(){}
    
    public EstadisticaPartidoFutbol8(PartidoFutbol8 partido){
        
        var eqLoc = (EquipoFutbol8) partido.getEqLocal();
        this.setEqLocal(eqLoc.getNombre());        
        this.setEsfuerzoLocal(eqLoc.getAlineacion().getEsfuerzo().name());
        this.setEstrategiaLocal(eqLoc.getAlineacion().getEstrategia().name());
        this.setTacticaLocal(eqLoc.getAlineacion().getTactica().getNumero());
        this.setPrimasLocal(eqLoc.getAlineacion().getPrimas());
        var eqVisit = (EquipoFutbol8) partido.getEqVisitante();
        this.setEsfuerzoVisitante(eqVisit.getAlineacion().getEsfuerzo().name());
        this.setEstrategiaVisitante(eqVisit.getAlineacion().getEstrategia().name());
        this.setTacticaVisitante(eqVisit.getAlineacion().getTactica().getNumero());
        this.setPrimasVisitante(eqVisit.getAlineacion().getPrimas());
        this.setEqVisitante(eqVisit.getNombre());
        
        this.setPartido(partido);
                
    }
    

    public PartidoFutbol8 getPartido() {
        return partido;
    }

    public void setPartido(PartidoFutbol8 partido) {
        this.partido = partido;
    }

    public String getEqLocal() {
        return eqLocal;
    }

    public void setEqLocal(String eqLocal) {
        this.eqLocal = eqLocal;
    }

    public String getEqVisitante() {
        return eqVisitante;
    }

    public void setEqVisitante(String eqVisitante) {
        this.eqVisitante = eqVisitante;
    }

    public String getAlineacionLocal() {
        return alineacionLocal;
    }

    public void setAlineacionLocal(String alineacionLocal) {
        this.alineacionLocal = alineacionLocal;
    }

    public String getAlineacionVisitante() {
        return alineacionVisitante;
    }

    public void setAlineacionVisitante(String alineacionVisitante) {
        this.alineacionVisitante = alineacionVisitante;
    }

    public int getGolesLocal() {
        return golesLocal;
    }

    public void setGolesLocal(int golesLocal) {
        this.golesLocal = golesLocal;
    }

    public int getGolesVisitante() {
        return golesVisitante;
    }

    public void setGolesVisitante(int golesVisitante) {
        this.golesVisitante = golesVisitante;
    }

    public int getTacticaLocal() {
        return tacticaLocal;
    }

    public void setTacticaLocal(int tacticaLocal) {
        this.tacticaLocal = tacticaLocal;
    }

    public int getTacticaVisitante() {
        return tacticaVisitante;
    }

    public void setTacticaVisitante(int tacticaVisitante) {
        this.tacticaVisitante = tacticaVisitante;
    }

    public String getEsfuerzoLocal() {
        return esfuerzoLocal;
    }

    public void setEsfuerzoLocal(String esfuerzoLocal) {
        this.esfuerzoLocal = esfuerzoLocal;
    }

    public String getEsfuerzoVisitante() {
        return esfuerzoVisitante;
    }

    public void setEsfuerzoVisitante(String esfuerzoVisitante) {
        this.esfuerzoVisitante = esfuerzoVisitante;
    }

    public String getEstrategiaLocal() {
        return estrategiaLocal;
    }

    public void setEstrategiaLocal(String estrategiaLocal) {
        this.estrategiaLocal = estrategiaLocal;
    }

    public String getEstrategiaVisitante() {
        return estrategiaVisitante;
    }

    public void setEstrategiaVisitante(String estrategiaVisitante) {
        this.estrategiaVisitante = estrategiaVisitante;
    }

    public int getPrimasLocal() {
        return primasLocal;
    }

    public void setPrimasLocal(int primasLocal) {
        this.primasLocal = primasLocal;
    }

    public int getPrimasVisitante() {
        return primasVisitante;
    }

    public void setPrimasVisitante(int primasVisitante) {
        this.primasVisitante = primasVisitante;
    }

    public int getPosesionLocal() {
        return posesionLocal;
    }

    public void setPosesionLocal(int posesionLocal) {
        this.posesionLocal = posesionLocal;
    }

    public int getPosesionVisitante() {
        return posesionVisitante;
    }

    public void setPosesionVisitante(int posesionVisitante) {
        this.posesionVisitante = posesionVisitante;
    }

    public int getJugadasLocal() {
        return jugadasLocal;
    }

    public void setJugadasLocal(int jugadasLocal) {
        this.jugadasLocal = jugadasLocal;
    }

    public int getJugadasVisitante() {
        return jugadasVisitante;
    }

    public void setJugadasVisitante(int jugadasVisitante) {
        this.jugadasVisitante = jugadasVisitante;
    }

    public int getOcasionesLocal() {
        return ocasionesLocal;
    }

    public void setOcasionesLocal(int ocasionesLocal) {
        this.ocasionesLocal = ocasionesLocal;
    }

    public int getOcasionesVisitante() {
        return ocasionesVisitante;
    }

    public void setOcasionesVisitante(int ocasionesVisitante) {
        this.ocasionesVisitante = ocasionesVisitante;
    }

    public int getTirosPuertaLocal() {
        return tirosPuertaLocal;
    }

    public void setTirosPuertaLocal(int tirosPuertaLocal) {
        this.tirosPuertaLocal = tirosPuertaLocal;
    }

    public int getTirosPuertaVisitante() {
        return tirosPuertaVisitante;
    }

    public void setTirosPuertaVisitante(int tirosPuertaVisitante) {
        this.tirosPuertaVisitante = tirosPuertaVisitante;
    }

    public int getFaltasDirectasLocal() {
        return faltasDirectasLocal;
    }

    public void setFaltasDirectasLocal(int faltasDirectasLocal) {
        this.faltasDirectasLocal = faltasDirectasLocal;
    }

    public int getFaltasDirectasVisitante() {
        return faltasDirectasVisitante;
    }

    public void setFaltasDirectasVisitante(int faltasDirectasVisitante) {
        this.faltasDirectasVisitante = faltasDirectasVisitante;
    }

    public int getPenaltiesLocal() {
        return penaltiesLocal;
    }

    public void setPenaltiesLocal(int penaltiesLocal) {
        this.penaltiesLocal = penaltiesLocal;
    }

    public int getPenaltiesVisitante() {
        return penaltiesVisitante;
    }

    public void setPenaltiesVisitante(int penaltiesVisitante) {
        this.penaltiesVisitante = penaltiesVisitante;
    }

    public int getCornersLocal() {
        return cornersLocal;
    }

    public void setCornersLocal(int cornersLocal) {
        this.cornersLocal = cornersLocal;
    }

    public int getCornersVisitante() {
        return cornersVisitante;
    }

    public void setCornersVisitante(int cornersVisitante) {
        this.cornersVisitante = cornersVisitante;
    }

    public String getGoleadoresLocal() {
        return goleadoresLocal;
    }

    public void setGoleadoresLocal(String goleadoresLocal) {
        this.goleadoresLocal = goleadoresLocal;
    }
    
     public String[] getGoleadoresLocalTxt() {
        return this.getGoleadoresLocal().split(";");
    }

    public String getGoleadoresVisitante() {
        return goleadoresVisitante;
    }
    
    public String[] getGoleadoresVisitanteTxt() {
        return this.getGoleadoresVisitante().split(";");
    }

    public void setGoleadoresVisitante(String goleadoresVisitante) {
        this.goleadoresVisitante = goleadoresVisitante;
    }

    public String getTarjetasLocal() {
        return tarjetasLocal;
    }

    public void setTarjetasLocal(String tarjetasLocal) {
        this.tarjetasLocal = tarjetasLocal;
    }

    public String getTarjetasVisitante() {
        return tarjetasVisitante;
    }

    public void setTarjetasVisitante(String tarjetasVisitante) {
        this.tarjetasVisitante = tarjetasVisitante;
    }

    public String getExtrasEqLocal() {
        return extrasEqLocal;
    }

    public void setExtrasEqLocal(String extrasEqLocal) {
        this.extrasEqLocal = extrasEqLocal;
    }

    public String getExtrasEqVisitante() {
        return extrasEqVisitante;
    }

    public void setExtrasEqVisitante(String extrasEqVisitante) {
        this.extrasEqVisitante = extrasEqVisitante;
    }
    
    public String[] getExtrasEqLocalTxt() {
        return this.getExtrasEqLocal().split(";");
    }

    public String[] getExtrasEqVisitanteTxt() {
        return this.getExtrasEqVisitante().split(";");
    }

    public String getLesionadosVisitante() {
        return lesionadosVisitante;
    }

    public void setLesionadosVisitante(String lesionadosVisitante) {
        this.lesionadosVisitante = lesionadosVisitante;
    }

    public String getLesionadosLocal() {
        return lesionadosLocal;
    }

    public void setLesionadosLocal(String lesionadosLocal) {
        this.lesionadosLocal = lesionadosLocal;
    }

    public String[] getTarjetasLocalTxt() {
        return this.getTarjetasLocal().split(";");
    }
     
    public String[] getTarjetasVisitanteTxt() {
        return this.getTarjetasVisitante().split(";");
    }
   
    public String[] getLesionadosLocalTxt() {
        return this.getLesionadosLocal().split(";");
    }
     
     public String[] getLesionadosVisitanteTxt() {
        return this.getLesionadosVisitante().split(";");
    }

    public int getMoralLocal() {
        return moralLocal;
    }

    public void setMoralLocal(int moralLocal) {
        this.moralLocal = moralLocal;
    }

    public int getMoralVisitante() {
        return moralVisitante;
    }

    public void setMoralVisitante(int moralVisitante) {
        this.moralVisitante = moralVisitante;
    }

    public int getTirosLejanosLocal() {
        return tirosLejanosLocal;
    }

    public void setTirosLejanosLocal(int tirosLejanosLocal) {
        this.tirosLejanosLocal = tirosLejanosLocal;
    }

    public int getTirosLejanosVisitante() {
        return tirosLejanosVisitante;
    }

    public void setTirosLejanosVisitante(int tirosLejanosVisitante) {
        this.tirosLejanosVisitante = tirosLejanosVisitante;
    }

    public int getVictoriasLocal() {
        return victoriasLocal;
    }

    public void setVictoriasLocal(int victoriasLocal) {
        this.victoriasLocal = victoriasLocal;
    }

    public int getVictoriasVisitante() {
        return victoriasVisitante;
    }

    public void setVictoriasVisitante(int victoriasVisitante) {
        this.victoriasVisitante = victoriasVisitante;
    }
    
   
}
