/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.List;
import java.util.Optional;
import mld.playhitsgame.exemplars.Tema;



/**
 *
 * @author miguel
 */
public interface TemaServicio {
    
    List<Tema> findAll();   
    
    Optional<Tema> findById(Long id);

    Tema update(Long id, Tema tema);
    
}
