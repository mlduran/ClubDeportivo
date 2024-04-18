/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mld.clubdeportivo.base.basket;

import java.util.ArrayList;

/**
 *
 * @author Miguel
 */
public final class EstadisticaResultadosBasket {
    
    private EstadisticaPartidoBasket partido;
    private String partidoDes;
    private String equipo;
    private boolean ganado; 
    private boolean empatado; 
    private boolean perdido; 
    private int posGanar; 
    private int posEmpatar; 
    private int posPerder;


    public EstadisticaResultadosBasket(EstadisticaPartidoBasket partido, EquipoBasket eq){
        
        this.setPartido(partido);        
        this.setEquipo(eq.getNombre());
        this.setPartidoDes(partido.getEqLocal() + " - " + partido.getEqVisitante());
        int golesEq = 0, golesOtro = 0, empate;
        if (partido.getEqLocal().equals(eq.getNombre())){
            golesEq = partido.getGolesLocal();
            golesOtro = partido.getGolesVisitante();
            this.setPosGanar(partido.getVictoriasLocal());
            this.setPosPerder(partido.getVictoriasVisitante());
            empate = 100 - partido.getVictoriasLocal() - partido.getVictoriasVisitante();
            this.setPosEmpatar(empate);
            
        }
        else if (partido.getEqVisitante().equals(eq.getNombre())){
            golesEq = partido.getGolesVisitante();
            golesOtro = partido.getGolesLocal();
            this.setPosGanar(partido.getVictoriasVisitante());
            this.setPosPerder(partido.getVictoriasLocal());
            empate = 100 - partido.getVictoriasLocal() - partido.getVictoriasVisitante();
            this.setPosEmpatar(empate);
        }
        
        this.setGanado(false);
        this.setPerdido(false);
        this.setEmpatado(false);        
        if (golesEq > golesOtro)
            this.setGanado(true);
        else if (golesEq < golesOtro)
            this.setPerdido(true);
        else
            this.setEmpatado(true);
        
        
    }
    

    public boolean isGanado() {
        return ganado;
    }

    public void setGanado(boolean ganado) {
        this.ganado = ganado;
    }

    public boolean isEmpatado() {
        return empatado;
    }

    public void setEmpatado(boolean empatado) {
        this.empatado = empatado;
    }

    public boolean isPerdido() {
        return perdido;
    }

    public void setPerdido(boolean perdido) {
        this.perdido = perdido;
    }

    public int getPosGanar() {
        return posGanar;
    }

    public void setPosGanar(int posGanar) {
        this.posGanar = posGanar;
    }

    public int getPosEmpatar() {
        return posEmpatar;
    }

    public void setPosEmpatar(int posEmpatar) {
        this.posEmpatar = posEmpatar;
    }

    public int getPosPerder() {
        return posPerder;
    }

    public void setPosPerder(int posPerder) {
        this.posPerder = posPerder;
    }
    
    public EstadisticaPartidoBasket getPartido() {
        return partido;
    }

    public void setPartido(EstadisticaPartidoBasket partido) {
        this.partido = partido;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }
    
    public String getPartidoDes() {
        return partidoDes;
    }

    public void setPartidoDes(String partidoDes) {
        this.partidoDes = partidoDes;
    }
    
    public String getResultado(){
        
        String result = "";
        if (this.isGanado()) result = "Ganado";
        else if (this.isPerdido()) result = "Perdido";
        else if (this.isEmpatado()) result = "Empatado";
        
        return result;
    }
    
    
    static public int getMediaGanados(ArrayList<EstadisticaResultadosBasket> resultados){
        
        int ganados = 0;
        int media = 0;
        
        if (resultados.size() > 0){
            for (EstadisticaResultadosBasket est : resultados) 
                if (est.isGanado()) ganados++;
            media = ganados * 100 / resultados.size();
        }
            
        return media;
    }
    
    static public int getMediaEmpatados(ArrayList<EstadisticaResultadosBasket> resultados){
        
        int empatados = 0;
        int media = 0;
        
        if (resultados.size() > 0){
            for (EstadisticaResultadosBasket est : resultados) 
                if (est.isEmpatado()) empatados++;
            media = empatados * 100 / resultados.size();
        }
            
        return media;
    }
    
    static public int getMediaPerdidos(ArrayList<EstadisticaResultadosBasket> resultados){
        
        int perdidos = 0;
        int media = 0;
        
        if (resultados.size() > 0){
            for (EstadisticaResultadosBasket est : resultados) 
                if (est.isPerdido()) perdidos++;
            media = perdidos * 100 / resultados.size();
        }
            
        return media;
    }

    
    static public int getMediaOpcionesGanar(ArrayList<EstadisticaResultadosBasket> resultados){
        
        int suma = 0;
        int media = 0;
        
        if (resultados.size() > 0){
            for (EstadisticaResultadosBasket est : resultados) 
                suma = suma + est.getPosGanar();
            media = suma / resultados.size();
        }
            
        return media;
    }
    
    static public int getMediaOpcionesEmpatar(ArrayList<EstadisticaResultadosBasket> resultados){
        
        int suma = 0;
        int media = 0;
        
        if (resultados.size() > 0){
            for (EstadisticaResultadosBasket est : resultados) 
                suma = suma + est.getPosEmpatar();
            media = suma / resultados.size();
        }
            
        return media;
    }
    
    static public int getMediaOpcionesPerder(ArrayList<EstadisticaResultadosBasket> resultados){
        
        int suma = 0;
        int media = 0;
        
        if (resultados.size() > 0){
            for (EstadisticaResultadosBasket est : resultados) 
                suma = suma + est.getPosPerder();
            media = suma / resultados.size();
        }
            
        return media;
    }
   
    
    
}
