/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.web;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import mld.playhitsgame.DAO.CancionDAO;
import mld.playhitsgame.exemplars.Cancion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@Slf4j
public class ControladorVista {
    
    @Autowired
    CancionDAO BDCanciones;
    
     
    @GetMapping("/listaCanciones")
    public String listaCanciones(Model model){
        
        List<Cancion> lista = BDCanciones.findAll();
        
        model.addAttribute("lista", lista);
       
        return "ListaCanciones";
        
    }
}
