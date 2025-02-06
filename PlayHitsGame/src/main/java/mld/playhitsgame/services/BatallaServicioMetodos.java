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
import mld.playhitsgame.exemplars.Partida;
import mld.playhitsgame.exemplars.StatusBatalla;
import mld.playhitsgame.exemplars.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class BatallaServicioMetodos implements BatallaServicio {

    @Autowired
    BatallaDAO DAO;
    @Autowired
    PartidaServicioMetodos servPartida;
    @Autowired
    UsuarioServicioMetodos servUsuario;

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
    }
    
    @Override
    public void deleteBatalla(Batalla batalla) {

        for (Partida partida : batalla.getPartidas()){
            servPartida.deletePartida(partida.getId());
        }
        
        for (Usuario usuario : batalla.getUsuariosInscritos()) {
            usuario.getBatallasInscritas().remove(batalla);
            servUsuario.update(usuario.getId(), usuario); 
        }
        
        for (Usuario usuario : batalla.getUsuarios()) {
            usuario.getBatallas().remove(batalla);
            servUsuario.update(usuario.getId(), usuario); 
        }
        
        DAO.delete(batalla);            
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
    public ArrayList<Batalla> batallasProgramadasPublicas() {
        return DAO.batallasProgramadasPublicas();
    }

    @Override
    public ArrayList<Batalla> batallasEnInscripcionPublicas() {
        return DAO.batallasEnInscripcionPublicas();
    }

    @Override
    public ArrayList<Batalla> batallasUsuario(Long id_usuario) {
        return DAO.batallasUsuario(id_usuario);
    }

    @Override
    public ArrayList<Batalla> batallasActuales() {
        return DAO.batallasActuales();
    }

    @Override
    public ArrayList<Batalla> batallasFinalizadas() {
        return DAO.batallasFinalizadas();
    }
    
    @Override
    public ArrayList<Batalla> batallasHistoricas() {
        return DAO.batallasHistoricas();
    }

}
