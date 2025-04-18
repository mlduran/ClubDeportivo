/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.List;
import mld.playhitsgame.DAO.PuntuacionDAO;
import mld.playhitsgame.exemplars.Puntuacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PuntuacionServicioMetodos implements PuntuacionServicio {

    @Autowired
    PuntuacionDAO DAO;   

    @Override
    public List<Puntuacion> obtenerPuntuacionesPersonales(Long tema_id) {
        return DAO.obtenerPuntuacionesPersonales(tema_id);
    }

    @Override
    public Puntuacion save(Puntuacion puntuacion) {
        return DAO.save(puntuacion);
    }
    
    @Override
    public List<Puntuacion> obtenerPuntuacionesUsuario(Long tema_id, Long id_usuario){
        
        return DAO.obtenerPuntuacionesUsuario(tema_id, id_usuario);        
    }

   

}
