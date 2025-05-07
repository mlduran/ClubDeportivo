
package mld.clubdeportivo.base.futbol8;

import static mld.clubdeportivo.base.futbol8.ClaseTiro.Cabeza;
import static mld.clubdeportivo.base.futbol8.ClaseTiro.Falta;
import static mld.clubdeportivo.base.futbol8.ClaseTiro.Penalti;
import static mld.clubdeportivo.base.futbol8.ClaseTiro.TiroArea;
import static mld.clubdeportivo.base.futbol8.ClaseTiro.TiroLejano;
import static mld.clubdeportivo.base.futbol8.ClaseTiro.TiroLejanoSolo;
import static mld.clubdeportivo.base.futbol8.ClaseTiro.TiroSolo;
import mld.clubdeportivo.utilidades.Calculos;
import static mld.clubdeportivo.utilidades.Calculos.obtener;
import static mld.clubdeportivo.utilidades.Calculos.obtenerResultado;

/**
 *
 * @author mlopezd
 */
public final class TiroPuertaFutbol8 {
        
    private JugadorFutbol8 jugador;
    private JugadorFutbol8 portero;
    private int dificultad;
    private ClaseTiro tipo;
    private boolean esGol;
    private boolean esFuera;
    private boolean paraPortero;
    private boolean esCorner;
    
    
    public TiroPuertaFutbol8(JugadorFutbol8 jug, 
            JugadorFutbol8 por, ClaseTiro tipo){
        
        if (jug == null) 
            throw new IllegalArgumentException("El jugador no puede ser null en el tiro a puerta");
        if (tipo == null) 
            throw new IllegalArgumentException("El tipo no puede ser null en el tiro a puerta");
        
        var dif = 0;
        // porcentaje de dificultad
        if (tipo.equals(TiroLejano)) dif = 80;
        else if (tipo.equals(TiroLejanoSolo)) dif = 45;
        else if (tipo.equals(TiroArea)) dif = 40;
        else if (tipo.equals(Falta)) dif = 30;
        else if (tipo.equals(Cabeza)) dif = 25;
        else if (tipo.equals(TiroSolo)) dif = 10;
        
        this.setJugador(jug);
        this.setPortero(por);
        this.setDificultad(dif);
        this.setTipo(tipo);
        
    }
    
    public String getTxtClase(){
        
        var txt = "";
        
        if (this.getTipo().equals(TiroLejano)) txt = "Tiro Lejano";
        else if (this.getTipo().equals(TiroArea)) txt = "Tiro Area";
        else if (this.getTipo().equals(Falta)) txt = "Falta";
        else if (this.getTipo().equals(Cabeza)) txt = "Cabeza";
        else if (this.getTipo().equals(TiroSolo)) txt = "Tiro Solo";
        else if (this.getTipo().equals(TiroLejanoSolo)) txt = "Tiro Solo Lejano";
        else if (this.getTipo().equals(Penalti)) txt = "Penalti";
        
        return txt;
        
    }
    
    
    public JugadorFutbol8 getJugador() {
        return jugador;
    }

    public void setJugador(JugadorFutbol8 jugador) {
        this.jugador = jugador;
    }

    public int getDificultad() {
        return dificultad;
    }

    public void setDificultad(int dificultad) {
        this.dificultad = dificultad;
    }


    public JugadorFutbol8 getPortero() {
        return portero;
    }

    public void setPortero(JugadorFutbol8 portero) {
        this.portero = portero;
    }
    
    
    
    public void hacerTiro(){
        // devuelve true si hay gol
        // nivelDificultad 1 maxima 10 minima excepto para penalties 
                   
        var gol = false;
        this.setEsGol(gol);
        this.setEsCorner(false);
        
        if (this.getPortero() == null){
            this.setEsGol(true);
            return;
        }
        
        if (this.getTipo().equals(Penalti)){
            tirarPenalti();   
            return; 
        }
        
        var tiro = this.getJugador().getTiro();
        var valPortero = this.getPortero().getValoracionPartido();
        // el calculo de ir fuera va al 50% por la dificultad 
        // y el otro 50% por la calidad del tiro del jugador
        var porcentajeFueraPuerta = (10 * this.getDificultad()) / 2;
        porcentajeFueraPuerta = porcentajeFueraPuerta + (5 * tiro / 100);
        
        var vaFuera = obtenerResultado(porcentajeFueraPuerta, 50);
   
        this.setEsFuera(vaFuera); 

        if (!vaFuera) {    
            gol = obtenerResultado(tiro  - (tiro * this.getDificultad() / 100), valPortero);  
            if (gol) 
                this.setEsGol(gol);
            else {
                this.setParaPortero(true);
                // posibilidad de corner
                if (obtener(5 + valPortero / 20))
                    this.setEsCorner(true);                    
            }
        }

    }
    
    public void tirarPenalti(){
        // El tirador multiplica por 10 su tiro
        // hay un 10% de posibilidades de que lo tire fuera
        
        var gol = false;
        this.setEsGol(gol);
        
        var tiro = this.getJugador().getTiro() * 10;
        var fueraPuerta = tiro / 10;
        var vaPuerta = true;
        
        if (fueraPuerta > 0)
            vaPuerta= obtenerResultado(tiro, fueraPuerta);
        
        this.setEsFuera(!vaPuerta); 
        
        if (vaPuerta)  {   
            gol = obtenerResultado(tiro,
                    this.getPortero().getValoracionPartido() );             
            if (gol) 
                this.setEsGol(gol);
            else 
                this.setParaPortero(!gol);
        }

        
    }

    public ClaseTiro getTipo() {
        return tipo;
    }

    public void setTipo(ClaseTiro tipo) {
        this.tipo = tipo;
    }

    public boolean isEsGol() {
        return esGol;
    }

    public void setEsGol(boolean esGol) {
        this.esGol = esGol;
    }

    public boolean isEsFuera() {
        return esFuera;
    }

    public void setEsFuera(boolean esFuera) {
        this.esFuera = esFuera;
    }

    public boolean isParaPortero() {
        return paraPortero;
    }

    public void setParaPortero(boolean paraPortero) {
        this.paraPortero = paraPortero;
    }

    public boolean isEsCorner() {
        return esCorner;
    }

    public void setEsCorner(boolean esCorner) {
        this.esCorner = esCorner;
    }
    
}
