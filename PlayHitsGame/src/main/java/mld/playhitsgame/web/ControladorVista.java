/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.web;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import mld.playhitsgame.exemplars.Cancion;
import mld.playhitsgame.exemplars.Partida;
import mld.playhitsgame.exemplars.Ronda;
import mld.playhitsgame.exemplars.Usuario;
import mld.playhitsgame.services.CancionServicioMetodos;
import mld.playhitsgame.services.PartidaServicioMetodos;
import mld.playhitsgame.services.RondaServicioMetodos;
import mld.playhitsgame.services.UsuarioServicioMetodos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;


@Controller
@SessionAttributes({"usuarioSesion"})
@Slf4j
public class ControladorVista {
    
     
    @Autowired
    CancionServicioMetodos servCancion;
    @Autowired
    UsuarioServicioMetodos servUsuario;
    @Autowired
    PartidaServicioMetodos servPartida;
    @Autowired
    RondaServicioMetodos servRonda;
    
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
    
    @GetMapping("/partida/crear")
    public String crearPartida(Model modelo){     
        
        
        Partida newPartida = new Partida();
        newPartida.setAnyoInicial(1950);
        newPartida.setAnyoFinal(2023);
        modelo.addAttribute("newpartida", new Partida());       
        
        return "CrearPartida";
        
    }
    
    @PostMapping("/partida/crear")
    public String crearPartida(@ModelAttribute("newpartida") Partida partida, 
            @ModelAttribute("nrondas") Integer nrondas, Model modelo){     
        
   
        String resp = "OK";
        
        Usuario usu = (Usuario) modelo.getAttribute("usuarioSesion");

        try{
            partida.setMaster(usu);
            Partida newPartida = servPartida.savePartida(partida);
            usu.getPartidasMaster().add(newPartida);
            servUsuario.updateUsuario(usu.getId(), usu);            
            
            //crear las rondas con nrondas
            partida.setRondas(new ArrayList<Ronda>());
            for (int i = 1; i <= nrondas; i++){
                Ronda obj = new Ronda();
                obj.setNumero(i);
                obj.setPartida(partida); 
                Ronda ronda = servRonda.saveRonda(obj);
                partida.getRondas().add(ronda);
                
            }
           servPartida.updatePartida(partida.getId(), partida);           
            
            
            
            //asignar usuarios
            
            
        }catch(Exception ex){
            resp = "ERROR " + ex; 
        }   
        
        modelo.addAttribute("result", resp);  
        
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
