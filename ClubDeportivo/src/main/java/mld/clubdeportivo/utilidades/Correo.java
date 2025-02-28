
package mld.clubdeportivo.utilidades;
import jakarta.mail.Authenticator;
import static jakarta.mail.Message.RecipientType.BCC;
import static jakarta.mail.Message.RecipientType.CC;
import static jakarta.mail.Message.RecipientType.TO;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import static jakarta.mail.Session.getInstance;
import static jakarta.mail.Transport.send;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.*;
import static java.util.logging.Level.INFO;
import static java.util.logging.Logger.getLogger;


/**
 *
 * Ejemplo de utilizacion:
 * 
 * String txt = "Texto del mail";   
 * Correo.getCorreo().enviarMail("Asunto", txt, true, club.getMail());
 */
public final class Correo {
    
    private static Logger logApp
            = getLogger(Correo.class.getName());

    static private Correo INSTANCE = new Correo();
    private String rutaFichConf; 
    private Properties config; 
    
    static public Correo getCorreo() {

        return INSTANCE;
    }
    
    public void initCorreo(String configCorreo)
            throws IOException {

       
        // Obtenermos parametros
        config = new Properties();

        logApp.info("Leyendo fichero configuracion de: ".concat(configCorreo));
        config.load(new FileReader(configCorreo));
        rutaFichConf = configCorreo;       

        logApp.info("Parametros correo inicializados");

    }
    
    private String firma(Properties config, boolean esHTML){
        
        var texto = config.getProperty("mail.firma");
        
        if (esHTML){
            texto = "<br/><br/>" + "<a href=\"" + texto + "\">" + texto + "</a>";
        }
        else{
            texto = "\n\n" + texto;
        }
        
        return texto;
                
        
    }
    
    private Session connectServer() {
        
        return getInstance(config, new Authenticator() {
	
		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(
                                config.getProperty("mail.usuario"), 
                                config.getProperty("mail.password"));
		}
	});
        
    }
    
    

    public void enviarMail(String asunto, String contenido,
            boolean esHTML, String[] destinatarios, String[] cc, 
            String [] cco){        
        
        if (asunto == null || contenido == null || 
                (destinatarios == null || destinatarios.length == 0) &&
                (cco == null || cco.length == 0)){
            logApp.log(INFO, "No se envia mail, valores incorrectos");
            return;
        }           

        var sesion = connectServer();
        MimeMessage mail;

        try {  
            
            // Esto es para cargar el archivo mail.properties            
            var entorno = config.getProperty("mail.entorno");
            if (entorno != null && entorno.equals("desarrollo")){
                logApp.log(INFO, "Mail desactivado para:");
                logApp.log(INFO, "TO:" + destinatarios);
                logApp.log(INFO, "CC:" + cc);
                logApp.log(INFO, "CCO:" + cco);
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
            
            var firma = firma(config, esHTML);

            mail = new MimeMessage(sesion);            

            mail.setSubject(asunto, "UTF-8");
            if (esHTML){
                mail.setText(contenido + firma, "UTF-8", "html");
            }
            else{
                mail.setText(contenido + firma, "UTF-8", "plain");
            }
            mail.setSentDate(new Date());
            mail.setFrom(new InternetAddress(config.getProperty("mail.deusuario")));
            
            if (destinatarios != null){
                for (var un_dest : destinatarios) {
                    try{
                        if (un_dest != null){
                            mail.addRecipient(TO,
                                    new InternetAddress(un_dest, true));// el true comprueba formato mail
                        }
                    }
                    catch (AddressException ex){
                        // Hacemos un log y continuamos
                    }
                }
            }
            if (cc != null){
                for (var un_cc : cc) {
                    try{
                        if (un_cc != null){
                            mail.addRecipient(CC,
                            new InternetAddress(un_cc, true));// el true comprueba formato mail
                        }
                    }
                    catch (AddressException ex){
                        // Hacemos un log y continuamos
                    }
                }
            }
            if (cco != null){
                for (var un_cco : cco) {
                    try{
                     if (un_cco != null){
                            mail.addRecipient(BCC,
                            new InternetAddress(un_cco, true));// el true comprueba formato mail
                        }
                    }
                    catch (AddressException ex){
                        // Hacemos un log y continuamos
                    }
                }
            }
 
            send(mail, mail.getAllRecipients());
            
        }catch (MessagingException messagingException) {
            // Fallo envio mail
            // por ejemplo hacer un log
            logApp.info("Error envio Correo: ".concat(messagingException.getMessage()));        
        }
        // Fallo al recuperar configuracion
        // por ejemplo hacer un log
         catch (Exception ex) {
            // Fallo envio mail
            // por ejemplo hacer un log
            if (ex.getMessage() != null)
                logApp.info("Error envio Correo: ".concat(ex.getMessage()));
        }   

    }
    
    public void enviarMail(String asunto, String contenido,
            boolean esHTML, String[] destinatarios, String[] cc) {
        
        enviarMail(asunto, contenido, esHTML, destinatarios, cc, null);
    }
    
    public void enviarMailMasivo(String asunto, String contenido,
            boolean esHTML, List<String> cco) {
        
        var lista = new String[cco.size()];
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
        
         for (var destinatario : destinatarios) {
             enviarMail(asunto, contenido, esHTML, destinatario);
         }
                
    }


}

