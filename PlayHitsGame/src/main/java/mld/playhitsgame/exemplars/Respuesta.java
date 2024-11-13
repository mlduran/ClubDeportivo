/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.exemplars;

import jakarta.persistence.*;
import java.time.Duration;
import java.time.LocalTime;
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
@Table(name = "respuestas")
public class Respuesta{
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private int anyo;
    private boolean anyoOk;
    private String titulo;
    private boolean tituloOk;
    private String interprete;
    private boolean interpreteOk;
    private int puntos;
    private LocalTime inicio;
    
    @ManyToOne(fetch=FetchType.EAGER)
    private Ronda ronda;
    
    @ManyToOne
    private Usuario usuario;


    public String getPuntosTxt(){
        return " ( ".concat(String.valueOf(puntos) + " )");        
    }    
    
    public String getTiempoEmpleado(){
        
        String tiempo = "";
        
        if (this.getInicio() != null &&
                this.getRonda() != null &&
                this.getRonda().getInicio() != null
                ){
            Duration duration = Duration.between(this.getRonda().getInicio(), this.getInicio());
            Long segundos = duration.getSeconds();
            tiempo = segundos.toString(); 
        }        
        
        return tiempo;
    }
    
    
    
}


