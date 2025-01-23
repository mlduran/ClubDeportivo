/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mld.playhitsgame.DAO.PartidaDAO;
import mld.playhitsgame.exemplars.Partida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PartidaServicioMetodos implements PartidaServicio{
    
    @Autowired
    PartidaDAO DAO;

    @Override
    public List<Partida> findAll() {
        return DAO.findAll();
    }   
        
    @Override
    public Optional<Partida> findById(Long id) {
        return DAO.findById(id);        
    }

    @Override
    public Partida savePartida(Partida partida) {
        return DAO.save(partida);
    }

    @Override
    public Partida updatePartida(Long id, Partida partida) {
        Partida obj = DAO.findById(id).get();
        
        if(Objects.nonNull(partida.getStatus())){
            obj.setStatus(partida.getStatus());
        }

        if(Objects.nonNull(partida.getTipo())){
            obj.setTipo(partida.getTipo());
        }
        
        if(Objects.nonNull(partida.getDificultad())){
            obj.setDificultad(partida.getDificultad());
        } 
        
        if(Objects.nonNull(partida.getTema()) && !"".equalsIgnoreCase(partida.getTema())){
            obj.setTema(partida.getTema());
        }        

        if(Objects.nonNull(partida.getGrupo())){
            obj.setGrupo(partida.getGrupo());
        }
        
        if(Objects.nonNull(partida.getGanador()) && !"".equalsIgnoreCase(partida.getGanador())){
            obj.setGanador(partida.getGanador());
        }
        
        if(Objects.nonNull(partida.getNombre()) && !"".equalsIgnoreCase(partida.getNombre())){
            obj.setNombre(partida.getNombre());
        }
        
        obj.setFase(partida.getFase());
        obj.setPublica(partida.isPublica());
        obj.setFecha(partida.getFecha());
        obj.setAnyoInicial(partida.getAnyoInicial());
        obj.setAnyoFinal(partida.getAnyoFinal());
        obj.setNCanciones(partida.getNCanciones());
        obj.setRondaActual(partida.getRondaActual());
        obj.setActivarPlay(partida.isActivarPlay());
        obj.setSinOfuscar(partida.isSinOfuscar());
        obj.setSonidos(partida.isSonidos());
        
        return DAO.save(obj);
    }

    @Override
    public void deletePartida(Long id) {
        DAO.eliminarRelacionRespuestas(id);
        DAO.eliminarRelacionRondas(id);
        DAO.eliminarRelacionUsuarios(id);
        DAO.deleteById(id);
    }    

    @Override
    public Optional<Partida> partidaUsuarioMaster(Long idUsuario) {
        return DAO.partidaUsuarioMaster(idUsuario);
    }
    
    @Override
    public ArrayList<Partida> partidaBatallaUsuario(Long id_batalla, Long id_usuario){
        
        return DAO.partidaBatallaUsuario(id_batalla, id_usuario);
    }

    @Override
    public Page<Partida> partidasGrupo(int numeroPagina, int tamanioPagina){
        
        ArrayList<Partida> partidas = DAO.partidasGrupo();
        
        // Calcula los índices para paginación manual
        int start = numeroPagina * tamanioPagina;
        int end = Math.min(start + tamanioPagina, partidas.size());

        // Verifica si los índices están fuera de rango
        List<Partida> partidasPaginadas = start > end ? new ArrayList<>() : partidas.subList(start, end);

        // Retorna un objeto Page creado a partir de los usuarios paginados
        return new PageImpl<>(partidasPaginadas, Pageable.ofSize(tamanioPagina).withPage(numeroPagina), partidasPaginadas.size());
   
    }

    @Override
    public ArrayList<Partida> partidasActuales() {
        return DAO.partidasActuales();
    }

    @Override
    public ArrayList<Partida> partidasFinalizadas() {
        return DAO.partidasFinalizadas();
    }

 
 
}