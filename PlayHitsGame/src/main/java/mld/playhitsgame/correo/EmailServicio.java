/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.correo;

import jakarta.mail.MessagingException;

/**
 *
 * @author miguel
 */
public interface EmailServicio {
    
     public void enviarCorreo(Mail mail) throws MessagingException;
    
}
