
package mld.clubdeportivo.base.basket;

import mld.clubdeportivo.base.Jugador;

/**
 *
 * @author Miguel
 * Esta clase solo es da apoyo para poder realizar la presentacion mas comoda
 */
public class EstadisticaAlineacionBasket {
    
    private String jugadorLocal;
    private String posicionLocal;
    private String valoracionLocal;
    private String valoracionPartidoLocal;
    
    private String jugadorVisitante;
    private String posicionVisitante;
    private String valoracionVisitante;
    private String valoracionPartidoVisitante;

    public String getJugadorLocal() {
        return jugadorLocal;
    }

    public void setJugadorLocal(String jugadorLocal) {
        this.jugadorLocal = jugadorLocal;
    }

    public String getPosicionLocal() {
        return posicionLocal;
    }

    public void setPosicionLocal(String posicionLocal) {
        this.posicionLocal = posicionLocal;
    }

    public String getValoracionLocal() {
        return valoracionLocal;
    }

    public void setValoracionLocal(String valoracionLocal) {
        this.valoracionLocal = valoracionLocal;
    }

    public String getJugadorVisitante() {
        return jugadorVisitante;
    }

    public void setJugadorVisitante(String jugadorVisitante) {
        this.jugadorVisitante = jugadorVisitante;
    }

    public String getPosicionVisitante() {
        return posicionVisitante;
    }

    public void setPosicionVisitante(String posicionVisitante) {
        this.posicionVisitante = posicionVisitante;
    }

    public String getValoracionVisitante() {
        return valoracionVisitante;
    }

    public void setValoracionVisitante(String valoracionVisitante) {
        this.valoracionVisitante = valoracionVisitante;
    }
    
    public String getColorValoracionLocal(){

        return Jugador.colorFondoCelda(Integer.parseInt(this.getValoracionLocal()));
    }
    
    public String getColorValoracionVisitante(){

        return Jugador.colorFondoCelda(Integer.parseInt(this.getValoracionVisitante()));
    }

    public String getValoracionPartidoLocal() {
        return valoracionPartidoLocal;
    }

    public void setValoracionPartidoLocal(String valoracionPartidoLocal) {
        this.valoracionPartidoLocal = valoracionPartidoLocal;
    }

    public String getValoracionPartidoVisitante() {
        return valoracionPartidoVisitante;
    }

    public void setValoracionPartidoVisitante(String valoracionPartidoVisitante) {
        this.valoracionPartidoVisitante = valoracionPartidoVisitante;
    }
    
    public String getColorValoracionPartidoLocal(){

        return Jugador.colorFondoCelda(Integer.parseInt(this.getValoracionPartidoLocal()));
    }
    
    public String getColorValoracionPartidoVisitante(){

        return Jugador.colorFondoCelda(Integer.parseInt(this.getValoracionPartidoVisitante()));
    }
    
}

