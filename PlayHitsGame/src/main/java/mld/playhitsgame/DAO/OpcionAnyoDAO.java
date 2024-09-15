/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.DAO;

import java.util.List;
import mld.playhitsgame.exemplars.OpcionAnyoTmp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author miguel
 */
public interface OpcionAnyoDAO extends JpaRepository<OpcionAnyoTmp, Long> {

    @Query(value = "SELECT * FROM opcionesanyotmp WHERE partida=:partida AND ronda=:ronda ;", nativeQuery = true)
    List<OpcionAnyoTmp> findByPartidaRonda(Long partida, Long ronda);
    
    @Modifying   
    @Transactional
    @Query(value = "DELETE FROM opcionesanyotmp WHERE partida=:partida ;", nativeQuery = true)
    void deleteByPartida(Long partida);

}
