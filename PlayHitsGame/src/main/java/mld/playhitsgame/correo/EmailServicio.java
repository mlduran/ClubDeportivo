/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.correo;

import java.util.List;

/**
 *
 * @author miguel
 */
public interface EmailServicio {
    
     public void encolarMail(Mail mail);
     public void encolarMails(List<Mail> mails);
    
}
