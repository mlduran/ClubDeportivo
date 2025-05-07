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
    private int aciertos1;
    private int aciertos2;
    private int ptsCol1;
    private int ptsCol2;
    private int ptsJornada;
    private int posicionReal;

        /**
     * @return the aciertos1
     */
    public int getAciertos1() {
        return aciertos1;
    }

    /**
     * @param aciertos1 the aciertos1 to set
     */
    public void setAciertos1(int aciertos1) {
        this.aciertos1 = aciertos1;
    }
    
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

    /**
     * @return the aciertos2
     */
    public int getAciertos2() {
        return aciertos2;
    }

    /**
     * @param aciertos2 the aciertos2 to set
     */
    public void setAciertos2(int aciertos2) {
        this.aciertos2 = aciertos2;
    }
    
    
    /**
     * @return the ptsJornada
     */
    public int getPtsJornada() {
        return ptsJornada;
    }

    /**
     * @param ptsJornada the ptsJornada to set
     */
    public void setPtsJornada(int ptsJornada) {
        this.ptsJornada = ptsJornada;
    }
    
      /**
     * @return the posicionReal
     */
    public int getPosicionReal() {
        return posicionReal;
    }

    /**
     * @param posicionReal the posicionReal to set
     */
    public void setPosicionReal(int posicionReal) {
        this.posicionReal = posicionReal;
    }


}
