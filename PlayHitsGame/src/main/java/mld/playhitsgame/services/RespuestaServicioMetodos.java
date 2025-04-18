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
import org.springframework.transaction.annotation.Transactional;

@Transactional
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
        
        if (obj.getAnyo() == 0 && respuesta.getAnyo() != 0)
            obj.setAnyo(respuesta.getAnyo());
           
        if (obj.getInterprete() == null && respuesta.getInterprete() != null)
            obj.setInterprete(respuesta.getInterprete());
        
        if (obj.getTitulo() == null && respuesta.getTitulo() != null)
            obj.setTitulo(respuesta.getTitulo());
        
        obj.setAnyoOk(respuesta.isAnyoOk());
        obj.setInterpreteOk(respuesta.isInterpreteOk());
        obj.setTituloOk(respuesta.isTituloOk());
        obj.setPuntos(respuesta.getPuntos());
        if (respuesta.getInicio() != null)
            obj.setInicio(respuesta.getInicio());
        if (respuesta.getFin() != null)
            obj.setFin(respuesta.getFin());
        obj.setCompletada(respuesta.isCompletada());
        
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