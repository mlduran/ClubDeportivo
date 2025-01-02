/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.services;

import java.time.LocalDateTime;
import mld.playhitsgame.DAO.EstrellaDAO;
import mld.playhitsgame.exemplars.Estrella;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class EstrellaServicioMetodos implements EstrellaServicio {

    @Autowired
    EstrellaDAO DAO;

    @Override
    public Estrella update(Long id, Estrella estrella) {

        Estrella obj = DAO.findById(id).get();

        obj.setFecha(estrella.getFecha());
        obj.setUsuario(estrella.getUsuario());

        return DAO.save(obj);
    }

    @Override
    public Estrella save(Estrella estrella) {
        return DAO.save(estrella);
    }

    public Estrella darEstrella(Long idUsuario, int maxEstrellas) {

        Estrella newEstrella;

        if (DAO.count() >= maxEstrellas) {
            newEstrella = DAO.fechaMasAntigua();
        } else {
            newEstrella = new Estrella();
        }
        newEstrella.setFecha(LocalDateTime.now());
        newEstrella.setUsuario(idUsuario);
        return DAO.save(newEstrella);
    }

}
