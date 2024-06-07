/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.DAO;


import java.util.List;
import java.util.Optional;
import mld.playhitsgame.exemplars.Cancion;
import mld.playhitsgame.projections.ampliada.CancionAmpliadaView;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author miguel
 */
//@Transactional(readOnly = true)
public interface CancionDAO extends JpaRepository<Cancion, Long>{
    
    List<CancionAmpliadaView> findBy();
    
    //@Query("SELECT * FROM cancion WHERE id = :id")
    //Optional<Cancion> findCancionById(long id);
    
    @Override
    Optional<Cancion> findById(Long id);
    
    
}