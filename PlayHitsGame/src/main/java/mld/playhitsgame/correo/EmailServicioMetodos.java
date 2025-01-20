/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.correo;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author miguel
 */
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

@Service
public class EmailServicioMetodos implements EmailServicio {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${custom.server.ip}")
    private String customIp;

    @Value("${mail.username}")
    private String mailApp;
    
    private final int TIEMPO_ESPERA_ENVIO = 9000;  //Para enviar 400 mails por hora

    private final BlockingQueue<Mail> mailQueue = new PriorityBlockingQueue<>();
    private volatile boolean running = true;
    private Thread emailProcessorThread;

    public EmailServicioMetodos(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @PostConstruct
    public void startProcessing() {
        emailProcessorThread = new Thread(() -> {
            while (running) {
                try {
                    // Toma un correo de la cola (bloquea si está vacía)
                    Mail mail = mailQueue.take();
                    enviaCorreo(mail); // Envía el correo
                    Thread.sleep(TIEMPO_ESPERA_ENVIO); 
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    // Maneja cualquier excepción del envío
                    System.err.println("Error enviando correo: " + e.getMessage());
                }
            }
        });
        emailProcessorThread.start();
    }

    @PreDestroy
    public void stopProcessing() {
        running = false;
        emailProcessorThread.interrupt(); // Detiene el hilo
    }

    /**
     * Agrega un correo a la cola para ser procesado.
     *
     * @param mail
     */
    @Override
    public void encolarMail(Mail mail) {
        try {
            mailQueue.put(mail);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error al agregar correo a la cola", e);
        }
    }

    @Override
    public void encolarMails(List<Mail> mails) {
        for (Mail mail : mails) {
            encolarMail(mail);
        }
    }

    private void enviaCorreo(Mail mail) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(mailApp);
        helper.setTo(mail.getDestinatario());
        helper.setSubject(mail.getAsunto());

        String plantilla = mail.getPlantilla() != null ? mail.getPlantilla() : "Correo";
        Context context = new Context();
        context.setVariable("imagen", customIp + "/images/playhitsgamePresentacion.png");
        context.setVariable("mail", mailApp);

        if (mail.getMensaje() != null) {
            List<String> txts = Arrays.asList(mail.getMensaje().split("\\R"));
            context.setVariable("mensajes", txts);
        } else if (mail.getMensajes() != null) {
            context.setVariable("mensajes", mail.getMensajes());
        }

        if (mail.getUrl() != null) {
            context.setVariable("url", mail.getUrl());
            context.setVariable("textoUrl", mail.getTextoUrl());
        }

        context.setVariable("nombre", mail.getNombre());

        try {
            String contenttHTML = templateEngine.process(plantilla, context);
            helper.setText(contenttHTML, true);
            javaMailSender.send(message);
            System.out.println("Se envió correo " + mail.getAsunto() + " a " + mail.getDestinatario());
        } catch (Exception e) {
            System.err.println("Error procesando la plantilla: " + plantilla);
            throw e;
        }
    }
}
