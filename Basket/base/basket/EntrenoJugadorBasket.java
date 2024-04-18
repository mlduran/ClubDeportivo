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
public final class EntrenoJugadorBasket {
    
    private Long id;
    private String nombre;
    private PosicionJugBasket posicion;
    private int VAInicial;
    private int VAFinal;
    private int EFInicial;
    private int EFFinal;
    private int eqTecnico;
    private boolean puedeEntrenar;
    private JugadorBasket jugador;
    private JuvenilBasket juvenil;
    
    public EntrenoJugadorBasket(JugadorBasket jug){
        this.setId(jug.getId());
        this.setNombre(jug.getNombre());
        this.setPosicion(jug.getPosicion());
        this.setVAInicial(jug.getValoracion());
        this.setVAFinal(jug.getValoracion());
        this.setEFInicial(jug.getEstadoFisico());
        this.setEFFinal(jug.getEstadoFisico());
        this.setEqTecnico(jug.getEquipo().getEqTecnico());
        boolean puede = jug.getJornadasLesion() == 0 && !jug.getEquipo().isEntrenamiento();
        this.setPuedeEntrenar(puede);
        this.setJugador(jug);
    }

    public EntrenoJugadorBasket(JuvenilBasket jug) {
        
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

    public PosicionJugBasket getPosicion() {
        return posicion;
    }

    public void setPosicion(PosicionJugBasket posicion) {
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
    
    public JugadorBasket getJugador() {
        return jugador;
    }

    public void setJugador(JugadorBasket jugador) {
        this.jugador = jugador;
    }
    
    
    public void entrenar(PosicionJugBasket pos){
        
        int numTecs = this.getEqTecnico();
        
        // por cada tecnico el jugador sube un 10 punto en 100 y
        // 1 en 100
        int subida = ((1100 - this.getVAInicial()) / 100) * numTecs;
        
        // el 50% es fijo y el resto un ramdom
        subida = (subida / 2) + (Calculos.valorAleatorio(subida / 2));
        
        int bajada = subida / 10;
        
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
        
        PosicionJugBasket pos = null;
        try {
            pos = PosicionJugBasket.valueOf(posTxt);
        } catch (Exception ex){}
                    
        this.entrenar(pos);
        
    }
        

   public String getColorVAInicial(){

        return Jugador.colorFondoCelda(this.getVAInicialMostrada());
    }


    public String getColorVAFinal(){

        return Jugador.colorFondoCelda(this.getVAFinalMostrada());
    }
    
    public String getColorEFInicial(){

        return Jugador.colorFondoCelda(this.getEFInicial());
    }

    public String getColorEFFinal(){

        return Jugador.colorFondoCelda(this.getEFFinal());
    }

    public JuvenilBasket getJuvenil() {
        return juvenil;
    }

    public void setJuvenil(JuvenilBasket juvenil) {
        this.juvenil = juvenil;
    }
  
}
