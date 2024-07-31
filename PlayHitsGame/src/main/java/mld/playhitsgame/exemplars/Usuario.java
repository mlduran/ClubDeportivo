/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.exemplars;

import mld.playhitsgame.seguridad.UsuarioRol;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@Table(name = "usuarios")
public class Usuario{    
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    @Email
    @NotBlank
    private String usuario;
    private boolean activo;
    private String alias;
    @NotBlank
    private String contrasenya;
    private String grupo;
    private String idioma;
    private String preferencias;
    
    @ManyToMany(fetch = FetchType.EAGER, targetEntity = UsuarioRol.class, cascade = CascadeType.PERSIST)
    @JoinTable(name = "usuario_roles", joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private Set<UsuarioRol> roles;
    
    @Temporal(TemporalType.DATE) 
    @Column( nullable = false, updatable = false)
    @CreationTimestamp 
    private Date alta;
    
    @OneToMany(mappedBy = "master")
    private List<Partida> partidasMaster;
    
    @OneToMany(mappedBy = "usuario")
    private List<Respuesta> respuestas;
    
    @ManyToMany
    @JoinTable(
            name = "usuario_partida", 
            joinColumns = @JoinColumn(name="usuario_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="partida_id", referencedColumnName = "id")
    )
    private List<Partida> partidasInvitado;
    
    
    public String getNombre(){
        
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
        
        return this.getNombre() + this.getClass().toString();        
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
        
        return partidaMasterEnCurso() == null;
        
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
    
    public String getTxtGrupo(){
        
        if (this.getGrupo() == null || this.getGrupo().isBlank())
            return "Sin Informar";
        else
            return this.getGrupo();
    }
    

}


