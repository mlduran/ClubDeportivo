/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mld.playhitsgame.exemplars.Partida;
import org.springframework.data.domain.Page;



/**
 *
 * @author miguel
 */
public interface PartidaServicio {
    
    List<Partida> findAll();     
   
    //Optional<Cancion> findCancionById(Long id); //si retorna solo 1 reg, sino poner list
    Optional<Partida> findById(Long id);
    ArrayList<Partida> partidasActuales();
    ArrayList<Partida> partidasFinalizadas();
    ArrayList<Partida> partidasHistoricas();
    
    Partida savePartida(Partida partida);    
    Partida updatePartida(Long id, Partida partida);
    void deletePartida(Long id);
    
    Optional<Partida> partidaUsuarioMaster(Long idUsuario);
    Page<Partida> partidasGrupo(int numeroPagina, int tamanioPagina);
    ArrayList<Partida> partidaBatallaUsuario(Long id_batalla, Long id_usuario);
}
