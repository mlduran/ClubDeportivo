/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.correo;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 *
 * @author miguel
 */
@Service
public class EmailServicioMetodos implements EmailServicio {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${custom.server.ip}")
    private String customIp;

    @Value("${mail.username}")
    private String mailApp;

    public EmailServicioMetodos(JavaMailSender javaMailSender,
            TemplateEngine templateEngine) {

        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;

    }

    @Override
    public void enviarCorreo(Mail mail) throws MessagingException, MailSendException {

        MimeMessage message
                = javaMailSender.createMimeMessage();
        MimeMessageHelper helper
                = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(mailApp);
        helper.setTo(mail.getDestinatario());
        helper.setSubject(mail.getAsunto());
        String plantilla = "";
        if (mail.getPlantilla() != null) {
            plantilla = plantilla + mail.getPlantilla();
        } else {
            plantilla = plantilla + "Correo";
        }

        // DEBUG: Imprime el nombre final de la plantilla
        System.out.println("Plantilla generada: " + plantilla);

        Context context = new Context();
        context.setVariable("imagen", customIp + "/images/playhitsgamePresentacion.png");
        context.setVariable("mail", mailApp);
        if (mail.getMensaje() != null) {
            context.setVariable("mensaje", mail.getMensaje());
        } else if (mail.getMensajes() != null) {
            context.setVariable("mensajes", mail.getMensajes());
        }
        if (mail.getUrl() != null) {
            context.setVariable("url", mail.getUrl());
            context.setVariable("textoUrl", mail.getTextoUrl());
        }

        context.setVariable("nombre", mail.getNombre());

        // Procesar plantilla
        try {
            String contenttHTML = templateEngine.process(plantilla, context);
            helper.setText(contenttHTML, true);
            javaMailSender.send(message);
            System.out.println("Se envió correo a " + mail.getDestinatario());
        } catch (Exception e) {
            System.out.println("Error procesando la plantilla: " + plantilla);            
            throw e; // Vuelve a lanzar la excepción para gestionarla adecuadamente
        }

    }

}
