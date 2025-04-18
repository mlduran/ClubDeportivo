/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.DAO;


import java.util.List;
import mld.playhitsgame.exemplars.OpcionTituloTmp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author miguel
 */

public interface OpcionTituloDAO extends JpaRepository<OpcionTituloTmp, Long>{
    
    
    @Query(value = "SELECT * FROM opcionestitulotmp WHERE partida=:partida AND ronda=:ronda ;", nativeQuery = true)
    List<OpcionTituloTmp> findByPartidaRonda(Long partida, Long ronda);
    
    @Modifying   
    @Transactional
    @Query(value = "DELETE FROM opcionestitulotmp WHERE partida=:partida ;", nativeQuery = true)
    void deleteByPartida(Long partida);
    
}