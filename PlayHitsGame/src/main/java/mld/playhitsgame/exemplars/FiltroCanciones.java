/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.exemplars;

/**
 *
 * @author miguel
 */
public class FiltroCanciones {
    
    private String genero;
    private String idioma;
    private String tema;
    private int anyoInicial;
    private int anyoFinal;
    private boolean revisar;
    
        public FiltroCanciones(){
        
        this.genero = Genero.Generico.toString();
        this.idioma = Idioma.English.toString();
        this.tema = "";
        this.anyoInicial = 1950;
        this.anyoFinal = 2050;
        this.revisar = false;

    }

    /**
     * @return the genero
     */
    public String getGenero() {
        return genero;
    }

    /**
     * @param genero the genero to set
     */
    public void setGenero(String genero) {
        this.genero = genero;
    }

    /**
     * @return the idioma
     */
    public String getIdioma() {
        return idioma;
    }

    /**
     * @param idioma the idioma to set
     */
    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    /**
     * @return the tema
     */
    public String getTema() {
        return tema;
    }

    /**
     * @param tema the tema to set
     */
    public void setTema(String tema) {
        this.tema = tema;
    }

    /**
     * @return the anyoInicial
     */
    public int getAnyoInicial() {
        return anyoInicial;
    }

    /**
     * @param anyoInicial the anyoInicial to set
     */
    public void setAnyoInicial(int anyoInicial) {
        this.anyoInicial = anyoInicial;
    }

    /**
     * @return the anyoFinal
     */
    public int getAnyoFinal() {
        return anyoFinal;
    }

    /**
     * @param anyoFinal the anyoFinal to set
     */
    public void setAnyoFinal(int anyoFinal) {
        this.anyoFinal = anyoFinal;
    }

    /**
     * @return the revisar
     */
    public boolean isRevisar() {
        return revisar;
    }
    
    public String revisarTxt() {
        if (this.isRevisar())
            return "true";
        else
            return "false";
    }

    /**
     * @param revisar the revisar to set
     */
    public void setRevisar(boolean revisar) {
        this.revisar = revisar;
    }
    

    public String filtroSQL(){
        
        String sql = "";
        
        sql = sql.concat("genero = '").concat(this.getGenero()).concat("' AND ");
        sql = sql.concat("idioma = '").concat(this.getIdioma()).concat("' AND ");
        if (!"".equals(this.getTema()))
            sql = sql.concat("tema = '").concat(this.getTema()).concat("' AND ");
        sql = sql.concat("anyo >= ").concat(String.valueOf(this.getAnyoInicial())).concat(" AND ");
        sql = sql.concat("anyo <= ").concat(String.valueOf(this.getAnyoFinal())).concat(" AND ");
        sql = sql.concat("revisar = ").concat(this.revisarTxt());
        return sql;
        
    }
    
    
    
}
