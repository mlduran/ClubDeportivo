/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import mld.playhitsgame.DAO.CancionTmpDAO;
import mld.playhitsgame.exemplars.SearchCriteria;
import mld.playhitsgame.exemplars.SearchOperation;
import mld.playhitsgame.exemplars.SearchSpecifications;
import mld.playhitsgame.exemplars.CancionTmp;
import mld.playhitsgame.exemplars.FiltroCanciones;
import mld.playhitsgame.exemplars.Tema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CancionTmpServicioMetodos implements CancionTmpServicio {

    @Autowired
    CancionTmpDAO DAO;

    @Override
    public List<CancionTmp> findAll() {

        return DAO.findAll();

    }

    @Override
    public Optional<CancionTmp> findByIdSpotify(String Id) {
        return DAO.findByIdSpotify(Id);

    }

    @Override
    public Optional<CancionTmp> findById(Long id) {
        return DAO.findById(id);

    }

    @Override
    public CancionTmp saveCancionTmp(CancionTmp cancion) {
        return DAO.save(cancion);
    }

    @Override
    public CancionTmp updateCancionTmp(Long id, CancionTmp cancion) {
        CancionTmp cancionObj = DAO.findById(id).get();
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

        cancionObj.setRevisar(cancion.isRevisar());
        cancionObj.setSoloTemas(cancion.isSoloTemas());

        return DAO.save(cancionObj);
    }

    @Override
    public CancionTmp updateTemasCancionTmp(Long id, CancionTmp cancion) {

        CancionTmp cancionObj = DAO.findById(id).get();
        cancionObj.getTematicas().addAll(cancion.getTematicas());
        // Eliminamos posibles duplicados
        Set<Tema> hashSet = new HashSet(cancionObj.getTematicas());
        cancionObj.getTematicas().clear();
        cancionObj.getTematicas().addAll(hashSet);

        return DAO.save(cancionObj);
    }

    @Override
    public void deleteCancionTmp(Long id) {
        DAO.eliminarRelacionTemas(id);
        DAO.deleteById(id);
    }

    public List<CancionTmp> buscarCancionesPorCriterios(List<String> generos, List<String> idiomas,
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

    public List<CancionTmp> buscarCancionesPorFiltro(FiltroCanciones filtro) {

        if (!"".equals(filtro.getTema())) {
            return DAO.findByFiltroConTema(filtro.getTema(), filtro.getAnyoInicial(), filtro.getAnyoFinal(), filtro.isRevisar(), filtro.isSoloTemas());
        } else {
            return DAO.findByFiltroBasico(filtro.getAnyoInicial(), filtro.getAnyoFinal(), filtro.isRevisar(), filtro.isSoloTemas());
        }

    }    
    
}
