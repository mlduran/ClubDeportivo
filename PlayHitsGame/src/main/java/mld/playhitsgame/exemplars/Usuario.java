/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.exemplars;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
    @Column(unique = true)
    private String usuario;
    private String alias;
    private String contrasenya;
    private String grupo;
    private String pais;
    private String preferencias;

    @Temporal(TemporalType.DATE) 
    @Column( nullable = false, updatable = false)
    @CreationTimestamp 
    private Date alta;
    
    @OneToMany(mappedBy = "master", fetch = FetchType.EAGER)
    private List<Partida> partidasMaster;
    
    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
    private List<Respuesta> respuestas;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "usuario_partida", 
            joinColumns = @JoinColumn(name="usuario_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="partida_id", referencedColumnName = "id")
    )
    private List<Partida> partidasInvitado;
    
    
    public String nombre(){
        
        String nombre; 
        
        if (this.getAlias() == null || this.getAlias().isBlank()){
            String[] x = this.getUsuario().split("@");
            nombre = x[0];
        }
        else 
            nombre = this.getAlias();
        
        return nombre;        
    }
    
    public String nombreId(){
        
        return nombre() + this.getClass().toString();        
    }      
    
    
    public Partida partidaMasterPendienteAnyadirJugadores(){
        
        Partida result = null;
        
        for (Partida elem : this.getPartidasMaster()){
            if (elem.getStatus() == StatusPartida.AnyadirJugadores){
                result = elem;
                break;
            }
        }        
        return result;        
    }
    
    public Partida partidaMasterEnCurso(){
        
        Partida result = null;
        
        for (Partida elem : this.getPartidasMaster()){
            if (elem.getStatus() == StatusPartida.EnCurso){
                result = elem;
                break;
            }
        }        
        return result;        
    }
    
    public boolean sePuedeCrearPartidaMaster(){
        
        return partidaMasterEnCurso() == null && 
                partidaMasterPendienteAnyadirJugadores() == null;
        
    }
        
    public List<Partida> partidasInvitadoPendientes(){
        
        List<Partida>  result = new ArrayList<>();
        
        for (Partida elem : this.getPartidasInvitado()){
            if (elem.getStatus() == StatusPartida.EnCurso){
                result.add(elem);
            }
        }        
        return result;        
    }
    
    public boolean hayPartidasInvitadoPendientes(){       
        return !partidasInvitadoPendientes().isEmpty();        
    }
    
    
    public List<Partida> partidasTerminadas(){
        
        List<Partida>  result = new ArrayList<>();
        
        for (Partida elem : this.getPartidasMaster()){
            if (elem.getStatus() == StatusPartida.Terminada){
                result.add(elem);
            }
        }        
        
        for (Partida elem : this.getPartidasInvitado()){
            if (elem.getStatus() == StatusPartida.Terminada){
                result.add(elem);
            }
        }        
        return result;        
    }
    
    public boolean hayPartidasTerminadas(){
        
        return !partidasTerminadas().isEmpty();      
        
    }
    

}


