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
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.EAGER )
    private List<Tema> tematicas;
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
    
    public String getTematicasTxt(){
        
        String txt = "";
        boolean primero = true;
        for (Tema elTema : this.getTematicas()){
            if (primero)
                primero = false;
            else
                txt = txt.concat(", ");
            txt = txt.concat(elTema.getTema());
        }
        
        return txt;
    }
    
    public void eliminarTematica(Tema tema){
        
        Tema elim = null;
        for (Tema elTema: this.getTematicas()){
            if (elTema.equals(tema))
                elim = elTema;
        }
        if (elim != null)
            this.getTematicas().remove(elim);
    }
    
    public void anyadirTematica(Tema tema){
        
        for (Tema elTema: this.getTematicas()){
            if (elTema.equals(tema))
                return;
        }
        this.getTematicas().add(tema);
    }
    
    public boolean isTieneTemas(List<Tema> temas){
        // Devuelve si la cancion tiene icluidos todos los
        // temas de la lista
        
        return this.tematicas.containsAll(temas);        
    }
     
    
}


