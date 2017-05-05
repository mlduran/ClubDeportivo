/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mld.clubdeportivo.base.futbol8;

import java.util.ArrayList;
import mld.clubdeportivo.base.Cronica;
import mld.clubdeportivo.utilidades.StringUtil;
import mld.clubdeportivo.utilidades.UtilGenericas;

/**
 *
 * @author Miguel
 */
public final class CronicaFutbol8 extends Cronica{
    
    private long idPartido;
    private PartidoFutbol8 partido;
    private int minuto;
    private String accion;
    private String cuadrante;
    
    public CronicaFutbol8(){};
    
    private CronicaFutbol8(PartidoFutbol8 partido, String accion, String cuadrante,
            int minuto){
        
        this.setPartido(partido);
        this.setIdPartido(partido.getId());
        this.setAccion(accion);
        this.setCuadrante(cuadrante);
        this.setMinuto(minuto);
    }
    
    private CronicaFutbol8(PartidoFutbol8 partido, String accion, String cuadrante){
        this(partido, accion, cuadrante, 0);
    }
    
    public static CronicaFutbol8 escribir(PartidoFutbol8 partido, String accion){
        
        return  new CronicaFutbol8(partido, accion, "");        
    }   
    
    public static CronicaFutbol8 escribir(PartidoFutbol8 partido, String accion, String cuadrante){
        
        return  new CronicaFutbol8(partido, accion, cuadrante);
        
    }   
    
     public static CronicaFutbol8 escribir(PartidoFutbol8 partido, String accion, 
             String cuadrante, int minuto){
        
        return  new CronicaFutbol8(partido, accion, cuadrante, minuto);
        
    }   
   

    public long getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(long idPartido) {
        this.idPartido = idPartido;
    }

    public PartidoFutbol8 getPartido() {
        return partido;
    }

    public void setPartido(PartidoFutbol8 partido) {
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
    
    
    public static String datosJsonCronicaFutbol8(ArrayList<CronicaFutbol8> cronica) {
        
        StringBuilder txt = new StringBuilder();
        txt.append('[');
                       
        if (cronica != null){      
            
            boolean primero = true;
            for (CronicaFutbol8 jug : cronica) {
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
