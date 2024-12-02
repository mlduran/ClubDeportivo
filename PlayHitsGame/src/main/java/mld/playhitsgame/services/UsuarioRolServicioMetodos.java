/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.Optional;
import mld.playhitsgame.DAO.UsuarioRolDAO;
import mld.playhitsgame.seguridad.UsuarioRol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UsuarioRolServicioMetodos implements UsuarioRolServicio{
    
    @Autowired
    UsuarioRolDAO DAO;

    @Override
    public Optional<UsuarioRol> findById(Long id) {
        return DAO.findById(id);
    }

    @Override
    public UsuarioRol save(UsuarioRol usuarioRol) {
        return DAO.save(usuarioRol);
    } 

    @Override
    public void deleteById(Long id) {
        DAO.deleteById(id);
    }
   

  
  
 
}