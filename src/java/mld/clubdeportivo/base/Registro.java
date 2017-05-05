package mld.clubdeportivo.base;

import java.util.Date;

/**
 *
 * @author Miguel
 */
public final class Registro extends Objeto {

    private String aplicacion;
    private TipoRegistro tipo;
    private String ip;
    private String usuario;
    private Date fecha;
    private String observaciones;


    public Registro(){};

    public Registro(String ip){

        this.setAplicacion("");
        this.setTipo(TipoRegistro.Visita);
        this.setIp(ip);
        this.setUsuario("");
        this.setFecha(new Date());
        this.setObservaciones("");

    }

    public String getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(String aplicacion) {
        this.aplicacion = aplicacion;
    }

    public TipoRegistro getTipo() {
        return tipo;
    }

    public void setTipo(TipoRegistro tipo) {
        this.tipo = tipo;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }


}

