
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
    private int puntos;
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


    public int getPuntos() {
        return puntos;
    }


    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public boolean resultadosCompletos(){
        
        boolean completo = true;
        
        for (String result : this.getResultado()) {
            if (result == null || (!result.equals("1") && !result.equals("X") && !result.equals("2"))) {
                completo = false;
                break;
            }
        }
        
        return completo;
    }
    
    public String getDescripcionPuntos(){
        
        String pts = "";
        if (this.getPuntos() > 0)
            pts = " (Puntos " + String.valueOf(this.getPuntos()) + ")"; 
        return this.getDescripcion() + pts;
    }

}
