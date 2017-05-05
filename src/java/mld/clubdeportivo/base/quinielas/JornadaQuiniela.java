
package mld.clubdeportivo.base.quinielas;

import java.util.ArrayList;
import mld.clubdeportivo.base.Jornada;

/**
 *
 * @author Miguel
 */
public class JornadaQuiniela extends Jornada{

    private String[] partido;
    private String[] resultado; // 1 X 2
    private boolean validada;
    private boolean bloqueada;
    private ArrayList<EstadisticaQuiniela> estadisticas;

    
    public String[] getPartido() {
        return partido;
    }

    public void setPartido(String[] partido) {
        this.partido = partido;
    }

    public String[] getResultado() {
        return resultado;
    }

    public void setResultado(String[] resultado) {
        this.resultado = resultado;
    }

    public boolean isValidada() {
        return validada;
    }

    public void setValidada(boolean validada) {
        this.validada = validada;
    }

    public ArrayList<EstadisticaQuiniela> getEstadisticas() {
        return estadisticas;
    }

    public void setEstadisticas(ArrayList<EstadisticaQuiniela> estadisticas) {
        this.estadisticas = estadisticas;
    }

    public boolean isBloqueada() {
        return bloqueada;
    }

    public void setBloqueada(boolean bloqueada) {
        this.bloqueada = bloqueada;
    }


}
