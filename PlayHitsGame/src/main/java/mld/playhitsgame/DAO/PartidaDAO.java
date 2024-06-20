/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.DAO;


import java.util.List;
import java.util.Optional;
import mld.playhitsgame.exemplars.Partida;
import mld.playhitsgame.projections.ampliada.UsuarioAmpliadaView;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author miguel
 */

public interface PartidaDAO extends JpaRepository<Partida, Long>{
    
    List<UsuarioAmpliadaView> findBy();
    
    @Override
    Optional<Partida> findById(Long id);
    
    
}