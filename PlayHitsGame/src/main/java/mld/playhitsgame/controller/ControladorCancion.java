/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.controller;

import java.util.List;
import java.util.Optional;
import mld.playhitsgame.exemplars.SearchCriteria;
import mld.playhitsgame.exemplars.SearchSpecifications;
import mld.playhitsgame.exemplars.Cancion;
import mld.playhitsgame.projections.ampliada.CancionAmpliadaView;
import mld.playhitsgame.services.CancionServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
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
@RequestMapping("api/Cancion")
public class ControladorCancion {
    
    @Autowired
    CancionServicio CancionServicio;
    
    @GetMapping("/listaCanciones")
    public List<Cancion> listaCanciones(Model modelo){
        
        SearchSpecifications<SearchCriteria> searchSpecifications =
             new SearchSpecifications();   
        //return CancionServicio.findAll(searchSpecifications);
        return CancionServicio.findAll();
        
    }
    
    @GetMapping("/listaCancionesAmpliada")
    public List<CancionAmpliadaView> listaCancionesAmpliada(){
        
        return CancionServicio.findBy();
        
    }
    
    /*
    @GetMapping("/findCancionById/{id}")
    public Optional<Cancion> findCancionById(@PathVariable Long id){
        
        return CancionServicio.findCancionById(id);
        
    }*/
    
    @GetMapping("/findById/{id}")
    public Optional<Cancion> findById(@PathVariable Long id){
        
        return CancionServicio.findById(id);
        
    }
    
    @PostMapping("/saveCancion")
    public Cancion saveCancion(@RequestBody Cancion cancion){
        return CancionServicio.saveCancion(cancion);
        
    }
    
    
    @PutMapping("/updateCancion/{id}")
    public Cancion saveCancion(@PathVariable Long id, @RequestBody Cancion cancion){
        return CancionServicio.updateCancion(id, cancion);
        
    }
    
    @DeleteMapping("/deleteCancion/{id}")
    public String deleteCancion(@PathVariable Long id){
        CancionServicio.deleteCancion(id);
        return "Ok";
    }
    
            
    
    
}
