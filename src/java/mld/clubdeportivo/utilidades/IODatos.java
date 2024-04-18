package mld.clubdeportivo.utilidades;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import mld.clubdeportivo.base.EquipoMaestro;
import mld.clubdeportivo.base.futbol8.EntrenadorMaestroFutbol8;
import mld.clubdeportivo.base.futbol8.JugadorMaestroFutbol8;
import mld.clubdeportivo.bd.DAOException;
import mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8;
import java.util.logging.*;

/**
 *
 * @author Miguel
 */
public class IODatos {

     private static Logger logApp
            = Logger.getLogger(IODatos.class.getName());
     
     
     private static void copiarFichero(File f1, File f2){
 
         try {
             InputStream in = new FileInputStream(f1);
             OutputStream out = new FileOutputStream(f2);
             
             byte[] buf = new byte[1024];
             int len;
             
             while ((len = in.read(buf)) > 0) {
                 out.write(buf, 0, len);
             }
             
             in.close();
             out.close();
             
         } catch (IOException ioe){
         }
     }

     public static String leerFicheroUTF8(String nomFich) throws IOException {
         
         StringBuilder buffer = new StringBuilder();
         
         FileInputStream fis = new FileInputStream(nomFich);
         InputStreamReader isr = new InputStreamReader(fis, "UTF8");
         Reader in = new BufferedReader(isr);
         int ch;
         boolean primero = true;
         while ((ch = in.read()) > -1) {
             // El primer caracter no lo cogemos, parece que son codigos
             //if (!primero)
                 buffer.append((char)ch);
             //else 
             //    primero = false;
         }
         in.close(); 
         
         return buffer.toString();

    }
     
    public static String[] lineasFicheroUTF8(String nomFich) throws IOException {
        
        String txt = leerFicheroUTF8(nomFich);
        
        return txt.split("\n");
        
    }
    
    private IODatos(){}

    public static void formatearApellidoNombre(String fich_origen,
            String fich_destino) throws IOException

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
    
    
    public static BufferedReader br(String nomFich) 
            throws FileNotFoundException, UnsupportedEncodingException{
        
        BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(nomFich), "UTF8"));
        
        return br;
    }

    public static void cargarMaestroJugadores(String fich_origen
            ) throws IOException, DAOException

    {

          String[] lineas = lineasFicheroUTF8(fich_origen);


          try {

              for (String linea : lineas) {

                  String[] campos = linea.split("\t");

                  JugadorMaestroFutbol8 jug = new JugadorMaestroFutbol8(campos[0],
                          campos[1]);
                  JDBCDAOFutbol8.grabarJugadorMaestroFutbol8(jug);
              }
          } catch (DAOException ex) {
              throw ex;
          }
    }

    public static void cargarMaestroEntrenadores(String fich_origen
            ) throws IOException, DAOException

    {

         String[] lineas = lineasFicheroUTF8(fich_origen);

          try {

              for (String linea : lineas) {

                  String[] campos = linea.split("\t");

                  EntrenadorMaestroFutbol8 entr =
                          new EntrenadorMaestroFutbol8(campos[0]);
                  JDBCDAOFutbol8.grabarEntrenadorMaestroFutbol8(entr);
              }
          } catch (DAOException ex) {
              throw ex;
          }
        
    }

    public static void cargarMaestroEquipos(String fich_origen
            ) throws IOException, DAOException

    {
        
       String[] lineas = lineasFicheroUTF8(fich_origen);
        
        try {
            
            for (String linea : lineas) {
                
                String[] campos = linea.split("\t");
                
                if (campos[0].equals("")) continue;
                EquipoMaestro eq = new EquipoMaestro();
                eq.setNombre(UtilGenericas.formatearNumChars(campos[0], 20));
                try{        
                    JDBCDAOFutbol8.grabarEquipoMaestroFutbol8(eq);
                }
                catch (Exception ex){
                    logApp.log(Level.SEVERE, "Error alta nombre equipo maestro : ".concat(ex.getMessage()));
                }
            }
        }
        catch (Exception ex){
            logApp.log(Level.SEVERE, "Error alta nombre equipo maestro : ".concat(ex.getMessage()));
        }
        
    }
    
    public static void eliminarFicheroJornadaValidada(String ruta, String comp, int jornada){
        // hacemos copia del fichero a directorio old
        // y lo borramos
        // Los nombres de fichero tienen formato competicion_XXXX_jornada_XX_quini.txt
        
        File fichero = new File(ruta + "\\competicion_" + comp + "_jornada_" + jornada + "_quini.txt");
        if (fichero.exists()){
            // Si no existe creamos directorio old
            File dir = new File(ruta + "\\old");
            if (!dir.exists()) dir.mkdir();
            File ficheroOld = new File(ruta + "\\old\\competicion_" + comp + "_jornada_" + jornada + "_quini.txt");
            copiarFichero(fichero, ficheroOld);
            fichero.delete();
        }
        
    }

    public static ArrayList<HashMap<String,Object>> obtenerDatosFicherosQuini(
            String ruta) throws FileNotFoundException, IOException{
        // Los nombres de fichero tienen formato competicion_XXXX_jornada_XX_quini.txt
        // Devuelve un lista de vectores con competicion, jornada, partido, resultado

        ArrayList datosFich = new ArrayList<HashMap<String,Object>>();

        File dir = new File(ruta);
        String[] listaFiles = dir.list();
        String nomFich;

        for (String nomFile : listaFiles) {
            
            if (!nomFile.contains("quini"))
                continue;

            HashMap<String,Object> datos = new HashMap<String,Object>();

            String[] info = nomFile.split("_");

            if (info.length != 5) {
                logApp.log(Level.SEVERE, "Nombre de formato fichero quiniela incorrecto ".concat(nomFile));
                throw new IllegalArgumentException(
                        "Nombre de formato fichero quiniela incorrecto ".concat(nomFile));
            }

            nomFich = ruta + "/" + nomFile;

            datos.put("competicion", info[1]);
            datos.put("jornada", info[3]);
            
            String[] lineas = lineasFicheroUTF8(nomFich);
            try{

                String partidos[] = new String[15];
                String resultados[] = new String[15];

                int i = 0;
                for (String linea : lineas) {
                    String[] campos = linea.split("\t");
                    if (campos.length > 0){

                        partidos[i] = campos[0];
                        if (campos.length > 1 && !campos[1].equals(""))
                            resultados[i] = campos[1];
                        else
                            resultados[i] = null;

                        i++;
                    }
                }

                datos.put("partidos", partidos);
                datos.put("resultados", resultados);

            } catch (Exception ex) {
                logApp.log(Level.SEVERE, ex.getMessage());
           
            } 

            datosFich.add(datos);
        }

        return datosFich;


    }

     public static void eliminarFicheros(
            String ruta) throws FileNotFoundException, IOException{
        // Elimina todos los ficheros que estan en la ruta

        eliminarFicheros(ruta, null);
    }
     
    public static void eliminarFicheros(
            String ruta, String patron) throws FileNotFoundException, IOException{
        // Elimina todos los ficheros que estan en la ruta

        File dir = new File(ruta);
        String[] listaFiles = dir.list();

        for (String nomFile : listaFiles) {
            
            if (patron != null && !nomFile.contains(patron))
                continue;

            String nomFich = ruta + "/" + nomFile;

            File fr = new File(nomFich);

            fr.delete();
         }
    }
    
     public static HashMap<String,String> obtenerAyuda(
            String nomFich, String codigo) {
        
        HashMap datos = new HashMap<String,String>();                   
       
        try{
            String[] lineas = lineasFicheroUTF8(nomFich);
            String linea;
            
            boolean encontrado = false;
            for (int i = 0; i < lineas.length; i++){
                linea = lineas[i];
                if (!linea.isEmpty() && !linea.equals("\r") && linea.substring(0, 2).equals("+%")){
                    if (linea.split("%")[1].equals(codigo)){
                        datos.put("titulo", linea.split("%")[2]);
                        StringBuilder txt = new StringBuilder();
                        for (int ii = i + 1; ii < lineas.length; ii++){
                            linea = lineas[ii];
                            if (linea.contains("***")) break;
                            txt.append(linea).append("</br>");
                        }
                        datos.put("texto", txt.toString());
                        encontrado = true;
                        break;
                    }                   
                }

            }  
            if (!encontrado){
                datos.put("titulo", "No se encontro nada sobre el tema");
                datos.put("texto", "No se puede mostrar ayuda");
            }

        } catch (Exception ex) {
            datos.put("titulo", "Error en localizacion");
            datos.put("texto", "No se puede mostrar ayuda\n Error: " + ex.getMessage());
            
            
        }
        
        return datos;


    }
	
    public static String obtenerIndiceAyuda(
            String nomFich, String rutaBase, String codigo) {
        
	StringBuilder txt = new StringBuilder();
    
        try{
            String[] lineas = lineasFicheroUTF8(nomFich);
            String linea;
			
            txt.append("<b>").append(codigo.toUpperCase()).append("</b>").append("</br>").append("</br>");
            
            boolean encontrado = false;
            for (int i = 0; i < lineas.length; i++){
                linea = lineas[i];
                if (!linea.isEmpty() && !linea.equals("\r") && linea.substring(0, 2).equals("+%")){
                    String codItem = linea.split("%")[1];
                    String tituloItem = linea.split("%")[2];
                    txt.append("<a href=\"").append(rutaBase).append("/ayuda?grupo=").append(codigo).append("&codigo=");
                    txt.append(codItem).append("\" target=\"_blank\">").append(tituloItem).append("</a><br/>");
                    
                    encontrado = true;
                }
            }  
            txt.append("</br>");
            if (!encontrado){
                return "No se puede mostrar ayuda";
            }

        } catch (Exception ex) {
            return "No se puede mostrar ayuda\n Error: " + ex.getMessage();
        }
        
        return txt.toString();


    }


}

