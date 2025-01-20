/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.correo;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 *
 * @author miguel
 */


@Configuration
public class EmailConfig {    
    
    @Value("${mail.smtp.auth}")
    private String auth;    
    @Value("${mail.smtp.starttls.enable}")
    private String tlsEnable;    
    @Value("${mail.smtp.host}")
    private String host;
    @Value("${mail.smtp.port}")
    private String port;
    @Value("${mail.username}")
    private String mail;
    @Value("${mail.password}")
    private String password;
    
    
    
    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.starttls.enable", tlsEnable);
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        return properties;        
    }
    
    @Bean
    public JavaMailSender javaMailSender(){
        
        JavaMailSenderImpl mailSender = 
                new JavaMailSenderImpl();
        
        mailSender.setJavaMailProperties(getMailProperties());
        mailSender.setUsername(mail);
        mailSender.setPassword(password);
        return mailSender;
    }
    
    @Bean
    public ResourceLoader resourceLoader(){
        return new DefaultResourceLoader();
    }
    
    
}
