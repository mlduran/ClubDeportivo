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

    private String numero;
    private String partido;
    private String columna1;
    private String columna2;
    private String resultado;
    private String url;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
