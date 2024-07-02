/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mld.playhitsgame.DAO.CancionDAO;
import mld.playhitsgame.exemplars.Cancion;
import mld.playhitsgame.exemplars.Partida;
import mld.playhitsgame.exemplars.Ronda;
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
        
        if(Objects.nonNull(cancion.getAlbum()) && !"".equalsIgnoreCase(cancion.getAlbum())){
            cancionObj.setAlbum(cancion.getAlbum());
        }
        
        if(Objects.nonNull(cancion.getTema()) && !"".equalsIgnoreCase(cancion.getTema())){
            cancionObj.setTema(cancion.getTema());
        }
        
        if(Objects.nonNull(cancion.getGenero())){
            cancionObj.setGenero(cancion.getGenero());
        }
        
        cancionObj.setAnyo(cancion.getAnyo());
        
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
    
    private Cancion cancionRandom(List<Cancion> lista){
        int i;  
        i = (int) (Math.floor(Math.random() * lista.size()));
        
        return lista.get(i);        
    }

    @Override
    public Cancion cancionAleatoria() {
                
        return cancionRandom(DAO.findAll());
        
      
    }
    
    @Override
    public Cancion cancionAleatoria(int anyoInicial, int anyoFinal) {
                
        return cancionRandom(DAO.findByAnyo(anyoInicial, anyoFinal));        
        
        
    }
    
    
    public void asignarcancionesAleatorias(Partida partida) {
                
       HashMap<Long,Cancion> listaCanciones =  new HashMap();
       
       while (listaCanciones.size() < partida.getRondas().size() + 1){
           Cancion cancion = cancionAleatoria(partida.getAnyoInicial(), partida.getAnyoFinal());
           listaCanciones.put(cancion.getId(), cancion);           
       }
           
       ArrayList<Cancion> lista = new ArrayList();
       for (HashMap.Entry<Long, Cancion> elem : listaCanciones.entrySet()){
           lista.add(elem.getValue());
       }
       
       int i = 0;
       for (Ronda ronda : partida.getRondas()){
           ronda.setCancion(lista.get(i));
           i = i + 1;
       }
       
        
    }
    
    
    
    
    
 
}