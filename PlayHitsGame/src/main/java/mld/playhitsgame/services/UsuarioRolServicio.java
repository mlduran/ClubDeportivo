/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.Optional;
import mld.playhitsgame.seguridad.UsuarioRol;



/**
 *
 * @author miguel
 */
public interface UsuarioRolServicio {
    
    Optional<UsuarioRol> findById(Long id);
    
    UsuarioRol save(UsuarioRol usuarioRol);
    void deleteById(Long id); 
    

    
}
