/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mld.clubdeportivo.base;

/**
 *
 * @author Miguel
 */
public abstract class Entrenador extends Objeto
        implements Comparable<Entrenador>{
    
    public static final int JORNADAS_CONTRATO = 40;
    public static final int NUM_MAX_ENTRENADORES_LIBRES = 5;
    public static final int FICHA_INICIAL = 50;

    private String nombre;
    private Equipo equipo;
    private int ficha;
    private int contrato;
    private Grupo grupo;
    

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public int getFicha() {
        return ficha;
    }

    public void setFicha(int ficha) {
        this.ficha = ficha;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public int getContrato() {
        return contrato;
    }

    public void setContrato(int contrato) {
        this.contrato = contrato;
    }
    
    public int compareTo(Entrenador o) {

         if (o == null)
            throw new NullPointerException("Referencia nula");

        return this.getNombre().compareTo(o.getNombre());

    }
    
    public boolean equals(Entrenador obj) {

        return obj.getId() == this.getId();
    }

    

}
