
package mld.clubdeportivo.utilidades;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.regex.Pattern;

/**
 *
 * @author Miguel
 */
public class UtilGenericas {

    private UtilGenericas(){};


    public static boolean isMail(String email){

       if (email == null)
            throw new NullPointerException("Sin valor de referencia");
        return Pattern.matches("^(([A-Za-z0-9]+_+)|([A-Za-z0-9]+\\-+)|([A-Za-z0-9]+\\.+)|([A-Za-z0-9]+\\++))*[A-Za-z0-9]+@((\\w+\\-+)|(\\w+\\.))*\\w{1,63}\\.[a-zA-Z]{2,6}$", email);
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
		Collections.shuffle(lista);
		return lista;
	}

    public static void formatearApellidoNombre(String fich_origen, String fich_destino) throws IOException

    {

          FileReader fr = new FileReader(fich_origen);
          FileWriter fw = new FileWriter(fich_destino);
          BufferedReader bf = new BufferedReader(fr);

          String linea;

          while ((linea = bf.readLine())!=null) {

                String[] campos = linea.split("\t");

                String[] nombre = campos[0].split(",");

                if (nombre.length == 2){

                      fw.write(nombre[1] + " " + nombre[0] + "\t" + campos[1] + "\r\n");}

                else {System.out.println("Error en linea: " + linea);}

          }

          fr.close();
          fw.close();
    }
    
    public static String formatearNumChars(String nombre, int num){
        /* 
         * Recorta el nombre al numero de caracteres indicado
         */
        
        String nomFinal = "";
        
        if (nombre.length() > num){
            
            for (String  parte : nombre.split(" ")) 
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
            java.util.Date fecha_actual = new Date();
            SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
            String fecha =  sdf.format(fecha_actual);
            return fecha;
    }


    public static String pilaError(Throwable ex){
        
        StringBuilder pilaTxt = new StringBuilder();
        StackTraceElement[] pila = ex.getStackTrace();
        for (StackTraceElement elem : pila) {
            pilaTxt.append(elem.toString()).append("\n");
        }
        
        return pilaTxt.toString();
        
    } 

    public static boolean isDomingo() {
        // tipo es Liga, Copa etc

        boolean result;

        Calendar calendario = Calendar.getInstance();
        calendario.setTime(new Date());

        result = calendario.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;

        return result;

    }
    
    public static boolean isLunes() {
        // tipo es Liga, Copa etc

        boolean result;

        Calendar calendario = Calendar.getInstance();
        calendario.setTime(new Date());

        result = calendario.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY;

        return result;

    }
    
      public static boolean isMartes() {
        // tipo es Liga, Copa etc

        boolean result;

        Calendar calendario = Calendar.getInstance();
        calendario.setTime(new Date());

        result = calendario.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY;

        return result;

    }
    
    public static boolean isViernes() {
        // tipo es Liga, Copa etc

        boolean result;

        Calendar calendario = Calendar.getInstance();
        calendario.setTime(new Date());

        result = calendario.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY;

        return result;

    }
    
     public static boolean isJueves() {
        // tipo es Liga, Copa etc

        boolean result;

        Calendar calendario = Calendar.getInstance();
        calendario.setTime(new Date());

        result = calendario.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY;

        return result;

    }
     
    
}
