/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.exemplars;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author miguel
 */
public class FiltroTemas {

    private List<Idioma> idiomas;
    private List<Genero> generos;
    private Usuario usuario;

    public FiltroTemas(Locale idioma) {

        this.idiomas = new ArrayList<>();
        this.idiomas.add(Idioma.International);
        if ("es".equals(idioma.getLanguage()))
            this.idiomas.add(Idioma.Spanish);        
        
        this.generos = new ArrayList<>();
        this.generos.addAll(Arrays.asList(Genero.values()));

    }
    
    public List<Tema> filtrarTemas( List<Tema> temas){
        
        ArrayList<Tema> temasFiltados = new ArrayList();
        
        for (Tema tema : temas){
            if (this.getIdiomas().contains(tema.getIdioma()) &&
                    this.getGeneros().contains(tema.getGenero()))
                temasFiltados.add(tema);
        }
        
        return temasFiltados;
    }

    /**
     * @return the idiomas
     */
    public List<Idioma> getIdiomas() {
        return idiomas;
    }

    /**
     * @param idiomas the idiomas to set
     */
    public void setIdiomas(List<Idioma> idiomas) {
        this.idiomas = idiomas;
    }

    /**
     * @return the genero
     */
    public List<Genero> getGeneros() {
        return generos;
    }

    /**
     * @param generos the genero to set
     */
    public void setGenero(List<Genero> generos) {
        this.generos = generos;
    }

    /**
     * @return the usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    

}
