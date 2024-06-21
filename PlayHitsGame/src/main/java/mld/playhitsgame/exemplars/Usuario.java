/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.exemplars;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;



/**
 *
 * @author miguel
 */


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuarios")
public class Usuario{
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String usuario;
    private String alias;
    private String contrasenya;
    private String grupo;
    private String pais;
    private String preferencias;
    @Temporal(TemporalType.DATE) 
    @Column(name = "alta", nullable = false, updatable = false)
    @CreationTimestamp 
    private Date alta;
    
    @OneToMany(mappedBy = "master")
    private List<Partida> partidasMaster;
    
    //@ManyToMany(mappedBy = "invitados")
    //private List<Partida> partidasInvitado;
    
    
    public boolean hayPartidasMaster(){
        
        return !this.getPartidasMaster().isEmpty();
        
    }
    
    public boolean hayPartidas(){
        
        return false;
        //return !this.getPartidasMaster().isEmpty() ||
        //        !this.getPartidasInvitado().isEmpty();
        
    }
    

}


