/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.websocket;

import lombok.Data;

/**
 *
 * @author miguel
 */
@Data
public class UsuarioWS implements Comparable<UsuarioWS>{
    
    private long id;
    private String usuario;
    private boolean respAnyo;
    private boolean respTitulo;
    private boolean respInterprete;
    private int orden;
    
    
    public boolean isTodoRespondido(){
        return this.isRespAnyo() && 
                this.isRespInterprete() &&
                this.isRespTitulo();
    }   

    @Override 
    public int compareTo(UsuarioWS otroUsuarioWS) {
        
        if (this.orden > otroUsuarioWS.getOrden()) {
           return 1; 
        } 
        if (this.orden < otroUsuarioWS.getOrden()) {
           return -1; 
        } 
        return 0; 
    }
}

