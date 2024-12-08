/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.List;
import java.util.Optional;
import mld.playhitsgame.exemplars.CancionTmp;


/**
 *
 * @author miguel
 */
public interface CancionTmpServicio {
    
    List<CancionTmp> findAll();
  
    Optional<CancionTmp> findById(Long id);
    
    Optional<CancionTmp> findByIdSpotify(String id);
    
    CancionTmp saveCancionTmp(CancionTmp cancion);    
    CancionTmp updateCancionTmp(Long id, CancionTmp cancion);
    CancionTmp updateTemasCancionTmp(Long id, CancionTmp cancion);
    void deleteCancionTmp(Long id);
    
}
