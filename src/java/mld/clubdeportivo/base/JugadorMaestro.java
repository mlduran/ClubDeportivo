
package mld.clubdeportivo.base;

/**
 *
 * @author Miguel
 */


public abstract class JugadorMaestro extends Objeto
        implements Comparable<JugadorMaestro>{

    private String nombre;


    public int compareTo(JugadorMaestro o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

   
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {

        if (nombre == null || nombre.length() > 50)
            throw new IllegalArgumentException("Parametro incorrecto");

        this.nombre = nombre;
    }



}