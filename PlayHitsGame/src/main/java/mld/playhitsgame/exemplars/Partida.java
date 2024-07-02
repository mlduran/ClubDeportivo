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
    
    
    @ManyToMany(mappedBy = "partidasInvitado", fetch = FetchType.EAGER) 
    private List<Usuario> invitados;
    
    @OneToMany(mappedBy = "partida", fetch = FetchType.EAGER) // poner LAZY para no cargar hasta hacer un get 
    private List<Ronda> rondas;
    
  
    private int rondaActual;
    private Genero genero;
    private Pais pais;
    private String tema; // por ejemplo Descripciones genericas
    private int anyoInicial;
    private int anyoFinal;
    private String grupo;
    private String ganador;
    
    public String getDescripcion(){
        
        String txt = "";
        
        if (this.getGrupo() != null && !this.getGrupo().isEmpty())
            txt = txt.concat("Grupo: ").concat(this.getGrupo() + "\n");
        if (this.getTema()!= null && !this.getTema().isEmpty())
            txt = txt.concat("Tema: ").concat(this.getTema()+ "\n");
        if (this.getGenero()!= null && this.getGenero() != null)
            txt = txt.concat("Genero: ").concat(this.getGenero().toString() + "\n");
        if (this.getPais()!= null && this.getPais()!= null)
            txt = txt.concat("Pais: ").concat(this.getPais().toString() + "\n");
        
        txt = txt.concat("Año inicial: " + String.valueOf(this.getAnyoInicial()) + "\n");
        txt = txt.concat("Año final: " + String.valueOf(this.getAnyoFinal()));
        
        
        return txt;
        
    }
    
    
    public Ronda rondaActiva(){
        
        return this.rondas.get(this.rondaActual - 1);        
        
    }
    
    public List<Usuario> usuariosPartida(){
        
        ArrayList<Usuario> lista = new ArrayList();
        
        lista.add(this.getMaster());
        if (!this.getInvitados().isEmpty())
            lista.addAll(this.getInvitados());
        
        return lista;       
        
    }
    
    public void pasarSiguienteRonda(){
        this.setRondaActual(this.getRondaActual() + 1);
    }
    
    public boolean hayMasRondas(){
        return this.getRondaActual() < this.getRondas().size();
    }
    
    public int ptsUsuario(Usuario usuario){
        
        int pts = 0;
        
        for (Ronda ronda : this.getRondas()){
            for (Respuesta resp : ronda.getRespuestas()){
                if (Objects.equals(resp.getUsuario().getId(), usuario.getId()))
                    pts = pts + resp.getPuntos();
            }
        }
        return pts;
    }
    
    public void asignarGanador(){
        
        String ganadorPartida = "";
        int ptsGanador = 0;
        
        for (Usuario usuario : this.usuariosPartida()){
            int ptsUsu = ptsUsuario(usuario);
            if(ptsUsu > ptsGanador){
                ganadorPartida = usuario.nombre();
                ptsGanador = ptsUsu;
            }
        }
        
        this.setGanador(ganadorPartida);
        
    }
    
    
}


