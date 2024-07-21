/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.exemplars;

import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 *
 * @author miguel
 */


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "canciones")
public class Cancion{
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String interprete;
    private String album;
    private Integer anyo;
    @Enumerated(EnumType.STRING)
    private Genero genero;
    @Enumerated(EnumType.STRING)
    private Idioma idioma;
    private String tema; // por ejemplo Numeros 1 40 principales
    @Column(unique = true)
    private String spotifyid;
    private String spotifyplay;
    private String spotifyimagen;
    @OneToMany
    private List<Ronda> rondas ;
    private boolean revisar;
    
    public String selId(){
        
        return "sel_" + String.valueOf(this.getId());
    }
    
    public boolean pendienteRevision(){
        
        return this.isRevisar();
        
    }
    
    public String txtRevision(){
        
        if (this.isRevisar())
            return "SI";
        else 
            return "NO";
        
    }
     
    
}


