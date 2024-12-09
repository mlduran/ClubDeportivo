/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.spotify;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import mld.playhitsgame.exemplars.Cancion;
import mld.playhitsgame.exemplars.CancionTmp;
import mld.playhitsgame.exemplars.Tema;
import mld.playhitsgame.services.CancionServicioMetodos;
import mld.playhitsgame.services.CancionTmpServicioMetodos;
import mld.playhitsgame.services.TemaServicioMetodos;
import mld.playhitsgame.utilidades.Utilidades;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author miguel
 */
@Service
public class UtilidadesSpotify {

    @Autowired
    CancionServicioMetodos servCancion;
    @Autowired
    CancionTmpServicioMetodos servCancionTmp;
    @Autowired
    TemaServicioMetodos servTema;

    private final String URL_PREV = "https://";

    public JSONArray obtenerDatosSpotify(String datos) {

        JSONArray lista = new JSONArray();
        JSONObject track, grupo, album, imagen;
        JSONArray artists, imagenes;

        JSONObject obJson = new JSONObject(datos);
        JSONArray elems;

        try {
            elems = obJson.getJSONArray("items");
        } catch (JSONException ex) {
            elems = new JSONArray();// No hay elementos
        }

        for (Object elem : elems) {

            try {

                JSONObject cancion = new JSONObject();

                JSONObject elemJson = (JSONObject) elem;
                track = elemJson.getJSONObject("track");
                artists = track.getJSONArray("artists");
                album = track.getJSONObject("album");
                int anyo = 0;
                String elAlbum = "";
                String laImagen = "";
                if (!album.isEmpty()) {
                    String anyoAlbum = album.getString("release_date").substring(0, 4);
                    anyo = Integer.parseInt(anyoAlbum);
                    imagenes = album.getJSONArray("images");
                    elAlbum = album.getString("name");
                    if (!imagenes.isEmpty()) {
                        imagen = imagenes.getJSONObject(0);
                        laImagen = imagen.getString("url");
                    }
                }
                grupo = artists.getJSONObject(0);

                cancion.append("id", track.getString("id"));
                String titulo = Utilidades.filtrarTitulo((String) track.getString("name"));
                cancion.append("titulo", titulo);
                cancion.append("interprete", grupo.getString("name"));
                cancion.append("anyo", anyo);
                cancion.append("album", elAlbum);
                cancion.append("imagen", laImagen);
                cancion.append("preview_url", URL_PREV);

                lista.put(cancion);

            } catch (JSONException ex) {
                System.out.println("Error al obtener cancion json: " + ex);
            }

        }

        return lista;

    }

    public List<CancionTmp> obtenerDatosJson(String datos, String anyo) {

        List<CancionTmp> lista = new LinkedList<>();
        JSONObject track, grupo, album, imagen;
        JSONArray artists, imagenes;

        JSONObject obJson = new JSONObject(datos);
        JSONArray elems;

        try {
            elems = obJson.getJSONArray("items");
        } catch (JSONException ex) {
            elems = new JSONArray();// No hay elementos
        }

        for (Object elem : elems) {

            try {

                CancionTmp cancion = new CancionTmp();

                JSONObject elemJson = (JSONObject) elem;
                track = elemJson.getJSONObject("track");
                artists = track.getJSONArray("artists");
                album = track.getJSONObject("album");
                imagenes = album.getJSONArray("images");
                grupo = artists.getJSONObject(0);
                String titulo = Utilidades.filtrarTitulo((String) track.getString("name"));
                cancion.setTitulo(titulo);
                cancion.setInterprete((String) grupo.getString("name"));
                cancion.setAnyo(Integer.getInteger(anyo));
                cancion.setSpotifyid((String) track.getString("id"));
                cancion.setSpotifyplay(URL_PREV);
                if (!imagenes.isEmpty()) {
                    imagen = imagenes.getJSONObject(0);
                    cancion.setSpotifyimagen(imagen.getString("url"));
                }

                lista.add(cancion);
            } catch (JSONException ex) {
                System.out.println("Error al obtener cancion json: " + ex);
            }

        }

        return lista;

    }

    public List<CancionTmp> obtenerDatosJson(String datos) {

        List<CancionTmp> lista = new LinkedList<>();
        JSONObject track, grupo, album, imagen;
        JSONArray artists, imagenes;

        JSONObject obJson = new JSONObject(datos);
        JSONArray elems;

        try {
            elems = obJson.getJSONArray("items");
        } catch (JSONException ex) {
            elems = new JSONArray();// No hay elementos
        }

        for (Object elem : elems) {

            try {

                CancionTmp cancion = new CancionTmp();

                JSONObject elemJson = (JSONObject) elem;
                track = elemJson.getJSONObject("track");
                artists = track.getJSONArray("artists");
                album = track.getJSONObject("album");
                if (!album.isEmpty()) {
                    String anyoAlbum = album.getString("release_date").substring(0, 4);
                    int anyo = Integer.parseInt(anyoAlbum);
                    cancion.setAnyo(anyo);
                    imagenes = album.getJSONArray("images");
                    cancion.setAlbum(album.getString("name"));
                    if (!imagenes.isEmpty()) {
                        imagen = imagenes.getJSONObject(0);
                        cancion.setSpotifyimagen(imagen.getString("url"));
                    }
                }
                grupo = artists.getJSONObject(0);

                String titulo = Utilidades.filtrarTitulo((String) track.getString("name"));
                cancion.setTitulo(titulo);
                cancion.setInterprete((String) grupo.getString("name"));
                cancion.setSpotifyid((String) track.getString("id"));
                cancion.setSpotifyplay(URL_PREV);

                lista.add(cancion);
            } catch (JSONException ex) {
                System.out.println("Error al obtener cancion json: " + ex);
            }

        }

        return lista;
    }

    public void grabarListaCanciones(List<CancionTmp> lista, boolean isTemas) {

        List<CancionTmp> cancionesBDTmp = servCancionTmp.findAll();
        List<Cancion> cancionesBD = servCancion.findAll();
        CancionTmp cancionParaGrabar;

        for (CancionTmp cancion : lista) {

            boolean altaTmp = false;
            cancionParaGrabar = null;

            String des = cancion.getSpotifyid() + " - " + cancion.getTitulo() + " - " + cancion.getInterprete();

            Optional<CancionTmp> resultadoTmp = servCancionTmp.findByIdSpotify(cancion.getSpotifyid());
            if (resultadoTmp.isPresent() && isTemas) {
                cancionParaGrabar = resultadoTmp.get();
                System.out.println("Cancion encontrada en BD Temporal, se añade para tema: " + des);
            }

            if (cancionParaGrabar == null) {
                Optional<Cancion> resultado = servCancion.findByIdSpotify(cancion.getSpotifyid());
                if (resultado.isPresent() && isTemas) {
                    cancionParaGrabar = cancion;
                    cancionParaGrabar.setSoloTemas(true);
                    altaTmp = true;
                    System.out.println("Cancion encontrada en BD, se añade para tema: " + des);
                }
            }

            // verificamos tambien si hay alguna coincidencia por titulo e interprete
            if (cancionParaGrabar == null) {
                cancionParaGrabar = Utilidades.existeCancionTmp(cancion, cancionesBDTmp);
                if (cancionParaGrabar != null && isTemas) {
                    cancionParaGrabar.setSoloTemas(true);
                    System.out.println("Cancion encontrada en BD, se añade para tema : " + des);
                }
            }
            if (cancionParaGrabar == null) {
                boolean existeEnBD = Utilidades.isExisteCancionTmp(cancion, cancionesBD);
                if (existeEnBD && isTemas) {
                    cancionParaGrabar = cancion;
                    cancionParaGrabar.setSoloTemas(true);
                    altaTmp = true;
                    System.out.println("Cancion encontrada en BD, se añade para tema : " + des);
                }
            }

            if (cancionParaGrabar == null) {
                servCancionTmp.saveCancionTmp(cancion);
            } else if (isTemas) {
                if (cancionParaGrabar.isSoloTemas()) {
                    // la cancion exite en BD pero no en la temporal
                    if (altaTmp) {
                        cancionParaGrabar = servCancionTmp.saveCancionTmp(cancion);
                    } else {
                        servCancionTmp.updateCancionTmp(cancionParaGrabar.getId(), cancionParaGrabar);
                    }
                }

                for (Tema tema : cancion.getTematicas()) {
                    cancionParaGrabar.anyadirTematica(tema);
                }
                servCancionTmp.updateTemasCancionTmp(cancionParaGrabar.getId(), cancionParaGrabar);
            }
        }
    }

    public JSONArray obtenerDatosLista(String idPlayList, String token) {

        // {'Authorization': 'Bearer {}'.format(access_token), 'Accept': 'application/json', 'Content-Type': 'application/json'}
        boolean acabar = false;
        int offset = 0;
        HttpResponse<String> response = null;
        JSONArray canciones;
        JSONArray listaCanciones = new JSONArray();
        while (acabar == false) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.spotify.com/v1/playlists/" + idPlayList + "/tracks?offset=" + Integer.toString(offset))) //&limit=1000")) para mas registros
                    .header("Authorization", "Bearer " + token)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            try {
                response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(SpotifyController.class.getName()).log(
                        Level.SEVERE, "Error al procesar lista " + idPlayList, ex);
            }

            canciones = obtenerDatosSpotify(response.body());

            if (!canciones.isEmpty()) {
                listaCanciones.put(canciones);
                offset = offset + 100;
            } else {
                acabar = true;
            }

        }
        return listaCanciones;
    }

    public String procesarLista(String idPlayList, String anyoPlayList, String token) {

        // {'Authorization': 'Bearer {}'.format(access_token), 'Accept': 'application/json', 'Content-Type': 'application/json'}
        boolean acabar = false;
        int offset = 0;
        String err = "";
        HttpResponse<String> response = null;
        while (acabar == false) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.spotify.com/v1/playlists/" + idPlayList + "/tracks?offset=" + Integer.toString(offset))) //&limit=1000")) para mas registros
                    .header("Authorization", "Bearer " + token)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            try {
                response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(SpotifyController.class.getName()).log(Level.SEVERE, null, ex);
            }

            List<CancionTmp> canciones = obtenerDatosJson(response.body(), anyoPlayList);

            Optional<Tema> temaPlayHitsGame = servTema.findBytema("PlayHitsGame");
            if (!canciones.isEmpty()) {
                for (CancionTmp cancion : canciones) {
                    cancion.setAnyo(Integer.valueOf(anyoPlayList));
                    cancion.setRevisar(false);
                    cancion.setTematicas(new ArrayList());
                    if (temaPlayHitsGame.isPresent()) {
                        cancion.anyadirTematica(temaPlayHitsGame.get());
                    }
                }
                grabarListaCanciones(canciones, false);
                offset = offset + 100;
            } else {
                acabar = true;
                if (offset == 0) {
                    err = "ERROR " + idPlayList + "( " + anyoPlayList + " ) : " + response.body();
                }

            }

        }
        return err;

    }

    public String procesarListaTema(String idPlayList, String temaPlayList, String token) {

        // {'Authorization': 'Bearer {}'.format(access_token), 'Accept': 'application/json', 'Content-Type': 'application/json'}
        boolean acabar = false;
        int offset = 0;
        String err = "";
        HttpResponse<String> response = null;
        while (acabar == false) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.spotify.com/v1/playlists/" + idPlayList + "/tracks?offset=" + Integer.toString(offset))) //&limit=1000")) para mas registros
                    .header("Authorization", "Bearer " + token)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            try {
                response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(SpotifyController.class.getName()).log(Level.SEVERE, null, ex);
            }

            List<CancionTmp> canciones = obtenerDatosJson(response.body());

            Tema tema = altaTema(temaPlayList, idPlayList);

            if (!canciones.isEmpty()) {
                for (CancionTmp cancion : canciones) {
                    if (cancion.getTematicas() == null) {
                        cancion.setTematicas(new ArrayList());
                    }
                    cancion.getTematicas().add(tema);
                    cancion.setRevisar(true);
                }
                grabarListaCanciones(canciones, true);
                offset = offset + 100;
            } else {
                acabar = true;
                if (offset == 0) {
                    err = "ERROR " + idPlayList + "( " + temaPlayList + " ) : " + response.body();
                }

            }

        }
        return err;

    }

    private Tema altaTema(String temaPlayList, String idPlayList) {

        Tema unTema;
        Optional<Tema> tema = servTema.findBytema(temaPlayList);

        if (tema.isEmpty()) {
            Tema newTema = new Tema();
            newTema.setTema(temaPlayList);
            newTema.setListasSpotify(idPlayList);
            unTema = servTema.save(newTema);
        } else {
            unTema = tema.get();
        }

        return unTema;

    }

}
