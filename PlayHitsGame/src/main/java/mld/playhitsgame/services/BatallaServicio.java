/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mld.playhitsgame.exemplars.Batalla;



/**
 *
 * @author miguel
 */
public interface BatallaServicio {
    
    List<Batalla> findAll();     
   
    Optional<Batalla> findById(Long id);
    ArrayList<Batalla> batallasActuales();
    ArrayList<Batalla> batallasFinalizadas();
    ArrayList<Batalla> batallasHistoricas();
    
    Batalla save(Batalla batalla);    
    Batalla update(Long id, Batalla batalla);
    void delete(Batalla batalla);
    void deleteBatalla(Batalla batalla);
    
    ArrayList<Batalla> batallasProgramadasPublicas();
    ArrayList<Batalla> batallasEnInscripcionPublicas();
    ArrayList<Batalla> batallasUsuario(Long id_usuario);

}
