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
@Table(name = "partidas")
public class Partida{
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Temporal(TemporalType.DATE) 
    @Column(name = "fecha", nullable = false, updatable = false)
    @CreationTimestamp 
    private Date fecha;
    
    @Enumerated(EnumType.STRING)
    private StatusPartida status;
    
    
    @ManyToOne(fetch = FetchType.EAGER)// poner LAZY para no cargar hasta hacer un get
    private Usuario master;
    
    
    //@ManyToMany(fetch = FetchType.EAGER) // poner LAZY para no cargar hasta hacer un get 
    //@JoinTable(
    //        name = "invitados_usuarios", 
    //        joinColumns = @JoinColumn(name="idInvitado"),
    //        inverseJoinColumns = @JoinColumn(name="idUsuario")
    //)
    //private List<Usuario> invitados;
    
    @OneToMany(mappedBy = "partida", fetch = FetchType.EAGER) // poner LAZY para no cargar hasta hacer un get 
    private List<Ronda> rondas;
    
  
  
    private Genero genero;
    private Pais pais;
    private int anyoInicial;
    private int anyoFinal;
    private String grupo;
    private String ganador;
    
    

    
    
}


