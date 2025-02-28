package mld.clubdeportivo.base.ajedrez;

import mld.clubdeportivo.base.*;


public abstract class Pieza extends Objeto
        implements Comparable<Pieza>{

   
    private String nombre;


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String jugador) {
        this.nombre = jugador;
    }

  
  
   




}
