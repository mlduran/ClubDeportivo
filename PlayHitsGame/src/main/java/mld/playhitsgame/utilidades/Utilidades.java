/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.utilidades;

import jakarta.mail.MessagingException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import mld.playhitsgame.correo.EmailServicioMetodos;
import mld.playhitsgame.correo.Mail;
import mld.playhitsgame.exemplars.Cancion;
import mld.playhitsgame.exemplars.CancionTmp;
import mld.playhitsgame.exemplars.Dificultad;
import mld.playhitsgame.exemplars.OpcionAnyoTmp;
import mld.playhitsgame.exemplars.OpcionInterpreteTmp;
import mld.playhitsgame.exemplars.OpcionTituloTmp;
import mld.playhitsgame.exemplars.Partida;
import mld.playhitsgame.exemplars.PtsUsuario;
import mld.playhitsgame.exemplars.Respuesta;
import mld.playhitsgame.exemplars.Ronda;
import mld.playhitsgame.exemplars.Tema;
import mld.playhitsgame.exemplars.Usuario;
import mld.playhitsgame.web.ControladorVista;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailSendException;

/**
 *
 * @author miguel
 */
public class Utilidades {

    private static final int NUMERO_OPCIONES = 5;
    private static final double UMBRAL_SIMILITUD = 0.90;

    public static int calcularPtsPorAnyo(int anyo, Cancion cancion, Dificultad dificultad) {

        int pts = 0;
        if (anyo == cancion.getAnyo()) {
            pts = 15;
        }

        // Dependiendo de la dificultad multiplicamos o dividimos por 2
        if (dificultad.equals(Dificultad.Facil)) {
            pts = pts / 2;
        }
        if (dificultad.equals(Dificultad.Dificil)) {
            pts = pts * 2;
        }

        return pts;
    }

    public static int calcularPtsPorTitulo(String titulo, Cancion cancion,
            Dificultad dificultad, boolean sinOfuscar) {

        int pts = 0;
        if (titulo != null && titulo.equals(cancion.getTitulo())) {
            pts = 15;
        }

        // Dependiendo de la dificultad multiplicamos o dividimos por 2
        if (dificultad.equals(Dificultad.Facil)) {
            pts = pts / 2;
        }
        if (dificultad.equals(Dificultad.Dificil)) {
            pts = pts * 2;
        }

        if (sinOfuscar) {
            pts = pts / 2;
        }

        return pts;

    }

    public static int calcularPtsPorInterprete(String interprete, Cancion cancion,
            Dificultad dificultad, boolean sinOfuscar) {

        int pts = 0;
        if (interprete != null && interprete.equals(cancion.getInterprete())) {
            pts = 15;
        }

        // Dependiendo de la dificultad multiplicamos o dividimos por 2
        if (dificultad.equals(Dificultad.Facil)) {
            pts = pts / 2;
        }
        if (dificultad.equals(Dificultad.Dificil)) {
            pts = pts * 2;
        }

        if (sinOfuscar) {
            pts = pts / 2;
        }

        return pts;
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

    public static List<Cancion> cancionesParaListaOpciones(List<Cancion> canciones,
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

    public static String encriptarString(String txt) {

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

    public static List<OpcionTituloTmp> opcionesTitulosCanciones(Ronda ronda, List<Cancion> canciones, boolean isEncriptar) {

        List<Cancion> cancionesParaOpciones
                = cancionesParaListaOpciones(canciones, ronda.getCancion(), NUMERO_OPCIONES);

        return obtenerOpcionesTitulosCanciones(ronda, cancionesParaOpciones, isEncriptar);
    }

    private static List<OpcionTituloTmp> obtenerOpcionesTitulosCanciones(Ronda ronda,
            List<Cancion> cancionesParaOpciones, boolean isEncriptar) {

        ArrayList<OpcionTituloTmp> opciones = new ArrayList();
        OpcionTituloTmp newObj;
        for (Cancion cancion : cancionesParaOpciones) {
            newObj = new OpcionTituloTmp();
            newObj.setPartida(ronda.getPartida().getId());
            newObj.setRonda(ronda.getId());
            newObj.setCancion(cancion.getId());
            if (isEncriptar) {
                newObj.setOpTitulo(encriptarString(cancion.getTitulo()));
            } else {
                newObj.setOpTitulo(cancion.getTitulo());
            }
            opciones.add(newObj);
        }

        return opciones;
    }

    public static List<OpcionTituloTmp> opcionesTitulosCanciones(Ronda ronda, boolean isEncriptar) {

        // de las canciones elije aleatoriamente que une a la correcta y 
        // devuelve una lista con las canciones encriptadas
        List<Cancion> cancionesParaOpciones
                = cancionesParaListaOpciones(ronda.getPartida().canciones(), ronda.getCancion(), NUMERO_OPCIONES);

        return obtenerOpcionesTitulosCanciones(ronda, cancionesParaOpciones, isEncriptar);
    }

    public static List<OpcionInterpreteTmp> opcionesInterpretesCanciones(Ronda ronda,
            List<Cancion> canciones, boolean isEncriptar) {

        List<Cancion> cancionesParaOpciones
                = cancionesParaListaOpciones(canciones, ronda.getCancion(), NUMERO_OPCIONES);

        return obtenerOpcionesInterpretesCanciones(ronda, cancionesParaOpciones, isEncriptar);
    }

    private static List<OpcionInterpreteTmp> obtenerOpcionesInterpretesCanciones(Ronda ronda,
            List<Cancion> cancionesParaOpciones, boolean isEncriptar) {

        // de las canciones elije aleatoriamente que une a la correcta y 
        // devuelve una lista con las canciones encriptadas
        ArrayList<OpcionInterpreteTmp> opciones = new ArrayList();

        OpcionInterpreteTmp newObj;
        for (Cancion cancion : cancionesParaOpciones) {
            newObj = new OpcionInterpreteTmp();
            newObj.setPartida(ronda.getPartida().getId());
            newObj.setRonda(ronda.getId());
            newObj.setCancion(cancion.getId());
            if (isEncriptar) {
                newObj.setOpInterprete(encriptarString(cancion.getInterprete()));
            } else {
                newObj.setOpInterprete(cancion.getInterprete());
            }
            opciones.add(newObj);
        }

        return opciones;
    }

    public static List<OpcionInterpreteTmp> opcionesInterpretesCanciones(Ronda ronda, boolean isEncriptar) {

        List<Cancion> cancionesParaOpciones
                = cancionesParaListaOpciones(ronda.getPartida().canciones(),
                        ronda.getCancion(), NUMERO_OPCIONES);

        return obtenerOpcionesInterpretesCanciones(ronda, cancionesParaOpciones, isEncriptar);

    }

    public static int[] rangoAnyosCanciones(ArrayList<Cancion> canciones) {

        int ini = 9999;
        int fin = 0;

        for (Cancion elem : canciones) {
            if (elem.getAnyo() < ini) {
                ini = elem.getAnyo();
            }
            if (elem.getAnyo() > fin) {
                fin = elem.getAnyo();
            }
        }

        return new int[]{ini, fin};
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

    public static List<String> opcionesAnyosCancionesInvitado(Cancion cancion, int anyoIni, int anyoFin) {

        // de las canciones elije aleatoriamente que une a la correcta y 
        // devuelve una lista con las canciones encriptadas
        int anyoOk = cancion.getAnyo();
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

        ArrayList<String> listaAnyos = new ArrayList();
        for (int i = 1; i <= NUMERO_OPCIONES; i++) {
            listaAnyos.add(String.valueOf(anyos[i]));
        }

        return listaAnyos;
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

    public static Cancion existeCancion(Cancion newCancion, List<Cancion> canciones) {

        Cancion cancionExistente = null;
        LevenshteinDistance distancia = new LevenshteinDistance();

        for (Cancion cancion : canciones) {

            double similitudTitulo = calcularSimilitud(distancia, newCancion.getTitulo(), cancion.getTitulo());
            double similitudInterprete = calcularSimilitud(distancia, newCancion.getInterprete(), cancion.getInterprete());

            if (similitudTitulo >= UMBRAL_SIMILITUD && similitudInterprete >= UMBRAL_SIMILITUD) {
                cancionExistente = cancion;
            }
        }

        return cancionExistente;
    }

    public static CancionTmp existeCancionTmp(CancionTmp newCancion, List<CancionTmp> canciones) {

        CancionTmp cancionExistente = null;
        LevenshteinDistance distancia = new LevenshteinDistance();

        for (CancionTmp cancion : canciones) {

            double similitudTitulo = calcularSimilitud(distancia, newCancion.getTitulo(), cancion.getTitulo());
            double similitudInterprete = calcularSimilitud(distancia, newCancion.getInterprete(), cancion.getInterprete());

            if (similitudTitulo >= UMBRAL_SIMILITUD && similitudInterprete >= UMBRAL_SIMILITUD) {
                cancionExistente = cancion;
            }
        }

        return cancionExistente;
    }

    public static Cancion existeCancion(CancionTmp newCancion, List<Cancion> canciones) {

        Cancion cancionExistente = null;
        LevenshteinDistance distancia = new LevenshteinDistance();

        for (Cancion cancion : canciones) {

            double similitudTitulo = calcularSimilitud(distancia, newCancion.getTitulo(), cancion.getTitulo());
            double similitudInterprete = calcularSimilitud(distancia, newCancion.getInterprete(), cancion.getInterprete());

            if (similitudTitulo >= UMBRAL_SIMILITUD && similitudInterprete >= UMBRAL_SIMILITUD) {
                cancionExistente = cancion;
            }
        }

        return cancionExistente;
    }

    public static boolean isExisteCancionTmp(CancionTmp newCancion, List<Cancion> canciones) {

        boolean cancionExistente = false;
        LevenshteinDistance distancia = new LevenshteinDistance();

        for (Cancion cancion : canciones) {

            double similitudTitulo = calcularSimilitud(distancia, newCancion.getTitulo(), cancion.getTitulo());
            double similitudInterprete = calcularSimilitud(distancia, newCancion.getInterprete(), cancion.getInterprete());

            if (similitudTitulo >= UMBRAL_SIMILITUD && similitudInterprete >= UMBRAL_SIMILITUD) {
                cancionExistente = true;
            }
        }

        return cancionExistente;
    }

    public static List<Cancion> duplicadosParaEliminar(List<Cancion> canciones) {

        return buscarDuplicados(canciones, true, false);
    }

    public static List<Cancion> duplicadosParaActualizar(List<Cancion> canciones) {

        return buscarDuplicados(canciones, false, true);
    }

    public static List<Cancion> buscarDuplicados(List<Cancion> canciones,
            boolean isEliminar, boolean isModificar) {

        ArrayList<Cancion> lista = new ArrayList();
        LevenshteinDistance distancia = new LevenshteinDistance();

        for (int i = 0; i < canciones.size(); i++) {
            Cancion cancion1 = canciones.get(i);
            List<Cancion> grupo = new ArrayList<>();
            for (int j = i + 1; j < canciones.size(); j++) {
                Cancion cancion2 = canciones.get(j);

                double similitudTitulo = calcularSimilitud(distancia, cancion1.getTitulo(), cancion2.getTitulo());
                double similitudInterprete = calcularSimilitud(distancia, cancion1.getInterprete(), cancion2.getInterprete());

                if (similitudTitulo >= UMBRAL_SIMILITUD && similitudInterprete >= UMBRAL_SIMILITUD) {
                    grupo.add(cancion2);
                }
            }
            if (!grupo.isEmpty()) {
                grupo.add(0, cancion1);  // Añadir la primera canción también al grupo
            }

            if (isEliminar) {
                // Si los años son diferentes no eliminamos
                int anyo = 0;
                for (Cancion canDuplicada : grupo) {
                    if (anyo == 0) {
                        anyo = canDuplicada.getAnyo();
                        continue;
                    }
                    if (anyo == canDuplicada.getAnyo()) {
                        lista.add(canDuplicada);
                    }
                }
            } else if (isModificar) {
                HashSet<Tema> temas = new HashSet();
                for (Cancion canDuplicada : grupo) {
                    temas.addAll(canDuplicada.getTematicas());
                }
                for (Cancion canDuplicada : grupo) {
                    canDuplicada.setTematicas(new ArrayList<>(temas));
                    lista.add(canDuplicada);
                }
            } else {
                for (Cancion canDuplicada : grupo) {
                    lista.add(canDuplicada);
                }
            }
        }

        return lista;
    }

    private static double calcularSimilitud(LevenshteinDistance distancia, String s1, String s2) {
        int maxLen = Math.max(s1.length(), s2.length());
        int distanciaLevenshtein = distancia.apply(s1, s2);

        // Similaridad: 1 - (Distancia de Levenshtein / Longitud máxima de las dos cadenas)
        return 1.0 - (double) distanciaLevenshtein / maxLen;
    }

    public static String ejecutarComando(String comando) {
        StringBuilder resultado = new StringBuilder();

        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command("bash", "-c", comando);
            Process process = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String linea;
            while ((linea = reader.readLine()) != null) {
                resultado.append(linea).append("\n");
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Error al ejecutar el comando: " + comando);
            }

        } catch (IOException | InterruptedException | RuntimeException e) {
        }

        return resultado.toString();
    }

    public static String getCountryFromIP(String ipAddress) {
        String apiURL = "http://ip-api.com/json/" + ipAddress;
        try {
            URL url = new URL(apiURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            StringBuilder response;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }

            // Convert response to JSON
            JSONObject jsonResponse = new JSONObject(response.toString());
            return jsonResponse.getString("country"); // Retorna el país

        } catch (Exception e) {
            //e.printStackTrace();
            return "Desconocido";
        }
    }

    public static String filtrarTitulo(String titulo) {

        String[] phrasesToRemove = {
            "- Original mix",
            "- Radio Edit",
            "- Single Version",
            "- Radio Mix",
            "- Edit",
            "\\(7\" Version\\)",
            "\\(Remastered\\)",
            "- Remastered",
            "(Re-Record)",
            "\\(Remasterizado\\)",
            "- Remasterizado",
            "\\(Remasterizada\\)",
            "- Remasterizada"
        };

        for (String phrase : phrasesToRemove) {
            titulo = titulo.replaceAll("\\s*" + phrase + "\\s*", "");
        }

        titulo = titulo.replaceAll("- .*? Remaster", "");
        titulo = titulo.replaceAll("\\(From.*", "");
        titulo = titulo.replaceAll("\\- From.*", "");
        titulo = titulo.replaceAll("\\(Theme from.*", "");

        return titulo.trim();

    }

    public static boolean enviarMail(EmailServicioMetodos emailServicio,
            Usuario usuario, String asunto, String txt, String plantilla) {
        return enviarMail(emailServicio, usuario.getUsuario(), usuario.getNombre(), asunto, txt, plantilla);
    }

    public static boolean enviarMail(EmailServicioMetodos emailServicio,
            String mailDestino, String nombre, String asunto, String txt, String plantilla) {
        boolean ok = true;
        Mail mail = new Mail();
        try {
            mail.setAsunto(asunto);
            mail.setDestinatario(mailDestino);
            mail.setMensaje(txt);
            mail.setPlantilla(plantilla);
            mail.setNombre(nombre);
            emailServicio.enviarCorreo(mail);
        } catch (MessagingException | MailSendException ex) {
            ok = false;
            Logger.getLogger(ControladorVista.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ok;
    }

    private static boolean isURLValid(String urlString) {
        try {
            // Crear un objeto URL
            URL url = new URL(urlString);

            // Abrir una conexión HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Establecer el método HTTP HEAD para evitar descargar el archivo completo
            connection.setRequestMethod("HEAD");

            // Establecer tiempo de espera
            connection.setConnectTimeout(5000); // 5 segundos
            connection.setReadTimeout(5000);

            // Conectar al servidor
            connection.connect();

            // Verificar el código de respuesta HTTP
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                return false; // No es accesible
            }

            // Verificar el encabezado Content-Type
            String contentType = connection.getContentType();
            if (contentType != null && contentType.equals("audio/mpeg")) {
                return true; // La URL es válida y es un MP3
            }
        } catch (IOException e) {
            System.out.println("Se ha detectado una URL no valida : " + urlString);
            return false;
        }

        return false;
    }

    public static boolean validarReproduccion(Cancion cancion) {

        return isURLValid(cancion.getSpotifyplay());
    }

    public static boolean validarReproduccion(CancionTmp cancion) {

        return isURLValid(cancion.getSpotifyplay());
    }

    public static PtsUsuario ptsFindByUsuario(List<PtsUsuario> list, Usuario usuarioBuscado) {
        for (PtsUsuario ptsUsuario : list) {
            if (ptsUsuario.getUsuario().equals(usuarioBuscado)) {
                return ptsUsuario;
            }
        }
        return null; // No encontrado
    }

}
