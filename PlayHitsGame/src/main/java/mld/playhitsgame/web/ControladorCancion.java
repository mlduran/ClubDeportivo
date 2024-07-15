/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.web;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import lombok.extern.slf4j.Slf4j;
import mld.playhitsgame.exemplars.Cancion;
import mld.playhitsgame.exemplars.FiltroCanciones;
import mld.playhitsgame.exemplars.Tema;
import mld.playhitsgame.services.CancionServicioMetodos;
import mld.playhitsgame.services.TemaServicioMetodos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;


@Controller
@SessionAttributes({"filtro"})
@Slf4j
public class ControladorCancion {
    
     
    @Autowired
    CancionServicioMetodos servCancion;
    @Autowired
    TemaServicioMetodos servTema; 

     
    @GetMapping("/listaCanciones")
    public String listaCanciones(Model model){       
        
        List<Cancion> lista = servCancion.findAll();
        
        TreeMap<Integer,Integer> estadistica = new TreeMap();
        for (int i = 1950; i <= 2023; i++) {
           estadistica.put(i, 0);
        }        
        for (Cancion obj: lista){  
            estadistica.put(obj.getAnyo(), estadistica.get(obj.getAnyo()) + 1);
        }            
   
        model.addAttribute("estadistica", estadistica);
        model.addAttribute("lista", lista);       
        return "ListaCanciones";        
    }
    
    @GetMapping("/altaCancion")
    public String altaCancion(Model modelo){       
        
        modelo.addAttribute("newcancion", new Cancion());
        return "AltaCancion";        
    }
    
    @PostMapping("/altaCancion")
    public String altaCancion(@ModelAttribute("newcancion") Cancion cancion,Model modelo){       
        
        String resp = "OK";
        
        try{
            servCancion.saveCancion(cancion);
        }catch(Exception ex){
            resp = "ERROR " + ex; 
        }   
        
        modelo.addAttribute("result", resp);        
        
    
        return "AltaCancion";        
    }
    
    
    @GetMapping("/modificarCancion/{id}")
    public String modificarCancion(@ModelAttribute("id") Long id, Model modelo){       
        
        Optional<Cancion> cancion = servCancion.findById(id);
        temasBD(modelo);
        
        modelo.addAttribute("cancion", cancion);
        return "ModificarCancion";        
    }
    
    @PostMapping("/modificarCancion")
    public String modificarCancion( @ModelAttribute("cancion") Cancion cancion,Model modelo){       
        
        String resp = "OK";
        
        try{
            servCancion.updateCancion(cancion.getId(),cancion);
        }catch(Exception ex){
            resp = "ERROR " + ex; 
        }   
        
        modelo.addAttribute("result", resp); 
        
        return "ModificarCancion";        
    }
    
    @GetMapping("/eliminarCancion/{id}")
    public String eliminarCancion(@ModelAttribute("id") Long id, Model modelo){       
        
        servCancion.deleteCancion(id);   
        return "redirect:/gestionCanciones";        
    }
    
    
    private void temasBD(Model modelo){
        
         ArrayList temas = new ArrayList();
        temas.add("");
        for (Tema tema : servTema.findAll())
            temas.add(tema.getTema());
        modelo.addAttribute("temas", temas);        
        
    }
    
    @GetMapping("/gestionCanciones")
    public String revisionCanciones(Model modelo){  
        
        temasBD(modelo);
        
        FiltroCanciones filtro;
        if (modelo.getAttribute("filtro") == null){  
            filtro = new FiltroCanciones();
            modelo.addAttribute("filtro", filtro);
        }else{
            filtro = (FiltroCanciones) modelo.getAttribute("filtro");
        }
        
        List<Cancion> canciones = servCancion.buscarCancionesPorFiltro(filtro);
        
        modelo.addAttribute("canciones", canciones);
           
        return "GestionCanciones";        
    }
    
    @PostMapping("/gestionCanciones")
    public String revisionCanciones(Model modelo, @ModelAttribute("filtro") FiltroCanciones filtro){  
             
        temasBD(modelo);        
        List<Cancion> canciones = servCancion.buscarCancionesPorFiltro(filtro);
        modelo.addAttribute("canciones", canciones);
        
        return "GestionCanciones";
        
    }
    
    @PostMapping("/marcarRevision")
    public String marcarRevision(Model modelo, HttpServletRequest req){  
             
        FiltroCanciones filtro = (FiltroCanciones) modelo.getAttribute("filtro");
        List<Cancion> canciones = servCancion.buscarCancionesPorFiltro(filtro);
        String a = "";
        for(Cancion cancion: canciones){
            if (req.getParameter(cancion.selId()) != null)
                if ("on".equals(req.getParameter(cancion.selId()))){
                    cancion.setRevisar(true);
                    servCancion.updateCancion(cancion.getId(), cancion);
                }
        }
        
        return "redirect:/gestionCanciones"; 
        
    }
   
    
}
