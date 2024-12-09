/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.DAO;

import java.util.List;
import mld.playhitsgame.exemplars.Puntuacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author miguel
 */
public interface PuntuacionDAO extends JpaRepository<Puntuacion, Long> {

    @Override
    Puntuacion save(Puntuacion puntuacion); 
    
    @Query(value = "SELECT * FROM puntuaciones WHERE tipo_partida=1 AND tema_id=:tema_id ORDER BY puntos DESC LIMIT 50; ", nativeQuery = true)
    List<Puntuacion> obtenerPuntuacionesPersonales(Long tema_id);
    
    @Query(value = "SELECT * FROM puntuaciones WHERE tipo_partida=1 AND tema_id=:tema_id AND id_usuario=:id_usuario; ", nativeQuery = true)
    List<Puntuacion> obtenerPuntuacionesUsuario(Long tema_id, Long id_usuario);

}
