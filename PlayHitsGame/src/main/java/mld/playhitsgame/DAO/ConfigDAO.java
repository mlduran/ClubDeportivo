/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.DAO;


import mld.playhitsgame.exemplars.Config;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author miguel
 */

public interface ConfigDAO extends JpaRepository<Config, Long>{
    
 
    
}