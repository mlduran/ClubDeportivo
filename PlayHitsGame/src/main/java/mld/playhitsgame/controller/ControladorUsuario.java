/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.controller;

import java.util.List;
import java.util.Optional;
import mld.playhitsgame.exemplars.Usuario;
import mld.playhitsgame.projections.ampliada.UsuarioAmpliadaView;
import mld.playhitsgame.services.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author miguel
 */
@RestController
@RequestMapping("api/Usuario")
public class ControladorUsuario {
    
    @Autowired
    UsuarioServicio Servicio;
    
    @GetMapping("/listaUsuarios")
    public List<Usuario> listaUsuarios(){
        
        return Servicio.findAll();
        
    }
    
    @GetMapping("/listaUsuariosAmpliada")
    public List<UsuarioAmpliadaView> listaUsuariosAmpliada(){
        
        return Servicio.findBy();
        
    }
    
    
    @GetMapping("/findById/{id}")
    public Optional<Usuario> findById(@PathVariable Long id){
        
        return Servicio.findById(id);
        
    }
    
    @PostMapping("/saveUsuario")
    public Usuario saveUsuario(@RequestBody Usuario usuario){
        return Servicio.saveUsuario(usuario);
        
    }
    
    
    @PutMapping("/updateUsuario/{id}")
    public Usuario saveUsuario(@PathVariable Long id, @RequestBody Usuario usuario){
        return Servicio.updateUsuario(id, usuario);
        
    }
    
    @DeleteMapping("/deleteUsuario/{id}")
    public String deleteUsuario(@PathVariable Long id){
        Servicio.deleteUsuario(id);
        return "Ok";
    }
    
    
}
