/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mld.playhitsgame.DAO.TemaDAO;
import mld.playhitsgame.exemplars.Tema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TemaServicioMetodos implements TemaServicio {

    @Autowired
    TemaDAO DAO;

    @Override
    public List<Tema> findAll() {
        return DAO.findAll();
    }
    
    @Override
    public List<Tema> findAllPorPartidas() {
        return DAO.findAll(Sort.by(Sort.Direction.DESC, "nPartidas"));
    }
    
    @Override
    public Optional<Tema>findById(Long id){
        
        return DAO.findById(id);
    }
    
    @Override
    public Optional<Tema> findBytema(String tema){
        return DAO.findBytema(tema);
    }

    @Override
    public Tema update(Long id, Tema tema) {
        
        Tema obj = DAO.findById(id).get();
        
        if (Objects.nonNull(tema.getIdioma())) {
            obj.setIdioma(tema.getIdioma());
        }

        if (Objects.nonNull(tema.getGenero())) {
            obj.setGenero(tema.getGenero());
        }
        
        if (Objects.nonNull(tema.getListasSpotify()))
                obj.setListasSpotify(tema.getListasSpotify());
                
        obj.setPuntos(tema.getPuntos());
        obj.setRecordUsuario(tema.getRecordUsuario());
        obj.setActivo(tema.isActivo());
        obj.setNPartidas(tema.getNPartidas());        

        return DAO.save(obj);
    }

    @Override
    public void delete(Tema tema) {
        DAO.delete(tema);
    }

    @Override
    public Tema save(Tema tema) {
        return DAO.save(tema);        
    }
}
