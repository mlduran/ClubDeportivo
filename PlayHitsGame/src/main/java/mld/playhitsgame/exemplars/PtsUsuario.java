/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.exemplars;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author miguel
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PtsUsuario implements Comparable<PtsUsuario>{
    
    Usuario usuario;
    Integer pts;
    String puntos;
    String link;
    
    
    public String getPuntosTxt(){
        
        if (this.puntos == null)
            if (this.pts != null)
                return String.valueOf(this.pts);
            else
                return "";
        else
            return this.puntos;
        
    }
    
    @Override
    public int compareTo(PtsUsuario o) {
        return o.pts.compareTo(this.pts);
    }    
    
    
}
