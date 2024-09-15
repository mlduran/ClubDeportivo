/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.DAO;


import java.util.List;
import mld.playhitsgame.exemplars.OpcionInterpreteTmp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author miguel
 */

public interface OpcionInterpreteDAO extends JpaRepository<OpcionInterpreteTmp, Long>{
    
    
    @Query(value = "SELECT * FROM opcionesinterpretetmp WHERE partida=:partida AND ronda=:ronda  ;", nativeQuery = true)
    List<OpcionInterpreteTmp> findByPartidaRonda(Long partida, Long ronda);
    
    @Modifying   
    @Transactional
    @Query(value = "DELETE FROM opcionesinterpretetmp WHERE partida=:partida  ;", nativeQuery = true)
    void deleteByPartida(Long partida);
}