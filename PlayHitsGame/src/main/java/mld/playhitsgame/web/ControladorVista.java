/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.web;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import mld.playhitsgame.exemplars.Cancion;
import mld.playhitsgame.exemplars.Usuario;
import mld.playhitsgame.services.CancionServicioMetodos;
import mld.playhitsgame.services.UsuarioServicioMetodos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;


@Controller
@SessionAttributes({"usuLogin"})
@Slf4j
public class ControladorVista {
    
     
    @Autowired
    CancionServicioMetodos servCancion;
    @Autowired
    UsuarioServicioMetodos servUsuario;
    
    
    @GetMapping("/panel")
    public String panel(Model modelo){
        
        return "Panel";
        
    }
    
 
    @GetMapping("/altaUsuario")
    public String altaUsuario(Model modelo){
        
        modelo.addAttribute("newusuario", new Usuario());
        return "AltaUsuario";
        
    }
    
    @PostMapping("/altaUsuario")
    public String altaUsuario(@ModelAttribute("newusuario") Usuario usuario, Model modelo){
        
        String resp = "OK";
        
        try{
            servUsuario.saveUsuario(usuario);
        }catch(Exception ex){
            resp = "ERROR " + ex; 
        }   
        
        modelo.addAttribute("result", resp);        
        
        return "AltaUsuario";
        
    }
    
    @GetMapping("/crearPartida")
    public String crearPartida(Model modelo){        
        
        
        return "CrearPartida";
        
    }
    
    @GetMapping("/partida")
    public String partida(Model modelo){
        
        Cancion cancionAleatoria = servCancion.cancionAleatoria();
        
        modelo.addAttribute("cancion", cancionAleatoria);
        
        return "Partida";
        
    }
    
     
    @GetMapping("/listaCanciones")
    public String listaCanciones(Model model){
        
        List<Cancion> lista = servCancion.findAll();
        
        model.addAttribute("lista", lista);
       
        return "ListaCanciones";
        
    }
}
