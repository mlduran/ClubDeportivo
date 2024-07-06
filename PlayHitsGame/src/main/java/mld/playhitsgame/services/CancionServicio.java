/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.List;
import java.util.Optional;
import mld.playhitsgame.exemplars.SearchCriteria;
import mld.playhitsgame.exemplars.SearchSpecifications;
import mld.playhitsgame.exemplars.Cancion;
import mld.playhitsgame.projections.ampliada.CancionAmpliadaView;


/**
 *
 * @author miguel
 */
public interface CancionServicio {
    
    List<Cancion> findAll();
    //List<Cancion> findAllSpecificaciones(SearchSpecifications<SearchCriteria> searchSpecifications);   
  
    List<CancionAmpliadaView> findBy();
    
    //Optional<Cancion> findCancionById(Long id); //si retorna solo 1 reg, sino poner list
    Optional<Cancion> findById(Long id);
    
    Cancion saveCancion(Cancion cancion);    
    Cancion updateCancion(Long id, Cancion cancion);
    void deleteCancion(Long id);
    


    
}
