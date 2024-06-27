/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.List;
import java.util.Optional;
import mld.playhitsgame.DAO.RespuestaDAO;
import mld.playhitsgame.exemplars.Respuesta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RespuestaServicioMetodos implements RespuestaServicio{
    
    @Autowired
    RespuestaDAO DAO;

    @Override
    public List<Respuesta> findAll() {
        return DAO.findAll();
    }
        
   
        
    @Override
    public Optional<Respuesta> findById(Long id) {
        return DAO.findById(id);
        
    }

    @Override
    public Respuesta saveRespuesta(Respuesta respuesta) {
        return DAO.save(respuesta);
    }

    @Override
    public Respuesta updateRespuesta(Long id, Respuesta respuesta) {
        Respuesta obj = DAO.findById(id).get();
        
        obj.setAnyo(respuesta.getAnyo());
        obj.setPuntos(respuesta.getPuntos());
       
        
        return DAO.save(obj);
    }

    @Override
    public void deleteRespuesta(Long id) {
        DAO.deleteById(id);
    }    

    @Override
    public Respuesta buscarPorRondaUsuario(Long idronda, Long idusuario) {
        return DAO.buscarPorRondaUsuario(idronda, idusuario);
    }

  
  
 
}