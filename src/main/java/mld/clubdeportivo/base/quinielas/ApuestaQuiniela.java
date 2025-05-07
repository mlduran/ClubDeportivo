
package mld.clubdeportivo.base.quinielas;

import java.util.Date;
import mld.clubdeportivo.base.Objeto;

/**
 *
 * @author mlopezd
 */
public class ApuestaQuiniela extends Objeto {

    private EquipoQuiniela equipo;
    private JornadaQuiniela jornada;
    private String[] resultado; // 1 X 2
    private int[] puntos;
    private Date actualizada;

    public JornadaQuiniela getJornada() {
        return jornada;
    }

    public void setJornada(JornadaQuiniela jornada) {
        this.jornada = jornada;
    }

    public EquipoQuiniela getEquipo() {
        return equipo;
    }

    public void setEquipo(EquipoQuiniela equipo) {
        this.equipo = equipo;
    }

       public String[] getResultado() {
        return resultado;
    }

    public void setResultado(String[] resultado) {
        this.resultado = resultado;
    }

    /**
     * @return the actualizada
     */
    public Date getActualizada() {
        return actualizada;
    }

    /**
     * @param actualizada the actualizada to set
     */
    public void setActualizada(Date actualizada) {
        this.actualizada = actualizada;
    }
    
    /**
     * @return the puntoscol1
     */
    public int[] getPuntos() {
        return puntos;
    }

    /**
     * @param puntos the puntos to set
     */
    public void setPuntos(int[] puntos) {
        this.puntos = puntos;
    }

 
    public boolean isCumplentadaOK(){
        
        var ok = true;
        
        if (this.getResultado() == null || this.getResultado().length < 15)
            ok = false;
        else{
            for (var i = 0; i < 15; i++){
                var r = this.getResultado()[i];
                if (r == null || (!r.equals("1") && !r.equals("X") && !r.equals("2"))){
                    ok = false;
                    break;
                }
            }
        }
        
        return ok;
    }
   

}
