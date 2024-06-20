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
    @JoinColumn(name = "idMaster")
    private Usuario master;
    
    
    @ManyToMany(fetch = FetchType.EAGER) // poner LAZY para no cargar hasta hacer un get 
    @JoinTable(
            name = "invitados_usuarios", 
            joinColumns = @JoinColumn(name="idInvitado"),
            inverseJoinColumns = @JoinColumn(name="idUsuario")
    )
    private List<Usuario> invitados;
    
    @ManyToMany(fetch = FetchType.LAZY) // poner LAZY para no cargar hasta hacer un get 
    @JoinTable(
            name = "partidas_canciones", 
            joinColumns = @JoinColumn(name="id_cancion"),
            inverseJoinColumns = @JoinColumn(name="id_usuario")
    )
    private List<Cancion> canciones;
    
    //@OneToOne(mappedBy="partida", cascade = CascadeType.ALL)
    //PartidaPuntos puntos;
    
    private int numeroRondas;
    private Genero genero;
    private String pais;
    private String grupo;
    private String ganador;
    
    

    
    
}


