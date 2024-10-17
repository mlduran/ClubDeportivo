/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.correo;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
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
    public void enviarCorreo(Mail mail) throws MessagingException {

        MimeMessage message
                = javaMailSender.createMimeMessage();
        MimeMessageHelper helper
                = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(mail.getDestinatario());
        helper.setSubject(mail.getAsunto());
        String plantilla = "correo/";
        if (mail.getPlantilla() != null) {
            plantilla = plantilla + mail.getPlantilla();
        } else {
            plantilla = plantilla + "Correo";
        }

        Context context = new Context();
        context.setVariable("imagen", customIp + "/images/playhitsgamePresentacion.png");       
        context.setVariable("mail", mailApp);
        context.setVariable("mensaje", mail.getMensaje());
        String contenttHTML
                = templateEngine.process(plantilla, context);
        helper.setText(contenttHTML, true);
        javaMailSender.send(message);

    }

}
