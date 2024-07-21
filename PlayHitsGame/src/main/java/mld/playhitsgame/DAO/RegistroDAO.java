/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.DAO;


import java.util.List;
import mld.playhitsgame.exemplars.Registro;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author miguel
 */

public interface RegistroDAO extends JpaRepository<Registro, Long>{
    
    List<Registro> findBy();    
 
    
}