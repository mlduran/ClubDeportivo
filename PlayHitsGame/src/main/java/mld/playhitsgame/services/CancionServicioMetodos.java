/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import mld.playhitsgame.DAO.CancionDAO;
import mld.playhitsgame.exemplars.Batalla;
import mld.playhitsgame.exemplars.SearchCriteria;
import mld.playhitsgame.exemplars.SearchOperation;
import mld.playhitsgame.exemplars.SearchSpecifications;
import mld.playhitsgame.exemplars.Cancion;
import mld.playhitsgame.exemplars.FiltroCanciones;
import mld.playhitsgame.exemplars.Partida;
import mld.playhitsgame.exemplars.Tema;
import mld.playhitsgame.projections.ampliada.CancionAmpliadaView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CancionServicioMetodos implements CancionServicio {

    @Autowired
    CancionDAO DAO;

    @Override
    public List<Cancion> findAll() {

        return DAO.findAll();

    }

    public long numRegs() {

        return DAO.count();
    }

    // @Override
    // public List<Cancion> findAllSpecificaciones(SearchSpecifications<SearchCriteria> searchSpecifications){
    //     return DAO.findAllSpecificaciones(searchSpecifications);           
    //}
    @Override
    public List<CancionAmpliadaView> findBy() {
        return DAO.findBy();

    }

    @Override
    public Optional<Cancion> findByIdSpotify(String Id) {
        return DAO.findByIdSpotify(Id);

    }

    @Override
    public Optional<Cancion> findById(Long id) {
        return DAO.findById(id);

    }

    @Override
    public Cancion saveCancion(Cancion cancion) {
        return DAO.save(cancion);
    }

    @Override
    public Cancion updateCancion(Long id, Cancion cancion) {
        Cancion cancionObj = DAO.findById(id).get();
        if (Objects.nonNull(cancion.getTitulo()) && !"".equalsIgnoreCase(cancion.getTitulo())) {
            cancionObj.setTitulo(cancion.getTitulo());
        }

        if (Objects.nonNull(cancion.getInterprete()) && !"".equalsIgnoreCase(cancion.getInterprete())) {
            cancionObj.setInterprete(cancion.getInterprete());
        }

        if (Objects.nonNull(cancion.getAlbum()) && !"".equalsIgnoreCase(cancion.getAlbum())) {
            cancionObj.setAlbum(cancion.getAlbum());
        }

        cancionObj.setAnyo(cancion.getAnyo());

        if (Objects.nonNull(cancion.getSpotifyid()) && !"".equalsIgnoreCase(cancion.getSpotifyid())) {
            cancionObj.setSpotifyid(cancion.getSpotifyid());
        }

        if (Objects.nonNull(cancion.getSpotifyplay()) && !"".equalsIgnoreCase(cancion.getSpotifyplay())) {
            cancionObj.setSpotifyplay(cancion.getSpotifyplay());
        }

        if (Objects.nonNull(cancion.getSpotifyimagen()) && !"".equalsIgnoreCase(cancion.getSpotifyimagen())) {
            cancionObj.setSpotifyimagen(cancion.getSpotifyimagen());
        }

        cancionObj.setRevisar(cancion.pendienteRevision());

        return DAO.save(cancionObj);
    }

    @Override
    public Cancion updateTemasCancion(Long id, Cancion cancion) {

        Cancion cancionObj = DAO.findById(id).get();
        cancionObj.getTematicas().addAll(cancion.getTematicas());
        // Eliminamos posibles duplicados
        Set<Tema> hashSet = new HashSet(cancionObj.getTematicas());
        cancionObj.getTematicas().clear();
        cancionObj.getTematicas().addAll(hashSet);

        return DAO.save(cancionObj);
    }

    @Override
    public void deleteCancion(Long id) {
        DAO.eliminarRelacionTemas(id);
        DAO.eliminarRelacionRondas(id);
        DAO.deleteById(id);
    }

    public List<Cancion> buscarCancionesPorCriterios_borrar(List<String> generos, List<String> idiomas,
            List<String> temas, int anyoInicial, int anyofinal) {

        SearchSpecifications searchSpecifications = new SearchSpecifications();

        if (!generos.isEmpty()) {
            searchSpecifications.add(new SearchCriteria("genero", generos, SearchOperation.IN));
        }
        if (!idiomas.isEmpty()) {
            searchSpecifications.add(new SearchCriteria("idioma", idiomas, SearchOperation.IN));
        }
        if (!temas.isEmpty()) {
            searchSpecifications.add(new SearchCriteria("tema", temas, SearchOperation.IN));
        }
        searchSpecifications.add(new SearchCriteria("anyo", anyoInicial, SearchOperation.GREATER_THAN_EQUAL));
        searchSpecifications.add(new SearchCriteria("anyo", anyofinal, SearchOperation.LESS_THAN_EQUAL));

        //return findAll(searchSpecifications);
        return DAO.findAll();
    }

    public List<Cancion> buscarCancionesPorFiltro(FiltroCanciones filtro) {

        if (!"".equals(filtro.getTema())) {
            String[] temas = filtro.getTema().split(",");
            Set<Cancion> cancionesTema = new HashSet<>();
            for (String tema : temas) {
                cancionesTema.addAll( DAO.findByFiltroConTema(tema.trim(), filtro.getAnyoInicial(), filtro.getAnyoFinal(), filtro.isRevisar()));
            }
            return new ArrayList<>(cancionesTema);
        } else {
            return DAO.findByFiltroBasico(filtro.getAnyoInicial(), filtro.getAnyoFinal(), filtro.isRevisar());
        }

    }

    public List<Cancion> obtenerCanciones(Partida partida) {

        List<Cancion> canciones;

        if (!partida.getTema().isBlank()) {
            String[] temas = partida.getTema().split(",");
            Set<Cancion> cancionesTema = new HashSet<>();
            for (String tema : temas) {
                canciones = DAO.findByFiltroConTema(tema.trim(), partida.getAnyoInicial(),
                        partida.getAnyoFinal(), false);
                cancionesTema.addAll(canciones);
            }
            canciones = new ArrayList<>(cancionesTema);
        } else {
            canciones = DAO.findByFiltroBasico(partida.getAnyoInicial(),
                    partida.getAnyoFinal(), false);
        }

        return canciones;
    }

    public List<Cancion> obtenerCanciones(Batalla batalla) {

        List<Cancion> canciones;

        if (!batalla.getTema().isBlank()) {
            String[] temas = batalla.getTema().split(",");
            Set<Cancion> cancionesTema = new HashSet<>();
            for (String tema : temas) {
                canciones = DAO.findByFiltroConTema(tema.trim(), batalla.getAnyoInicial(),
                        batalla.getAnyoFinal(), false);
                cancionesTema.addAll(canciones);
            }
            canciones = new ArrayList<>(cancionesTema);
        } else {
            canciones = DAO.findByFiltroBasico(batalla.getAnyoInicial(),
                    batalla.getAnyoFinal(), false);
        }

        return canciones;
    }

}
