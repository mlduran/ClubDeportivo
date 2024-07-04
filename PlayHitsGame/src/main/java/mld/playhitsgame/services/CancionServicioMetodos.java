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
import mld.playhitsgame.exemplars.SearchCriteria;
import mld.playhitsgame.exemplars.SearchOperation;
import mld.playhitsgame.exemplars.SearchSpecifications;
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
    public List<Cancion> findAll(){
        
        return DAO.findAll();        
        
    }
        
   // @Override
   // public List<Cancion> findAllSpecificaciones(SearchSpecifications<SearchCriteria> searchSpecifications){
   //     return DAO.findAllSpecificaciones(searchSpecifications);           
   //}
    
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
        
        cancionObj.setRevisar(cancion.pendienteRevision());
        
        
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
    
    @Override
    public Cancion cancionAleatoriaPorTema(int anyoInicial, int anyoFinal, String tema) {
                
        return cancionRandom(DAO.findByTema(anyoInicial, anyoFinal, tema));        
        
        
    }
    
    public List<Cancion> buscarCancionesPorCriterios(List<String> generos, List<String> paises, 
            List <String> temas, int anyoInicial, int anyofinal ){
        
        
        SearchSpecifications<SearchCriteria> searchSpecifications = new SearchSpecifications();
        
        if(!generos.isEmpty())
            searchSpecifications.add(new SearchCriteria("genero",generos, SearchOperation.IN));
        if(!paises.isEmpty())
            searchSpecifications.add(new SearchCriteria("pais",paises, SearchOperation.IN));
        if(!temas.isEmpty())
            searchSpecifications.add(new SearchCriteria("tema",temas, SearchOperation.IN));
        searchSpecifications.add(new SearchCriteria("anyo", anyoInicial, SearchOperation.GREATER_THAN_EQUAL));
        searchSpecifications.add(new SearchCriteria("anyo", anyofinal, SearchOperation.LESS_THAN_EQUAL));
         
        //return findAll(searchSpecifications);
        return DAO.findAll();
    }
    
     
    
    
    public void asignarcancionesAleatorias(Partida partida) {
                
       HashMap<Long,Cancion> listaCanciones =  new HashMap();       
       
       // esto es por si no hay suficientes canciones poder cager de toda la BD
       // en este momento ya hemos creado la partida y no podemos volver a atras
       int intentos = 0;
       
       while (listaCanciones.size() < partida.getRondas().size() + 1){
           
           Cancion cancion = null;
           
            if (intentos < partida.getRondas().size() * 3){
                           
                // Si lo hacemos por tema
                if (!partida.getTema().isBlank()){
                    cancion = cancionAleatoriaPorTema(partida.getAnyoInicial(), partida.getAnyoFinal(), partida.getTema());
                }

                // No hay ninguna seleecion ademas del año
                if (partida.getTema().isBlank()){ //aquiponer el resto
                    cancion = cancionAleatoria(partida.getAnyoInicial(), partida.getAnyoFinal());
                }
            
            }else{
                cancion = cancionAleatoria();
            }
            
            if (cancion != null && !cancion.pendienteRevision())
                listaCanciones.put(cancion.getId(), cancion); 
            else 
               intentos = intentos + 1;
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