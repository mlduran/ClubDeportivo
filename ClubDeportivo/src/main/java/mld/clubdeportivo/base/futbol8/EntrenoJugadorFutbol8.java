/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mld.clubdeportivo.base.futbol8;

import mld.clubdeportivo.base.Jugador;
import static mld.clubdeportivo.base.Jugador.colorFondoCelda;
import static mld.clubdeportivo.base.futbol8.PosicionJugFutbol8.valueOf;
import mld.clubdeportivo.utilidades.Calculos;
import static mld.clubdeportivo.utilidades.Calculos.valorAleatorio;

/**
 *
 * @author mlopezd
 */
public final class EntrenoJugadorFutbol8 {
    
    private Long id;
    private String nombre;
    private PosicionJugFutbol8 posicion;
    private int VAInicial;
    private int VAFinal;
    private int EFInicial;
    private int EFFinal;
    private int eqTecnico;
    private boolean puedeEntrenar;
    private JugadorFutbol8 jugador;
    private JuvenilFutbol8 juvenil;
    
    public EntrenoJugadorFutbol8(JugadorFutbol8 jug){
        this.setId(jug.getId());
        this.setNombre(jug.getNombre());
        this.setPosicion(jug.getPosicion());
        this.setVAInicial(jug.getValoracion());
        this.setVAFinal(jug.getValoracion());
        this.setEFInicial(jug.getEstadoFisico());
        this.setEFFinal(jug.getEstadoFisico());
        this.setEqTecnico(jug.getEquipo().getEqTecnico());
        var puede = jug.getJornadasLesion() == 0 && !jug.getEquipo().isEntrenamiento();
        this.setPuedeEntrenar(puede);
        this.setJugador(jug);
    }

    public EntrenoJugadorFutbol8(JuvenilFutbol8 jug) {
        
        this.setId(jug.getId());
        this.setNombre(jug.getNombre());
        this.setPosicion(jug.getPosicion());
        this.setVAInicial(jug.getValoracion());
        this.setVAFinal(jug.getValoracion());
        this.setEFInicial(100);
        this.setEFFinal(100);
        this.setEqTecnico(jug.getEquipo().getEqTecnico());
        this.setPuedeEntrenar(true);
        this.setJuvenil(jug);
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public PosicionJugFutbol8 getPosicion() {
        return posicion;
    }

    public void setPosicion(PosicionJugFutbol8 posicion) {
        this.posicion = posicion;
    }

    public int getVAInicial() {
        return VAInicial;
    }
    
    public int getVAInicialMostrada() {
        return this.getVAInicial()/10;
    }

    public void setVAInicial(int VAInicial) {
        this.VAInicial = VAInicial;
    }

    public int getVAFinal() {
        return VAFinal;
    }
    public int getVAFinalMostrada() {
        return this.getVAFinal()/10;
    }

    public void setVAFinal(int VAFinal) {
        this.VAFinal = VAFinal;
    }

    public int getEFInicial() {
        return EFInicial;
    }

    public void setEFInicial(int EFInicial) {
        this.EFInicial = EFInicial;
    }

    public int getEFFinal() {
        return EFFinal;
    }

    public void setEFFinal(int EFFinal) {
        this.EFFinal = EFFinal;
    }
    
    
    public int getEqTecnico() {
        return eqTecnico;
    }

    public void setEqTecnico(int eqTecnico) {
        this.eqTecnico = eqTecnico;
    }
        
    
    public boolean isPuedeEntrenar() {
        return puedeEntrenar;
    }

    public void setPuedeEntrenar(boolean puedeEntrenar) {
        this.puedeEntrenar = puedeEntrenar;
    }
    
    public JugadorFutbol8 getJugador() {
        return jugador;
    }

    public void setJugador(JugadorFutbol8 jugador) {
        this.jugador = jugador;
    }
    
    
    public void entrenar(PosicionJugFutbol8 pos){
        
        var numTecs = this.getEqTecnico();
        // por cada tecnico el jugador sube un 10 punto en 100 y
        // 1 en 100
        var subida = ((1100 - this.getVAInicial()) / 100) * numTecs;
        
        // el 50% es fijo y el resto un ramdom
        subida = (subida / 2) + (valorAleatorio(subida / 2));
        
        var bajada = subida / 10;
        
        this.setVAFinal(this.getVAFinal() + subida);
        if (this.getVAFinal() > 1000)
            this.setVAFinal(1000);
        this.setEFFinal(this.getEFFinal() - bajada);
        if (this.getEFFinal() < 10)
                 this.setEFFinal(10);
        
        if (this.getJugador() != null){        
            this.getJugador().setValoracion(this.getVAFinal());
            this.getJugador().setEstadoFisico(this.getEFFinal());
        }
        else if (this.getJuvenil() != null){    
            this.getJuvenil().setValoracion(this.getVAFinal());
        }
  
    }
    
    public void entrenar(String posTxt){
        
        PosicionJugFutbol8 pos = null;
        try {
            pos = valueOf(posTxt);
        } catch (Exception ex){}
                    
        this.entrenar(pos);
        
    }
        

   public String getColorVAInicial(){

        return colorFondoCelda(this.getVAInicialMostrada());
    }


    public String getColorVAFinal(){

        return colorFondoCelda(this.getVAFinalMostrada());
    }
    
    public String getColorEFInicial(){

        return colorFondoCelda(this.getEFInicial());
    }

    public String getColorEFFinal(){

        return colorFondoCelda(this.getEFFinal());
    }

    public JuvenilFutbol8 getJuvenil() {
        return juvenil;
    }

    public void setJuvenil(JuvenilFutbol8 juvenil) {
        this.juvenil = juvenil;
    }
  
}
