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

    /**
     * @return the soloTemas
     */
    public boolean isSoloTemas() {
        return soloTemas;
    }

    /**
     * @param soloTemas the soloTemas to set
     */
    public void setSoloTemas(boolean soloTemas) {
        this.soloTemas = soloTemas;
    }
    
    private String tema;
    private int anyoInicial;
    private int anyoFinal;
    private boolean revisar;
    private boolean duplicados;
    private boolean soloTemas;
    
        public FiltroCanciones(){        

        this.tema = "";
        this.anyoInicial = 1950;
        this.anyoFinal = 2050;
        this.revisar = false;
        this.duplicados = false;
        this.soloTemas = false;

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
    
    public boolean isDuplicados() {
        return duplicados;
    }
    
    public String duplicadosTxt() {
        if (this.isDuplicados())
            return "true";
        else
            return "false";
    }

    public void setDuplicados(boolean duplicados) {
        this.duplicados = duplicados;
    }
    

    public String filtroSQL(){
        
        String sql = "";
        if (!"".equals(this.getTema()))
            sql = sql.concat("tema = '").concat(this.getTema()).concat("' AND ");
        sql = sql.concat("anyo >= ").concat(String.valueOf(this.getAnyoInicial())).concat(" AND ");
        sql = sql.concat("anyo <= ").concat(String.valueOf(this.getAnyoFinal())).concat(" AND ");
        sql = sql.concat("revisar = ").concat(this.revisarTxt());
        return sql;
        
    }
    
    
    
}
