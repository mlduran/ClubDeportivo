
package mld.clubdeportivo.base.futbol8;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.parseInt;
import static java.lang.Integer.parseInt;
import static java.lang.Integer.parseInt;
import mld.clubdeportivo.base.Jugador;
import static mld.clubdeportivo.base.Jugador.colorFondoCelda;

/**
 *
 * @author Miguel
 * Esta clase solo es da apoyo para poder realizar la presentacion mas comoda
 */
public class EstadisticaAlineacionFutbol8 {
    
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

        return colorFondoCelda(parseInt(this.getValoracionLocal()));
    }
    
    public String getColorValoracionVisitante(){

        return colorFondoCelda(parseInt(this.getValoracionVisitante()));
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

        return colorFondoCelda(parseInt(this.getValoracionPartidoLocal()));
    }
    
    public String getColorValoracionPartidoVisitante(){

        return colorFondoCelda(parseInt(this.getValoracionPartidoVisitante()));
    }
    
}

