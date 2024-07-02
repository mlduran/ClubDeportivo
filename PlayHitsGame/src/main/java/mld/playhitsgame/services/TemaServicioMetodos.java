/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.List;
import mld.playhitsgame.DAO.TemaDAO;
import mld.playhitsgame.exemplars.Tema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TemaServicioMetodos implements TemaServicio{
    
    @Autowired
    TemaDAO DAO;

    @Override
    public List<Tema> findAll() {
        return DAO.findAll();
    }
        
   

  
  
 
}