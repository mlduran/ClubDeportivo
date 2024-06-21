/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mld.playhitsgame.DAO.CancionDAO;
import mld.playhitsgame.exemplars.Cancion;
import mld.playhitsgame.projections.ampliada.CancionAmpliadaView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CancionServicioMetodos implements CancionServicio{
    
    @Autowired
    CancionDAO DAO;

    @Override
    public List<Cancion> findAll() {
        return DAO.findAll();
    }

    
    @Override
    public List<CancionAmpliadaView> findBy() {
        return DAO.findBy();
        
    }

    /*@Override
    public Optional<Cancion> findCancionById(Long id) {
        return DAO.findCancionById(id);
    }*/
    
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
        if(Objects.nonNull(cancion.getTitulo()) && !"".equalsIgnoreCase(cancion.getTitulo())){
            cancionObj.setTitulo(cancion.getTitulo());
        }
        
        if(Objects.nonNull(cancion.getInterprete()) && !"".equalsIgnoreCase(cancion.getInterprete())){
            cancionObj.setInterprete(cancion.getInterprete());
        }
        
        if(Objects.nonNull(cancion.getGenero())){
            cancionObj.setGenero(cancion.getGenero());
        }
        
        if(Objects.nonNull(cancion.getAnyo()) && !"".equalsIgnoreCase(cancion.getAnyo())){
            cancionObj.setAnyo(cancion.getAnyo());
        }
        
        if(Objects.nonNull(cancion.getPais())){
            cancionObj.setPais(cancion.getPais());
        }
        
        if(Objects.nonNull(cancion.getSpotifyid()) && !"".equalsIgnoreCase(cancion.getSpotifyid())){
            cancionObj.setSpotifyid(cancion.getSpotifyid());
        }
        
        
        return DAO.save(cancionObj);
    }

    @Override
    public void deleteCancion(Long id) {
        DAO.deleteById(id);
    }

    @Override
    public Cancion cancionAleatoria() {
                
        List<Cancion> lista = DAO.findAll();
        
        int i;  
        i = (int) (Math.floor(Math.random() * lista.size()));
        
        return lista.get(i);
        
    }
    
 
}