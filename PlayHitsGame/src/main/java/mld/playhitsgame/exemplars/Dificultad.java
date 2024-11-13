/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package mld.playhitsgame.exemplars;

/**
 *
 * @author miguel
 */
public enum Dificultad {
 
    Facil("dificultad.facil"),
    Normal("dificultad.normal"),
    Dificil("dificultad.dificil"),
    Entreno("dificultad.entreno");

    private final String messageKey;

    Dificultad(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        
        return messageKey;
    }
}
