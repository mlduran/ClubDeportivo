/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.DAO;


import java.util.Optional;
import mld.playhitsgame.exemplars.Respuesta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author miguel
 */

public interface RespuestaDAO extends JpaRepository<Respuesta, Long>{
    
     
    @Override
    Optional<Respuesta> findById(Long id);
    
    @Query(value = "SELECT * FROM respuestas WHERE ronda_id=:idronda AND usuario_id=:idusuario ;", nativeQuery=true)
    Respuesta buscarPorRondaUsuario(Long idronda, Long idusuario);
    
}