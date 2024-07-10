/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 *
 * @author miguel
 */

@Configuration
@EnableWebSocket
public class webSocketConfig implements WebSocketConfigurer{
    
    @Autowired
    private PlayHitsGameHandler playHitsGameHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry){
        
        registry.addHandler(playHitsGameHandler, "/websocket");
        
    }

    
}
