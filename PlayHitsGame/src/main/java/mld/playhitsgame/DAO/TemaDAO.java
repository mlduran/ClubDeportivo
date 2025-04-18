/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.DAO;


import java.util.List;
import java.util.Optional;
import mld.playhitsgame.exemplars.Tema;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author miguel
 */

public interface TemaDAO extends JpaRepository<Tema, Long>{
    
    List<Tema> findBy();  
    Optional<Tema> findBytema(String tema);
    @Override
    Optional<Tema> findById(Long id);
    @Override
    void delete(Tema tema);

 
    
}