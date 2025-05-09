package mld.clubdeportivo.utilidades;

import java.io.*;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.HashMap;
import mld.clubdeportivo.base.EquipoMaestro;
import mld.clubdeportivo.base.futbol8.EntrenadorMaestroFutbol8;
import mld.clubdeportivo.base.futbol8.JugadorMaestroFutbol8;
import mld.clubdeportivo.bd.DAOException;
import mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.grabarEntrenadorMaestroFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.grabarEquipoMaestroFutbol8;
import static mld.clubdeportivo.bd.futbol8.JDBCDAOFutbol8.grabarJugadorMaestroFutbol8;
import static mld.clubdeportivo.utilidades.UtilGenericas.formatearNumChars;

/**
 *
 * @author Miguel
 */
public class IODatos {

     private static Logger logApp
            = LoggerFactory.getLogger(IODatos.class.getName());
     
     
     private static void copiarFichero(File f1, File f2){
 
         try {
             OutputStream out;
            try (InputStream in = new FileInputStream(f1)) {
                out = new FileOutputStream(f2);
                var buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
             out.close();
             
         } catch (IOException ioe){
         }
     }

     public static String leerFicheroUTF8(String nomFich) throws IOException {
         
         var buffer = new StringBuilder();
         var fis = new FileInputStream(nomFich);
         var isr = new InputStreamReader(fis, "UTF8");
        try (Reader in = new BufferedReader(isr)) {
            int ch;
            var primero = true;
            while ((ch = in.read()) > -1) {
                // El primer caracter no lo cogemos, parece que son codigos
                //if (!primero)
                buffer.append((char)ch);
                //else
                //    primero = false;
            }
        }
         
         return buffer.toString();

    }
     
    public static String[] lineasFicheroUTF8(String nomFich) throws IOException {
        
        var txt = leerFicheroUTF8(nomFich);
        
        return txt.split("\n");
        
    }
    
    private IODatos(){}

    public static void formatearApellidoNombre(String fich_origen,
            String fich_destino) throws IOException

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
    
    
    public static BufferedReader br(String nomFich) 
            throws FileNotFoundException, UnsupportedEncodingException{
        
        var br = new BufferedReader(
                new InputStreamReader(new FileInputStream(nomFich), "UTF8"));
        
        return br;
    }

    public static void cargarMaestroJugadores(String fich_origen
            ) throws IOException, DAOException

    {

          var lineas = lineasFicheroUTF8(fich_origen);


          try {

              for (var linea : lineas) {

                  var campos = linea.split("\t");
                  var jug = new JugadorMaestroFutbol8(campos[0],
                          campos[1]);
                  grabarJugadorMaestroFutbol8(jug);
              }
          } catch (DAOException ex) {
              throw ex;
          }
    }

    public static void cargarMaestroEntrenadores(String fich_origen
            ) throws IOException, DAOException

    {

         var lineas = lineasFicheroUTF8(fich_origen);

          try {

              for (var linea : lineas) {

                  var campos = linea.split("\t");
                  var entr =
                          new EntrenadorMaestroFutbol8(campos[0]);
                  grabarEntrenadorMaestroFutbol8(entr);
              }
          } catch (DAOException ex) {
              throw ex;
          }
        
    }

    public static void cargarMaestroEquipos(String fich_origen
            ) throws IOException, DAOException

    {
        
        var lineas = lineasFicheroUTF8(fich_origen);
        
        try {
            
            for (var linea : lineas) {
                
                var campos = linea.split("\t");
                
                if (campos[0].equals("")) continue;
                var eq = new EquipoMaestro();
                eq.setNombre(formatearNumChars(campos[0], 20));
                try{        
                    grabarEquipoMaestroFutbol8(eq);
                }
                catch (Exception ex){
                    logApp.error("Error alta nombre equipo maestro : ".concat(ex.getMessage()));
                }
            }
        }
        catch (Exception ex){
            logApp.error("Error alta nombre equipo maestro : ".concat(ex.getMessage()));
        }
        
    }
    
    public static void eliminarFicheroJornadaValidada(String ruta, String comp, int jornada){
        // hacemos copia del fichero a directorio old
        // y lo borramos
        // Los nombres de fichero tienen formato competicion_XXXX_jornada_XX_quini.txt
        
        var fichero = new File(ruta + "\\competicion_" + comp + "_jornada_" + jornada + "_quini.txt");
        if (fichero.exists()){
            // Si no existe creamos directorio old
            var dir = new File(ruta + "\\old");
            if (!dir.exists()) dir.mkdir();
            var ficheroOld = new File(ruta + "\\old\\competicion_" + comp + "_jornada_" + jornada + "_quini.txt");
            copiarFichero(fichero, ficheroOld);
            fichero.delete();
        }
        
    }

    public static ArrayList<HashMap<String,Object>> obtenerDatosFicherosQuini(
            String ruta) throws FileNotFoundException, IOException{
        // Los nombres de fichero tienen formato competicion_XXXX_jornada_XX_quini.txt
        // Devuelve un lista de vectores con competicion, jornada, partido, resultado

        ArrayList datosFich = new ArrayList<HashMap<String,Object>>();

        var dir = new File(ruta);
        var listaFiles = dir.list();
        String nomFich;

        for (var nomFile : listaFiles) {
            
            if (!nomFile.contains("quini"))
                continue;

            var datos = new HashMap<>();
            var info = nomFile.split("_");

            if (info.length != 5) {
                logApp.error("Nombre de formato fichero quiniela incorrecto ".concat(nomFile));
                throw new IllegalArgumentException(
                        "Nombre de formato fichero quiniela incorrecto ".concat(nomFile));
            }

            nomFich = ruta + "/" + nomFile;

            datos.put("competicion", info[1]);
            datos.put("jornada", info[3]);
            
            var lineas = lineasFicheroUTF8(nomFich);
            try{

                var partidos = new String[15];
                var resultados = new String[15];
                var i = 0;
                for (var linea : lineas) {
                    var campos = linea.split("\t");
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
                logApp.error(ex.getMessage());
           
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

        var dir = new File(ruta);
        var listaFiles = dir.list();
        for (var nomFile : listaFiles) {
            
            if (patron != null && !nomFile.contains(patron))
                continue;

            var nomFich = ruta + "/" + nomFile;
            var fr = new File(nomFich);

            fr.delete();
         }
    }
    
     public static HashMap<String,String> obtenerAyuda(
            String nomFich, String codigo) {
        
        HashMap datos = new HashMap<String,String>();                   
       
        try{
            var lineas = lineasFicheroUTF8(nomFich);
            String linea;
            
            var encontrado = false;
            for (var i = 0; i < lineas.length; i++){
                linea = lineas[i];
                if (!linea.isEmpty() && !linea.equals("\r") && linea.substring(0, 2).equals("+%")){
                    if (linea.split("%")[1].equals(codigo)){
                        datos.put("titulo", linea.split("%")[2]);
                        var txt = new StringBuilder();
                        for (var ii = i + 1; ii < lineas.length; ii++){
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
        
	var txt = new StringBuilder();
    
        try{
            var lineas = lineasFicheroUTF8(nomFich);
            String linea;
			
            txt.append("<b>").append(codigo.toUpperCase()).append("</b>").append("</br>").append("</br>");
            
            var encontrado = false;
            for (String linea1 : lineas) {
                linea = linea1;
                if (!linea.isEmpty() && !linea.equals("\r") && linea.substring(0, 2).equals("+%")) {
                    var codItem = linea.split("%")[1];
                    var tituloItem = linea.split("%")[2];
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

