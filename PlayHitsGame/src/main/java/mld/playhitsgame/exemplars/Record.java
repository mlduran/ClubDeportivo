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
public class Record {
    
    private Tema tema;
    private String descripcion;
    private int canciones;   
    private Usuario usuarioRecord;
    private int puntos;    
    
    
}
