/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.DAO;


import java.util.List;
import mld.playhitsgame.exemplars.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author miguel
 */

public interface RegistroDAO extends JpaRepository<Registro, Long>{
    
    List<Registro> findBy();    
 
    @Modifying   
    @Transactional
    @Query(value = "DELETE FROM registro WHERE tipo=:tipo ;", nativeQuery = true)
    void deleteByTipo(String tipo);
    
}