/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.DAO;

import java.util.ArrayList;
import java.util.Optional;
import mld.playhitsgame.exemplars.Partida;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author miguel
 */
public interface PartidaDAO extends JpaRepository<Partida, Long> {

    @Override
    Optional<Partida> findById(Long id);
    
    @Query(value = "SELECT * FROM partidas WHERE status <> 'Terminada' AND status <> 'Historico' ;", nativeQuery = true)
    ArrayList<Partida> partidasActuales();
    
    @Query(value = "SELECT * FROM partidas WHERE status = 'Terminada' ;", nativeQuery = true)
    ArrayList<Partida> partidasFinalizadas();

    @Query(value = "SELECT * FROM partidas WHERE master_id=:idusuario AND (status <> 'Terminada' AND status <> 'Historico') ;", nativeQuery = true)
    Optional<Partida> partidaUsuarioMaster(Long idusuario);
    
    @Query(value = "SELECT * FROM partidas WHERE tipo='grupo' AND  status <> 'Historico' ORDER BY fecha DESC ;", nativeQuery = true)
    ArrayList<Partida> partidasGrupo();
    
    @Query(value = "SELECT * FROM partidas WHERE id_batalla=:id_batalla AND master_id=:id_usuario ;", nativeQuery = true)
    ArrayList<Partida> partidaBatallaUsuario(Long id_batalla, Long id_usuario);
    
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM respuestas WHERE ronda_id IN (SELECT id FROM rondas WHERE partida_id=:idPartida);", nativeQuery = true)
    void eliminarRelacionRespuestas(Long idPartida);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM rondas WHERE partida_id=:idPartida ;", nativeQuery = true)
    void eliminarRelacionRondas(Long idPartida);
    
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM usuario_partida WHERE partida_id=:idPartida ;", nativeQuery = true)
    void eliminarRelacionUsuarios(Long idPartida);

}
