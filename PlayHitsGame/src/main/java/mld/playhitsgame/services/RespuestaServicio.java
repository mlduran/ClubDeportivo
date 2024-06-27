/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.List;
import java.util.Optional;
import mld.playhitsgame.exemplars.Respuesta;



/**
 *
 * @author miguel
 */
public interface RespuestaServicio {
    
    List<Respuesta> findAll();     
   
    //Optional<Cancion> findCancionById(Long id); //si retorna solo 1 reg, sino poner list
    Optional<Respuesta> findById(Long id);
    
    Respuesta buscarPorRondaUsuario(Long idronda, Long idusuario);
    
    Respuesta saveRespuesta(Respuesta respuesta);    
    Respuesta updateRespuesta(Long id, Respuesta respuesta);
    void deleteRespuesta(Long id);
    
    
}
