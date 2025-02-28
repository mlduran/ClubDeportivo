
package mld.clubdeportivo.utilidades;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.System.out;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.FRIDAY;
import static java.util.Calendar.MONDAY;
import static java.util.Calendar.SUNDAY;
import static java.util.Calendar.THURSDAY;
import static java.util.Calendar.TUESDAY;
import static java.util.Calendar.getInstance;
import java.util.Collections;
import static java.util.Collections.shuffle;
import java.util.Date;
import java.util.regex.Pattern;
import static java.util.regex.Pattern.matches;

/**
 *
 * @author Miguel
 */
public class UtilGenericas {

    private UtilGenericas(){};


    public static boolean isMail(String email){

       if (email == null)
            throw new NullPointerException("Sin valor de referencia");
        return matches("^(([A-Za-z0-9]+_+)|([A-Za-z0-9]+\\-+)|([A-Za-z0-9]+\\.+)|([A-Za-z0-9]+\\++))*[A-Za-z0-9]+@((\\w+\\-+)|(\\w+\\.))*\\w{1,63}\\.[a-zA-Z]{2,6}$", email);
    }

    public static boolean isUsername(String username){

        // solo letras y numeros y de 4 a 15 caracteres

        if (username == null)
            throw new NullPointerException("Sin valor de referencia");
         return username.matches( "^[A-Za-z0-9ñÑçÇ_]{4,15}$");
    }

    public static boolean isPassword(String password){

         // solo letras y numeros y algunos caracteres especiales
         // de 6 a 15 caracteres

        if (password == null)
            throw new NullPointerException("Sin valor de referencia");

         return password.matches( "^[A-Za-z0-9$_@ñÑçÇ-]{6,15}$");
    }

    public static boolean isGrupo(String grp){

         // solo letras y numeros y algunos caracteres especiales
         // de 6 a 20 caracteres

        if (grp == null)
            throw new NullPointerException("Sin valor de referencia");

         return grp.matches( "^[A-Za-z0-9$_@ñÑçÇ-]{6,20}$");
    }

     public static boolean isClub(String club){

         // solo letras y numeros y algunos caracteres especiales
         // de 4 a 15 caracteres

        if (club == null)
            throw new NullPointerException("Sin valor de referencia");

         return club.length() > 3 && club.length() < 21;
    }

    static public ArrayList desordenarLista(ArrayList lista)
	{
		shuffle(lista);
		return lista;
	}

    public static void formatearApellidoNombre(String fich_origen, String fich_destino) throws IOException

    {

        FileWriter fw;
        try (var fr = new FileReader(fich_origen)) {
            fw = new FileWriter(fich_destino);
            var bf = new BufferedReader(fr);
            String linea;
            while ((linea = bf.readLine())!=null) {
                var campos = linea.split("\t");
                var nombre = campos[0].split(",");
                if (nombre.length == 2) {
                    fw.write(nombre[1] + " " + nombre[0] + "\t" + campos[1] + "\r\n");
                } else {
                    out.println("Error en linea: " + linea);
                }
            }
        }
          fw.close();
    }
    
    public static String formatearNumChars(String nombre, int num){
        /* 
         * Recorta el nombre al numero de caracteres indicado
         */
        
        var nomFinal = "";
        
        if (nombre.length() > num){
            
            for (var  parte : nombre.split(" ")) 
                if (parte.length() <= num && parte.length() > nomFinal.length())
                    nomFinal = parte;
            if (nomFinal.isEmpty()) nomFinal = nombre.substring(0, num - 1);
                    
        }
        else
            nomFinal = nombre;
        
        return nomFinal;
        
    }

    public static String fechaActual()
    {
            var fecha_actual = new Date();
            var sdf=new SimpleDateFormat("dd/MM/yyyy");
            var fecha =  sdf.format(fecha_actual);
            return fecha;
    }


    public static String pilaError(Throwable ex){
        
        var pilaTxt = new StringBuilder();
        var pila = ex.getStackTrace();
        for (var elem : pila) {
            pilaTxt.append(elem.toString()).append("\n");
        }
        
        return pilaTxt.toString();
        
    } 

    public static boolean isDomingo() {
        // tipo es Liga, Copa etc

        boolean result;

        var calendario = getInstance();
        calendario.setTime(new Date());

        result = calendario.get(DAY_OF_WEEK) == SUNDAY;

        return result;

    }
    
    public static boolean isLunes() {
        // tipo es Liga, Copa etc

        boolean result;

        var calendario = getInstance();
        calendario.setTime(new Date());

        result = calendario.get(DAY_OF_WEEK) == MONDAY;

        return result;

    }
    
      public static boolean isMartes() {
        // tipo es Liga, Copa etc

        boolean result;

        var calendario = getInstance();
        calendario.setTime(new Date());

        result = calendario.get(DAY_OF_WEEK) == TUESDAY;

        return result;

    }
    
    public static boolean isViernes() {
        // tipo es Liga, Copa etc

        boolean result;

        var calendario = getInstance();
        calendario.setTime(new Date());

        result = calendario.get(DAY_OF_WEEK) == FRIDAY;

        return result;

    }
    
     public static boolean isJueves() {
        // tipo es Liga, Copa etc

        boolean result;

        var calendario = getInstance();
        calendario.setTime(new Date());

        result = calendario.get(DAY_OF_WEEK) == THURSDAY;

        return result;

    }
     
    
}
