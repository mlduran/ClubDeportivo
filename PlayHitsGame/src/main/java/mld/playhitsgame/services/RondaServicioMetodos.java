/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mld.playhitsgame.DAO.RondaDAO;
import mld.playhitsgame.exemplars.Partida;
import mld.playhitsgame.exemplars.Ronda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RondaServicioMetodos implements RondaServicio{
    
    @Autowired
    RondaDAO DAO;

    @Override
    public List<Ronda> findAll() {
        return DAO.findAll();
    }
        
   
        
    @Override
    public Optional<Ronda> findById(Long id) {
        return DAO.findById(id);
        
    }

    @Override
    public Ronda saveRonda(Ronda ronda) {
        return DAO.save(ronda);
    }

    @Override
    public Ronda updateRonda(Long id, Ronda ronda) {
        Ronda obj = DAO.findById(id).get();
        
        obj.setNumero(ronda.getNumero());
        obj.setIdCancion(ronda.getIdCancion());  
       
        
        return DAO.save(obj);
    }

    @Override
    public void deleteRonda(Long id) {
        DAO.deleteById(id);
    }    

  
  
 
}