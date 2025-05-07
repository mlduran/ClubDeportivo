/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mld.clubdeportivo.base;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Miguel
 */
public final class Bolsa extends Objeto{
    
    private Date fecha;
    private int valor;
    
    public Bolsa(){}
    
    public Bolsa(int valor){
        
        this.setFecha(new Date());
        this.setValor(valor); 
        
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }
    
    public String toJson() {
        
        var txt = new StringBuilder();
        var formato = new SimpleDateFormat("dd/MM/yyyy");
       
        txt.append('{');
        txt.append("\"fecha\": \"").append(formato.format(this.getFecha())).append("\"");
        txt.append(',');
        txt.append("\"valor\": ").append(this.getValor());
        txt.append('}');
        
        return txt.toString();
    }
    
}
