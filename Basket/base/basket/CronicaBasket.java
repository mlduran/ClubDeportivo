/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mld.clubdeportivo.base.basket;

import java.util.ArrayList;
import mld.clubdeportivo.base.Cronica;
import mld.clubdeportivo.utilidades.StringUtil;
import mld.clubdeportivo.utilidades.UtilGenericas;

/**
 *
 * @author Miguel
 */
public final class CronicaBasket extends Cronica{
    
    private long idPartido;
    private PartidoBasket partido;
    private int minuto;
    private String accion;
    private String cuadrante;
    
    public CronicaBasket(){};
    
    private CronicaBasket(PartidoBasket partido, String accion, String cuadrante,
            int minuto){
        
        this.setPartido(partido);
        this.setIdPartido(partido.getId());
        this.setAccion(accion);
        this.setCuadrante(cuadrante);
        this.setMinuto(minuto);
    }
    
    private CronicaBasket(PartidoBasket partido, String accion, String cuadrante){
        this(partido, accion, cuadrante, 0);
    }
    
    public static CronicaBasket escribir(PartidoBasket partido, String accion){
        
        return  new CronicaBasket(partido, accion, "");        
    }   
    
    public static CronicaBasket escribir(PartidoBasket partido, String accion, String cuadrante){
        
        return  new CronicaBasket(partido, accion, cuadrante);
        
    }   
    
     public static CronicaBasket escribir(PartidoBasket partido, String accion, 
             String cuadrante, int minuto){
        
        return  new CronicaBasket(partido, accion, cuadrante, minuto);
        
    }   
   

    public long getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(long idPartido) {
        this.idPartido = idPartido;
    }

    public PartidoBasket getPartido() {
        return partido;
    }

    public void setPartido(PartidoBasket partido) {
        this.partido = partido;
    }

    public int getMinuto() {
        return minuto;
    }

    public void setMinuto(int minuto) {
        this.minuto = minuto;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getCuadrante() {
        return cuadrante;
    }

    public void setCuadrante(String cuadrante) {
        this.cuadrante = cuadrante;
    }
    
    
    public static String datosJsonCronicaBasket(ArrayList<CronicaBasket> cronica) {
        
        StringBuilder txt = new StringBuilder();
        txt.append('[');
                       
        if (cronica != null){      
            
            boolean primero = true;
            for (CronicaBasket jug : cronica) {
                if (primero) primero = false;
                else txt.append(',');
                txt.append('{');
                txt.append(StringUtil.formatJson("id", jug.getId())).append(',');
                txt.append(StringUtil.formatJson("minuto", jug.getMinuto())).append(',');
                txt.append(StringUtil.formatJson("accion", jug.getAccion())).append(',');
                txt.append(StringUtil.formatJson("cuadrante", jug.getCuadrante()));
                txt.append('}');
            }            
        }
        
        txt.append(']');

        return txt.toString();
    }
    
}
