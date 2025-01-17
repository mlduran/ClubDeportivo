/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.correo;

import java.util.List;
import lombok.Data;

/**
 *
 * @author miguel
 */

@Data
public class Mail {
    
    private String destinatario;
    private String asunto;
    private String mensaje;   
    private List<String> mensajes;
    private String url;
    private String textoUrl;
    private String plantilla;
    private String nombre;
    
    
}
