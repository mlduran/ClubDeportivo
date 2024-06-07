/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.DAO;


import java.util.List;
import java.util.Optional;
import mld.playhitsgame.exemplars.Usuario;
import mld.playhitsgame.projections.ampliada.UsuarioAmpliadaView;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author miguel
 */

public interface UsuarioDAO extends JpaRepository<Usuario, Long>{
    
    List<UsuarioAmpliadaView> findBy();
    
    //@Query("SELECT * FROM cancion WHERE id = :id")
    //Optional<Cancion> findCancionById(long id);
    
    @Override
    Optional<Usuario> findById(Long id);
    
    
}