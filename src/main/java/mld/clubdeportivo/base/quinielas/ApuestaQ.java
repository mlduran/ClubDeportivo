/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mld.clubdeportivo.base.quinielas;

/**
 *
 * @author Miguel
 */
public class ApuestaQ {

    private EquipoQuiniela equipo;
    private String numero;
    private String partido;
    private String columna1;
    private String columna2;
    private String resultado;
    private int ptsCol1;
    private int ptsCol2;

    public String getPartido() {
        return partido;
    }

    public void setPartido(String partido) {
        this.partido = partido;
    }

    public String getColumna1() {
        return columna1;
    }

    public void setColumna1(String columna1) {
        this.columna1 = columna1;
    }

    public String getColumna2() {
        return columna2;
    }

    public void setColumna2(String columna2) {
        this.columna2 = columna2;
    }

    public String getNumero() {
        if (numero.length() == 1) return "0" + numero;
        else return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getColorFondoCol1() {
        if (this.getResultado() != null &&
                this.getResultado().equals(this.getColumna1()))
            return "GREEN";
        else
            return "RED";
    }

    public String getColorFondoCol2() {
        if (this.getResultado() != null &&
                this.getResultado().equals(this.getColumna2()))
            return "GREEN";
        else
            return "RED";
    }

    /**
     * @return the ptsCol1
     */
    public int getPtsCol1() {
        return ptsCol1;
    }

    /**
     * @param ptsCol1 the ptsCol1 to set
     */
    public void setPtsCol1(int ptsCol1) {
        this.ptsCol1 = ptsCol1;
    }

    /**
     * @return the ptsCol2
     */
    public int getPtsCol2() {
        return ptsCol2;
    }

    /**
     * @param ptsCol2 the ptsCol2 to set
     */
    public void setPtsCol2(int ptsCol2) {
        this.ptsCol2 = ptsCol2;
    }

    /**
     * @return the equipo
     */
    public EquipoQuiniela getEquipo() {
        return equipo;
    }

    /**
     * @param equipo the equipo to set
     */
    public void setEquipo(EquipoQuiniela equipo) {
        this.equipo = equipo;
    }


}
