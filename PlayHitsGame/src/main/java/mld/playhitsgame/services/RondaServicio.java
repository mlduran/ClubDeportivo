/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.List;
import java.util.Optional;
import mld.playhitsgame.exemplars.Ronda;



/**
 *
 * @author miguel
 */
public interface RondaServicio {
    
    List<Ronda> findAll();     
   
    //Optional<Cancion> findCancionById(Long id); //si retorna solo 1 reg, sino poner list
    Optional<Ronda> findById(Long id);
    
    Ronda saveRonda(Ronda ronda);    
    Ronda updateRonda(Long id, Ronda ronda);
    void deleteRonda(Long id);
    
    
}
