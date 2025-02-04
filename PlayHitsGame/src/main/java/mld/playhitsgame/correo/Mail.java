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
public class Mail implements Comparable<Mail>{
    
    private String destinatario;
    private String asunto;
    private String mensaje;   
    private List<String> mensajes;
    private String url;
    private String textoUrl;
    private String plantilla;
    private String nombre;
    private String urlCancelacionCorreo;
    private String textoUrlCancelacionCorreo;
    private boolean prioritario;
    
    
    @Override
    public int compareTo(Mail o) {
        // Prioritarios (true) deben ir antes que no prioritarios (false)
        return Boolean.compare(o.prioritario, this.prioritario);
    }
    
    
}
