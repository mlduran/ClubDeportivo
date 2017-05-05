package mld.clubdeportivo.base;

import java.util.Date;

/**
 *
 * @author Miguel
 */
public abstract class Jornada extends Objeto{

    private int numero;
    private String descripcion;
    private Competicion competicion;
    private Date fecha;

   
    public int getNumero() {
        return numero;
    }

    public final void setNumero(int numero) {
        this.numero = numero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public final void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Competicion getCompeticion() {
        return competicion;
    }

    public final void setCompeticion(Competicion competicion) {
        this.competicion = competicion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean isDisputada(){
        return this.fecha != null;
    }
    
    
}

