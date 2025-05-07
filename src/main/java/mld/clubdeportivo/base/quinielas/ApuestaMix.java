
package mld.clubdeportivo.base.quinielas;

import static java.lang.String.valueOf;

/**
 *
 * @author Miguel
 */
public final class ApuestaMix {

    private String numero;
    private String partido;
    private boolean col1;
    private boolean colX;
    private boolean col2;

    public ApuestaMix(){};

    public ApuestaMix(String num, String partido,
            boolean col1, boolean colX, boolean col2){

        this.setNumero(num);
        this.setPartido(partido);
        this.setCol1(col1);
        this.setColX(colX);
        this.setCol2(col2);

    }

    public String getNumero() {
        if (numero.length() == 1) return "0" + numero;
        else return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getPartido() {
        return partido;
    }

    public void setPartido(String partido) {
        this.partido = partido;
    }

    public boolean isCol1() {
        return col1;
    }

    public void setCol1(boolean col1) {
        this.col1 = col1;
    }

    public boolean isColX() {
        return colX;
    }

    public void setColX(boolean colX) {
        this.colX = colX;
    }

    public boolean isCol2() {
        return col2;
    }

    public void setCol2(boolean col2) {
        this.col2 = col2;
    }

    public void setNumero(int i) {
        this.numero = valueOf(i);
    }

}
