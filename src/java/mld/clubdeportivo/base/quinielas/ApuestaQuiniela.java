
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

    public boolean isCumplentadaOK(){
        
        boolean ok = true;
        
        if (this.getResultado() == null || this.getResultado().length < 15)
            ok = false;
        else{
            for (int i = 0; i < 15; i++){
                String r = this.getResultado()[i];
                if (r == null || (!r.equals("1") && !r.equals("X") && !r.equals("2"))){
                    ok = false;
                    break;
                }
            }
        }
        
        return ok;
    }
   

}
