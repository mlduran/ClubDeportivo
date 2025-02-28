
package mld.clubdeportivo.utilidades;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;

/**
 *
 * @author java1
 */
public final class StringUtil {

    private StringUtil(){}

    public static boolean isNullOrEmpty(String message){

        return (message == null || message.trim().length() == 0);
          
    }
    public static String truncate(String message, int maxLength,
                                    String truncateChars){

        String txt;

        if (message == null)
            throw new NullPointerException("Sin valor de referencia");
        if (maxLength <= 0 || maxLength <= truncateChars.length())
            throw new IllegalArgumentException("Maxima longitud incorrecta");

        if (message.length() > maxLength)
            txt = format("%s%s",
                    message.substring(0, maxLength - truncateChars.length()),
                    truncateChars);
        else txt = message;

        return txt;
    }
    public static String truncate(String message, int maxLength){

        return truncate( message, maxLength, "");
    }
    public static String removeWhiteSpaces(String message){

        if (message == null)
           throw new NullPointerException("Sin valor de referencia");

        return message.replaceAll("\\s+", "");
    }
    public static String trimWhiteSpaces(String message){

        if (message == null)
           throw new NullPointerException("Sin valor de referencia");

        return message.replaceAll("\\s+", " ").trim();

    }
    public static String removeCharsEspeciales(String texto){

       
        if (texto == null)
           throw new NullPointerException("Sin valor de referencia");
        
        var charsNoValidos = "<>'%;)(&+-\"";
        
        for (var i = 0; i < charsNoValidos.length(); i++) {
            var c = charsNoValidos.charAt(i);
            texto = texto.replace(c, ' ');
        }

        return texto;
    }
   
    public static String removeNoDigits(String message){

        if (message == null)
           throw new NullPointerException("Sin valor de referencia");

        return message.replaceAll("[^0-9]", "");
 
    }
    
    public static String tratarSaltosLineaHTML(String message){
        // Reemplaza los saltos de linea por <br/>
        if (message == null)
           throw new NullPointerException("Sin valor de referencia");

        return message.replaceAll("\n", "<br/>");
 
    }

    public static String toHexString(byte[] bytes) {
        if (bytes == null)
            throw new NullPointerException("Referencia nula no permitida");

        var sb = new StringBuilder();
        //Hacemos un recorrido secuencial por los bytes
        for (var b : bytes) {
            sb.append(format("%02x", b));
        }

        return sb.toString();

    }

    public static boolean isNumero(String cadena){
        try {
            parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
    
    public static String formatJson(String campo, int valor){
        return "\"" + campo + "\":" + valor;        
    }
    
    public static String formatJson(String campo, long valor){
        return "\"" + campo + "\":" + valor;        
    }

    public static String formatJson(String campo, String valor){
        return "\"" + campo + "\":\"" + valor + "\"";        
    }
    
    

}
