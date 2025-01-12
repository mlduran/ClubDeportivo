/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mld.playhitsgame.DAO.BatallaDAO;
import mld.playhitsgame.exemplars.Batalla;
import mld.playhitsgame.exemplars.StatusBatalla;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class BatallaServicioMetodos implements BatallaServicio {

    @Autowired
    BatallaDAO DAO;

    @Override
    public Batalla update(Long id, Batalla batalla) {
        Batalla obj = DAO.findById(id).get();

        if (Objects.nonNull(batalla.getStatus())) {
            obj.setStatus(batalla.getStatus());
        }

        if (Objects.nonNull(batalla.getTema()) && !"".equalsIgnoreCase(batalla.getTema())) {
            obj.setTema(batalla.getTema());
        }

        if (Objects.nonNull(batalla.getNombre()) && !"".equalsIgnoreCase(batalla.getNombre())) {
            obj.setNombre(batalla.getNombre());
        }

        obj.setFase(batalla.getFase());
        obj.setPublica(batalla.isPublica());
        obj.setFecha(batalla.getFecha());
        obj.setAnyoInicial(batalla.getAnyoInicial());
        obj.setAnyoFinal(batalla.getAnyoFinal());
        obj.setNCanciones(batalla.getNCanciones());
        obj.setNRondas(batalla.getNRondas());

        return DAO.save(obj);
    }

    @Override
    public void delete(Batalla batalla) {

        if (batalla.getStatus().equals(StatusBatalla.Programada)) {
            DAO.delete(batalla);
        }
        //DAO.eliminarRelacionRespuestas(id);
        //DAO.eliminarRelacionRondas(id);
        //DAO.eliminarRelacionUsuarios(id);
        //DAO.deleteById(id);
    }

    @Override
    public List<Batalla> findAll() {
        return DAO.findAll();
    }

    @Override
    public Optional<Batalla> findById(Long id) {
        return DAO.findById(id);
    }

    @Override
    public Batalla save(Batalla batalla) {
        return DAO.save(batalla);
    }

    @Override
    public ArrayList<Batalla> BatallasProgramadasPublicas() {
        return DAO.BatallasProgramadasPublicas();
    }

    @Override
    public ArrayList<Batalla> BatallasEnInscripcionPublicas() {
        return DAO.BatallasEnInscripcionPublicas();
    }

    @Override
    public ArrayList<Batalla> BatallasUsuario(Long id_usuario) {
        return DAO.BatallasUsuario(id_usuario);
    }

}
