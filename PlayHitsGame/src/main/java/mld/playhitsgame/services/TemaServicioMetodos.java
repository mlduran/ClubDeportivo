/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mld.playhitsgame.DAO.TemaDAO;
import mld.playhitsgame.exemplars.Cancion;
import mld.playhitsgame.exemplars.Tema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TemaServicioMetodos implements TemaServicio {

    @Autowired
    TemaDAO DAO;

    @Override
    public List<Tema> findAll() {
        return DAO.findAll();
    }
    
    @Override
    public Optional<Tema>findById(Long id){
        
        return DAO.findById(id);
    }

    @Override
    public Tema update(Long id, Tema tema) {
        
        Tema obj = DAO.findById(id).get();
        
        if (Objects.nonNull(tema.getDescripcion()) && !"".equalsIgnoreCase(tema.getDescripcion())) {
            obj.setDescripcion(tema.getDescripcion());
        }

        if (Objects.nonNull(tema.getIdioma())) {
            obj.setIdioma(tema.getIdioma());
        }

        if (Objects.nonNull(tema.getGenero())) {
            obj.setGenero(tema.getGenero());
        }

        return DAO.save(obj);
    }

}
