
package mld.clubdeportivo.utilidades;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Ejemplo de utilizacion:
 * 
 * String txt = "Texto del mail";   
 * Correo.getCorreo().enviarMail("Asunto", txt, true, club.getMail());
 */
public final class Correo {
    
    private static Logger logApp
            = LogManager.getLogger(Correo.class);

    static private Correo INSTANCE = new Correo();
    private String rutaFichConf; 
    
    static public Correo getCorreo() {

        return INSTANCE;
    }
    
    public void initCorreo(String configCorreo)
            throws IOException {

       
        // Obtenermos parametros
        Properties config = new Properties();

        logApp.info("Leyendo fichero configuracion de: ".concat(configCorreo));
        config.load(new FileReader(configCorreo));
        rutaFichConf = configCorreo;       

        logApp.info("Parametros correo inicializados");

    }
    
    private String firma(Properties config, boolean esHTML){
        
        String texto = config.getProperty("mail.firma");
        
        if (esHTML){
            texto = "<br/><br/>" + "<a href=\"" + texto + "\">" + texto + "</a>";
        }
        else{
            texto = "\n\n" + texto;
        }
        
        return texto;
                
        
    }

    public void enviarMail(String asunto, String contenido,
            boolean esHTML, String[] destinatarios, String[] cc, 
            String [] cco){        
        
        if (asunto == null || contenido == null || 
                (destinatarios == null || destinatarios.length == 0) &&
                (cco == null || cco.length == 0)){
            logApp.debug("No se envia mail, valores incorrectos");
            return;
        }           

        Session sesion;
        Transport canal = null;
        MimeMessage mail;


        try {  
            
            // Esto es para cargar el archivo mail.properties
            Properties config = new Properties();            

            config.load(new FileReader(rutaFichConf));
            
            String entorno = config.getProperty("mail.entorno");
            if (entorno != null && entorno.equals("desarrollo")){
                logApp.debug("Mail desactivado para:");
                logApp.debug("TO:" + destinatarios);
                logApp.debug("CC:" + cc);
                logApp.debug("CCO:" + cco);
                return;
            }
            else if (entorno != null && entorno.equals("debug")){                
                if (destinatarios != null){
                    destinatarios = new String[1];
                    destinatarios[0] = "ml_duran@live.com";
                }
                if (cc != null){
                    cc = new String[1];
                    cc[0] = "ml_duran@live.com";
                }
                if (cco != null){
                    cco = new String[1];
                    cco[0] = "ml_duran@live.com";
                }
            }
            
            String firma = firma(config, esHTML);

            sesion = Session.getDefaultInstance(config);
            canal = sesion.getTransport();
            mail = new MimeMessage(sesion);

            mail.setSubject(asunto);
            if (esHTML){
                mail.setText(contenido + firma, "UTF-8", "html");
            }
            else{
                mail.setText(contenido + firma, "UTF-8", "plain");
            }
            mail.setSentDate(new Date());
            mail.setFrom(new InternetAddress(config.getProperty("mail.deusuario")));
            
            if (destinatarios != null){
                for (String un_dest : destinatarios) {
                    try{
                        if (un_dest != null){
                            mail.addRecipient(Message.RecipientType.TO,
                                    new InternetAddress(un_dest, true));// el true comprueba formato mail
                        }
                    }
                    catch (AddressException ex){
                        // Hacemos un log y continuamos
                    }
                }
            }
            if (cc != null){
                for (String un_cc : cc) {
                    try{
                        if (un_cc != null){
                            mail.addRecipient(Message.RecipientType.CC,
                            new InternetAddress(un_cc, true));// el true comprueba formato mail
                        }
                    }
                    catch (AddressException ex){
                        // Hacemos un log y continuamos
                    }
                }
            }
            if (cco != null){
                for (String un_cco : cco) {
                    try{
                     if (un_cco != null){
                            mail.addRecipient(Message.RecipientType.BCC,
                            new InternetAddress(un_cco, true));// el true comprueba formato mail
                        }
                    }
                    catch (AddressException ex){
                        // Hacemos un log y continuamos
                    }
                }
            }
           
            canal.connect(config.getProperty("mail.usuario"),
                    config.getProperty("mail.password"));

            canal.sendMessage(mail, mail.getAllRecipients());
        } catch (IOException iOException) {
            // Fallo al recuperar configuracion
             // por ejemplo hacer un log
            logApp.info("Error envio Correo: ".concat(iOException.getMessage()));

        } catch (MessagingException messagingException) {
            // Fallo envio mail
            // por ejemplo hacer un log
            logApp.info("Error envio Correo: ".concat(messagingException.getMessage()));        
        } catch (Exception ex) {
            // Fallo envio mail
            // por ejemplo hacer un log
            if (ex.getMessage() != null)
                logApp.info("Error envio Correo: ".concat(ex.getMessage()));
        }
        finally {
            if (canal != null){
                try {
                    canal.close();
                } catch (MessagingException ex) {
                    logApp.info("Error envio Correo: ".concat(ex.getMessage()));
                }
            }
        }

    }
    
    public void enviarMail(String asunto, String contenido,
            boolean esHTML, String[] destinatarios, String[] cc) {
        
        enviarMail(asunto, contenido, esHTML, destinatarios, cc, null);
    }
    
    public void enviarMailMasivo(String asunto, String contenido,
            boolean esHTML, List<String> cco) {
        
        String[] lista = new String[cco.size()];
        lista = cco.toArray(lista);
        
        enviarMail(asunto, contenido, esHTML, null, null, lista);
    }
    
    public void enviarMail(String asunto, String contenido,
            boolean esHTML, String[] destinatarios) {
        
        enviarMail(asunto, contenido, esHTML, destinatarios, null, null);

    }
         
    public void enviarMail(String asunto, String contenido,
            boolean esHTML, String destinatario) {
        
        String[] destinatarios = {destinatario};
        enviarMail(asunto, contenido, esHTML, destinatarios, null, null);
        
    }
    
     public void enviarMails(String asunto, String contenido,
            boolean esHTML, List<String> destinatarios) {
        
         for (String destinatario : destinatarios) {
             enviarMail(asunto, contenido, esHTML, destinatario);
         }
                
    }


}
