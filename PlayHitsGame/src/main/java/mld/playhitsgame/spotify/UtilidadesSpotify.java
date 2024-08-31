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
import mld.playhitsgame.DAO.CancionDAO;
import mld.playhitsgame.DAO.TemaDAO;
import mld.playhitsgame.exemplars.Cancion;
import mld.playhitsgame.exemplars.Tema;
import mld.playhitsgame.services.CancionServicioMetodos;
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
    TemaDAO DAOtema;

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
                cancion.append("titulo", track.getString("name"));
                cancion.append("interprete", grupo.getString("name"));
                cancion.append("anyo", anyo);
                cancion.append("album", elAlbum);
                cancion.append("imagen", laImagen);
                String urlPlay = track.getString("preview_url");
                cancion.append("preview_url", urlPlay);

                lista.put(cancion);

            } catch (JSONException ex) {
                System.out.println("Error al obtener cancion json: " + ex);
            }

        }

        return lista;

    }

    public List<Cancion> obtenerDatosJson(String datos, String anyo) {

        List<Cancion> lista = new LinkedList<>();
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

                Cancion cancion = new Cancion();

                JSONObject elemJson = (JSONObject) elem;
                track = elemJson.getJSONObject("track");
                artists = track.getJSONArray("artists");
                album = track.getJSONObject("album");
                imagenes = album.getJSONArray("images");
                grupo = artists.getJSONObject(0);

                cancion.setTitulo((String) track.getString("name"));
                cancion.setInterprete((String) grupo.getString("name"));
                cancion.setAnyo(Integer.getInteger(anyo));
                cancion.setSpotifyid((String) track.getString("id"));
                cancion.setSpotifyplay((String) track.getString("preview_url"));
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

    public List<Cancion> obtenerDatosJson(String datos) {

        List<Cancion> lista = new LinkedList<>();
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

                Cancion cancion = new Cancion();

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

                cancion.setTitulo((String) track.getString("name"));
                cancion.setInterprete((String) grupo.getString("name"));
                cancion.setSpotifyid((String) track.getString("id"));
                cancion.setSpotifyplay((String) track.getString("preview_url"));

                lista.add(cancion);
            } catch (JSONException ex) {
                System.out.println("Error al obtener cancion json: " + ex);
            }

        }

        return lista;

    }

    public void grabarListaCanciones(List<Cancion> lista) {

        for (Cancion cancion : lista) {
            Optional<Cancion> cancionBD = servCancion.findByIdSpotify(cancion.getSpotifyid());
            if (cancionBD.isEmpty()) {
                servCancion.saveCancion(cancion);
            } else {
                servCancion.updateTemasCancion(cancionBD.get().getId(), cancion);
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

            List<Cancion> canciones = obtenerDatosJson(response.body(), anyoPlayList);

            if (!canciones.isEmpty()) {
                grabarListaCanciones(canciones);
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

            List<Cancion> canciones = obtenerDatosJson(response.body());

            Tema tema = altaTema(temaPlayList);

            if (!canciones.isEmpty()) {
                for (Cancion cancion : canciones) {
                    if (cancion.getTematicas() == null)
                        cancion.setTematicas(new ArrayList());
                    cancion.getTematicas().add(tema);
                    cancion.setRevisar(true);
                }
                grabarListaCanciones(canciones);
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

    private Tema altaTema(String temaPlayList) {

        Tema unTema;
        Optional<Tema> tema = DAOtema.findBytema(temaPlayList);

        if (tema.isEmpty()) {
            Tema newTema = new Tema();
            newTema.setTema(temaPlayList);
            unTema = DAOtema.save(newTema);
        } else {
            unTema = tema.get();
        }

        return unTema;

    }

}
