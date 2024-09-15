/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.utilidades;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mld.playhitsgame.exemplars.Cancion;
import mld.playhitsgame.exemplars.OpcionAnyoTmp;
import mld.playhitsgame.exemplars.OpcionInterpreteTmp;
import mld.playhitsgame.exemplars.OpcionTituloTmp;
import mld.playhitsgame.exemplars.Partida;
import mld.playhitsgame.exemplars.Respuesta;
import mld.playhitsgame.exemplars.Ronda;
import mld.playhitsgame.exemplars.Usuario;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 *
 * @author miguel
 */
public class Utilidades {

    private static final int NUMERO_OPCIONES = 5;

    public static int calcularPtsPorAnyo(int anyo, Cancion cancion) {

        if (anyo == cancion.getAnyo()) {
            return 15;
        } else {
            return 0;
        }
    }

    public static int calcularPtsPorTitulo(String titulo, Cancion cancion) {

        if (titulo != null && titulo.equals(cancion.getTitulo())) {
            return 15;
        } else {
            return 0;
        }
    }

    public static int calcularPtsPorInterprete(String interprete, Cancion cancion) {

        if (interprete != null && interprete.equals(cancion.getInterprete())) {
            return 15;
        } else {
            return 0;
        }
    }

    public static Cancion cancionRandom(List<Cancion> lista) {
        int i;
        i = (int) (Math.floor(Math.random() * lista.size()));

        return lista.get(i);
    }

    public static void asignarCancionesAleatorias(Partida partida, List<Cancion> canciones) {

        HashMap<Long, Cancion> listaCanciones = new HashMap();

        while (listaCanciones.size() < partida.getRondas().size() + 1) {

            Cancion cancion = cancionRandom(canciones);
            listaCanciones.put(cancion.getId(), cancion);
        }

        ArrayList<Cancion> lista = new ArrayList();
        for (HashMap.Entry<Long, Cancion> elem : listaCanciones.entrySet()) {
            lista.add(elem.getValue());
        }

        int i = 0;
        for (Ronda ronda : partida.getRondas()) {
            ronda.setCancion(lista.get(i));
            i = i + 1;
        }

    }

    private static List<Cancion> cancionesParaListaOpciones(List<Cancion> canciones,
            Cancion cancionCorrecta, int numero) {

        Map<Long, Cancion> lista = new HashMap();
        lista.put(cancionCorrecta.getId(), cancionCorrecta);
        while (lista.size() < numero) {
            Cancion aleatoria = cancionRandom(canciones);
            lista.put(aleatoria.getId(), aleatoria);
        }
        ArrayList<Cancion> listaFinal = new ArrayList();
        for (Map.Entry<Long, Cancion> elem : lista.entrySet()) {
            listaFinal.add(elem.getValue());
        }

        // Las reordenamos aleatoriamente para que la correcta, no este
        // siempre la primera
        ArrayList<Cancion> listaDesordenada = new ArrayList();
        while (!listaFinal.isEmpty()) {
            int i = (int) (Math.floor(Math.random() * listaFinal.size()));
            listaDesordenada.add(listaFinal.get(i));
            listaFinal.remove(i);
        }

        return listaDesordenada;
    }

    private static String encriptarString(String txt) {

        // Subtituimos la mitad aleatoria de letras por *
        int x = txt.replace(" ", "").length() / 2;
        StringBuilder newText = new StringBuilder(txt);

        double s;
        for (int i = 0; i < x; i++) {
            s = Math.floor(Math.random() * txt.length());
            if (newText.charAt((int) s) == ' ') {
                x = x + 1;
                continue;
            }
            newText.setCharAt((int) s, '*');
        }

        return newText.toString();
    }
    
    public static List<OpcionTituloTmp> opcionesTitulosCanciones(Ronda ronda, List<Cancion> canciones) {
        
        List<Cancion> cancionesParaOpciones
                = cancionesParaListaOpciones(canciones, ronda.getCancion(), NUMERO_OPCIONES);
        
        return  obtenerOpcionesTitulosCanciones(ronda, cancionesParaOpciones);     
    }    

    private static List<OpcionTituloTmp> obtenerOpcionesTitulosCanciones(Ronda ronda, List<Cancion> cancionesParaOpciones) {

        ArrayList<OpcionTituloTmp> opciones = new ArrayList();
        OpcionTituloTmp newObj;
        for (Cancion cancion : cancionesParaOpciones) {
            newObj = new OpcionTituloTmp();
            newObj.setPartida(ronda.getPartida().getId());
            newObj.setRonda(ronda.getId());
            newObj.setCancion(cancion.getId());
            newObj.setOpTitulo(encriptarString(cancion.getTitulo()));
            opciones.add(newObj);
        }

        return opciones;
    }

    public static List<OpcionTituloTmp> opcionesTitulosCanciones(Ronda ronda) {

        // de las canciones elije aleatoriamente que une a la correcta y 
        // devuelve una lista con las canciones encriptadas
        List<Cancion> cancionesParaOpciones
                = cancionesParaListaOpciones(ronda.getPartida().canciones(), ronda.getCancion(), NUMERO_OPCIONES);

        return obtenerOpcionesTitulosCanciones(ronda, cancionesParaOpciones);
    }
    
    public static List<OpcionInterpreteTmp> opcionesInterpretesCanciones(Ronda ronda, List<Cancion> canciones) {
        
        List<Cancion> cancionesParaOpciones
                = cancionesParaListaOpciones(canciones, ronda.getCancion(), NUMERO_OPCIONES);
        
        return  obtenerOpcionesInterpretesCanciones(ronda, cancionesParaOpciones);     
    }    

    private static List<OpcionInterpreteTmp> obtenerOpcionesInterpretesCanciones(Ronda ronda, List<Cancion> cancionesParaOpciones) {

        // de las canciones elije aleatoriamente que une a la correcta y 
        // devuelve una lista con las canciones encriptadas
        ArrayList<OpcionInterpreteTmp> opciones = new ArrayList();        

        OpcionInterpreteTmp newObj;
        for (Cancion cancion : cancionesParaOpciones) {
            newObj = new OpcionInterpreteTmp();
            newObj.setPartida(ronda.getPartida().getId());
            newObj.setRonda(ronda.getId());
            newObj.setCancion(cancion.getId());
            newObj.setOpInterprete(encriptarString(cancion.getInterprete()));
            opciones.add(newObj);
        }

        return opciones;
    }

    public static List<OpcionInterpreteTmp> opcionesInterpretesCanciones(Ronda ronda) {

        List<Cancion> cancionesParaOpciones
                = cancionesParaListaOpciones(ronda.getPartida().canciones(),
                        ronda.getCancion(), NUMERO_OPCIONES);
        
        return obtenerOpcionesInterpretesCanciones(ronda, cancionesParaOpciones);
        
    }
    
    
    public static int[] rangoAnyosCanciones(ArrayList<Cancion> canciones){
        
        int ini = 9999;
        int fin = 0;
        
        for (Cancion elem : canciones){
            if (elem.getAnyo() < ini)
                ini = elem.getAnyo();
            if (elem.getAnyo() > fin)
                fin = elem.getAnyo();
        }
        
        return new int[]{ini,fin};        
    }
    

    public static List<OpcionAnyoTmp> opcionesAnyosCanciones(Ronda ronda, int anyoIni, int anyoFin) {

        // de las canciones elije aleatoriamente que une a la correcta y 
        // devuelve una lista con las canciones encriptadas
        ArrayList<OpcionAnyoTmp> opciones = new ArrayList();
        int anyoOk = ronda.getCancion().getAnyo();
        int[] anyos = new int[NUMERO_OPCIONES + 1];

        for (int i = 1; i <= NUMERO_OPCIONES; i++) {
            anyos[i] = (int) (Math.random() * (anyoFin - anyoIni)) + anyoIni;
            for (int ii = 1; ii < i; ii++) {
                if (anyos[i] == anyos[ii]) {
                    i = i - 1;
                    break;
                }
            }
        }

        boolean opOk = false;
        for (int i = 1; i <= NUMERO_OPCIONES; i++) {
            if (anyos[i] == anyoOk) {
                opOk = true;
                break;
            }
        }
        // insertamos la correcta aleatoriamente entre las opciones
        if (!opOk) {
            int n = (int) (Math.random() * NUMERO_OPCIONES) + 1;
            anyos[n] = anyoOk;
        }

        OpcionAnyoTmp newObj;
        for (int i = 1; i <= NUMERO_OPCIONES; i++) {
            newObj = new OpcionAnyoTmp();
            newObj.setPartida(ronda.getPartida().getId());
            newObj.setRonda(ronda.getId());
            newObj.setCancion(ronda.getCancion().getId());
            newObj.setOpAnyo(anyos[i]);
            opciones.add(newObj);
        }

        return opciones;
    }

    public static String nombreServidor() {

        InetAddress ip;
        String host = null;
        try {
            ip = InetAddress.getLocalHost();
            host = ip.getHostName();
        } catch (UnknownHostException ex) {
            System.out.println("No se puede obtener nombre del host");
        }

        return host;
    }

    public static void validarPassword(String pw1, String pw2) throws Exception {

        if (pw1 == null || pw2 == null) {
            throw new Exception("Alguna contraseña esta en blanco");
        }
        if (!pw1.equals(pw2)) {
            throw new Exception("Los contraseñas son diferentes");
        }
        if (pw1.contains(" ")) {
            throw new Exception("Los contraseña no puede tener espacios en blanco");
        }
        if (pw1.length() < 4) {
            throw new Exception("Los contraseña debe tener al menos 4 caracteres");
        }

    }

    public static int calcularPtsUsuario(Usuario usuario, Partida partida, boolean isGeneral) {

        List<Respuesta> respuestas = new ArrayList();

        for (Ronda ronda : partida.getRondas()) {
            for (Respuesta respuesta : ronda.getRespuestas()) {
                if (respuesta.getUsuario().equals(usuario)) {
                    respuestas.add(respuesta);
                }
            }
        }

        return calcularPts(respuestas, partida, isGeneral);
    }

    public static int calcularPts(List<Respuesta> respuestas, Partida partida, boolean isGeneral) {

        int pts = 0;

        for (Respuesta resp : respuestas) {
            pts = pts + resp.getPuntos();
        }

        if (isGeneral) {

            // factor correccion por numero canciones posibles    
            int n = partida.getNCanciones();
            pts = pts * n / 1000;

            // factor correccion por el numero de años
            int anyoIni = partida.getAnyoInicial();
            int anyoFin = partida.getAnyoFinal();
            pts = pts * (anyoFin - anyoIni + 1);

            // factor rondas
            pts = pts / partida.getRondas().size();
        }

        return pts;
    }

    public static ArrayList<String> leerAyuda(String fich) {

        ArrayList<String> lineas = new ArrayList();

        Resource resource = new ClassPathResource(fich);

        try {
            InputStream inputStream = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                lineas.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error al obtener ayuda " + fich + " : " + e.getMessage());
        }

        return lineas;
    }

    public static void exportarCanciones(List<Cancion> canciones, String ruta) {

        StringBuilder buffer = new StringBuilder();
        char sep = '\t';

        for (Cancion cancion : canciones) {
            buffer.append(String.valueOf(cancion.getId())).append(sep);
            buffer.append(cancion.getTitulo()).append(sep);
            buffer.append(cancion.getInterprete()).append(sep);
            buffer.append(String.valueOf(cancion.getAnyo())).append("\n");
        }

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");
        String nomfich = ruta + "/canciones_validar_" + currentDate.format(formatter) + ".csv";

        escribirFichero(buffer, nomfich);
    }

    public static void escribirFichero(StringBuilder txt, String nomfich) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomfich))) {
            writer.write(txt.toString());
            System.out.println("Contenido escrito en el archivo: " + nomfich);
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }

    }

}
