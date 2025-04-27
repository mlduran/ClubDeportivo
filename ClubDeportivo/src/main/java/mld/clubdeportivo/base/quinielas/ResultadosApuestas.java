/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mld.clubdeportivo.base.quinielas;

/**
 *
 * @author Miguel
 */
public class ResultadosApuestas {
    

    private EquipoQuiniela equipo;
    private String aciertosCol1;
    private String aciertosCol2;
    private int ptsCol1;
    private int ptsCol2;

    /**
     * @return the aciertosCol1
     */
    public String getAciertosCol1() {
        return aciertosCol1;
    }

    /**
     * @param aciertosCol1 the aciertosCol1 to set
     */
    public void setAciertosCol1(String aciertosCol1) {
        this.aciertosCol1 = aciertosCol1;
    }

    /**
     * @return the aciertosCol2
     */
    public String getAciertosCol2() {
        return aciertosCol2;
    }

    /**
     * @param aciertosCol2 the aciertosCol2 to set
     */
    public void setAciertosCol2(String aciertosCol2) {
        this.aciertosCol2 = aciertosCol2;
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
