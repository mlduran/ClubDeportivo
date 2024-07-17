/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.websocket;

import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

/**
 *
 * @author miguel
 */
@Data
public class UsuarioWS {
    
    private long id;
    private WebSocketSession session;
    private String usuario;
    private boolean respAnyo;
    private boolean respTitulo;
    private boolean respInterprete;
    private int orden;
    
}
