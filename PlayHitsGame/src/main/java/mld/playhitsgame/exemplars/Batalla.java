/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.exemplars;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
@Table(name = "batallas")
public class Batalla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private boolean publica;
    private String nombre;

    @Column(name = "fecha", nullable = false)
    @CreationTimestamp
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    private StatusBatalla status;    

    @OneToMany(mappedBy = "batalla", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Partida> partidas;
    
    @ManyToMany(mappedBy = "batallas")
    private List<Usuario> usuarios;
    
    private String tema; 
    @Min(1950)
    private int anyoInicial;
    @Min(1950)
    private int anyoFinal;
    private int nCanciones;
    private int nRondas;
    
    @OneToOne
    private Usuario ganador;
    
    
    public String getCuentaAtras(){
        
        LocalDateTime actual = LocalDateTime.now();
        Duration duration = Duration.between(actual, this.getFecha());
        
        if (actual.isBefore(this.getFecha())) {
            // Convertir la diferencia a horas y minutos
            long hours = duration.toHours();
            long minutes = duration.toMinutes() % 60;

            return hours + "h " + minutes + "m";
        } else {
            return "";
        }
    }
    
    public List<Descripcion> getDescripcion() {

        ArrayList<Descripcion> txt = new ArrayList();
        Descripcion des;       
        if (this.getTema() != null && !this.getTema().isEmpty()) {
            des = new Descripcion();
            des.setEtiqueta("general.tema");
            des.setValor(this.getTema());
            txt.add(des);
        }
        if (this.nCanciones > 0) {
            des = new Descripcion();
            des.setEtiqueta("general.canciones");
            des.setValor(String.valueOf(this.nCanciones));
            txt.add(des);
        }

        des = new Descripcion();
        des.setEtiqueta("general.anyoini");
        des.setValor(String.valueOf(String.valueOf(this.getAnyoInicial())));
        txt.add(des);
        des = new Descripcion();
        des.setEtiqueta("general.anyofin");
        des.setValor(String.valueOf(String.valueOf(this.getAnyoFinal())));
        txt.add(des);

        return txt;
    }
    
    public String getUsuariosTxT() {

        String txt = "";

        boolean primero = true;

        for (Usuario usu : this.getUsuarios()) {
            if (primero) {
                primero = false;
            } else {
                txt = txt.concat(", ");
            }
            txt = txt.concat(usu.getNombre());
        }
        return txt;
    }


}
